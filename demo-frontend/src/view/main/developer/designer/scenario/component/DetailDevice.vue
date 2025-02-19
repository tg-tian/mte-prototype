<template>
  <div class="scenario-subtitle" style="display: flex;justify-content: space-between">
    <div id="设备">设备</div>
    <el-button type="primary" @click="dialogVisible = true">添加设备</el-button>
  </div>
  <div class="scenario-content">
    TODO：点击编辑按钮跳转到设备的详情页，详情页展示设备基本信息、属性数据、命令列表、事件列表
    <Table :header="header" :data="data" />
  </div>
  <el-dialog v-model="dialogVisible" title="添加设备" width="50%">
<!--  选择设备类型  -->
    <div v-if="selectedDevice === ''">
      <div style="display: flex;justify-content: space-between;font-size: 16px;margin-bottom: 20px;margin-top: 10px">
        <div>领域设备类型</div>
      </div>
      <div style="display: flex;flex-wrap: wrap;gap: 20px;">
        <Card v-for="device in domainDevice" :key="device.code" :cardItem="device" @itemClick="handleDomainDeviceClick(device)" />
      </div>
    </div>
<!--  添加具体设备  -->
    <div v-else>
      <div style="padding: 20px">
        TODO:根据选择的领域设备类型到对应iot平台获取对应的设备列表并选择
        <div>
          <el-button type="primary" @click="submit">
            确认
          </el-button>
          <el-button @click="handleReturnDomainDevice">返回</el-button>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
import Table from "@/view/main/common/Table.vue";
import Card from "@/view/main/common/Card.vue";
import {
  loadScenarioBindingData,
  loadScenarioData
} from "@/api/scenarioApi";
import {getAssetsFile, getDeviceImage} from '@/utils/pub-use'

const props = defineProps({
  scenarioId: String,
  scenarioName: String
});

interface State{
  header: any[];
  data: any[];
  domainDevice: any[];
  dialogVisible: boolean;
  selectedDevice: any;
  searchInput: String
}
const state = reactive<State>({
  header: [
    {
      code: "deviceCode",
      name: "设备码",
      type: "String"
    },
    {
      code: "deviceName",
      name: "设备名",
      type: "String"
    },
    {
      code: "deviceType",
      name: "设备模型名称",
      type: "String"
    },
    {
      code: "platform",
      name: "平台",
      type: "String"
    },
    {
      code: "nodeType",
      name: "节点类型",
      type: "String"
    },
    {
      code: "onlineStatus",
      name: "在线状态",
      type: "String"
    }
  ],
  data: [],
  domainDevice: [],
  dialogVisible: false,
  selectedDevice: '',
  searchInput: ''
})
const {header, data, domainDevice, dialogVisible, selectedDevice, searchInput} = toRefs(state)

onMounted(()=>{
    getScenarioDevice()


})

const getScenarioDevice = ()=>{

  loadScenarioBindingData("Device","SmartBuilding").then((res:any)=>{
    if(res.status === 200){
      domainDevice.value = res.data.map( v => {
        return {
          code: v.deviceTypeCode,
          name:v.deviceTypeName,
          imageUrl: getDeviceImage(v.imgPath),
        }
      })

      //加载设备数据
      data.value = [];
      loadScenarioData("PhysicalBuilding").then((res:any) =>{
        if (res.status === 200){
          const devices = res.data;
          devices.forEach((device: any) => {
            // 查找设备类型对应的 domainDevice 项
            const matchedDevice = domainDevice.value.find(d => d.code === device.deviceType);
            const newDevice = {
              deviceCode: device.deviceCode,
              deviceName: device.deviceName,
              deviceType:device.deviceType,
              protocol:device.protocol,
              host:device.host,
              port:device.port,
            }
            data.value.push(newDevice);
          })
        }
      })
    }
  })
}

const handleDomainDeviceClick = (device)=>{
  selectedDevice.value = device
}

const handleReturnDomainDevice = ()=>{
  selectedDevice.value = ''
}

const submit = () => {
}
</script>
