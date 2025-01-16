<template>
    <div v-for="(domain, index) in scenarioList" :key="index">
      <div class="collapse-block" >
        <div class="collapse-title">
          {{domain.domainName + '(' + domain.children.length + ')'}}
        </div>
      </div>
      <div style="display: flex;flex-wrap: wrap;gap: 20px;">
        <Card
            v-for="(scenario, index2) in domain.children"
            :key="index2"
            :cardItem="scenario"
            canSelect
            :dropDownItems="dropDownItems"
            @commandClick="handleCommand(scenario, $event)"
            @itemClick="handleClick(scenario, domain)"
            @update:isSelected="updateIsSelected(index, $event)"/>
      </div>
    </div>
</template>
<script setup lang="ts">
import Card from "../../../common/Card.vue";
import {getAssetsFile} from '@/utils/pub-use'

interface State {
  scenarioList: any[],
  dropDownItems: any[]
}
const router = useRouter()

onActivated(()=>{
  scenarioList.value = [
    {
      domainCode: 'SmartBuilding',
      domainName: '智慧楼宇',
      collapse: false,
      children: [
        {
          code: "CrossingBuilding",
          name: "交叉二号楼",
          imageUrl: getAssetsFile('logo.png'),
          isSelected: false
        },
        {
          code: "PhysicalBuilding",
          name: "物理楼",
          imageUrl: getAssetsFile('logo.png'),
          isSelected: false
        }
      ]
    },
    {
      domainCode: 'SmartMine',
      domainName: '智慧矿山',
      collapse: false,
      children: []
    }
  ]
})

const state = reactive<State>({
  scenarioList: [],
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
const { scenarioList, dropDownItems } = toRefs(state)

const updateIsSelected = (index, value) => {
  scenarioList.value[index].isSelected = value;
};

const handleCommand = (scenario, command)=>{
  console.log('Clicked item:', scenario, command);
}

const handleClick = (scenario: any, domain: any)=>{
  console.log(scenario)
  router.push({path: '/developer/scenario/detail', query: {scenarioId: scenario.code, scenarioName: scenario.name, domainId: domain.domainCode}})
}
</script>
<style scoped>
.collapse-block {
  margin-top: 20px;
  margin-bottom: 10px;
  display: flex;
}

.collapse-title {
  font-size: 14px;
  color: gray;
  cursor: pointer;
}
</style>
