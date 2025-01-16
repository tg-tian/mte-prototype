<template>
  <div style="font-size: 16px">
    <el-button link @click="router.go(-1)" style="margin-right: 10px"><el-icon><ArrowLeft /></el-icon></el-button>
    <span>新增设备类型</span>
  </div>
  <el-steps style="margin: 30px" :active="active">
    <el-step title="Step 1" description="信息录入" :icon="Picture"/>
    <el-step title="Step 2" description="属性设置" :icon="Operation"/>
    <el-step title="Step 3" description="功能设置" :icon="Setting"/>
    <el-step title="Step 4" description="品牌设置" :icon="Edit"/>
  </el-steps>


  <div style="margin-top: 30px">
    <template v-if ="active === 0">
      <Device_Information  @update-info="updateDeviceInfo" :info="deviceInfo"/>
    </template>
    <template v-if ="active === 1">
      <Attribute_Set @attribute-info="" :name="deviceName" :info="deviceInfo"/>
    </template>
    <template v-if ="active === 2">
      <Function_Set @operation-info="updateOperation" :name="deviceName"/>
    </template>
    <template v-if ="active === 3">
      <Service_Set @service-info="updateService" :name="deviceName" :info="deviceInfo" />
    </template>
  </div>

  <div style="margin-right: 40px;display: flex;justify-content: end">
    <el-button style="margin-left: 12px;margin-top: 20px" @click="prev" v-if="active!==0">上一步</el-button>
    <el-button type="primary" style="margin-left: 12px;margin-top: 20px" @click="next" v-if="active!==3 && active !== 0">下一步</el-button>
    <el-button type="primary" style="margin-left: 12px;margin-top: 20px" v-if="active===3" @click="UploadDeviceInfo">完成</el-button>
  </div>
</template>
<script setup lang="ts">

import Device_Information from "./device_component/Device_Information.vue";
import Function_Set from "./device_component/Function_Set.vue";
import Service_Set from "./device_component/Service_Set.vue";
import Attribute_Set from "./device_component/Attribute_Set.vue";
import {ArrowLeft, Edit, Picture, Setting, Operation} from "@element-plus/icons-vue";
import {initDeviceType, uploadDeviceType} from "@/api/DeviceExpand";
import {ElMessage} from "element-plus";

const router = useRouter()

interface State {
  active: number;
  //保存DeviceJson所需的所有参数
  deviceName:string;
  deviceInfo: any;
  //保存Function_set所需中的所需参数
  operationCode:String[];
  operationInfo: any[];
  factoryCode:String[];
  factoryInfo: any[];
}

const state = reactive<State>({
  active: 0,
  deviceName: "",
  deviceInfo: {},
  operationCode:[],
  operationInfo:[],
  factoryCode:[],
  factoryInfo:[],
})
const {active,deviceName, deviceInfo,operationCode,operationInfo,factoryCode,factoryInfo} = toRefs(state)

const prev = ()=>{
  active.value--
}

const next = ()=>{
  if (active.value++ > 2) active.value = 0
}

const updateDeviceInfo = (new_device: any) => {
  deviceName.value = new_device.deviceCode;
  deviceInfo.value = new_device;
  initDeviceType(new_device.deviceCode, new_device.deviceName, new_device.imageUrl??"").then((res)=>{
    if (res.status === 200){
      next()
    }
  })
  // next()
}

const updateOperation = (new_operation: any) =>{
  operationInfo.value.push(new_operation);  //传递操作码
  operationCode.value.push(new_operation.operation_Code);
  console.log(new_operation.operation_Code);
}

const updateService = (new_service: any)=>{
  factoryInfo.value.push(new_service);  //传递厂商号
  factoryCode.value.push(new_service.factory_Code);
  console.log(new_service.factory_Code);
}

const UploadDeviceInfo = () =>{
  uploadDeviceType({
    deviceTypeCode : deviceInfo.value.deviceCode,
    deviceTypeName : deviceInfo.value.deviceName,
    imgPath : deviceInfo.value.imgUrl
  }).then(async (res:any) => {
        if(res.status === 200){
          console.log('DeviceInfo posted successfully', res.data);
          ElMessage.success('上传设备类型成功')
          await router.push('/developer/workspace')
        }
  })
}
</script>
