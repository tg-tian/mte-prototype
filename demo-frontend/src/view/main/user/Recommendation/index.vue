<template>
  <div style="display: flex;flex-wrap: wrap;gap: 20px">
    <div v-for="(application, index) in applicationList" :key="index">
      <Card
          :cardItem="application"
          @itemClick="handleClick(application)"/>
    </div>
  </div>
</template>
<script setup lang="ts">
import Card from "../../common/Card.vue";

const router = useRouter()

interface State {
  applicationList: any[],
  dropDownItems: any[]
}

onActivated(()=>{
  applicationList.value = [
    {
      code: "SelfServeCoffee",
      name: "自助咖啡",
      imageUrl: new URL('@/assets/logo.png', import.meta.url).href,
      isSelected: false,
      route: '/demo/application/detail?applicationId=SelfServeCoffee&applicationName=自助咖啡&scenarioName=物理楼'
    }
  ]
})

const state = reactive<State>({
  applicationList: [],
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
const { applicationList } = toRefs(state)

// const updateIsSelected = (index: number, value: any) => {
//   applicationList.value[index].isSelected = value;
// };

// const handleCommand = (application: any, command: any)=>{
//   console.log('Clicked item:', application, command);
// }

const handleClick = (application: any)=>{
  console.log(application)
  const fullPath = router.resolve(application.route).href
  window.open(fullPath, '_blank')
}
</script>
