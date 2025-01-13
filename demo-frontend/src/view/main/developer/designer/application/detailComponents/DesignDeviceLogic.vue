<template>
  <PageHeader :title="applicationName+'——设备列表'" :button-group="buttonGroup" @button-click="handleHeaderButtonClick"/>
  <div class="body-box">
    <div class="title-box">场景设备</div>
    <div class="table-box">
      <div class="table-row">
        <div class="title-cell label">设备名称</div>
        <div class="table-cell">
          <el-input  style="width: 240px" placeholder="制作" />
        </div>
        <div class="title-cell label">设备状态</div>
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
        <Table :header="header_public" :button-group="tableButtonGroup" :data="data_public"/>
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
}

const  state = reactive<State>({
  applicationId:"",
  applicationName:"",
  header_public:[
    {
     code: "name",
     name:"设备名称",
     type:"String",
    },{
      code: "description",
      name:"位置信息",
      type:"String",
    },{
      code: "status",
      name:"逻辑状态",
      type:"String",
    },
  ],
  data_public:[
    {
      name:'咖啡机1',
      description:'物理楼A2003...',
      status: "已启用",
    },{
      name:'咖啡机2',
      description:'物理楼1楼水吧',
      status: "已启用",
    }
  ],
})
const  {applicationId,applicationName,header_public,data_public} = toRefs(state)
const options = [
  {
    value: '1',
    label: '已启用',
  },
  {
    value: '0',
    label: '未启用',
  },
]
const buttonGroup = [
  {
    code: 'newDevice',
    name: '+新增设备',
    type: 'primary'
  }
]
const tableButtonGroup =[
  {
    code:'unavailable',
    name:'禁用',
    type:'warning',
    size:'small',
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