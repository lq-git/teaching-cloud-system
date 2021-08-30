<template>
  <el-form :inline="true" label-position="left" ref="queryForm" :model="queryModel" class="queryForm">
    <el-form-item label="姓名" prop="nickName">
      <el-input v-model="queryModel.nickName" placeholder="请输入姓名"></el-input>
    </el-form-item>
    <el-form-item label="小组" prop="teamId">
      <el-select v-model="queryModel.teamId" clearable placeholder="请选择小组">
        <el-option v-for="item in dict.selectOption.team" :key="item.id" :label="item.teamName" :value="item.id"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click.prevent="query(queryModel)">查询</el-button>
      <el-button type="info" @click.prevent="resetForm('queryForm')">重置</el-button>
    </el-form-item>
  </el-form>
  <el-table :data="tableData.records" style="width: 100%">
    <el-table-column label="ID" prop="id"> </el-table-column>
    <el-table-column v-if="false" prop="userId"></el-table-column>
    <el-table-column label="姓名" prop="nickName"></el-table-column>
    <el-table-column label="操作类型" prop="type" align="center">
      <template #default="scope" v-if="dict.map.dispatchdType">
        {{ dict.map.dispatchdType[scope.row.type] }}
      </template>
    </el-table-column>
    <el-table-column v-if="false" prop="teamId"></el-table-column>
    <el-table-column label="所在小组" prop="teamName"></el-table-column>
    <el-table-column label="操作时间" prop="createTime" align="center">
      <template #default="scope">
        <i class="el-icon-time" v-if="scope.row.createTime"></i>
        <span style="margin-left: 10px">{{ scope.row.createTime }}</span>
      </template>
    </el-table-column>
    <el-table-column label="操作" align="center" :width="200">
      <template #default="scope">
        <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <Page></Page>
</template>
<script lang='ts'>
import { defineComponent, reactive } from "vue";
import { request, responseMsg, responseEntity } from "../utils/request";
import Page from "../components/Page.vue";
import { tableData, query } from "../components/Page.vue";
import Dispatch from "../types/dispatch";
import Dict from "../types/dict";
import { loadDictData, convertDictMap } from "../utils/dictUtil";
export default defineComponent({
  name: "UserDispatchRecord",
  components: {
    Page,
  },
  setup() {
    // 条件查询form
    const queryModel: Dispatch = reactive({});
    // 数据表格字典字段转换
    const dict = reactive({ map: {}, selectOption: {} as any });
    loadDictData((data: Array<Dict>) => {
      dict.map = convertDictMap(data);
    });
    const loadSelectOption = () => {
      request("/user/team/query", {}).then((response) => {
        const res = response as responseEntity;
        if (res.code === 0) {
          dict.selectOption.team = (res.data as any).records;
        }
      });
    };
    loadSelectOption();
    query("", "/user/dispatchRecord/query");
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
    handleDelete(index: number, row: any) {
      const confirmTitle = "删除";
      responseMsg.confirm((del: Function) => {
        request("/user/team/delete", { id: row.id }, "delete").then(
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