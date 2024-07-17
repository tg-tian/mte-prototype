<template>
  <div class="domain-subtitle" style="display: flex;justify-content: space-between">
    <el-button type="primary" style="margin-left: auto;">新增设备</el-button>
  </div>
  <div class = "domain-content">
    <Table :header="header" :data="data" :canChoose="true" />
  </div>
  <div class="domain-subtitle" style="display: flex;justify-content: space-between">
    <el-button type="primary" style="margin-left: auto;" plain>保存选择</el-button>
    <el-button type="primary"  style="margin-right: auto;" plain>清除选择</el-button>
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
    code: "ID",
    name: "设备ID",
    type: "String"
  },{
    code: "name",
    name: "设备名称",
    type: "String"
  }]
  data.value = [{
    number: "001",
    ID: "AbCdeF",
    name: "咖啡机"
  },{
    number: "002",
    ID: "AdCeeF",
    name: "空调"
  }]
})
</script>

<style scoped>

</style>