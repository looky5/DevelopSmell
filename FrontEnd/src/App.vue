<template>
  <v-app>
    <v-main id="main_con">
      <header-bar id="head"></header-bar>
      <router-view></router-view>
    </v-main>
  </v-app>
</template>

<script>
import HeaderBar from "./components/Layout/HeaderBar.vue";

export default {
  name: "App",

  components: {
    HeaderBar
  },
  mounted() {
    var lastScrollTop = 0;
    window.addEventListener(
      "scroll",
      function() {
        var st = window.pageYOffset || document.documentElement.scrollTop;
        if (st > lastScrollTop) {
          //down
          this.document.getElementById("head").style.display = "none";
        } else {
          this.document.getElementById("head").style.display = "";
        }
        lastScrollTop = st <= 0 ? 0 : st; // For Mobile or negative scrolling
      },
      false
    );
  },

  data: () => ({
    //
  })
};
</script>
<style scoped>
#main_con {
  width: 1440px;
  margin: 0 auto;
}
@media (min-width: 576px) {
  #main_con {
    width: 480px;
  }
}
@media (min-width: 768px) {
  #main_con {
    width: 720px;
  }
}
@media (min-width: 992px) {
  #main_con {
    width: 930px;
  }
}
@media (min-width: 1200px) {
  #main_con {
    width: 1080px;
  }
}
@media (min-width: 1440px) {
  #main_con {
    width: 1360px;
  }
}

</style>
