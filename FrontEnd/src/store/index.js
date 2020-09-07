import Vue from "vue";
import Vuex from "vuex";
import userm from "./modules/user";
import authm from "./modules/auth";

Vue.use(Vuex);

export default new Vuex.Store({
  mutations: {},
  state: {
    search: {
      tags: [],
      word: "",
      isSearch: false
    },
    location: {
      isMyblog: false
    }
  },
  actions: {},
  modules: { userm, authm }
});
