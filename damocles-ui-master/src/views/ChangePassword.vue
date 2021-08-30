<template>
  <el-form :model="user" :rules="rules" ref="changeForm">
    <el-form-item class="oldPassword" label="旧的密码：" prop="oldPassword">
      <el-col :span="11">
        <el-input type="password" v-model="user.oldPassword" auto-complete="off"></el-input>
      </el-col>
    </el-form-item>
    <el-form-item label="新的密码：" prop="password">
      <el-col :span="11">
        <el-input type="password" v-model="user.password" auto-complete="off"></el-input>
      </el-col>
    </el-form-item>
    <el-form-item label="确认密码：" prop="password2">
      <el-col :span="11">
        <el-input type="password" v-model="user.password2" auto-complete="off"></el-input>
      </el-col>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click.prevent="submit('changeForm')">提交</el-button>
      <el-button @click="resetForm('changeForm')">重新填写</el-button>
    </el-form-item>
  </el-form>
</template>
<script lang='ts'>
import { defineComponent } from "vue";
import { user, rules } from "../utils/changePasswordVal";
import { request, responseEntity } from "../utils/request";
import { ElMessage } from "element-plus";
export default defineComponent({
  name: "ChangePassword",
  setup() {
    return {
      user,
      rules,
    };
  },
  methods: {
    resetForm(formName: string) {
      const _this = this as any;
      _this.$refs[formName].resetFields();
    },
    submit(formName: string) {
      const _this = this as any;
      _this.$refs[formName].validate((valid: boolean) => {
        if (valid) {
          request("/admin/changePassword", _this.user, "post", true).then(
            (response) => {
              const res = response as responseEntity;
              if (res.code === 0) {
                ElMessage({
                  showClose: true,
                  message: res.msg.concat(",3秒后回到登录页面"),
                  type: "success",
                  onClose: () => {
                    request("/admin/logout", {}, "post").then(() => {
                      localStorage.removeItem("token");
                      return this.$router.replace({ name: "Logout" });
                    });
                  },
                });
                return;
              }
              return ElMessage.error(res.msg);
            }
          );
        } else {
          ElMessage.error("请正确填写登录信息");
          return false;
        }
      });
    },
  },
});
</script>
<style  scoped>
.oldPassword {
  margin-left: 9px;
}
</style>