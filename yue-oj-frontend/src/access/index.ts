import router from "@/router";
import store from "@/store";

router.beforeEach((to, from, next) => {
  if (
    to.meta?.access === "canAdmin" &&
    store.state.user?.loginUser?.userRole !== "admin"
  ) {
    next("/noauth");
    return;
  }
  next();
});
