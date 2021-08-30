<template>
  <el-form :inline="true" label-position="left" ref="queryForm" :model="queryModel" class="queryForm">
    <el-form-item label="课程名" prop="cname">
      <el-input v-model="queryModel.cname" placeholder="请输入课程名称"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click.prevent="query(queryModel)">查询</el-button>
      <el-button type="info" @click.prevent="resetForm('queryForm')">重置</el-button>
      <el-button type="success" @click.prevent="handleAdd">新建</el-button>
    </el-form-item>
  </el-form>
  <el-table :data="tableData.records" style="width: 100%">
    <el-table-column label="ID" prop="cid"> </el-table-column>
    <el-table-column label="课程名称" prop="cname" align="center"></el-table-column>
    <el-table-column label="学分" prop="ccredit" align="center"></el-table-column>
    <el-table-column label="学时" prop="cperiod" align="center"></el-table-column>
    <el-table-column label="已选人数" prop="cselcount" align="center"></el-table-column>
    <el-table-column label="限定人数" prop="cmaxcount" align="center"></el-table-column>
    <!-- <el-table-column label="创建时间" prop="createTime" align="center" :width="200">
      <template #default="scope">
        <i class="el-icon-time"></i>
        <span style="margin-left: 10px">{{ scope.row.createTime }}</span>
      </template>
    </el-table-column> -->
    <el-table-column label="操作" align="center">
      <template #default="scope">
        <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
        <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <Page></Page>
  <el-dialog :title="dialogWrapper.title" v-model="dialogWrapper.visible" width="40%">
    <el-form ref="updateForm" :inline="true" :model="dialogForm.data" label-width="150px">
      <el-form-item label="ID" prop="cid" v-if="false">
        <el-input v-model="dialogForm.data.cid"></el-input>
      </el-form-item>
      <el-form-item label="授课教师编号" prop="cteacherid" v-if="false">
        <el-input v-model="dialogForm.data.cteacherid"></el-input>
      </el-form-item>
      <el-form-item label="课程名称" prop="cname">
        <el-input v-model="dialogForm.data.cname" placeholder="请输入课程名称"></el-input>
      </el-form-item>
      <el-form-item label="学分" prop="ccredit">
        <el-input v-model="dialogForm.data.ccredit" placeholder="请输入学分">
        </el-input>
      </el-form-item>
      <el-form-item label="学时" prop="cperiod">
        <el-input v-model="dialogForm.data.cperiod" placeholder="请输入学时">
        </el-input>
      </el-form-item>
      <el-form-item label="已选人数" prop="cselcount" v-if="false">
        <el-input v-model="dialogForm.data.cselcount"></el-input>
      </el-form-item>
      <el-form-item label="限定人数" prop="cmaxcount">
        <el-input v-model="dialogForm.data.cmaxcount" placeholder="请输入课程限定人数">
        </el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="save()">保存</el-button>
        <el-button @click="cancel()">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script lang='ts'>
import { defineComponent, reactive } from "vue";
import Course from "../types/course";
import { request, responseMsg } from "../utils/request";
import Page from "../components/Page.vue";
import { tableData, query } from "../components/Page.vue";
import Popups from "../types/popups";
export default defineComponent({
  name: "MyCourse",
  components: {
    Page,
  },
  setup() {
    query("", "/course/myCourse/query");
    // 条件查询form
    const queryModel = reactive({} as Course);
    // 弹出框构造
    const dialogForm = reactive({ data: {} as Course });
    const dialogWrapper = reactive(new Popups());
    return {
      query,
      tableData,
      queryModel,
      dialogForm,
      dialogWrapper,
    };
  },
  methods: {
    resetForm(formName: string) {
      const _this = this as any;
      _this.$refs[formName].resetFields();
    },
    handleAdd() {
      this.dialogWrapper.type = "add";
      this.dialogWrapper.visible = true;
      this.dialogWrapper.title = "新建课程";
    },
    handleEdit(index: number, row: any) {
      this.dialogWrapper.type = "edit";
      this.dialogWrapper.visible = true;
      this.dialogWrapper.title = "编辑课程";
      this.dialogForm.data = { ...row };
    },
    handleDelete(index: number, row: any) {
      const confirmTitle = "删除";
      responseMsg.confirm((del: Function) => {
        request("/course/myCourse/delete", { cid: row.cid }, "delete").then(
          (response) => {
            del(response, confirmTitle, query);
          }
        );
      }, confirmTitle);
    },
    save() {
      const _this = this as any;
      const queryUrl =
        _this.dialogWrapper.type === "add"
          ? "/course/myCourse/create"
          : "/course/myCourse/update";
      const msgTitle = _this.dialogWrapper.title;
      request(queryUrl, _this.dialogForm.data, "post")
        .then((response) => {
          responseMsg.success(response, msgTitle, query);
        })
        .catch((error) => {
          responseMsg.error(error, msgTitle);
        });
      this.cancel();
    },
    cancel() {
      this.dialogWrapper.visible = false;
      this.dialogForm.data = {} as Course;
    },
  },
});
</script>
<style scoped>
</style>