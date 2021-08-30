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
    <el-table-column label="学号" prop="sno" align="center"></el-table-column>
    <el-table-column label="班级" prop="className" align="center"></el-table-column>
    <el-table-column label="状态" prop="isSubmitted" align="center"></el-table-column>
  </el-table>
  <Page></Page>
</template>
<script lang='ts'>
import { defineComponent, reactive } from "vue";
import Page from "../components/Page.vue";
import { tableData, query } from "../components/Page.vue";
import User from "../types/user";
import Dict from "../types/dict";
import {
  loadDictData,
  convertDictMap,
  convertDictList,
} from "../utils/dictUtil";
export default defineComponent({
  name: "DailyUserRecord",
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
    query("", "/daily/userRecord");
    return {
      queryModel,
      dict,
      tableData,
      query,
    };
  },
  methods: {
    resetForm(formName: string) {
      const _this = this as any;
      _this.$refs[formName].resetFields();
    },
  },
});
</script>
<style scoped>
</style>