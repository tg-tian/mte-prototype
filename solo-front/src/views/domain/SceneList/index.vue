<template>
  <div class="scene-list-container">
    <div class="scene-header">
      <h2>场景列表 <small v-if="currentDomain">- {{ currentDomain.name }}</small></h2>
      <div class="header-actions">
        <el-button type="primary" @click="navigateToSceneSetting()">创建场景</el-button>
        <el-button @click="toggleViewMode">
          {{ isMapView ? '切换到列表视图' : '切换到地图视图' }}
        </el-button>
      </div>
    </div>
    
    <el-card class="scene-search">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="场景名称">
          <el-input v-model="searchForm.name" placeholder="请输入场景名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="活跃" value="active"></el-option>
            <el-option label="非活跃" value="inactive"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- Map View -->
    <div v-if="isMapView" class="map-container">
      <div id="map-canvas" ref="mapCanvas"></div>
    </div>
    
    <!-- List View -->
    <el-table
      v-else
      v-loading="sceneStore.loading"
      :data="filteredScenes"
      style="width: 100%; margin-top: 20px"
      border
    >
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="场景名称" min-width="150"></el-table-column>
      <el-table-column prop="description" label="描述" min-width="200"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="120"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="120"></el-table-column>
      <el-table-column prop="deviceCount" label="设备数量" width="100"></el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 'active' ? 'success' : 'info'">
            {{ scope.row.status === 'active' ? '活跃' : '非活跃' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <el-button type="primary" size="small" @click="navigateToSceneSetting(scope.row)">编辑</el-button>
          <el-button type="success" size="small" @click="handleViewScene(scope.row)">进入场景</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, onUnmounted, toRefs, nextTick } from 'vue'
import { useSceneStore } from '@/store/scene'
import { useDomainStore } from '@/store/domain'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Scene } from '@/types/models'

const router = useRouter()
const route = useRoute()
const sceneStore = useSceneStore()
const domainStore = useDomainStore()
const mapCanvas = ref<HTMLElement | null>(null)

// 状态
const state = reactive({
  searchForm: {
    name: '',
    status: ''
  },
  dialogVisible: false,
  currentId: null,
  isMapView: true, // Default to map view
  baiduMap: null as BMap.Map | null,
  markers: [] as BMap.Marker[],
  infoWindow: null as BMap.InfoWindow | null
})

const { searchForm, dialogVisible, currentId, isMapView, baiduMap, markers, infoWindow } = toRefs(state)

// 获取当前域
const currentDomain = computed(() => {
  return domainStore.currentDomain
})

// 过滤后的场景列表
const filteredScenes = computed(() => {
  if (!sceneStore.scenes) return []
  
  return sceneStore.scenes.filter((scene: any) => {
    const nameMatch = !searchForm.value.name || scene.name.toLowerCase().includes(searchForm.value.name.toLowerCase())
    const statusMatch = !searchForm.value.status || scene.status === searchForm.value.status
    return nameMatch && statusMatch
  })
})

// 初始化
onMounted(async () => {
  // Get domainId parameter
  const domainId = parseInt(route.query.domainId as string)
  if (domainId) {
    await sceneStore.fetchScenes(domainId)
  } else {
    await sceneStore.fetchScenes()
  }
  
  // Initialize map after data is loaded
  nextTick(() => {
    if (isMapView.value) {
      initMap()
    }
  })
})

// Clean up on component unmount
onUnmounted(() => {
  // Clean up map resources if needed
  if (baiduMap.value) {
    baiduMap.value.clearOverlays()
  }
})

// Toggle between map view and list view
const toggleViewMode = () => {
  isMapView.value = !isMapView.value
  
  if (isMapView.value && !baiduMap.value) {
    nextTick(() => {
      initMap()
    })
  }
}

// Initialize Baidu Map
const initMap = () => {
  if (!mapCanvas.value) return
  
  // Create map instance
  baiduMap.value = new BMap.Map('map-canvas')
  
  // Set initial center and zoom
  let centerPoint: BMap.Point
  
  // Check if we have scenes with locations
  const scenesWithLocation = filteredScenes.value.filter(
    (scene: Scene) => scene.location && scene.location.lng && scene.location.lat
  )
  
  if (scenesWithLocation.length > 0) {
    // Use first scene with location as center
    const firstScene = scenesWithLocation[0]
    centerPoint = new BMap.Point(firstScene.location!.lng, firstScene.location!.lat)
  } else {
    // Default to Shanghai as center
    centerPoint = new BMap.Point(121.4737, 31.2304)
  }
  
  baiduMap.value.centerAndZoom(centerPoint, 12)
  
  // Enable scroll wheel zoom
  baiduMap.value.enableScrollWheelZoom()
  
  // Add navigation control
  const navigationControl = new BMap.NavigationControl({
    type: BMAP_NAVIGATION_CONTROL_LARGE
  })
  baiduMap.value.addControl(navigationControl)
  
  // Add scale control
  const scaleControl = new BMap.ScaleControl()
  baiduMap.value.addControl(scaleControl)
  
  // Add markers for each scene
  addSceneMarkers()
}

// Add markers for scenes
const addSceneMarkers = () => {
  if (!baiduMap.value) return
  
  // Clear any existing markers
  baiduMap.value.clearOverlays()
  markers.value = []
  
  // Create info window for markers
  infoWindow.value = new BMap.InfoWindow('', {
    width: 300,
    height: 140,
    enableAutoPan: true
  })
  
  // Add a marker for each scene with location
  filteredScenes.value.forEach((scene: Scene) => {
    if (scene.location && scene.location.lng && scene.location.lat) {
      const point = new BMap.Point(scene.location.lng, scene.location.lat)
      const marker = new BMap.Marker(point)
      
      // Add marker to map
      baiduMap.value?.addOverlay(marker)
      markers.value.push(marker)
      
      // Add click listener to marker
      marker.addEventListener('click', () => {
        showSceneInfo(scene, marker)
      })
    }
  })
}

// Show scene info when marker is clicked
const showSceneInfo = (scene: Scene, marker: BMap.Marker) => {
  if (!infoWindow.value || !baiduMap.value) return
  
  // Create info window content
  const content = document.createElement('div')
  content.className = 'map-info-window'
  
  // Scene title
  const title = document.createElement('h3')
  title.textContent = scene.name
  title.style.marginBottom = '5px'
  content.appendChild(title)
  
  // Scene description
  const description = document.createElement('p')
  description.textContent = scene.description
  description.style.fontSize = '12px'
  description.style.marginBottom = '10px'
  content.appendChild(description)
  
  // Status
  const statusContainer = document.createElement('div')
  statusContainer.style.marginBottom = '10px'
  
  const statusLabel = document.createElement('span')
  statusLabel.textContent = '状态: '
  statusContainer.appendChild(statusLabel)
  
  const statusValue = document.createElement('span')
  statusValue.textContent = scene.status === 'active' ? '活跃' : '非活跃'
  statusValue.style.padding = '2px 6px'
  statusValue.style.borderRadius = '4px'
  statusValue.style.backgroundColor = scene.status === 'active' ? '#67C23A' : '#909399'
  statusValue.style.color = 'white'
  statusValue.style.fontSize = '12px'
  statusContainer.appendChild(statusValue)
  
  content.appendChild(statusContainer)
  
  // Action buttons
  const actionsContainer = document.createElement('div')
  actionsContainer.style.display = 'flex'
  actionsContainer.style.justifyContent = 'space-between'
  
  // Edit button
  const editButton = document.createElement('button')
  editButton.textContent = '编辑'
  editButton.style.backgroundColor = '#409EFF'
  editButton.style.color = 'white'
  editButton.style.border = 'none'
  editButton.style.padding = '5px 10px'
  editButton.style.borderRadius = '4px'
  editButton.style.cursor = 'pointer'
  editButton.onclick = (e) => {
    e.preventDefault()
    navigateToSceneSetting(scene)
  }
  actionsContainer.appendChild(editButton)
  
  // Enter button
  const enterButton = document.createElement('button')
  enterButton.textContent = '进入场景'
  enterButton.style.backgroundColor = '#67C23A'
  enterButton.style.color = 'white'
  enterButton.style.border = 'none'
  enterButton.style.padding = '5px 10px'
  enterButton.style.borderRadius = '4px'
  enterButton.style.cursor = 'pointer'
  enterButton.onclick = (e) => {
    e.preventDefault()
    handleViewScene(scene)
  }
  actionsContainer.appendChild(enterButton)
  
  // Delete button
  const deleteButton = document.createElement('button')
  deleteButton.textContent = '删除'
  deleteButton.style.backgroundColor = '#F56C6C'
  deleteButton.style.color = 'white'
  deleteButton.style.border = 'none'
  deleteButton.style.padding = '5px 10px'
  deleteButton.style.borderRadius = '4px'
  deleteButton.style.cursor = 'pointer'
  deleteButton.onclick = (e) => {
    e.preventDefault()
    baiduMap.value?.closeInfoWindow() // Close info window first
    handleDelete(scene)
  }
  actionsContainer.appendChild(deleteButton)
  
  content.appendChild(actionsContainer)
  
  // Set info window content and open it
  infoWindow.value.setContent(content)
  marker.openInfoWindow(infoWindow.value)
}

// Refresh map markers when search criteria change
watch([() => searchForm.value.name, () => searchForm.value.status], () => {
  if (isMapView.value && baiduMap.value) {
    nextTick(() => {
      addSceneMarkers()
    })
  }
})

// 搜索处理
const handleSearch = () => {
  if (isMapView.value && baiduMap.value) {
    addSceneMarkers()
  }
}

// 重置搜索
const resetSearch = () => {
  searchForm.value.name = ''
  searchForm.value.status = ''
  
  if (isMapView.value && baiduMap.value) {
    addSceneMarkers()
  }
}

// 导航到场景设置页面
const navigateToSceneSetting = (scene?: any) => {
  const domainId = parseInt(route.query.domainId as string) || 
                  (currentDomain.value ? currentDomain.value.id : null)
                  
  if (!domainId) {
    ElMessage.warning('请先选择一个领域')
    return
  }
  
  if (scene) {
    // For editing, set the current scene and navigate with scene ID
    sceneStore.setCurrentScene(scene)
    router.push(`/domain/scene/setting?sceneId=${scene.id}&domainId=${domainId}&mode=edit`)
  } else {
    // For creation, navigate to setting page with domain ID
    router.push(`/domain/scene/setting?domainId=${domainId}&mode=create`)
  }
}

// 查看场景
const handleViewScene = (row: any) => {
  sceneStore.setCurrentScene(row)
  router.push(`/scene/information?sceneId=${row.id}`)
}

// 删除场景
const handleDelete = (row: any) => {
  ElMessageBox.confirm(
    `确定要删除场景 "${row.name}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
  .then(async () => {
    try {
      await sceneStore.deleteScene(row.id)
      ElMessage.success('删除成功')
      
      // Refresh map markers after deletion
      if (isMapView.value && baiduMap.value) {
        addSceneMarkers()
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
  .catch(() => {
    // 用户取消操作
  })
}
</script>

<style scoped>
.scene-list-container {
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

.scene-search {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
}

.map-container {
  width: 100%;
  height: 600px;
  margin-top: 20px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

#map-canvas {
  width: 100%;
  height: 100%;
}

:deep(.BMap_bubble_content) {
  overflow: hidden;
}

:deep(.map-info-window) {
  font-family: 'Arial', sans-serif;
  padding: 5px;
}
</style>
