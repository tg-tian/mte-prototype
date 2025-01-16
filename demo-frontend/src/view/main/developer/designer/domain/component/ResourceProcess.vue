<template>
  <div class="domain-subtitle" style="display: flex;justify-content: space-between">
    <el-button type="primary" @click="dialogVisible = true" style="margin-left: auto;">添加流程类型</el-button>
  </div>
  <div class = "domain-content">
    <Table :header="header" :data="data" :canChoose="true" />
  </div>
  <el-dialog v-model = "dialogVisible" title="添加流程类型" width="50%" @close="clearProcess">
    <div style="display: flex;justify-content: space-between;font-size: 16px;margin-bottom: 20px;margin-top: 10px">
      <div>模板库</div>
      <div style="color: #50a5fb">
        <el-input
            v-model="searchInput"
            style="width: 240px"
            placeholder="搜索"
            :prefix-icon="Search"
        />
      </div>
    </div>
    <div style="display: flex;flex-wrap: wrap;gap: 20px;">
      <Card
          v-for="(process, index) in domainProcess"
          :key="process.code"
          :cardItem="process"
          canSelect
          @update:isSelected="updateIsSelected(index, $event)"/>
    </div>
    <div style="margin-top: 20px;display: flex;justify-content: end">
      <el-button type="primary">确定</el-button>
      <el-button @click="clearProcess">清空</el-button>
    </div>
  </el-dialog>
</template>

<script setup lang = "ts">

import Table from "@/view/main/common/Table.vue";
import Card from '@/view/main/common/Card.vue'
import {Search} from "@element-plus/icons-vue";
import {getDomainComponent} from "@/api/DomainApi";

const props = defineProps({
  domainId: String,
  domainName: String
})

//定义状态接口
interface State{
  header: any[];
  data: any[];
  searchInput: string;
  dialogVisible: boolean;
  domainProcess: any[];
  selectedProcessList: [];
}

//初始化
const state = reactive<State>({
  header:[
      {
    code: "number",
    name: "流程号",
    type: "String"
  },{
    code: "name",
    name: "流程名称",
    type: "String"
  },{
    code: "brief",
    name: "流程简介",
    type: "String"
  }],
  data:[],
  searchInput: '',
  dialogVisible: false,
  domainProcess: [],
  selectedProcessList:[],
})

//从初始化的state中获取元素结点
const {header, data,searchInput,dialogVisible,selectedProcessList, domainProcess} = toRefs(state)

//赋值
onMounted(()=> {
  if (import.meta.env.VITE_MODE === "mock") {
    data.value = [{
      number: "001",
      name: "预约流程",
      brief: "此流程用于各类预约系统，可以实现预约时间选择、预约队列管理等"
    }]
  }else {
    getDomainData()
  }
  domainProcess.value = [{
    code: "001",
    name: "预约流程",
    isSelected: false,
    imageUrl: new URL('@/assets/logo.png', import.meta.url).href
  },
    {
      code: "002",
      name: "审批流程",
      isSelected: false,
      imageUrl: new URL('@/assets/logo.png', import.meta.url).href
    }
  ]
})

const getDomainData = () =>{
  getDomainComponent("Process", props.domainId).then((res:any) =>{
    if (res.status === 200){
      console.log(res.data)
      data.value = res.data.componentAbout.map(v=>{
        return {
          number: v.componentId,
          name: v.componentName,
          brief: v.brief
        }
      })
    }
  })
}

const updateIsSelected = (index, value) => {
  domainProcess.value[index].isSelected = value;
};

const clearProcess = ()=>{
  domainProcess.value.forEach((device)=>{
    device.isSelected=false
  })
  selectedProcessList.value = []
}
</script>

<style scoped>

</style>
