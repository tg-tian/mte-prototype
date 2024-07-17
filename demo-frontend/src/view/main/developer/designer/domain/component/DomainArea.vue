<template>
  <div class="domain-subtitle" style="display: flex;justify-content: space-between">
    <div id="字段列表">字段列表</div>
    <el-button type="primary">新增字段</el-button>
  </div>
  <div class="domain-content">
    <Table :header="header" :data="data" />
  </div>
</template>

<script setup lang="ts">

import Table from "@/view/main/common/Table.vue";

//暂时没用
const props = defineProps({
  domainId: String,
  domainName: String
});

interface State{
  header: any[];
  data: any[];
}

const state = reactive<State>({
  header: [],
  data:[],
})

const  {header ,data} = toRefs(state)

onMounted(()=>{
  header.value = [{
    code: "number",
    name: "序号",
    type: "String"
  },{
    code: "name",
    name: "字段名称",
    type: "String"
  },{
    code: "type",
    name: "数据类型",
    type: "String"
  }]
  data.value = [{
    number: "001",
    name: "楼层",
    type: "字符串"
  },{
    number: "002",
    name: "描述",
    type: "字符串"
  },{
    number: "003",
    name: "平面图",
    type: "图像"
  }]
})
</script>
