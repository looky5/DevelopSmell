<template>
  <div class="mt-16">
    <div id="titleback">
      <div id="title">
        게시물 수정
      </div>
    </div>
    <v-card>
      <v-text-field
        label="Title"
        name="title"
        v-model="title"
        prepend-icon="mdi-label"
        type="text"
        class="mx-10 "
        id="title"
      ></v-text-field>
      <v-text-field
        label="SubTitle"
        name="subtitle"
        v-model="subtitle"
        prepend-icon="mdi-label-outline"
        type="text"
        class="mx-10"
        id="subtitle"
      ></v-text-field>

      <div class="mx-10">
        <v-row>
          <v-chip
            class="ma-1"
            color="indigo darken-3"
            height="100px"
            outlined
            v-for="(tag, index) in usetags"
            :key="index"
            @click="removetag(index)"
          >
            {{ tag }}
          </v-chip>
        </v-row>
        <v-tooltip bottom>
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              id="inputTag"
              prepend-icon="mdi-tag-multiple"
              v-bind="attrs"
              v-on="on"
              v-model="inputTag"
              required
              placeholder="태그를 입력하세요."
              @keyup.native="enterkey"
            >
            </v-text-field>
          </template>
          <span left>Enter 클릭 시 등록되며, 태그 클릭 시 삭제 됩니다.</span>
        </v-tooltip>
        <v-file-input
          v-model="thumbnail"
          ref="Thumbnail"
          id="Thumbnail"
          accept="image/png, image/jpeg, image/bmp"
          placeholder="Pick an Thumbnail"
          prepend-icon="mdi-camera"
          label="Thumbnail"
        ></v-file-input>
      </div>

      <v-row class="ml-3">
        <v-chip
          class="mx-2 mb-3"
          color="indigo darken-3"
          height="100px"
          outlined
          v-for="(template, index) in templates"
          :key="index"
          @click="getTemplate(index)"
        >
          {{ template }}
        </v-chip>
      </v-row>

      <editor
        id="editor"
        ref="toastuiEditor"
        height="900px"
        v-if="editorText != null"
        :initialValue="editorText"
      >
      </editor>
      <div style="text-align:right">
        <v-btn class="ma-2" color="rgb(240, 162, 46)" @click="createAction(0)">
          임시저장
        </v-btn>
        <v-btn class="ma-2" color="rgb(240, 162, 46)" @click="createAction(1)">
          등록하기
        </v-btn>
      </div>
    </v-card>
  </div>
</template>

<script>
import "@toast-ui/editor/dist/toastui-editor.css";
import { Editor } from "@toast-ui/vue-editor";
import Swal from "sweetalert2";
import axios from "axios";
export default {
  data: () => {
    return {
      title: "",
      subtitle: "",
      inputTag: "",
      usetags: [],
      editorText: null,
      thumbnail: null,
      templates: ["template1", "template2", "template3"]
    };
  },
  props: [],
  components: {
    Editor
  },
  created() {
    let postno = this.$route.params.postno;

    axios({
      method: "GET",
      url:
        this.$baseurl +
        "/post/detail/" +
        postno +
        "/" +
        this.$store.state.userm.userno
    }).then(res => {
      let data = res.data.object;
      this.title = data.title;
      this.subtitle = data.subtitle;
      let tags = data.tags;

      if (tags != null) {
        let tagsArr = tags.split(",");
        this.usetags = tagsArr;
      }

      this.editorText = data.content;
    });
  },
  methods: {
    getTemplate(index) {
      let templateno = index + 1;
      axios({
        method: "GET",
        url: this.$baseurl + "/post/template/" + templateno
      }).then(res => {
        this.$refs.toastuiEditor.invoke("setMarkdown", res.data.object);
      });
    },

    enterkey() {
      if (window.event.keyCode == 13) {
        let usetag = this.inputTag.trim();
        if (usetag != "") {
          if (this.usetags.length >= 5) {
            alert("더이상 태그를 붙일 수 없습니다.");
          } else if (this.usetags.indexOf(usetag) == -1) {
            if (this.inputTag.length < 10) {
              this.usetags.push(usetag);
            } else {
              alert("태그 길이 10글자 이하");
            }
          } else {
            alert("이미 등록되어있는 태그입니다.");
          }
        } else {
          alert("태그를 입력해주세요");
        }
        this.inputTag = "";
      }
    },
    removetag(index) {
      this.usetags.splice(index, 1);
    },

    createAction(psave) {
      if (this.checkInput()) {
        let postno = this.$route.params.postno;

        const formData = new FormData();
        formData.append("thumbnail", this.thumbnail);
        formData.append("postno", postno);
        formData.append("userno", this.$store.state.userm.userno);
        formData.append("title", this.title.trim());
        formData.append("subtitle", this.subtitle.trim());
        formData.append("usetags", this.usetags);
        formData.append("save", psave);
        formData.append(
          "content",
          this.$refs.toastuiEditor.invoke("getMarkdown")
        );
        axios({
          method: "PUT",
          url: this.$baseurl + "/post/",
          data: formData,
          headers: {
            Authorization: this.$store.state.userm.token
          }
        })
          .then(() => {
            if (psave == 1) {
              Swal.fire({
                position: "center",
                icon: "success",
                title: "게시글이 수정되었습니다.",
                showConfirmButton: false,
                timer: 1500
              });
            } else {
              Swal.fire({
                position: "center",
                icon: "success",
                title: "임시저장 되었습니다.",
                showConfirmButton: false,
                timer: 1500
              });
            }
            // location.reload();
          })
          .catch(() => {
            alert("처리에 실패하였습니다.");
          })
          .finally(() => {
            this.$router.go(-1);
          });
      }
    },
    checkInput() {
      let title = this.title.trim();
      let subtitle = this.subtitle.trim();
      let usetags = this.usetags;
      let thumbnail = this.thumbnail;
      let content = this.$refs.toastuiEditor.invoke("getMarkdown"); // content를 저장하는 액션 처리 }
      if (title == "") {
        alert("제목을 입력해주세요");
        document.getElementById("title").focus();
        return false;
      }

      if (subtitle == "") {
        alert("부제목을 입력해주세요");
        document.getElementById("subtitle").focus();
        return false;
      }

      if (usetags == "") {
        alert("태그를 등록해주세요");
        document.getElementById("inputTag").focus();
        return false;
      }

      if (content == "") {
        alert("내용을 입력해주세요");
        document.getElementById("editor").focus();
        return false;
      }
      if (thumbnail == null) {
        alert("썸네일을 등록해주세요");
        return false;
      }
      return true;
    }
  }
};
</script>

<style scoped>
#box {
  width: 90%;
  border: 1px solid #b0bec5;
  height: 100px;
  margin: 10px;
  padding: 10px;
  border-radius: 10px;
}
#title {
  position: relative;
  top: 30px;
  text-align: center;
  font-size: 30px;
  font-weight: bold;
}
#titleback {
  width: 300px;
  background-size: 300px;
  height: 100px;
  color: rgb(115, 50, 20);
  opacity: 0.8;
  background-image: url("../../assets/back.png");
  margin: 0 auto;
}
</style>
