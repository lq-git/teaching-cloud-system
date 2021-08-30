<template>
  <el-form ref="loginForm" :model="loginUser" :rules="rules" label-width="100px" class="loginForm sign-in-form">
    <div class="login" v-if="!forgetFlag">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="loginUser.username" placeholder="Enter Email..."></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="loginUser.password" type="password" placeholder="Enter Password..."></el-input>
      </el-form-item>
      <el-form-item label="验证码" prop="verifyCode">
        <div class="verifyCode">
          <el-input v-model="loginUser.verifyCode" type="text" placeholder="Enter verifyCode..."></el-input>
          <el-image :src="verifyCodeUrl" @click="toggleVerifyCode" @error="errorHandler"></el-image>
        </div>
      </el-form-item>
    </div>
    <div class="forget" v-show="forgetFlag">
      <div class="forget_title">找回密码</div>
      <el-form-item label="邮箱" prop="username">
        <el-input v-model="loginUser.username" placeholder="Enter binding Email..."></el-input>
      </el-form-item>
    </div>
    <el-form-item>
      <el-button @click="forgetFlag ? handleForget('loginForm') :handleLogin('loginForm')" type="primary" class="submit-btn">提交</el-button>
    </el-form-item>
    <!-- 找回密码 -->
    <div class="tiparea">
      <p @click="forgetFlag=!forgetFlag"><template v-if="!forgetFlag">忘记密码？ <a>立即找回</a></template><template v-else><a>立即登录</a></template></p>
    </div>
  </el-form>
</template>

<script lang="ts">
import { loginUser, rules } from "../utils/loginValidators";
import { request, responseEntity, responseMsg } from "../utils/request";
import { ElMessage } from "element-plus";
import errorHandler from "../utils/errorHandler";
import { ref } from "vue";
export default {
  setup() {
    // 切换验证码
    const verifyCodeUrl = ref("/api/verifyCodeImg");
    const toggleVerifyCode = () => {
      const randomNum = Math.random();
      verifyCodeUrl.value = verifyCodeUrl.value.concat("?random=") + randomNum;
    };
    const forgetFlag = ref(false);
    return {
      loginUser,
      rules,
      errorHandler,
      verifyCodeUrl,
      toggleVerifyCode,
      forgetFlag,
    };
  },
  methods: {
    handleLogin(formName: string) {
      const _this = this as any;
      _this.$refs[formName].validate((valid: boolean) => {
        if (valid) {
          request("/admin/login", _this.loginUser, "post", true).then(
            (response) => {
              const res = response as responseEntity;
              if (res.code === 0) {
                const data = res.data as { tokenHead: ""; access_token: "" };
                const authToken = data.tokenHead
                  .concat(" ")
                  .concat(data.access_token);
                localStorage.setItem("token", authToken);
                _this.$router.replace({ name: "Home" });
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
    handleForget(formName: string) {
      const _this = this as any;
      const msgTitle = "提交";
      _this.$refs[formName].validate((valid: boolean) => {
        if (valid) {
          request(
            "/admin/forget",
            { email: _this.loginUser.username },
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
    resetForm(formName: string) {
      const _this = this as any;
      _this.$refs[formName].resetFields();
    },
  },
};
</script>
<style scoped>
/* form */
.loginForm {
  margin-top: 20px;
  background-color: #fff;
  padding: 20px 40px 20px 20px;
  border-radius: 5px;
  box-shadow: 0px 5px 10px #cccc;
}

.loginForm .verifyCode {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
}

.loginForm .verifyCode > .el-image {
  width: 30%;
  height: 40px;
  border: 1px solid #ccc;
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
  padding: 10px 0 20px 95px;
}
</style>