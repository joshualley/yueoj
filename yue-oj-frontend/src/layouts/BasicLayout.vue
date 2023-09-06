<script lang="ts">
import GlobalHeader from "@/components/GlobalHeader.vue";
export default {
  data() {
    return {};
  },
  components: {
    GlobalHeader,
  },
  methods: {},
};
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
  height: 500px;
}
.header {
  margin: 8px;
}
.content {
  background: linear-gradient(to right, #eee, #000);
  margin: 8px;
}
.footer {
  background: #efefef;
  padding: 16px;
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}
</style>
