<template>
  <div class="domain-subtitle" style="display: flex;justify-content: space-between">
    <el-button type="primary"  @click="openDialog" style="margin-left: auto;">添加设备模型</el-button>
  </div>
  <div class = "domain-content">
    <Table :header="header" :data="data" :can-edit="false" />
  </div>

  <!--添加设备表单-->
  <el-dialog v-model = "dialogVisible" title="添加设备模型" width="50%" @close="clearDevice">
    TODO：选择iot平台-获取设备模型列表-选择要添加的设备模型
    <div style="margin-top: 20px;display: flex;justify-content: end">
      <el-button type="primary" @click="SubmitDevice">确定</el-button>
      <el-button @click="clearDevice">清空</el-button>
    </div>
  </el-dialog>
</template>

<script setup lang = "ts">

import Table from "@/view/main/common/Table.vue";
import {Search} from "@element-plus/icons-vue";
import Card from '@/view/main/common/Card.vue'
import {loadDomainBindingData,loadDoaminComponentData,uploadDomainBindingData} from "@/api/DomainApi";
import {ElMessage} from "element-plus";
import {getAssetsFile, getDeviceImage} from '@/utils/pub-use'

const props = defineProps({
  domainId: String,
  domainName: String
})

//定义接口
interface State{
  header: any[];
  data: any[];
  dialogVisible: boolean;
  searchInput: String;
  domainDevice: any[];
  selectedDeviceList: any[]
}

//初始化
const state = reactive<State>({
  header:[
    {
      code: "ID",
      name: "设备模型编码",
      type: "String"
    },{
      code: "name",
      name: "设备模型名称",
      type: "String"
    },{
      code: "platform",
      name: "所属平台",
      type: "String"
    }],
  data:[],
  dialogVisible:false,
  searchInput: '',
  domainDevice: [],
  selectedDeviceList: []
})

//获取元素结点
const {header, data, dialogVisible, searchInput, domainDevice, selectedDeviceList} = toRefs(state)


//赋值
onMounted(()=> {
    getDomainData()
})

const openDialog = () => {
  dialogVisible.value = true;
  getDomainData();
}

const updateIsSelected = (index, value) => {
  domainDevice.value[index].isSelected = value;
};

const SubmitDevice = () => {
  const selectedDevices = domainDevice.value.filter(device => device.isSelected);
  if(selectedDevices.length === 0){
    ElMessage.warning("请先选择设备！");
    return;
  }
  //提取被选择的设备code
  const deviceCodes = selectedDevices.map(device => device.code);
  console.log(selectedDevices);
  uploadDomainBindingData({selectedCodes: deviceCodes},"Device",props.domainId).then(async (res: any) => {
    if (res.status === 200) {
      console.log('Binding successful', res.data);
      ElMessage.success('组件绑定成功')
      await getDomainData();
    }
  });
  dialogVisible.value = false;
}

const clearDevice = ()=>{
  domainDevice.value.forEach((device)=>{
    device.isSelected=false
  })
  selectedDeviceList.value = []
}

const getDomainData = () =>{
  loadDomainBindingData("Device",props.domainId).then((res:any) =>{
    if (res.status === 200){
      data.value = res.data.map( v=>{
        return {
          ID: v.deviceTypeCode,
          name: v.deviceTypeName
        }
      })
    }
  })
  domainDevice.value=[];
  loadDoaminComponentData("Device").then((res:any) =>{
    if(res.status === 200){
      const dataArray = res.data;
      dataArray.forEach((device : any) => {
        // 检查 deviceTypeCode 是否不在 data.ID 数组中
        const isDeviceInData = data.value.some((item: any) => item.ID === device.deviceTypeCode);
        if(!isDeviceInData)
        {
          const newDevice = {
            code: device.deviceTypeCode,
            name: device.deviceTypeName,
            isSelected: false,
            imageUrl: getDeviceImage(device.imgPath),
          }
          domainDevice.value.push(newDevice);  // 将对象加入到 domainDevice 中
        };
      })
    }
  })

}

</script>

<style scoped>

</style>
