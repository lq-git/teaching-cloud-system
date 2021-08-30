<template>
  <div class="body">
    <img src="@/assets/img/forgetBg.png">

    <el-form ref="forgetForm" :model="registerUser" :rules="registerRules" label-width="100px" class="forgetForm sign-in-form">
      <div class="forget">
        <div class="forget_title">重置密码</div>
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerUser.password" type="password" placeholder="Enter Password..."></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="password2">
          <el-input v-model="registerUser.password2" type="password" placeholder="Enter Password..."></el-input>
        </el-form-item>
      </div>
      <el-form-item>
        <el-button @click="handleForget('forgetForm')" type="primary" class="submit-btn">提交</el-button>
      </el-form-item>
      <div class="tiparea">
        <p @click="gotoLogin"><a>立即登录</a></p>
      </div>
    </el-form>

  </div>

</template>
<script lang='ts'>
import { defineComponent } from "vue";
import { ElMessage } from "element-plus";
import { registerUser, registerRules } from "../utils/registerValidators";
import { request, responseMsg } from "../utils/request";
export default defineComponent({
  name: "ForgetPassword",
  setup() {
    return {
      registerUser,
      registerRules,
    };
  },
  methods: {
    resetForm(formName: string) {
      const _this = this as any;
      _this.$refs[formName].resetFields();
    },
    handleForget(formName: string) {
      const _this = this as any;
      const msgTitle = "重置密码";
      _this.$refs[formName].validate((valid: boolean) => {
        if (valid) {
          const forgetToken = _this.$route.query.forgetToken;
          request(
            "/admin/forgetPassword",
            { forgetToken: forgetToken, password: _this.registerUser.password },
            "post",
            true
          )
            .then((response) => {
              responseMsg.success(
                response,
                msgTitle,
                _this.resetForm(formName)
              );
            })
            .catch((error) => {
              responseMsg.error(error, msgTitle);
            });
        } else {
          ElMessage.error("请正确填写信息");
          return false;
        }
      });
    },
    gotoLogin() {
      return this.$router.replace({ name: "Login" });
    },
  },
});
</script>
<style scoped>
.body > img {
  width: 35%;
  height: 50%;
  position: absolute;
  bottom: 10px;
}

.forgetForm {
  margin: 200px auto;
  width: 50vh;
  background-color: #fff;
  padding: 20px 40px 20px 20px;
  border-radius: 5px;
  box-shadow: 0px 5px 10px #cccc;
}

.submit-btn {
  width: 100%;
}
.tiparea {
  text-align: right;
  font-size: 12px;
  color: #333;
}
.tiparea p a {
  color: #409eff;
  cursor: pointer;
}

.forget_title {
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  color: rgb(56, 56, 56);
  padding: 10px 0 30px 95px;
}
</style>