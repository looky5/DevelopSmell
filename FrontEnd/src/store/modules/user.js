import { USER_REQUEST, USER_ERROR, USER_SUCCESS } from "../actions/user";
import axios from "axios";
import Vue from "vue";
import { AUTH_LOGOUT } from "../actions/auth";
import Swal from 'sweetalert2'

const state = {
  status: "",
  uid: localStorage.getItem("uid") || "",
  userno: localStorage.getItem("user-login") || "0",
  isLogin: localStorage.getItem("isLogin") || "",
  profile: localStorage.getItem("profile") || "noimage.jpg",
  isAdmin: localStorage.getItem("isAdmin") || false,
  token: localStorage.getItem("token") || ""
};

const getters = {
  getProfile: state => state.profilename,
  isProfileLoaded: state => !!state.profilename
};

const actions = {
  [USER_REQUEST]: ({ commit, dispatch }, userno) => {
    return new Promise((resolve, reject) => {
      axios({
        methos: "GET",
        // url: "http://i3b208.p.ssafy.io:8080/blog/account/get/" + userno
        url: "http://localhost:8080/blog/account/get/" + userno
      })
        .then(resp => {
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: '로그인에 성공했습니다.',
            showConfirmButton: false,
            timer: 1500
          })
          commit(USER_SUCCESS, resp);
          resolve();
        })
        .catch(async () => {
          Swal.fire({
            icon: 'error',
            title: '로그인 실패',
            text: '로그인에 실패했습니다.',
          })
          commit(USER_ERROR);
          await dispatch(AUTH_LOGOUT);
          reject();
        });
    });
  }
};

const mutations = {
  [USER_REQUEST]: state => {
    state.status = "loading";
  },
  [USER_SUCCESS]: (state, resp) => {
    state.status = "success";
    localStorage.setItem("user-login", resp.data.object.userno);
    localStorage.setItem("token", resp.data.object.token);
    localStorage.setItem("uid", resp.data.object.uid);
    localStorage.setItem("isLogin", true);
    Vue.set(state, "userno", resp.data.object.userno);
    Vue.set(state, "token", resp.data.object.token);
    Vue.set(state, "uid", resp.data.object.uid);
    Vue.set(state, "isLogin", true);
    if (resp.data.object.isadmin == 1) {
      localStorage.setItem("isAdmin", true);
      Vue.set(state, "isAdmin", true);
    } else {
      localStorage.setItem("isAdmin", false);
      Vue.set(state, "isAdmin", false);
    }
  },
  [USER_ERROR]: state => {
    state.status = "error";
  },
  [AUTH_LOGOUT]: state => {
    state.profilename = "";
  }
};

export default {
  state,
  getters,
  actions,
  mutations,
  Swal
};
