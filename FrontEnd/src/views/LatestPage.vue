<template>
  <v-container justify="center" class="mt-10" >
    <div id="box">
   
    </div>

    <!--
            <div class="cube">
                <div class="front">
                    <h2>Front side</h2>
                    Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
                </div>
                <div class="back">
                    <h2>Back side</h2>
                    Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
                </div>
                <div class="top">
                    <h2>Top side</h2>
                    Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
                </div>
                <div class="bottom">
                    <h2>Bottom side</h2>
                    Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
                </div>
                <div class="left">
                    <h2>Left side</h2>
                    Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
                </div>
                <div class="right">
                    <h2>Right side</h2>
                    Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
                </div>
            </div>-->
             <p class="s1">Develop Smell</p>
        
    <v-timeline :dense="$vuetify.breakpoint.smAndDown">
      <v-timeline-item v-for="(n, index) in items" :key="index">
        <template v-slot:icon>
          <v-avatar style="cursor:pointer" @click="userprofile(n.uid)">
            <!-- <v-btn @click="detail(n.postno)" > -->
            <img :src="n.profilename" />
            <!-- </v-btn> -->
          </v-avatar>
        </template>
  
        <v-card @click="detail(n.postno)" style="cursor:pointer" class="elevation-2" width="800px">
            <v-img
            height="200"
            width="475"
            :src="n.thumbnail"
          ></v-img>
          <v-card-title class="title">
            {{ n.title }}
          </v-card-title>
          <v-divider></v-divider>
          <v-card-text>
            {{ n.subtitle }}
          </v-card-text>
          <v-card-subtitle class="subtitle">
            by {{ n.uid }} - {{ n.create_date }}
          </v-card-subtitle>
        </v-card>
      </v-timeline-item>
    </v-timeline>
    <infinite-loading @infinite="infiniteHandler" spinner="waveDots">
      <div slot="no-more"></div>
      <div slot="no-results">등록된 게시물이 없습니다.</div>
    </infinite-loading>
  </v-container>
</template>
<script>
import axios from "axios";
import InfiniteLoading from "vue-infinite-loading";
export default {
  components: {
    InfiniteLoading
  },
  data: function() {
    return {
      items: [],
      limit: 0,
      count: 6
    };
  },
  methods: {
    detail(postno) {
      this.$router.push("/post/detail/" + postno);
    },
    userprofile(uid) {
       this.$router.push("/userblog/"+uid);
    },
    infiniteHandler($state) {
      axios({
        method: "GET",
        url: this.$baseurl + "/post/latest/" + this.limit,
        dataType: "json"
      }).then(res => {
        setTimeout(() => {
          if (res.data.object.length) {
            let data = res.data.object;
            console.log(data)
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
      });
    }
  }
};
</script>
<style scoped>
.title {
  white-space: nowrap; 
  overflow: hidden;
  text-overflow: ellipsis;
}
.subtitle {
  white-space: nowrap; 
  overflow: hidden;
  text-overflow: ellipsis;
}
  p {
        font-family: "Arial Black", sans-serif;
        font-size: 150px;
        font-weight: bold;
        color: #dfa812;
      }
      .s1 {
        text-shadow: 4px 4px 0px #573ee6, 8px 8px 0px #573ee6, 12px 12px 0px #E3F2FD;
        position:fixed;
        top:50%;
        transform: skewY( 20deg );
        left:16%;
        opacity: 0.3;

        
      }
  .wrap {
                margin-top: 150px;
                perspective: 1000px;
                perspective-origin: 50% 50%;
                background-image: linear-gradient(135deg,#ccc 25%,#ddd 25%,#ddd 50%, #ccc 50%, #ccc 75%,#ddd 75%,#ddd 100%);
                background-size: 80px 80px;
                border-top: solid 3px #999;
                border-bottom: solid 3px #999;
                /* Background stripes animation */
                animation: bganim 3s linear 2s infinite;
            }

            @keyframes bganim {
                from {
                    background-position: 0px;
                }

                to {
                    background-position: 80px;
                }
            }

            .cube {
                margin: auto;
            
                
                height: 200px;
                width: 200px;
                transform-style: preserve-3d;
            }

                .cube div {
                    position: absolute;
                    padding: 10px;
                    box-sizing: border-box;
                    height: 100%;
                    width: 100%;
                    opacity: 0.9;
                    background-color: #000;
                    border: solid 1px #eee;
                    color: #fff;
                    font: 10px arial;
                    transition: transform 0.2s ease-in;
                }

            .front {
                transform: translateZ(100px);
            }

            .back {
                transform: translateZ(-100px) rotateY(180deg);
            }

            .right {
                transform: rotateY(-270deg) translateX(100px);
                transform-origin: top right;
            }

            .left {
                transform: rotateY(270deg) translateX(-100px);
                transform-origin: center left;
            }

            .top {
                transform: rotateX(-270deg) translateY(-100px);
                transform-origin: top center;
            }

            .bottom {
                transform: rotateX(270deg) translateY(100px);
                transform-origin: bottom center;
            }

            @keyframes rotate {
                from {
                    transform: rotateX(0deg) rotateY(0deg);
                }

                to {
                    transform: rotateX(360deg) rotateY(360deg);
                }
            }

            .cube {
                animation: rotate 20s infinite linear;
            }

            .wrap:hover .front {
                transform: translateZ(200px);
            }

            .wrap:hover .back {
                transform: translateZ(-200px) rotateY(180deg);
            }

            .wrap:hover .right {
                transform: rotateY(-270deg) translateZ(100px) translateX(100px);
            }

            .wrap:hover .left {
                transform: rotateY(270deg) translateZ(100px) translateX(-100px);
            }

            .wrap:hover .top {
                transform: rotateX(-270deg) translateZ(100px) translateY(-100px);
            }

            .wrap:hover .bottom {
                transform: rotateX(270deg) translateZ(100px) translateY(100px);
            }



</style>
