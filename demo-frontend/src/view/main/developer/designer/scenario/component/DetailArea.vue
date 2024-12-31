<template>
  <div>
    <div class="scenario-subtitle" style="display: flex;justify-content: space-between">
      <div id="场景地图">场景地图</div>
      <el-button type="primary">导入地图</el-button></div>
    <div class="scenario-content">
      <img :src="getAssetsFile('images/'+mapPath)" alt="场景地图" style="width: 80%;margin-left: 10%" />
    </div>
  </div>
  <div>
    <div class="scenario-subtitle" style="display: flex;justify-content: space-between">
      <div id="区域列表">区域列表</div>
      <el-button type="primary">新增区域</el-button>
    </div>
    <div class="scenario-content">
      <Table :header="header" :data="data" />
    </div>
  </div>
</template>

<script setup lang="ts">
import Table from "@/view/main/common/Table.vue";
import {getDomainJson} from "@/api/DomainApi";
import {getScenarioJson} from "@/api/scenarioApi";
import getAssetsFile from '@/utils/pub-use'

const props = defineProps({
  scenarioId: String,
  scenarioName: String
});

interface State{
  header: any[];
  data: any[];
  mapPath: String;
}
const state = reactive<State>({
  header: [],
  data: [],
  mapPath: 'map.png'
})
const {header, data, mapPath} = toRefs(state)

onMounted(()=>{
  if (import.meta.env.VITE_MODE === "mock"){
    header.value = [{
      code: "floor",
      name: "楼层",
      type: "String"
    },{
      code: "description",
      name: "描述",
      type: "String"
    },{
      code: "planPath",
      name: "平面图",
      type: "Image"
    }]

    data.value = [{
      floor: "二层",
      description: "交叉二号楼二楼",
      planPath: getAssetsFile('images/floor.jpeg')
    }]
  } else {
    getDomainField()
  }
})

const getScenarioData = () =>{
  getScenarioJson().then((res:any) =>{
    if (res.status === 200){
      mapPath.value = res.data.mapPath
      data.value = res.data.maplist.map((v)=>{
        header.value.forEach(item => {
          if (item.type === "Image" && v[item.code]) {
            const imgPath = "images/"+v[item.code]
            v[item.code] = getAssetsFile(imgPath)
          }
        });
        return v
      })
    }
  })
}

const getDomainField = ()=>{
  getDomainJson().then((res:any) =>{
    if (res.status === 200){
      header.value = res.data.domainField

      getScenarioData()
    }
  })
}
</script>
