<script setup lang="ts">
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Editor } from "@bytemd/vue-next";
import zhHans from "bytemd/lib/locales/zh_Hans.json";
import "bytemd/dist/index.css";
import "highlight.js/styles/vs.css";

interface MdEditorProps {
  value: string;
  mode: string;
  onchange: (v: string) => void;
}

const plugins = [gfm(), highlight()];
withDefaults(defineProps<MdEditorProps>(), {
  value: () => "",
  mode: () => "",
  onchange: (v: string) => {
    console.log(v);
  },
});
</script>

<template>
  <div class="md-editor">
    <Editor
      :value="value"
      :plugins="plugins"
      :mode="mode"
      :locale="zhHans"
      @change="onchange"
    />
  </div>
</template>

<style>
/* 隐藏github图标 */
.bytemd-toolbar-icon.bytemd-tippy.bytemd-tippy-right:last-child {
  display: none;
}
.md-editor {
  width: 100%;
}
</style>
