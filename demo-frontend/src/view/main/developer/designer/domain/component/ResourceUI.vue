<template>
  <div class="domain-subtitle" style="display: flex;justify-content: space-between">
    <el-button type="primary" style="margin-left: auto;" @click="dialogVisible=true">添加UI组件</el-button>
  </div>
  <div class = "domain-content">
    <Table :header="header" :data="data" :canChoose="true" />
  </div>

  <!--添加UI组件-->
  <el-dialog v-model = "dialogVisible" title="添加UI组件" width="50%" @close="clearUIComponent">
    <div style="display: flex;justify-content: space-between;font-size: 16px;margin-bottom: 20px;margin-top: 10px">
      <div>模板库</div>
      <div style="color: #50a5fb">
        <el-input
            v-model="searchInput"
            style="width: 240px"
            placeholder="搜索"
            :prefix-icon="Search"
        />
      </div>
    </div>
    <div style="display: flex;flex-wrap: wrap;gap: 20px;">
      <Card
          v-for="(ui, index) in domainUI"
          :key="ui.code"
          :cardItem="ui"
          canSelect
          @update:isSelected="updateIsSelected(index, $event)"/>
    </div>
    <div style="margin-top: 20px;display: flex;justify-content: end">
      <el-button type="primary">确定</el-button>
      <el-button @click="clearUIComponent">清空</el-button>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">

import Table from "@/view/main/common/Table.vue";
import Card from '@/view/main/common/Card.vue'
import {Search} from "@element-plus/icons-vue";
import {getDomainComponent} from "@/api/DomainApi";

const props = defineProps({
  domainId: String,
  domainName: String
})

//定义接口
interface State{
  header: any[];
  data: any[];
  dialogVisible: Boolean;
  searchInput: String;
  domainUI: any[];
  selectedUIList: any[]
}

//初始化
const state = reactive<State>({
  header:[
      {
    code: "code",
    name: "组件号",
    type: "Int"
  },{
    code: "name",
    name: "组件名称",
    type: "String"
  },{
    code: "imgUrl",
    name: "组件图标",
    type: "Image"
  }],
  data:[],
  dialogVisible: false,
  searchInput: '',
  domainUI: [],
  selectedUIList: []
})

const {header, data, dialogVisible, searchInput, domainUI, selectedUIList} = toRefs(state)

//赋值
onMounted(()=> {
  if (import.meta.env.VITE_MODE === "mock") {
    data.value = [
      {
        code: "001",
        name: "场景化导航",
        imgUrl: new URL('@/assets/images/guide.svg', import.meta.url).href
      },{
        code: "002",
        name: "柱状图",
        imgUrl: new URL('@/assets/images/bar.svg', import.meta.url).href
      }]
  }else {
    getDomainData()
  }

  domainUI.value = [{
    code: "001",
    name: "场景化导航",
    isSelected: false,
    imageUrl: new URL('@/assets/images/guide.svg', import.meta.url).href
  },
    {
      code: "002",
      name: "柱状图",
      isSelected: false,
      imageUrl: new URL('@/assets/images/bar.svg', import.meta.url).href
    }
  ]
})

const getDomainData = () =>{
  getDomainComponent("UI").then((res:any) =>{
    if (res.status === 200){
      console.log(res.data)
      data.value = res.data.componentAbout.map(v=>{
        const imgPath = "../../../../../../assets/icon/"+v.imgPath
        return {
          code: v.componentId,
          name: v.componentName,
          imgUrl: new URL(imgPath, import.meta.url).href,
          isSelected: false
        }
      })
    }
  })
}

const updateIsSelected = (index, value) => {
  domainUI.value[index].isSelected = value;
};

const clearUIComponent = ()=>{
  domainUI.value.forEach((device)=>{
    device.isSelected=false
  })
  selectedUIList.value = []
}

</script>

<style scoped>

</style>
