<script setup lang="ts"></script>

<template>
  <div id="userLayout">
    <a-layout class="layout">
      <a-layout-header class="header">
        <div>
          <img class="logo" src="../assets/logo.png" alt="logo" />
          <div class="title">YUE-OJ</div>
        </div>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
    </a-layout>
  </div>
</template>

<style scoped>
#userLayout {
  height: 100vh;
  background-color: whitesmoke;
}

.header {
  margin: 50px 8px 8px 8px;
  text-align: center;
}
.content {
  margin: 10px 100px;
}
.logo {
  height: 100px;
}
</style>
