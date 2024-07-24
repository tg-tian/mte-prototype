<template>
  <div class="domain-subtitle" style="display: flex;justify-content: space-between">
    <el-button type="primary"  @click="dialogVisible = true" style="margin-left: auto;">添加设备类型</el-button>
  </div>
  <div class = "domain-content">
    <Table :header="header" :data="data" :canChoose="true" />
  </div>

  <!--添加设备表单-->
  <el-dialog v-model = "dialogVisible" title="添加设备类型" width="50%" @close="clearDevice">
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
          v-for="(device, index) in domainDevice"
          :key="device.code"
          :cardItem="device"
          canSelect
          @update:isSelected="updateIsSelected(index, $event)"/>
    </div>
    <div style="margin-top: 20px;display: flex;justify-content: end">
      <el-button type="primary">确定</el-button>
      <el-button @click="clearDevice">清空</el-button>
    </div>
  </el-dialog>
</template>

<script setup lang = "ts">

import Table from "@/view/main/common/Table.vue";
import {Search} from "@element-plus/icons-vue";
import Card from '@/view/main/common/Card.vue'

const props = defineProps({
  domainId: String,
  domainName: String
})

//定义接口
interface State{
  header: any[];
  data: any[];
  dialogVisible: boolean;
  searchInput: String;
  domainDevice: any[];
  selectedDeviceList: any[]
}

//初始化
const state = reactive<State>({
  header:[
    {
      code: "ID",
      name: "类型ID",
      type: "String"
    },{
      code: "name",
      name: "设备名称",
      type: "String"
    }],
  data:[],
  dialogVisible:false,
  searchInput: '',
  domainDevice: [],
  selectedDeviceList: []
})

//获取元素结点
const {header, data, dialogVisible, searchInput, domainDevice, selectedDeviceList} = toRefs(state)


//赋值
onMounted(()=> {
  data.value = [
      {
    ID: "CoffeeMaker",
    name: "咖啡机"
  },{
    ID: "AirConditioner",
    name: "空调"
  }]
  domainDevice.value = [
    {
      code: "CoffeeMaker",
      name: "咖啡机器人",
      isSelected: false,
      imageUrl: new URL('@/assets/logo.png', import.meta.url).href
    },
    {
      code: "AirConditioner",
      name: "空调",
      isSelected: false,
      imageUrl: new URL('@/assets/logo.png', import.meta.url).href
    },
    {
      code: "SmokeDetector",
      name: "烟感器",
      isSelected: false,
      imageUrl: new URL('@/assets/logo.png', import.meta.url).href
    }
  ]
})

const updateIsSelected = (index, value) => {
  domainDevice.value[index].isSelected = value;
};

const clearDevice = ()=>{
  domainDevice.value.forEach((device)=>{
    device.isSelected=false
  })
  selectedDeviceList.value = []
}

</script>

<style scoped>

</style>
