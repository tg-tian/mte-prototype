<template>
  <div class="component-setting-container">
    <div class="page-header">
      <div class="page-title-group">
        <h2 class="page-main-title">{{ isEditMode ? '编辑组件' : '创建组件' }}</h2>
        <p v-if="isEditMode" class="page-sub-title">{{ componentForm.name || '组件详情' }}</p>
        <p v-else class="page-sub-title">定义新的流程节点或连接边组件及其拓扑约束</p>
      </div>
      <div class="header-actions">
        <el-button @click="navigateBack">返回列表</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
      </div>
    </div>

    <el-card class="setting-card" shadow="never">
      <template #header>
        <div class="card-header">
          <el-icon><InfoFilled /></el-icon>
          <span>基础配置</span>
        </div>
      </template>
      <el-form 
        :model="componentForm" 
        :rules="rules"
        ref="componentFormRef"
        label-position="top">
        <el-row :gutter="32">
          <el-col :span="8">
            <el-form-item label="组件编码" prop="code">
              <el-input v-model="componentForm.code" placeholder="例如：temp_sensor" :disabled="isEditMode"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="组件名称" prop="name">
              <el-input v-model="componentForm.name" placeholder="例如：温度传感器"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="用途" prop="purpose">
              <el-select v-model="componentForm.purpose" placeholder="选择用途" style="width: 100%">
                <el-option :label="'业务流'" :value="PurposeType.BusinessFlow"></el-option>
                <el-option :label="'界面流'" :value="PurposeType.InterfaceFlow"></el-option>
                <el-option :label="'设备逻辑'" :value="PurposeType.DeviceLogic"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="32">
          <el-col :span="8">
            <el-form-item label="组件类型" prop="type">
              <el-radio-group v-model="componentForm.type" @change="handleTypeChange">
                <el-radio-button :label="ComponentType.Node">节点 (Node)</el-radio-button>
                <el-radio-button :label="ComponentType.Edge">边 (Edge)</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="组件描述" prop="description">
              <el-input type="textarea" :rows="1" v-model="componentForm.description" placeholder="请输入组件的功能说明"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 约束配置 -->
        <div class="constraint-divider">
          <span>拓扑约束配置</span>
        </div>
        
        <!-- 节点约束 -->
        <template v-if="componentForm.type === ComponentType.Node">
          <div class="constraint-section">
            <el-row :gutter="40">
              <el-col :span="12">
                <div class="constraint-group">
                  <div class="group-title">入口约束 (Input)</div>
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="数量上限" prop="inputConstraint.quantity">
                        <el-input-number 
                          v-model="componentForm.inputConstraint.quantity" 
                          :min="-1" 
                          style="width: 100%"
                        ></el-input-number>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="允许类型" prop="inputConstraint.type">
                        <el-input v-model="componentForm.inputConstraint.type" placeholder="any / none"></el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <div class="form-tip">数量为 -1 表示无限制，0 表示不允许接入</div>
                </div>
              </el-col>

              <el-col :span="12">
                <div class="constraint-group">
                  <div class="group-title">出口约束 (Output)</div>
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="数量上限" prop="outputConstraint.quantity">
                        <el-input-number 
                          v-model="componentForm.outputConstraint.quantity" 
                          :min="-1" 
                          style="width: 100%"
                        ></el-input-number>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="允许类型" prop="outputConstraint.type">
                        <el-input v-model="componentForm.outputConstraint.type" placeholder="any / none"></el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
              </el-col>
            </el-row>
          </div>
        </template>
        
        <!-- 边约束 -->
        <template v-if="componentForm.type === ComponentType.Edge">
          <div class="constraint-section">
            <el-row :gutter="40">
              <el-col :span="12">
                <div class="constraint-group">
                  <div class="group-title">起点约束 (Start)</div>
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="数量上限" prop="startConstraint.quantity">
                        <el-input-number v-model="componentForm.startConstraint.quantity" :min="-1" style="width: 100%"></el-input-number>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="节点属性" prop="startConstraint.type">
                        <el-input v-model="componentForm.startConstraint.type" placeholder="node / 标识符"></el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
              </el-col>

              <el-col :span="12">
                <div class="constraint-group">
                  <div class="group-title">终点约束 (End)</div>
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="数量上限" prop="endConstraint.quantity">
                        <el-input-number v-model="componentForm.endConstraint.quantity" :min="-1" style="width: 100%"></el-input-number>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="节点属性" prop="endConstraint.type">
                        <el-input v-model="componentForm.endConstraint.type" placeholder="node / 标识符"></el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
              </el-col>
            </el-row>
          </div>
        </template>
      </el-form>
    </el-card>

    <!-- JSON查看对话框 -->
    <el-dialog v-model="jsonDialogVisible" title="组件JSON" width="60%" class="premium-dialog">
      <pre class="json-viewer">{{ formattedJson }}</pre>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="copyJson">复制</el-button>
          <el-button @click="jsonDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch, toRefs } from 'vue'
import { InfoFilled } from '@element-plus/icons-vue'
import { useComponentStore } from '@/store/component'
import { Component, ComponentType, PurposeType } from '@/types/models'
import { ElMessage, type FormInstance } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const componentStore = useComponentStore()
const componentFormRef = ref<FormInstance>()

// State
const state = reactive({
  componentForm: {
    code: '',
    name: '',
    description: '',
    type: ComponentType.Node,
    purpose: PurposeType.BusinessFlow,
    inputConstraint: {
      quantity: 0,
      type: 'none'
    },
    outputConstraint: {
      quantity: 1,
      type: 'any'
    },
    startConstraint: {
      quantity: 1,
      type: 'node'
    },
    endConstraint: {
      quantity: 1,
      type: 'node'
    }
  } as Component,
  submitting: false,
  jsonDialogVisible: false
})

const { componentForm, submitting, jsonDialogVisible } = toRefs(state)

// Determine if we're in edit mode
const isEditMode = computed(() => {
  return route.query.mode === 'edit'
})

// Get component ID if in edit mode
const componentId = computed(() => {
  return parseInt(route.query.componentId as string) || null
})

// Format JSON for viewing
const formattedJson = computed(() => {
  return JSON.stringify(componentForm.value, null, 2)
})

// Rules
const rules = {
  code: [
    { required: true, message: '请输入组件编码', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入组件名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  purpose: [
    { required: true, message: '请选择用途', trigger: 'change' }
  ]
}

// Reset form
const resetFormData = () => {
  componentForm.value = {
    code: '',
    name: '',
    description: '',
    type: ComponentType.Node,
    purpose: PurposeType.BusinessFlow,
    inputConstraint: {
      quantity: 0,
      type: 'none'
    },
    outputConstraint: {
      quantity: 1,
      type: 'any'
    },
    startConstraint: {
      quantity: 1,
      type: 'node'
    },
    endConstraint: {
      quantity: 1,
      type: 'node'
    }
  }
}

// Handle component type change
const handleTypeChange = (value: any) => {
  if (value === ComponentType.Node) {
    componentForm.value.inputConstraint = { quantity: 0, type: 'none' }
    componentForm.value.outputConstraint = { quantity: 1, type: 'any' }
  } else {
    componentForm.value.startConstraint = { quantity: 1, type: 'node' }
    componentForm.value.endConstraint = { quantity: 1, type: 'node' }
  }
}

// Copy JSON
const copyJson = () => {
  navigator.clipboard.writeText(formattedJson.value)
    .then(() => ElMessage.success('JSON已复制'))
    .catch(() => ElMessage.error('复制失败'))
}

const loadComponent = (data : any) => {
  const comp = JSON.parse(JSON.stringify(data))
  if(comp.type === ComponentType.Node){
    if(!comp.inputConstraint) comp.inputConstraint = { quantity: 0, type: 'none' }
    if(!comp.outputConstraint) comp.outputConstraint = { quantity: 1, type: 'any' }
  } else {
    if(!comp.startConstraint) comp.startConstraint = { quantity: 1, type: 'node' }
    if(!comp.endConstraint) comp.endConstraint = { quantity: 1, type: 'node' }
  }
  componentForm.value = comp
}

watch([() => route.query.componentId, () => route.query.mode], async ([newId, newMode]) => {
  if (newMode === 'create') {
    resetFormData()
  } else if (newMode === 'edit' && newId) {
    await componentStore.fetchComponentById(parseInt(newId as string))
    if (componentStore.currentComponent) {
      loadComponent(componentStore.currentComponent)
    }
  }
}, { immediate: true })

onMounted(async () => {
  if (isEditMode.value && componentId.value && !componentStore.currentComponent) {
    await componentStore.fetchComponentById(componentId.value)
    if (componentStore.currentComponent) {
      loadComponent(componentStore.currentComponent)
    }
  }
})

const navigateBack = () => {
  router.push('/meta/component/list')
}

const submitForm = async () => {
  if (!componentFormRef.value) return
  await componentFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (isEditMode.value && componentId.value) {
          await componentStore.updateComponent(componentId.value, componentForm.value)
          ElMessage.success('更新成功')
        } else {
          await componentStore.createComponent(componentForm.value)
          ElMessage.success('创建成功')
        }
        navigateBack()
      } catch (error) {
        ElMessage.error('保存失败')
      } finally {
        submitting.value = false
      }
    }
  })
}
</script>

<style scoped>
.component-setting-container {
  width: 100%;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.setting-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
}

.constraint-divider {
  margin: 32px 0 24px;
  position: relative;
  text-align: center;
}

.constraint-divider:before {
  content: "";
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: #ebeef5;
}

.constraint-divider span {
  position: relative;
  background: #fff;
  padding: 0 16px;
  color: #909399;
  font-size: 14px;
  font-weight: 500;
}

.constraint-section {
  background: #f8fafd;
  padding: 24px;
  border-radius: 8px;
  border: 1px solid #edf2f9;
}

.constraint-group {
  margin-bottom: 0;
}

.group-title {
  font-size: 15px;
  font-weight: 600;
  color: #475669;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.group-title:before {
  content: "";
  width: 4px;
  height: 16px;
  background: #409eff;
  border-radius: 2px;
  margin-right: 8px;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.json-viewer {
  background-color: #f5f7fa;
  color: #606266;
  padding: 16px;
  border-radius: 4px;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', 'Consolas', monospace;
  font-size: 14px;
  line-height: 1.5;
  overflow: auto;
  max-height: 60vh;
  white-space: pre-wrap;
}
</style>
