<template>
  <PageHeader :title="applicationName+'——页面流列表'" :button-group="buttonGroup" @button-click="handlePageFlowClick"/>
  <div class="body-box">
    <div class="title-box">页面流</div>
    <div class="table-box">
      <div class="table-row">
        <div class="title-cell label">页面流名称</div>
        <div class="table-cell">
          <el-input  style="width: 240px" placeholder="请输入" />
        </div>
        <div class="title-cell label">页面流状态</div>
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
        <Table :header="header" :button-group="tableButtonGroup" :data="data" :canEdit="false"/>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import PageHeader from "@/view/main/common/PageHeader.vue";
import Table from "@/view/main/common/Table.vue";
import {ref} from "vue";
const router = useRouter()
const buttonGroup = [
  {
    code: 'newPageFlow',
    name: '新增页面流',
    type: 'primary'
  },
]
interface State {
  applicationId: String,
  applicationName: String,
  header:any[],
  data:any[],
}

const value = ref('')
const options = [
  {
    value: '0',
    label: '待发布',
  },
  {
    value: '1',
    label: '已发布',
  },
]

const tableButtonGroup=[
    {
      code:'setting',
      name:'配置',
      type:'info',
      size:'small',
    },{
      code:'publish',
      name:'发布',
      type:'success',
      size:'small',
  }
]

const  state = reactive<State>({
  applicationId:"",
  applicationName:"",
  header:[
    {
      code: "name",
      name:"页面流名称",
      type:"String",
    },{
      code: "code",
      name:"页面流编码",
      type:"String",
    },{
      code: "status",
      name:"状态",
      type:"String",
    }
  ],
  data:[
    {
      name:'咖啡点单页面流',
      code:'CoffeeMakerPageflow',
      status:'待发布',
    },{
      name:'会议室预定页面流',
      code:'ConferenceBookingPageflow',
      status:'待发布',
    }
  ],
})

const  {applicationId,applicationName,header,data} = toRefs(state)

watchEffect(() => {
  if (typeof router.currentRoute.value.query.applicationId === 'string') {
    applicationId.value = router.currentRoute.value.query.applicationId || ''
  }
  if (typeof router.currentRoute.value.query.applicationName === 'string') {
    applicationName.value = router.currentRoute.value.query.applicationName || ''
  }
})
const handlePageFlowClick=()=>{
  console.log("create the pageFlow");
}
</script>

<style scoped>

</style>