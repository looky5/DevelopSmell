<template>
  <v-dialog v-model="dialog" persistent max-width="400">
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        class="mr-10 float-right"
        color="primary"
        dark
        icon
        v-bind="attrs"
        v-on="on"
      >
        비밀번호를 잃어버리셨나요?
      </v-btn>
    </template>
    <v-card id="card">
      <v-toolbar id="toolbar" flat>
        <v-toolbar-title>비밀번호 변경</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-icon class="material-icons" @click="closeDialog"> mdi-close</v-icon>
      </v-toolbar>
      <v-card-text id="certification">
        <v-form @submit.prevent>
          <v-text-field
            label="Email"
            name="email"
            v-model="email"
            prepend-icon="mdi-email"
            type="text"
            id="Email"
          ></v-text-field>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-btn id="emailbtn" width="100%" @click="sendCode" v-if="!this.emailsent"
          >이메일로 인증코드 전송</v-btn
        >
      </v-card-actions>
      <v-card-text id="modpw" v-if="emailsent">
        <v-form @submit.prevent>
          <v-text-field
            label="code"
            name="code"
            v-model="code"
            prepend-icon="mdi-lock"
            type="text"
          ></v-text-field>
          <v-text-field
            label="password"
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
        </v-form>
      </v-card-text>
      <v-card-actions v-if="emailsent">
        <v-btn id="passwordbtn" width="100%" @click="modifyPW"
          >비밀번호 변경</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import axios from "axios";
export default {
  data: () => {
    return {
      emailsent: false,
      dialog: false,
      email: "",
      password: "",
      passwordConfirm: "",
      code: ""
    };
  },
  methods: {
    closeDialog() {
      document.getElementById("Email").disabled = false;
      this.emailsent = false;
      this.dialog = false;
      Object.assign(this.$data, this.$options.data());
    },
    sendCode() {
      axios({
        method: "POST",
        url: this.$baseurl + "/account/certification/0",
        data: {
          email: this.email
        }
      })
        .then(res => {
          let data = res.data.data;
          if (data == "success") {
            alert("해당 이메일로 인증코드가 전송되었습니다.");
            document.getElementById("Email").disabled = true;
            this.emailsent = true;
          } else if (data == "noEmail") {
            alert("해당 이메일이 존재하지 않습니다.");
          }
        })
        .catch(err => {
          alert("해당 이메일을 찾지 못했습니다.");
          console.log(err);
        });
    },
    modifyPW() {
      if (this.password == this.passwordConfirm) {
        axios({
          method: "PUT",
          url: this.$baseurl + "/account/changepw",
          headers: { "Content-Type": "application/json" },
          data: {
            email: this.email,
            password: this.password,
            code: this.code
          }
        })
          .then(() => {
            alert("비밀번호가 변경되었습니다. 다시 로그인해주세요.");
            this.closeDialog();
          })
          .catch(() => {
            alert("비밀번호 변경에 실패하였습니다. 다시 시도해주세요.");
          });
      } else {
        alert("입력한 두 비밀번호가 서로 같지 않습니다.");
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
#passwordbtn{
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
