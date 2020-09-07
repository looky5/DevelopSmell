import Vue from "vue";
import VueRouter from "vue-router";

import MainPage from "../views/MainPage.vue";
import BlogPage from "../views/BlogPage.vue";
import ModifyPage from "../views/ModifyPage.vue";
import LikePage from "../views/LikePage.vue";
import RankPage from "@/views/UserRankPage.vue";
import PostDetail from "@/components/Post/PostDetail.vue";
import ReportPage from "@/views/ReportPage.vue";
import LatestPage from "@/views/LatestPage.vue";

import UserBlogPage from "@/views/UserBlogPage.vue";

import PostRegist from "@/components/Post/PostRegist.vue";
import PostbyTag from "../views/PostsByTagPage.vue";


import PostModify from "@/components/Post/PostModify.vue";
// import ErrorPage from '@/page/error/ErrorPage'
import PageNotFound from '@/components/Error/PageNotFound'
/*
import LikePage from "@/components/Page/LikePage.vue";


import PostRegist from "@/components/Article/PostRegist.vue";


import DetailPage from "@/components/Article/DetailPost.vue";
*/
Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "MainPage",
    component: MainPage
  },
  { 
    path : "/post/regist",
    name : 'postregist',
    component : PostRegist
  },
  {
    path : "/post/modify/:postno",
    name : "postmodify",
    component : PostModify
  },
  {
    path: "/myblog/:uid",
    name: "blog",
    component: BlogPage
  },
  {
    path: "/post/detail/:postno",
    name: "postdetail",
    component: PostDetail
  },
  {
    path: "/modify",
    name: "usermodify",
    component: ModifyPage
  },
  {
    path: "/rank",
    name: "rankpage",
    component: RankPage
  },
  {
    path: "/report",
    name: "reportpage",
    component: ReportPage
  },
  {
    path: "/post/like",
    name: "likepage",
    component: LikePage
  },
  {
    path: "/userblog/:bloguser",
    name: "UserBlogPage",
    component: UserBlogPage
  },
  {
    path: "/post/tag/:tag",
    name: "postbyTag",
    component: PostbyTag
  },
  // {
  //   path: "/user/profile/:userno",
  //   name: "userprofile",
  //   component: UserProfile
  // },
  {
    path:'*',
    redirect:'/404'
  },
  {
    path:'/404',
    component: PageNotFound
  },

  {
    path : '/latest',
    name: "latestpage",
    component : LatestPage
  },
  /*

  {
    path : '/post/regist',
    name : "postregist",
    component : PostRegist
  },
  {
    path : '/post/like',
    component : LikePage
  },
  */
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

export default router;
