<template>
  <el-form :inline="true" label-position="left" ref="queryForm" :model="queryModel" class="queryForm">
    <el-form-item label="单词" prop="english">
      <el-input v-model="queryModel.english" placeholder="请输入单词"></el-input>
    </el-form-item>
    <el-form-item label="中文" prop="chinese">
      <el-input v-model="queryModel.chinese" placeholder="请输入单词"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click.prevent="query(queryModel)">查询</el-button>
      <el-button type="info" @click.prevent="resetForm('queryForm')">重置</el-button>
      <el-button type="success" @click.prevent="handleAdd">新建</el-button>
    </el-form-item>
  </el-form>
  <el-table :data="tableData.records" style="width: 100%">
    <el-table-column label="ID" prop="id"> </el-table-column>
    <el-table-column label="单词" prop="english" align="center"></el-table-column>
    <el-table-column label="中文" prop="chinese" align="center"></el-table-column>
    <el-table-column v-if="false" prop="userId"></el-table-column>
    <el-table-column label="作者" prop="author" align="center"></el-table-column>
    <el-table-column label="点赞数" prop="likeNum" align="center"></el-table-column>
    <el-table-column label="创建时间" prop="createTime" align="center" :width="200">
      <template #default="scope">
        <i class="el-icon-time"></i>
        <span style="margin-left: 10px">{{ scope.row.createTime }}</span>
      </template>
    </el-table-column>
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
      <el-form-item label="ID" prop="id" v-if="false">
        <el-input v-model="dialogForm.data.id"></el-input>
      </el-form-item>
      <el-form-item label="ID" prop="userId" v-if="false">
        <el-input v-model="dialogForm.data.userId"></el-input>
      </el-form-item>
      <el-form-item label="单词" prop="english">
        <el-input v-model="dialogForm.data.english" placeholder="请输入英文单词"></el-input>
      </el-form-item>
      <el-form-item label="中文" prop="chinese">
        <el-input v-model="dialogForm.data.chinese" placeholder="请输入中文翻译">
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
import Word from "../types/word";
import { request, responseMsg } from "../utils/request";
import Page from "../components/Page.vue";
import { tableData, query } from "../components/Page.vue";
import Popups from "../types/popups";
export default defineComponent({
  name: "WordMe",
  components: {
    Page,
  },
  setup() {
    query("", "/word/myWord/query");
    // 条件查询form
    const queryModel = reactive({} as Word);
    // 弹出框构造
    const dialogForm = reactive({ data: {} as Word });
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
      this.dialogWrapper.title = "创建单词";
    },
    handleEdit(index: number, row: any) {
      this.dialogWrapper.type = "edit";
      this.dialogWrapper.visible = true;
      this.dialogWrapper.title = "编辑单词";
      this.dialogForm.data = { ...row };
    },
    handleDelete(index: number, row: any) {
      const confirmTitle = "删除";
      responseMsg.confirm((del: Function) => {
        request("/word/myWord/delete", { id: row.id }, "delete").then(
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
          ? "/word/myWord/create"
          : "/word/myWord/update";
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
      this.dialogForm.data = {} as Word;
    },
  },
});
</script>
<style scoped>
</style>