<template>
  <el-pagination background layout="total, sizes, prev, pager, next, jumper" :total="tableData.total" :page-sizes="[5, 10, 20, 50]" :page-size="paging.size" @size-change="(currentSize) => (paging.size = currentSize)" :current-page="paging.current" @current-change="(currentPage) => (paging.current = currentPage)">
  </el-pagination>
</template>
<script lang='ts'>
import { defineComponent, reactive, watch, toRaw, ref } from "vue";
import { request, responseEntity } from "../utils/request";

// 表格数据
export const tableData = reactive({
  records: [], // 数据
  total: 0, // 总条数
});

// 分页参数
let paging = reactive({ current: 1, size: 10 });
// 查询参数
const queryUrl = ref(""),
  queryParams = ref("");

/**
 * 分页及条件查询解决方案
 * @param params  查询参数
 * @param url 请求地址
 * @doc 查询所有时params参数需使用空串或空对象进行占位
 * 进行过一次带url查询后，无需传递url参数
 *
 */
export const query = async (
  params: any = queryParams.value,
  url: any = queryUrl.value
) => {
  const pagingParams = toRaw(paging);
  queryUrl.value = url;
  queryParams.value = params;
  paging = reactive(pagingParams);
  await request(url, {
    ...params,
    ...pagingParams,
  }).then((response) => {
    const res = response as responseEntity;
    if (res.code === 0) {
      const data = res.data as any;
      tableData.records = data.records;
      tableData.total = data.total;
    }
  });
};

export default defineComponent({
  name: "Page",
  setup() {
    // 监听分页参数
    watch(
      () => paging,
      () => query(queryParams.value, queryUrl.value),
      { deep: true }
    );
    return {
      tableData,
      paging,
    };
  },
});
</script>
<style scoped>
</style>