import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import { BootstrapVue, IconsPlugin } from "bootstrap-vue";

// Install BootstrapVue
Vue.use(BootstrapVue);
// Optionally install the BootstrapVue icon components plugin
Vue.use(IconsPlugin);

Vue.config.productionTip = false;
// Vue.prototype.$baseurl = "http://i3b208.p.ssafy.io:8080/blog";
Vue.prototype.$baseurl = "http://localhost:8080/blog";
new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount("#app");
