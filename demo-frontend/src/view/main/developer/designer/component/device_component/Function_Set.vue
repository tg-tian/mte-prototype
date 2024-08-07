<template>
  <div>

    <div class="domain-content">
      <div id="功能列表" style="margin-bottom: 20px;">功能列表</div>
      <Table :header="header" :data="data" :canChoose="false" @handleEdit="onEdit"/>
    </div>
  </div>

  <el-dialog v-model="dialogVisible" width="50%">
    <!--通过slot插槽来实现title的动态改变-->
    <span slot="title" style="font-size: large;">操作名称：{{dia_title}}</span>
    <div style="padding:  20px;">

      <div style="display: flex;justify-content: space-between;">
        <div id="公共事件" style="margin-top: 10px;">公共事件</div>
        <el-button  type="primary" style="margin-left: auto; margin-bottom:20px;" plain>新增事件</el-button>
      </div>
      <Table :header="event_header" :data="event_data"/>


      <div style="display: flex;justify-content: space-between;margin-top: 20px;">
        <div id="支持服务" style="margin-top: 10px;">支持服务</div>
        <el-button  type="primary" style="margin-left: auto; margin-bottom:20px;" plain>新增服务</el-button>
      </div>
      <Table :header="service_header" :data="service_data"/>
    </div>
      <div class="domain-subtitle" style="display: flex;justify-content: space-between">
        <el-button type="primary"  style="margin-left: auto;" @click="Commit">确定</el-button>
        <el-button type="primary"  style="margin-right: auto;" @click="dialogVisible = false" plain>返回</el-button>
      </div>
  </el-dialog>
</template>

<script setup lang="ts">
import Table from "@/view/main/common/Table.vue";
import {getDomainJson} from "../../../../../../api/DomainApi";

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
  dia_title:String;
}

//定义响应式对象
const  state = reactive<State>({

  service_header:[
    {
      code:"factory_id",
      name:"厂商号",
      type:"String"
    },{
      code:"factory_name",
      name:"厂商名称",
      type:"String"
    },{
      code:"factory_file",
      name:"服务定义文件",
      type:"String"
    },
  ],
  service_data:[
    {
      factory_id:"fac_A001",
      factory_name:"A公司",
      factory_file:"Service_fac_A.json"
    }
  ],
  event_header:[
    {
      code:"event_id",
      name:"事件号",
      type:"String"
    },{
      code:"event_name",
      name:"事件名称",
      type:"String"
    },{
      code:"event_description",
      name:"事件描述",
      type:"String"
    },{
      code:"event_file",
      name:"事件文件",
      type:"String"
    }
  ],
  event_data:[
    {
      event_id:"001",
      event_name:"onMakeCoffeeStart",
      event_description:"prepare",
      event_file:"prepare.json"
    }
  ],

  header:[
    {
      code:"id",
      name:"操作号",
      type:"String"
    },{
      code:"name",
      name:"操作名称",
      type:"String"
    },{
      code:"events_count",
      name:"已绑定事件",
      type:"Int"
    },{
      code:"services_count",
      name:"支持服务",
      type:"Int"
    }
  ],
  data:[
    {
      id:"00011",
      name:"MakeCoffee",
      events_count:3,
      services_count:2
    }
  ],
  dialogVisible:false,
  dia_title:"编辑操作",
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
const  {service_header,service_data,event_header,event_data,header ,data,dialogVisible,dia_title} = toRefs(state)


const onEdit = (row) =>{
  //要修改 dialogVisible 的值，应该修改 dialogVisible.value 而不是 dialogVisible，因为 dialogVisible 是一个 ref 对象，实际的值存储在 value 属性中。
  //这种情况下，dialogVisible 被重新赋值成一个布尔值 true，而不是修改原来的 ref 对象。
  dialogVisible.value = true;
  dia_title.value = row.name;
  console.log(row.name);
};

</script>

<style scoped>

</style>