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
    redirect: "/question/view",
    meta: {
      hide: true,
    },
  },
  {
    path: "/question",
    redirect: "/question/view",
    name: "题目",
    // component: () => import("../views/HomeView.vue"),
    meta: {
      access: AccessEnum.NotLogin,
    },
    children: [
      {
        path: "/question/view",
        name: "浏览题目",
        component: () => import("../views/question/QuestionsView.vue"),
        meta: {
          access: AccessEnum.NotLogin,
        },
      },
      {
        path: "/question/view/:id",
        name: "做题",
        component: () => import("../views/question/DoQuestionView.vue"),
        meta: {
          access: AccessEnum.User,
          hide: true,
        },
      },
      {
        path: "/question/submit/view",
        name: "题目提交记录",
        component: () => import("../views/question/QuestionSubmitView.vue"),
        meta: {
          access: AccessEnum.User,
        },
      },
      {
        path: "/question/submit/view/:id",
        name: "查看提交记录",
        component: () => import("../views/question/ViewQuestionSubmitView.vue"),
        meta: {
          access: AccessEnum.User,
          hide: true,
        },
      },
      {
        path: "/question/manage",
        name: "管理题目",
        component: () => import("../views/question/ManageQuestionView.vue"),
        meta: {
          access: AccessEnum.User,
        },
      },
      {
        path: "/question/add",
        name: "创建题目",
        component: () =>
          import("../views/question/AddOrUpdateQuestionView.vue"),
        meta: {
          access: AccessEnum.User,
          hide: true,
        },
      },
      {
        path: "/question/update",
        name: "修改题目",
        component: () =>
          import("../views/question/AddOrUpdateQuestionView.vue"),
        meta: {
          access: AccessEnum.User,
          hide: true,
        },
      },
    ],
  },
  {
    path: "/noauth",
    name: "无权限",
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
