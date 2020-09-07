<template>
  <v-dialog v-model="dialog" persistent max-width="500">
    <template v-slot:activator="{ on, attrs }">
      <v-btn icon color="black" dark v-bind="attrs" v-on="on">
        <v-icon>mdi-lock-outline</v-icon>
      </v-btn>
    </template>
    <v-card class="">
      <div class="wrap-login100">
        <div></div>
        <v-spacer></v-spacer>

        <form class="login100-form validate-form" @submit.prevent>
          <v-icon
            class="material-icons float-right exitcon"
            large
            @click="dialog = false"
          >
            mdi-close</v-icon
          >
          <div class="logo_con">
          <img class="logoimage" src="../../assets/Developsmell.png" />
          </div>
          <span class="login100-form-title p-b-43"> </span>

          <div
            class="wrap-input100 validate-input"
            data-validate="Valid email is required: ex@abc.xyz"
          >
            <input
              class="input100"
              type="text"
              v-model="email"
              placeholder="Email"
            />
            <span class="focus-input100"></span>
            <!-- <span class="label-input100">Email</span> -->
          </div>

          <div
            class="wrap-input100 validate-input"
            data-validate="Password is required"
          >
            <input
              class="input100"
              type="password"
              v-model="password"
              placeholder="Password"
            />
            <span class="focus-input100"></span>
            <!-- <span class="label-input100">Password</span> -->
          </div>

          <div class="flex-sb-m w-full p-t-3 p-b-32">
            <div class="contact100-form-checkbox">
              <input
                class="input-checkbox100"
                id="ckb1"
                type="checkbox"
                name="remember-me"
              />
            </div>

            <div>
              <forgotPW></forgotPW>
            </div>
          </div>

          <div class="container-login100-form-btn">
            <button id="login" class="login100-form-btn" @click="checkLogin">
              로그인
            </button>
          </div>
          <join></join>

          <div class="text-center p-t-46 p-b-20"></div>

          <div class="login100-form-social flex-c-m">
            <img style="cursor:pointer" @click="kakaoLogin" src="../../assets/kakao_login.png"/>
          </div>
        </form>

        <div
          class="login100-more"
          style="background-image: url('images/bg-01.jpg');"
        ></div>
      </div>
    </v-card>
  </v-dialog>
</template>


<script>
import "@/assets/css/main.css";
import "@/assets/css/util.css";
import axios from "axios";
import Swal from 'sweetalert2'
import join from "@/components/User/Join.vue";
import forgotPW from "@/components/User/forgotPW.vue";
import { AUTH_REQUEST } from "../../store/actions/auth";
import { USER_SUCCESS } from "../../store/actions/user";

export default {
  components: {
    join,
    forgotPW,
    Swal
  },
  data: function() {
    return {
      dialog: false,
      email: null,
      password: null
    };
  },
  mounted() {},

  methods: {
    checkLogin() {
      let count = 0;
      var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
      // 검증에 사용할 정규식 변수 regExp에 저장
      if (!this.email.match(regExp)) {
        if (count == 0) {
          alert("이메일 양식을 확인해주세요! (예):abc@abc.com");
          this.email = "";
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
        }
        count++;
      }
      if (count == 0) {
        this.login();
      }
    },  
    login() {
      const { email, password } = this;
      this.$store
        .dispatch(AUTH_REQUEST, { email, password })
        .then(() => {
          this.dialog = false;
        })
        .finally(() => {
          location.reload();
        });
    },
    kakaoLogin() {
      // Kakao.Auth.authorize({
      //   redirectUri:${window.location.origin}/kakaocallback`
      // });
      const loginvue = this;
      Kakao.Auth.login({
        success: function(response) {
          axios({
            method: "GET",
            url:
              loginvue.$baseurl +
              "/account/kakaologin?access_token=" +
              response.access_token
          }).then(res => {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: '카카오 로그인 성공.',
                showConfirmButton: false,
                timer: 1500
            })
            loginvue.$store.commit(USER_SUCCESS, res);
            loginvue.dialog = false;
            location.reload();
          });
        },
        fail: function(error) {
            Swal.fire({
                icon: 'error',
                title: '카카로 로그인 실패',
                text: '카카오 로그인에 실패했습니다.',
            })
          console.log(error);
        }
      });
    }
  }
};
</script>

<style scoped>
/* #kakao{
    background-color: rgb(240, 162, 46);
} */
#login {
  background-color: rgb(115, 50, 20);
}
@media (max-width: 992px) {
  .login100-form {
    width: 100%;
    padding-left: 30px;
    padding-right: 30px;
  }
}
.logoimage {
  width: 50vw;
  max-width: 100%;
  text-align: center;
}
.logo_con {
  text-align: center;
}
.exitcon {
  position: absolute;
  right: 10px;
  top: 10px;
}
</style>