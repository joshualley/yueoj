<script setup lang="ts">
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Viewer } from "@bytemd/vue-next";
import "bytemd/dist/index.css";
import "highlight.js/styles/vs.css";

interface MdEditorProps {
  value: string;
}

const plugins = [gfm(), highlight()];
withDefaults(defineProps<MdEditorProps>(), {
  value: () => "",
});
</script>

<template>
  <div class="md-editor">
    <Viewer :value="value" :plugins="plugins" />
  </div>
</template>

<style scoped>
.md-editor {
  width: 100%;
}
</style>
