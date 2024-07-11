<template>
  <div style="display: flex;flex-wrap: wrap;gap: 20px">
    <div v-for="(domain, index) in domainList" :key="index">
      <Card :imageUrl="domain.image" :name="domain.name" :isSelected="domain.isSelected" @update:isSelected="updateIsSelected(index, $event)"/>
    </div>
  </div>
</template>
<script setup lang="ts">
import Card from "./component/Card.vue";

interface State {
  domainList: any[]
}

onActivated(()=>{
  domainList.value = [
    {
      code: "SmartBuilding",
      name: "智慧楼宇",
      image: new URL('@/assets/logo.png', import.meta.url).href,
      isSelected: false
    },
    {
      code: "SmartMine",
      name: "智慧矿山",
      image: new URL('@/assets/logo.png', import.meta.url).href,
      isSelected: false
    }
  ]
})

const state = reactive<State>({
  domainList: []
})
const { domainList } = toRefs(state)

const updateIsSelected = (index, value) => {
  domainList.value[index].isSelected = value;
};
</script>
