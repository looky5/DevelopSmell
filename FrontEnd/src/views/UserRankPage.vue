<template>
  <div class="userRank">
    <div>
      <b-table class="userRankTable" hover :items="items" :fields="rankfields">
        <template v-slot:cell(userrating)="{ item }">
          <b-img :src="item.userrating" fluid class="tierImg"></b-img>
        </template>
        <template v-slot:cell(nickname)="{ item }">
          <div v-on:click=" goBlog(item.uid)" style="cursor:pointer"> 
          <b-img
            :src="item.nickname.profilename"
            v-if="item.nickname.profilename"
   
            class="profileImg"
          >
          </b-img>
          <span class="id-span">{{ item.uid }}</span>
          </div>
        </template>
      </b-table>
    </div>
    <infinite-loading
      @infinite="rankinfiteHandler"
      ref="infiniteLoad"
      spinner="waveDots"
    >
      <div slot="no-more"></div>
      <div slot="no-results">등록된 유저가 없습니다.</div>
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
      limit: 0,
      items: [],
      ranklimit: 0,
      // Note 'isActive' is left out and will not appear in the rendered table
      //신고글에 따른 빈도수가 필요
      rankfields: [
        {
          key: "userrank",
          label: "순위",
          sortable: true
        },
        {
          key: "userrating",
          label: "등급",
          sortable: false
        },
        {
          key: "userscore",
          label: "점수",
          sortable: false
        },
        {
          key: "nickname",
          label: "닉네임",
          sortable: true
        }
      ]
    };
  },
  methods: {
    goBlog(uid){
      this.$router.push("/userblog/"+uid);
    },
    rankinfiteHandler($state) {
      axios({
        method: "GET",
        url: this.$baseurl + "/user/rank/" + this.limit
      })
        .then(res => {
          setTimeout(() => {
            if (res.data.object.length) {
              let data = res.data.object;

              for (let i = 0; i < data.length; i++) {
                let tmp = data[i];
                let nickname = { uid: tmp.uid, profilename: tmp.profilename };
                data[i].nickname = nickname;
                this.items.push(data[i]);
              }

              $state.loaded();
              this.limit += 5;
              if (data.length % 5 != 0) {
                $state.complete();
              }
            } else {
              $state.complete();
            }
          }, 500);
        })
        .catch(error => {
          console.error(error);
        });
    }
  }
};
</script>

<style>
.userRank {
  width: 100%;
  padding: 5%;
}
.userRankTable {
  text-align: center;
}
.userRankTable th {
  background-color: rgb(240, 162, 46);
  color:  rgb(115, 50, 20);
}
.userRankTable td {
  vertical-align: middle;
}
.tierImg {
  width: 10vw;
  max-width: 80px;
}
.profileImg {
  width: 3rem;
  height: 3rem;
  float: left;
  margin: 0 0 0 25%;
}
@media (max-width: 768px) {
  .profileImg {
    margin: 0 0 0 5%;
  }
}
.id-span {
  padding-left: 1rem;
}
.id-span {
  float: left;
  padding: 0 0 0 10%;
}
</style>
