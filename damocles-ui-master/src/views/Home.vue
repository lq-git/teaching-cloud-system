<template>
  <div class="home">
    <el-container>
      <el-container>
        <Aside :collapse="collapse" :user="user" />
        <el-container class="home-container">
          <el-header class="home-haeder">
            <Header :collapse="collapse" :user="user" />
          </el-header>
          <el-main class="home-main">
            <Main />
          </el-main>
          <el-footer class="home-footer">
            <Footer />
          </el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<script lang="ts">
import { defineComponent, onBeforeMount, reactive } from "vue";
import Aside from "../components/Aside.vue";
import Main from "../components/Main.vue";
import Header from "../components/Header.vue";
import Footer from "../components/Footer.vue";
import User from "../types/user";
import { ElMessage } from "element-plus";
import { request, responseEntity } from "../utils/request";

export default defineComponent({
  name: "Home",
  components: {
    Aside,
    Main,
    Header,
    Footer,
  },
  setup() {
    // 菜单栏折叠
    const collapse = reactive({
      isFoldIcon: "el-icon-s-fold",
      isCollapse: false,
      headbarSize: 80,
      headbarHeight: "120px",
      toggle: () => {
        if (collapse.isCollapse) {
          collapse.isFoldIcon = "el-icon-s-fold";
          collapse.headbarSize = 80;
          collapse.headbarHeight = "120px";
        } else {
          collapse.isFoldIcon = "el-icon-s-unfold";
          collapse.headbarSize = 40;
          collapse.headbarHeight = "50px";
        }
        collapse.isCollapse = !collapse.isCollapse;
      },
    });
    // 获取用户信息
    const user: User = reactive({
      id: 1,
      username: "系统管理员",
      password: "123456",
      nickName: "向零",
      icon: "images/admin.png",
    });
    const loadUser = async () => {
      await request("/admin/loginUser").then((response) => {
        const res = response as responseEntity;
        if (res.code === 0) {
          const data = res.data as User;
          user.id = data.id;
          user.username = data.username;
          user.nickName = data.nickName;
          user.sex = data.sex;
          user.phone = data.phone;
          user.email = data.email;
          user.icon = data.icon;
          ElMessage.success("欢迎回来，" + user.nickName + "!");
        }
      });
    };
    onBeforeMount(() => {
      loadUser();
    });
    return {
      collapse,
      user,
    };
  },
});
</script>
<style scoped>
.el-header,
.el-footer {
  background-color: rgb(255, 255, 255);
  color: #333;
  text-align: center;
  line-height: 10vh;
  padding: 0;
}

.home-haeder {
  height: 70px !important;
  box-shadow: 10px 10px 5px #888888;
}

.home > .el-container {
  height: 100vh;
}
</style>