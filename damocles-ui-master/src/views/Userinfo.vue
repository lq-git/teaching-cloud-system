<template>
  <el-form :inline="true" label-position="left" ref="queryForm" :model="queryModel" class="queryForm">
    <el-form-item label="姓名" prop="nickName">
      <el-input v-model="queryModel.nickName" placeholder="请输入姓名"></el-input>
    </el-form-item>
    <el-form-item label="用户名" prop="username">
      <el-input v-model="queryModel.username" placeholder="请输入用户名"></el-input>
    </el-form-item>
    <el-form-item label="性别" prop="sex">
      <el-select v-model="queryModel.sex" clearable placeholder="请选择性别">
        <el-option v-for="item in dict.selectOption.sex" :key="item.id" :label="item.text" :value="item.code"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="学历" prop="degree">
      <el-select v-model="queryModel.degree" clearable placeholder="请选择学历">
        <el-option v-for="item in dict.selectOption.degree" :key="item.id" :label="item.text" :value="item.code"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click.prevent="query(queryModel)">查询</el-button>
      <el-button type="info" @click.prevent="resetForm('queryForm')">重置</el-button>
    </el-form-item>
  </el-form>
  <el-table :data="tableData.records" style="width: 100%">
    <el-table-column label="ID" prop="id"> </el-table-column>
    <el-table-column label="姓名" prop="nickName"></el-table-column>
    <el-table-column label="用户名" prop="username" align="center"></el-table-column>
    <el-table-column label="头像" prop="icon" align="center">
      <template #default="scope">
        <el-avatar :size="40" v-if="scope.row.icon" shape="square">
          <img :src="'/api/' + scope.row.icon" @error="errorHandler" />
        </el-avatar>
      </template>
    </el-table-column>
    <el-table-column label="性别" prop="sex" align="center">
      <template #default="scope" v-if="dict.map.sex">
        {{ dict.map.sex[scope.row.sex] }}
      </template>
    </el-table-column>
    <el-table-column label="年龄" prop="age" align="center"></el-table-column>
    <el-table-column label="电话" prop="phone" align="center"></el-table-column>
    <el-table-column label="邮箱" prop="email" align="center"></el-table-column>
    <el-table-column label="住址" prop="address" align="center"></el-table-column>
    <el-table-column label="学号" prop="sno" align="center"></el-table-column>
    <el-table-column label="班级" prop="className" align="center"></el-table-column>
    <el-table-column label="入学时间" prop="admissionDate" align="center" :width="200">
      <template #default="scope">
        <i class="el-icon-time" v-if="scope.row.admissionDate"></i>
        <span style="margin-left: 10px">{{ scope.row.admissionDate }}</span>
      </template>
    </el-table-column>
    <el-table-column label="学历" prop="degree" align="center">
      <template #default="scope" v-if="dict.map.degree">
        {{ dict.map.degree[scope.row.degree] }}
      </template>
    </el-table-column>
    <el-table-column label="状态" prop="status" align="center">
      <template #default="scope" v-if="dict.map.userStatus">
        {{ dict.map.userStatus[scope.row.status] }}
      </template>
    </el-table-column>
    <el-table-column label="操作" align="center" :width="200">
      <template #default="scope">
        <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
        <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <Page></Page>
  <el-dialog :title="dialogWrapper.title" v-model="dialogWrapper.visible" width="40%">
    <el-form ref="updateForm" :inline="true" :model="dialogForm.data" label-width="130px">
      <el-form-item label="ID" prop="id" v-if="false">
        <el-input v-model="dialogForm.data.id"></el-input>
      </el-form-item>
      <el-form-item label="姓名" prop="nickName">
        <el-input v-model="dialogForm.data.nickName" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-select v-model="dialogForm.data.sex" clearable placeholder="请选择性别">
          <div v-show="false">{{ dict.selectOption }}</div>
          <el-option v-for="item in dict.selectOption.sex" :key="item.id" :label="item.text" :value="item.code"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input v-model="dialogForm.data.age" placeholder="请输入年龄">
        </el-input>
      </el-form-item>
      <el-form-item label="电话" prop="phone">
        <el-input v-model="dialogForm.data.phone" placeholder="请输入电话"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="dialogForm.data.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input v-model="dialogForm.data.address" placeholder="请输入住址"></el-input>
      </el-form-item>
      <el-form-item label="学号" prop="sno">
        <el-input v-model="dialogForm.data.sno" placeholder="请输入学号"></el-input>
      </el-form-item>
      <el-form-item label="班级" prop="className">
        <el-input v-model="dialogForm.data.className" placeholder="请输入班级"></el-input>
      </el-form-item>
      <el-form-item label="入学时间" prop="admissionDate">
        <el-input v-model="dialogForm.data.admissionDate" placeholder="请输入入学时间"></el-input>
      </el-form-item>
      <el-form-item label="学历" prop="degree">
        <el-select v-model="dialogForm.data.degree" clearable placeholder="请选择性别">
          <div v-show="false">{{ dict.selectOption }}</div>
          <el-option v-for="item in dict.selectOption.degree" :key="item.id" :label="item.text" :value="item.code"></el-option>
        </el-select>
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
import { request, responseMsg } from "../utils/request";
import Page from "../components/Page.vue";
import { tableData, query } from "../components/Page.vue";
import User from "../types/user";
import Dict from "../types/dict";
import {
  loadDictData,
  convertDictMap,
  convertDictList,
} from "../utils/dictUtil";
import Popups from "../types/popups";
export default defineComponent({
  name: "Userinfo",
  components: {
    Page,
  },
  setup() {
    // 条件查询form
    const queryModel: User = reactive({});
    // 数据表格字典字段转换
    const dict = reactive({ map: {}, selectOption: {} });
    loadDictData((data: Array<Dict>) => {
      dict.selectOption = convertDictList(data);
      dict.map = convertDictMap(data);
    });
    query("", "/user/userinfo/query");
    // 弹出框构造
    const dialogForm = reactive({ data: {} as User });
    const dialogWrapper = reactive(new Popups());
    return {
      queryModel,
      dict,
      tableData,
      query,
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
      this.dialogWrapper.title = "添加用户";
    },
    handleEdit(index: number, row: any) {
      this.dialogWrapper.type = "edit";
      this.dialogWrapper.visible = true;
      this.dialogWrapper.title = "编辑用户";
      this.dialogForm.data = { ...row };
    },
    handleDelete(index: number, row: any) {
      const confirmTitle = "删除";
      responseMsg.confirm((del: Function) => {
        request("/user/userinfo/delete", { roleId: row.id }, "delete").then(
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
          ? "/user/userinfo/create"
          : "/user/userinfo/update";
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
      this.dialogForm.data = {} as User;
    },
  },
});
</script>
<style scoped>
</style>