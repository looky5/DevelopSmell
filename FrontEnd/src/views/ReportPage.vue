<template>
  <div id="report">
    <b-button class="reportbtn" variant="secondary" @click="loadPostReport()"
      >신고 글</b-button
    >
    <b-button class="reportbtn" variant="secondary" @click="loadCommentReport()"
      >신고 댓글</b-button
    >
    <div>
      <b-table
        v-if="isPostReport"
        striped
        hover
        :items="postitems"
        :fields="postfields"
        :tbody-tr-class="postrowClass"
      ></b-table>
      <b-table
        v-if="isCommentReport"
        striped
        hover
        :items="commentitems"
        :fields="commentfields"
        :tbody-tr-class="commentrowClass"
      ></b-table>
    </div>
    <infinite-loading
      v-if="isPostReport"
      @infinite="postinfiniteHandler"
      ref="infiniteLoad"
      spinner="waveDots"
    >
      <div slot="no-more"></div>
      <div slot="no-results">등록된 신고 글이 없습니다.</div>
    </infinite-loading>

    <infinite-loading
      v-if="isCommentReport"
      @infinite="commentinfiniteHandler"
      ref="infiniteLoad"
      spinner="waveDots"
    >
      <div slot="no-more"></div>
      <div slot="no-results">등록된 신고 댓글이 없습니다.</div>
    </infinite-loading>
  </div>
</template>

<script>
import axios from "axios";
import InfiniteLoading from "vue-infinite-loading";
export default {
  components: {
    InfiniteLoading
  },
  data() {
    return {
      isPostReport: true,
      isCommentReport: false,
      postlimit: 0,
      commentlimit: 0,
      postitems: [],
      commentitems: [],
      //신고글에 따른 빈도수가 필요
      postfields: [
        {
          key: "reportno",
          label: "신고번호",
          sortable: true
        },
        {
          key: "userno",
          label: "신고자 번호",
          sortable: true
        },
        {
          key: "uid",
          label: "신고자 닉네임",
          sortable: true
        },
        {
          key: "postno",
          label: "신고글 번호",
          sortable: true
        },
        {
          key: "title",
          label: "신고글 제목",
          sortable: true
        },
        {
          key: "content",
          label: "신고 내용",
          sortable: false
        },
        {
          key: "reportcount",
          label: "게시글 누적 신고수",
          sortable: true
        }
      ],
      commentfields: [
        {
          key: "creportno",
          label: "신고번호",
          sortable: true
        },
        {
          key: "userno",
          label: "신고자 번호",
          sortable: true
        },
        {
          key: "uid",
          label: "신고자 닉네임",
          sortable: false
        },
        {
          key: "postno",
          label: "신고 게시글 번호",
          sortable: true
        },
        {
          key: "content",
          label: "신고사유",
          sortable: false
        },
        {
          key: "commentno",
          label: "댓글 번호",
          sortable: true
        },
        {
          key: "reply",
          label: "댓글 내용",
          sortable: false
        },
        {
          key: "reportcount",
          label: "댓글 누적 신고수",
          sortable: true
        }
      ]
    };
  },
  methods: {
    loadPostReport() {
      this.postlimit = 0;
      this.postitems = [];
      this.commentitems = [];
      this.$refs.infiniteLoad.stateChanger.reset();
      this.isCommentReport = false;
      this.isPostReport = true;
    },
    loadCommentReport() {
      this.commentlimit = 0;
      this.postitems = [];
      this.commentitems = [];
      this.$refs.infiniteLoad.stateChanger.reset();
      this.isPostReport = false;
      this.isCommentReport = true;
    },
    postrowClass(item, type) {
      if (!item || type !== "row") {
        return;
      }
      if (item.reportcount >= 5) item._rowVariant = "danger";
    },
    postinfiniteHandler($state) {
      axios({
        method: "GET",
        url: this.$baseurl + "/post/report/" + this.postlimit,
        data: {},
        headers: {
          Authorization: this.$store.state.userm.token
        }
      })
        .then(res => {
          setTimeout(() => {
            if (res.data.object.length) {
              let data = res.data.object;

              for (let i = 0; i < data.length; i++) {
                this.postitems.push(data[i]);
              }

              $state.loaded();
              this.postlimit += 10;
              if (data.length % 10 != 0) {
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
    commentrowClass(item, type) {
      if (!item || type !== "row") {
        return;
      }
      if (item.reportcount >= 5) item._rowVariant = "danger";
    },
    commentinfiniteHandler($state) {
      axios({
        method: "GET",
        url: this.$baseurl + "/comment/report/" + this.commentlimit,
        data: {},
        headers: {
          Authorization: this.$store.state.userm.token
        }
      })
        .then(res => {
          setTimeout(() => {
            if (res.data.object.length) {
              let data = res.data.object;
              for (let i = 0; i < data.length; i++) {
                this.commentitems.push(data[i]);
              }

              $state.loaded();
              this.commentlimit += 10;
              if (data.length % 10 != 0) {
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

<style>
.reportbtn {
  margin: 1rem 0.3rem;
}
#report {
  width: 100%;
  padding: 5%;
  text-align: center;
}
</style>
