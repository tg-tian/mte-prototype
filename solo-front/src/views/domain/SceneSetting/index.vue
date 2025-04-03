<template>
  <div class="scene-setting-container">
    <div class="scene-header">
      <h2>{{ isEditMode ? '编辑场景' : '创建场景' }} <small v-if="currentDomain">- {{ currentDomain.name }}</small></h2>
      <div class="header-actions">
        <el-button @click="navigateBack">返回列表</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
      </div>
    </div>
    
    <el-card class="setting-content">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-form 
            :model="sceneForm" 
            :rules="rules"
            ref="sceneFormRef"
            label-width="120px">
            <el-form-item label="场景名称" prop="name">
              <el-input v-model="sceneForm.name" placeholder="请输入场景名称"></el-input>
            </el-form-item>
            <el-form-item label="场景描述" prop="description">
              <el-input type="textarea" rows="3" v-model="sceneForm.description" placeholder="请输入场景描述"></el-input>
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="sceneForm.status" placeholder="请选择状态">
                <el-option label="活跃" value="active"></el-option>
                <el-option label="非活跃" value="inactive"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="场景类型">
              <el-select placeholder="请选择场景类型">
                <el-option label="工厂车间" value="factory"></el-option>
                <el-option label="智慧城市" value="city"></el-option>
                <el-option label="医疗设施" value="hospital"></el-option>
                <el-option label="教育场所" value="education"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="设备类型定义" name="device">
          <el-empty description="暂无设备类型" />
        </el-tab-pane>
        
        <el-tab-pane label="数据流定义" name="dataflow">
          <el-empty description="暂无数据流定义" />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch, toRefs } from 'vue'
import { useSceneStore } from '@/store/scene'
import { useDomainStore } from '@/store/domain'
import { ElMessage, type FormInstance } from 'element-plus'
import { getMockSceneById } from '@/api/scene'

const router = useRouter()
const route = useRoute()
const sceneStore = useSceneStore()
const domainStore = useDomainStore()
const sceneFormRef = ref<FormInstance>()

// State
const state = reactive({
  activeTab: 'basic',
  sceneForm: {
    name: '',
    description: '',
    status: 'active',
    domainId: parseInt(route.query.domainId as string) || null
  },
  submitting: false
})

const { activeTab, sceneForm, submitting } = toRefs(state)

// Determine if we're in edit mode
const isEditMode = computed(() => {
  return route.query.mode === 'edit'
})

// Get current scene ID from query params
const sceneId = computed(() => {
  return parseInt(route.query.sceneId as string) || null
})

// Get domain ID from query params
const domainId = computed(() => {
  return parseInt(route.query.domainId as string) || null
})

// Get current domain
const currentDomain = computed(() => {
  return domainStore.currentDomain
})

// Rules for form validation
const rules = {
  name: [
    { required: true, message: '请输入场景名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入场景描述', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// Clear form for creation mode
const resetFormData = () => {
  sceneForm.value = {
    name: '',
    description: '',
    status: 'active',
    domainId: domainId.value
  }
}

// Helper function to load scene data to form
const loadSceneToForm = (scene: any) => {
  // First reset the form to clear any previous data
  resetFormData()
  
  // Then load the scene data
  if (scene) {
    sceneForm.value.name = scene.name || ''
    sceneForm.value.description = scene.description || ''
    sceneForm.value.status = scene.status || 'active'
    sceneForm.value.domainId = scene.domainId || domainId.value
    
    console.log('Scene data loaded to form:', sceneForm.value)
  }
}

// Watch for changes in route params to update form data accordingly
watch([() => route.query.sceneId, () => route.query.mode, () => route.query.domainId], async ([newSceneId, newMode, newDomainId]) => {
  // Update domainId in form when it changes
  if (newDomainId) {
    sceneForm.value.domainId = parseInt(newDomainId as string)
  }
  
  if (newMode === 'create') {
    // Clear form data when switching to create mode
    resetFormData()
  } else if (newMode === 'edit' && newSceneId) {
    // Load scene data when switching to edit mode or changing scene ID
    try {
      const res: any = await getMockSceneById(parseInt(newSceneId as string))
      if (res.data && res.data.code === 200 && res.data.data) {
        sceneStore.setCurrentScene(res.data.data)
        loadSceneToForm(res.data.data)
      } else {
        ElMessage.warning('场景数据不存在或获取失败')
        navigateBack()
      }
    } catch (error) {
      console.error('Failed to fetch scene:', error)
      ElMessage.warning('场景数据不存在或获取失败')
      navigateBack()
    }
  }
}, { immediate: true })

// Load scene data if in edit mode
onMounted(async () => {
  // Clear form when in create mode
  if (!isEditMode.value) {
    resetFormData()
  }
  else if (isEditMode.value && sceneId.value) {
    const currentScene = sceneStore.currentScene
    
    if (currentScene && currentScene.id === sceneId.value) {
      // Load from current scene in store
      loadSceneToForm(currentScene)
    } else {
      // Try to fetch scene data from API if not in store
      try {
        const res: any = await getMockSceneById(sceneId.value)
        if (res.data && res.data.code === 200 && res.data.data) {
          sceneStore.setCurrentScene(res.data.data)
          loadSceneToForm(res.data.data)
        } else {
          ElMessage.warning('场景数据不存在或获取失败')
          navigateBack()
        }
      } catch (error) {
        console.error('Failed to fetch scene:', error)
        ElMessage.warning('场景数据不存在或获取失败')
        navigateBack()
      }
    }
  } else if (!domainId.value) {
    // If not in edit mode and no domain ID, show warning
    ElMessage.warning('请先选择一个领域')
    navigateBack()
  }
})

// Navigate back to scene list
const navigateBack = () => {
  if (domainId.value) {
    router.push(`/domain/scene/list?domainId=${domainId.value}`)
  } else {
    router.push('/domain/scene/list')
  }
}

// Submit form - either create or update scene
const submitForm = async () => {
  if (!sceneFormRef.value) return
  
  await sceneFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (isEditMode.value && sceneId.value) {
          // Update existing scene
          await sceneStore.updateScene(sceneId.value, sceneForm.value)
          ElMessage.success('更新成功')
        } else {
          // Create new scene
          await sceneStore.createScene(sceneForm.value)
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
.scene-setting-container {
  padding: 20px;
}

.scene-header {
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
