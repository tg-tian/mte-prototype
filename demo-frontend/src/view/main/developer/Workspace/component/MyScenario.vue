<template>
    <div v-for="(domain, index) in scenarioList" :key="index">
      <div class="collapse-block" >
        <div class="collapse-title">
          {{domain.domainName + '(' + domain.children.length + ')'}}
        </div>
      </div>
      <div style="display: flex;flex-wrap: wrap;gap: 20px">
        <Card
            v-for="(scenario, index2) in domain.children"
            :key="index2"
            :cardItem="scenario"
            canSelect
            :dropDownItems="dropDownItems"
            @commandClick="handleCommand(scenario, $event)"
            @itemClick="handleClick(scenario)"
            @update:isSelected="updateIsSelected(index, $event)"/>
      </div>
    </div>
</template>
<script setup lang="ts">
import Card from "../../../common/Card.vue";

interface State {
  scenarioList: any[],
  dropDownItems: any[]
}

onActivated(()=>{
  scenarioList.value = [
    {
      domainCode: 'SmartBuilding',
      domainName: '智慧楼宇',
      collapse: false,
      children: [
        {
          code: "BuildingA",
          name: "交叉二号楼",
          imageUrl: new URL('@/assets/logo.png', import.meta.url).href,
          isSelected: false
        },
        {
          code: "BuildingB",
          name: "物理楼",
          imageUrl: new URL('@/assets/logo.png', import.meta.url).href,
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
    },
    {
      code: 'edit',
      name: '编辑'
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

const handleClick = (scenario)=>{
  console.log(scenario)
}
</script>
<style scoped>
.collapse-block {
  margin-top: 10px;
  margin-bottom: 5px;
  display: flex;
}

.collapse-title {
  font-size: 14px;
  color: gray;
  cursor: pointer;
}
</style>
