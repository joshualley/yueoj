<script setup lang="ts">
import * as monaco from "monaco-editor";
import { ref, defineProps, withDefaults, onMounted } from "vue";

interface Props {
  code: string;
  onchange: (v: string) => void;
}

const props = withDefaults(defineProps<Props>(), {
  code: () => "",
  onchange: (v: string) => {
    console.log(v);
  },
});

const codeEditorRef = ref();

onMounted(() => {
  if (!codeEditorRef.value) {
    return;
  }
  const editor = monaco.editor.create(codeEditorRef.value, {
    value: props.code,
    language: "java",
    automaticLayout: true,
    lineNumbers: "on",
    roundedSelection: false,
    scrollBeyondLastLine: false,
    readOnly: false,
    theme: "vs-dark",
    minimap: {
      enabled: false,
      scale: 5,
    },
  });
  // 监听编辑器的变化
  editor.onDidChangeModelContent(() => {
    const v = editor.getValue();
    props.onchange(v);
  });
});
</script>

<template>
  <div id="codeEditor" ref="codeEditorRef"></div>
</template>

<style scoped>
#codeEditor {
  min-height: 400px;
}
</style>
