<template>
  <div style="display: flex;flex-wrap: wrap;gap: 20px">
    <div v-for="(domain, index) in domainList" :key="index">
      <Card
          :cardItem="domain"
          canSelect
          :dropDownItems="dropDownItems"
          @commandClick="handleCommand(domain, $event)"
          @itemClick="handleClick(domain)"
          @update:isSelected="updateIsSelected(index, $event)"/>
    </div>
  </div>
</template>
<script setup lang="ts">
import Card from "../../../common/Card.vue";

interface State {
  domainList: any[],
  dropDownItems: any[]
}

const router = useRouter() //引入路由

onActivated(()=>{
  domainList.value = [
    {
      code: "SmartBuilding",
      name: "智慧楼宇",
      imageUrl: new URL('@/assets/logo.png', import.meta.url).href,
      isSelected: false
    },
    {
      code: "SmartMine",
      name: "智慧矿山",
      imageUrl: new URL('@/assets/logo.png', import.meta.url).href,
      isSelected: false
    }
  ]
})

const state = reactive<State>({
  domainList: [],
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
const { domainList, dropDownItems } = toRefs(state)

const updateIsSelected = (index, value) => {
  domainList.value[index].isSelected = value;
};

const handleCommand = (domain, command)=>{
  console.log('Clicked item:', domain, command);
}

const handleClick = (domain)=>{
  console.log(domain)
  router.push({path: '/developer/domain/detail',query: {domainId: domain.code, domainName: domain.name }})
}
</script>
