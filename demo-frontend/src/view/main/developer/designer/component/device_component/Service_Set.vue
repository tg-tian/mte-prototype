<template>
  <!--服务表单-->
  <div style="display: flex;justify-content: space-between;margin-top: 20px;">
    <div id="支持服务" style="margin-top: 10px;">设备支持</div>
    <el-button  @click="serviceVisible = true" type="primary" style="margin-left: auto; margin-bottom:20px;" >新增设备支持</el-button>
  </div>
  <Table :header="service_header" :data="service_data" @handleEdit="onEdit"/>

  <!--新增服务-->
  <el-dialog
      v-model="serviceVisible"
      width="40%"
      append-to-body
  >
    <div style="display: flex;justify-content: space-between;">
      <div id="新增设备支持" style="margin-bottom: 20px; font-size: large;">新增设备支持</div>
    </div>
    <el-form
        :rules="service_rules"
        :model="ServiceForm"
        ref = "serviceFormRef"
        label-width="auto"
        label-position="left"
        style="max-width: 800px;margin: auto;"
    >
      <el-form-item label="厂家名称" prop="factory_Name">
        <el-input v-model="ServiceForm.factory_Name" placeholder="请输入"/>
      </el-form-item>
      <el-form-item label="服务描述" prop="factory_Description">
        <el-input v-model="ServiceForm.factory_Description" placeholder="请输入"/>
      </el-form-item>
      <div class="domain-subtitle" style="display: flex;justify-content: space-between">
        <el-button type="primary" @click="submitForm(serviceFormRef)" style="margin-left: auto;">
          确认
        </el-button>
        <el-button @click="resetForm" style="margin-right: auto;">重置</el-button>
      </div>
    </el-form>
  </el-dialog>

  <!--编辑服务-->
  <el-dialog
      v-model="dialogVisible"
      width="40%"
      append-to-body
  >
    <div style="display: flex;justify-content: space-between;">
      <div id="编辑设备支持" style="margin-bottom: 20px; font-size: large;">设备支持编辑</div>
    </div>
    <el-form
        :rules="edit_rules"
        :model="ServiceForm"
        ref = "serviceFormRef"
        label-width="auto"
        label-position="left"
        style="max-width: 800px;margin: auto;"
    >
      <el-form-item label="厂家名称" prop="factory_Name">
        <el-input v-model="ServiceForm.factory_Name" :placeholder="selectedService?.factory_Name || '请输入厂家名称'"/>
      </el-form-item>
      <el-form-item label="服务描述" prop="factory_Description">
        <el-input v-model="ServiceForm.factory_Description" :placeholder="selectedService?.factory_Description || '请输入服务描述'"/>
      </el-form-item>
      <el-form-item>
        <div>
          点击<a href="https://www.gitlink.org.cn" target="_blank">此处</a>编辑文件
        </div>
      </el-form-item>
      <div class="domain-subtitle" style="display: flex;justify-content: space-between">
        <el-button type="primary" @click="submitForm(serviceFormRef)" style="margin-left: auto;">
          确认
        </el-button>
        <el-button @click="resetForm" style="margin-right: auto;">重置</el-button>
      </div>
    </el-form>
  </el-dialog>
</template>

<script  setup lang = "ts">

import Table from "@/view/main/common/Table.vue";
import {ElMessage, FormInstance, FormRules} from 'element-plus'

interface State{
  service_header:any[];
  service_data:any[];
  serviceVisible:boolean;
  dialogVisible:boolean;
  dia_title:String;
  selectedService: any;  // 添加一个 selectedService 用于保存选中的行数据
}
interface Service_RuleForm{
  factory_Name:String;
  factory_Description:String;
  //signature:String;
  //event_Args:
}


const serviceFormRef = ref<FormInstance>()

const ServiceForm = reactive<Service_RuleForm>({
  factory_Name:"",
  factory_Description:"",
})

const edit_rules = reactive<FormRules<Service_RuleForm>>({
  factory_Name:[
    {required: false, message:'请输入厂家名称', trigger:'blur'},
  ],
  factory_Description:[
    {required: false, message:'请输入服务描述', trigger:'blur'},
  ]

})

const service_rules = reactive<FormRules<Service_RuleForm>>({
  factory_Name:[
    {required: true, message:'请输入厂家名称', trigger:'blur'},
  ],
  factory_Description:[
    {required: true, message:'请输入服务描述', trigger:'blur'},
  ]

})

const  state = reactive<State>({
  service_header:[
    {
      code:"factory_Name",
      name:"厂商名称",
      type:"String"
    },{
      code:"factory_Description",
      name:"支持服务描述",
      type:"String"
    },{
      code:"factory_file",
      name:"服务定义文件",
      type:"Link"
    },
  ],
  service_data:[
    {
      factory_Name:"A公司",
      factory_Description:"A公司咖啡机可以做（摩卡，美式）咖啡，支持（加糖，加奶）操作。",
      factory_file:"AService.json（文件生成后自动展示）"
    }
  ],
  serviceVisible:false,
  dialogVisible:false,
  dia_title: "",
  selectedService: null,  // 初始化为空
})



const onEdit = (row) =>{
  //要修改 dialogVisible 的值，应该修改 dialogVisible.value 而不是 dialogVisible，因为 dialogVisible 是一个 ref 对象，实际的值存储在 value 属性中。
  //这种情况下，dialogVisible 被重新赋值成一个布尔值 true，而不是修改原来的 ref 对象。
  dialogVisible.value = true;
  state.selectedService = row;  // 保存选中的行数据
  dia_title.value = row.name;
  console.log(row.name);
};

const  {service_header,service_data,header ,data,serviceVisible,dialogVisible,dia_title,selectedService} = toRefs(state)

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (serviceFormRef) {
      console.log('submit!service', ServiceForm);
      state.serviceVisible = false;
      serviceFormRef.value.resetFields()
    } else {
      console.log('error submit!', fields);
    }

  })
}

const resetForm = () => {
if(serviceFormRef){
    serviceFormRef.value.resetFields()
  }
  return
}

</script>

<style >



</style>