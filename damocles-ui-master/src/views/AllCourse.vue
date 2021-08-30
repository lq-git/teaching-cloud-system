<template>
  <el-form :inline="true" label-position="left" ref="queryForm" :model="queryModel" class="queryForm">
    <el-form-item label="课程名" prop="cname">
      <el-input v-model="queryModel.cname" placeholder="请输入课程名称"></el-input>
    </el-form-item>
    <el-form-item label="教师" prop="cteachername">
      <el-input v-model="queryModel.cteachername" placeholder="请输入教师姓名"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click.prevent="query(queryModel)">查询</el-button>
    </el-form-item>
  </el-form>
  <el-table :data="tableData.records" style="width: 100%">
    <el-table-column label="ID" prop="cid"> </el-table-column>
    <el-table-column label="课程名称" prop="cname" align="center"></el-table-column>
    <el-table-column label="学分" prop="ccredit" align="center"></el-table-column>
    <el-table-column label="学时" prop="cperiod" align="center"></el-table-column>
    <el-table-column label="已选人数" prop="cselcount" align="center"></el-table-column>
    <el-table-column label="限定人数" prop="cmaxcount" align="center"></el-table-column>
    <el-table-column label="授课教师" prop="cteachername" align="center"></el-table-column>
    <el-table-column label="操作" align="center">
      <template #default="scope">
        <el-button size="mini"  @click="handleAdd(scope.$index, scope.row)">添加</el-button>
      </template>
    </el-table-column>
  </el-table>
  <Page></Page>
</template>
<script lang='ts'>
import { defineComponent, reactive } from "vue";
import Course from "../types/course";
import { request, responseMsg } from "../utils/request";
import Page from "../components/Page.vue";
import { tableData, query } from "../components/Page.vue";
import Popups from "../types/popups";
export default defineComponent({
  name: "AllCourse",
  components: {
    Page,
  },
  setup() {
    query("", "/course/allCourse/query");
    // 条件查询form
    const queryModel = reactive({} as Course);
    // 弹出框构造
    const dialogForm = reactive({ data: {} as Course });
    return {
      query,
      tableData,
      queryModel,
    };
  },
  methods: {
    handleAdd(index: number, row: any) {
      const confirmTitle = "添加";
      responseMsg.confirm((del: Function) => {
        request("/course/allCourse/add", { cid: row.cid }, "post").then(
          (response) => {
            del(response, confirmTitle, query);
          }
        );
      }, confirmTitle);
    },
  },
});
</script>
<style scoped>
</style>