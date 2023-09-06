import AccessEnum from "@/access/AccessEnum";
import { RouteRecordRaw } from "vue-router";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/user",
    name: "用户",
    component: import("../layouts/UserLayout.vue"),
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
  {
    path: "/",
    name: "浏览题目",
    component: () => import("../views/HomeView.vue"),
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
    path: "/admin",
    name: "管理员",
    component: () => import("../views/AdminView.vue"),
    meta: {
      access: AccessEnum.Admin,
    },
  },
  {
    path: "/about",
    name: "关于我的",
    component: () => import("../views/AboutView.vue"),
  },
];

export default routes;
