<template>
  <div class="location-container">
    <div class="location-header">
      <h2>场景布局 <small v-if="currentScene">- {{ currentScene.name }}</small></h2>
      <div class="header-actions">
        <el-button type="primary" @click="handleViewDevices">设备列表</el-button>
      </div>
    </div>
    
    <el-card class="layout-card">
      <div class="layout-toolbar">
        <div class="toolbar-left">
          <el-tooltip content="放大" placement="top">
            <el-button circle :icon="ZoomIn" @click="changeZoom(0.1)"></el-button>
          </el-tooltip>
          <el-tooltip content="缩小" placement="top">
            <el-button circle :icon="ZoomOut" @click="changeZoom(-0.1)"></el-button>
          </el-tooltip>
          <el-tooltip content="重置视图" placement="top">
            <el-button circle :icon="Refresh" @click="resetZoom"></el-button>
          </el-tooltip>
        </div>
        
        <div class="toolbar-right">
          <el-switch
            v-model="showDeviceLabels"
            active-text="显示标签"
            inactive-text=""
          />
        </div>
      </div>
      
      <div 
        class="layout-canvas"
        ref="canvasContainer"
        @mousedown="startDrag"
        @mousemove="doDrag"
        @mouseup="stopDrag"
        @mouseleave="stopDrag"
      >
        <div 
          class="canvas-inner" 
          :style="{ 
            transform: `scale(${zoom})`, 
            backgroundSize: `${gridSize * zoom}px ${gridSize * zoom}px`
          }"
        >
          <!-- 设备元素 -->
          <div 
            v-for="device in deviceStore.devices" 
            :key="device.id"
            class="device-item"
            :class="{ 'device-offline': device.status === 'offline' }"
            :style="{ 
              left: `${device.location.x}px`, 
              top: `${device.location.y}px` 
            }"
            @click="selectDevice(device)"
          >
            <el-icon><Monitor /></el-icon>
            <span v-if="showDeviceLabels || selectedDevice === device" class="device-label">{{ device.name }}</span>
          </div>
          
          <!-- 背景网格在CSS中定义 -->
        </div>
      </div>
      
      <!-- 右侧设备信息面板 -->
      <div class="device-panel" v-if="selectedDevice">
        <div class="panel-header">
          <h3>设备信息</h3>
          <el-button type="text" @click="selectedDevice = null">关闭</el-button>
        </div>
        <div class="panel-content">
          <div class="info-row">
            <span class="label">名称:</span>
            <span class="value">{{ selectedDevice.name }}</span>
          </div>
          <div class="info-row">
            <span class="label">类型:</span>
            <span class="value">{{ selectedDevice.type }}</span>
          </div>
          <div class="info-row">
            <span class="label">状态:</span>
            <span class="value">
              <el-tag :type="selectedDevice.status === 'online' ? 'success' : 'danger'" size="small">
                {{ selectedDevice.status === 'online' ? '在线' : '离线' }}
              </el-tag>
            </span>
          </div>
          <div class="info-row">
            <span class="label">最后更新:</span>
            <span class="value">{{ selectedDevice.lastUpdated }}</span>
          </div>
          <div class="info-row">
            <span class="label">位置:</span>
            <span class="value">X: {{ selectedDevice.location.x }}, Y: {{ selectedDevice.location.y }}</span>
          </div>
          
          <div class="panel-actions">
            <el-button type="primary" size="small" @click="editSelectedDevice">编辑设备</el-button>
          </div>
        </div>
      </div>
    </el-card>
    
    <!-- 编辑设备对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="编辑设备信息"
      width="50%"
    >
      <el-form
        :model="deviceForm"
        label-width="120px"
        :rules="rules"
        ref="deviceFormRef"
      >
        <el-form-item label="设备名称" prop="name">
          <el-input v-model="deviceForm.name" placeholder="请输入设备名称"></el-input>
        </el-form-item>
        <el-form-item label="设备类型" prop="type">
          <el-select v-model="deviceForm.type" placeholder="请选择设备类型">
            <el-option label="传感器" value="传感器"></el-option>
            <el-option label="监测器" value="监测器"></el-option>
            <el-option label="控制设备" value="控制设备"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="deviceForm.status" placeholder="请选择状态">
            <el-option label="在线" value="online"></el-option>
            <el-option label="离线" value="offline"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="位置X坐标" prop="x">
          <el-input-number v-model="deviceForm.x" :min="0" :max="500"></el-input-number>
        </el-form-item>
        <el-form-item label="位置Y坐标" prop="y">
          <el-input-number v-model="deviceForm.y" :min="0" :max="500"></el-input-number>
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
import { useDeviceStore } from '@/store/device'
import { useSceneStore } from '@/store/scene'
import { ElMessage, type FormInstance } from 'element-plus'
import { ZoomIn, ZoomOut, Refresh, Monitor } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const deviceStore = useDeviceStore()
const sceneStore = useSceneStore()

// 引用
const deviceFormRef = ref<FormInstance>()
const canvasContainer = ref<HTMLElement | null>(null)

// 状态
const state = reactive({
  deviceForm: {
    name: '',
    type: '传感器',
    status: 'online',
    sceneId: parseInt(route.query.sceneId as string) || null,
    x: 100,
    y: 100
  },
  dialogVisible: false,
  selectedDevice: null as any,
  zoom: 1,
  gridSize: 20,
  showDeviceLabels: false,
  isDragging: false,
  dragStartX: 0,
  dragStartY: 0,
  canvasOffsetX: 0,
  canvasOffsetY: 0,
  lastCanvasOffsetX: 0,
  lastCanvasOffsetY: 0
})

const { 
  deviceForm, dialogVisible, selectedDevice, zoom, gridSize, 
  showDeviceLabels, isDragging, dragStartX, dragStartY,
  canvasOffsetX, canvasOffsetY, lastCanvasOffsetX, lastCanvasOffsetY
} = toRefs(state)

// 获取当前场景
const currentScene = computed(() => {
  return sceneStore.currentScene
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入设备名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择设备类型', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 初始化
onMounted(async () => {
  // 获取sceneId参数
  const sceneId = parseInt(route.query.sceneId as string)
  if (sceneId) {
    await deviceStore.fetchDevices(sceneId)
  } else if (currentScene.value && currentScene.value.id) {
    await deviceStore.fetchDevices(currentScene.value.id)
  } else {
    await deviceStore.fetchDevices()
  }
})

// 查看设备列表
const handleViewDevices = () => {
  const sceneId = currentScene.value ? currentScene.value.id : (route.query.sceneId as string)
  router.push(`/scene/device?sceneId=${sceneId}`)
}

// 选择设备
const selectDevice = (device: any) => {
  selectedDevice.value = device
}

// 编辑选中的设备
const editSelectedDevice = () => {
  if (selectedDevice.value) {
    deviceForm.value = {
      name: selectedDevice.value.name,
      type: selectedDevice.value.type,
      status: selectedDevice.value.status,
      sceneId: selectedDevice.value.sceneId,
      x: selectedDevice.value.location ? selectedDevice.value.location.x : 100,
      y: selectedDevice.value.location ? selectedDevice.value.location.y : 100
    }
    dialogVisible.value = true
  }
}

// 提交表单
const submitForm = async () => {
  if (!deviceFormRef.value || !selectedDevice.value) return
  
  await deviceFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 构建location对象
        const formData = {
          ...deviceForm.value,
          location: {
            x: deviceForm.value.x,
            y: deviceForm.value.y
          }
        }
        
        delete formData.x
        delete formData.y
        
        await deviceStore.updateDevice(selectedDevice.value.id, formData)
        dialogVisible.value = false
        ElMessage.success('更新成功')
        
        // 更新选中的设备
        selectedDevice.value = deviceStore.devices.find((d: any) => d.id === selectedDevice.value.id)
      } catch (error) {
        ElMessage.error('更新失败')
      }
    }
  })
}

// 画布缩放相关
const changeZoom = (delta: number) => {
  zoom.value = Math.min(Math.max(0.2, zoom.value + delta), 3)
}

const resetZoom = () => {
  zoom.value = 1
  canvasOffsetX.value = 0
  canvasOffsetY.value = 0
  lastCanvasOffsetX.value = 0
  lastCanvasOffsetY.value = 0
}

// 画布拖动相关
const startDrag = (e: MouseEvent) => {
  isDragging.value = true
  dragStartX.value = e.clientX
  dragStartY.value = e.clientY
}

const doDrag = (e: MouseEvent) => {
  if (!isDragging.value) return
  
  const deltaX = e.clientX - dragStartX.value
  const deltaY = e.clientY - dragStartY.value
  
  canvasOffsetX.value = lastCanvasOffsetX.value + deltaX
  canvasOffsetY.value = lastCanvasOffsetY.value + deltaY
  
  if (canvasContainer.value) {
    const inner = canvasContainer.value.querySelector('.canvas-inner') as HTMLElement
    if (inner) {
      inner.style.marginLeft = `${canvasOffsetX.value}px`
      inner.style.marginTop = `${canvasOffsetY.value}px`
    }
  }
}

const stopDrag = () => {
  if (isDragging.value) {
    isDragging.value = false
    lastCanvasOffsetX.value = canvasOffsetX.value
    lastCanvasOffsetY.value = canvasOffsetY.value
  }
}
</script>

<style scoped>
.location-container {
  padding: 20px;
}

.location-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.layout-card {
  height: calc(100vh - 180px);
}

.layout-toolbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  padding: 5px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.toolbar-left, .toolbar-right {
  display: flex;
  align-items: center;
}

.toolbar-left > * {
  margin-right: 5px;
}

.layout-canvas {
  position: relative;
  width: 100%;
  height: calc(100% - 50px);
  overflow: hidden;
  border: 1px solid #dcdfe6;
  background-color: #f5f7fa;
  cursor: move;
}

.canvas-inner {
  position: relative;
  width: 100%;
  height: 100%;
  background-image: linear-gradient(#dcdfe6 1px, transparent 1px), 
                    linear-gradient(90deg, #dcdfe6 1px, transparent 1px);
  background-size: 20px 20px;
  transform-origin: 0 0;
}

.device-item {
  position: absolute;
  width: 30px;
  height: 30px;
  margin-left: -15px;
  margin-top: -15px;
  background-color: #409eff;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  transition: transform 0.2s;
}

.device-item:hover {
  transform: scale(1.1);
}

.device-offline {
  background-color: #909399;
}

.device-label {
  position: absolute;
  top: 35px;
  white-space: nowrap;
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
}

.device-panel {
  position: absolute;
  right: 20px;
  top: 60px;
  width: 300px;
  background-color: white;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px;
  border-bottom: 1px solid #dcdfe6;
}

.panel-header h3 {
  margin: 0;
}

.panel-content {
  padding: 15px;
}

.info-row {
  margin-bottom: 10px;
  display: flex;
}

.info-row .label {
  width: 80px;
  color: #606266;
  font-weight: bold;
}

.info-row .value {
  flex: 1;
}

.panel-actions {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}
</style>
