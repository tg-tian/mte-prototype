<template>
  <div style="margin-top: 10px;margin-bottom: 20px">页面列表</div>
  <div v-for="(page, index) in pageList" :key="index">
    <Card
        :cardItem="page"
        :dropDownItems="dropDownItems"
        @commandClick="handleCommand(page, $event)"
        @itemClick="handleClick(page)"/>
  </div>
</template>

<script setup lang="ts">
import Card from '../../../../common/Card.vue'
const router = useRouter()

interface State {
  pageList: any[],
  dropDownItems: any[]
}

onActivated(()=>{
  pageList.value = [
    {
      code: "CoffeeServicePage",
      name: "咖啡自助服务页面",
      imageUrl: new URL('@/assets/images/page.png', import.meta.url).href
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
  ]
})
const { pageList, dropDownItems } = toRefs(state)

const handleCommand = (page, command)=>{
  console.log('Clicked item:', page, command);
}

const handleClick = (page)=>{
  console.log(page)
  const fullPath = router.resolve(`/design/edit/page?pageId=${page.code}&pageName=${page.name}`).href
  window.open(fullPath, '_blank')
}
</script>



