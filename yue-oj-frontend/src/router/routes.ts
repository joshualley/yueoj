import AccessEnum from "@/access/AccessEnum";
import { RouteRecordRaw } from "vue-router";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/user",
    name: "用户",
    component: () => import("../layouts/UserLayout.vue"),
    meta: {
      hide: true,
    },
    children: [
      {
        path: "/user/login",
        name: "用户登录",
        component: () => import("../views/user/LoginView.vue"),
      },
      {
        path: "/user/register",
        name: "用户注册",
        component: () => import("../views/user/RegisterView.vue"),
      },
    ],
  },
  // 题目模块
  {
    path: "/",
    name: "浏览题目",
    component: () => import("../views/HomeView.vue"),
    meta: {
      access: AccessEnum.NotLogin,
    },
  },
  {
    path: "/add/question",
    name: "创建题目",
    component: () => import("../views/question/AddOrUpdateQuestionView.vue"),
    meta: {
      access: AccessEnum.Admin,
    },
  },
  {
    path: "/update/question",
    name: "修改题目",
    component: () => import("../views/question/AddOrUpdateQuestionView.vue"),
    meta: {
      access: AccessEnum.Admin,
    },
  },
  {
    path: "/manage/question",
    name: "管理题目",
    component: () => import("../views/question/ManageQuestionView.vue"),
    meta: {
      access: AccessEnum.Admin,
    },
  },

  {
    path: "/noauth",
    name: "没权限",
    component: () => import("../views/NoAuthView.vue"),
    meta: {
      hide: true,
    },
  },
  {
    path: "/about",
    name: "关于",
    component: () => import("../views/AboutView.vue"),
  },
];

export default routes;
