<template>
  <el-form :inline="true" label-position="left" ref="queryForm" :model="queryModel" class="queryForm">
    <el-form-item label="权限名称" prop="name">
      <el-input v-model="queryModel.name" placeholder="请输入权限名称"></el-input>
    </el-form-item>
    <el-form-item label="权限类型" prop="type">
      <el-select v-model="queryModel.type" clearable placeholder="请选择类型">
        <el-option v-for="item in dict.selectOption.authType" :key="item.id" :label="item.text" :value="item.code"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click.prevent="query(queryModel)">查询</el-button>
      <el-button type="info" @click.prevent="resetForm('queryForm')">重置</el-button>
      <el-button type="success" @click.prevent="handleAdd">新建</el-button>
    </el-form-item>
  </el-form>
  <el-table :data="tableData.records" style="width: 100%">
    <el-table-column label="ID" prop="id"> </el-table-column>
    <el-table-column label="权限名称" prop="name"></el-table-column>
    <el-table-column label="权限值" prop="value" align="center"></el-table-column>
    <el-table-column label="图标" prop="icon" align="center">
      <template #default="scope"><i :class="scope.row.icon ? scope.row.icon : 'el-icon-more-outline'"></i></template>
    </el-table-column>
    <el-table-column label="类型" prop="type" align="center">
      <template #default="scope" v-if="dict.map.authType">
        {{ dict.map.authType[scope.row.type] }}
      </template>
    </el-table-column>
    <el-table-column label="资源路径" prop="uri"></el-table-column>
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
      <el-form-item label="权限名称" prop="name">
        <el-input v-model="dialogForm.data.name" placeholder="请输入权限名称"></el-input>
      </el-form-item>
      <el-form-item label="权限值" prop="value">
        <el-input v-model="dialogForm.data.value" placeholder="请输入权限值">
        </el-input>
      </el-form-item>
      <el-form-item label="显示图标" prop="icon" v-if="dialogForm.data.type == 0">
        <el-input v-model="dialogForm.data.icon" placeholder="请输入">
        </el-input>
      </el-form-item>
      <el-form-item label="权限类型" prop="type">
        <el-select v-model="dialogForm.data.type" clearable>
          <el-option v-for="item in dict.selectOption.authType" :key="item.id" :label="item.text" :value="parseInt(item.code)">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="资源路径" prop="uri" v-if="dialogForm.data.type != 0">
        <el-input v-model="dialogForm.data.uri" placeholder="请输入">
        </el-input>
      </el-form-item>
      <el-form-item label="父权限节点" prop="pid">
        <el-select v-model="dialogForm.data.pid">
          <el-option :value='0'>
            <el-tree :props="{label:'name'}" :data="authTree" show :check-on-click-node="true" @current-change="handleCurrentChange" accordion empty-text="暂无数据">
            </el-tree>
          </el-option>
        </el-select>
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
</template>
<script lang='ts'>
import { defineComponent, onBeforeMount, reactive, ref } from "vue";
import { Authority } from "../types/authority";
import { request, responseEntity, responseMsg } from "../utils/request";
import Page from "../components/Page.vue";
import { tableData, query } from "../components/Page.vue";
import Dict from "../types/dict";
import {
  loadDictData,
  convertDictList,
  convertDictMap,
} from "../utils/dictUtil";
export default defineComponent({
  name: "Authority",
  components: {
    Page,
  },
  setup() {
    query("", "/authority/index/query");
    // 条件查询form
    const queryModel = reactive({} as Authority);
    // 数据表格字典字段转换
    const dict = reactive({ map: {}, selectOption: {} });
    loadDictData((data: Array<Dict>) => {
      dict.selectOption = convertDictList(data);
      dict.map = convertDictMap(data);
    });
    // 弹出框构造
    const dialogForm = reactive({ data: {} as Authority });
    const dialogWrapper = reactive({
      type: "",
      visible: false,
      title: "",
    });
    const authTree = ref({});
    const loadAuthTree = () => {
      request("/authority/authTree").then((response) => {
        const res = response as responseEntity;
        // authTree.value = { label: "damocles", children: res.data };
        authTree.value = res.data;
      });
    };
    onBeforeMount(() => {
      loadAuthTree();
    });
    return {
      tableData,
      queryModel,
      query,
      dict,
      dialogForm,
      dialogWrapper,
      authTree,
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
      this.dialogWrapper.title = "添加权限";
    },
    handleEdit(index: number, row: any) {
      this.dialogWrapper.type = "edit";
      this.dialogWrapper.visible = true;
      this.dialogWrapper.title = "编辑权限";
      this.dialogForm.data = { ...row };
    },
    handleDelete(index: number, row: any) {
      const confirmTitle = "删除";
      responseMsg.confirm((del: Function) => {
        request("/authority/index/delete", { id: row.id }, "delete").then(
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
          ? "/authority/index/create"
          : "/authority/index/update";
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
      this.dialogForm.data = {} as Authority;
    },
    handleCurrentChange(current: any) {
      this.dialogForm.data.pid = current.id;
    },
  },
});
</script>
<style scoped>
el-option {
  z-index: -100;
}

.el-select-dropdown__item {
  height: auto;
}
</style>