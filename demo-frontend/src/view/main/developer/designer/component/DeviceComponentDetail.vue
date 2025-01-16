<template>
  <div style="display: flex;justify-content: space-between">
    <div style="font-size: 18px;margin-bottom: 30px">
      <el-button link @click="router.go(-1)" style="margin-right: 10px"><el-icon><ArrowLeft /></el-icon></el-button>
      设备类型详情-{{deviceName}}
    </div>
    <el-button type="primary" @click="dialogVisible = true;startPublish()">发布</el-button>
  </div>
  <el-dialog v-model="dialogVisible" title="打包发布" width="500">
    <el-image :src="progressImage" style="width: 90%;margin-left: 5%"></el-image>
    <div style="font-size: 16px;margin-bottom: 15px">{{progressMessage}}</div>
    <el-progress :percentage="progress" :color="customColorMethod"></el-progress>
  </el-dialog>
  <div style="margin: 20px">
      <el-descriptions
          title="基本信息"
          :column="2"
          border
      >
        <template #extra>
          <el-button type="primary" plain @click="goToCodeServer()">编辑</el-button>
        </template>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              设备类型代码
            </div>
          </template>
          {{deviceData.code}}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              设备类型名
            </div>
          </template>
          {{ deviceData.name }}
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
    <div style="font-weight: bold; margin-bottom: 20px">功能服务</div>
    <el-tabs type="card">
      <el-tab-pane label="操作">
        <div style="margin-bottom: 10px;display: flex;justify-content: end">
          <el-button type="primary" plain>新增</el-button>
        </div>
        <Table :header="commandHeader" :data="commandData"/>
      </el-tab-pane>
      <el-tab-pane label="事件">
        <div style="margin-bottom: 10px;display: flex;justify-content: end">
          <el-button type="primary" plain>新增</el-button>
        </div>
        <Table :header="eventHeader" :data="eventData"/>
      </el-tab-pane>
      <el-tab-pane label="品牌">
        <div style="margin-bottom: 10px;display: flex;justify-content: end">
          <el-button type="primary" plain>新增</el-button>
        </div>
        <Table :header="serviceHeader" :data="serviceData" @handleLinkClick="editServiceFile($event)"/>
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
  serviceData: any[];
  dialogVisible: boolean;
  progress: number;
  progressMessage: string;
  progressImage: any
}
const state = reactive<State>({
  deviceCode: '',
  deviceName: '',
  deviceData: {},
  commandData: [],
  eventData: [],
  serviceData: [],
  dialogVisible: false,
  progress: 0,
  progressMessage: "开始打包发布",
  progressImage: ''
})
const {deviceCode, deviceName, deviceData, commandData, eventData, serviceData, dialogVisible, progress, progressMessage, progressImage} = toRefs(state)

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
  if(import.meta.env.VITE_MODE === "mock"){
    deviceData.value={
      code: "CoffeeMaker",
      name: "咖啡机器人",
      imageUrl: new URL('@/assets/device/coffeeMaker.png', import.meta.url).href
    }
    commandData.value = [
      {
        commandCode: "MakeCoffee",
        commandName: "做咖啡",
        eventNum: 3,
        serviceNum: 2
      }
    ]
    eventData.value = [
      {
        name: "onMakeCoffeeStart",
        description:"准备咖啡",
        commandId: "MakeCoffee",
        type: "onStart"
      },
      {
        name: "onMakeCoffeeComplete",
        description:"咖啡完成提醒",
        commandId: "MakeCoffee",
        type: "onComplete"
      },
      {
        name: "onMakeCoffeeError",
        description:"错误警告",
        commandId: "MakeCoffee",
        type: "onError"
      },
    ]
    serviceData.value = [
      {
        code: "AService",
        name: "A品牌",
        description: "A公司咖啡机可以做（摩卡，美式）咖啡，支持（加糖，加奶）操作。",
        filename: "AService.json"
      }
    ]
  }else {
    loadDeviceData()
  }
})

/**
 *确认你是在使用 相同组件 且路由确实在变化。例如，如果你点击的按钮只是改变了 URL 的查询参数（而不是路径），并且没有改变整个路由的路径，可能不会触发路由更新。因此需要确保确实触发了 beforeRouteUpdate。
 * 如果你只是更新了查询参数（query）而没有更新路径（path），可以使用 watch 来监控 query 的变化。
 * */
watch(route, (newRoute, oldRoute) => {
  loadDeviceData();
});

const goToCodeServer = ()=>{
  window.open(`http://139.196.147.52:5200/?folder=/home/coder/project/workplace/deviceType/${deviceCode.value}/definitions/`, '_blank');
}

const loadDeviceData = () => {
  commandData.value = []
  serviceData.value = []
  eventData.value = []
  getDeviceData()
  getCommandData()
  getEventData()
  getServiceData()
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
    code: "eventNum",
    name: "已绑定事件",
    type: "String"
  },
  {
    code: "serviceNum",
    name: "支持品牌",
    type: "Number"
  }
]
const eventHeader = [
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
    code: "commandId",
    name: "绑定操作",
    type: "String"
  },
  {
    code: "type",
    name: "事件类型",
    type: "String"
  }
]
const serviceHeader = [
  {
    code: "name",
    name: "厂商名称",
    type: "String"
  },
  {
    code: "description",
    name: "厂商描述",
    type: "String"
  },
  {
    code: "filename",
    name: "厂商定义文件",
    type: "Link"
  }
]
// const images = import.meta.glob('/src/assets/icon/*.png');
// const getImage = async (deviceTypeCode) => {
//   const imagePath = `/src/assets/icon/${deviceTypeCode}.png`; // 构建路径
//
//   try {
//     if (images[imagePath]) {
//       const imageModule: any = await images[imagePath]();  // 调用懒加载函数加载模块
//       return imageModule.default; // 返回图片的默认导出（图片路径）
//     } else {
//       console.error(`Image not found for device code: ${deviceTypeCode}`);
//       return '/logo.png'; // 图片未找到时返回空字符串或默认图片
//     }
//   } catch (e) {
//     console.error('Error loading image:', e);
//     return '/logo.png'; // 如果出错，返回默认图片路径或空
//   }
// }

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

const getServiceData = ()=>{
  getService(deviceCode.value,"AService").then((res:any) =>{
    if(res.status === 200){
      serviceData.value = res.data
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

const startPublish = ()=>{
  progress.value = 0;
  progressImage.value = getAssetsFile('progress/generate.png')
  // EventSource 是 HTML5 提供的一个用于服务器发送事件（Server-Sent Events, SSE）的接口。
  // 它允许网页与服务器之间建立持久地连接，服务器可以通过该连接持续向客户端推送消息，而客户端可以实时接收这些消息。
  // 与 WebSocket 不同的是，EventSource 是单向的，只能由服务器向客户端发送数据，客户端不能向服务器发送消息。
  const eventSource = new EventSource(import.meta.env.VITE_BASE_PATH+`/device/publish?deviceType=${deviceData.value.code}`)
  eventSource.onmessage = (event) => {
    const progressData = JSON.parse(event.data);
    console.log(progressData);
    progressMessage.value = progressData.message
    progress.value = progressData.progress
    if (progress.value < 50) {
      progressImage.value = getAssetsFile('progress/generate.png')
    }else if (progress.value < 75) {
      progressImage.value = getAssetsFile('progress/package.png')
    }else {
      progressImage.value = getAssetsFile('progress/copy.png')
    }

    if (progress.value === 100){
      updateDevicePublish(deviceData.value.code).then((res:any)=>{
        if(res.status === 200) {
          ElMessage.success('设备发布成功')
        }
      })
    }
  };

  eventSource.onerror = () => {
    eventSource.close();
  };
}

const customColorMethod = (percentage: number) => {
  if (percentage < 30) {
    return '#909399'
  }
  if (percentage < 70) {
    return '#e6a23c'
  }
  return '#67c23a'
}

/**
 * 在 Vue 中，$event 是一个特殊的占位符，用于在事件处理程序中传递事件对象或自定义参数。当你在模板中使用 $event 时，它通常代表事件处理函数的默认参数，即触发事件时生成的事件对象或在自定义事件中传递的参数
 * */
const editServiceFile = (fileName)=>{
  console.log(fileName)
}
</script>
