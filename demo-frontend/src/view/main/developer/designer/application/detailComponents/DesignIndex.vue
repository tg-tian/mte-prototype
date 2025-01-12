<template>
    <PageHeader :title="applicationName+'——应用信息'" :button-group="buttonGroup" @button-click="handleHeaderButtonClick"/>
    <div class="body-box">
      <div class="title-box">
        应用信息
      </div>
      <el-form label-width="auto" :form="applicationForm" class="application-form">
        <el-form-item label="应用名称" class="application-form-item">
          <el-input v-model="applicationForm.applicationName" :disabled="!editStatus"/>
        </el-form-item>
        <el-form-item label="应用介绍" class="application-form-item">
          <el-input type="textarea" v-model="applicationForm.description" :disabled="!editStatus"/>
        </el-form-item>
        <el-form-item label="应用路径" class="application-form-item">
          <el-input v-model="applicationForm.applicationPath" :disabled="!editStatus"/>
        </el-form-item>
        <el-form-item label="所属场景" class="application-form-item">
          <el-select v-model="applicationForm.scenario" placeholder="请选择" disabled></el-select>
        </el-form-item>
        <el-form-item label="所属领域" class="application-form-item">
          <el-select v-model="applicationForm.domain" placeholder="请选择" disabled></el-select>
        </el-form-item>
        <el-form-item label="发布情况" class="application-form-item">
          <el-tag type="success" size="large" v-if="applicationForm.isPublish">已发布</el-tag>
          <el-tag type="warning" size="large" v-if="!applicationForm.isPublish">编辑中</el-tag>
        </el-form-item>
        <el-form-item class="application-form-item">
          <el-button type="primary" v-if="!editStatus" @click="editStatus = true">编辑</el-button>
          <el-button type="primary" v-if="editStatus" @click="editStatus = false">保存</el-button>
          <el-button type="primary" v-if="editStatus" @click="editStatus = false">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
</template>
<script setup lang="ts">
import PageHeader from "@/view/main/common/PageHeader.vue";
import {useApplicationStore} from "@/store/modules/applicationStore";

interface ApplicationForm {
  applicationName: String;
  description: String;
  applicationPath: String;
  scenario: String;
  domain: String;
  isPublish: boolean;
}

interface State{
  applicationId: String;
  applicationName: String;
  applicationForm: ApplicationForm;
  editStatus: boolean;
}
const state = reactive<State>({
  applicationId: '',
  applicationName: '',
  applicationForm: {
    applicationName: '',
    description: '',
    applicationPath: '',
    scenario: '物理楼',
    domain: '智慧楼宇',
    isPublish: false
  },
  editStatus: false
})
const {applicationId, applicationName, applicationForm, editStatus} = toRefs(state)

const router = useRouter()
const applicationStore = useApplicationStore()
watchEffect(() => {
  applicationId.value = applicationStore.getApplicationId
  applicationName.value = applicationStore.getApplicationName
  applicationForm.value.applicationName = applicationName.value
})

const buttonGroup = computed(()=>{
  return applicationForm.value.isPublish ? [
    {
      code: 'preview',
      name: '预览',
      type: 'primary'
    },
    {
      code: 'revert',
      name: '撤回',
      type: 'warning'
    },
    {
      code: 'code-view',
      name: '质量分析',
      type: 'warning'
    },
    {
      code: 'code-server',
      name: '代码编辑',
      type: 'primary'
    },
  ] : [
    {
      code: 'preview',
      name: '预览',
      type: 'primary'
    },
    {
      code: 'publish',
      name: '发布',
      type: 'success'
    },
    {
      code: 'code-view',
      name: '质量分析',
      type: 'warning'
    },
    {
      code: 'code-server',
      name: '代码编辑',
      type: 'primary'
    },
  ]
})

const goToCodeServer = ()=>{
  window.open("http://139.196.147.52:5200/?folder=/home/coder", '_blank');
}

const handleHeaderButtonClick = (code: string)=>{
  if (code === 'code-server'){
    goToCodeServer()
  } else if (code === 'publish') {
    applicationForm.value.isPublish = true
  } else if (code === 'revert') {
    applicationForm.value.isPublish = false
  }
}
</script>
<style scoped>
.application-form {
  margin-left: 50px;
  margin-right: 50px;
  margin-top: 20px;
  max-width: 60%;
}

.application-form-item {
  margin-bottom: 25px;
}
</style>
