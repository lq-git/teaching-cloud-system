<template>
  <el-menu default-active="0-0" class="el-menu-vertical-demo" :collapse="collapse.isCollapse" :unique-opened="true" background-color="#545c64" text-color="#fff" active-text-color="#ffd04b" :router="true">
    <div class="topbar">
      <i class="el-icon-menu"></i>
      <span v-show="!collapse.isCollapse">NWPU</span>
    </div>
    <div class="headbar" :style="{ height: collapse.headbarHeight }">
      <el-avatar :size="collapse.headbarSize">
        <img :src="'/api/' + user.icon" @error="errorHandler" />
      </el-avatar>
      <span v-show="!collapse.isCollapse" class="tip">您好，{{ user.nickName }} 欢迎登录</span>
    </div>
    <el-submenu v-for="(item, index) in menuData.data" :key="index" :index="index + ''">
      <template #title>
        <i :class="item.icon"></i>
        <span>{{ item.name }}</span>
      </template>
      <el-menu-item-group>
        <el-menu-item v-for="(child, cIndex) in item.children" :key="index + '-' + cIndex" :index="index + '-' + cIndex" :route="child.uri">{{ child.name }}</el-menu-item>
      </el-menu-item-group>
    </el-submenu>
  </el-menu>
</template>
<script lang='ts'>
import { defineComponent, onBeforeMount, reactive } from "vue";
import errorHandler from "../utils/errorHandler";
import { request, responseEntity } from "../utils/request";
export default defineComponent({
  name: "Aside",
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
    const menuData = reactive({ data: {} });
    const getMenuData = async () => {
      await request("/authority/menuData").then((response) => {
        menuData.data = (response as responseEntity).data;
      });
    };
    onBeforeMount(() => {
      getMenuData();
    });
    return { menuData, errorHandler };
  },
});
</script>
<style lang="css" scoped>
.el-menu {
  height: 100%;
}

.topbar {
  height: 70px;
  line-height: 70px;
  text-align: center;
  background: #eee;
  color: #000;
  font-size: 2rem;
}

.headbar {
  text-align: center;
  height: 120px;
  padding-top: 20px;
  border-bottom: 1px solid gray;
}

.headbar > .el-avatar {
  padding: 1px;
  border: 2px solid rgb(155, 185, 229);
}

.headbar > .tip {
  display: block;
  margin: 10px;
  color: #fff;
  font-weight: bold;
}

/* 解决element侧边栏收缩时的卡顿情况（覆盖它默认的css） */
.horizontal-collapse-transition {
  transition: 0s width ease-in-out, 0s padding-left ease-in-out,
    0s padding-right ease-in-out;
}
</style>