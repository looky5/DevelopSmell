<template>
  <div class="wrapB search">
    <!-- 검색창 -->
    <div>
      <b-form-input id="searchinput"
        v-model="word"
        size="sm"
        class="mt-3"
        placeholder="검색어를 입력해주세요!"
      ></b-form-input>
    </div>
    <!-- 테그 -->
    <div>
      <b-form-group>
        <b-form-tags id="formtag" v-model="value" no-outer-focus class="mb-2">
          <template v-slot="{ tags, disabled, addTag, removeTag }">
            <ul v-if="tags.length > 0" class="list-inline d-inline-block mb-2">
              <li v-for="tag in tags" :key="tag" class="list-inline-item">
                <b-form-tag
                  @remove="removeTag(tag)"
                  :title="tag"
                  :disabled="disabled"
                  variant="info"
                  >{{ tag }}</b-form-tag
                >
              </li>
            </ul>

            <b-dropdown
              size="sm"
              variant="warning"
              block
              menu-class="w-100"
            >
              <template id="tag" v-slot:button-content> 
                <b-icon icon="tag-fill"></b-icon> 태그 선택
              </template>
              <b-dropdown-form @submit.stop.prevent="() => {}">
                <b-form-group
                  label-for="tag-search-input"
                  label="태그 검색"
                  label-cols-md="auto"
                  class="mb-0"
                  label-size="sm"
                  :description="searchDesc"
                  :disabled="disabled"
                >
                  <b-form-input
                    v-model="search"
                    id="tag-search-input"
                    type="search"
                    size="sm"
                    autocomplete="off"
                  ></b-form-input>
                </b-form-group>
              </b-dropdown-form>
              <b-dropdown-divider></b-dropdown-divider>
              <b-dropdown-item-button
                v-for="option in options"
                :key="option"
                @click="onOptionClick({ option, addTag })"
              >
                {{ option }}
              </b-dropdown-item-button>
              <b-dropdown-text v-if="options.length == 0">
                검색 조건에 맞는 태그가 존재하지 않습니다.
              </b-dropdown-text>
            </b-dropdown>
          </template>
        </b-form-tags>
      </b-form-group>
    </div>
  </div>
</template>
<script>
import axios from "axios";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import _ from "lodash";

export default {
  data() {
    return {
      //검색
      word: "",
      //검색된 태그들
      options: [],
      //search는 태그 검색
      search: "",
      //태그를 검색할때 선택한 태그들을 저장
      value: [],
      //로그인한 유저 정보
      userno: this.$store.state.userm.userno
    };
  },
  computed: {
    criteria: function() {
      if (this.search != "") {
        // eslint-disable-next-line vue/no-async-in-computed-properties
        axios({
          method: "GET",
          url: this.$baseurl + "/post/search/" + this.search
        }).then(res => {
          this.options = res.data.object;
        });
      }else{
        this.options = "";
      }

      return this.search.trim();
    },
    searchDesc: function() {
      if (this.criteria && this.options.length == 0) {
        return "There are no tags matching your search criteria";
      }
      return "";
    }
  },
  watch: {
    word: _.debounce(function() {
      this.searchword();
      this.$emit("resetList");
    }, 1000),
    value: _.debounce(function() {
      this.searchword();
      this.$emit("resetList");
    }, 1000)
  },
  methods: {
    searchword() {
      this.$store.state.search.tags = this.value;
      this.$store.state.search.word = this.word;
    },
    onOptionClick({ option, addTag }) {
      addTag(option);
      this.search = "";
    }
  }
};
</script>
<style>
.dropdown-menu {
  height: 285px;
  overflow-y: auto;
  overflow-x: hidden;
}
.search {
  margin: 20px;
  width: 300px;
}
@media (min-width: 576px) {
  .search {
    width: 300px;
  }
}
@media (min-width: 768px) {
  .search {
    width: 640px;
  }
}
@media (min-width: 992px) {
  .search {
    width: 640px;
  }
}
@media (min-width: 1200px) {
  .search {
    width: 640px;
  }
}
@media (min-width: 1440px) {
  .search {
    width: 980px;
  }
}
#formtag{
  background-color: rgb(240, 162, 46);
}
.btn-warning {
    color: rgb(115, 50, 20);
    background-color: rgb(240, 162, 46);
    border-color: rgb(240, 162, 46);
}
#tag{
  color: rgb(115, 50, 20);
}
#searchinput{
  margin-bottom: 10px !important;
}
</style>
