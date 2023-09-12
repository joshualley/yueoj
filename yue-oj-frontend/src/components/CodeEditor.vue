<script setup lang="ts">
import * as monaco from "monaco-editor";
import { watch } from "vue";
import { ref, onMounted } from "vue";

interface Props {
  code: string;
  language: string;
  onchange: (v: string) => void;
}

const props = withDefaults(defineProps<Props>(), {
  code: "",
  language: "java",
  onchange: (v: string) => {
    console.log(v);
  },
});

let editor: monaco.editor.IStandaloneCodeEditor | undefined = undefined;
const codeEditorRef = ref();

/**
 * 初始化代码编辑器
 */
const eidtorInit = () => {
  if (!codeEditorRef.value) {
    return;
  }
  if (editor) {
    editor?.dispose();
  }
  editor = monaco.editor.create(codeEditorRef.value, {
    value: props.code,
    language: props.language,
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
    if (!editor) {
      return;
    }
    const v = editor.getValue();
    props.onchange(v);
  });
};

// 语言改变时，重新初始化编辑器
watch(
  () => props.language,
  () => {
    // console.log(props.language);
    eidtorInit();
  },
);

onMounted(() => {
  eidtorInit();
});
</script>

<template>
  <div id="codeEditor" ref="codeEditorRef"></div>
</template>

<style scoped>
#codeEditor {
  min-height: 65vh;
  width: 100%;
}
</style>
