<template>
  <div class="domain-subtitle" style="display: flex;justify-content: space-between">
    <div id="字段列表">字段列表</div>
    <el-button @click="dialogVisible = true" type="primary" style="margin-left: auto;">新增字段</el-button>
  </div>
  <div class="domain-content">
    <Table :header="header" :data="data" :canChoose="true" />
  </div>

  <el-dialog v-model="dialogVisible" title="新增字段" width="50%">
    <div style="padding:  20px">
      <el-form
        :rules="rules"
        :model="fieldForm"
        ref = "fieldFormRef"
        label-width="auto"
        label-position="left"
        style="max-width: 500px"
      >
        <el-form-item label="字段号" prop="fieldNumber">
          <el-input v-model="FieldForm.fieldNumber" placeholder="请输入"/>
        </el-form-item>
        <el-form-item label="字段名称" prop="fieldName">
          <el-input v-model="FieldForm.fieldName" placeholder="请输入"/>
        </el-form-item>
        <el-form-item label="数据类型" prop="fieldType">
          <el-input v-model="FieldForm.fieldType" placeholder="请输入"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm(fieldFormRef)">
            确认
          </el-button>
          <el-button @click="resetForm(ruleFormRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">

import Table from "@/view/main/common/Table.vue";
import {FormInstance, FormRules} from "element-plus";

//暂时没用
const props = defineProps({
  domainId: String,
  domainName: String
});

interface State{
  header: any[];
  data: any[];
  dialogVisible: boolean;
}

interface RuleForm
{
  fieldNumber:String;
  fieldName:String;
  fieldType:String;
}

const rules = reactive<FormRules<RuleForm>>({
  fieldNumber:[
    {required: true, message:'请输入字段号', trigger:'blur'},
  ],
  fieldName:[
    {required: true, message:'请输入字段名称', trigger:'blur'},
  ],
  fieldType:[
    {required: true, message:'请输入字段数据类型', trigger:'blur'},
  ]
})


const fieldFormRef = ref<FormInstance>()
const FieldForm = reactive<RuleForm>({
  fieldNumber:"",
  fieldName:"",
  fieldType:""
})

const state = reactive<State>({
  header: [],
  data:[],
  dialogVisible: false,
})

const  {header ,data,dialogVisible} = toRefs(state)

onMounted(()=>{
  header.value = [{
    code: "number",
    name: "字段号",
    type: "String"
  },{
    code: "name",
    name: "字段名称",
    type: "String"
  },{
    code: "type",
    name: "数据类型",
    type: "String"
  }]
  data.value = [{
    number: "001",
    name: "楼层",
    type: "字符串"
  },{
    number: "002",
    name: "描述",
    type: "字符串"
  },{
    number: "003",
    name: "平面图",
    type: "图像"
  }]
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
      console.log('submit!', FieldForm)
    } else {
      console.log('error submit!', fields)
    }
  })
}
</script>
