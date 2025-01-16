<template>
  <div>
    <div style="display: flex;justify-content: space-between;">
      <div id="属性列表" style="margin-bottom: 20px;">属性列表</div>
      <div>——{{props.name}} </div>
      <el-button @click="dialogVisible = true" type="primary" style="margin-left: auto;">新增属性</el-button>
    </div>
    <Table :header="header" :data="data" :canChoose="false" @handleEdit="onEdit"/>
  </div>

  <!--新增属性-->
  <el-dialog
      v-model="dialogVisible"
      width="40%"
      append-to-body
  >
    <div style="display: flex;justify-content: space-between;">
      <div id="新增设备属性" style="margin-bottom: 20px; font-size: large;">新增设备属性</div>
    </div>
    <el-form
        :rules="attribute_rules"
        :model="AttributeForm"
        ref = "attributeFormRef"
        label-width="auto"
        label-position="left"
        style="max-width: 800px;margin: auto;"
    >
      <el-form-item label="属性名称" prop="attribute_Name">
        <el-input v-model="AttributeForm.attribute_Name"  placeholder="请输入"/>
      </el-form-item>
      <el-form-item label="属性编码" prop="attribute_Code">
        <el-input v-model="AttributeForm.attribute_Code" placeholder="请输入"/>
      </el-form-item>
      <el-form-item label="读写权限" prop="accessMode">
        <el-radio-group v-model="AttributeForm.accessMode">
          <el-radio value="r">只读</el-radio>
          <el-radio value="w">只写</el-radio>
          <el-radio value="rw">读写</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否存在范围" prop="enableValidate">
        <el-select v-model="AttributeForm.enableValidate" placeholder="请选择" style="width: 240px">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item v-if="AttributeForm.enableValidate=== 'true'" label="属性数据范围" prop="validateParams">
        <el-input v-model="AttributeForm.validateParams" placeholder="请输入"/>
      </el-form-item>

      <el-form-item label="属性数据类型" prop="attribute_Type">
        <el-select v-model="AttributeForm.attribute_Type" placeholder="请选择" style="width: 240px">
          <el-option
              v-for="item in type_options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item v-if="AttributeForm.attribute_Type !== 'bool' && AttributeForm.attribute_Type !== '' " label="属性单位" prop="attribute_Unit">
        <el-input v-model="AttributeForm.attribute_Unit" placeholder="请输入"/>
      </el-form-item>
      <el-form-item v-if="AttributeForm.attribute_Type !== 'bool' && AttributeForm.attribute_Type !== '' " label="属性单位名称" prop="attribute_UnitName">
        <el-input v-model="AttributeForm.attribute_UnitName" placeholder="请输入"/>
      </el-form-item>




      <div class="domain-subtitle" style="display: flex;justify-content: space-between">
        <el-button type="primary" @click="submitForm(attributeFormRef)" style="margin-left: auto;">
          确认
        </el-button>
        <el-button @click="resetForm" style="margin-right: auto;">重置</el-button>
      </div>
    </el-form>
  </el-dialog>

</template>

<script setup lang="ts">
import Table from "@/view/main/common/Table.vue";
import router from "@/router";
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {addDeviceProperty, getAttribute} from "@/api/DeviceExpand";

interface State{
  header:any[],
  data:any[],
  dialogVisible:boolean;
  attributeVisible:boolean;
  dia_title:string;
  selectedAttribute: any;  // 添加一个 selectedService 用于保存选中的行数据
}

interface Attribute_RuleForm{
  attribute_Name: string;
  attribute_Code: string;
  accessMode:string;
  enableValidate:boolean;
  validateParams: any[];
  attribute_Unit: string;
  attribute_UnitName: string;
  attribute_Type:string;

}

const type_options = [
  {
    value:'float',
    label:'浮点数',
  },{
    value: 'int',
    label: '整形',
  }
]

const options = [
  {
    value: 'true',
    label: '是',
  },
  {
    value: 'false',
    label: '否',
  },
]

const emit = defineEmits(['attribute-info'])
const attributeFormRef = ref<FormInstance>()
const AttributeForm = reactive<Attribute_RuleForm>({
  attribute_Name: "",
  attribute_Code:"",
  accessMode:"",
  enableValidate:"",
  validateParams:[],
  attribute_Unit:"",
  attribute_UnitName:"",
  attribute_Type:"",
})
const state = reactive<State>({
  header:[
    {
      code:"name",
      name:"属性名称",
      type:"String",
    },{
      code:"identifier",
      name:"属性编码",
      type:"String",
    },{
      code:"type",
      name:"数据类型",
      type:"String",
    },{
      code:"unit",
      name:"单位",
      type:"String",
    },{
      code:"unitName",
      name:"单位名称",
      type:"String",
    }
  ],
  data:[],
  dialogVisible:false,
  attributeVisible:false,
  dia_title: "",
  selectedAttribute: null,  // 初始化为空
})

const attribute_rules = reactive<FormRules<Attribute_RuleForm>>({
  attribute_Name:[
    {required: true, message:'请输入属性名称', trigger:'blur'},
  ],
  attribute_Code:[
    {required: true, message:'请输入属性代码', trigger:'blur'},
  ],
  accessMode:[
    {required: true, message:'请选择读写权限', trigger:'blur'},
  ],
  enableValidate:[
    {required: true, message:'请选择是否存在范围', trigger:'blur'},
  ],
  validateParams:[
    {message:'请输入数据范围', trigger:'blur'},
  ],
  attribute_Type:[
    {required: true, message:'请选择数据类型', trigger:'blur'},
  ],
  attribute_Unit:[
    {message:'请输入属性单位', trigger:'blur'},
  ],
  attribute_UnitName:[
    { message:'请输入属性单位名称', trigger:'blur'},
  ],
})

const {header,data,dialogVisible,attributeVisible,dia_title,selectedAttribute}=toRefs(state)

onMounted(()=>{
  getAttributeData()
})

const props = defineProps({
  name: String,
  info: Object,
});

const onEdit = (row) => {

}

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {

      const specs={
        unit:AttributeForm.attribute_Unit,
        unitName:AttributeForm.attribute_UnitName,
      }
      const dataType = {
        type: AttributeForm.attribute_Type,
        specs:specs,
      }
      const attribute = {
        identifier: AttributeForm.attribute_Code,
        name: AttributeForm.attribute_Name,
        accessMode:AttributeForm.accessMode,
        enableValidate:AttributeForm.enableValidate,
        validateParams:[], //todo:这里需要根据enableValidate做出修改，暂时赋值为空
        dataDataType:dataType,
      }
      const propertiesList = []
      propertiesList.push(attribute)
      const data={
        deviceType:<String>props.info.deviceCode,
        properties:propertiesList,
      }

      addDeviceProperty(data).then((res)=>{
        if (res.status === 200){
          ElMessage.success("新增属性成功")
          state.attriVisible = false;
          attributeFormRef.value.resetFields();
          getAttributeData();
        }
      })
    } else {
      console.log('error submit!', fields);
    }

  })
}

const getAttributeData =() =>{
  getAttribute(<String>props.info.deviceCode).then((res:any) =>{
    if(res.status === 200){

      data.value = res.data.map((v: any)=>{
        return {
          name: v.name,
          identifier: v.identifier,
          type: v.deviceDataType.type,
          unit: v.deviceDataType.specs.unit,
          unitName: v.deviceDataType.specs.unitName,
        }
      })
    }
  })
}

const resetForm = () => {
  if(attributeFormRef){
    attributeFormRef.value.resetFields()
  }
  return
}
const changeForm = () => {
  console.log("change the form");
}
</script>

<style scoped>

</style>