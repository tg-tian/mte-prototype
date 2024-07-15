<template>
  <div class="scenario-subtitle" style="display: flex;justify-content: space-between">
    <div id="区域列表">区域列表</div>
    <el-button type="primary">新增区域</el-button>
  </div>
  <div class="scenario-content">
    <Table :header="header" :data="data" />
  </div>
</template>

<script setup lang="ts">
import Table from "@/view/main/common/Table.vue";

const props = defineProps({
  scenarioId: String,
  scenarioName: String
});

interface State{
  header: any[];
  data: any[]
}
const state = reactive<State>({
  header: [],
  data: []
})
const {header, data} = toRefs(state)

onMounted(()=>{
  header.value = [{
    code: "floor",
    name: "楼层",
    type: "String"
  },{
    code: "description",
    name: "描述",
    type: "String"
  },{
    code: "imgUrl",
    name: "平面图",
    type: "Image"
  }]
  data.value = [{
    floor: "二层",
    description: "交叉二号楼二楼",
    imgUrl: new URL('@/assets/images/floor.jpeg', import.meta.url).href
  }]
})
</script>
