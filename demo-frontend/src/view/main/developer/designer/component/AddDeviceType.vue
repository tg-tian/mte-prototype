<template>
  <div style="font-size: 16px">
    <el-button link @click="router.go(-1)" style="margin-right: 10px"><el-icon><ArrowLeft /></el-icon></el-button>
    <span>新增设备模板</span>
  </div>
  <el-steps style="margin: 30px" :active="active">
    <el-step title="Step 1" description="基本信息" :icon="Edit"/>
    <el-step title="Step 2" description="功能设置" :icon="Setting"/>
    <el-step title="Step 3" description="图标上传" :icon="Picture"/>
  </el-steps>
  <div style="margin-top: 20px;display: flex;justify-content: end">
    <el-button style="margin-left: 12px" @click="prev" v-if="active!==0">上一步</el-button>
    <el-button style="margin-left: 12px" @click="next" v-if="active!==2">下一步</el-button>
    <el-button type="primary" style="margin-left: 12px" v-if="active===2">完成</el-button>
  </div>
</template>
<script setup lang="ts">
import {ArrowLeft, Edit, Picture, Setting} from "@element-plus/icons-vue";

const router = useRouter()

interface State {
  active: number;
}

const state = reactive<State>({
  active: 0
})
const {active} = toRefs(state)

const prev = ()=>{
  active.value--
}

const next = ()=>{
  if (active.value++ > 2) active.value = 0
}
</script>
