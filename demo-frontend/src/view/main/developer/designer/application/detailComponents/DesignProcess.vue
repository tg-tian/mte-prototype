<template>
  <PageHeader :title="applicationName+'——业务流程列表'" :button-group="buttonGroup" @button-click="handleHeaderButtonClick"/>
  <div class="body-box">
    <div v-for="(process, index) in processList" :key="index">
      <Card
          :cardItem="process"
          :dropDownItems="dropDownItems"
          @commandClick="handleCommand(process, $event)"
          @itemClick="handleClick(process)"/>
    </div>
  </div>
  <el-dialog
      v-model="dialogVisible"
      title="新增业务流程"
      width="500"
  >
    <el-radio-group v-model="radio">
      <el-radio value="head">从头创建</el-radio>
      <el-radio value="template">从模板创建</el-radio>
    </el-radio-group>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addProcess">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import Card from '../../../../common/Card.vue'
import {getAssetsFile} from '@/utils/pub-use'
import PageHeader from "@/view/main/common/PageHeader.vue";
const router = useRouter()
const buttonGroup = [
  {
    code: 'newBusinessProcess',
    name: '新增业务流程',
    type: 'primary'
  },
]

interface State {
  processList: any[],
  dropDownItems: any[],
  applicationId: String,
  applicationName: String,
  dialogVisible: boolean,
  radio: String
}

onActivated(()=>{
  processList.value = [
    {
      code: "CoffeeService",
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
  ],
  applicationId: '',
  applicationName: '',
  dialogVisible: false,
  radio: 'head'
})
const { processList, dropDownItems,applicationId, applicationName, dialogVisible, radio} = toRefs(state)

watchEffect(() => {
  if (typeof router.currentRoute.value.query.applicationId === 'string') {
    applicationId.value = router.currentRoute.value.query.applicationId || ''
  }
  if (typeof router.currentRoute.value.query.applicationName === 'string') {
    applicationName.value = router.currentRoute.value.query.applicationName || ''
  }
})

const handleHeaderButtonClick = (code: string)=>{
  if (code == "newBusinessProcess") {
    dialogVisible.value = true
  }
}

const handleCommand = (process, command)=>{
  console.log('Clicked item:', process, command);
}

const handleClick = (process)=>{
  console.log(process)
  const fullPath = router.resolve(`/design/edit/process?processId=${process.code}&processName=${process.name}`).href
  window.open(fullPath, '_blank')
}

const addProcess = ()=>{
  dialogVisible.value = false
  console.log(radio.value)
}
</script>


