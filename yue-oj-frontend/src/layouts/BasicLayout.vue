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
        <div>Copyright©2023 Jose</div>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<style scoped>
.layout {
  min-height: 500px;
  position: relative;
}
.header {
  margin: 0 10px;
  background: white;
  position: sticky;
  top: 0;
  z-index: 99;
}
.content {
  margin: 10px 10px 60px 10px;
}
.footer {
  background: whitesmoke;
  padding: 16px;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}
</style>
