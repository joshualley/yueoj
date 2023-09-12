<template>
  <div class="add-question">
    <a-form :model="form" @submit="onSubmit" auto-label-width>
      <a-form-item field="title" label="标题">
        <a-input v-model="form.title" placeholder="请输入标题" />
      </a-form-item>
      <a-form-item field="tags" label="标签">
        <a-input-tag v-model="form.tags" allow-clear placeholder="请输入标签" />
      </a-form-item>
      <a-form-item field="content" label="内容">
        <MdEditor :value="form.content" :onchange="onContentChange" />
      </a-form-item>
      <a-form-item field="answer" label="答案">
        <MdEditor :value="form.answer" :onchange="onAnswerChange" />
      </a-form-item>

      <a-form-item label="判题配置" :content-flex="false" :merge-props="false">
        <a-space direction="horizontal" fill>
          <a-card style="min-width: 480px">
            <a-form-item field="title" label="内存限制(KB)">
              <a-input-number
                v-model="form.judgeConfig.memoryLimit"
                placeholder="请输入内存限制"
                mode="button"
                :min="0"
              />
            </a-form-item>
            <a-form-item field="title" label="时间限制(ms)">
              <a-input-number
                v-model="form.judgeConfig.timeLimit"
                placeholder="请输入时间限制"
                mode="button"
                :min="0"
              />
            </a-form-item>
            <a-form-item field="title" label="堆栈限制(KB)">
              <a-input-number
                v-model="form.judgeConfig.stackLimit"
                placeholder="请输入堆栈限制"
                mode="button"
                :min="0"
              />
            </a-form-item>
          </a-card>
        </a-space>
      </a-form-item>

      <a-form-item label="判题用例" :content-flex="false" :merge-props="false">
        <a-space direction="vertical">
          <a-card
            v-for="(item, index) in form.judgeCase"
            :title="`测试用例-${index + 1}`"
            :key="index"
            style="min-width: 480px"
          >
            <template #extra>
              <a-button
                type="primary"
                status="danger"
                @click="onRemoveJudgeCase(index)"
              >
                <template #icon>
                  <icon-delete />
                </template>
              </a-button>
            </template>
            <a-form-item
              field="`form.judgeCase[${index}].input`"
              label="输入用例"
            >
              <a-input v-model="item.input" placeholder="请输入输入用例" />
            </a-form-item>
            <a-form-item
              field="`form.judgeCase[${index}].output`"
              label="输出用例"
            >
              <a-input v-model="item.output" placeholder="请输入输出用例" />
            </a-form-item>
            <a-button
              type="outline"
              style="float: right"
              @click="onAddJudgeCase"
            >
              <template #icon>
                <icon-plus />
              </template>
            </a-button>
            <div style="clear: both" />
          </a-card>
        </a-space>
      </a-form-item>

      <a-form-item>
        <a-button type="primary" html-type="submit">
          {{ isUpdate ? "修改" : "创建" }}
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { QuestionControllerService, QuestionVO } from "@/api";
import MdEditor from "@/components/MdEditor.vue";
import router from "@/router";
import { Message } from "@arco-design/web-vue";
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();
// 页面是否为修改页面
const isUpdate = route.path.includes("update");

const form = ref({
  title: "",
  content: "",
  tags: [],
  answer: "",
  judgeConfig: {
    memoryLimit: 0,
    stackLimit: 0,
    timeLimit: 0,
  },
  judgeCase: [
    {
      input: "",
      output: "",
    },
  ],
});

onMounted(async () => {
  // 更新时才执行加载页面数据逻辑
  if (!isUpdate) {
    return;
  }
  let id = route.query.id as any;
  console.log(route.query.id, " <=> ", id);

  if (id >= 0) {
    const resp = await QuestionControllerService.getQuestionVoByIdUsingGet(id);
    if (resp.code === 0) {
      // 将属性重新赋值给form
      console.log(resp.data);
      form.value = resp.data as any;
      // form.value.tags = JSON.parse(resp.data?.tags ?? "[]");
      // form.value.judgeCase = JSON.parse(resp.data?.judgeCase ?? "[]");
      // form.value.judgeConfig = JSON.parse(resp.data?.judgeConfig ?? "{}");
    } else {
      Message.error("未能加载出题目：" + resp.message);
    }
  } else {
    Message.error("不存在要修改的题目");
    router.back();
  }
});

/**
 * 提交题目
 */
const onSubmit = async () => {
  if (isUpdate) {
    // 更新题目
    const resp = await QuestionControllerService.updateQuestionUsingPost(
      form.value,
    );
    if (resp.code === 0) {
      Message.success("题目修改成功");
    } else {
      Message.error("题目修改失败：" + resp.message);
    }
  } else {
    // 创建题目
    const resp = await QuestionControllerService.addQuestionUsingPost(
      form.value,
    );
    if (resp.code === 0) {
      Message.success("题目创建成功");
    } else {
      Message.error("题目创建失败：" + resp.message);
    }
  }
};

const onContentChange = (v: string) => {
  form.value.content = v;
};
const onAnswerChange = (v: string) => {
  form.value.answer = v;
};

/**
 * 移除判题用例
 * @param index
 */
const onRemoveJudgeCase = (index: number) => {
  if (form.value.judgeCase.length <= 1) {
    Message.warning("必须保留一个用例");
    return;
  }
  form.value.judgeCase.splice(index, 1);
};
/**
 * 添加一行判题用例
 */
const onAddJudgeCase = () => {
  form.value.judgeCase.push({
    input: "",
    output: "",
  });
};
</script>

<style scoped>
.add-question {
  margin: 10px 10px 40px 10px;
}
</style>
