<template>
  <div class="view-question-submit">
    <a-card
      :title="questionSubmit?.questionVO?.title"
      :bordered="false"
      style="max-width: 1280px; margin: 0 auto"
    >
      <template #extra>
        <a-space>
          <a-tag
            v-for="(item, index) in questionSubmit?.questionVO?.tags"
            :key="index"
            color="arcoblue"
            size="small"
          >
            {{ item }}
          </a-tag>
          <a-button type="primary" @click="onDoQuestion">重做</a-button>
        </a-space>
      </template>
      <div>
        <div style="margin-bottom: 5px">
          <strong>判题状态</strong>
          <span
            :style="
              'margin-left: 10px;' +
              (questionSubmit?.status === 2
                ? 'color: green'
                : questionSubmit?.status === 3
                ? 'color: red'
                : 'color: orange')
            "
            >{{ QustionSubmitStatusMap(questionSubmit?.status ?? 0) }}</span
          >
        </div>
        <a-divider :size="0" />
        <a-space>
          <div style="margin-bottom: 5px">
            <strong>判题结果</strong>
            <span
              :style="
                'margin-left: 10px;' +
                (questionSubmit?.judgeInfo?.message === 'Accepted'
                  ? 'color: green'
                  : 'color: red')
              "
              >{{ questionSubmit?.judgeInfo?.message }}</span
            >
          </div>
          <a-button
            v-if="questionSubmit?.status !== 1"
            type="primary"
            @click="onJudgeQuestion"
            >重新判题</a-button
          >
        </a-space>

        <a-divider :size="0" />
      </div>
      <a-row>
        <a-col :span="12">
          <div style="margin-bottom: 5px"><strong>限制条件</strong></div>
          <div>
            时间限制:
            {{ questionSubmit?.questionVO?.judgeConfig?.timeLimit ?? 0 }} ms
          </div>
          <div>
            内存限制:
            {{ questionSubmit?.questionVO?.judgeConfig?.memoryLimit ?? 0 }} kb
          </div>
        </a-col>
        <a-col :span="12">
          <div style="margin-bottom: 5px"><strong>资源消耗</strong></div>
          <div>
            时间消耗:
            {{ questionSubmit?.judgeInfo?.time ?? 0 }} ms
          </div>
          <div>
            内存消耗:
            {{ questionSubmit?.judgeInfo?.memory ?? 0 }} kb
          </div>
        </a-col>
      </a-row>

      <a-divider :size="0" />

      <div style="margin-bottom: 5px"><strong>题目内容</strong></div>
      <MdViewer :value="questionSubmit?.questionVO?.content" />

      <a-divider :size="0" />
      <div style="margin-bottom: 5px">
        <strong>提交的代码</strong>
        <span style="color: green">
          [ {{ questionSubmit?.language?.toUpperCase() }} ]
        </span>
      </div>
      <MdViewer :value="questionSubmit?.code" />
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { QuestionControllerService, QuestionSubmitVO } from "@/api";
import MdViewer from "@/components/MdViewer.vue";
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import QustionSubmitStatusMap from "@/utils/QustionSubmitStatusMap";

const router = useRouter();
const route = useRoute();
const questionSubmit = ref<QuestionSubmitVO>();

/**
 * 跳转到做题页面
 */
const onDoQuestion = () => {
  router.push({
    path: `/question/view/${questionSubmit.value?.questionId}`,
  });
};

const onJudgeQuestion = async () => {
  const resp = await QuestionControllerService.doJudgeUsingPost(
    questionSubmit.value?.id,
  );
  if (resp.code === 0) {
    questionSubmit.value = resp.data;
    questionSubmit.value!.code =
      "```" +
      questionSubmit.value!.language +
      "\n" +
      questionSubmit.value?.code +
      "\n```";
  }
};

const loadData = async (id: number) => {
  if (id >= 0) {
    const resp =
      await QuestionControllerService.getQuestionSubmitVoByIdUsingGet(id);
    if (resp.code === 0) {
      questionSubmit.value = resp.data;
      questionSubmit.value!.code =
        "```" +
        questionSubmit.value!.language +
        "\n" +
        questionSubmit.value?.code +
        "\n```";
    }
  }
};

onMounted(async () => {
  let id = route.params.id as any;
  console.log("id: " + id);
  loadData(id);
  // 每隔1秒刷新一下判题状态
  const intervalId = setInterval(() => {
    if (!questionSubmit.value?.judgeInfo?.message) {
      loadData(id);
    }
  }, 2000);

  // 10秒后清除
  setTimeout(() => {
    clearInterval(intervalId);
  }, 10000);
});
</script>

<style scoped>
.view-question-submit {
  margin: 10px 10px 40px 10px;
}
</style>
