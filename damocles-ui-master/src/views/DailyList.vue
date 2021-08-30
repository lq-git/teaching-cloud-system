<template>
  <el-form :inline="true" label-position="left" ref="queryForm" :model="queryModel" class="queryForm">
    <el-form-item label="作业状态" prop="status">
      <el-select v-model="queryModel.status" clearable placeholder="请选择状态">
        <el-option v-for="item in dict.selectOption.dailyStatus" :key="item.id" :label="item.text" :value="item.code"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click.prevent="query(queryModel)">查询</el-button>
      <el-button type="info" @click.prevent="resetForm('queryForm')">重置</el-button>
    </el-form-item>
  </el-form>
  <el-table :data="tableData.records" style="width: 100%">
    <el-table-column label="ID" prop="id"> </el-table-column>
    <el-table-column label="提交人" prop="author"></el-table-column>
    <el-table-column label="提交时间" prop="time" align="center"></el-table-column>
    <el-table-column label="专业" prop="major" align="center"></el-table-column>
    <el-table-column label="老师" prop="coach" align="center"></el-table-column>
    <!-- <el-table-column label="督导" prop="councilor" align="center"></el-table-column> -->
    <el-table-column label="内容" prop="content" v-if="false" align="center"></el-table-column>
    <el-table-column label="事件" prop="event" v-if="false" align="center"></el-table-column>
    <el-table-column label="表现" prop="starred" v-if="false" align="center"></el-table-column>
    <el-table-column label="收获" prop="harvest" v-if="false" align="center"></el-table-column>
    <el-table-column label="疑惑" prop="doubt" v-if="false" align="center"></el-table-column>
    <el-table-column label="帮助" prop="help" v-if="false" align="center"></el-table-column>
    <el-table-column label="状态" prop="status" align="center">
      <template #default="scope" v-if="dict.map.dailyStatus">
        {{ dict.map.dailyStatus[scope.row.status] }}
      </template>
    </el-table-column>
    <el-table-column label="创建时间" prop="createTime" align="center" :width="200">
      <template #default="scope">
        <i class="el-icon-time"></i>
        <span style="margin-left: 10px">{{ scope.row.createTime }}</span>
      </template>
    </el-table-column>
    <el-table-column label="操作" align="center" :width="400">
      <template #default="scope">
        <el-button size="mini" @click="handleCheck(scope.$index, scope.row)">查看</el-button>
        <el-button size="mini" @click="handleDoc(scope.$index, scope.row)"><i class="el-icon-document"></i></el-button>
      </template>
    </el-table-column>
  </el-table>
  <Page></Page>
  <el-dialog :title="dialogWrapper.title" v-model="dialogWrapper.visible" width="40%" center>
    <el-form ref="updateForm" :inline="true" :model="dialogForm.data" label-width="60px">
      <div class="daily_container">
        <div class="header one">
          <el-form-item label="ID" prop="id" v-if="false">
            <el-input v-model="dialogForm.data.id"></el-input>
          </el-form-item>
          <el-form-item label="提交人" prop="author">
            <el-input v-model="dialogForm.data.author"></el-input>
          </el-form-item>
          <el-form-item label="日期" prop="time">
            <el-input v-model="dialogForm.data.time"></el-input>
          </el-form-item>
        </div>
        <div class="header two">
          <el-form-item label="专业" prop="major">
            <el-input v-model="dialogForm.data.major"></el-input>
          </el-form-item>
          <el-form-item label=老师 prop="coach">
            <el-input v-model="dialogForm.data.coach"></el-input>
          </el-form-item>
          <!-- <el-form-item label="督导" prop="councilor">
            <el-input v-model="dialogForm.data.councilor"></el-input>
          </el-form-item> -->
        </div>
        <div class="daily_body">
          <div class="item">作业完成情况</div>
          <div class="item">1、作业名称？</div>
          <div class="item">2、作业内容？</div>
          <div class="item">3、今天有什么好的收获</div>
          <div class="item">4、今天有什么不懂的吗？</div>
          <div class="item">5、有什么需要教练或机构帮助的吗？</div>
          <el-input class="item" type="textarea" v-model="dialogForm.data.content"></el-input>
          <el-input class="item" type="textarea" v-model="dialogForm.data.event"></el-input>
          <el-input class="item" type="textarea" v-model="dialogForm.data.starred"></el-input>
          <el-input class="item" type="textarea" v-model="dialogForm.data.harvest"></el-input>
          <el-input class="item" type="textarea" v-model="dialogForm.data.doubt"></el-input>
          <el-input class="item" type="textarea" v-model="dialogForm.data.help"></el-input>
        </div>
        <div class="daily_footer">
          保密须知：此文件仅用于503B教研室内部传阅，未得到许可，项目所涉及的文件不得以任何形式外传或做其他用途，否则将承担相应的法律责任。
        </div>
      </div>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="cancel()">确认</el-button>
        <el-button @click="cancel()">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script lang='ts'>
import { defineComponent, onBeforeMount, reactive } from "vue";
import Daily from "../types/daily";
import { download } from "../utils/request";
import Page from "../components/Page.vue";
import { tableData, query } from "../components/Page.vue";
import Popups from "../types/popups";
import Dict from "../types/dict";
import { ElMessageBox, ElMessage } from "element-plus";
import {
  loadDictData,
  convertDictMap,
  convertDictList,
} from "../utils/dictUtil";
export default defineComponent({
  name: "DailyList",
  components: {
    Page,
  },
  setup() {
    // 条件查询form
    const queryModel = reactive({} as Daily);
    // 数据表格字典字段转换
    const dict = reactive({ map: {}, selectOption: {} });
    loadDictData((data: Array<Dict>) => {
      dict.selectOption = convertDictList(data);
      dict.map = convertDictMap(data);
    });
    // 弹出框构造
    const dialogForm = reactive({ data: {} as Daily });
    const dialogWrapper = reactive(new Popups());
    onBeforeMount(() => {
      query("", "/daily/list/query");
    });
    return {
      tableData,
      queryModel,
      query,
      dict,
      dialogForm,
      dialogWrapper,
    };
  },
  methods: {
    resetForm(formName: string) {
      const _this = this as any;
      _this.$refs[formName].resetFields();
    },
    handleCheck(index: number, row: any) {
      this.dialogWrapper.type = "check";
      this.dialogWrapper.visible = true;
      this.dialogWrapper.title = "查看作业";
      this.dialogForm.data = { ...row };
    },
    handleDoc(index: number, row: any) {
      const confirmTitle = "生成word文档";
      ElMessageBox.confirm("确定要" + confirmTitle + "吗, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          download("/daily/list/doc", row);
        })
        .catch(() => {
          ElMessage({
            type: "info",
            message: "已取消".concat(confirmTitle),
          });
        });
    },
    cancel() {
      this.dialogWrapper.visible = false;
      this.dialogForm.data = {} as Daily;
    },
  },
});
</script>
<style scoped>
</style>