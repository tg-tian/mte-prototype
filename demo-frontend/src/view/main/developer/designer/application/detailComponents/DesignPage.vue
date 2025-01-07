<template>
  <PageHeader :title="applicationName+'——应用页面列表'" :button-group="buttonGroup" @button-click="handlePageClick"/>
  <div class="body-box">
    <div v-for="(page, index) in pageList" :key="index">
      <Card
          :cardItem="page"
          :dropDownItems="dropDownItems"
          @commandClick="handleCommand(page, $event)"
          @itemClick="handleClick(page)"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import Card from '../../../../common/Card.vue'
import getAssetsFile from '@/utils/pub-use'
import PageHeader from "@/view/main/common/PageHeader.vue";
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
})
const { pageList, dropDownItems,applicationId,applicationName} = toRefs(state)

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



