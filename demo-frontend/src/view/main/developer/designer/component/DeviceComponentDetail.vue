<template>
  <div style="display: flex;justify-content: space-between">
    <div style="font-size: 18px;margin-bottom: 30px">
      <el-button link @click="router.go(-1)" style="margin-right: 10px"><el-icon><ArrowLeft /></el-icon></el-button>
      设备模型详情-{{deviceName}}
    </div>
    <el-button type="primary" @click="">同步</el-button>
  </div>
  <div style="margin: 20px">
      <el-descriptions
          title="基本信息"
          :column="2"
          border
      >
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              设备模型编码
            </div>
          </template>
          {{deviceData.code}}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              设备模型名称
            </div>
          </template>
          {{ deviceData.name }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              设备类型
            </div>
          </template>
          {{ deviceData.type }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              设备模型描述
            </div>
          </template>
          {{ deviceData.description }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              图标
            </div>
          </template>
            <el-image :src="deviceData.imageUrl" style="width: 10%"/>
        </el-descriptions-item>
      </el-descriptions>
  </div>
  <div style="margin: 50px 20px 20px;">
    <div style="font-weight: bold; margin-bottom: 20px">设备属性</div>
    <Table :header="propertyHeader" :data="propertyData" :can-operate="false"/>
  </div>
  <div style="margin: 50px 20px 20px;">
    <div style="font-weight: bold; margin-bottom: 20px">功能服务</div>
    <el-tabs type="card">
      <el-tab-pane label="操作">
        <Table :header="commandHeader" :data="commandData" :can-operate="false"/>
      </el-tab-pane>
      <el-tab-pane label="事件">
        <Table :header="eventHeader" :data="eventData" :can-operate="false"/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import {ArrowLeft} from "@element-plus/icons-vue";
import Table from "@/view/main/common/Table.vue";
import {getOperationCommand, getOperationEvent, getService, loadDeviceInfo,updateDevicePublish} from "@/api/DeviceExpand";
import { watch } from 'vue';
import {ElMessage} from "element-plus";
import {getAssetsFile, getDeviceImage} from '@/utils/pub-use'
interface State {
  deviceCode: String;
  deviceName: String;
  deviceData: any;
  commandData: any[];
  eventData: any[];
  propertyData: any[]
}
const state = reactive<State>({
  deviceCode: '',
  deviceName: '',
  deviceData: {},
  commandData: [],
  eventData: [],
  propertyData: []
})
const {deviceCode, deviceName, deviceData, commandData, eventData, propertyData} = toRefs(state)

const router = useRouter()
const route = useRoute();
watchEffect(() => {
  if (typeof router.currentRoute.value.query.deviceCode === 'string') {
    deviceCode.value = router.currentRoute.value.query.deviceCode || ''
  }
  if (typeof router.currentRoute.value.query.deviceName === 'string') {
    deviceName.value = router.currentRoute.value.query.deviceName || ''
  }
})

onMounted(()=>{
  loadDeviceData()
})

/**
 *确认你是在使用 相同组件 且路由确实在变化。例如，如果你点击的按钮只是改变了 URL 的查询参数（而不是路径），并且没有改变整个路由的路径，可能不会触发路由更新。因此需要确保确实触发了 beforeRouteUpdate。
 * 如果你只是更新了查询参数（query）而没有更新路径（path），可以使用 watch 来监控 query 的变化。
 * */
watch(route, (newRoute, oldRoute) => {
  loadDeviceData();
});

const loadDeviceData = () => {
  commandData.value = []
  eventData.value = []
  getDeviceData()
  getCommandData()
  getEventData()
}
const commandHeader = [
  {
    code: "commandCode",
    name: "操作码",
    type: "String"
  },
  {
    code: "commandName",
    name: "操作名称",
    type: "String"
  },
  {
    code: "description",
    name: "描述",
    type: "String"
  }
]
const eventHeader = [
  {
    code: "code",
    name: "事件编码",
    type: "String"
  },
  {
    code: "name",
    name: "事件名称",
    type: "String"
  },
  {
    code: "description",
    name: "事件描述",
    type: "String"
  },
  {
    code: "type",
    name: "事件类型",
    type: "String"
  },
  {
    code: "source",
    name: "来源",
    type: "String"
  },
]
const propertyHeader = [
  {
    code:"name",
    name:"属性名称",
    type:"String",
  },{
    code:"identifier",
    name:"属性编码",
    type:"String",
  },{
    code:"type",
    name:"数据类型",
    type:"String",
  },{
    code:"unit",
    name:"单位",
    type:"String",
  },
  {
    code:"accessMode",
    name:"读写类型",
    type:"String",
  }
]

const getDeviceData = () =>{
  loadDeviceInfo(deviceCode.value).then(async (res:any)=>{
    if(res.status === 200){
      deviceData.value = {
        code: res.data.deviceTypeCode,
        name: res.data.deviceTypeName,
        imageUrl:getDeviceImage(res.data.imgPath)
      }
    }
  })
}

const getCommandData = ()=>{
  getOperationCommand(deviceCode.value).then((res: any)=>{
    if(res.status === 200){
      commandData.value = res.data.map((v)=>{
        return {
          commandCode: v.commandCode,
          commandName: v.commandName,
          eventNum: (v.events?.length ?? 0) + "/3",
          serviceNum: v.services?.length ?? 0
        }
      })
    }
  })
}

const getEventData = ()=>{
  getOperationEvent(deviceCode.value, "").then((res:any)=>{
    if(res.status === 200){
      eventData.value = res.data
    }
  })
}
</script>
