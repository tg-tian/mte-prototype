<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <h2>数据面板 <small v-if="currentScene">- {{ currentScene.name }}</small></h2>
      <div class="header-actions">
        <el-button type="primary" @click="refreshData">刷新数据</el-button>
      </div>
    </div>
    
    <!-- 设备状态卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="status-card">
          <template #header>
            <div class="card-header">
              <span>设备总数</span>
            </div>
          </template>
          <div class="card-value">{{ deviceCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="status-card">
          <template #header>
            <div class="card-header">
              <span>在线设备</span>
            </div>
          </template>
          <div class="card-value">{{ onlineDevices }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="status-card">
          <template #header>
            <div class="card-header">
              <span>离线设备</span>
            </div>
          </template>
          <div class="card-value">{{ offlineDevices }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="status-card">
          <template #header>
            <div class="card-header">
              <span>健康状态</span>
            </div>
          </template>
          <div class="card-value">
            <el-progress 
              :percentage="healthPercentage" 
              :status="healthStatus" 
              :stroke-width="20"
            ></el-progress>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 设备监控列表 -->
    <el-card class="monitor-card">
      <template #header>
        <div class="card-header">
          <span>设备监控</span>
          <el-switch 
            v-model="autoRefresh" 
            active-text="自动刷新" 
            inactive-text="手动刷新"
          ></el-switch>
        </div>
      </template>
      
      <el-table 
        :data="deviceData" 
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="设备名称" min-width="150"></el-table-column>
        <el-table-column prop="type" label="类型" width="120"></el-table-column>
        <el-table-column prop="lastUpdated" label="最后更新" width="180"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'online' ? 'success' : 'danger'">
              {{ scope.row.status === 'online' ? '在线' : '离线' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="当前值" width="180">
          <template #default="scope">
            <span v-if="scope.row.properties && scope.row.properties.currentValue !== undefined">
              {{ scope.row.properties.currentValue }} 
              {{ scope.row.properties.unit || '' }}
            </span>
            <span v-else-if="scope.row.properties && scope.row.properties.openState !== undefined">
              {{ scope.row.properties.openState ? '开启' : '关闭' }}
            </span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button 
              type="primary" 
              size="small" 
              @click="viewDeviceDetails(scope.row)"
            >详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 设备详情对话框 -->
    <el-dialog v-model="dialogVisible" title="设备详情" width="50%">
      <div v-if="selectedDevice">
        <h3>{{ selectedDevice.deviceName }}</h3>
        
        <el-descriptions :column="2" border>
          <el-descriptions-item label="设备编码">{{ selectedDevice.deviceCode }}</el-descriptions-item>
          <el-descriptions-item label="设备类型">{{ selectedDevice.deviceType?.name }}</el-descriptions-item>
          <el-descriptions-item label="当前状态">
            <el-tag :type="selectedDevice.status === 1 ? 'success' : 'danger'">
              {{ selectedDevice.status === 1 ? '在线' : (selectedDevice.status === 2 ? '未激活' : '离线') }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="最近上线时间">{{ selectedDevice.lastOnlineTime }}</el-descriptions-item>
        </el-descriptions>
        
        <!-- 模拟历史数据图表 -->
        <h4 style="margin-top: 20px">历史数据</h4>
        <div class="chart-placeholder">
          <el-empty description="此处将显示设备历史数据图表"></el-empty>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, onUnmounted, toRefs } from 'vue'
import { useDeviceStore } from '@/store/device'
import { useSceneStore } from '@/store/scene'
import { Device } from '@/types/models'

const route = useRoute()
const deviceStore = useDeviceStore()
const sceneStore = useSceneStore()

// 状态
const state = reactive({
  loading: false,
  autoRefresh: false,
  dialogVisible: false,
  selectedDevice: null as Device | null,
  refreshTimer: null as ReturnType<typeof setInterval> | null
})

const { loading, autoRefresh, dialogVisible, selectedDevice, refreshTimer } = toRefs(state)

// 获取当前场景
const currentScene = computed(() => {
  return sceneStore.currentScene
})

// 设备统计
const deviceData = computed(() => {
  return deviceStore.devices
})

const deviceCount = computed(() => {
  return deviceData.value.length
})

const onlineDevices = computed(() => {
  return deviceData.value.filter(device => device.status === 'online').length
})

const offlineDevices = computed(() => {
  return deviceData.value.filter(device => device.status === 'offline').length
})

const healthPercentage = computed(() => {
  return deviceCount.value > 0 
    ? Math.round((onlineDevices.value / deviceCount.value) * 100) 
    : 0
})

const healthStatus = computed(() => {
  if (healthPercentage.value >= 90) return 'success'
  if (healthPercentage.value >= 70) return 'warning'
  return 'exception'
})

// 初始化
onMounted(async () => {
  await loadData()
  
  // 监视自动刷新状态
  watch(autoRefresh, (newValue) => {
    if (newValue) {
      refreshTimer.value = setInterval(() => {
        refreshData()
      }, 5000) // 每5秒刷新一次
    } else if (refreshTimer.value) {
      clearInterval(refreshTimer.value)
      refreshTimer.value = null
    }
  })
})

// 组件卸载时清除定时器
onUnmounted(() => {
  if (refreshTimer.value) {
    clearInterval(refreshTimer.value)
  }
})

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const sceneId = parseInt(route.query.sceneId as string)
    if (sceneId) {
      await deviceStore.fetchDevices(sceneId)
    } else if (currentScene.value && currentScene.value.id) {
      await deviceStore.fetchDevices(currentScene.value.id)
    } else {
      await deviceStore.fetchDevices()
    }
  } finally {
    loading.value = false
  }
}

// 刷新数据
const refreshData = async () => {
  await loadData()
}

// 查看设备详情
const viewDeviceDetails = (device: Device) => {
  selectedDevice.value = device
  dialogVisible.value = true
}
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.status-card {
  margin-bottom: 20px;
}

.card-value {
  font-size: 28px;
  font-weight: bold;
  text-align: center;
  padding: 10px 0;
  color: #606266;
}

.monitor-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-placeholder {
  height: 300px;
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
