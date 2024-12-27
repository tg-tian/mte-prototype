<template>
  <div style="margin-top: 10px;margin-bottom: 20px">功能列表</div>
  <div v-for="(process, index) in processList" :key="index">
    <Card
        :cardItem="process"
        :dropDownItems="dropDownItems"
        @commandClick="handleCommand(process, $event)"
        @itemClick="handleClick(process)"/>
  </div>
</template>

<script setup lang="ts">
import Card from '../../../../common/Card.vue'
import getAssetsFile from '@/utils/pub-use'
const router = useRouter()

interface State {
  processList: any[],
  dropDownItems: any[]
}

onActivated(()=>{
  processList.value = [
    {
      code: "ConferenceService",
      name: "咖啡服务",
      imageUrl: getAssetsFile('images/process.png')
    }
  ]
})

const state = reactive<State>({
  processList: [],
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
const { processList, dropDownItems } = toRefs(state)

const handleCommand = (process, command)=>{
  console.log('Clicked item:', process, command);
}

const handleClick = (process)=>{
  console.log(process)
  const fullPath = router.resolve(`/design/edit/process?processId=${process.code}&processName=${process.name}`).href
  window.open(fullPath, '_blank')
}
</script>


