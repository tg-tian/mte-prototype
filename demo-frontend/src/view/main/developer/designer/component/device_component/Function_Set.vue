<template>
  <div>
    <div style="display: flex;justify-content: space-between;">
      <div id="功能列表" style="margin-bottom: 20px;">操作列表</div>
      <div>——{{props.name}} </div>
      <el-button @click="operationVisible = true" type="primary" style="margin-left: auto;">新增操作</el-button>
    </div>
    <Table :header="header" :data="data" :canChoose="false" @handleEdit="onEdit"/>
  </div>

<!--  操作编辑表单-->
  <el-dialog v-model="dialogVisible" width="50%" @close="beforeOperationEditClose">
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

          <el-form-item label="输入参数设定" prop="operation_InputParam">
            <div style="display: flex;justify-content: space-between; width: 600px">
              <el-button @click="" type="primary" style="margin-left: auto; margin-bottom: 20px;" plain>新增输入参数</el-button>
            </div>
            <Table :header="OperationForm.Param_header" :data="OperationForm.operation_InputParam"/>
          </el-form-item>
          <el-form-item label="输出参数设定" prop="operation_OutputParam">
            <el-select
                v-model="operation_OutputParam"
                placeholder="请选择输出参数"
                style="width: 350px"
            >
              <el-option
                  v-for="item in outputParams"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-form>
      </div>


      <!--事件表单-->
      <div style="display: flex;justify-content: space-between;">
        <div id="公共事件" style="margin-top: 10px;">公共事件</div>
        <el-button  @click="event_data.length===3 ? $message.warning('当前已无可绑定事件') : eventsVisible=true" type="primary" style="margin-left: auto; margin-bottom:20px;" plain>绑定事件</el-button>
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
              <el-option label="操作开始事件" value="onStart" :disabled = "!EventForm.eventType_Is_Chosen.onStart" />
              <el-option label="操作完成事件" value="onComplete" :disabled = "!EventForm.eventType_Is_Chosen.onComplete"/>
              <el-option label="操作错误事件" value="onError" :disabled = "!EventForm.eventType_Is_Chosen.onError"/>
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
<!--  新增操作表单-->
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
            v-model="operation_OutputParam"
            placeholder="请选择输出参数"
            style="width: 350px"
        >
          <el-option
              v-for="item in outputParams"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
      <div class="domain-subtitle" style="display: flex;justify-content: space-between">
        <el-button type="primary"  style="margin-left: auto;" @click="CommitOperation(operationFormRef)">确定</el-button>
        <el-button  style="margin-right: auto;" @click="operationVisible = false">返回</el-button>
      </div>

    </el-form>
  </el-dialog>
</template>

<script setup lang="ts">
import Table from "@/view/main/common/Table.vue";
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {
  addDeviceTypeEvent,
  addDeviceTypeOperation,
  getOperationCommand,
  getOperationEvent,
  getOperationParam
} from "@/api/DeviceExpand";

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
  onStart:boolean;
  onComplete:boolean;
  onError:boolean;
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

// rules
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

// form
// 添加事件
const EventForm = reactive<Event_RuleForm> ({
  eventType_Is_Chosen:{
    onStart:true,
    onComplete:true,
    onError:true,
  },
  event_Name:"",
  event_Description:"",
  event_Type:"",
  signature:"",
  event_Args:"",
})
//列表展示及visible变量
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
  data:[],
  dialogVisible:false,
  eventsVisible:false,
  operationVisible:false,
  selectedService:null,
  dia_title:"编辑操作",
})
// 编辑&新增操作表单
const outputParams = [
  {
    value: 'void',
    label: 'void',
  },
  {
    value: 'Object',
    label: 'Object',
  },
]
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
  operation_OutputParam:''
})
//toRefs(state) 会返回一个包含 state 各个属性的 ref 对象的对象
//在这里，header、data 和 dialogVisible 都是 ref 对象，它们分别引用 state 对象中的对应属性。
const  {event_header,event_data,header ,data,dialogVisible,eventsVisible,operationVisible,dia_title,selectedService} = toRefs(state)
const {Param_header,operation_InputParam,operation_OutputParam} = toRefs(OperationForm)
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
  }
  getCommandData()
})


const emit = defineEmits(['operation-info'])
// 监控props的改变并且更新当前的字
const props = defineProps({
  name: String,
});
watchEffect(() => {
  //保证操作名称的传递
  if (props.name) {
    console.log("Device_name:", props.name);
  }
  else {
    console.log("Can't receive device_name",props);
  }
  //防止重复添加
  // if(EventForm.event_Type.includes("Start")){
  //   EventForm.eventType_Is_Chosen.onStart = false;
  // }else if(EventForm.event_Type.includes("Complete")){
  //   EventForm.eventType_Is_Chosen.onComplete = false;
  // }else if(EventForm.event_Type.includes("Error")){
  //   EventForm.eventType_Is_Chosen.onError = false;
  // }

});

// 编辑操作
const onEdit = (row) =>{
  //要修改 dialogVisible 的值，应该修改 dialogVisible.value 而不是 dialogVisible，因为 dialogVisible 是一个 ref 对象，实际的值存储在 value 属性中。
  //这种情况下，dialogVisible 被重新赋值成一个布尔值 true，而不是修改原来的 ref 对象。
  dialogVisible.value = true;
  dia_title.value = row.name;
  selectedService.value = row;
  console.log(row);
  OperationForm.operation_Code = row.code
  OperationForm.operation_Name = row.name
  getEventData()
  getParams()
};
const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (eventFormRef) {
      const event = {
        name: EventForm.event_Name,
        type: EventForm.event_Type,
        description: EventForm.event_Description
      }
      console.log(event)
      addDeviceTypeEvent(<String>props.name, OperationForm.operation_Code, event).then((res)=>{
        if (res.status === 200){
          ElMessage.success('新增事件成功')
          state.eventsVisible = false;
          eventFormRef.value.resetFields()
          getEventData()
          getCommandData()
        }
      })
    } else {
      console.log('error submit!', fields);
    }

  })
}
const resetForm = () => {
  if(eventFormRef){
    eventFormRef.value.resetFields()
  }
}
const beforeOperationEditClose = ()=>{
  OperationForm.operation_Code = ""
  OperationForm.operation_Name = ""
  OperationForm.operation_InputParam = []
  OperationForm.operation_OutputParam = ""
  event_data.value = []
}

const editEventFile = (fileName)=>{
  console.log(fileName)
}

const getCommandData = ()=>{
  getOperationCommand(<String>props.name).then((res:any)=>{
    if (res.status === 200){
      state.data = res.data.map((v)=>{
        return {
          code: v.commandCode,
          name: v.commandName,
          events_count: v.events.length + "/3"
        }
      })
    }
  })
}

const getParams = () =>{
  getOperationParam(<String>props.name,selectedService.value.code).then((res:any) =>{
    if (res.status === 200){
      operation_InputParam.value = res.data.inputParams
      operation_OutputParam.value = res.data.outputParam
    }
  })
}

const getEventData =() =>{
  EventForm.eventType_Is_Chosen.onStart = true
  EventForm.eventType_Is_Chosen.onComplete = true
  EventForm.eventType_Is_Chosen.onError = true

  getOperationEvent(<String>props.name,selectedService.value.code).then((res:any) =>{
    if(res.status === 200){
      event_data.value = res.data
      res.data.forEach((v)=>{
        EventForm.eventType_Is_Chosen[v.type] = false
      })
    }
  })
}

// 新增操作
const CommitOperation =  async (formEl: FormInstance | undefined)=>{
  operationVisible.value = false;
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (operationFormRef) {
      console.log('submit! operation', OperationForm);
      emit('operation-info',OperationForm)

      const operation = {
        commandCode: OperationForm.operation_Code,
        commandName: OperationForm.operation_Name,
        inputParam: OperationForm.operation_InputParam,
        outputParam: OperationForm.operation_OutputParam
      }
      addDeviceTypeOperation(<String>props.name, operation).then((res)=>{
        if (res.status === 200){
          getCommandData()
        }
      })
    } else {
      console.log('error submit!', fields);
    }
  })
}

const Commit =()=>{
  console.log("commit");
}
</script>

<style scoped>

</style>
