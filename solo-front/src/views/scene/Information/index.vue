<template>
  <div class="scene-info-container">
    <div class="scene-header">
      <h2>场景信息 <small v-if="currentScene">- {{ currentScene.name }}</small></h2>
      <el-button type="primary" @click="handleEdit">编辑场景</el-button>
    </div>
    
    <el-card class="scene-info-card">
      <template #header>
        <div class="card-header">
          <span>基本信息</span>
        </div>
      </template>
      
      <div class="info-item">
        <span class="info-label">场景名称:</span>
        <span class="info-value">{{ currentScene?.name || '-' }}</span>
      </div>
      
      <div class="info-item">
        <span class="info-label">描述:</span>
        <span class="info-value">{{ currentScene?.description || '-' }}</span>
      </div>
      
      <div class="info-item">
        <span class="info-label">创建时间:</span>
        <span class="info-value">{{ currentScene?.createTime || '-' }}</span>
      </div>
      
      <div class="info-item">
        <span class="info-label">更新时间:</span>
        <span class="info-value">{{ currentScene?.updateTime || '-' }}</span>
      </div>
      
      <div class="info-item">
        <span class="info-label">设备数量:</span>
        <span class="info-value">{{ currentScene?.deviceCount || '0' }}</span>
      </div>
      
      <div class="info-item">
        <span class="info-label">状态:</span>
        <span class="info-value">
          <el-tag :type="currentScene?.status === 'active' ? 'success' : 'info'">
            {{ currentScene?.status === 'active' ? '活跃' : '非活跃' }}
          </el-tag>
        </span>
      </div>
    </el-card>
    
    <!-- 场景图表卡片 -->
    <el-card class="scene-chart-card">
      <template #header>
        <div class="card-header">
          <span>场景数据概览</span>
        </div>
      </template>
      
      <el-empty description="暂无图表数据" />
    </el-card>
    
    <!-- 编辑场景对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="编辑场景信息"
      width="50%"
    >
      <el-form
        :model="sceneForm"
        label-width="120px"
        :rules="rules"
        ref="sceneFormRef"
      >
        <el-form-item label="场景名称" prop="name">
          <el-input v-model="sceneForm.name" placeholder="请输入场景名称"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="sceneForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入场景描述"
          ></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="sceneForm.status" placeholder="请选择状态">
            <el-option label="活跃" value="active"></el-option>
            <el-option label="非活跃" value="inactive"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, toRefs } from 'vue'
import { useSceneStore } from '@/store/scene'
import { ElMessage, type FormInstance } from 'element-plus'

const router = useRouter()
const route = useRoute()
const sceneStore = useSceneStore()

// 表单引用
const sceneFormRef = ref<FormInstance>()

// 状态
const state = reactive({
  sceneForm: {
    name: '',
    description: '',
    status: 'active'
  },
  dialogVisible: false
})

const { sceneForm, dialogVisible } = toRefs(state)

// 获取当前场景
const currentScene = computed(() => {
  return sceneStore.currentScene
})

// 表单验证规则
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

// 初始化
onMounted(async () => {
  const sceneId = parseInt(route.query.sceneId as string)
  // 如果有sceneId参数但没有当前场景，可能需要从API获取
  if (sceneId && !currentScene.value) {
    // 这里可以添加获取场景详情的逻辑
  }
})

// 打开编辑对话框
const handleEdit = () => {
  if (currentScene.value) {
    sceneForm.value = {
      name: currentScene.value.name,
      description: currentScene.value.description,
      status: currentScene.value.status
    }
    dialogVisible.value = true
  }
}

// 提交表单
const submitForm = async () => {
  if (!sceneFormRef.value || !currentScene.value) return
  
  await sceneFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await sceneStore.updateScene(currentScene.value.id, sceneForm.value)
        dialogVisible.value = false
        ElMessage.success('更新成功')
      } catch (error) {
        ElMessage.error('更新失败')
      }
    }
  })
}
</script>

<style scoped>
.scene-info-container {
  padding: 20px;
}

.scene-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.scene-info-card, .scene-chart-card {
  margin-bottom: 20px;
}

.scene-chart-card {
  min-height: 300px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-item {
  margin-bottom: 10px;
  display: flex;
}

.info-label {
  width: 100px;
  color: #606266;
}

.info-value {
  flex: 1;
  color: #303133;
}
</style>
