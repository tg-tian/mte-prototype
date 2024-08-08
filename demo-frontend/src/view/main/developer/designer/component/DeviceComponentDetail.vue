<template>
  <div style="font-size: 18px;margin-bottom: 30px">
    <el-button link @click="router.go(-1)" style="margin-right: 10px"><el-icon><ArrowLeft /></el-icon></el-button>
    设备模板详情-{{deviceName}}
  </div>
  <div style="margin: 20px">
      <el-descriptions
          title="基本信息"
          :column="2"
          border
      >
        <template #extra>
          <el-button type="primary">编辑</el-button>
        </template>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              设备模板代码
            </div>
          </template>
          {{deviceData.code}}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              设备模板名
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
          <el-image :src="deviceData.imageUrl" />
        </el-descriptions-item>
      </el-descriptions>
  </div>

  <div style="margin: 50px 20px 20px;">
    <div style="font-weight: bold; margin-bottom: 20px">功能服务</div>
    <el-tabs type="card">
      <el-tab-pane label="操作">
        <div style="margin-bottom: 10px;display: flex;justify-content: end">
          <el-button type="primary">新增</el-button>
        </div>
        <Table :header="commandHeader" :data="commandData"/>
      </el-tab-pane>
      <el-tab-pane label="事件">
        <div>事件文件：CoffeeMakerController.java
          <el-button link style="margin-left: 10px;color: #50a5fb">编辑</el-button>
        </div>
        <div style="margin-bottom: 10px;display: flex;justify-content: end">
          <el-button type="primary">新增</el-button>
        </div>
        <Table :header="eventHeader" :data="eventData"/>
      </el-tab-pane>
      <el-tab-pane label="服务">
        <div style="margin-bottom: 10px;display: flex;justify-content: end">
          <el-button type="primary">新增</el-button>
        </div>
        <Table :header="serviceHeader" :data="serviceData" @handleLinkClick="editServiceFile($event)"/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import {ArrowLeft} from "@element-plus/icons-vue";
import Table from "@/view/main/common/Table.vue";
interface State {
  deviceCode: String;
  deviceName: String;
  deviceData: any;
  commandData: any[];
  eventData: any[];
  serviceData: any[]
}

const state = reactive<State>({
  deviceCode: '',
  deviceName: '',
  deviceData: {},
  commandData: [],
  eventData: [],
  serviceData: []
})
const {deviceCode, deviceName, deviceData, commandData, eventData, serviceData} = toRefs(state)

const router = useRouter()
watchEffect(() => {
  if (typeof router.currentRoute.value.query.deviceCode === 'string') {
    deviceCode.value = router.currentRoute.value.query.deviceCode || ''
  }
  if (typeof router.currentRoute.value.query.deviceName === 'string') {
    deviceName.value = router.currentRoute.value.query.deviceName || ''
  }
})

onMounted(()=>{
  deviceData.value={
    code: "CoffeeMaker",
    name: "咖啡机器人",
    imageUrl: new URL('@/assets/device/coffeeMaker.png', import.meta.url).href
  }
  commandData.value = [
    {
      commandId: "MakeCoffee",
      commandName: "做咖啡",
      eventNum: 3,
      serviceNum: 2
    }
  ]
  eventData.value = [
    {
      eventId: "event1",
      eventName: "onMakeCoffeeStart",
      commandId: "MakeCoffee",
      eventMethod: "prepare"
    },
    {
      eventId: "event2",
      eventName: "onMakeCoffeeComplete",
      commandId: "MakeCoffee",
      eventMethod: "sendMessage"
    },
    {
      eventId: "event3",
      eventName: "onMakeCoffeeError",
      commandId: "MakeCoffee",
      eventMethod: "handleError"
    }
  ]
  serviceData.value = [
    {
      serviceId: "AService",
      serviceName: "A品牌",
      serviceFile: "AService.java"
    }
  ]
})

const commandHeader = [
  {
    code: "commandId",
    name: "操作号",
    type: "String"
  },
  {
    code: "commandName",
    name: "操作名",
    type: "String"
  },
  {
    code: "eventNum",
    name: "已绑定事件",
    type: "Number"
  },
  {
    code: "serviceNum",
    name: "支持服务",
    type: "Number"
  }
]

const eventHeader = [
  {
    code: "eventId",
    name: "事件号",
    type: "String"
  },
  {
    code: "eventName",
    name: "事件名",
    type: "String"
  },
  {
    code: "commandId",
    name: "绑定操作号",
    type: "String"
  },
  {
    code: "eventMethod",
    name: "事件方法名",
    type: "String"
  }
]

const serviceHeader = [
  {
    code: "serviceId",
    name: "厂商号",
    type: "String"
  },
  {
    code: "serviceName",
    name: "厂商名",
    type: "String"
  },
  {
    code: "serviceFile",
    name: "厂商定义文件",
    type: "Link"
  }
]

const editServiceFile = (fileName)=>{
  console.log(fileName)
}
</script>
