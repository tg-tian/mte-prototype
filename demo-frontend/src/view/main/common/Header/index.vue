<template>
  <div class="header">
<!--    <div class="header-title" style="cursor: pointer" @click="router.push({path:'/'})">-->
<!--      <img width="30" height="30" title="低代码" :src="logo" />-->
<!--      <div style="line-height: 32px; padding-left: 10px">面向场景计算的低代码平台</div>-->
<!--    </div>-->
    <div class="header-right">
<!--      <el-button link style="margin-right: 20px;display: flex;color: white">-->
<!--        <el-icon style="margin-right: 2px; margin-top: 1px;" size="20" color="white"><DataBoard /></el-icon>-->
<!--      </el-button>-->
      <div style="margin-right: 20px;margin-left: 30px; ">
        <el-dropdown @command="handleDropdownItemClick">
          <el-button link style="display: flex;color: white">
            <el-icon style="margin-right: 2px; margin-top: 1px;" size="20" color=#50a5fb><Setting /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item key="systemConfig" command="system">系统设置</el-dropdown-item>
              <el-dropdown-item key="developerConfig" command="developer" v-if="roles.includes('developer') || roles.includes('admin')">开发者设置</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>

      <el-dropdown @command="handleCommand">
        <el-button link style="display: flex;color: #50a5fb;font-size: 16px">
          <div>
            {{ isAuthenticated ? username : '登录/注册' }}
          </div>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="admin">系统管理员</el-dropdown-item>
            <el-dropdown-item command="developer">场景配置员</el-dropdown-item>
            <el-dropdown-item command="business">业务人员</el-dropdown-item>
            <el-dropdown-item command="user">普通用户</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import logo from '../../../../assets/logo.png'
import {Setting, DataBoard} from "@element-plus/icons-vue";
import {useUserStore} from "@/store/modules/userStore";
import {storeToRefs} from "pinia";
const router = useRouter()

const userStore = useUserStore()
const { username, isAuthenticated, roles } = storeToRefs(userStore)

const handleCommand = (command) => {
  userStore.login({
    username: '用户',
    roles: [command]
  })
  router.push({path: '/'})
}

// 监听 store 状态变化
watch([isAuthenticated, username, roles], () => {
  console.log('isAuthenticated:', isAuthenticated.value);
  console.log('username:', username.value);
  console.log('roles', roles.value)
});

const handleDropdownItemClick = (item) => {
  // 处理设置项点击事件逻辑
  if (item === 'developer') {
    router.push({path: '/developer/setting'})
  }
};
</script>

<style>
.header {
  width: 10%;
  height: 100%;
  background-color: #ffffff;
  color: #50a5fb;
  display: flex;
  justify-content: space-between;
  align-items: center;
  float: right;
}

.header-title {
  text-align: center;
   /*color: #337ecc; */
  font-size: 18px;
  font-weight: 600;
  font-family: 'Courier New', Courier, monospace;
  display: flex;
  justify-content: space-evenly;
  margin-left: 20px;
}

.header-right {
  display: flex;
}

.el-dropdown-link {
  cursor: pointer;
  font-size: 1rem;
  color: white;
  align-items: center;
  display: flex;
}
</style>
