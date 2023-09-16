<template>
  <div class="question-submit-view">
    <a-input-search
      v-model="searchText"
      :style="{ minWidth: '320px', margin: '10px 0' }"
      placeholder="请输入要搜索的关键字..."
      @press-enter="onSearch"
      @search="onSearch"
    />
    <a-table
      :data="questionSubmits"
      :pagination="{
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total: total,
        showTotal: true,
      }"
      @page-change="onPageChange"
    >
      <template #columns>
        <a-table-column title="创建日期" data-index="createTime">
          <template #cell="{ record }">
            {{ moment(record.createTime).format("YYYY/MM/DD") }}
          </template>
        </a-table-column>
        <a-table-column
          title="提交用户"
          data-index="userVO.userName"
        ></a-table-column>
        <a-table-column
          title="题目"
          data-index="questionVO.title"
        ></a-table-column>
        <a-table-column title="标签">
          <template #cell="{ record }">
            <a-space>
              <a-tag
                v-for="(item, index) in record.questionVO.tags"
                :key="index"
                color="arcoblue"
                size="small"
              >
                {{ item }}
              </a-tag>
            </a-space>
          </template>
        </a-table-column>
        <a-table-column title="语言" data-index="language"></a-table-column>
        <a-table-column title="状态" data-index="">
          <template #cell="{ record }">
            <div>{{ QustionSubmitStatusMap(record.status) }}</div>
          </template>
        </a-table-column>
        <a-table-column title="资源耗用">
          <a-table-column
            title="内存消耗(kb)"
            data-index="judgeInfo.memory"
          ></a-table-column>
          <a-table-column
            title="时间消耗(ms)"
            data-index="judgeInfo.time"
          ></a-table-column>
        </a-table-column>
        <a-table-column title="操作">
          <template #cell="{ record }">
            <a-space>
              <a-button type="outline" @click="doQuestion(record)">
                查看
              </a-button>
            </a-space>
          </template>
        </a-table-column>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { Question, QuestionControllerService, QuestionSubmitVO } from "@/api";
import { Message } from "@arco-design/web-vue";
import { watchEffect } from "vue";
import { ref } from "vue";
import { useRouter } from "vue-router";
import moment from "moment";
import QustionSubmitStatusMap from "@/utils/QustionSubmitStatusMap";

const router = useRouter();

// 页面数据
const questionSubmits = ref<Array<QuestionSubmitVO>>([]);
const total = ref(0);

const searchText = ref("");
const searchParams = ref({
  title: "",
  pageSize: 10,
  current: 1,
});

/**
 * 搜索题目
 */
const onSearch = () => {
  searchParams.value = {
    ...searchParams.value,
    title: searchText.value,
  };
};
/**
 * 加载数据
 */
const loadTableData = async () => {
  const resp =
    await QuestionControllerService.listQuestionSubmitByPageUsingPost(
      searchParams.value,
    );
  if (resp.code === 0) {
    questionSubmits.value = resp.data.records;
    total.value = Number(resp.data.total);
  } else {
    Message.error("未能获取到题目提交记录数据：" + resp.message);
  }
};

/**
 * 跳转到做题页面
 * @param question
 */
const doQuestion = (question: Question) => {
  router.push({
    path: `/question/submit/view/${question.id}`,
  });
};

const onPageChange = (page: number) => {
  // 需要整个替换变量，以触发事件的监听
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

// 监听searchParams的改变
watchEffect(() => {
  loadTableData();
});
</script>

<style scoped>
.question-submit-view {
  margin: 0 auto;
  max-width: 1280px;
}
</style>
