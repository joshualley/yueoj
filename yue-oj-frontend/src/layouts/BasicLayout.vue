<script setup lang="ts">
import GlobalHeader from "@/components/GlobalHeader.vue";
</script>

<template>
  <div id="basicLayout">
    <a-layout class="layout">
      <a-layout-header class="header">
        <GlobalHeader />
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">
        <a href="http://yupi.icu">@c by Jose</a>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<style scoped>
.layout {
  min-height: 500px;
}
.header {
  margin: 0 10px;
  background: white;
  position: sticky;
  top: 0;
  z-index: 99;
}
.content {
  height: 100vh;
  margin: 8px;
}
.footer {
  background: #efefef;
  padding: 16px;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}
</style>
