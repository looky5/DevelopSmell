<template>
  <div class="mt-13" id="maincontainer">
    <h1 v-text="'#' + tag"></h1>
    <v-container>
      <v-list three-line>
        <v-list-item
          style="border-bottom:1px dotted black"
          class="pb-3"
          v-for="(item, index) in items"
          :key="index"
          justify-center
        >
          <v-row class="float-center">
            <v-list-item>
              <v-list-item-avatar style="cursor:pointer" @click="profile(item.uid)">
                <img :src="item.profilename" />
              </v-list-item-avatar>

              <v-list-item-content>
                <v-list-item-title>by {{ item.uid }}</v-list-item-title>
              </v-list-item-content>
            </v-list-item>

            <div class="subtitle">
              <v-chip
                small
                class="mb-3 mx-1"
                color="indigo darken-3"
                outlined
                v-for="(tag, index) in item.tags"
                :key="index"
                v-on:click.prevent.self
                @click="searchbyTag(tag, $event)"
              >
                {{ tag }}
              </v-chip>
            </div>

            <v-list-item-title>
              <div style="font-size:50px; cursor:pointer;" @click="detail(item.postno)">{{ item.title }}</div>
            </v-list-item-title>
            <v-list-item-subtitle>
              <div class="my-3" style="font-size:30px;">
                {{ item.subtitle }}
              </div>
            </v-list-item-subtitle>
            <div>{{ item.create_date }}</div>
          </v-row>
        </v-list-item>
      </v-list>
    </v-container>
    <rightsection></rightsection>

    <infinite-loading
      @infinite="infiniteHandler"
      ref="infiniteLoad"
      spinner="waveDots"
    >
      <div slot="no-more"></div>
      <div slot="no-results">등록된 게시물이 없습니다.</div>
    </infinite-loading>
  </div>
</template>

<script>
import rightsection from "@/components/Layout/MainRightBar.vue";
import InfiniteLoading from "vue-infinite-loading";
import { mapState } from "vuex";
import axios from "axios";
export default {
  components: {
    rightsection,
    InfiniteLoading
  },
  computed: {
    ...mapState({
      loginUserNo: state => state.userm.userno,
      isLogin: state => state.userm.isLogin,
      userUid: state => state.userm.uid,
      isSearch: state => state.search.isSearch
    })
  },
  data: () => {
    return {
      tag: "",
      items: [],
      limit: 0,
      width: "100px",
      count: 9
    };
  },
  created() {
    this.tag = this.$route.params.tag;
  },
  methods: {
    infiniteHandler($state) {
      axios({
        method: "GET",
        url: this.$baseurl + "/post/tags/" + this.tag
      })
        .then(res => {
          setTimeout(() => {
            if (res.data.object.length) {
              let data = res.data.object;

              for (let i = 0; i < data.length; i++) {
                //태그
                let tags = data[i].tags;
                if (tags != null) {
                  let tagsArr = tags.split(",");
                  data[i].tags = tagsArr;
                }

                this.items.push(data[i]);
              }

              $state.loaded();
              this.limit += 9;
              if (data.length % 9 != 0) {
                $state.complete();
              }
            } else {
              $state.complete();
            }
          }, 1000);
        })
        .catch(error => {
          console.error(error);
        });
    },
    searchbyTag(tag, event) {
      this.tag = tag;
      this.resetlist();
      // this.$router.push("/post/tag/" + tag);
    },
    resetlist: function() {
      this.items = [];
      this.limit = 0;
      this.$refs.infiniteLoad.stateChanger.reset();
    },
    detail(postno) {
      this.$router.push("/post/detail/" + postno);
    },
    profile(uid){
      console.log(uid)
        this.$router.push("/userblog/"+uid);
    }
  }
};
</script>
<style scoped>
#maincontainer{
  padding: 2% 7%;
  margin: 0 20% 0 0;
}
</style>
