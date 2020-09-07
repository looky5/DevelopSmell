<template>
  <v-container >
     <div class="card" id="mycard"> 
        <div class="card-header">
            <img :src="userprofile" alt="Profile Image" class="profile-img" style="z-index:10">
        </div>
        <div class="card-body">
            <p class="full-name">@{{useruid}}</p>
            <!-- <p class="username">{{useremail}}</p> -->
            <!-- <p class="city">New York</p> -->
            <p class="desc">{{userintroduce}}</p>
           
        </div>
        <div class="card-footer">
            <div class="col vr">
                <span class="count">contact : {{useremail}}</span>
            </div>
        </div>
        
    </div>

       <v-divider></v-divider>
    <v-container class="con_layout">
      <div class="postlist" v-for="(item, index) in items" :key="index">
        <post id="post"
          :title="item.title"
          :content="item.content"
          :userno="item.userno"
          :postno="item.postno"
          :nickname="item.uid"
          :subtitle="item.subtitle"
          :tags="item.tags"
          :createdate="item.create_date"
          :save="item.save"
          :kind="item.kind"
          :thumbnail="item.thumbnail"
          :likecount="item.likecount"
          :dislikecount="item.dislikecount"
          :profilename="item.profilename"
          :userrating="item.userrating"
          :postrating="item.postrating"
        ></post>
      </div>
      <infinite-loading @infinite="infiniteHandler" spinner="waveDots">
        <div slot="no-more"></div>
        <div slot="no-results">등록된 게시물이 없습니다.</div>
      </infinite-loading>
    </v-container>
  </v-container>
</template>
<script>
import post from "@/components/Post/Post.vue";
import { mapState } from "vuex";
import axios from "axios";
import InfiniteLoading from "vue-infinite-loading";
export default {
  data: function() {
    return {
      items: [],
      limit: 0,
      count: 9,
      userprofile:"",
      useremail:"",
      useruid:"",
      userintroduce:"",
    };
  },
  computed: {
    ...mapState({
      userCheck: state => state.user.userno
    })
  },
  components: {
    post,
    InfiniteLoading
  },
  mounted() {
    axios({
        methos: "GET",
        // url: 'http://i3b208.p.ssafy.io:8080/blog/user/bloguser/' + this.bloguser
        url: this.$baseurl+'/user/bloguser/' + this.$route.params.bloguser
      })
        .then((resp) => {
         let data = resp.data.object;
            this.userprofile=data.profilename;
            this.useremail=data.email;
            this.useruid=data.uid;
            this.userintroduce=data.introduce;
        })
        .catch(() => {
          alert("유저 정보 불러오기 실패!")
        });
  },
  methods: {


    infiniteHandler($state) {
      let bloguser = this.$route.params.bloguser;
      this.bloguser = bloguser;
      let userno = this.$store.state.userm.userno || '0' ;
      axios({
        method: "GET",

        url:
          this.$baseurl +
          "/post/other/" +
          bloguser +
          "/" +
          userno +
          "/" +
          this.limit,
        dataType: "json"
      })
        .then(res => {
          setTimeout(() => {
            if (res.data.object.length) {
              let data = res.data.object;
              for (let i = 0; i < data.length; i++) {
                //태그
                let tags = data[i].tags;
                if (tags != null) {
                  let tagsArr = tags.split(",");
                  data[i].tags = tagsArr;
                }

                this.items.push(data[i]);
              }

              $state.loaded();
              this.limit += this.count;
              if (data.length % this.count != 0) {
                $state.complete();
              }
            } else {
              $state.complete();
            }
          }, 1000);
        })
        .catch(error => {
          console.error(error);
        });
    }
  }
};
</script>
<style scoped>
/* img{
  max-width: 100%;
  height: auto;
} */
* {
    box-sizing: border-box;
}

body {
    font-family: "Raleway";
    background-color: #e6e6e6;
}

.card {
    width: 400px;
    margin: 75px auto 0;
    background-color: #fff;
    box-shadow: 0 3px 30px rgba(0, 0, 0, .14);
    color: #444;
    font-size: 16px;
}

.card .card-header {
    position: relative;
    height: 48px;
}

.card .card-header .profile-img {
    width: 96px;
    height: 96px;
    border-radius: 1000px;
    position: absolute;
    left: 50%;
    transform: translate(-50%, -50%);
    border: 6px solid #fff;
    box-shadow: 0 0 20px rgba(0, 0, 0, .2);
}

.card .card-body {
    padding: 10px 40px;
}

.card .card-body .full-name {
    font-size: 20px;
    font-weight: 600;
    text-transform: uppercase;
    margin: 10px 0 0;
}

.card .card-body .username {
    font-size: 13px;
    color: #777;
    margin: 5px 0 0;
}

.card .card-body .city {
    font-weight: 500;
    margin: 10px 0 0;
}

.card .card-body .desc {
    line-height: 24px;
}

.card .card-footer {
    display: table;
    width: 100%;
    border-top: 1px solid #e6e6e6;
}

.card .card-footer .col {
    display: table-cell;
    padding: 5px 10px;
    font-size: 15px;
}

.card .card-footer .count {
    font-size: 18px;
    font-weight: 600;
}
#mycard{
  text-align: center;
}
.vr {
    border-right: 1px solid #e6e6e6;
}

@media screen and (max-width: 575px) {
    .card {
        width: 96%;
    }

    .card .card-body {
        padding: 10px 20px;
    }

    .card .card-footer .col {
        padding: 0 10px;
    }

    .card .card-footer .count {
        display: block;
        margin-bottom: 5px;
    }
}
.postlist {
  display: inline-block;
}

#post{
  text-align: left;
}
@media (max-width: 575px) {
  .con_layout {
    text-align: center;
  }
}
@media (min-width: 576px) {
  .con_layout {
    padding: 0 0 0 12%;
  }
}
@media (min-width: 768px) {
  .con_layout {
    padding: 0 0 0 26%;
  }
}
@media (min-width: 992px) {
  .con_layout {
    padding: 0 0 0 12%;
  }
}
@media (min-width: 1200px) {
  .con_layout {
    padding: 0 0 0 17%;
  }
}
@media (min-width: 1440px) {
  .con_layout {
    padding: 0 0 0 4%;
  }
}



</style>
