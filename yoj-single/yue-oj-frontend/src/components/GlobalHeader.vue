<template>
  <div class="global-header">
    <a-card>
      <a-row align="center" :wrap="false">
        <a-col flex="auto">
          <a-menu
            mode="horizontal"
            accordion
            :selected-keys="selectedKeys"
            @menu-item-click="onMenuClick"
          >
            <a-menu-item
              key="oj"
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
            <a-popover>
              <a-avatar :style="{ background: '#14a9f8' }">
                {{ loginUser?.userName }}
              </a-avatar>
              <template #content>
                <a-doption value="/user/info">个人信息</a-doption>
                <a-doption value="/user" @click="onLogoutClick">登出</a-doption>
              </template>
            </a-popover>
          </template>
          <template v-else>
            <a-button
              type="outline"
              @click="() => router.replace('/user/login')"
            >
              登录
            </a-button>
          </template>
        </a-col>
      </a-row>
    </a-card>
    <a-space style="margin: 15px 0 0 0">
      <a-button type="outline" @click="() => router.back()">
        <template #icon>
          <icon-arrow-left />
        </template>
      </a-button>
      <a-breadcrumb>
        <a-breadcrumb-item>
          {{ router.currentRoute.value.matched[0]?.name ?? "" }}
        </a-breadcrumb-item>
        <a-breadcrumb-item> {{ currPath?.name ?? "" }} </a-breadcrumb-item>
      </a-breadcrumb>
    </a-space>
  </div>
</template>

<script setup lang="ts">
import routes from "../router/routes";
import { RouteLocationNormalized, RouteRecordRaw, useRouter } from "vue-router";
import { ref } from "vue";
import { useStore } from "vuex";
import { State } from "@/store/type";
import checkAccess from "@/access/CheckAccess";
import AccessEnum from "@/access/AccessEnum";
import { UserControllerService } from "@/api";
import { onMounted } from "vue";

const router = useRouter();
const store = useStore<State>();
const selectedKeys = ref(["/"]);
const currPath = ref<RouteLocationNormalized>();
const fromPath = ref<RouteLocationNormalized>();
const loginUser = ref(store.state.user?.loginUser);

// 过滤隐藏的菜单项
const menus = ref<RouteRecordRaw[]>([]);

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
  // console.log("filterMenu", item.name, access, r);
  return checkAccess(loginUser.value, access);
};

/**
 * 加载菜单
 */
const loadMenus = async () => {
  // 获取当前登录用户
  let user = store.state.user.loginUser;
  if (!user || user.userRole === AccessEnum.NotLogin) {
    await store.dispatch("getLoginUser");
    user = store.state.user.loginUser;
  }
  loginUser.value = user;
  // console.log("loadMenus", routes);
  let fliterMenus = routes.filter(filterMenu);
  // 检查2级菜单的权限
  for (let i = 0; i < fliterMenus.length; i++) {
    if (fliterMenus[i].children) {
      fliterMenus[i].children = fliterMenus[i].children?.filter(filterMenu);
    }
  }
  menus.value = fliterMenus;
};

/**
 * 用户登出
 */
const onLogoutClick = async () => {
  await UserControllerService.userLogoutUsingPost();
  router.push({
    path: "/user/login",
    replace: true,
  });
};

/**
 * 菜单点击事件
 * @param path
 */
const onMenuClick = (path: string) => {
  router.push({
    path: path,
  });
};

onMounted(() => {
  loadMenus();
});

router.afterEach((to, from) => {
  // loadMenus();
  currPath.value = to;
  fromPath.value = from;
  selectedKeys.value = [to.path];
});
</script>

<style scoped>
.global-header {
  max-width: 1280px;
  margin: 0 auto;
}
.title-bar {
  display: flex;
  align-items: center;
}
.logo {
  height: 40px;
}
.title {
  color: black;
  margin: 0 10px;
}
</style>
