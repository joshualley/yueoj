<template>
  <div class="do-question">
    <a-row :gutter="8">
      <a-col :span="12">
        <a-card class="q-intruction">
          <a-tabs default-active-key="question">
            <a-tab-pane key="question" title="题目描述">
              <a-card :title="question.title" :bordered="false">
                <template #extra>
                  <a-space>
                    <a-tag
                      v-for="(item, index) in question.tags"
                      :key="index"
                      color="arcoblue"
                      size="small"
                    >
                      {{ item }}
                    </a-tag>
                  </a-space>
                </template>
                <div style="margin-bottom: 5px"><strong>限制条件</strong></div>
                <div>时间限制: {{ question.judgeConfig?.timeLimit }} ms</div>
                <div>内存限制: {{ question.judgeConfig?.memoryLimit }} kb</div>
                <div>堆栈限制: {{ question.judgeConfig?.stackLimit }} kb</div>
                <a-divider :size="0" />

                <div style="margin-bottom: 5px"><strong>题目内容</strong></div>
                <MdViewer :value="question.content" />
              </a-card>
            </a-tab-pane>
            <a-tab-pane key="answer" title="题解">
              <MdViewer :value="question.answer" />
            </a-tab-pane>
            <a-tab-pane key="comment" title="评论">
              <a-list>
                <template #header> List title </template>
                <a-list-item
                  >Beijing Bytedance Technology Co., Ltd.</a-list-item
                >
                <a-list-item>Bytedance Technology Co., Ltd.</a-list-item>
                <a-list-item>Beijing Toutiao Technology Co., Ltd.</a-list-item>
                <a-list-item
                  >Beijing Volcengine Technology Co., Ltd.</a-list-item
                >
                <a-list-item
                  >China Beijing Bytedance Technology Co., Ltd.</a-list-item
                >
              </a-list>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card class="q-editor">
          <a-form-item field="title" label="语言">
            <a-space direction="horizontal">
              <a-select
                style="min-width: 200px"
                placeholder="请选择语言"
                v-model:model-value="form.language"
              >
                <a-option value="java">Java</a-option>
                <a-option value="cpp">C++</a-option>
                <a-option value="go">Golang</a-option>
                <a-option value="javascript">Javascript</a-option>
              </a-select>
              <a-button type="outline" @click="onSubmit">提交</a-button>
            </a-space>
          </a-form-item>
          <a-form-item>
            <CodeEditor
              :code="form.code"
              :language="form.language"
              :onchange="onCodeChange"
            />
          </a-form-item>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import {
  QuestionControllerService,
  QuestionSubmitAddRequest,
  QuestionVO,
} from "@/api";
import MdViewer from "@/components/MdViewer.vue";
import CodeEditor from "@/components/CodeEditor.vue";
import { Message } from "@arco-design/web-vue";
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import router from "@/router";

const route = useRoute();

onMounted(() => {
  loadQuestion();
});

const question = ref<QuestionVO>({
  title: "",
  content: "",
  tags: [],
  answer: "",
  judgeConfig: {
    memoryLimit: 0,
    stackLimit: 0,
    timeLimit: 0,
  },
});

// 题目提交表单数据
const form = ref<QuestionSubmitAddRequest>({
  language: "java",
  code: "",
  questionId: 0,
});

/**
 * 加载题目数据
 */
const loadQuestion = async () => {
  let id = route.params.id as any;
  console.log(route.query.id, " <=> ", id);

  if (id >= 0) {
    const resp = await QuestionControllerService.getQuestionVoByIdUsingGet(id);
    if (resp.code === 0) {
      question.value = resp.data ?? {};
      // 获取题目id
      form.value.questionId = question.value.id;
    } else {
      Message.error("未能加载出题目：" + resp.message);
    }
  }
};

const onCodeChange = (v: string) => {
  form.value.code = v;
};

/**
 * 提交解题代码
 */
const onSubmit = async () => {
  // 创建题目提交
  const resp = await QuestionControllerService.doQuestionSubmitUsingPost(
    form.value,
  );
  if (resp.code === 0) {
    Message.success("题目提交成功，请等待判题结果。");
    router.push({
      path: `/question/submit/view/${resp.data}`,
    });
  } else {
    Message.error("题目提交失败：" + resp.message);
    console.log(resp);
  }
};
</script>

<style scoped>
.do-question {
  margin: 5px 10px 40px 10px;
}
.q-editor {
  min-height: 80vh;
  margin-right: 100px;
}
.q-intruction {
  margin-left: 100px;
  min-height: 80vh;
}
</style>
