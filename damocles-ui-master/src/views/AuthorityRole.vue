<template>
  <el-form :inline="true" label-position="left" ref="queryForm" :model="queryModel" class="queryForm">
    <el-form-item label="角色名称" prop="name">
      <el-input v-model="queryModel.name" placeholder="请输入角色名称"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click.prevent="query(queryModel)">查询</el-button>
      <el-button type="info" @click.prevent="resetForm('queryForm')">重置</el-button>
      <el-button type="success" @click.prevent="handleAdd">新建</el-button>
    </el-form-item>
  </el-form>
  <el-table :data="tableData.records" style="width: 100%">
    <el-table-column label="ID" prop="id"> </el-table-column>
    <el-table-column label="角色编号" prop="code"></el-table-column>
    <el-table-column label="角色名称" prop="name"></el-table-column>
    <el-table-column label="描述" prop="description" align="center"></el-table-column>
    <el-table-column label="状态" prop="status" align="center">
      <template #default="scope" v-if="dict.map.status">
        {{ dict.map.status[scope.row.status] }}
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
        <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
        <el-button size="mini" type="success" plain @click="handleAllot('menu', scope.row)">分配菜单</el-button>
        <el-button size="mini" type="success" plain @click="handleAllot('auth', scope.row)">分配权限</el-button>
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
      <el-form-item label="角色编号" prop="code">
        <el-input v-model="dialogForm.data.code" placeholder="请输入角色编号"></el-input>
      </el-form-item>
      <el-form-item label="角色名称" prop="name">
        <el-input v-model="dialogForm.data.name" placeholder="请输入权限名称"></el-input>
      </el-form-item>
      <el-form-item label="角色描述" prop="description">
        <el-input type="textarea" v-model="dialogForm.data.description" placeholder="请输入角色描述">
        </el-input>
      </el-form-item>
      <el-form-item label="是否启用" prop="status">
        <el-switch v-model="dialogForm.data.status" :active-value="1" :inactive-value="0"></el-switch>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="save()">保存</el-button>
        <el-button @click="cancel()">关闭</el-button>
      </div>
    </template>
  </el-dialog>
  <el-dialog :title="allotWrapper.title" v-model="allotWrapper.visible" width="40%">
    <div style="text-align: center">
      <el-transfer v-model="transferData.value" :props="{ key: 'id', label: 'name', disabled: 'status' }" :data="transferData.data" style="text-align: left; display: inline-block" filterable :titles="[
          '未拥有' + allotWrapper.title.substring(2),
          '已拥有' + allotWrapper.title.substring(2),
        ]" :button-texts="['移除', '分配']" :format="{
          noChecked: '${total}',
          hasChecked: '${checked}/${total}',
        }" />
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="saveAllot()">保存</el-button>
        <el-button @click="cancelAllot()">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script lang='ts'>
import { defineComponent, onBeforeMount, reactive } from "vue";
import { Role } from "../types/role";
import { request, responseMsg } from "../utils/request";
import Page from "../components/Page.vue";
import { tableData, query } from "../components/Page.vue";
import Popups from "../types/popups";
import Dict from "../types/dict";
import { loadDictData, convertDictMap } from "../utils/dictUtil";
export default defineComponent({
  name: "AuthorityRole",
  components: {
    Page,
  },
  setup() {
    // 条件查询form
    const queryModel = reactive({} as Role);
    // 弹出框构造
    const dialogForm = reactive({ data: {} as Role });
    const dialogWrapper = reactive(new Popups());
    // 权限分配
    const allotForm = reactive({ roleId: "", authIds: [] as Array<any> });
    const allotWrapper = reactive(new Popups());
    // 数据表格字典字段转换
    const dict = reactive({ map: {} });
    loadDictData((data: Array<Dict>) => {
      dict.map = convertDictMap(data, "status");
    });
    onBeforeMount(() => {
      query("", "/authority/role/query");
    });
    return {
      queryModel,
      tableData,
      query,
      dialogForm,
      dialogWrapper,
      allotForm,
      allotWrapper,
      dict,
    };
  },
  data() {
    return {
      transferData: { data: [], value: [] },
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
      this.dialogWrapper.title = "添加角色";
    },
    handleEdit(index: number, row: any) {
      this.dialogWrapper.type = "edit";
      this.dialogWrapper.visible = true;
      this.dialogWrapper.title = "编辑角色";
      this.dialogForm.data = { ...row };
    },
    handleDelete(index: number, row: any) {
      const confirmTitle = "删除";
      responseMsg.confirm((del: Function) => {
        request("/authority/role/delete", { roleId: row.id }, "delete").then(
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
          ? "/authority/role/create"
          : "/authority/role/update";
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
      this.dialogForm.data = {} as Role;
    },
    handleAllot(type: string, row: any) {
      this.loadTransferData(row.id, type);
      this.allotWrapper.title = type === "menu" ? "分配菜单" : "分配权限";
      this.allotWrapper.type = type;
      this.allotForm.roleId = row.id;
    },
    cancelAllot() {
      this.allotWrapper.visible = false;
      this.allotForm.roleId = "";
      this.allotForm.authIds = [];
    },
    loadTransferData(roleId: number, type: string) {
      const _this = this as any;
      const requestUrl =
        type === "menu"
          ? "/authority/role/queryMenuMap"
          : "/authority/role/queryAuthMap";
      request(requestUrl, { roleId: roleId }).then((response) => {
        const res = response as { data: []; value: [] };
        _this.transferData = res.data.map((item: any) => {
          item.status = item.status === 0;
          return item;
        });
        _this.transferData.data = res.data;
        _this.transferData.value = res.value;
        _this.allotWrapper.visible = true;
      });
    },
    saveAllot() {
      const _this = this as any;
      const requestUrl =
        _this.allotWrapper.type === "auth"
          ? "/authority/role/assignAuthority"
          : "/authority/role/assignMenu";
      console.log();
      request(
        requestUrl,
        { roleId: _this.allotForm.roleId, authIds: _this.transferData.value },
        "post",
        true
      )
        .then((response) =>
          responseMsg.success(
            response,
            _this.allotWrapper.title,
            _this.cancelAllot
          )
        )
        .catch((error) => responseMsg.error(error, _this.allotWrapper.title));
    },
  },
});
</script>
<style scoped>
</style>