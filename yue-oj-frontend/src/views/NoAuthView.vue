<template>
  <div class="no-auth">
    <p class="warn-text">您没有该页面的访问权限！</p>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped>
.no-auth {
  background: whitesmoke;
  height: 70vh;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: auto auto;
  text-align: center;
}

.warn-text {
  color: red;
  font-size: 32px;
}
</style>
