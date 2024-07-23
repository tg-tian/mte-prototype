<template>
  <div style="display: flex;flex-wrap: wrap;gap: 20px">
    <div v-for="(application, index) in applicationList" :key="index">
      <Card
          :cardItem="application"
          canSelect
          :dropDownItems="dropDownItems"
          @commandClick="handleCommand(application, $event)"
          @itemClick="handleClick(application)"
          @update:isSelected="updateIsSelected(index, $event)"/>
    </div>
  </div>
</template>
<script setup lang="ts">
import Card from "../../../common/Card.vue";
import {useApplicationStore} from "@/store/modules/applicationStore";

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
      isSelected: false
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
    },
    {
      code: 'edit',
      name: '编辑'
    }
  ]
})
const { applicationList, dropDownItems } = toRefs(state)

const updateIsSelected = (index, value) => {
  applicationList.value[index].isSelected = value;
};

const handleCommand = (application, command)=>{
  console.log('Clicked item:', application, command);
}

const applicationStore = useApplicationStore()
const handleClick = (application)=>{
  console.log(application)
  applicationStore.loadApplication({
    applicationId: application.code,
    applicationName: application.name
  })
  const fullPath = router.resolve(`/design/index?applicationId=${application.code}&applicationName=${application.name}`).href
  window.open(fullPath, '_blank')
}
</script>
