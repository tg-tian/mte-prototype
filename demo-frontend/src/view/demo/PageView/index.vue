<template>
  <div>
    <div style="padding: 20px">
      <div>{{scenarioName}}-{{applicationName}}应用-{{processName}}功能</div>
      <div style="margin-top: 20px;">
        <el-breadcrumb :separator-icon="ArrowRight">
          <el-breadcrumb-item>咖啡自助服务</el-breadcrumb-item>
          <el-breadcrumb-item>点咖啡</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div v-for="configId in configIds" :key="configId" style="margin-top: 20px">
        <el-form :model="config[configId]" :rules="rules[configId]" :ref="el => formRefs[configId] = el">
          <el-form-item v-for="configItem in config[configId]" :label="configItem.name" :key="configItem.code" required :prop="configItem.code">
            <div style="width: 50%">
              <el-input v-model="configItem.value" v-if="configItem.type === 'String'"></el-input>
              <el-input v-model="configItem.value" v-if="configItem.type === 'Number'" type="number"></el-input>
              <el-select v-model="configItem.value" v-if="configItem.type === 'Enum'">
                <el-option v-for="option in configItem.optional" :key="option" :label="option" :value="option"></el-option>
              </el-select>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <div><el-button @click="fetchExecuteProcess" type="primary" style="margin-top: 20px;" v-loading="executing">下单</el-button></div>
    </div>
  </div>
</template>

<script setup lang="ts">

import {executeProcess, getActionConfig} from "../../../api/processApi";
import {ElMessage, FormRules} from "element-plus";
import { ArrowRight } from '@element-plus/icons-vue'

interface State {
  processName: String;
  processId: String;
  applicationName: String;
  scenarioName: String;

  executing: Boolean;

  configIds: String;
  config: any;
  saveConfig: any;
  rules: any
}

const state = reactive<State>({
  processName: '',
  processId: '',
  applicationName: '',
  scenarioName: '',

  executing: false,
  configIds: '',
  config: {},
  saveConfig: {},
  rules: {}
})
const { processId, processName, executing, configIds, config, saveConfig, applicationName, scenarioName, rules } = toRefs(state)

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

const formRefs = ref({});
const fetchConfig = () => {
  configIds.value = ["makeCoffee"]
  config.value = {
    makeCoffee: [{
      code: "coffeeType",
      name: "咖啡类型",
      type: "Enum",
      optional: ["摩卡", "美式"],
      value: ""
    }]
  }

  configIds.value.forEach(configId => {
    rules.value[configId] = {}
    config.value[configId].forEach(configItem =>{
      rules.value[configId][configItem.code] = [
        { required: true, message: `${configItem.name} is required`, trigger: 'blur' }
      ];
    })
  })
}

const fetchExecuteProcess = () => {
  nextTick(()=>{

    let allValid = true
        configIds.value.forEach(configId => {
          saveConfig.value[configId] = {}

          config.value[configId].forEach(configItem =>{
            saveConfig.value[configId][configItem.code] = configItem.value

            if (configItem.value === "" || configItem.value === undefined){
              allValid = false
            }
          })
        })

    if (allValid) {
        executing.value = true
        executeProcess(processId.value, saveConfig.value).then((res: any) => {
          if (res.status === 200) {

          }
          executing.value = false
        })
      } else {
        ElMessage.warning("请进行相关配置")
      }
    });
};

</script>
