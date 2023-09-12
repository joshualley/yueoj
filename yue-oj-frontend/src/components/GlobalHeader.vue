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
        <template v-if="loginUser?.userRole !== AccessEnum.NotLogin">
          <a-dropdown @select="onUsernameClick">
            <div class="username-p">
              <div>{{ loginUser?.userName }}</div>
            </div>
            <template #content>
              <a-doption value="/user/info">个人信息</a-doption>
              <a-doption value="/user">登出</a-doption>
            </template>
          </a-dropdown>
        </template>
        <template v-else>
          <a href="/user/login">未登录</a>
        </template>
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

const onUsernameClick = (
  v: string | number | Record<string, any> | undefined,
) => {
  console.log(v);
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
.username-p {
  background: skyblue;
  text-align: center;
  color: white;
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 25px;
}
</style>
