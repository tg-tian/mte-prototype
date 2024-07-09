<template>
  <el-menu :default-active="selectedItem" class="nav-menu" @select="handleMenuSelect">
    <div style="margin-top: 10px">
      <div class="sub-title">系统业务</div>
      <el-sub-menu index="0" class="nav-item">
        <template #title>
          <el-icon><Monitor /></el-icon>
          <div class="menu-item">应用管理</div>
        </template>
        <el-menu-item index="0-1" class="sub-menu-item">权限配置</el-menu-item>
        <el-menu-item index="0-2" class="sub-menu-item">应用发布</el-menu-item>
      </el-sub-menu>
      <el-menu-item index="1" class="nav-item">
        <el-icon><User /></el-icon>
        <div class="menu-item">用户管理</div>
      </el-menu-item>
    </div>
    <div style="margin-top: 10px">
      <div class="sub-title">工作台</div>
      <el-menu-item index="2" class="nav-item">
        <el-icon><Compass /></el-icon>
        <div class="menu-item">模板库</div>
      </el-menu-item>
      <el-menu-item index="3" class="nav-item">
        <el-icon><DataBoard /></el-icon>
        <div class="menu-item">我的空间</div>
      </el-menu-item>
    </div>
    <div style="margin-top: 10px">
      <div class="sub-title">我的应用</div>
      <el-menu-item
          v-for="item in domainItems"
          :key="item.index"
          :index="item.index"
          class="nav-item"
      >
        <el-icon><Folder /></el-icon>
        <div class="menu-item">{{ item.name }}</div>
      </el-menu-item>
    </div>
  </el-menu>
</template>

<script setup lang="ts">
import { House, Monitor, User, Compass, DataBoard, Folder } from '@element-plus/icons-vue'
import router from '../../../../router/index.ts'

const fetchMenuItems = async () => {
  return [
    { index: '4', name: '智慧楼宇', route: '/demo' },
    { index: '5', name: '智慧矿山', route: '/demo2' }
  ];
};

interface State {
  selectedItem: string
  domainItems: any;
  items: any;
}

const state = reactive<State>({
  selectedItem: '0',
  domainItems: [],
  items: [
    {
      index: '0-1',
      name: '权限配置',
      route: '/auth'
    },
    {
      index: '0-2',
      name: '应用发布',
      route: '/publish-setting'
    },
    {
      index: '1',
      name: '用户管理',
      route: '/user'
    },
    {
      index: '2',
      name: '模板库',
      route: '/template'
    },
    {
      index: '3',
      name: '我的空间',
      route: '/my-workspace'
    }
  ]
})
const { selectedItem, domainItems, items } = toRefs(state)

onMounted(async ()=>{
  domainItems.value = await fetchMenuItems();
  domainItems.value.forEach((item) => {
    items.value.push(item)
  })
})

watchEffect(() => {
  const path = router.currentRoute.value.path
  const matchingItemData = items.value.find(item => item.route === path);
  if (matchingItemData) {
    selectedItem.value = matchingItemData.index;
  } else {
    selectedItem.value = '';
  }
})

const handleMenuSelect = (key: string, _keyPath: string[]) => {
  selectedItem.value = key
  const selectedItemData = items.value.find(item => item.index === key);
  if (selectedItemData) {
    router.push(selectedItemData.route)
  }
}
</script>
<style scoped>
.nav-menu {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.nav-item {
  width: 100%;
}

.sub-title {
  color: grey;
  margin-left: 10px;
  font-size: 14px;
  margin-bottom: 5px;
}

.menu-item {
  width: 100px;
  text-align: center;
  font-size: 16px;
}

.sub-menu-item {
  text-align: center;
  font-size: 16px;
}
</style>
