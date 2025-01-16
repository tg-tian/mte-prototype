<template>
  <div class="component-container">
    <div class="component-subtitle" style="display: flex;justify-content: space-between">
      <div @click="deviceVisible = !deviceVisible" style="cursor: pointer">
        设备组件({{deviceData.length}})
        <el-icon>
          <CaretBottom v-if="deviceVisible"/>
          <CaretTop v-if="!deviceVisible"/>
        </el-icon>
      </div>
      <el-button type="primary" @click="router.push({path: '/developer/device-component/add'})">新增设备组件</el-button>
    </div>
    <div class="component-content" v-if="deviceVisible">
      <div style="display: flex;flex-wrap: wrap;gap: 20px">
        <div v-for="(device, index) in deviceData" :key="index">
          <Card
              :cardItem="device"
              canSelect
              needPublish
              @itemClick="handleDeviceClick(device)"
              @update:isSelected="updateDeviceIsSelected(index, $event)"/>
        </div>
      </div>
    </div>
  </div>
  <div style="margin-top: 30px" class="component-container">
    <div class="component-subtitle" style="display: flex;justify-content: space-between">
      <div @click="businessVisible = !businessVisible" style="cursor: pointer">
        业务模板({{businessData.length}})
        <el-icon>
          <CaretBottom v-if="businessVisible"/>
          <CaretTop v-if="!businessVisible"/>
        </el-icon>
      </div>
      <el-button type="primary">新增业务模板</el-button>
    </div>
    <div class="component-content" v-if="businessVisible">
      <div style="display: flex;flex-wrap: wrap;gap: 20px">
        <div v-for="(business, index) in businessData" :key="index">
          <Card
              :cardItem="business"
              canSelect
              needPublish
              @itemClick="handleBusinessClick(business)"
              @update:isSelected="updateBusinessIsSelected(index, $event)"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import Card from "@/view/main/common/Card.vue";
import {CaretBottom, CaretTop} from "@element-plus/icons-vue";
import {loadDoaminComponentData} from "@/api/DomainApi";
import { getDeviceImage, getAssetsFile } from '@/utils/pub-use'
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

onMounted(async ()=>{
  if(import.meta.env.VITE_MODE === "mock"){
    deviceData.value = [
      {
        code: "CoffeeMaker",
        name: "咖啡机器人",
        imageUrl: getAssetsFile('device/CoffeeMaker.png'),
        isSelected: false
      },
      {
        code: "AirConditioner",
        name: "空调",
        imageUrl: getAssetsFile('device/AirConditioner.png'),
        isSelected: false
      },
      {
        code: "SmokeDetector",
        name: "烟感器",
        imageUrl: getAssetsFile('device/SmokeDetector.png'),
        isSelected: false
      }
    ]
  }else {
    businessData.value = [
      {
        code: "ConferenceService",
        name: "会议接待流程图",
        imageUrl: getAssetsFile('icon/process-template.png'),
        isSelected: false
      }
    ]
  }
})

const router = useRouter()

const updateDeviceIsSelected = (index, value) => {
  deviceData.value[index].isSelected = value;
};
const handleDeviceClick = (device)=>{
  router.push({path: "/developer/device-component/detail", query: {deviceCode: device.code, deviceName: device.name}})
}

const updateBusinessIsSelected = (index, value) => {
  businessData.value[index].isSelected = value;
};
const handleBusinessClick = (device)=>{
  console.log(device)
}

const getComponentData = async ()=>{
  await loadDoaminComponentData("Device").then( (res:any) =>{
    deviceData.value =[]
    if(res.status === 200){
      const dataArray = res.data;
      dataArray.forEach(async (device : any) => {
          const newDevice = {
            code: device.deviceTypeCode,
            name: device.deviceTypeName,
            isSelected: false,
            isPublish: device.publish,
            imageUrl: getDeviceImage(device.imgPath)
          }
          deviceData.value.push(newDevice);  // 将对象加入到 domainDevice 中
      })
    }
  })
}

// 暴露方法getComponentData
defineExpose({
  getComponentData,
});

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
