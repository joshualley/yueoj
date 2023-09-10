<script setup lang="ts">
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Editor, Viewer } from "@bytemd/vue-next";
import { ref, defineProps, withDefaults } from "vue";

interface MdEditorProps {
  value: string;
  onchange: (v: string) => void;
}

const plugins = [gfm(), highlight()];
const props = withDefaults(defineProps<MdEditorProps>(), {
  value: () => "",
  onchange: (v: string) => {
    console.log(v);
  },
});
</script>

<template>
  <div>
    <Editor :value="value" :plugins="plugins" @change="onchange" />
  </div>
</template>

<style>
/* 隐藏github图标 */
.bytemd-toolbar-icon.bytemd-tippy.bytemd-tippy-right:last-child {
  display: none;
}
</style>
