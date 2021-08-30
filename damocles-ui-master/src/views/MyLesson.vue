<template>

  <el-table :data="tableData.records" style="width: 100%">
    <el-table-column label="课程编号" prop="cid" align="center"></el-table-column>
    <el-table-column label="课程名称" prop="cname" align="center"></el-table-column>
    <el-table-column label="学分" prop="ccredit" align="center"></el-table-column>
    <el-table-column label="学时" prop="cperiod" align="center"></el-table-column>
    <el-table-column label="已选人数" prop="cselcount" align="center"></el-table-column>
    <el-table-column label="限定人数" prop="cmaxcount" align="center"></el-table-column>
    <el-table-column label="授课教师" prop="cteachername" align="center"></el-table-column>
  </el-table>

  <Page></Page>
</template>
<script lang='ts'>
import { defineComponent, reactive } from "vue";
import Page from "../components/Page.vue";
import { tableData, query } from "../components/Page.vue";
export default defineComponent({
  name: "MyLesson",
  components: {
    Page,
  },
  setup() {
    query("", "/course/myLesson/query");
    return {
      query,
      tableData,
    };
  },
});
</script>
<style scoped>
</style>