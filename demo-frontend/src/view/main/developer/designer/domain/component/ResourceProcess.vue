<template>
  <div class="domain-subtitle" style="display: flex;justify-content: space-between">
    <el-button type="primary" @click="dialogVisible = true" style="margin-left: auto;">添加流程类型</el-button>
  </div>
  <div class = "domain-content">
    <Table :header="header" :data="data" :canChoose="true" />
  </div>
  <el-dialog v-model = "dialogVisible" title="添加流程类型" width="50%">
    <div style="padding: 20px">
    <el-form
      :model="ProcessForm"
      :rules="rules"
      ref="processFormRef"
      label-width="auto"
      label-position="left"
      style="max-width: 500px"
    >
      <el-form-item label="流程号" prop="ProcessNumber">
        <el-input v-model="ProcessForm.ProcessNumber" placeholder="请输入"/>
      </el-form-item>
      <el-form-item label="流程名称" prop="ProcessName">
        <el-input v-model="ProcessForm.ProcessName" placeholder="请输入"/>
      </el-form-item>
      <el-form-item label="流程简介" prop="ProcessBrief">
        <el-input v-model="ProcessForm.ProcessBrief" placeholder="请输入"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm(processFormRef)">
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

//定义状态接口
interface State{
  header: any[];
  data: any[];
  dialogVisible: boolean;
  selectedDevice: String;
}

//定义表格接口
interface RuleForm
{
  ProcessNumber: String;
  ProcessName:String;
  ProcessBrief:String;
}

//表格规则（接口形式）初始化

const rules = reactive<FormRules<RuleForm>>({
  ProcessNumber:[
    {required: true, message: '请填写流程号', trigger:'blur'},
  ],
  ProcessName:[
    {required: true, message: '请填写流程名称', trigger:'blur'}
  ],
  ProcessBrief:[
    {required: true, message: '请填写流程简介', trigger:'blur'}
  ],
})

const processFormRef = ref<FormInstance>()
//接口初始化
const ProcessForm = reactive<RuleForm>({
  ProcessNumber: "",
  ProcessName:"",
  ProcessBrief:""
})

//初始化
const state = reactive<State>({
  header:[],
  data:[],
  dialogVisible: false,
  selectedProcess:"",
})

//从初始化的state中获取元素结点
const {header, data,dialogVisible,selectedProcess} = toRefs(state)

//获取

//赋值
onMounted(()=> {
  header.value = [{
    code: "number",
    name: "流程号",
    type: "String"
  },{
    code: "name",
    name: "流程名称",
    type: "String"
  },{
    code: "brief",
    name: "流程简介",
    type: "String"
  }]
  data.value = [{
    number: "001",
    name: "预约流程",
    brief: "此流程用于各类预约系统，可以实现预约时间选择、预约队列管理等"
  }]
})



const resetForm = () => {
  if(processFormRef){
    processFormRef.value.resetFields()
  }
  return
}

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      console.log('submit!', ProcessForm)
    } else {
      console.log('error submit!', fields)
    }
  })
}
</script>

<style scoped>

</style>