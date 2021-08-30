<template>
  <div>
    <el-form :inline="true" label-position="left" ref="queryForm" :model="queryModel" class="queryForm">
      <el-form-item label="小组名称" prop="teamName">
        <el-input v-model="queryModel.teamName" placeholder="请输入小组名称"></el-input>
      </el-form-item>
      <el-form-item label="学习阶段" prop="stage">
        <el-input v-model="queryModel.stage" placeholder="请输入学习阶段"></el-input>
      </el-form-item>
      <el-form-item label="小组长" prop="teamLeaderId">
        <el-select v-model="queryModel.teamLeaderId" clearable placeholder="请选择小组长">
          <el-option v-for="item in selectOption.teamLeader" :key="item.id" :label="item.nickName" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="教练" prop="teamCoachId">
        <el-select v-model="queryModel.teamCoachId" clearable placeholder="请选择教练">
          <el-option v-for="item in selectOption.teamCoach" :key="item.id" :label="item.nickName" :value="item.id"></el-option>
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
      <el-table-column label="小组名称" prop="teamName"></el-table-column>
      <el-table-column label="所在教室" prop="classroom"></el-table-column>
      <el-table-column label="学习阶段" prop="stage"></el-table-column>
      <el-table-column prop="teamLeaderId" v-if="false"></el-table-column>
      <el-table-column label="小组长" prop="teamLeaderName"></el-table-column>
      <el-table-column prop="teamCoachId" v-if="false"></el-table-column>
      <el-table-column label="教练" prop="teamCoachName"></el-table-column>
      <el-table-column label="创建时间" prop="createTime" align="center" :width="200">
        <template #default="scope">
          <i class="el-icon-time"></i>
          <span style="margin-left: 10px">{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" :width="400">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button size="mini" type="success" plain @click="handleCheck(scope.row)">查看成员</el-button>
          <el-button size="mini" type="success" plain @click="handleAllot(scope.row)">分配成员</el-button>
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
        <el-form-item label="小组名称" prop="teamName">
          <el-input v-model="dialogForm.data.teamName" placeholder="请输入小组名称"></el-input>
        </el-form-item>
        <el-form-item label="所在教室" prop="classroom">
          <el-input v-model="dialogForm.data.classroom" placeholder="请输入所在教室"></el-input>
        </el-form-item>
        <el-form-item label="学习阶段" prop="stage">
          <el-input v-model="dialogForm.data.stage" placeholder="请输入学习阶段"></el-input>
        </el-form-item>
        <el-form-item label="小组长" prop="teamLeaderId" v-show="dialogWrapper.type !== 'add'">
          <el-select v-model="dialogForm.data.teamLeaderId" clearable placeholder="请选择小组长">
            <el-option v-for="item in checkWrapper.data" :key="item.id" :label="item.nickName" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="教练" prop="teamCoachId">
          <el-select v-model="dialogForm.data.teamCoachId" clearable placeholder="请选择教练">
            <el-option v-for="item in selectOption.teamCoach" :key="item.id" :label="item.nickName" :value="item.id"></el-option>
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
    <el-dialog :title="checkWrapper.title" v-model="checkWrapper.visible" width="40%" center>
      <div class="container" v-if="checkWrapper.data.length">
        <div class="memberBox" v-for="(item, index) in checkWrapper.data" :key="index">
          <el-avatar class="headImg" :size="80" shape="square">
            <img :src="'/api/' + item.icon" @error="errorHandler" />
          </el-avatar>
          <span class="username">{{ item.nickName }}</span>
          <span class="role">{{
            item.id === checkWrapper.teamLeaderId ? "组长" : "成员"
          }}</span>
        </div>
      </div>
      <div v-else style="text-align: center">
        <span style="font-size: 18px">暂无成员</span>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="cancel()">确认</el-button>
          <el-button @click="cancel()">关闭</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog :title="allotWrapper.title" v-model="allotWrapper.visible" width="40%">
      <div style="text-align: center">
        <el-transfer v-model="transferData.value" :props="{ key: 'id', label: 'nickName', disabled: 'status' }" :data="transferData.data" style="text-align: left; display: inline-block" filterable :titles="[
            '未分组' + allotWrapper.title.substring(2),
            '当前小组' + allotWrapper.title.substring(2),
          ]" :button-texts="['移除', '分配']" :format="{
            noChecked: '${total}',
            hasChecked: '${checked}/${total}',
          }" />
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="saveAllot()">保存</el-button>
          <el-button @click="cancel()">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script lang='ts'>
import { defineComponent, reactive } from "vue";
import User from "../types/user";
import Team from "../types/team";
import { request, responseEntity, responseMsg } from "../utils/request";
import Page from "../components/Page.vue";
import { tableData, query } from "../components/Page.vue";
import Popups from "../types/popups";
import errorHandler from "../utils/errorHandler";
export default defineComponent({
  name: "UserTeam",
  components: {
    Page,
  },
  setup() {
    // 条件查询form
    const queryModel = reactive({} as Team);
    // 弹出框构造
    const dialogForm = reactive({ data: {} as Team });
    const dialogWrapper = reactive(new Popups());
    const selectOption = reactive({
      teamLeader: [] as Array<User>,
      teamCoach: [] as Array<User>,
    });
    const loadSelectOption = (roleCode: string, callback: Function) => {
      request("/user/queryUserMapByRoleType", { roleCode: roleCode }).then(
        (response) => {
          const res = response as responseEntity;
          if (res.code === 0) {
            callback(res.data);
          }
        }
      );
    };
    loadSelectOption("leader", (data: any) => (selectOption.teamLeader = data));
    loadSelectOption("coach", (data: any) => (selectOption.teamCoach = data));
    query("", "/user/team/query");
    const checkWrapper = reactive({
      title: "查看成员",
      visible: false,
      data: [] as Array<any>,
      teamLeaderId: -1,
    });
    // 成员分配
    const allotForm = reactive({ teamId: "", userIds: [] as Array<any> });
    const allotWrapper = reactive(new Popups("分配成员", "allot"));
    return {
      query,
      queryModel,
      tableData,
      dialogForm,
      dialogWrapper,
      selectOption,
      checkWrapper,
      allotForm,
      allotWrapper,
      errorHandler,
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
      this.dialogWrapper.title = "添加小组";
    },
    handleEdit(index: number, row: any) {
      this.dialogWrapper.type = "edit";
      this.dialogWrapper.title = "编辑小组";
      this.dialogForm.data = { ...row };
      this.getTeamMembers(row.id, () => (this.dialogWrapper.visible = true));
    },
    handleDelete(index: number, row: any) {
      const confirmTitle = "删除";
      responseMsg.confirm((del: Function) => {
        request("/user/team/delete", { teamId: row.id }, "delete").then(
          (response) => {
            del(response, confirmTitle, query);
          }
        );
      }, confirmTitle);
    },
    handleCheck(row: any) {
      const _this = this as any;
      _this.checkWrapper.teamLeaderId = row.teamLeaderId;
      _this.getTeamMembers(row.id, () => (this.checkWrapper.visible = true));
    },
    handleAllot(row: any) {
      const _this = this as any;
      const requestUrl = "/user/team/queryMemberMap";
      this.allotForm.teamId = row.id;
      request(requestUrl, { teamId: row.id }).then((response) => {
        const res = response as { data: []; value: [] };
        _this.transferData = res.data.map((item: any) => {
          item.status = item.status !== "normal";
          return item;
        });
        _this.transferData.data = res.data;
        _this.transferData.value = res.value;
        _this.allotWrapper.visible = true;
      });
    },
    save() {
      const _this = this as any;
      let queryUrl = "";
      switch (_this.dialogWrapper.type) {
        case "add":
          queryUrl = "/user/team/create";
          break;
        case "edit":
          queryUrl = "/user/team/update";
          break;
      }
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
    saveAllot() {
      const _this = this as any;
      request(
        "/user/team/assignMembers",
        { teamId: _this.allotForm.teamId, userIds: _this.transferData.value },
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
      this.cancel();
    },
    getTeamMembers(id: number, callback: Function) {
      const _this = this as any;
      request("/user/team/checkMembers", { teamId: id }).then((response) => {
        const res = response as responseEntity;
        if (res.code === 0) {
          _this.checkWrapper.data = res.data;
          callback();
        }
      });
    },
    cancel() {
      this.dialogWrapper.visible = false;
      this.dialogForm.data = {} as Team;
      this.checkWrapper.visible = false;
      this.allotWrapper.visible = false;
    },
  },
});
</script>
<style scoped>
.container {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  margin: 10px auto;
}

.memberBox {
  width: 20%;
  padding: 10px 0 10px;
  margin-bottom: 10px;
  border: 1px solid rgba(163, 163, 163, 0.29);
  background-color: rgba(206, 206, 206, 0.26);
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
}

.memberBox .headImg {
  width: 60%;
  height: 60%;
  border-radius: 50%;
  font-size: 20px;
  line-height: 300px;
  text-align: center;
}

.memberBox .username {
  font-weight: bold;
  color: #ffb800;
}

.memberBox .role {
  color: #ffffff;
  line-height: 20px;
  text-align: center;
  border-radius: 3px;
  width: 40%;
  height: 15%;
  background: #1e9fff;
}
</style>