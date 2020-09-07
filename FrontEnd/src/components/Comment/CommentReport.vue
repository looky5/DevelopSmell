<template>
  <v-dialog v-model="dialog" persistent max-width="400">
    <template v-slot:activator="{ on, attrs }">
      <v-btn v-bind="attrs" v-on="on" class="btn">
       신고
      </v-btn>
    </template>
    <v-card id="card">
      <v-toolbar id="toolbar" flat>
        <v-toolbar-title>Report</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-icon class="material-icons" @click="dialog = false">mdi-close</v-icon>
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
        <v-btn id="reportbtn"  @click="report(commentno)">Report</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>


<script>
import axios from "axios";
import Swal from 'sweetalert2'
import { mapState, mapGetters } from "vuex";

export default {
  data: function() {
    return {
      dialog: false,
      content: null
    };
  },
  props: ["commentno"],
  computed: {
    ...mapState({
      loginUserNo: state => state.userm.userno
    })
  },

  methods: {
    report(commentno) {
      let userno = this.$store.state.userm.userno;
      // let commentno = this.commentno;
      console.log(userno)
      console.log(commentno)
      axios({
        method: "POST",
        url: this.$baseurl+"/comment/report/",

        data: {
          userno: userno,
          commentno: commentno,
          content: this.content
        },
        headers: {
          Authorization: this.$store.state.userm.token
        }
      })
        .then(res => {
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
        .catch(res => {
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
.theme--light.v-btn:not(.v-btn--flat):not(.v-btn--text):not(.v-btn--outlined) {
    background-color: rgba(255, 255, 255, 0.12);
    width: 6.5rem;
}
dropdown-menu.show {
    height: auto;
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