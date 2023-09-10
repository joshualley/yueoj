<template>
  <div class="home">
    <CodeEditor :code="code" :onchange="onCodeChange" />
    <MdEditor :value="value" :onchange="onMdChange" />
  </div>
</template>

<script setup lang="ts">
import MdEditor from "@/components/MdEditor.vue";
import CodeEditor from "@/components/CodeEditor.vue";
import { ref } from "vue";

const value = ref("");
const onMdChange = (v: string) => {
  value.value = v;
};

const code = ref("");
const onCodeChange = (v: string) => {
  code.value = v;
};
</script>
