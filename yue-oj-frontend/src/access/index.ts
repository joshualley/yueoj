import router from "@/router";
import store from "@/store";
import AccessEnum from "./AccessEnum";
import checkAccess from "./CheckAccess";

router.beforeEach(async (to, from, next) => {
  console.log("跳转页面：", to);
  // 自动登录
  const loginUser = store.state.user.loginUser;
  if (!loginUser || loginUser.userRole === AccessEnum.NotLogin) {
    await store.dispatch("getLoginUser");
  }

  // 判断权限
  const needAccess = (to.meta?.access ?? AccessEnum.NotLogin) as AccessEnum;
  if (needAccess === AccessEnum.NotLogin) {
    next();
    return;
  } else {
    if (!loginUser) {
      next(`/user/login?redirect=${to.fullPath}`);
      return;
    }
    if (!checkAccess(loginUser, needAccess)) {
      next("/noauth");
      return;
    }
  }

  next();
});
