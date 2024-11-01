<template>
  <div class="component-container">
    <div class="component-subtitle" style="display: flex;justify-content: space-between">
      <div @click="deviceVisible = !deviceVisible" style="cursor: pointer">
        设备模板({{deviceData.length}})
        <el-icon>
          <CaretBottom v-if="deviceVisible"/>
          <CaretTop v-if="!deviceVisible"/>
        </el-icon>
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
        <el-icon>
          <CaretBottom v-if="businessVisible"/>
          <CaretTop v-if="!businessVisible"/>
        </el-icon>
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
import { ref } from 'vue';
import Card from "@/view/main/common/Card.vue";
import {CaretBottom, CaretTop} from "@element-plus/icons-vue";
import {loadDoaminComponentData} from "@/api/DomainApi";
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
        imageUrl: new URL('@/assets/device/CoffeeMaker.png', import.meta.url).href,
        isSelected: false
      },
      {
        code: "AirConditioner",
        name: "空调",
        imageUrl: new URL('@/assets/device/AirConditioner.png', import.meta.url).href,
        isSelected: false
      },
      {
        code: "SmokeDetector",
        name: "烟感器",
        imageUrl: new URL('@/assets/device/SmokeDetector.png', import.meta.url).href,
        isSelected: false
      }
    ]
  }else {
    businessData.value = [
      {
        code: "ConferenceService",
        name: "会议接待流程图",
        imageUrl: new URL('@/assets/icon/process-template.png', import.meta.url).href,
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

//import.meta.glob 使用时，路径并不支持 @ 这种别名。import.meta.glob 只能处理相对于项目根目录的相对路径
const images = import.meta.glob('/src/assets/icon/*.png');
const getImage = async (deviceTypeCode) => {
  const imagePath = `/src/assets/icon/${deviceTypeCode}.png`; // 构建路径

  try {
    if (images[imagePath]) {
      const imageModule = await images[imagePath]();  // 调用懒加载函数加载模块
      return imageModule.default; // 返回图片的默认导出（图片路径）
    } else {
      console.error(`Image not found for device code: ${deviceTypeCode}`);
      return '/logo.png'; // 图片未找到时返回空字符串或默认图片
    }
  } catch (e) {
    console.error('Error loading image:', e);
    return '/logo.png'; // 如果出错，返回默认图片路径或空
  }
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
            imageUrl: await getImage(device.deviceTypeCode)
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
