<template>
  <div class="manage-question">
    <a-table
      :data="questions"
      :pagination="{ ...pageQuery, total, showTotal: true }"
    >
      <template #columns>
        <a-table-column title="ID" data-index="id"></a-table-column>
        <a-table-column title="标题" data-index="title"></a-table-column>
        <a-table-column title="标签" data-index="tags"></a-table-column>
        <a-table-column title="提交数" data-index="submitNum"></a-table-column>
        <a-table-column
          title="通过数"
          data-index="acceptedNum"
        ></a-table-column>

        <a-table-column title="判题配置">
          <a-table-column
            title="内存限制"
            data-index="judgeConfig.memoryLimit"
          ></a-table-column>
          <a-table-column
            title="时间限制"
            data-index="judgeConfig.timeLimit"
          ></a-table-column>
          <a-table-column
            title="堆栈限制"
            data-index="judgeConfig.stackLimit"
          ></a-table-column>
        </a-table-column>
        <a-table-column title="操作">
          <template #cell="{ record }">
            <a-space>
              <a-button type="outline" @click="onUpdateRow(record)">
                修改
              </a-button>
              <a-button
                type="outline"
                status="danger"
                @click="onDeleteRow(record)"
              >
                删除
              </a-button>
            </a-space>
          </template>
        </a-table-column>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { Question, QuestionControllerService } from "@/api";
import { Message } from "@arco-design/web-vue";
import { onMounted } from "vue";
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

// 页面数据
const questions = ref<Array<Question>>([]);
const total = ref(0);
const pageQuery = ref({
  pageSize: 10,
  current: 1,
});

onMounted(async () => {
  const resp = await QuestionControllerService.listQuestionVoByPageUsingPost(
    pageQuery.value,
  );
  if (resp.code === 0) {
    questions.value = resp.data.records;
    total.value = resp.data.total;
    console.log(questions.value);
  } else {
    Message.error("未能获取到题目数据：" + resp.message);
  }
});

const onDeleteRow = async (question: Question) => {
  console.log(question);
  const resp = await QuestionControllerService.deleteQuestionUsingPost({
    id: question.id,
  });
  if (resp.code === 0) {
    Message.success("删除成功");
    // 更新表格
    const idx = questions.value.findIndex((v) => v.id === question.id);
    questions.value.splice(idx, 1);
  } else {
    Message.error("删除失败：" + resp.message);
  }
};

const onUpdateRow = (question: Question) => {
  console.log(question);
  router.push({
    path: "/update/question",
    query: {
      id: question.id,
    },
  });
};
</script>

<style scoped>
.manage-question {
  margin: 10px 10px 40px 10px;
}
</style>
