<template>
  <v-container class="float-left mt-10 post_con">
    <v-row justify="center" class="ma-2">
      <p class="font-weight-bold display-3 contenttitle">
        {{ title }}
      </p>
    </v-row>

    <v-row justify="end">
      <p class="display-0">
        {{ subtitle }}
      </p>
    </v-row>

    <v-divider class="my-2"></v-divider>

    <v-row>
      <v-list-item two-line >
        <v-list-item-avatar style="cursor:pointer" @click="userprofile(uid)">
          <img :src="profilename" />
        </v-list-item-avatar>

        <v-list-item-content>
          <v-list-item-title>by {{ uid }}</v-list-item-title>
          <v-list-item-subtitle>{{ create_date }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-row>

    <v-row>
      <v-spacer></v-spacer>
      <div>
        <v-btn
          icon
          v-if="loginUserNo == this.userno || isAdmin == true"
          @click="deletePost()"
        >
          삭제
        </v-btn>
         <v-btn
          icon
          v-if="loginUserNo == this.userno || isAdmin == true"
          @click="modifyPost()"
        >
          수정
        </v-btn>
      
      </div>
    </v-row>

    <v-row justify="end">
      <v-btn icon @click="likepost(1)">
        <v-icon v-if="kind != 1 || kind == -1">mdi-thumb-up-outline</v-icon>
        <v-icon v-if="kind == 1 && isLogin">mdi-thumb-up</v-icon>
      </v-btn>
      <span class="mx-1">{{ likecount }}</span>

      <v-btn icon @click="likepost(0)">
        <v-icon v-if="kind != 0 || kind == -1">mdi-thumb-down-outline</v-icon>
        <v-icon v-if="kind == 0 && isLogin">mdi-thumb-down</v-icon>
      </v-btn>
      <span class="mx-2">{{ dislikecount }}</span>

      <div>
        <b-dropdown variant="link" toggle-class="text-decoration-none" no-caret>
          <template v-slot:button-content>
            <v-icon>mdi-share-variant</v-icon>
          </template>
          <b-dropdown-item @click="kakaoShare"
            >카카오톡 공유하기</b-dropdown-item
          >
          <b-dropdown-item @click="facebookShare">
            페이스북 공유하기</b-dropdown-item
          >
          <b-dropdown-item @click="share">URL 복사하기</b-dropdown-item>
        </b-dropdown>

        <PostReport v-if="isLogin" />
      </div>
    </v-row>

    <v-row align="center" v-if="tags != null">
      <v-chip
        class="my-2 mx-1"
        color="indigo darken-3"
        outlined
        v-for="(tag, index) in tags.split(',')"
        :key="index"
        @click="searchbyTag(tag, $event)"
      >
        {{ tag }}
      </v-chip>
    </v-row>

     <v-row class=" mt-3 pa-3" justify="center" align="center">
        <Viewer v-if="content != null" :initialValue="content" />
      </v-row>
      <div class="rightsidebar">
        <b-button id="rightsidebutton" @click="viewcomment()">◀</b-button>
      </div>
      <div class="leftsidebar">
        <b-button id="leftsidebutton" @click="closecomment()">▶</b-button>
      </div>

    <commentbox  id="commentbar"></commentbox>
  </v-container>
</template>
<script>
import "codemirror/lib/codemirror.css";
import "@toast-ui/editor/dist/toastui-editor.css";
import PostReport from "@/components/Post/PostReport.vue";
import { Viewer } from "@toast-ui/vue-editor";
import axios from "axios";
import commentbox from "@/components/Comment/Comment.vue";
import { mapState } from "vuex";
import Swal from "sweetalert2";

export default {
  components: {
    Viewer,
    commentbox,
    PostReport
  },
  data: function() {
    return {
      userno: 0,
      content: null,
      likecount: 0,
      dislikecount: 0,
      comment: null,
      tags: null,
      title: null,
      subtitle: null,
      create_date: null,
      postno: -1,
      uid: null,
      kind: -1,
      profilename: null,
      // url: "https://www.naver.com"
    };
  },
  computed: {
    ...mapState({
      loginUserNo: state => state.userm.userno,
      isLogin: state => state.userm.isLogin,
      isAdmin: state => state.userm.isAdmin,
      userUid: state => state.userm.uid
    })
  },
  methods: {
    deletePost() {
      //여기에 이제 현재 로그인 되어있는 유저의 게시글만 삭제할수 있으므로, 게시글 번호로 삭제
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
          let postno = this.$route.params.postno;

          axios({
            method: "DELETE",
            url: this.$baseurl + "/post/" + postno,
            headers: {
              Authorization: this.$store.state.userm.token
            }
          }).then(() => {
            Swal.fire("Deleted!", "게시글이 삭제되었습니다.", "success");
            let isMyblog = this.$store.state.location.isMyblog;
            //내블로그에서 상세페이지로 들어가서 게시글을 삭제했다면 나의 블로그로...
            if (isMyblog) {
              this.$router.push({ path: "/myblog/"+ this.$store.state.userm.uid });
              this.$store.state.location.isMyblog = false;
            }
            //메인화면에서 상세페이지로 들어가서 게시글을 삭제헀다면...
            else {
              this.$router.push({ path: "/" });
            }
            location.reload();
          });
        }
      });
    },
    share() {
      var dummy = document.createElement("textarea");
      document.body.appendChild(dummy);
      dummy.value = window.location.href;;
      dummy.select();
      document.execCommand("copy");
      document.body.removeChild(dummy);
    },
    facebookShare() {
      var linkUrl = window.location.href;
      window.open(
        "http://www.facebook.com/sharer.php?u=" + encodeURIComponent(linkUrl),
        "_blank",
        "width=500,height=600"
      );
    },

    kakaoShare() {
       let url = document.location.href;
        Kakao.Link.sendDefault({
          objectType: "feed",
          content: {
            title: this.title,
            imageUrl: "",
            link: {
              mobileWebUrl: url,
              webUrl: url
            }
          }
        });
    },
    searchbyTag(tag, event) {
      this.$router.push("/post/tag/" + tag);
    },
    addlike(userno, postno, kind) {
      axios({
        method: "POST",
        url: this.$baseurl + "/post/like",
        data: {
          userno: userno,
          targetno: postno,
          kind: kind
        },
        headers: {
          Authorization: this.$store.state.userm.token
        }
      }).then(() => {
        this.addLikeCount(kind);
        this.kind = kind;
      });
    },
    addLikeCount(kind) {
      if (kind == 0) {
        this.dislikecount++;
      } else {
        this.likecount++;
      }
    },
    minusLikeCount(kind) {
      if (kind == 0) {
        this.dislikecount--;
      } else {
        this.likecount--;
      }
    },
    modifylike(userno, postno, kind) {
      axios({
        method: "PUT",
        url: this.$baseurl + "/post/like/",
        data: {
          userno: userno,
          targetno: postno,
          kind: kind
        },
        headers: {
          Authorization: this.$store.state.userm.token
        }
      }).then(() => {
        if (kind == 1) {
          this.addLikeCount(kind);
          this.minusLikeCount(0);
        } else {
          this.addLikeCount(kind);
          this.minusLikeCount(1);
        }
        this.kind = kind;
      });
    },
    modifyPost(){
        let postno = this.$route.params.postno;
        this.$router.push("/post/modify/"+postno);
    },
    deletelike(userno, postno, kind) {
      axios({
        method: "DELETE",
        url: this.$baseurl + "/post/like/" + userno + "/" + postno,
        headers: {
          Authorization: this.$store.state.userm.token
        }
      }).then(() => {
        this.minusLikeCount(kind);
        this.kind = -1;
      });
    },
    viewcomment() {
      document.getElementById("commentbar").style.display = "block";
      document.getElementById("commentbar").style.width = "100%";
      document.getElementById("commentbar").style.right = "0";
      document.getElementsByClassName("leftsidebar")[0].style.display = "block";
      document.getElementsByClassName("rightsidebar")[0].style.display = "none";
    },
    closecomment() {
      document.getElementById("commentbar").style.display = "none";
      document.getElementById("commentbar").style.width = "20%";
      document.getElementsByClassName("rightsidebar")[0].style.display = "block";
      document.getElementsByClassName("leftsidebar")[0].style.display = "none";
      document.getElementById("commentbar").removeAttribute("style");
      document.getElementsByClassName("leftsidebar")[0].removeAttribute("style");
      document.getElementsByClassName("rightsidebar")[0].removeAttribute("style");
    },
    likepost(kind) {
      let userno = this.$store.state.userm.userno;

      if (userno != 0) {
        let postno = this.$route.params.postno;
        if (this.kind == 1) {
          if (kind == 0) {
            this.modifylike(userno, postno, kind);
          } else if (kind == 1) {
            this.deletelike(userno, postno, kind);
          }
        } else if (this.kind == 0) {
          if (kind == 1) {
            this.modifylike(userno, postno, kind);
          } else if (kind == 0) {
            this.deletelike(userno, postno, kind);
          }
        } else {
          //this.kind = -1
          this.addlike(userno, postno, kind);
        }
      } else {
        alert("로그인후 사용가능합니다.");
      }
    },
    userprofile(uid){
      console.log(uid)
      this.$router.push("/userblog/"+uid);
    }
  },
  mounted() {
    var postno = this.$route.params.postno;
    let userno = this.$store.state.userm.userno || '0';
    console.log(userno)
    //여기서 정보 져오기
    axios({
      method: "GET",
      url: this.$baseurl + "/post/detail/" + postno + "/" + userno
    }).then(res => {
      let data = res.data.object;

      this.content = data.content;
      this.title = data.title;
      this.subtitle = data.subtitle;
      this.uid = data.uid;
      this.create_date = data.create_date;
      this.tags = data.tags;
      this.likecount = data.likecount;
      this.dislikecount = data.dislikecount;
      this.kind = data.kind;
      this.profilename = data.profilename;
      this.userno = data.userno;
    }).catch(err =>{
      console.log(err)
    });
  }
};
</script>

<style scoped>
.post_con {
  width: 70%;
  padding: 5% 5% 0 5%;
}
.rightsidebar {
  display: none;
  position: fixed;
  right: 0px;
  top: 50%;
}
.leftsidebar {
  display: none;
  position: fixed;
  left: 0px;
  top: 50%;
  z-index: 50;
}
#rightsidebutton {
  width: 20px;
  height: 50px;
  padding: 0;
}
#leftsidebutton {
  width: 20px;
  height: 50px;
  padding: 0;
}
#commentbar {
  display: block;
}
@media (max-width: 768px) {
  #commentbar {
    display: none;
  }
  .post_con {
    width: 100%;
  }
  .rightsidebar {
    display: block;
  }
  .leftsidebar {
    display: none;
  }
}
@media (min-width: 768px) {
  #commentbar {
    display: block;
    width: 27%;
  }
  .post_con {
    width: 70%;
  }
  .rightsidebar {
    display: none;
  }
  .leftsidebar {
    display: none;
  }
}
.contenttitle {
  word-break: keep-all;
}
</style>
