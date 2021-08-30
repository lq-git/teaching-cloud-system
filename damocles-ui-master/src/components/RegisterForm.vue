<template>
  <el-form ref="registerForm" :model="registerUser" :rules="registerRules" label-width="100px" class="registerForm sign-up-form">
    <el-form-item label="姓名" prop="nickName">
      <el-input v-model="registerUser.nickName" placeholder="Enter NickName..."></el-input>
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="registerUser.email" placeholder="Enter Email..."></el-input>
    </el-form-item>
    <el-form-item label="密码" prop="password">
      <el-input v-model="registerUser.password" type="password" placeholder="Enter Password..."></el-input>
    </el-form-item>
    <el-form-item label="确认密码" prop="password2">
      <el-input v-model="registerUser.password2" type="password" placeholder="Enter Password..."></el-input>
    </el-form-item>

    <el-form-item>
      <el-button @click="handleRegister('registerForm')" type="primary" class="submit-btn">注册</el-button>
    </el-form-item>
  </el-form>
</template>
<script lang='ts'>
import { defineComponent } from "vue";
import { registerUser, registerRules } from "../utils/registerValidators";
import { request, responseEntity } from "../utils/request";
import { ElMessage } from "element-plus";
export default defineComponent({
  name: "RegisterForm",
  setup() {
    return {
      registerUser,
      registerRules,
    };
  },
  methods: {
    // 注册
    handleRegister(formName: string) {
      const _this = this as any;
      _this.$refs[formName].validate((valid: boolean) => {
        if (valid) {
          request("/admin/register", _this.registerUser, "post").then(
            (response) => {
              const res = response as responseEntity;
              if (res.code == 0) {
                _this.$refs[formName].resetFields();
                return ElMessage.success(res.msg);
              }
              return ElMessage.error(res.msg);
            }
          );
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
  },
});
</script>
<style scoped>
</style>