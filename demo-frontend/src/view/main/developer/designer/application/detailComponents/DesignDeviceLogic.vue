<template>
  <PageHeader :title="applicationName+'——设备逻辑列表'" :button-group="buttonGroup" @button-click="handleHeaderButtonClick"/>
  <div class="body-box">
    <div class="title-box">共享设备逻辑</div>


    <div class="table-box">
      <div class="table-row">
        <div class="title-cell label">逻辑名称</div>
        <div class="table-cell">
          <el-input v-model="input" style="width: 240px" placeholder="制作" />
        </div>
        <div class="title-cell label">应用状态</div>
        <div class="table-cell">
          <el-select
              v-model="value"
              placeholder="请选择"
              style="width: 240px"
          >
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </div>
        <el-button type="primary" style="margin-left: 30px">筛选</el-button>
      </div>
      <div class="table">
        <Table :header="header_public" :data="data_public"/>
      </div>
    </div>


    <div class="title-box">私有设备逻辑</div>
    <div class="table-box">
      <div class="table-row">
        <div class="title-cell label">逻辑名称</div>
        <div class="table-cell">
          <el-input v-model="input" style="width: 240px" placeholder="制作" />
        </div>
        <div class="title-cell label">应用状态</div>
        <div class="table-cell">
          <el-select
              v-model="value"
              placeholder="请选择"
              style="width: 240px"
          >
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </div>
        <el-button type="primary" style="margin-left: 30px">筛选</el-button>
      </div>
      <div class="table">
        <Table :header="header_private" :data="data_private"/>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import PageHeader from "@/view/main/common/PageHeader.vue";
import {ref} from 'vue'
import Table from "@/view/main/common/Table.vue";

const value = ref('')
const router = useRouter()
interface State {
  applicationId: String,
  applicationName: String,
  header_public: any[];
  data_public:any[];
  header_private:any[];
  data_private: any[];

}

const  state = reactive<State>({
  applicationId:"",
  applicationName:"",
  header_public:[
    {
     code: "name",
     name:"逻辑名称",
     type:"String",
    },{
      code: "description",
      name:"功能描述",
      type:"String",
    },{
      code: "number",
      name:"共享应用数",
      type:"Number",
    },{
      code: "status",
      name:"逻辑状态",
      type:"Any",
    },
  ],
  header_private:[
    {
      code: "name",
      name:"逻辑名称",
      type:"String",
    },{
      code: "description",
      name:"功能描述",
      type:"String",
    },{
      code: "belong",
      name:"所属应用",
      type:"String",
    },{
      code: "status",
      name:"逻辑状态",
      type:"Any",
    },
  ],
  data_public:[
    {
      name:'咖啡制作',
      description:'咖啡制作流程用于完善制作咖啡的关联操作...',
      number:2,
    }
  ],
  data_private:[
    {
      name:'咖啡机自检',
      description:'用于检查咖啡机当前的状态',
      belong:'会议接待',
    }
  ],
})
const  {applicationId,applicationName,header_public,header_private,data_public,data_private} = toRefs(state)
const options = [
  {
    value: '11',
    label: '（已启用,已绑定）',
  },
  {
    value: '10',
    label: '（已启用,未绑定）',
  },
  {
    value: '00',
    label: '（未启用,未绑定）',
  },
  {
    value: '01',
    label: '（未启用,已绑定）',
  },
]
const buttonGroup = [
  {
    code: 'newDeviceLogic',
    name: '新增设备逻辑',
    type: 'primary'
  }
]

watchEffect(() => {
  if (typeof router.currentRoute.value.query.applicationId === 'string') {
    applicationId.value = router.currentRoute.value.query.applicationId || ''
  }
  if (typeof router.currentRoute.value.query.applicationName === 'string') {
    applicationName.value = router.currentRoute.value.query.applicationName || ''
  }
})
const handleHeaderButtonClick=()=>{
  console.log("create the device logic")
}
</script>

<style scoped>
.title-cell{
  margin-left: 10%;
  margin-top: 8px;
}
.table-cell {
  text-align: left;
}
.label {
  flex: 0 0 90px;
  font-weight: bold;
  color: #666;
}
.table{
  width: 80%;
  margin-top: 20px;
  margin-left: 8%;
}
</style>