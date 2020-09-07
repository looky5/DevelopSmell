<template>
  <v-container class="likepost">
    <v-banner two-line>
      <v-avatar slot="icon" color="#78909C" size="40">
        <v-icon color="white">
          mdi-blogger
        </v-icon>
      </v-avatar>

      좋아요 누른 게시글
    </v-banner>
    <v-divider></v-divider>
    <v-container class="float-left ml-16">
      <div v-for="(item, index) in items" :key="index">
        <post
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

    <infinite-loading
      @infinite="infiniteHandler"
      ref="infiniteLoad"
      spinner="waveDots"
    >
      <div slot="no-more"></div>
      <div slot="no-results">등록된 게시물이 없습니다.</div>
    </infinite-loading>
  </v-container>
</template>
<script>
import post from "@/components/Post/Post.vue";
import InfiniteLoading from "vue-infinite-loading";
import { mapState } from "vuex";
import axios from "axios";
export default {
  data: function() {
    return {
      items: [],
      limit: 0
    };
  },
  computed: {
    ...mapState({
      userCheck: state => state.user,
      loginUserNo: state => state.userm.userno,
    })
  },
  components: {
    post,
    InfiniteLoading
  },
  methods: {
    infiniteHandler($state) {
      let userno = this.$store.state.userm.userno;
      axios({
        method: "GET",
        url: this.$baseurl + "/post/like/" + userno + "/" + this.limit,
        headers: {
          Authorization: this.$store.state.userm.token
        }
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
          }, 500);
        })
        .catch(error => {
          console.error(error);
        });
    }
  }
};
</script>
<style scoped>
.likepost {
  padding-top: 3rem;
}
</style>
