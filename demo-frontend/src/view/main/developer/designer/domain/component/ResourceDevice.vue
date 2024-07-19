<template>
  <div class="domain-subtitle" style="display: flex;justify-content: space-between">
    <el-button type="primary"  @click="dialogVisible = true" style="margin-left: auto;">添加设备类型</el-button>
  </div>
  <div class = "domain-content">
    <Table :header="header" :data="data" :canChoose="true" />
  </div>

  <!--添加设备表单-->
  <el-dialog v-model = "dialogVisible" title="添加设备类型" width="50%">
    <div style="padding: 20px">
      <el-form
          :rules="rules"
          :model="deviceForm"
          ref="deviceFormRef"
          label-width="auto"
          label-position="left"
          style="max-width: 500px"
      >
        <el-form-item label="设备ID" prop="deviceId">
          <el-input v-model="deviceForm.deviceId" placeholder="请输入"/>
        </el-form-item>
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="deviceForm.deviceName" placeholder="请输入"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm(deviceFormRef)">
            确认
          </el-button>
          <el-button @click="resetForm(ruleFormRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
</template>

<script setup lang = "ts">

import Table from "@/view/main/common/Table.vue";
import {FormInstance, FormRules} from "element-plus";

const props = defineProps({
  resourceId: String,
  resourceName: String
})

//定义接口
interface State{
  header: any[];
  data: any[];
  dialogVisible: boolean;
  selectedDevice: String;
}

interface RuleForm
{
  deviceNumber: number;
  deviceId: String;
  deviceName: String;

}

const rules = reactive<FormRules<RuleForm>>({
  deviceId:[
    { required: true, message: '请填写设备ID号', trigger: 'blur' },
  ],
  deviceName:[
    { required: true, message: '请选择设备名称', trigger: 'blur' },
  ]
})

const deviceFormRef = ref<FormInstance>()
const deviceForm = reactive<RuleForm>({
  deviceNumber:"",
  deviceId:"",
  deviceName:""
})

//初始化
const state = reactive<State>({
  header:[],
  data:[],
  dialogVisible:false,
  selectedDevice:"",
})

//获取元素结点
const {header, data, dialogVisible,selectedDevice} = toRefs(state)


//赋值
onMounted(()=> {
  header.value = [{
    code: "ID",
    name: "设备ID",
    type: "String"
  },{
    code: "name",
    name: "设备名称",
    type: "String"
  }]
  data.value = [{
    ID: "AbCdeF",
    name: "咖啡机"
  },{
    ID: "AdCeeF",
    name: "空调"
  }]
})

const handleReturnDomainDevice = ()=>{
  if (deviceFormRef){
    deviceFormRef.value.resetFields()
  }
  selectedDevice.value = ''
}

const resetForm = () => {
  if(deviceFormRef){
    deviceFormRef.value.resetFields()
  }
  return
}

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      console.log('submit!', deviceForm)
    } else {
      console.log('error submit!', fields)
    }
  })
}
</script>

<style scoped>

</style>