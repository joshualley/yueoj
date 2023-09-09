<template>
  <div class="login-view">
    <a-form :model="form" @submit="onLogin" label-align="left" auto-label-width>
      <a-form-item class="form-item" field="userAccount" label="账号">
        <a-input v-model="form.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item
        class="form-item"
        field="userPassword"
        tooltip="密码不少于8位"
        label="密码"
      >
        <a-input-password
          v-model="form.userPassword"
          placeholder="请输入密码"
        />
      </a-form-item>
      <a-form-item>
        <a-button style="width: 100px" type="primary" html-type="submit"
          >登录</a-button
        >
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { UserControllerService } from "@/api";
import { Message } from "@arco-design/web-vue";
import { reactive } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

const router = useRouter();
const store = useStore();

const form = reactive({
  userAccount: "",
  userPassword: "",
});

const onLogin = async () => {
  const resp = await UserControllerService.userLoginUsingPost(form);
  if (resp.code === 0) {
    // 登录并调整到主页
    await store.dispatch("getLoginUser");
    Message.success("登录成功");
    console.log("登录成功", resp);
    router.push({
      path: "/",
      replace: true,
    });
  } else {
    Message.error("登录失败：" + resp.message);
  }
};
</script>

<style scoped>
.login-view {
  max-width: 300px;
  margin: auto auto;
  padding: 40px;
  background: white;
  border-radius: 5px;
}
.form-item {
  color: white;
}
</style>
