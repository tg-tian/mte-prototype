<template>
  <div>开发者设置</div>
  <el-form :model="form" label-width="auto" style="max-width: 50%;margin: 20px">
    <el-form-item label="maven目录">
      <el-input v-model="form.mavenPath" :disabled="!editable"/>
    </el-form-item>
    <el-form-item label="工作区目录">
      <el-input v-model="form.workspacePath" :disabled="!editable" @change="(v)=>{form.definitionPath=v+'deviceType/'}"/>
    </el-form-item>
    <el-form-item label="设备打包临时目录">
      <el-input v-model="form.projectPath" :disabled="!editable"/>
    </el-form-item>
    <el-form-item label="设备定义工作目录">
      <el-input v-model="form.definitionPath" disabled/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" v-if="!editable" @click="editable = true">修改</el-button>
      <el-button v-if="editable" @click="cancelEdit">取消</el-button>
      <el-button type="primary" v-if="editable" @click="saveEdit">保存</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import {editDeveloperConfig, getDeveloperConfig} from "@/api/configApi";
import {ElMessage} from "element-plus";

interface Config {
  mavenPath: string;
  workspacePath: string;
  projectPath: string;
  definitionPath: string;
}

interface State {
  form: Config,
  editable: boolean;
  rawData: Config
}

const state = reactive<State>({
  form: {
    mavenPath: '',
    workspacePath: '',
    projectPath: '',
    definitionPath: ''
  },
  editable: false,
  rawData: {}
})
const {form, editable, rawData} = toRefs(state)

onMounted(()=>{
  getConfig()
})

const getConfig = ()=>{
  getDeveloperConfig().then((res:any)=>{
    if (res.status == 200){
      form.value = {...res.data}
      rawData.value = {...res.data}
    }
  })
}

const cancelEdit = ()=>{
  form.value = {...rawData.value}
  editable.value=false
}

const saveEdit=()=>{
  editDeveloperConfig(form.value).then((res: any)=>{
    if (res.status == 200){
      ElMessage.success(res.data)
      rawData.value = {...form.value}
      editable.value=false
    }
  })
}
</script>

<style scoped>

</style>
