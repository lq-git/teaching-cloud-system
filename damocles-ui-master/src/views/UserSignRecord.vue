<template>
  <el-form :inline="true" label-position="left" ref="queryForm" :model="queryModel" class="queryForm">
    <el-form-item label="姓名" prop="nickName">
      <el-input v-model="queryModel.nickName" placeholder="请输入姓名"></el-input>
    </el-form-item>
    <el-form-item label="签到状态" prop="status">
      <el-select v-model="queryModel.status" clearable placeholder="请选择状态">
        <el-option v-for="item in dict.selectOption.signStatus" :key="item.id" :label="item.text" :value="item.code"></el-option>
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
    <el-table-column label="签到状态" prop="status" align="center">
      <template #default="scope" v-if="dict.map.signStatus">
        {{ dict.map.signStatus[scope.row.status] }}
      </template>
    </el-table-column>
    <el-table-column label="迟到时间" prop="latedTime" align="center">
      <template #default="scope">
        <!-- <i class="el-icon-time" v-if="scope.row.latedTime"></i> -->
        <!-- <span style="margin-left: 10px">{{ scope.row.latedTime }}</span> -->
        <el-date-picker v-if="scope.row.latedTime" v-model="scope.row.latedTime" :disabled="scope.row.signStatus != 'late'" type="datetime" placeholder="选择日期时间" :default-time="new Date(2000, 1, 1, 12, 0, 0)">
        </el-date-picker>
      </template>
    </el-table-column>
    <el-table-column label="签到时间" prop="createTime" align="center">
      <template #default="scope">
        <i class="el-icon-time" v-if="scope.row.createTime"></i>
        <span style="margin-left: 10px">{{ scope.row.createTime }}</span>
      </template>
    </el-table-column>
    <el-table-column label="更新时间" prop="updateTime" align="center">
      <template #default="scope">
        <i class="el-icon-time" v-if="scope.row.updateTime"></i>
        <span style="margin-left: 10px">{{ scope.row.updateTime }}</span>
      </template>
    </el-table-column>
    <!-- <el-table-column label="操作" align="center" :width="200">
      <template #default="scope">
        <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
        <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
      </template>
    </el-table-column> -->
  </el-table>
  <Page></Page>
</template>
<script lang='ts'>
import { defineComponent, reactive } from "vue";
// import { request, responseMsg } from "../utils/request";
import Page from "../components/Page.vue";
import { tableData, query } from "../components/Page.vue";
import Sign from "../types/sign";
import Dict from "../types/dict";
import {
  loadDictData,
  convertDictMap,
  convertDictList,
} from "../utils/dictUtil";
export default defineComponent({
  name: "UserSignRecord",
  components: {
    Page,
  },
  setup() {
    // 条件查询form
    const queryModel: Sign = reactive({});
    // 数据表格字典字段转换
    const dict = reactive({ map: {}, selectOption: {} });
    loadDictData((data: Array<Dict>) => {
      dict.selectOption = convertDictList(data);
      dict.map = convertDictMap(data);
    });
    query("", "/user/signRecord/query");
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