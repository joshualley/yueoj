<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
import { onMounted } from "vue";

// 全局初始化
const doInit = () => {
  console.log("hello");
};

onMounted(() => {
  doInit();
});
</script>

<template>
  <div id="app">
    <BasicLayout />
  </div>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
}
</style>
