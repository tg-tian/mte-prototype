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
import { useDomainStore } from "@/store/modules/domainStore";
import { useScenarioStore } from "@/store/modules/scenarioStore";
import {getAssetsFile} from '@/utils/pub-use'

const router = useRouter()

interface State {
  applicationList: any[],
  dropDownItems: any[]
}

onActivated(()=>{
  applicationList.value = [
    {
      code: "PumpSystem",
      name: "智慧水泵系统",
      imageUrl: getAssetsFile('logo.png'),
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
    },
    {
      code: 'scan',
      name: '扫描'
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
const scenarioStore = useScenarioStore()
const domainStore = useDomainStore()
const handleClick = (application)=>{
  console.log(application)
  applicationStore.loadApplication({
    applicationId: application.code,
    applicationName: application.name
  })
  scenarioStore.loadScenario({
    scenarioId: 'PhysicalBuilding',
    scenarioName: '物理楼'
  })
  domainStore.loadDomain({
    domainId: 'SmartBuilding',
    domainName: '智慧楼宇'
  })
  const fullPath = router.resolve(`/design/index?applicationId=${application.code}&applicationName=${application.name}`).href
  window.open(fullPath, '_blank')
}
</script>
