<template>
  <v-navigation-drawer
    class="border-top"
    permanent
    right
    width="27%"
    fixed
    collpase
  >
    <v-container class="mt-5">
      <v-row class="px-3 pt-5">
        <v-textarea
          class="mt-5"
          v-model="comment"
          auto-grow
          label="댓글달기"
          row-height="5"
          shaped
        ></v-textarea>
      </v-row>

      <v-row justify="end">
        <v-btn small class="mx-2" outlined color="indigo" @click="resetwrite"
          >취소</v-btn
        >
        <v-btn small class="mx-2" outlined color="indigo" @click="commentwrite"
          >작성</v-btn
        >
      </v-row>

      <v-row
        v-if="comments != null && comments.length > 0"
        class="rounded mt-3"
        align="center"
      >
        <v-list class="comment_content">
          <v-list-item v-for="(comment, index) in comments" :key="index">
            <v-row>
              <v-list-item two-line>
                <v-list-item-avatar
                  style="cursor:pointer"
                  @click="userprofile(comment.uid)"
                >
                  <img :src="comment.profilename" />
                </v-list-item-avatar>

                <v-list-item-content>
                  <v-list-item-title>by {{ comment.uid }}</v-list-item-title>
                  <v-list-item-subtitle>{{
                    comment.reply_date
                  }}</v-list-item-subtitle>
                </v-list-item-content>

                <div>
                  <b-dropdown
                    v-if="isLogin"
                    variant="link"
                    toggle-class="text-decoration-none"
                    no-caret
                  >
                    <template v-slot:button-content>
                      <v-icon>mdi-dots-vertical</v-icon>
                    </template>
                    <b-dropdown-item
                      v-if="loginUserNo == comment.userno || isAdmin == true"
                      @click="deleteComment(comment.commentno)"
                      ><v-btn>삭제</v-btn></b-dropdown-item
                    >
                    <b-dropdown-divider
                      v-if="loginUserNo == comment.userno || isAdmin == true"
                    ></b-dropdown-divider>
                    <b-dropdown-item>
                      <CommentReport :commentno="comment.commentno"/>
                    </b-dropdown-item>
                  </b-dropdown>
                </div>
                <!-- <v-btn><v-icon>mdi-dots-vertical</v-icon></v-btn>
                                <v-btn
                                icon
                                v-if="loginUserNo == comment.userno || isAdmin == true"
                               @click="deleteComment(comment.commentno)"
                                >
                                삭제
                                </v-btn> -->
                <!-- <CommentReport/> -->
              </v-list-item>

              <v-list-item-title>
                <div id="reply">{{ comment.reply }}</div>
              </v-list-item-title>
              <v-row justify="end" class="mr-1">
                <v-btn
                  icon
                  @click="
                    likecomment(index, comment.kind, comment.commentno, 1)
                  "
                >
                  <v-icon small v-if="comment.kind != 1 || comment.kind == -1"
                    >mdi-thumb-up-outline</v-icon
                  >
                  <v-icon small v-if="comment.kind == 1">mdi-thumb-up</v-icon>
                </v-btn>

                <span class="mx-1">{{ comment.likecount }} </span>

                <v-btn
                  icon
                  @click="
                    likecomment(index, comment.kind, comment.commentno, 0)
                  "
                >
                  <v-icon small v-if="comment.kind != 0 || comment.kind == -1"
                    >mdi-thumb-down-outline</v-icon
                  >
                  <v-icon small v-if="comment.kind == 0">mdi-thumb-down</v-icon>
                </v-btn>

                <span class="mx-2">{{ comment.dislikecount }}</span>

                <!-- <v-btn  icon v-if="loginUserNo == comment.userno || userUid =='admin'" @click="deleteComment(comment.commentno)">
                                <v-icon small> mdi-delete</v-icon>
                            </v-btn> -->
              </v-row>

              <!-- <v-divider ></v-divider> -->
            </v-row>
          </v-list-item>
        </v-list>
      </v-row>
      <v-row justify="center" aling="center">
        <v-pagination
          v-model="page"
          :length="this.totalcomments.length"
        ></v-pagination>
      </v-row>
    </v-container>
  </v-navigation-drawer>
</template>


<script>
import axios from "axios";
import { mapState } from "vuex";
import Swal from "sweetalert2";
import CommentReport from "@/components/Comment/CommentReport.vue";

export default {
  components: {
    CommentReport
  },

  data: () => {
    return {
      data: {},
      comment: "",
      totalcomments: [],
      comments: [],
      page: 1
    };
  },
  methods: {
    commentwrite() {
      let isLogin = this.$store.state.userm.isLogin || "";

      if (!isLogin) {
        alert("로그인 후 사용가능 합니다");
        return;
      }

      let flag = false;
      if (this.comment != null) {
        let commentData = this.comment.trim();
        if (commentData != "") {
          flag = true;
        }
      }
      if (!flag) {
        alert("답글을 작성해주세요");
        return;
      }

      axios({
        method: "POST",
        url: this.$baseurl + "/comment",
        data: {
          reply: this.comment,
          postno: this.$route.params.postno,
          userno: this.$store.state.userm.userno
        },
        headers: {
          Authorization: this.$store.state.userm.token
        }
      })
        .then(() => {
          location.reload();
        })
        .catch(res => {
          console.dir(res);
        });
    },
    resetwrite() {
      this.comment = "";
    },
    clickCallback(pageNum) {
      this.comments = this.totalcomments[pageNum - 1];
    },
    deleteComment(commentno) {
      Swal.fire({
        title: "삭제하시겠습니까?",
        text: "삭제하면 다시 복구할 수 없습니다!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Delete"
      }).then(result => {
        if (result.value) {
          axios({
            method: "DELETE",
            url: this.$baseurl + "/comment/" + commentno,
            headers: {
              Authorization: this.$store.state.userm.token
            }
          }).then(() => {
            location.reload();
            Swal.fire("Deleted!", "댓글이 삭제되었습니다.", "success");
          });
        }
      });
    },
    addlike(index, userno, commentno, newkind) {
      axios({
        method: "POST",
        url: this.$baseurl + "/comment/like",
        data: {
          userno: userno,
          targetno: commentno,
          kind: newkind
        },
        headers: {
          Authorization: this.$store.state.userm.token
        }
      }).then(() => {
        this.addLikeCount(index, newkind);
        this.comments[index].kind = newkind;
      });
    },
    addLikeCount(index, kind) {
      //추가, 수정

      if (kind == 1) {
        this.comments[index].likecount++;
      } else {
        this.comments[index].dislikecount++;
      }
    },

    minusLikeCount(index, kind) {
      //수정, 삭제
      if (kind == 1) {
        this.comments[index].likecount--;
      } else {
        //0
        this.comments[index].dislikecount--;
      }
    },

    modifylike(index, userno, commentno, kind) {
      axios({
        method: "PUT",
        url: this.$baseurl + "/comment/like/",
        data: {
          userno: userno,
          targetno: commentno,
          kind: kind
        },
        headers: {
          Authorization: this.$store.state.userm.token
        }
      }).then(() => {
        if (kind == 1) {
          this.minusLikeCount(index, 0);
          this.addLikeCount(index, kind);
        } else {
          this.minusLikeCount(index, 1);
          this.addLikeCount(index, kind);
        }
        this.comments[index].kind = kind;
      });
    },
    deletelike(index, userno, commentno, kind) {
      axios({
        method: "DELETE",
        url: this.$baseurl + "/comment/like/" + userno + "/" + commentno,
        headers: {
          Authorization: this.$store.state.userm.token
        }
      }).then(() => {
        this.minusLikeCount(index, kind);
        this.comments[index].kind = -1;
      });
    },
    likecomment(index, currentkind, commentno, newkind) {
      let userno = this.$store.state.userm.userno;
      if (userno != 0) {
        if (currentkind == 1) {
          if (newkind == 1) {
            //삭제
            this.deletelike(index, userno, commentno, newkind);
          } else {
            //0 수정
            this.modifylike(index, currentkind, commentno, newkind);
          }
        } else if (currentkind == 0) {
          if (newkind == 0) {
            //삭제
            this.deletelike(index, userno, commentno, newkind);
          } else {
            //0 수정
            this.modifylike(index, currentkind, commentno, newkind);
          }
        } else {
          //currentkind = -1
          this.addlike(index, userno, commentno, newkind);
        }
      } else {
        alert("로그인후 사용가능합니다.");
      }
    },
    userprofile(uid) {
      console.log(uid);
      this.$router.push("/userblog/" + uid);
    }
  },
  watch: {
    page: function(page) {
      this.comments = this.totalcomments[page - 1];
    }
  },

  created() {
    let postno = this.$route.params.postno;
    let userno = this.$store.state.userm.userno || "0";
    axios({
      method: "GET",
      url: this.$baseurl + "/comment/get/" + postno + "/" + userno
    }).then(res => {
      let data = res.data.object;

      //좋아요 , 싫어요 정보
      let currentIndex = 0;
      for (var i = 0; i < data.length; i++) {
        if (i % 10 == 0) {
          this.totalcomments.push([data[i]]);
          if (i != 0) {
            currentIndex++;
          }
        } else {
          this.totalcomments[currentIndex].push(data[i]);
        }
      }
      this.comments = this.totalcomments[0];
      this.data = data;
    });
  },
  computed: {
    ...mapState({
      loginUserNo: state => state.userm.userno,
      isLogin: state => state.userm.isLogin,
      isAdmin: state => state.userm.isAdmin,
      userUid: state => state.userm.uid
    })
  }
};
</script>


<style scoped>
#reply {
  width: 100%;
  white-space: pre-line;
  word-break: break-all;
  padding-left: 1rem;
}
#icon {
  padding-left: 23rem;
}
.v-btn--contained {
  box-shadow: 0px 0px 0px 0px rgba(0, 0, 0);
}
.theme--light.v-btn:not(.v-btn--flat):not(.v-btn--text):not(.v-btn--outlined) {
  background-color: rgba(255, 255, 255, 0.12);
  width: 6.5rem;
}
dropdown-menu.show {
  height: auto;
}
.border-top {
  border-right: 1px solid rgba(211, 211, 211, 0.9);
  right: 150px;
  top: 120px !important;
  box-shadow: 0px 3px 1px -2px rgba(0, 0, 0, 0.2), 0px 2px 2px 0px rgba(0, 0, 0, 0.14), 0px 1px 5px 0px rgba(0, 0, 0, 0.12);
  height: 80vh !important;
}
.comment_content {
  display: block;
}
.v-list-item--two-line {
  width: 0px;
}
@media (min-width: 768px) {
  .border-top {
    right: 40px;
  }
}
@media (min-width: 992px) {
  .border-top {
    right: 60px;
  }
}
@media (min-width: 1200px) {
  .border-top {
    right: 90px;
  }
}
@media (min-width: 1440px) {
  .border-top {
    right: 100px;
  }
}
@media (min-width: 1440px) {
  .title {
    font-size: 22px !important;
  }
  .list_font {
    font-size: 18px !important;
  }
}
</style>