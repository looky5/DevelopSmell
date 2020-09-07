<template>
  <v-dialog v-model="dialog" persistent max-width="400">
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        class="mr-5 float-right"
        color="primary"
        dark
        icon
        v-bind="attrs"
        v-on="on"
      >
        회원가입
      </v-btn>
    </template>
    <v-card id="card">
      <v-toolbar id="toolbar" flat>
        <v-toolbar-title>JOIN form</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-icon class="material-icons" @click="closeDialog"> mdi-close</v-icon>
      </v-toolbar>
      <v-card-text id="certification">
        <v-form @submit.prevent>
          <v-text-field
            id="Email"
            label="Email"
            name="email"
            v-model="email"
            prepend-icon="mdi-email"
            type="text"
          ></v-text-field>

          <v-text-field
            label="NickName"
            name="uid"
            v-model="uid"
            prepend-icon="mdi-email"
            type="text"
          ></v-text-field>
          <v-text-field
            id="password"
            label="Password"
            name="password"
            v-model="password"
            prepend-icon="mdi-lock"
            type="password"
          ></v-text-field>

          <v-text-field
            id="passwordConfirm"
            label="passwordConfirm"
            name="passwordConfirm"
            v-model="passwordConfirm"
            prepend-icon="mdi-lock"
            type="password"
          ></v-text-field>

          <v-text-field
            label="Introduce"
            name="introduce"
            v-model="introduce"
            prepend-icon="mdi-account"
            type="text"
          ></v-text-field>

          <v-row align="center" justify="center">
            <label>
              <input v-model="isTerm" type="checkbox" id="term" />
              <span id="agree" class="mr-3"> 약관에 동의합니다 </span>
            </label>
            <span> 약관 보기</span>
          </v-row>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-btn id="emailbtn" width="100%" @click="checkJoin" v-if="!this.codesent"
          >이메일 인증</v-btn
        >
      </v-card-actions>

      <v-card-text id="joinWithCode" v-if="codesent">
        <v-form @submit.prevent>
          <v-text-field
            label="Code"
            name="code"
            v-model="inputCode"
            prepend-icon="mdi-lock"
            type="text"
          ></v-text-field>
        </v-form>
      </v-card-text>
      <v-card-actions v-if="codesent">
        <v-btn id="joinbtn" width="100%" @click="join">가입하기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import axios from "axios";
import Swal from 'sweetalert2'
export default {
  components: {
    Swal
  },
  data: () => {
    return {
      dialog: false,
      email: "",
      uid: "",
      password: "",
      passwordConfirm: "",
      introduce: "",
      isTerm: false,
      profile: null,
      inputCode: "",
      codesent: false
    };
  },
  methods: {
    closeDialog() {
      document.getElementById("Email").disabled = false;
      this.codesent = false;
      this.dialog = false;
      Object.assign(this.$data, this.$options.data());
    },
    certification() {
      if (this.password != this.passwordConfirm) {
        alert("암호와 확인암호가 일치하지 않습니다.");
        return;
      }
      if (this.isTerm) {
        //동의 체크 -> 인증메일 발송
        axios({
          method: "POST",
          url: this.$baseurl + "/account/certification/1",
          data: {
            email: this.email
          }
        })
          .then(() => {
            //인증코드 발송 완료
            alert("코드가 전송되었습니다. 이메일을 확인해주세요.");
            this.codesent = true;
            document.getElementById("Email").disabled = true;
          })
          .catch(err => {
            //인증코드 발송 실패
            console.log(err);
          });
      } else {
        // 약관 동의 안했을 경우
        alert("약관에 동의해주세요.");
      }
    },
     checkJoin() {
      //항목들이 다 입력 되어있는지 확인
      let count = 0;
      if (this.uid == "") {
        if (count == 0) {
          alert("닉네임을 입력해주세요!");
        }
        count++;
      }
      if (this.email == null) {
        //정규식 필요
        if (count == 0) {
          alert("이메일을 입력해주세요");
        }
        count++;
      }
      var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
      // 검증에 사용할 정규식 변수 regExp에 저장
      if (!this.email.match(regExp)) {
        if (count == 0) {
          alert("이메일 양식을 확인해주세요! (예):abc@abc.com");
          this.email = "";
        }
        count++;
      }
      if (this.isTerm == false) {
        if (count == 0) {
          alert("약관에 동의해주세요.");
        }
        count++;
      }
      if (this.password == null) {
        if (count == 0) {
          alert("비밀번호를 입력해주세요.");
        }
        count++;
      }
      // : 숫자, 특문 각 1회 이상, 영문은 2개 이상 사용하여 8자리 이상 입력
      var regExpPw = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$/;
      if (!this.password.match(regExpPw)) {
        if (count == 0) {
          alert(
            "비밀번호 양식을 확인해주세요! (예) 8자이상 (숫자 1회, 특수문자 1회, 영문 2회 이상"
          );
          this.password = "";
          this.passwordConfirm = "";
        }
        count++;
      }
      if (this.passwordConfirm == null) {
        if (count == 0) {
          alert("비밀번호를 재입력해주세요.");
          this.passwordConfirm = "";
        }
        count++;
      }
      if (this.password != this.passwordConfirm) {
        if (count == 0) {
          alert("비밀번호를 일치시켜주세요.");
          this.password = "";
          this.passwordConfirm = "";
        }
        count++;
      }
      if (count == 0) {
        this.certification();
      }
    },
    join() {
      //회원가입
      if (this.uid != "") {
        axios({
          method: "POST",
          url: this.$baseurl + "/account/signup",
          contentType: "application/json",
          data: {
            uid: this.uid,
            email: this.email,
            password: this.password,
            introduce: this.introduce,
            code: this.inputCode
          }
        })
          .then(() => {
            Swal.fire({
              position: 'center',
              icon: 'success',
              title: '회원가입에 성공하였습니다.',
              showConfirmButton: false,
              timer: 1500
            })
            this.closeDialog();
          })
          .catch(() => {
            Swal.fire({
            icon: 'error',
            title: '회원가입 실패',
            text: '회원가입에 실패하였습니다.',
          })
            this.codesent = false;
          })
          .finally(() => {
            document.getElementById("Email").disabled = false;
          });
      } else {
        alert("닉네임을 입력해주세요.");
      }
    }
  }
};
</script>
<style scoped>
#emailbtn{
  background-color: rgb(115, 50, 20);
  color:  white;
}
#joinbtn{
  background-color: rgb(115, 50, 20);
  color:  white;
}
#toolbar {
  background-color: white;
}
#card{
  background-color: white;
}
</style>
