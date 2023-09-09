<template>
  <div id="globalHeader">
    <a-row align="center" :wrap="false">
      <a-col flex="auto">
        <a-menu
          mode="horizontal"
          :selected-keys="selectedKeys"
          @menu-item-click="onMenuClick"
        >
          <a-menu-item
            key="0"
            :style="{ padding: 0, marginRight: '38px' }"
            disabled
          >
            <div class="title-bar">
              <img class="logo" src="../assets/logo.png" alt="logo" />
              <div class="title">YUE-OJ</div>
            </div>
          </a-menu-item>
          <a-menu-item v-for="item in menus" :key="item.path">
            {{ item.name }}
          </a-menu-item>
        </a-menu>
      </a-col>
      <a-col flex="100px">
        <div v-if="loginUser?.userRole !== AccessEnum.NotLogin">
          {{ loginUser?.userName }}
        </div>
        <div v-else>
          <a href="/user/login">未登录</a>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import routes from "../router/routes";
import { useRouter } from "vue-router";
import { ref } from "vue";
import { useStore } from "vuex";
import { State, User } from "@/store/type";
import checkAccess from "@/access/CheckAccess";
import AccessEnum from "@/access/AccessEnum";

const router = useRouter();
const store = useStore<State>();
const selectedKeys = ref(["/"]);
const loginUser = ref(store.state.user?.loginUser);

console.log(loginUser);

// 过滤隐藏的菜单项
const menus = routes.filter((item) => {
  if (item.meta?.hide) {
    return false;
  }
  const access: AccessEnum = (item?.meta?.access ??
    AccessEnum.NotLogin) as AccessEnum;
  if (!checkAccess(loginUser.value, access)) {
    return false;
  }
  return true;
});

router.afterEach((to) => {
  selectedKeys.value = [to.path];
});

const onMenuClick = (path: string) => {
  console.log(path);
  router.push({
    path: path,
  });
};
</script>

<style scoped>
.title-bar {
  display: flex;
  align-items: center;
}
.logo {
  height: 48px;
}
.title {
  color: black;
  margin: 0 10px;
}
</style>
