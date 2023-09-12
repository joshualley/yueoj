<template>
  <div id="globalHeader">
    <a-row align="center" :wrap="false">
      <a-col flex="auto">
        <a-menu
          mode="horizontal"
          accordion
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
              <div class="title">YOJ</div>
            </div>
          </a-menu-item>
          <a-sub-menu v-for="item in menus" :key="item.path" selectable>
            <template #title>{{ item.name }}</template>
            <template v-if="item.children !== undefined">
              <a-menu-item v-for="citem in item.children" :key="citem.path">
                {{ citem.name }}
              </a-menu-item>
            </template>
            <template v-else>
              <a-menu-item :key="item.path">{{ item.name }}</a-menu-item>
            </template>
          </a-sub-menu>
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
import { RouteRecordRaw, useRouter } from "vue-router";
import { ref } from "vue";
import { useStore } from "vuex";
import { State } from "@/store/type";
import checkAccess from "@/access/CheckAccess";
import AccessEnum from "@/access/AccessEnum";

const router = useRouter();
const store = useStore<State>();
const selectedKeys = ref(["/"]);
const loginUser = ref(store.state.user?.loginUser);

// 过滤隐藏的菜单项
const menus = ref<RouteRecordRaw[]>([]);

/**
 * 菜单点击事件
 * @param path
 */
const onMenuClick = (path: string) => {
  router.push({
    path: path,
  });
};

/**
 * 过滤调无权限或者隐藏的菜单项
 * @param item
 */
const filterMenu = (item: RouteRecordRaw): boolean => {
  if (item.meta?.hide) {
    return false;
  }
  const access: AccessEnum = (item?.meta?.access ??
    AccessEnum.NotLogin) as AccessEnum;
  if (!checkAccess(loginUser.value, access)) {
    return false;
  }
  return true;
};

router.afterEach((to) => {
  selectedKeys.value = [to.path];
  // 请求后获取用户及有权限的菜单
  loginUser.value = store.state.user?.loginUser;
  menus.value = routes.filter(filterMenu);
  // 检查2级菜单的权限
  menus.value.forEach((submenu) => {
    if (submenu.children) {
      submenu.children = submenu.children.filter(filterMenu);
    }
  });
});
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
