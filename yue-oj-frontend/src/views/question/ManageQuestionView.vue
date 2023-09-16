<template>
  <div class="manage-question">
    <a-input-search
      v-model="searchText"
      :style="{ minWidth: '320px', margin: '10px 0' }"
      placeholder="请输入要搜索的关键字..."
      @press-enter="onSearch"
      @search="onSearch"
    />
    <a-table
      :data="questions"
      :pagination="{
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total: total,
        showTotal: true,
      }"
      @page-change="onPageChange"
    >
      <template #columns>
        <a-table-column title="ID" data-index="id"></a-table-column>
        <a-table-column title="标题" data-index="title"></a-table-column>
        <a-table-column title="标签" data-index="tags">
          <template #cell="{ record }">
            <a-space>
              <a-tag
                v-for="(item, index) in record.tags"
                :key="index"
                color="arcoblue"
                size="small"
              >
                {{ item }}
              </a-tag>
            </a-space>
          </template>
        </a-table-column>

        <a-table-column title="通过率">
          <template #cell="{ record }">
            <p>
              {{
                !record.submitNumb || record.submitNumb === 0
                  ? 0
                  : (record.acceptedNum / record.submitNumb) * 100
              }}
              % ({{ record.acceptedNum }} / {{ record.submitNumb }})
            </p>
          </template>
        </a-table-column>

        <a-table-column title="判题配置">
          <a-table-column
            title="内存限制"
            data-index="judgeConfig.memoryLimit"
          ></a-table-column>
          <a-table-column
            title="时间限制"
            data-index="judgeConfig.timeLimit"
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
import { watchEffect } from "vue";
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

// 页面数据
const questions = ref<Array<Question>>([]);
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
    current: 1, // 返回搜索后的第一页
    title: searchText.value,
  };
};

/**
 * 加载数据
 */
const loadTableData = async () => {
  const resp = await QuestionControllerService.listQuestionVoByPageUsingPost(
    searchParams.value,
  );
  if (resp.code === 0) {
    questions.value = resp.data.records;
    total.value = resp.data.total;
    console.log(questions.value);
  } else {
    Message.error("未能获取到题目数据：" + resp.message);
  }
};

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
    path: "/question/update",
    query: {
      id: question.id,
    },
  });
};

/**
 * 分页
 * @param page
 */
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
.manage-question {
  margin: 0 auto;
  max-width: 1280px;
}
</style>
