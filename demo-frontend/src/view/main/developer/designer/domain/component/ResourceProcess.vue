<template>
  <div class="domain-subtitle" style="display: flex;justify-content: space-between">
    <el-button type="primary" style="margin-left: auto;">新增流程</el-button>
  </div>
  <div class = "domain-content">
    <Table :header="header" :data="data" :canChoose="true" />
  </div>
  <div class="domain-subtitle" style="display: flex;justify-content: space-between">
    <el-button type="primary" style="margin-left: auto;" plain>保存选择</el-button>
    <el-button type="primary" style="margin-right: auto;" plain>清除选择</el-button>
  </div>
</template>

<script setup lang = "ts">

import Table from "@/view/main/common/Table.vue";

const props = defineProps({
  resourceId: String,
  resourceName: String
})

//定义接口
interface State{
  header: any[];
  data: any[];
}

//初始化
const state = reactive<State>({
  header:[],
  data:[],
})

const {header, data} = toRefs(state)

//赋值
onMounted(()=> {
  header.value = [{
    code: "number",
    name: "序号",
    type: "String"
  },{
    code: "name",
    name: "流程名称",
    type: "String"
  },{
    code: "brief",
    name: "流程简介",
    type: "String"
  }]
  data.value = [{
    number: "001",
    name: "预约流程",
    brief: "此流程用于各类预约系统，可以实现预约时间选择、预约队列管理等"
  }]
})
</script>

<style scoped>

</style>