<template>
  <div style="position: relative">
    <el-tabs v-model="activeName" class="workspace-tabs" @tab-click="handleClick">
<!--系统管理员（管理系统组件）-->
      <el-tab-pane label="我的控件" name="component" class="tab-pane" v-if="roles.includes('admin')">
        <MyComponent ref="myComponent"/>
      </el-tab-pane>
      <!-- 领域场景配置员（设置领域——增删改,设定领域组件/模板——UI/流程/设备） （设置场景，设定场景资源——设备/外部应用）  -->
      <el-tab-pane label="我的领域" name="domain" class="tab-pane" v-if="roles.includes('developer') || roles.includes('admin')">
        <MyDomain />
      </el-tab-pane>
      <el-tab-pane label="我的场景" name="scenario" class="tab-pane" v-if="roles.includes('developer') || roles.includes('admin')">
        <MyScenario />
      </el-tab-pane>
<!--   应用开发者（选定场景开发应用，应用增删改）   -->
      <el-tab-pane label="我的应用" name="application" class="tab-pane" v-if="roles.includes('business')">
        <MyApplication />
      </el-tab-pane>
    </el-tabs>
    <div class="add-button" v-if="activeName !== 'component'">
      <el-dropdown>
        <el-button type="primary">
          <el-icon size="18" color="white"><Plus /></el-icon>
          &nbsp;添加</el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>新增</el-dropdown-item>
            <el-dropdown-item>从模板库导入</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue';
import type { TabsPaneContext } from 'element-plus'
import {Plus} from "@element-plus/icons-vue";
import MyDomain from "./component/MyDomain.vue";
import MyComponent from "./component/MyComponent.vue";
import MyScenario from "./component/MyScenario.vue";
import MyApplication from "./component/MyApplication.vue";
import {useUserStore} from "../../../../store/modules/userStore";
import {storeToRefs} from "pinia";

const userStore = useUserStore()
const { roles } = storeToRefs(userStore)
const activeName = ref('component')
const router = useRouter()
const myComponent = ref<InstanceType<typeof MyComponent> | null>(null); // 引用 MyComponent

watchEffect(() => {
  if (typeof router.currentRoute.value.query.active === 'string') {
    activeName.value = router.currentRoute.value.query.active || 'component'
  }
  if (activeName.value === 'component') {
    console.log(myComponent)
    if (myComponent.value) {
      myComponent.value.getComponentData(); // 调用 MyComponent 的方法
    }
  }
})

const handleClick = (tab: TabsPaneContext, event: Event) => {
  router.push({ query: { ...router.currentRoute.value.query, active: tab.props.name } })
}
</script>

<style scoped>
.add-button {
  position: absolute;
  right: 40px;
  top: 4px;
}
</style>

<style>
.el-tabs__item {
  font-size: 16px !important;
}
.el-tabs__nav-wrap:after {
  height: 0 !important;
}
</style>
