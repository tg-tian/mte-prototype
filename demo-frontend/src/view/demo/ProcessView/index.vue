<template>
  <div>
  <div style="padding: 20px">
    <div>{{scenarioName}}-{{applicationName}}应用-{{processName}}功能</div>
      <div style="display: flex; flex-direction: row; align-items: center; margin-top: 20px;">
        <div style="border: 1px solid black; padding: 10px; margin-right: 10px;margin-left: 10px">开始</div>
        <div>---></div>
        <div style="border: 1px solid black; padding: 10px; margin-right: 10px;margin-left: 10px; cursor: pointer" @click="openConfig('makeCoffee', '做咖啡')">做咖啡</div>
        <div>---></div>
        <div style="border: 1px solid black; padding: 10px;margin-left: 10px">检查</div>
      </div>
    <div><el-button @click="fetchExecuteProcess" type="primary" style="margin-top: 20px;" v-loading="executing">执行</el-button></div>
  </div>

  <el-dialog :title="configTitle" v-model="configVisible">
    <el-form :model="config[configId]">
      <el-form-item v-for="configItem in config[configId]" :label="configItem.name" :key="configItem.code">
        <div style="width: 50%">
          <el-input v-model="configItem.value" v-if="configItem.type === 'String'"></el-input>
          <el-input v-model="configItem.value" v-if="configItem.type === 'Number'" type="number"></el-input>
          <el-select v-model="configItem.value" v-if="configItem.type === 'Enum'">
            <el-option v-for="option in configItem.option" :key="option" :label="option" :value="option"></el-option>
          </el-select>
        </div>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="configVisible = false">取消</el-button>
      <el-button type="primary" @click="handleSaveConfig">保存</el-button>
    </div>
  </el-dialog>
  </div>
</template>

<script setup lang="ts">

import {executeProcess, getActionConfig} from "../../../api/processApi";
import {ElMessage} from "element-plus";

interface State {
  processName: String;
  processId: String;
  applicationName: String;
  scenarioName: String;

  executing: Boolean;

  configId: String;
  configTitle: String;
  configVisible: Boolean;
  config: any;
  saveConfig: any
}

const state = reactive<State>({
  processName: '',
  processId: '',
  applicationName: '',
  scenarioName: '',

  executing: false,
  configId: '',
  configTitle: '',
  configVisible: false,
  config: {},
  saveConfig: {}
})
const { processId, processName, executing, configId, configTitle, configVisible, config, saveConfig, applicationName, scenarioName } = toRefs(state)

const router = useRouter()

onActivated(() => {
  if (typeof router.currentRoute.value.query.processId === 'string') {
    processId.value = router.currentRoute.value.query.processId || ''
  }
  if (typeof router.currentRoute.value.query.processName === 'string') {
    processName.value = router.currentRoute.value.query.processName || ''
  }
  if (typeof router.currentRoute.value.query.applicationName === 'string') {
    applicationName.value = router.currentRoute.value.query.applicationName || ''
  }
  if (typeof router.currentRoute.value.query.scenarioName === 'string') {
    scenarioName.value = router.currentRoute.value.query.scenarioName || ''
  }
})

onMounted(() => {
  fetchConfig()
})

const fetchConfig = () => {
  config.value = {
    makeCoffee: [{
      code: "coffeeType",
      name: "咖啡类型",
      type: "Enum",
      optional: ["摩卡", "美式"],
      value: ""
    }]
  }
}

const openConfig = async (actionId, actionName) => {
  configId.value = actionId
  configTitle.value = actionName;
  configVisible.value = true;
};

const handleSaveConfig = () => {
  configVisible.value = false

  saveConfig.value[configId.value] = {}

  config.value[configId.value].forEach(configItem =>{
    saveConfig.value[configId.value][configItem.code] = configItem.value
  })

  // {
  //   makeCoffee: {
  //     coffeeType: "摩卡"
  //   }
  // }
};

const fetchExecuteProcess = () => {
  let configComplete = Object.keys(config.value).every(key => Object.keys(saveConfig.value).includes(key))
  if (configComplete){
    executing.value = true
    executeProcess(processId.value, saveConfig.value).then((res: any) => {
      if (res.status === 200){

      }
      executing.value = false
    })
  }else {
    ElMessage.warning("请进行相关配置")
  }
};

</script>
