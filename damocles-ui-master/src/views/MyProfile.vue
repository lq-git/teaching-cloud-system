<template>
  <el-form label-position="left" ref="updateForm" :model="queryModel" class="updateForm">
    <el-form-item label="姓名" prop="nickName">
      <el-col :span="11">
        <el-input v-model="queryModel.nickName" placeholder="请输入姓名"></el-input>
      </el-col>
    </el-form-item>
    <el-form-item label="性别" prop="sex">
      <el-select v-model="queryModel.sex" clearable placeholder="请选择性别">
        <el-option v-for="item in dict.selectOption.sex" :key="item.id" :label="item.text" :value="item.code"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="手机" prop="phone">
      <el-col :span="11">
        <el-input v-model="queryModel.phone" placeholder="请输入手机号"></el-input>
      </el-col>
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-col :span="11">
        <el-input v-model="queryModel.email" placeholder="请输入邮箱"></el-input>
      </el-col>
    </el-form-item>
    <el-form-item label="头像" prop="icon">
      <el-col :span="11">
        <el-upload class="upload-demo" action="/api/admin/uploadImg" accept="image/*" :file-list="fileList" :limit="1" list-type="picture" :on-success="handleSuccess">
          <el-button size="small" type="primary">点击上传</el-button>
          <template #tip>
            <div class="el-upload__tip">
              只能上传 jpg/png 文件，且不超过 500kb
            </div>
          </template>
        </el-upload>
      </el-col>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm('updateForm')">提交</el-button>
      <el-button @click="resetForm('updateForm')">重新填写</el-button>
    </el-form-item>
  </el-form>
</template>
<script lang='ts'>
import { defineComponent, reactive } from "vue";
import { request, responseEntity, responseMsg } from "../utils/request";
import User from "../types/user";
import Dict from "../types/dict";
import {
  loadDictData,
  convertDictMap,
  convertDictList,
} from "../utils/dictUtil";
export default defineComponent({
  name: "MyProfile",
  setup() {
    const queryModel: User = reactive({});

    // 下拉框数据加载
    // 数据表格字典字段转换
    const dict = reactive({ map: {}, selectOption: {} as any });
    loadDictData((data: Array<Dict>) => {
      dict.map = convertDictMap(data);
      dict.selectOption = convertDictList(data);
    });
    const fileList: Array<any> = reactive([]);
    return {
      fileList,
      queryModel,
      dict,
    };
  },
  methods: {
    resetForm(formName: string) {
      const _this = this as any;
      _this.$refs[formName].resetFields();
    },
    submitForm(formName: string) {
      const msgTitle = "资料";
      const _this = this as any;
      request("/admin/changeProfile", this.queryModel, "post")
        .then((response) => {
          responseMsg.success(response, msgTitle, _this.resetForm(formName));
          this.$router.go(0);
        })
        .catch((error) => responseMsg.error(error, msgTitle));
    },
    handleSuccess(response: responseEntity) {
      const data = response.data as Array<string>;
      this.queryModel.icon = data[0];
    },
    init() {
      request("/admin/loginUser").then((response) => {
        const res = response as responseEntity;
        if (res.code === 0) {
          const data = res.data as User;
          this.queryModel.nickName = data.nickName;
          this.queryModel.sex = data.sex;
          this.queryModel.phone = data.phone;
          this.queryModel.email = data.email;
          this.queryModel.icon = data.icon;
          this.fileList.push({
            name: (this.queryModel.icon as string).split("/")[1],
            url: "/api/".concat(this.queryModel.icon as string),
          });
        }
      });
    },
  },
  beforeMount() {
    this.init();
  },
});
</script>
<style scoped>
</style>