<template>
  <div style="font-size: 16px">
    <el-button link @click="router.go(-1)" style="margin-right: 10px"><el-icon><ArrowLeft /></el-icon></el-button>
    <span>新增设备类型</span>
  </div>
  <el-steps style="margin: 30px" :active="active">
    <el-step title="Step 1" description="信息录入" :icon="Picture"/>
    <el-step title="Step 2" description="功能设置" :icon="Setting"/>
    <el-step title="Step 3" description="品牌设置" :icon="Edit"/>
  </el-steps>


  <div style="margin-top: 30px">
    <template v-if ="active === 0">
      <Device_Information  @update-info="updateDeviceInfo" :info="device_name"/>

    </template>
    <template v-if ="active === 1">
      <Function_Set :name="device_name"/>
    </template>
    <template v-if ="active === 2">
      <Service_Set :name="device_name"/>
    </template>
  </div>

  <div style="margin-top: 20px;display: flex;justify-content: end">
    <el-button style="margin-left: 12px" @click="prev" v-if="active!==0">上一步</el-button>
    <el-button style="margin-left: 12px" @click="next" v-if="active!==2">下一步</el-button>
    <el-button type="primary" style="margin-left: 12px" v-if="active===2">完成</el-button>
  </div>
</template>
<script setup lang="ts">

import Device_Information from "./device_component/Device_Information.vue";
import Function_Set from "./device_component/Function_Set.vue";
import Service_Set from "./device_component/Service_Set.vue";
import {ArrowLeft, Edit, Picture, Setting} from "@element-plus/icons-vue";

const router = useRouter()

interface State {
  active: number;

  //保存DeviceJson所需的所有参数
  device_name:String;
}

const state = reactive<State>({
  active: 0,
  device_name: "",
})
const {active,device_name} = toRefs(state)

const prev = ()=>{
  active.value--
}

const next = ()=>{
  if (active.value++ > 2) active.value = 0
}

const updateDeviceInfo = (new_device_name: String) => {
  state.device_name = new_device_name;
  console.log(state.device_name);
}

</script>
