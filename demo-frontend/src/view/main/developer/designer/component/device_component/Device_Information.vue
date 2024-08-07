<template>
  <div>
    <div style="padding:  20px; " >
      <el-form
          :rules="rules"
          :model="deviceForm"
          ref = "fieldFormRef"
          label-width="auto"
          label-position="left"
          style="max-width: 800px;margin: auto;"
      >
        <el-form-item label="设备ID" prop="deviceID">
          <el-input v-model="deviceForm.deviceID" placeholder="请输入"/>
        </el-form-item>
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="deviceForm.deviceName" placeholder="请输入"/>
        </el-form-item>
        <el-form-item label="设备类型" prop="deviceType">
          <el-input v-model="deviceForm.deviceType" placeholder="请输入"/>
        </el-form-item>
          <div class="domain-subtitle" style="display: flex;justify-content: space-between">
            <el-button type="primary" @click="submitForm(fieldFormRef)" style="margin-left: auto;">
              确认
            </el-button>
            <el-button @click="resetForm" style="margin-right: auto;">重置</el-button>
          </div>
      </el-form>
    </div>
  </div>
</template>

<script setup lang= "ts">

import {FormInstance, FormRules} from "element-plus";


//数据基本信息暂存数据结构
/**
 *
 * el-form-item 组件中的 prop 属性用于指定绑定的数据属性，以便与表单验证和错误提示关联。具体来说，prop 属性在以下几个方面起作用：
 * 表单验证：
 * 当 el-form-item 包含 prop 属性时，el-form 组件会根据验证规则对该属性进行验证。验证规则通常在 el-form 组件的 rules 属性中定义。prop 属性指定了该表单项对应的数据属性，这样可以将验证规则与数据属性关联起来。
 * 错误提示：
 * 当验证失败时，el-form-item 会显示相应的错误提示信息。通过 prop 属性，el-form-item 知道需要显示哪个属性的错误信息。
 * */
interface RuleForm{
  deviceID: string;
  deviceName: string;
  deviceType:string;
}

const fieldFormRef = ref<FormInstance>()
//验证规则,form-item中使用prop数据属性绑定此处的验证规则
const rules = reactive<FormRules<RuleForm>>({
  deviceID:[
    {required: true, message:'请输入设备ID', trigger:'blur'},
  ],
  deviceName:[
    {required: true, message:'请输入设备名', trigger:'blur'},
  ],
  deviceType:[
    {required: true, message:'请输入设备类型', trigger:'blur'},
  ]
})

const deviceForm = reactive<RuleForm>({
  deviceID:"",
  deviceName:"",
  deviceType:""
})
const resetForm = () => {
  if(fieldFormRef){
    fieldFormRef.value.resetFields()
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

<style >



</style>