<template>
  <div class="domain-setting-container">
    <div class="domain-header">
      <h2>{{ isEditMode ? '编辑领域-'+domainForm.name : '创建领域' }}</h2>
      <div class="header-actions">
        <el-button @click="navigateBack">返回列表</el-button>
        <el-button 
          type="primary" 
          @click="handlePublish"
          v-if="isEditMode"
        >{{domainForm.status==='1' ? '取消发布':'发布'}}</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">{{ isEditMode ? '保存' : '创建' }}</el-button>
        <el-button 
          type="primary" 
          plain
          v-if="!isEditMode"
          @click="importTemplate"
        >从模板导入</el-button>
        <el-button 
          type="primary" 
          plain
          v-if="!isEditMode"
          @click="saveTemplate"
        >保存为模板</el-button>
      </div>
    </div>
    
    <el-card class="setting-content">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-form 
            :model="domainForm" 
            :rules="rules"
            ref="domainFormRef"
            label-width="120px">
            <el-form-item label="领域编码" prop="code">
              <el-input v-model="domainForm.code" placeholder="请输入领域名称"></el-input>
            </el-form-item>
            <el-form-item label="领域名称" prop="name">
              <el-input v-model="domainForm.name" placeholder="请输入领域名称"></el-input>
            </el-form-item>
            <el-form-item label="领域描述" prop="description">
              <el-input type="textarea" :rows="3" v-model="domainForm.description" placeholder="请输入领域描述"></el-input>
            </el-form-item>
            <el-form-item label="代码编辑器" prop="codeEditor" class="half-width">
              <el-select v-model="domainForm.codeEditor" placeholder="请选择代码编辑器">
                <el-option label="默认" value="default" />
              </el-select>
            </el-form-item>
            <el-form-item label="模型编辑器" prop="modelEditor" class="half-width">
              <el-select v-model="domainForm.modelEditor" placeholder="请选择模型编辑器">
                <el-option label="默认" value="default" />
              </el-select>
            </el-form-item>
            <el-form-item label="基础运行框架" prop="baseFramework" class="half-width">
              <el-select v-model="domainForm.baseFramework" placeholder="请选择基础运行框架">
                <el-option label="Spring Boot" value="springboot" />
                <el-option label="Node.js" value="nodejs" />
              </el-select>
            </el-form-item>
            <el-form-item label="DSL标准" prop="dslStandard" class="half-width">
              <el-select v-model="domainForm.dslStandard" placeholder="请选择DSL标准">
                <el-option label="默认" value="default" />
                <el-option label="UBML" value="UBML" />
              </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-tag :type="domainForm.status==='1' ? 'success':'info'">{{ domainForm.status==='1' ? '已发布':'定制中' }}</el-tag>
            </el-form-item>
            <el-form-item label="领域地址" prop="url" v-if="domainForm.status==='1'">
              <el-input v-model="domainForm.url"></el-input>
            </el-form-item>
            <el-form-item label="图标">
              <el-upload
                action="#"
                list-type="picture-card"
                :auto-upload="false"
              >
                <el-icon><Plus /></el-icon>
              </el-upload>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="领域模板" name="template" v-if="isEditMode">
          <!-- 模板库对接 -->
          <DomainTemplate />
        </el-tab-pane>

        <el-tab-pane label="设备类型" name="model" v-if="isEditMode">
          <DomainDeviceType />
        </el-tab-pane>

        <el-tab-pane label="领域组件" name="component" v-if="isEditMode">
          <DomainComponent />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- Publish Dialog -->
    <el-dialog
    v-model="publishDialogVisible"
    title="发布领域"
    width="500"
    >
    <el-input v-model="domainForm.url" placeholder="请输入发布地址"></el-input>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="publishDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="publishDomain">
          确定
        </el-button>
      </div>
    </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch, toRefs } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { useDomainStore } from '@/store/domain'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { getDomainById, getMockDomainById } from '@/api/domain'
import { useRouter, useRoute } from 'vue-router'
import DomainDeviceType from './component/DomainDeviceType.vue'
import DomainTemplate from './component/DomainTemplate.vue'
import DomainComponent from './component/DomainComponent.vue'

interface DomainForm {
  code: string
  name: string
  description: string
  status: string
  codeEditor: string
  modelEditor: string
  baseFramework: string
  dslStandard: string
  url: string
}

const router = useRouter()
const route = useRoute()
const domainStore = useDomainStore()
const domainFormRef = ref<FormInstance>()

// State
const state = reactive({
  activeTab: 'basic',
  domainForm: {
    code: '',
    name: '',
    description: '',
    status: '0',
    codeEditor: '',
    modelEditor: '',
    baseFramework: '',
    dslStandard: '',
    url: ''
  } as DomainForm,
  submitting: false,
  publishDialogVisible: false
})

const { activeTab, domainForm, submitting, publishDialogVisible } = toRefs(state)

// Determine if we're in edit mode
const isEditMode = computed(() => {
  return route.query.mode === 'edit'
})

// Get current domain ID from query params
const domainId = computed(() => {
  return parseInt(route.query.domainId as string) || null
})

// Rules for form validation
const rules: FormRules = {
  code: [
    { required: true, message: '请输入领域编码', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入领域名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入领域描述', trigger: 'blur' }
  ],
  codeEditor: [
    { required: true, message: '请选择代码编辑器', trigger: 'change' }
  ],
  modelEditor: [
    { required: true, message: '请选择模型编辑器', trigger: 'change' }
  ],
  baseFramework: [
    { required: true, message: '请选择基础运行框架', trigger: 'change' }
  ],
  dslStandard: [
    { required: true, message: '请选择DSL标准', trigger: 'change' }
  ]
}

// Clear form for creation mode
const resetFormData = () => {
  domainForm.value = {
    code: '',
    name: '',
    description: '',
    status: '0',
    codeEditor: '',
    modelEditor: '',
    baseFramework: '',
    dslStandard: '',
    url: ''
  }
}

// Helper function to load domain data to form
const loadDomainToForm = (domain: any) => {
  // First reset the form to clear any previous data
  resetFormData()
  
  // Then load the domain data
  if (domain) {
    domainForm.value.code = domain.domainCode || ''
    domainForm.value.name = domain.domainName || ''
    domainForm.value.description = domain.domainDescription || ''
    domainForm.value.status = domain.status || '0'
    domainForm.value.url = domain.url || ''
    domainForm.value.codeEditor = domain.codeEditor || ''
    domainForm.value.modelEditor = domain.modelEditor || ''
    domainForm.value.baseFramework = domain.framework || ''
    domainForm.value.dslStandard = domain.dsl || ''
    
    console.log('Domain data loaded to form:', domainForm.value)
  }
}

// Watch for changes in route params to update form data accordingly
watch([() => route.query.domainId, () => route.query.mode], async ([newDomainId, newMode]) => {
  if (newMode === 'create') {
    // Clear form data when switching to create mode
    resetFormData()
    activeTab.value='basic'
  } else if (newMode === 'edit' && newDomainId) {
    // Load domain data when switching to edit mode or changing domain ID
    try {
      const res: any = await getDomainById(parseInt(newDomainId as string))
      if (res.data && res.status === 200) {
        domainStore.setCurrentDomain(res.data)
        loadDomainToForm(res.data)
      } else {
        ElMessage.warning('领域数据不存在或获取失败')
        navigateBack()
      }
    } catch (error) {
      console.error('Failed to fetch domain:', error)
      ElMessage.warning('领域数据不存在或获取失败')
      navigateBack()
    }
  }
}, { immediate: true })

// Load domain data if in edit mode
onMounted(async () => {
  // Clear form when in create mode
  if (!isEditMode.value) {
    resetFormData()
  }
  else if (isEditMode.value && domainId.value) {
    const currentDomain = domainStore.currentDomain
    
    if (currentDomain && currentDomain.domainId === domainId.value) {
      // Load from current domain in store
      loadDomainToForm(currentDomain)
    } else {
      // Try to fetch domain data from API if not in store
      try {
        const res: any = await getDomainById(domainId.value)
        if (res.data && res.status === 200) {
          domainStore.setCurrentDomain(res.data)
          loadDomainToForm(res.data)
        } else {
          ElMessage.warning('领域数据不存在或获取失败')
          navigateBack()
        }
      } catch (error) {
        console.error('Failed to fetch domain:', error)
        ElMessage.warning('领域数据不存在或获取失败')
        navigateBack()
      }
    }
  }
})

// Navigate back to domain list
const navigateBack = () => {
  router.push('/meta/domain/list')
}

// Submit form - either create or update domain
const submitForm = async () => {
  if (!domainFormRef.value) return
  
  await domainFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (isEditMode.value && domainId.value) {
          // Update existing domain
          await domainStore.updateDomain(domainId.value, domainForm.value)
          ElMessage.success('更新成功')
        } else {
          // Create new domain
          await domainStore.createDomain(domainForm.value)
          ElMessage.success('创建成功')
        }
        // Navigate back to list after successful operation
        // navigateBack()
      } catch (error) {
        ElMessage.error(isEditMode.value ? '更新失败' : '创建失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

// Handle publish
const handlePublish = async () => {
  if (!domainFormRef.value) return
  
  await domainFormRef.value.validate(async (valid) => {
    if (valid) {
      if(domainForm.value.status === '1') {
        domainStore.publishDomain(domainId.value, '', '0')
        .then((res)=>{
          ElMessage.success('取消发布成功')
          loadDomainToForm(res)
        }
        ).catch((error)=>{
          ElMessage.error('取消发布失败:', error)
        })
      }else{
        publishDialogVisible.value=true
      }
    }
  })
}

const publishDomain = async () => {
  if(domainForm.value.url){
    domainStore.publishDomain(domainId.value, domainForm.value.url, '1')
    .then((res)=>{
        ElMessage.success('发布成功')
        loadDomainToForm(res)
        publishDialogVisible.value = false
      }
    ).catch((error)=>{
      ElMessage.error('发布失败:', error)
    })
  }else{
    ElMessage.warning('请输入url')
  }
}

const importTemplate = () => {
  console.log('importTemplate')
}

const saveTemplate = () => {
  console.log('saveTemplate')
}
</script>

<style scoped>
.domain-setting-container {
  padding: 20px;
}

.domain-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.setting-content {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
}

.publish-url {
  margin-top: 10px;
  width: 100%;
}

:deep(.half-width) {
  width: 50%;
}
</style>
