import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "bytemd/dist/index.css";

import ArcoVue from "@arco-design/web-vue";
// 额外引入图标库
import ArcoVueIcon from "@arco-design/web-vue/es/icon";
import "@arco-design/web-vue/dist/arco.css";

import "@/plugins/axios";
import "@/access";

createApp(App)
  .use(store)
  .use(router)
  .use(ArcoVue)
  .use(ArcoVueIcon)
  .mount("#app");
