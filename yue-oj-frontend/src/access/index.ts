import router from "@/router";
import store from "@/store";
import AccessEnum from "./AccessEnum";
import checkAccess from "./CheckAccess";

router.beforeEach(async (to, from, next) => {
  // 自动登录
  let loginUser = store.state.user.loginUser;
  if (!loginUser || loginUser.userRole === AccessEnum.NotLogin) {
    await store.dispatch("getLoginUser");
    loginUser = store.state.user.loginUser;
  }
  // 判断权限
  const needAccess = (to.meta?.access ?? AccessEnum.NotLogin) as AccessEnum;
  if (needAccess !== AccessEnum.NotLogin) {
    if (!loginUser || loginUser.userRole == AccessEnum.NotLogin) {
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
