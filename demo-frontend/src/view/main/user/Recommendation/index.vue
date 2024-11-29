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
      code: "GuestReception",
      name: "来访接待",
      imageUrl: new URL('@/assets/logo.png', import.meta.url).href,
      isSelected: false,
      route: '/demo/application/detail?applicationId=GuestReception&applicationName=来访接待&scenarioName=交叉二号楼'
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
