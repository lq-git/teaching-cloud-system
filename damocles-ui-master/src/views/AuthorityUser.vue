<template>
  <el-form :inline="true" label-position="left" ref="queryForm" :model="queryModel" class="queryForm">
    <el-form-item label="姓名" prop="nickName">
      <el-input v-model="queryModel.nickName" placeholder="请输入姓名"></el-input>
    </el-form-item>
    <el-form-item label="用户名" prop="username">
      <el-input v-model="queryModel.username" placeholder="请输入用户名"></el-input>
    </el-form-item>
    <el-form-item label="角色" prop="roleId">
      <el-select v-model="queryModel.roleId" clearable placeholder="请选择角色" @click.once="loadSelectOption">
        <div v-show="false">{{ dict.selectOption }}</div>
        <el-option v-for="item in dict.selectOption.roleType" :key="item.id" :label="item.name" :value="item.id"></el-option>
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
    <el-table-column prop="roleId" v-if="false"></el-table-column>
    <el-table-column label="角色" prop="roleName" :width="80"></el-table-column>
    <el-table-column label="状态" prop="status" align="center" :width="80">
      <template #default="scope" v-if="dict.map.userStatus">
        {{ dict.map.userStatus[scope.row.status] }}
      </template>
    </el-table-column>
    <el-table-column label="操作" align="center">
      <template #default="scope">
        <el-button size="mini" type="primary" @click="handleAllot(scope.$index, scope.row)">分配角色</el-button>
        <template v-if="scope.row.status === 'normal'">
          <el-button size="mini" type="danger" @click="handleFreeze(scope.$index, scope.row)">冻结账户</el-button>
        </template>
        <template v-else-if="scope.row.status === 'freeze'">
          <el-button size="mini" type="success" @click="handleUnFreeze(scope.$index, scope.row)">解冻账户</el-button>
        </template>
      </template>
    </el-table-column>
  </el-table>
  <Page></Page>
  <el-dialog :title="dialogWrapper.title" v-model="dialogWrapper.visible" width="40%">
    <el-form ref="updateForm" :inline="true" :model="dialogForm.data" label-width="150px">
      <el-form-item label="ID" prop="id" v-if="false">
        <el-input v-model="dialogForm.data.id"></el-input>
      </el-form-item>
      <el-form-item label="用户" prop="nickName">
        <el-input v-model="dialogForm.data.nickName" disabled></el-input>
      </el-form-item>
      <el-form-item label="角色" prop="roleId">
        <el-select v-model="dialogForm.data.roleId" clearable placeholder="请选择角色">
          <div v-show="false">{{ dict.selectOption }}</div>
          <el-option v-for="item in dict.selectOption.roleType" :key="item.id" :label="item.name" :value="item.id"></el-option>
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
  <el-dialog :title="freezeWrapper.title" v-model="freezeWrapper.visible" width="30%">
    <div class="block">
      <span class="demonstration">选择冻结日期</span>
      <el-date-picker v-model="freezeWrapper.freezeDate" type="datetime" :disabledDate="disabledDate" placeholder="选择日期时间">
      </el-date-picker>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="freeze()">保存</el-button>
        <el-button @click="cancel()">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script lang='ts'>
import { defineComponent, onBeforeMount, reactive } from "vue";
import { request, responseEntity, responseMsg } from "../utils/request";
import Page from "../components/Page.vue";
import { tableData, query } from "../components/Page.vue";
import User from "../types/user";
import errorHandler from "../utils/errorHandler";
import Dict from "../types/dict";
import { loadDictData, convertDictMap } from "../utils/dictUtil";
export default defineComponent({
  name: "AuthorityUser",
  components: {
    Page,
  },
  setup() {
    query("", "/authority/user/query");
    const queryModel: User = reactive({});
    // 下拉框数据加载
    // 数据表格字典字段转换
    const dict = reactive({ map: {}, selectOption: {} as any });
    loadDictData((data: Array<Dict>) => {
      dict.map = convertDictMap(data);
    });
    const selectOption = reactive({ roleType: {} });
    const loadSelectOption = async () => {
      await request("/authority/role/query").then((response) => {
        const res = response as responseEntity;
        dict.selectOption.roleType = (res.data as any).records;
      });
    };
    // 弹出框构造
    const dialogForm = reactive({ data: {} as User });
    const dialogWrapper = reactive({
      type: "",
      visible: false,
      title: "",
    });
    // 冻结参数
    const freezeWrapper = reactive({
      visible: false,
      title: "冻结账户",
      freezeDate: "",
      username: "",
    });
    onBeforeMount(() => {
      loadSelectOption();
    });
    return {
      queryModel,
      selectOption,
      loadSelectOption,
      tableData,
      query,
      errorHandler,
      dialogForm,
      dialogWrapper,
      freezeWrapper,
      dict,
    };
  },
  methods: {
    resetForm(formName: string) {
      const _this = this as any;
      _this.$refs[formName].resetFields();
    },
    handleAllot(index: number, row: any) {
      this.dialogWrapper.type = "allot";
      this.dialogWrapper.visible = true;
      this.dialogWrapper.title = "分配角色";
      this.dialogForm.data = { ...row };
    },
    handleFreeze(index: number, row: any) {
      this.freezeWrapper.visible = true;
      this.freezeWrapper.username = row.username;
    },
    freeze() {
      const msgTitle = "冻结";
      const timezone = 8;
      const nowDate = new Date(this.freezeWrapper.freezeDate).getTime();
      const targetDate = new Date(nowDate + timezone * 60 * 60 * 1000);
      request(
        "/authority/user/freeze",
        {
          username: this.freezeWrapper.username,
          freezeDate: targetDate,
        },
        "post",
        true
      )
        .then((response) => responseMsg.success(response, msgTitle, query))
        .catch((error) => responseMsg.error(error, msgTitle));
      this.freezeWrapper.visible = false;
    },
    handleUnFreeze(index: number, row: any) {
      const confirmTitle = "解冻";
      responseMsg.confirm((unFreeze: Function) => {
        request(
          "/authority/user/unfreeze",
          { username: row.username },
          "post",
          true
        ).then((response) => unFreeze(response, confirmTitle, query));
      }, confirmTitle);
    },
    save() {
      const msgTitle = this.dialogWrapper.title;
      request("/authority/user/assignRole", this.dialogForm.data, "post", true)
        .then((response) => responseMsg.success(response, msgTitle, query))
        .catch((error) => responseMsg.error(error, msgTitle));
      this.cancel();
    },
    cancel() {
      this.dialogWrapper.visible = false;
      this.dialogForm.data = {} as User;
      this.freezeWrapper.visible = false;
    },
    disabledDate(time: Date) {
      return time.getTime() <= Date.now() - 24 * 60 * 60 * 1000;
    },
  },
});
</script>
<style scoped>
.block {
  height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
}
</style>