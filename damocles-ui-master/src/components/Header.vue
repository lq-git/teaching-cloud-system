<template>
  <el-button type="text" class="fold-toggle" @click="collapse.toggle"><i :class="collapse.isFoldIcon"></i></el-button>
  <div class="loginOption">
    <el-avatar :size="40" shape="square">
      <img :src="'/api/' + user.icon" @error="errorHandler" />
    </el-avatar>
    <el-dropdown @command="handleCommand">
      <span class="el-dropdown-link">
        {{ user.nickName }}<i class="el-icon-arrow-down el-icon--right"></i>
      </span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item command="MyProfile">个人中心</el-dropdown-item>
          <el-dropdown-item command="ChangePassword">修改密码</el-dropdown-item>
          <el-dropdown-item command="Logout">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>
<script lang='ts'>
import { defineComponent } from "vue";
import { request } from "../utils/request";
import errorHandler from "../utils/errorHandler";
export default defineComponent({
  name: "Header",
  props: {
    collapse: {
      type: Object,
      required: true,
    },
    user: {
      type: Object,
      required: true,
    },
  },
  setup() {
    return { errorHandler };
  },
  methods: {
    handleCommand(command: string) {
      if (command.toLowerCase() === "logout") {
        request("/admin/logout", {}, "post").then(() => {
          localStorage.removeItem("token");
          return this.$router.replace({ name: command });
        });
      }
      this.$router.push({ name: command, params: this.user });
    },
  },
});
</script>
<style lang="css" scoped>
.fold-toggle {
  float: left;
  width: 70px;
  height: 100%;
  color: #888;
  font-size: 30px;
}

.fold-toggle:hover {
  color: rgb(33, 33, 33);
}

.loginOption {
  width: 10%;
  height: 100%;
  margin-right: 10px;
  float: right;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-items: center;
}

.el-dropdown-link {
  user-select: none;
  cursor: pointer;
}
</style>