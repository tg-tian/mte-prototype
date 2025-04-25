<template>
  <div class="domain-setting-container">
    <div class="domain-header">
      <h2>{{ isEditMode ? '编辑领域' : '创建领域' }}</h2>
      <div class="header-actions">
        <el-button @click="navigateBack">返回列表</el-button>
        <el-button type="primary" >发布</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
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
            <el-form-item label="状态" prop="status">
              <el-tag :type="domainForm.status==='1' ? 'success':'info'">{{ domainForm.status==='1' ? '已发布':'定制中' }}</el-tag>
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
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch, toRefs } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { useDomainStore } from '@/store/domain'
import { ElMessage, type FormInstance } from 'element-plus'
import { getDomainById, getMockDomainById } from '@/api/domain'
import { useRouter } from 'vue-router'
import DomainDeviceType from './component/DomainDeviceType.vue'
import DomainTemplate from './component/DomainTemplate.vue'

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
    status: '0'
  },
  submitting: false
})

const { activeTab, domainForm, submitting } = toRefs(state)

// Determine if we're in edit mode
const isEditMode = computed(() => {
  return route.query.mode === 'edit'
})

// Get current domain ID from query params
const domainId = computed(() => {
  return parseInt(route.query.domainId as string) || null
})

// Rules for form validation
const rules = {
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
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// Clear form for creation mode
const resetFormData = () => {
  domainForm.value = {
    code: '',
    name: '',
    description: '',
    status: '0'
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
</style>
