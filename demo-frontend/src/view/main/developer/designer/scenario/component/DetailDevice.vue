<template>
  <div class="scenario-subtitle" style="display: flex;justify-content: space-between">
    <div id="设备">设备</div>
    <el-button type="primary" @click="dialogVisible = true">新增设备</el-button>
  </div>
  <div class="scenario-content">
    <Table :header="header" :data="data" />
  </div>
  <el-dialog v-model="dialogVisible" title="新增设备" width="50%">
<!--  选择设备类型  -->
    <div v-if="selectedDevice === ''">
      <div style="display: flex;justify-content: space-between;font-size: 16px;margin-bottom: 20px;margin-top: 10px">
        <div>领域设备类型</div>
        <div style="color: #50a5fb">
          <el-input
              v-model="searchInput"
              style="width: 240px"
              placeholder="从模板库搜索"
              :prefix-icon="Search"
          />
        </div>
      </div>
      <div style="display: flex;flex-wrap: wrap;gap: 20px;">
        <Card v-for="device in domainDevice" :key="device.code" :cardItem="device" @itemClick="handleDomainDeviceClick(device)" />
      </div>
    </div>
<!--  填写具体设备信息  -->
<!--  TODO：边缘设备获取  -->
    <div v-else>
      <div style="padding: 20px">
        <el-form
            :model="deviceForm"
            :rules="rules"
            ref="deviceFormRef"
            label-width="auto"
            label-position="left"
            style="max-width: 500px">
          <el-form-item label="设备码" prop="deviceCode">
            <el-input v-model="deviceForm.deviceCode" placeholder="请输入"/>
          </el-form-item>
          <el-form-item label="设备名" prop="deviceName">
            <el-input v-model="deviceForm.deviceName" placeholder="请输入"/>
          </el-form-item>
          <el-form-item label="设备类型" prop="deviceType.code">
            <el-select v-model="deviceForm.deviceType.code" disabled>
              <el-option v-for="device in domainDevice" :key="device.code" :label="device.name" :value="device.code" />
            </el-select>
          </el-form-item>
          <el-form-item label="设备品牌/厂商" prop="deviceService.code">
            <el-select v-model="deviceForm.deviceService.code" placeholder="请选择品牌/厂商">
              <el-option v-for="service in domainDevice.find(v=>v.code===deviceForm.deviceType.code)?.services ?? []" :key="service.code" :label="service.name" :value="service.code" />
            </el-select>
          </el-form-item>
          <el-form-item label="协议" prop="protocol">
            <el-select v-model="deviceForm.protocol" placeholder="请选择协议">
              <el-option label="HTTP" value="HTTP" />
              <el-option label="TCP/IP" value="TCP/IP" />
            </el-select>
          </el-form-item>
          <el-form-item label="主机名/IP地址" prop="host">
            <el-input v-model="deviceForm.host" placeholder="请输入"/>
          </el-form-item>
          <el-form-item label="端口号" prop="port">
            <el-input v-model="deviceForm.port" type="number" placeholder="请输入"/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm(deviceFormRef)">
              确认
            </el-button>
            <el-button @click="handleReturnDomainDevice">返回</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
import Table from "@/view/main/common/Table.vue";
import Card from "@/view/main/common/Card.vue";
import {Search} from "@element-plus/icons-vue";
import {FormInstance, FormRules} from "element-plus";
import {
  getScenarioResource,
  loadScenarioBindingData,
  loadScenarioData,
  uploadDeviceData,
  uploadDeviceRegisterData
} from "@/api/scenarioApi";

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
      name: "设备类型",
      type: "String"
    },
    {
      code: "deviceService",
      name: "品牌/厂商",
      type: "String"
    },
    {
      code: "protocol",
      name: "协议",//TCP/IP,HTTP
      type: "String"
    },
    {
      code: "host",
      name: "主机名/ip地址",
      type: "String"
    },
    {
      code: "port",
      name: "端口号",
      type: "Number"
    },
  ],
  data: [],
  domainDevice: [],
  dialogVisible: false,
  selectedDevice: '',
  searchInput: ''
})
const {header, data, domainDevice, dialogVisible, selectedDevice, searchInput} = toRefs(state)

interface RuleForm {
  deviceCode: String;
  deviceName: String;
  deviceType: any;
  deviceService: any;
  protocol: String;
  host: String;
  port: number|string;
}
const deviceFormRef = ref<FormInstance>()
const deviceForm = reactive<RuleForm>({
  deviceCode: '',
  deviceName: '',
  deviceType: '',
  deviceService: '',
  protocol: '',
  host: '',
  port: ''
})
const rules = reactive<FormRules<RuleForm>>({
  deviceCode: [
    { required: true, message: '请填写设备码', trigger: 'blur' },
  ],
  deviceName: [
    { required: true, message: '请填写设备名', trigger: 'blur' },
  ],
  "deviceType.code": [
    { required: true, message: '请选择设备类型', trigger: 'blur' },
  ],
  "deviceService.code": [
    { required: false, message: '请选择设备品牌/厂商', trigger: 'blur' },
  ],
  protocol: [
    { required: true, message: '请选择协议', trigger: 'blur' },
  ],
  host: [
    { required: true, message: '请填写主机名或ip地址', trigger: 'blur' },
  ],
  port: [
    { required: true, message: '请填写端口号', trigger: 'blur' },
  ],
})

onMounted(()=>{
  if (import.meta.env.VITE_MODE === "mock"){
    data.value = [{
      deviceCode: "deviceId",
      deviceName: "咖啡机器人A",
      deviceType: "咖啡机器人",
      deviceService: "A品牌",
      protocol: "HTTP",
      host: "http://aservice.coffee",
      port: 8080
    },
      {
        deviceCode: "deviceId2",
        deviceName: "咖啡机器人B",
        deviceType: "咖啡机器人",
        deviceService: "B品牌",
        protocol: "HTTP",
        host: "http://bservice.coffee",
        port: 8080
      }]

    domainDevice.value = [
      {
        code: "CoffeeMaker",
        name: "咖啡机器人",
        services: [
          {
            code: "AService",
            name: "A品牌"
          },
          {
            code: "BService",
            name: "B品牌"
          }
        ],
        imageUrl: new URL('@/assets/logo.png', import.meta.url).href
      },
      {
        code: "AirConditioner",
        name: "空调",
        services: [],
        imageUrl: new URL('@/assets/logo.png', import.meta.url).href
      }]

  }else {
    getScenarioDevice()
  }


})

const getScenarioDevice = ()=>{
  data.value = [];
  loadScenarioData("The second interdisciplinary building").then((res:any) =>{
    if (res.status === 200){
      const devices = res.data;
      devices.forEach((device: any) => {
        const newDevice = {
          deviceCode: device.deviceCode,
          deviceName: device.deviceName,
          deviceType:device.deviceType,
          deviceService: device.deviceService,
          protocol:device.protocol,
          host:device.host,
          port:device.port,
        }
        data.value.push(newDevice);
      })
    }
  })
  loadScenarioBindingData("Device","SmartBuilding").then((res:any)=>{
    if(res.status === 200){
      domainDevice.value = res.data.map( v => {
        return {
          code: v.deviceTypeCode,
          name:v.deviceTypeName,
          imageUrl: new URL('@/assets/logo.png', import.meta.url).href
        }
      })
    }
  })
}

const handleDomainDeviceClick = (device)=>{
  selectedDevice.value = device
  deviceForm.deviceType = device
}

const handleReturnDomainDevice = ()=>{
  if (deviceFormRef){
    deviceFormRef.value.resetFields()
  }
  selectedDevice.value = ''
}

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {

      uploadDeviceData({deviceForm:deviceForm}).then((res:any) => {
        if(res.status === 200){
          ElMessage.success("设备实例上传成功")
          uploadDeviceRegisterData({deviceForm:deviceForm},"The second interdisciplinary building",deviceForm.deviceName).then(async (res:any)=>{
            if(res.status === 200){
              ElMessage.success("设备实例注册成功")
              await getScenarioDevice();
              selectedDevice.value='';
            }
          })
        }
      })
      console.log('submit!', deviceForm)
    } else {
      console.log('error submit!', fields)
    }
  })
}
</script>
