<template>
  <PageHeader :title="applicationName+'——应用页面列表'" :button-group="buttonGroup" @button-click="handlePageClick"/>
  <div class="body-box">
    <div class="title-box">应用页面</div>
    <div class="table-box">
      <div class="table-row">
        <div class="title-cell label">页面名称</div>
        <div class="table-cell">
          <el-input  style="width: 240px" placeholder="请输入" />
        </div>
        <div class="title-cell label">所属页面流</div>
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
        <Table :header="header"  :data="data" @handle-edit="handleClick"/>
      </div>
    </div>
<!--    <div v-for="(page, index) in pageList" :key="index">-->
<!--      <Card-->
<!--          :cardItem="page"-->
<!--          :dropDownItems="dropDownItems"-->
<!--          @commandClick="handleCommand(page, $event)"-->
<!--          @itemClick="handleClick(page)"/>-->
<!--    </div>-->
  </div>
</template>

<script setup lang="ts">
import Card from '../../../../common/Card.vue'
import getAssetsFile from '@/utils/pub-use'
import PageHeader from "@/view/main/common/PageHeader.vue";
import Table from "@/view/main/common/Table.vue";
const router = useRouter()

const  buttonGroup =[
  {
    code: 'newPage',
    name: '新增页面',
    type: 'primary'
  },
]

interface State {
  pageList: any[],
  dropDownItems: any[],
  applicationId: String,
  applicationName: String,
  header:any[],
}

onActivated(()=>{
  pageList.value = [
    {
      code: "CoffeeServicePage",
      name: "咖啡自助服务页面",
      imageUrl: getAssetsFile('images/page.png')
    }
  ]
})

const state = reactive<State>({
  pageList: [],
  dropDownItems: [
    {
      code: 'rename',
      name: '重命名'
    },
    {
      code: 'delete',
      name: '删除'
    }
  ],
  applicationId:"",
  applicationName:"",
  header:[
    {
      code: "name",
      name:"页面名称",
      type:"String",
    },{
      code: "code",
      name:"页面编码",
      type:"String",
    },{
      code: "page_flow",
      name:"所在页面流",
      type:"String",
    }
  ],
  data:[
    {
      name:'咖啡点单页面',
      code:'CoffeeMakerPage',
      page_flow:'咖啡点单页面流',
    },{
      name:'会议室预定页面',
      code:'ConferenceBookingPage',
      page_flow:'会议室预定页面流',
    }
  ],
})
const { pageList, dropDownItems,applicationId,applicationName,header,data} = toRefs(state)

watchEffect(() => {
  if (typeof router.currentRoute.value.query.applicationId === 'string') {
    applicationId.value = router.currentRoute.value.query.applicationId || ''
  }
  if (typeof router.currentRoute.value.query.applicationName === 'string') {
    applicationName.value = router.currentRoute.value.query.applicationName || ''
  }
})
const handleCommand = (page, command)=>{
  console.log('Clicked item:', page, command);
}

const handleClick = (page)=>{
  console.log(page)
  const fullPath = router.resolve(`/design/edit/page?pageId=${page.code}&pageName=${page.name}`).href
  window.open(fullPath, '_blank')
}

const handlePageClick=()=>{
  console.log("create the page")
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

