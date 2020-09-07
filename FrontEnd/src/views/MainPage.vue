<template>
  <div class="mt-13" id="maincontainer">
    <v-container class="float-left con_layout" >
      <search v-if="isSearch" v-on:resetList="resetlist"></search>
      <div id="postlist" v-for="(item, index) in items" :key="index">
        <post id="post"
          :title="item.title"
          :content="item.content"
          :userno="item.userno"
          :postno="item.postno"
          :nickname="item.uid"
          :subtitle="item.subtitle"
          :tags="item.tags"
          :createdate="item.create_date"
          :save="item.save"
          :kind="item.kind"
          :thumbnail="item.thumbnail"
          :likecount="item.likecount"
          :dislikecount="item.dislikecount"
          :profilename="item.profilename"
          :userrating="item.userrating"
          :postrating="item.postrating"
        ></post>
      </div>
    </v-container>
    <rightsection id="rightbar"></rightsection>

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
import search from "@/components/Search/Search.vue";
import InfiniteLoading from "vue-infinite-loading";
import { mapState } from "vuex";
import post from "@/components/Post/Post.vue";
import axios from "axios";
export default {
  components: {
    rightsection,
    post,
    InfiniteLoading,
    search
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
      items: [],
      limit: 0,
      count: 9
    };
  },
  created() {
    console.log("mainpage : "+this.$store.state.search.isSearch)
    this.$store.state.location.isMyblog = false;
    // this.$store.state.search.isSearch = false;
  },
  methods: {
    resetlist: function() {
      this.items = [];
      this.limit = 0;
      this.$refs.infiniteLoad.stateChanger.reset();
    },
    infiniteHandler($state) {
      let userno = this.$store.state.userm.userno || 0;
      let tags = this.$store.state.search.tags;
      let word = this.$store.state.search.word || "";
      let url = this.$baseurl + "/post/search/" + this.limit + "/" + userno;
      url += "?word=" + word + "&";
      for (let i = 0; i < tags.length; i++) {
        url += "tags=" + tags[i];
        if (i < tags.length - 1) {
          url += "&";
        }
      }

      axios({
        method: "GET",
        url: url
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
    }
  }
};
</script>
<style scoped>
#maincontainer {
  padding: 2% 0;
  margin: 0 20% 0 0;
}
#postlist{
  display: inline-block;
}
.float-left{
  box-sizing: border-box;
}
#post{
  text-align: left;
}
@media (max-width: 575px) {
  .con_layout {
    text-align: center;
  }
}
@media (min-width: 576px) {
  .con_layout {
    padding: 0 0 0 15%;
  }
}
@media (min-width: 767px) {
  .con_layout {
    padding: 0 0 0 2.5%;
  }
}
@media (max-width: 992px) {
  #rightbar {
    display: none;
  }
  #maincontainer {
    margin: 0;
  }
}
@media (min-width: 992px) {
  .con_layout {
    padding: 0 0 0 4%;
  }
}
@media (min-width: 1200px) {
  .con_layout {
    padding: 0 0 0 10%;
  }
}
@media (min-width: 1440px) {
  .con_layout {
    padding: 0 0 0 3%;
  }
}
</style>
