<template>
  <v-dialog v-model="dialog" persistent max-width="400">
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        v-bind="attrs"
        v-on="on"
        class="v-btn--flat v-btn--icon v-btn--round"
      >
        <v-icon>mdi-alarm-light</v-icon>
      </v-btn>
    </template>
    <v-card id="card">
      <v-toolbar id="toolbar" flat>
        <v-toolbar-title>Report</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-icon class="material-icons" @click="dialog = false"
          >mdi-close</v-icon
        >
      </v-toolbar>
      <v-card-text>
        <v-form>
          <v-text-field
            label="신고할 내용을 입력하세요."
            name="content"
            v-model="content"
            prepend-icon="mdi-alarm-light"
            type="text"
          ></v-text-field>
        </v-form>
      </v-card-text>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn id="reportbtn" @click="report">Report</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import axios from "axios";
import Swal from 'sweetalert2'
import { mapState } from "vuex";

export default {
  data: function() {
    return {
      dialog: false,
      content: null
    };
  },
  computed: {
    ...mapState({
      loginUserNo: state => state.userm.userno
    })
  },

  methods: {
    report() {
      let userno = this.$store.state.userm.userno;
      let postno = this.$route.params.postno;
      axios({
        method: "POST",
        url: this.$baseurl + "/post/report",

        data: {
          userno: userno,
          postno: postno,
          content: this.content
        },
        headers: {
          Authorization: this.$store.state.userm.token
        }
      })
        .then(() => {
          // location.reload();
           Swal.fire({
              position: 'center',
              icon: 'success',
              title: '신고가 처리되었습니다.',
              showConfirmButton: false,
              timer: 1500
            })
          this.dialog = false;
        })
        .catch(() => {
          Swal.fire({
            icon: 'error',
            title: '신고 실패',
            text: '신고에 실패하였습니다.',
          })
        });
    }
  }
};
</script>

<style scoped>
.v-btn--contained {
  box-shadow: 0px 0px 0px 0px rgba(0, 0, 0);
}
#reportbtn{
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
