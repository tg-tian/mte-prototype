<template>
  <el-menu :default-active="selectedItem" class="nav-menu" @select="handleMenuSelect">
    <div>
      <el-menu-item index="0" class="nav-item">
        <el-icon><House /></el-icon>
        <div style="width: 80%; text-align: center">菜单管理</div>
      </el-menu-item>
      <el-menu-item index="1" class="nav-item">
        <el-icon><Monitor /></el-icon>
        <div style="width: 80%; text-align: center">用户管理</div>
      </el-menu-item>
    </div>
  </el-menu>
</template>

<script setup lang="ts">
import { House, Monitor } from '@element-plus/icons-vue'
import router from '../../../../router/index.ts'

interface State {
  selectedItem: string
}

const state = reactive<State>({
  selectedItem: '0'
})
const { selectedItem } = toRefs(state)

watchEffect(() => {
  const path = router.currentRoute.value.path
  switch (path) {
    case '/':
      selectedItem.value = '1'
      break
    default:
      selectedItem.value = '0'
  }
})

const handleMenuSelect = (key: string, _keyPath: string[]) => {
  selectedItem.value = key
  if (key === '0') {
    router.push({ path: '/' })
  } else if (key === '1') {
    router.push({ path: '/' })
  }
}
</script>
<style scoped>
.nav-menu {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.nav-item {
  width: 100%;
  justify-content: space-around;
}
</style>
