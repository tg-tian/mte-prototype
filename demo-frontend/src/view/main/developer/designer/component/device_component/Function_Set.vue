<template>
  <div>
    <div style="display: flex;justify-content: space-between;">
      <div id="功能列表" style="margin-bottom: 20px;">操作列表</div>
      <div>——{{props.name}} </div>
      <el-button @click="operationVisible = true" type="primary" style="margin-left: auto;">新增操作</el-button>
    </div>
    <Table :header="header" :data="data" :canChoose="false" @handleEdit="onEdit"/>
  </div>

  <el-dialog v-model="dialogVisible" width="50%">
    <!--通过slot插槽来实现title的动态改变-->
    <span slot="title" style="font-size: large;">操作名称：{{dia_title}}</span>
    <div style="padding:  20px;">

      <!--操作信息编辑-->
      <div>
        <div id="操作信息编辑" style="margin-top: 10px; margin-bottom: 20px; ">操作信息编辑</div>
        <el-form
            :rules="operation_change_rules"
            :model="OperationForm"
            ref = "operationFormRef"
            label-width="auto"
            label-position="left"
            style="max-width: 800px;margin: auto;"
        >
          <el-form-item label="操作码" prop="operation_Code">
            <el-input v-model="OperationForm.operation_Code" :placeholder="selectedService?.code||'请输入操作码'"/>
          </el-form-item>
          <el-form-item label="操作名称" prop="operation_Name">
            <el-input v-model="OperationForm.operation_Name" :placeholder="selectedService?.name||'请输入操作名称'"/>
          </el-form-item>
        </el-form>
      </div>


      <!--事件表单-->
      <div style="display: flex;justify-content: space-between;">
        <div id="公共事件" style="margin-top: 10px;">公共事件</div>
        <el-button  @click="eventsVisible=true" type="primary" style="margin-left: auto; margin-bottom:20px;" plain>绑定事件</el-button>
      </div>
      <Table :header="event_header" :data="event_data"  @handleLinkClick="editEventFile($event)"  />

      <!--新增事件-->
      <el-dialog
          v-model="eventsVisible"
          width="40%"
          append-to-body
      >
        <div style="display: flex;justify-content: space-between;">
          <div id="已绑定事件" style="margin-bottom: 20px; font-size: large;">绑定事件</div>
        </div>
        <el-form
            :rules="event_rules"
            :model="EventForm"
            ref = "eventFormRef"
            label-width="auto"
            label-position="left"
            style="max-width: 800px;margin: auto;"
        >
          <el-form-item label="事件名称" prop="event_Name">
            <el-input v-model="EventForm.event_Name" placeholder="请输入"/>
          </el-form-item>
          <el-form-item label="事件描述" prop="event_Description">
            <el-input v-model="EventForm.event_Description" placeholder="请输入"/>
          </el-form-item>
          <el-form-item label="事件类型" prop="event_Type">
            <el-select v-model="EventForm.event_Type" placeholder="请选择你的事件类型">
              <!--此处应该为通用事件的选择，而不应该是只针对咖啡事件的选择？-->
              <el-option label="操作开始事件" value="onMakeCoffeeStart" :disabled = "!EventForm.eventType_Is_Chosen.onMakeCoffeeStart" />
              <el-option label="操作完成事件" value="onMakeCoffeeComplete" :disabled = "!EventForm.eventType_Is_Chosen.onMakeCoffeeComplete"/>
              <el-option label="操作错误事件" value="onMakeCoffeeError" :disabled = "!EventForm.eventType_Is_Chosen. onMakeCoffeeError"/>
            </el-select>
          </el-form-item>
          <!--
          <el-form-item label="函数名称" prop="signature">
            <el-input v-model="EventForm.signature" placeholder="请输入"/>
          </el-form-item>-->
          <!--输入函数参数，目前为默认参数
          <el-form-item label="函数参数" prop="event_Args">
            <el-select v-model="EventForm.event_Args" placeholder="请选择你的函数参数">
              <el-option label="(空)" value=""/>
              <el-option label="(Event)" value="Event event"/>
            </el-select>
          </el-form-item>
          -->
          <div class="domain-subtitle" style="display: flex;justify-content: space-between">
            <el-button type="primary" @click="submitForm(eventFormRef)" style="margin-left: auto;">
              确认
            </el-button>
            <el-button @click="resetForm" style="margin-right: auto;">重置</el-button>
          </div>
        </el-form>
      </el-dialog>
    </div>
    <div class="domain-subtitle" style="display: flex;justify-content: space-between">
      <el-button type="primary"  style="margin-left: auto;" @click="Commit">确定</el-button>
      <el-button type="primary"  style="margin-right: auto;" @click="dialogVisible = false" plain>返回</el-button>
    </div>
  </el-dialog>
  <el-dialog v-model="operationVisible" width=" 50%">
    <div id="新增操作" style="margin-top: 10px; margin-bottom: 20px; font-size: large">新增操作</div>
    <el-form
        :rules="operation_add_rules"
        :model="OperationForm"
        ref = "operationFormRef"
        label-width="auto"
        label-position="left"
        style="max-width: 800px;margin: auto;"
    >
      <el-form-item label="操作代码" prop="operation_Code">
        <el-input v-model="OperationForm.operation_Code" placeholder="请输入操作代码"/>
      </el-form-item>
      <el-form-item label="操作名称" prop="operation_Name">
        <el-input v-model="OperationForm.operation_Name" placeholder="请输入操作名称"/>
      </el-form-item>
      <el-form-item label="输入参数设定" prop="operation_InputParam">
        <div style="display: flex;justify-content: space-between; width: 700px">
          <el-button @click="" type="primary" style="margin-left: auto; margin-bottom: 20px;" plain>新增输入参数</el-button>
        </div>
        <Table :header="OperationForm.Param_header" :data="OperationForm.operation_InputParam"/>
      </el-form-item>
      <el-form-item label="输出参数设定" prop="operation_OutputParam">
        <el-select
            v-model="value"
            placeholder="请选择输出参数"
            style="width: 350px"
        >
          <el-option
              v-for="item in operation_OutputParam"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
      <div class="domain-subtitle" style="display: flex;justify-content: space-between">
        <el-button type="primary"  style="margin-left: auto;" @click="Commit">确定</el-button>
        <el-button type="primary"  style="margin-right: auto;" @click="operationVisible = false" plain>返回</el-button>
      </div>

    </el-form>
  </el-dialog>
</template>

<script setup lang="ts">
import Table from "@/view/main/common/Table.vue";
import {getDomainJson} from "../../../../../../api/DomainApi";
import {FormInstance, FormRules} from "element-plus";
import {getOperationEvent, getOperationParam} from "@/api/DeviceExpand";
import {Connect} from "vite";

/**
 * interface State 的作用是定义 TypeScript 接口，以便为 state 对象提供类型约束。这样可以在开发过程中利用 TypeScript 的类型检查功能，提高代码的可靠性和可维护性。
 * 可以不写接口直接定义响应式对象。TypeScript 接口是为了提供类型约束和开发辅助功能，但在 Vue 3 中，您可以直接使用 reactive 定义响应式对象，而不使用接口。
 * */
//定义TS接口
interface State{
  service_header:any[];
  service_data:any[];
  header: any[];
  data: any[];
  event_header:any[];
  event_data:any[];
  dialogVisible: boolean;
  eventsVisible:boolean;
  operationVisible:boolean;
  dia_title:String;
  selectedService: any;  // 添加一个 selectedService 用于保存选中的行数据
}
//基础事件是否已经被选择
interface EventType_Is_Chosen {
  onMakeCoffeeStart:boolean;
  onMakeCoffeeComplete:boolean;
  onMakeCoffeeError:boolean;
}
interface Operation_RuleForm{
  operation_Code:String;
  operation_Name:String;
  Param_header:any[]; //参数头
  operation_InputParam:any[];  //输入参数,用于公共操作函数
  operation_OutputParam:any[]; //输出参数，用于公共操作函数
}
interface Event_RuleForm{
  eventType_is_chosen: EventType_Is_Chosen;
  event_Name:String;
  event_Description:String;
  event_Type:String;
  // Type_chosen:
  signature:String;
  event_Args:String;
}
const eventFormRef = ref<FormInstance>()
const operationFormRef = ref<FormInstance>()
const event_rules = reactive<FormRules<Event_RuleForm>>({
  event_Name:[
    {required: true, message:'请输入事件名称', trigger:'blur'},
  ],
  event_Description:[
    {required: true, message:'请输入事件描述', trigger:'blur'},
  ],
  event_Type:[
    {required: true, message:'请选择事件类型', trigger:'blur'},
  ],
  signature:[
    {required: true, message:'请输入函数名', trigger:'blur'},
  ],
  event_Args:[
    {required: true, message:'请选择所需参数声明', trigger:'blur'},
  ]
})
const operation_add_rules = reactive<FormRules<Operation_RuleForm>> ({
  operation_Code:[
    {required: true, message:"请输入操作代码", trigger:'blur'},
  ],
  operation_Name:[
    {required: true, message:"请输入操作名称", trigger:'blur'},
  ]
})
const operation_change_rules = reactive<FormRules<Operation_RuleForm>> ({
  operation_Code:[
    {required: false, message:"请输入操作代码", trigger:'blur'},
  ],
  operation_Name:[
    {required: false, message:"请输入操作名称", trigger:'blur'},
  ]
})
const EventForm = reactive<Event_RuleForm> ({
  eventType_Is_Chosen:{
    onMakeCoffeeStart:true,
    onMakeCoffeeComplete:true,
    onMakeCoffeeError:true,
  },
  event_Name:"",
  event_Description:"",
  event_Type:"",
  signature:"",
  event_Args:"",
})
//定义响应式对象
const  state = reactive<State>({
  event_header:[
    {
      code:"name",
      name:"事件名称",
      type:"String"
    },{
      code:"description",
      name:"事件描述",
      type:"String"
    },{
      code:"type",
      name:"事件类型",
      type:"String"
    }
  ],
  event_data:[],
  header:[
    {
      code:"code",
      name:"操作码",
      type:"String"
    },{
      code:"name",
      name:"操作名称",
      type:"String"
    },{
      code:"events_count",
      name:"已绑定事件",
      type:"Int"
    }
  ],
  data:[
    {
      code:"MakeCoffee",
      name:"做咖啡",
      events_count:"3/3"
    }
  ],
  dialogVisible:false,
  eventsVisible:false,
  operationVisible:false,
  selectedService:null,
  dia_title:"编辑操作",
})
const  OperationForm = reactive<Operation_RuleForm>({
  operation_Code:"",
  operation_Name : "",
  Param_header: [
    {
      code:"code",
      name:"变量码",
      type:"String",
    },{
      code:"name",
      name:"变量名",
      type:"String",
    },{
      code:"type",
      name:"变量数据类型",
      type:"any",
    }
  ],
  operation_InputParam:[],
  operation_OutputParam:[
    {
      value: 'void',
      label: 'void',
    },
    {
      value: 'Object',
      label: 'Object',
    },
  ]
})
/**
 * 在 Vue 3 中，toRefs 函数用于将 reactive 对象的属性转换为 ref 对象。这样可以使这些属性在模板中直接使用，并且可以更方便地进行解构和传递。
 * 使用 toRefs 的意义
 * 1.保持响应式：
 *  将 reactive 对象中的属性转换为 ref 对象，确保它们在解构时保持响应式。
 *  避免失去响应式状态，这是因为在直接解构 reactive 对象时，得到的属性是普通的 JavaScript 值，而不是响应式的。
 * 2.便于解构和传递：
 *  可以方便地将 reactive 对象中的属性解构出来，并直接在模板或其他函数中使用。
 *  使得代码更加简洁和易读。
 * */
//toRefs(state) 会返回一个包含 state 各个属性的 ref 对象的对象
//在这里，header、data 和 dialogVisible 都是 ref 对象，它们分别引用 state 对象中的对应属性。
const  {event_header,event_data,header ,data,dialogVisible,eventsVisible,operationVisible,dia_title,selectedService} = toRefs(state)
const {Param_header,operation_InputParam,operation_OutputParam} = toRefs(OperationForm)
const value = ref('')
onMounted(()=>{
  if (import.meta.env.VITE_MODE === "mock"){
    operation_InputParam.value = [{
        code:"coffeeType",
        name:"咖啡类型",
        type:"String",
      }]
    event_data.value=[{
        event_name:"onMakeCoffeeStart",
        event_description:"prepare the coffee",
    }]
  }else {
    getInputParam()
    getEventData()
  }
})

const onEdit = (row) =>{
  //要修改 dialogVisible 的值，应该修改 dialogVisible.value 而不是 dialogVisible，因为 dialogVisible 是一个 ref 对象，实际的值存储在 value 属性中。
  //这种情况下，dialogVisible 被重新赋值成一个布尔值 true，而不是修改原来的 ref 对象。
  dialogVisible.value = true;
  dia_title.value = row.name;
  selectedService.value = row;
  console.log(row.name);
};
const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (eventFormRef) {
      console.log('submit!', EventForm);
      state.eventsVisible = false;
      eventFormRef.value.resetFields()
    } else {
      console.log('error submit!', fields);
    }
  })
}
const resetForm = () => {
  if(eventFormRef){
    eventFormRef.value.resetFields()
  }
  return
}
// 监控props的改变并且更新当前的字
const props = defineProps({
  name: String,
});
watchEffect(() => {
  //动态修改操作名称的值
  if(state.data.name){
    OperationForm.operation_Name = state.data.name;
  }

  //保证操作名称的传递
  if (props.name) {
    console.log("Device_name:", props.name);
  }
  else {
    console.log("Can't receive device_name");
  }
  //防止重复添加
  if(EventForm.event_Type == "onMakeCoffeeStart"){
    EventForm.eventType_Is_Chosen.onMakeCoffeeStart = false;
  }else if(EventForm.event_Type == "onMakeCoffeeComplete"){
    EventForm.eventType_Is_Chosen.onMakeCoffeeComplete = false;
  }else if(EventForm.event_Type == "onMakeCoffeeError"){
    EventForm.eventType_Is_Chosen.onMakeCoffeeError = false;
  }

});
const editEventFile = (fileName)=>{
  console.log(fileName)
}

const getInputParam = () =>{
  console.log("Try to get param!")
  getOperationParam(<String>props.name,"MakeCoffee").then((res:any) =>{
    if (res.status === 200){
      operation_InputParam.value = res.data
      console.log(operation_InputParam.value)
    }
  })
}

const getEventData =() =>{
  console.log("Try to get event data!")
  getOperationEvent(<String>props.name,"MakeCoffee").then((res:any) =>{
    if(res.status === 200){
      event_data.value = res.data
      console.log(res)
    }
  })
}
</script>

<style scoped>

</style>