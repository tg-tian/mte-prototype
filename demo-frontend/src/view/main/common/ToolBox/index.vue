<template>
  <div>
    <div v-for="itemCategory in itemList" :key="itemCategory.type">
      <div style="display:flex;justify-content: space-between">
        <div class="sub-title">
          {{itemCategory.name}}
        </div>
        <el-button style="float: right;" type="primary" plain>新增</el-button>
      </div>
      <div style="display: flex;flex-wrap: wrap;gap: 2px">
        <div v-for="item in itemCategory.items" :key="item.id" class="tool-item">
          <div class="tool-icon">
            <el-image :src="requireIcon(item.icon ?? '')" style="width: 70%;margin: 15%;"></el-image>
          </div>
          <div class="tool-name">{{item.name}}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ToolboxCategory} from "@/view/main/common/ToolBox/toolboxTypes";
import {loadComponentData} from "@/api/toolBoxApi";
import {loadDomainBindingData} from "@/api/DomainApi";
import {getAssetsFile} from '@/utils/pub-use'

const props = defineProps({
  designerType: String,
  toolboxJson: Array<ToolboxCategory>
});

const itemList = ref<ToolboxCategory[]>(props.toolboxJson);

interface DeviceType {
  type:String,
  name:String,
  items:any[],
}

interface State{
  deviceType: DeviceType
}

const state = reactive<State>({
  deviceType:{
    type:'',
    name:'',
    items:[],
  }
})
const {deviceType} = toRefs(state)

onMounted(()=>{
  if(props.designerType === "Process")
  {
    deviceType.value.type = "domainDevice";
    deviceType.value.name = "领域设备类型控件";
    deviceType.value.items = []
    getDeviceType();
  }
})

const getDeviceType = ()=>{
  // loadComponentData("Device")
  loadDomainBindingData("Device", "SmartBuilding").then( (res:any)=> {
    if(res.status === 200){
      const deviceTypeList = res.data.map((v)=>{
        return{
          id:v.deviceTypeCode,
          type:"deviceType",
          name:v.deviceTypeName,
          category:"command",
          icon: 'device/'+v.deviceTypeCode+".png"
        }
      })
      // 使用赋值语法来添加属性
      deviceType.value.type = "domainDevice";
      deviceType.value.name = "领域设备类型控件";
      deviceType.value.items = deviceTypeList;
      itemList.value.push(deviceType.value as ToolboxCategory)
      console.log(itemList);
    }
  });
}

const requireIcon = (iconPath) => {
  if (iconPath === ""){
    return getAssetsFile('logo.png')
  }else {
    return getAssetsFile(iconPath)
  }
}
</script>

<style scoped>
.sub-title {
  color: grey;
  margin-top: 10px;
  font-size: 14px;
  margin-bottom: 5px;
}

.tool-item {
  text-align: center;
  padding: 10px;
}

.tool-icon {
  border: #d6d6d6 1px solid;
  width: 60px;
  height: 60px;
}

.tool-name {
  font-size: 14px;
  margin-top: 5px;
}
</style>
