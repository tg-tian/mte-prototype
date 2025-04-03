<template>
  <div class="domain-setting-container">
    <div class="domain-header">
      <h2>{{ isEditMode ? '编辑领域' : '创建领域' }}</h2>
      <div class="header-actions">
        <el-button @click="navigateBack">返回列表</el-button>
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
            <el-form-item label="领域名称" prop="name">
              <el-input v-model="domainForm.name" placeholder="请输入领域名称"></el-input>
            </el-form-item>
            <el-form-item label="领域描述" prop="description">
              <el-input type="textarea" rows="3" v-model="domainForm.description" placeholder="请输入领域描述"></el-input>
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="domainForm.status" placeholder="请选择状态">
                <el-option label="活跃" value="active"></el-option>
                <el-option label="非活跃" value="inactive"></el-option>
              </el-select>
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
        
        <el-tab-pane label="模型定义" name="model">
          <el-empty description="暂无模型数据" />
        </el-tab-pane>
        
        <el-tab-pane label="业务流程" name="process">
          <el-empty description="暂无业务流程" />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, toRefs } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { useDomainStore } from '@/store/domain'
import { ElMessage, type FormInstance } from 'element-plus'

const router = useRouter()
const route = useRoute()
const domainStore = useDomainStore()
const domainFormRef = ref<FormInstance>()

// State
const state = reactive({
  activeTab: 'basic',
  domainForm: {
    name: '',
    description: '',
    status: 'active'
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

// Load domain data if in edit mode
onMounted(() => {
  if (isEditMode.value && domainId.value) {
    const currentDomain = domainStore.currentDomain
    
    if (currentDomain && currentDomain.id === domainId.value) {
      // Load from current domain in store
      domainForm.value.name = currentDomain.name
      domainForm.value.description = currentDomain.description
      domainForm.value.status = currentDomain.status
    } else {
      // Redirect back if no domain data is available
      ElMessage.warning('领域数据不存在')
      navigateBack()
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
        navigateBack()
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
