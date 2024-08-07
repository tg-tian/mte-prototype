<template>
  <div class="component-container">
    <div class="component-subtitle" style="display: flex;justify-content: space-between">
      <div @click="deviceVisible = !deviceVisible" style="cursor: pointer">
        设备模板({{deviceData.length}})
        <el-icon><CaretBottom/></el-icon>
      </div>
      <el-button type="primary" @click="router.push({path: '/developer/device-component/add'})">新增设备模板</el-button>
    </div>
    <div class="component-content" v-if="deviceVisible">
      <div style="display: flex;flex-wrap: wrap;gap: 20px">
        <div v-for="(device, index) in deviceData" :key="index">
          <Card
              :cardItem="device"
              canSelect
              @itemClick="handleDeviceClick(device)"
              @update:isSelected="updateDeviceIsSelected(index, $event)"/>
        </div>
      </div>
    </div>
  </div>
  <div style="margin-top: 30px" class="component-container">
    <div class="component-subtitle" style="display: flex;justify-content: space-between">
      <div @click="businessVisible = !businessVisible" style="cursor: pointer">
        业务组件模板({{businessData.length}})
        <el-icon><CaretBottom/></el-icon>
      </div>
      <el-button type="primary">新增业务组件模板</el-button>
    </div>
    <div class="component-content" v-if="businessVisible">
      <div style="display: flex;flex-wrap: wrap;gap: 20px">
        <div v-for="(business, index) in businessData" :key="index">
          <Card
              :cardItem="business"
              canSelect
              @itemClick="handleBusinessClick(business)"
              @update:isSelected="updateBusinessIsSelected(index, $event)"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Card from "@/view/main/common/Card.vue";
import {CaretBottom} from "@element-plus/icons-vue";

interface State{
  deviceData: any[];
  businessData: any[];
  deviceVisible: boolean;
  businessVisible: boolean
}

const state = reactive<State>({
  deviceData:[],
  businessData: [],
  deviceVisible: true,
  businessVisible: true
})

const  {deviceData, businessData, deviceVisible, businessVisible} = toRefs(state)

onMounted(()=>{
  deviceData.value = [
    {
      code: "CoffeeMaker",
      name: "咖啡机器人",
      imageUrl: new URL('@/assets/device/coffeeMaker.png', import.meta.url).href,
      isSelected: false
    },
    {
      code: "AirConditioner",
      name: "空调",
      imageUrl: new URL('@/assets/device/airConditioner.png', import.meta.url).href,
      isSelected: false
    },
    {
      code: "SmokeDetector",
      name: "烟感器",
      imageUrl: new URL('@/assets/device/smokeDetector.png', import.meta.url).href,
      isSelected: false
    }
  ]
  businessData.value = [
    {
      code: "ConferenceService",
      name: "会议接待流程图",
      imageUrl: new URL('@/assets/icon/process-template.png', import.meta.url).href,
      isSelected: false
    }
  ]
})

const router = useRouter()

const updateDeviceIsSelected = (index, value) => {
  deviceData.value[index].isSelected = value;
};
const handleDeviceClick = (device)=>{
  console.log(device)
}

const updateBusinessIsSelected = (index, value) => {
  businessData.value[index].isSelected = value;
};
const handleBusinessClick = (device)=>{
  console.log(device)
}
</script>
<style scoped>
.component-subtitle {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
}
.component-content {
  margin-bottom: 10px;
}
.component-container {
  border: 1px solid #d6d6d6;
  padding: 15px;
}
</style>
