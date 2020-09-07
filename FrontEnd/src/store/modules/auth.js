import {
  AUTH_REQUEST,
  AUTH_ERROR,
  AUTH_SUCCESS,
  AUTH_LOGOUT
} from "../actions/auth";
import { USER_REQUEST } from "../actions/user";
import axios from "axios";
import Swal from 'sweetalert2'

const state = {
  userlogin: localStorage.getItem("user-login") || "",
  status: "",
  hasLoadedOnce: false
};

const getters = {
  isAuthenticated: state => !!state.userlogin,
  authStatus: state => state.status
};
//   {accessToken: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNTcwM…XYOy7KZva2LCwHnDixyQHAIX1E6K3EzmA9WSMNJklkIVthjyQ", tokenType: "Bearer"}

const actions = {
  [AUTH_REQUEST]: ({ commit, dispatch }, user) => {
    return new Promise((resolve, reject) => {
      commit(AUTH_REQUEST);
      axios({
        method: "POST",
        //  url: "http://i3b208.p.ssafy.io:8080/blog/account/login",
        url: "http://localhost:8080/blog/account/login",
        data: {
          email: user.email,
          password: user.password
        }
      })
        .then(async resp => {
          let data = resp.data.object;
          localStorage.setItem("user-login", data);
          commit(AUTH_SUCCESS, resp);
          await dispatch(USER_REQUEST, data);
          resolve(resp.data.object);
        })
        .catch(err => {
          Swal.fire({
            icon: 'error',
            title: '로그인 실패',
            text: '이메일과 패스워드를 확인해주세요.',
          })
          commit(AUTH_ERROR, err);
          localStorage.removeItem("user-login");
          reject(err);
        });
    });
  },
  [AUTH_LOGOUT]: ({ commit }) => {
    return new Promise(resolve => {
      commit(AUTH_LOGOUT);
      localStorage.clear();
      resolve();
    });
  }
};

const mutations = {
  [AUTH_REQUEST]: state => {
    state.status = "loading";
  },
  [AUTH_SUCCESS]: state => {
    state.status = "success";
    // state.userlogin = resp.data.object.userno;
    state.hasLoadedOnce = true;
  },
  [AUTH_ERROR]: state => {
    state.status = "error";
    state.hasLoadedOnce = true;
  },
  [AUTH_LOGOUT]: state => {
    state.userlogin = "";
  }
};

export default {
  state,
  getters,
  actions,
  mutations,
  Swal
};
