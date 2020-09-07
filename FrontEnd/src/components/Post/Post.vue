<template>
  <v-hover>
    <v-card
      width="300px"
      height="420px"
      class="ma-5 card"
      slot-scope="{ hover }"
      :class="`elevation-${hover ? 10 : 2}`"
      @click="detail(postno)"
      
    >
      <v-img
        :src="postrating"
        height="70"
        class="float-right"
        style="position:absolute; right:-30px; top:-30px"
        width="70"
      ></v-img>
   
      <v-list-item>
        <v-list-item-avatar>
          <img :src="profilename" />
        </v-list-item-avatar>
        <v-list-item-content>
          <div class="over">
            <b>{{ title }}</b>
          </div>
          <v-list-item-subtitle>by {{ nickname }} </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <div class="subtitle">
        <v-chip
          small
          class="my-2 mx-1"
          color="indigo darken-3"
          outlined
          v-for="(tag, index) in tags"
          :key="index"
          v-on:click.prevent.self
          @click="searchbyTag(tag, $event)"
        >
          {{ tag }}
        </v-chip>
      </div>

      <v-img height="150" :src="thumbnail"></v-img>

      <v-card-text class="subtitle">
        {{ subtitle }}
      </v-card-text>

      <v-card-text> - {{ createdate }} // {{save}}</v-card-text>
      <v-card-actions class="mt-2">
        <v-spacer></v-spacer>

        <span class="mr-2">
          <v-icon v-if="kind != 1 || kind == -1">mdi-thumb-up-outline</v-icon>
          <v-icon v-if="kind == 1 && isLogin">mdi-thumb-up</v-icon>
          <span class="mx-1">{{ likecount }}</span>
        </span>
        <span>
          <v-icon v-if="kind != 0 || kind == -1">mdi-thumb-down-outline</v-icon>
          <v-icon v-if="kind == 0 && isLogin">mdi-thumb-down</v-icon>
          <span class="mx-2">{{ dislikecount }}</span>
        </span>
      </v-card-actions>
    </v-card>
  </v-hover>
</template>
<script>
import { mapState } from "vuex";

export default {
  components: {},
  props: [
    "title",
    "content",
    "userno",
    "thumbnail",
    "postno",
    "nickname",
    "subtitle",
    "kind",
    "tags",
    "createdate",
    "save",
    "likecount",
    "dislikecount",
    "profilename",
    "userrating",
    "postrating"
  ],
  computed: {
    ...mapState({
      loginUserNo: state => state.userm.userno,
      isLogin: state => state.userm.isLogin
    })
  },
  data: () => {
    return {};
  },

  methods: {
    detail(postno) {
      this.$router.push("/post/detail/" + postno);
    },
    searchbyTag(tag, event) {
      this.$router.push("/post/tag/" + tag);
      event.stopPropagation();
    },
    // userprofile(userno){
    //   this.$router.push("/userblog/"+userno);
    // }
  }
};
</script>

<style scoped>
.card {
  display: inline-block;
}
.over {
  white-space: nowrap;
  width: 50%;
  overflow: hidden;
  font-size: 22px;
  text-overflow: ellipsis;
}
.subtitle {
  white-space: nowrap; 
  overflow: hidden;
  height: 45px;
  text-overflow: ellipsis;
}
</style>
