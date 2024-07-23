<template>
  <el-breadcrumb separator="/" class="scenario-breadcrumb">
    <el-breadcrumb-item :to="{ path: '/developer/workspace', query: {active: 'domain'}}" class="domain-breadcrumb-item">我的领域</el-breadcrumb-item>
    <el-breadcrumb-item class="domain-breadcrumb-item">
      {{domainName || '--领域'}}
    </el-breadcrumb-item>
  </el-breadcrumb>
  <el-row>
    <el-col :span="18">
      <div style="padding: 20px">
        <div class="domain-setting">
          <div class="domain-title" id="领域设置">领域设置</div>
          <div style="margin-top: 30px">
            <DomainArea :domainId="domainId" :domainName="domainName"/>
          </div>
        </div>
        <div class="domain-resource">
          <div class="domain-title" id="领域模板管理">模板管理</div>
          <div style="margin-top: 30px">
            <DomainResource :domainId="domainId" :domainName="domainName"/>
          </div>
        </div>
      </div>
    </el-col>
    <el-col :span="6">
      <el-anchor
        :container= "anchorRef"
        :offset = "30"
        direction="vertical"
        marker
        type="default"
        class="anchor"
      >
        <el-anchor-link href="#领域设置" title="领域设置">
        </el-anchor-link>
        <el-anchor-link href="#领域模板管理" title="领域模板管理">
          <!--
                    <template #sub-link>
            <el-anchor-link href="#设备" title="设备" />
            <el-anchor-link href="#UI组件" title="UI组件" />
            <el-anchor-link href="#流程" title="流程" />
          </template>

          -->
        </el-anchor-link>
      </el-anchor>
    </el-col>
  </el-row>


</template>

<script setup lang = "ts">
//设置语言为type script

import DomainArea from "@/view/main/developer/designer/domain/component/DomainArea.vue";
import DomainResource from "@/view/main/developer/designer/domain/component/DomainResource.vue";


/**
 *    定义接口：当数据结构复杂，或在团队协作和需要高可维护性时使用。
 *     使用 ref：处理简单类型或单一对象/数组时使用。
 *     使用 reactive：处理复杂对象或多属性状态时使用。
 * */
interface State {
  domainId: String;
  domainName: String;
}

const anchorRef = ref < HTMLElement | null>(null) //?

//接口初始化
const state = reactive<State>({
  domainId: '',
  domainName: ''
})

/**
 * ref、toRef、toRefs 都可以将某个对象中的属性变成响应式数据
 * ref的本质是拷贝，修改响应式数据，不会影响到原始数据，视图会更新
 * toRef、toRefs的本质是引用，修改响应式数据，会影响到原始数据，视图会更新
 * toRef 一次仅能设置一个数据，接收两个参数，第一个参数是哪个对象，第二个参数是对象的哪个属性
 * toRefs接收一个对象作为参数，它会遍历对象身上的所有属性，然后挨个调用toRef执行
 * */
const {domainId,domainName} = toRefs(state)

//使用路由
const router = useRouter()

//立刻运行一个函数，同时响应式地追踪其依赖，并在依赖更改时重新执行
watchEffect(()=>{
  if (typeof router.currentRoute.value.query.domainId === 'string') {
    domainId.value = router.currentRoute.value.query.domainId || ''
  }
  if (typeof router.currentRoute.value.query.domainName === 'string') {
    domainName.value = router.currentRoute.value.query.domainName || ''
  }

})
</script>



<!--#实现组件的私有化，不对全局造成样式污染，表示当前style属性只属于当前模块。-->
<style scoped>
.domain-breadcrumb {
  font-size: 18px;
  margin-top: 10px;
  margin-bottom: 20px;
}
.domain-setting{
  margin-bottom: 20px;
}

.domain-title {
  font-size: 18px;
  font-weight: bold;
  line-height: 32px;
  color: #606266;
  margin-bottom: 10px;

}
.anchor {
  border-left: 2px solid #d6d6d6;
  position: fixed;
}
</style>
<style>
.domain-subtitle {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
}
.domain-content {
  margin-bottom: 10px;
}
</style>
