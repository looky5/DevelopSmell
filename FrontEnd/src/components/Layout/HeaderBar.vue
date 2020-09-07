<template>
  <div>
    <div>
    <v-app-bar id="headerbar" class="marginauto" dense fixed light flat :color="color">
      <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->
      <v-img @click="home" id="logo" src="../../assets/Developsmell.png" />

      <v-tabs align-with-title color="rgb(240, 162, 46)">
        <!-- <v-tab class="pa-0 ma-0" style="min-width:0px" /> -->
        <v-tab class="tabs" @click="home">Home</v-tab>
        <v-tab class="tabs" @click="userrank()">USERRANK</v-tab>
        <v-tab class="tabs" @click="latest()">LATEST</v-tab>
        <v-tab class="tabs" @click="report()" v-if="isAdmin == 'true'">REPORT</v-tab>
      </v-tabs>


      <v-spacer></v-spacer>

      <v-btn @click="openSearchbar(isSearch)" icon>
        <v-icon>mdi-magnify</v-icon>
      </v-btn>

      <login v-if="!isLogin"></login>
     <v-menu class="notlogin" left bottom v-if="!isLogin">
        <template v-slot:activator="{ on, attrs }">
          <v-btn class="notlogin" icon v-bind="attrs" v-on="on">
            <v-icon>mdi-menu</v-icon>
          </v-btn>
        </template>
        <v-list>
          <v-list-item class="tabslist" @click="userrank">
            <v-list-item-icon>
              <v-icon>mdi-trophy</v-icon>
            </v-list-item-icon>

        <v-list-item-content>
          <v-list-item-title>USERRANK</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <v-list-item class="tabslist" @click="latest">
        <v-list-item-icon>
          <v-icon>mdi-new-box</v-icon>
        </v-list-item-icon>

        <v-list-item-content>
          <v-list-item-title>LATEST</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list>
  </v-menu>

      <v-menu left bottom v-if="isLogin">
        <template v-slot:activator="{ on, attrs }">
          <v-btn icon v-bind="attrs" v-on="on">
            <v-icon>mdi-account</v-icon>
          </v-btn>
        </template>

        <v-list>
          <v-list-item @click="myInformation">
            <v-list-item-icon>
              <v-icon>mdi-account-edit</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>Information</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

      
          <v-list-item class="" @click="myBlog">
            <v-list-item-icon>
              <v-icon>mdi-home-city</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>My blog</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          
           <v-list-item @click="postregist">
            <v-list-item-icon>
              <v-icon>mdi-fountain-pen-tip</v-icon>
            </v-list-item-icon>
            
            <v-list-item-content>
              <v-list-item-title>Write Post</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item class="" @click="myLikepost">
            <v-list-item-icon>
              <v-icon>mdi-heart-box</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>My LikePost</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-divider></v-divider>
          <v-list-item class="tabslist" @click="userrank">
        <v-list-item-icon>
          <v-icon>mdi-trophy</v-icon>
        </v-list-item-icon>

        <v-list-item-content>
          <v-list-item-title>USERRANK</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <v-list-item class="tabslist" @click="latest">
        <v-list-item-icon>
          <v-icon>mdi-new-box</v-icon>
        </v-list-item-icon>

        <v-list-item-content>
          <v-list-item-title>LATEST</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <v-list-item class="tabslist" @click="report">
        <v-list-item-icon>
          <v-icon>mdi-bell</v-icon>
        </v-list-item-icon>

        <v-list-item-content>
          <v-list-item-title>REPORT</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <v-divider class="tabslist"></v-divider>

          <v-list-item @click="logout">
            <v-list-item-icon class="mr-4">
              <v-icon>mdi-logout</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>Logout</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-app-bar>
  </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import { AUTH_LOGOUT } from "../../store/actions/auth";
import login from "@/components/User/Login.vue";
import axios from "axios";
export default {
  data: () => {
    return {
      color: "#FFFFFF",
      word: "",
      tags: [],
      tagflag: 0,
      limit: 0
    };
  },
  created(){
    console.log("header : "+this.$store.state.search.isSearch)
  },
  components: {
    login
  },
  computed: {
    ...mapState({
      isLogin: state => state.userm.isLogin,
      isSearch: state => !!state.search.isSearch,
      isAdmin: state => state.userm.isAdmin,
      uid: state => state.userm.uid
    })
  },
  methods: {
       postregist(){
      this.$router.push("/post/regist");
    },
    myLikepost() {
      if(this.$route.name !== "likepage"){
        this.$router.push("/post/like");
      }
      else{
        this.$router.go();
      }
    },
    home() {
      this.$store.state.search.tags = "";
      this.$store.state.search.word = "";
      if (this.$route.name !== "MainPage") {
        this.$router.push("/");
      } else {
        this.$router.go();
      }
    },
    userrank() {
      if (this.$route.name !== "rankpage") {
        this.$router.push("/rank");
      } else {
        this.$router.go();
      }
    },
    report() {
      if (this.$route.name !== "reportpage") {
        this.$router.push("/report");
      } else {
        this.$router.go();
      }
    },
    latest() {
      if (this.$route.name !== "latestpage") {
        this.$router.push("/latest");
      } else {
        this.$router.go();
      }
    },
    myInformation() {
      if(this.$route.name !== "usermodify"){
        this.$router.push("/modify");
      }
      else{
        this.$router.go();
      }
    },
    myBlog() {
       if(this.$route.name !== "blog"){
        this.$router.push("/myblog/" + this.$store.state.userm.uid);
       }
       else{
          this.$router.go();
       }
    },

    searchBtn() {
      let url =
        this.$baseurl +
        "/post/search/" +
        this.limit +
        "/" +
        this.$store.state.userm.userno;
      url += "?word=" + this.word + "&";

      for (let i = 0; i < this.tags.length; i++) {
        url += "tags=" + this.tags[i];
        if (i < this.tags.length - 1) {
          url += "&";
        }
      }

      axios({
        method: "GET",
        url: url
      }).then(() => {
        console.log("search");
      });
    },
    async logout() {
      await this.$store.dispatch(AUTH_LOGOUT);
      await this.$router
        .push("/")
        .then(() => {
          location.reload();
        })
        .catch(() => {
          location.reload();
        });
    },
    openSearchbar(isSearch) {
      console.log(this.$route.name)
      if(this.$route.name !== "MainPage"){
        this.$store.state.search.isSearch = true;
        this.$router.push("/");
        // this.$store.state.search.isSearch = !isSearch;
      }
      else{
        if (isSearch) {
          this.$store.state.search.isSearch = false;
        } else {
          this.$store.state.search.isSearch = true;
        }
      }
    }
  }
};
</script>
<style scoped>
.marginauto {
  margin: 0 auto;
}
#headerbar {
  /* position: inherit; */
  /* left: 10px; */
}
@media (max-width: 768px) {
  .tabs {
    display: none;
  }
  .tabslist {
    display: flex;
  }
  .notlogin {
    display: flex;
  }
}
@media (min-width: 768px) {
  .tabs {
    display: flex;
  }
  .tabslist {
    display: none;
  }
  .notlogin {
    display: none;
  }
}

@media (min-width: 576px) {
  #headerbar {
    width: 480px;
  }
}
@media (min-width: 768px) {
  #headerbar {
    width: 720px;
  }
}
@media (min-width: 992px) {
  #headerbar {
    width: 930px;
  }
}
@media (min-width: 1200px) {
  #headerbar {
    width: 1080px;
  }
}
@media (min-width: 1440px) {
  #headerbar {
    width: 1360px;
  }
}

#logo {
  width: 104px;
  height: 44px;
  cursor: pointer;
}
</style>
