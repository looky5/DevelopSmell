<template>
  <v-container class="mt-15">
    <v-simple-table fixed-header>
      <template v-slot:default>
        <tbody>
          <tr>
            <td>
              <v-list-item-avatar class="float-left">
                <img :src="profile" />
              </v-list-item-avatar>

              <v-file-input
                class="float-left"
                v-model="file"
                :src="file"
                id="file"
                accept="image/png, image/jpeg, image/bmp"
                placeholder="Pick an profile"
                label="프로필 사진"
              ></v-file-input>
            </td>
          </tr>
          <tr>
            <td class="text-left">
              <v-text-field
                label="nickname"
                name="nickname"
                v-model="nickname"
                prepend-icon="mdi-account"
                type="text"
              ></v-text-field>
            </td>
          </tr>
          <tr>
            <td class="text-left">
              <v-text-field
                label="email"
                name="email"
                v-model="email"
                prepend-icon="mdi-email"
                type="text"
                :disabled="true"
              ></v-text-field>
            </td>
          </tr>
          <tr>
            <td class="text-left">
              <v-text-field
                id="password"
                label="Password"
                name="password"
                v-model="password"
                prepend-icon="mdi-lock"
                type="password"
              ></v-text-field>
            </td>
          </tr>
          <tr>
            <td class="text-left">
              <v-text-field
                label="Introduce"
                name="introduce"
                v-model="introduce"
                prepend-icon="mdi-account"
                type="text"
              ></v-text-field>
            </td>
          </tr>

          <tr>
            <v-spacer></v-spacer>
            <v-btn @click="modify">MODIFY</v-btn>
            <v-btn @click="reset">RESET</v-btn>
          </tr>
        </tbody>
      </template>
    </v-simple-table>
  </v-container>
</template>
<script>
import axios from "axios";
export default {
  data: function() {
    return {
      profile: null,
      email: this.email,
      password: null,
      uid: this.nickname,
      nickname: this.nickname,
      introduce: this.introduce,
      file: null
    };
  },
  computed: {},
  created() {
    //회원 정보 가져오기

    axios({
      method: "GET",
      url: this.$baseurl + "/user/" + this.$store.state.userm.userno,
      headers: {
        Authorization: this.$store.state.userm.token
      }
    }).then(res => {
      let data = res.data.object;
      this.email = data.email;
      this.uid = data.nickname;
      this.nickname = data.uid;
      this.introduce = data.introduce;
      this.profile = data.profilename;
    });
  },
  methods: {
    modify() {
      if (this.password != null) {
        let formData = new FormData();

        formData.append("profile", this.file);
        let userInfo = {
          userno: this.$store.state.userm.userno,
          email: this.email,
          uid: this.nickname,
          introduce: this.nickname,
          password: this.password
        };

        formData.append("user", JSON.stringify(userInfo));
        axios({
          method: "PUT",
          url: this.$baseurl + "/user",
          contentType: "multipart/form-data",
          headers: {
            Authorization: this.$store.state.userm.token
          },
          data: formData
        }).then(() => {
          alert("정보가 수정되었습니다.");
        });
      } else {
        alert("비밀번호를 입력해주세요");
      }
    },
    reset() {
      this.nickname = null;
      this.email = null;
      this.password = null;
      this.introduce = null;
    }
  }
};
</script>
