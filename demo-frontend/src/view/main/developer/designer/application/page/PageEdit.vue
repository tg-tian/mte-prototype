<template>
  <div style="display: flex;flex-direction: column;height: 100%">
    <div class="page-header">
      <div>{{pageName}}-页面编辑</div>
      <div>
        <el-button type="primary">保存</el-button>
        <el-button type="primary" @click="goToPage">试运行</el-button>
      </div>
    </div>

    <!-- 绑定process，加载需要的参数 -->
    <div class="page-container">
      <div style="border: 1px solid lightgray; padding: 20px;width: 25%;overflow-y: auto">
        <div style="margin-bottom: 10px">工具栏</div>
        <el-input
            placeholder="搜索"
            :prefix-icon="Search"
        />
        <ToolBox :toolboxJson="pageTool"/>
      </div>

      <div style="border: 1px solid lightgray; padding: 20px;margin-left:20px;width: 50%;overflow-y: auto">
        编辑区
        <div style="margin-top: 20px;">
          <el-breadcrumb :separator-icon="ArrowRight">
            <el-breadcrumb-item>咖啡自助服务</el-breadcrumb-item>
            <el-breadcrumb-item>点咖啡</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <el-form :model="config" :rules="rules" ref="formRefs" style="margin-top: 10px">
          <el-form-item v-for="configItem in config" :label="configItem.name" :key="configItem.code" required :prop="configItem.code">
            <div style="width: 50%">
              <el-input v-model="configItem.value" v-if="configItem.type === 'String'" :id="configItem.code" @click="selectItem=selectItem !== configItem.code ? selectItem = configItem.code : selectItem = ''"></el-input>
              <el-input v-model="configItem.value" v-if="configItem.type === 'Number'" type="number"></el-input>
              <el-select v-model="configItem.value" v-if="configItem.type === 'Enum'" :id="configItem.code" @click="selectItem=selectItem !== configItem.code ? selectItem = configItem.code : selectItem = ''">
                <el-option v-for="option in configItem.optional" :key="option" :label="option" :value="option"></el-option>
              </el-select>
            </div>
          </el-form-item>
        </el-form>

        <div>
          <el-button type="primary" style="margin-top: 10px;" @click="selectItem !== 'button-52152' ? selectItem = 'button-52152' : selectItem = ''" id="button-52152">
            下单
          </el-button></div>
      </div>

      <div style="border: 1px solid lightgray; padding: 20px;margin-left: 20px;width: 20%;overflow-y: auto">
        属性配置区
        <el-form :model="commonConfig" ref="configFormRefs" style="margin-top: 10px" v-if="selectItem!==''">
          <el-form-item label="id">
            <el-input v-model="commonConfig.id" disabled/>
          </el-form-item>
        </el-form>
        <el-form :model="buttonConfig" ref="configFormRefs" style="margin-top: 10px" v-if="selectItem==='button-52152'">
          <el-form-item label="功能">
            <el-select v-model="buttonConfig.processId" @change="handleProcessChange" clearable>
              <el-option v-for="option in [{label: '咖啡服务', value: 'ConferenceService'}]" :key="option.value" :label="option.label" :value="option.value"></el-option>
            </el-select>
          </el-form-item>
          <div v-for="action in buttonConfig.params">
            <div style="font-size: 15px;color: gray;margin-bottom: 10px">{{action.actionName}}</div>
            <el-form-item
                v-for="configItem in action.executeArgs"
                :label="configItem.name"
                :key="configItem.code"
                :prop="configItem.code"
                style="margin-left: 30px"
            >
              <div style="width: 50%">
                <el-input v-model="configItem.value"></el-input>
              </div>
            </el-form-item>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ArrowRight } from '@element-plus/icons-vue'
import ToolBox from '@/view/main/common/ToolBox/index.vue'
import pageTool from './pageTool.json'
import {Search} from "@element-plus/icons-vue";
const router = useRouter()

const goToPage = ()=>{
  const fullPath = router.resolve(`/demo/page?processId=${buttonConfig.value.processId}`).href
  window.open(fullPath, '_blank')
}

const formRefs = ref({});
interface State {
  pageId: String;
  pageName: String;
  config: any;
  rules: any;
  selectItem: String;
  commonConfig: any
  buttonConfig: any;
}
const state = reactive<State>({
  pageId: '',
  pageName: '',
  config: {},
  rules: {},
  commonConfig: {},
  buttonConfig: {},
  selectItem: ''
})
const {pageId, pageName, config, rules, buttonConfig, selectItem, commonConfig} = toRefs(state)

onMounted(() => {
  fetchConfig()
})
const fetchConfig = () => {
  config.value = [{
      code: "coffeeType",
      name: "咖啡类型",
      type: "Enum",
      optional: ["摩卡", "美式"],
      value: ""
    }]

  rules.value = {}
  config.value.forEach(configItem =>{
    rules.value[configItem.code] = [
      { required: true, message: `${configItem.name} is required`, trigger: 'blur' }
    ];
  })

  commonConfig.value = {
    id: ''
  }

  buttonConfig.value = {
    processId: '',
    params: []
  }
}

watchEffect(() => {
  if (typeof router.currentRoute.value.query.pageId === 'string') {
    pageId.value = router.currentRoute.value.query.pageId || ''
  }
  if (typeof router.currentRoute.value.query.pageName === 'string') {
    pageName.value = router.currentRoute.value.query.pageName || ''
  }
})

watchEffect(() => {
  commonConfig.value.id = selectItem.value
})

const handleProcessChange = (processId)=>{
  if (processId === 'ConferenceService') {
    buttonConfig.value.params = [
      {
        "actionId": "makeCoffee",
        "actionName": "制作咖啡",
        "executeArgs": [
          {
            code: 'coffeeType',
            name: '咖啡类型',
            value: '${coffeeType}'
          }
        ]
      }
    ]
  }else {
    buttonConfig.value.params = []
  }
}

</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  border-bottom: lightgray 1px solid;
  padding: 10px;
  margin: 20px 20px 10px;
}

.page-container {
  display: flex;
  flex: 1;
  overflow: hidden;
  margin: 20px;
}
</style>
