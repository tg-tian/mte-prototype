<template>
  <div class="scene-setting-container">
    <div class="scene-header">
      <h2>{{ isEditMode ? '编辑场景' : '创建场景' }} <small v-if="currentDomain">- {{ currentDomain.name }}</small></h2>
      <div class="header-actions">
        <el-button @click="navigateBack">返回列表</el-button>
        <el-button type="primary" @click="dialogVisible=true">发布</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
      </div>
    </div>
    <el-dialog
    v-model="dialogVisible"
    title="发布场景"
    width="500"
    >
    <el-input v-model="sceneForm.url" placeholder="请输入发布地址"></el-input>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="publishScene">
          确定
        </el-button>
      </div>
    </template>
    </el-dialog>
    
    <el-card class="setting-content">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-form 
            :model="sceneForm" 
            :rules="rules"
            ref="sceneFormRef"
            label-width="120px">
            <el-form-item label="场景编码" prop="code">
              <el-input v-model="sceneForm.code" placeholder="请输入场景编码"></el-input>
            </el-form-item>
            <el-form-item label="场景名称" prop="name">
              <el-input v-model="sceneForm.name" placeholder="请输入场景名称"></el-input>
            </el-form-item>
            <el-form-item label="场景描述" prop="description">
              <el-input type="textarea" :rows="3" v-model="sceneForm.description" placeholder="请输入场景描述"></el-input>
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-tag :type="sceneForm.status==='active' ? 'success':'info'">{{ sceneForm.status==='active' ? '已发布':'定制中' }}</el-tag>
            </el-form-item>
            <el-form-item label="场景地址" prop="url" v-if="sceneForm.status==='active'">
              <el-input v-model="sceneForm.url" disabled></el-input>
            </el-form-item>
            
            <el-form-item label="地理位置">
              <div class="location-fields">
                <el-input-number
                  v-model="sceneForm.lng"
                  :precision="6"
                  :step="0.000001"
                  :min="-180"
                  :max="180"
                  placeholder="经度"
                  class="coordinate-input"
                ></el-input-number>
                <el-input-number
                  v-model="sceneForm.lat"
                  :precision="6"
                  :step="0.000001"
                  :min="-90"
                  :max="90"
                  placeholder="纬度"
                  class="coordinate-input"
                ></el-input-number>
              </div>
            </el-form-item>
            
            <el-form-item label="" v-if="sceneForm.lng && sceneForm.lat">
              <div id="location-map" ref="locationMap" class="location-map"></div>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="场景设备" name="device">
          <el-card class="device-search">
            <el-form :inline="true" :model="searchForm" class="search-form">
              <el-form-item label="设备名称">
                <el-input v-model="searchForm.name" placeholder="请输入设备名称" clearable></el-input>
              </el-form-item>
              <el-form-item label="类型">
                <el-select v-model="searchForm.type" placeholder="请选择类型" clearable>
                  <el-option label="传感器" value="传感器"></el-option>
                  <el-option label="监测器" value="监测器"></el-option>
                  <el-option label="控制设备" value="控制设备"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="状态">
                <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
                  <el-option label="在线" value="online"></el-option>
                  <el-option label="离线" value="offline"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleSearch">搜索</el-button>
                <el-button @click="resetSearch">重置</el-button>
              </el-form-item>
            </el-form>
          </el-card>
          <div v-if = "filteredDevices && filteredDevices.length>0">
            <el-table
                v-loading="deviceStore.loading"
                :data="filteredDevices"
                style="width: 100%; margin-top: 20px"
                border
            >
              <el-table-column prop="id" label="ID" width="80"></el-table-column>
              <el-table-column prop="deviceName" label="设备名称" min-width="150"></el-table-column>
              <el-table-column prop="type" label="类型" width="120"></el-table-column>
              <el-table-column prop="lastUpdated" label="最后更新" width="180"></el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template #default="scope">
                  <el-tag :type="scope.row.status === 'online' ? 'success' : 'danger'">
                    {{ scope.row.status === 'online' ? '在线' : '离线' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="220">
                <template #default="scope">
                  <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
                  <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <el-empty v-else description="暂无设备" />
        </el-tab-pane>
      </el-tabs>
    </el-card>
    <el-dialog
        v-model="deviceDialogVisible"
        :title="isEdit ? '编辑设备' : '添加设备'"
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
          <el-button @click="deviceDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitting">
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch, toRefs, nextTick } from 'vue'
import { useSceneStore } from '@/store/scene'
import { useDomainStore } from '@/store/domain'
import {ElMessage, ElMessageBox, type FormInstance} from 'element-plus'
import { getSceneById } from '@/api/scene'
import {useDeviceStore} from "@/store/device";

const router = useRouter()
const route = useRoute()
const sceneStore = useSceneStore()
const domainStore = useDomainStore()
const deviceStore = useDeviceStore()
const sceneFormRef = ref<FormInstance>()
const locationMap = ref<HTMLElement | null>(null)

// State
const state = reactive({
  activeTab: 'basic',
  searchForm: {
    name: '',
    type: '',
    status: ''
  },
  sceneForm: {
    code: '',
    name: '',
    description: '',
    status: 'inactive',
    domainId: parseInt(route.query.domainId as string) || null,
    lng: undefined as number | undefined,
    lat: undefined as number | undefined,
    url: ''
  },
  deviceForm: {
    name: '',
    type: '传感器',
    status: 'online',
    sceneId: parseInt(route.query.sceneId as string) || null,
    x: 100,
    y: 100
  },
  submitting: false,
  baiduMap: null as BMap.Map | null,
  locationMarker: null as BMap.Marker | null,
  dialogVisible: false,
  deviceDialogVisible:false,
  isEdit: false,
  currentId: null,
})

const { activeTab, sceneForm, submitting, baiduMap, locationMarker, dialogVisible,searchForm,isEdit,deviceForm,currentId,deviceDialogVisible } = toRefs(state)

// Determine if we're in edit mode
const isEditMode = computed(() => {
  return route.query.mode === 'edit'
})

// Get current scene ID from query params
const sceneId = computed(() => {
  return parseInt(route.query.sceneId as string) || null
})
const handleSearch = () => {
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm(
      `确定要删除设备 "${row.name}" 吗？`,
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
  )
      .then(async () => {
        try {
          await deviceStore.deleteDevice(row.id)
          ElMessage.success('删除成功')
        } catch (error) {
          ElMessage.error('删除失败')
        }
      })
      .catch(() => {
        // 用户取消操作
      })
}
const resetSearch = () => {
  searchForm.value.name = ''
  searchForm.value.type = ''
  searchForm.value.status = ''
}

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
  code: [
    { required: true, message: '请输入场景编码', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入场景名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入场景描述', trigger: 'blur' }
  ]
}

// Clear form for creation mode
const resetFormData = () => {
  sceneForm.value = {
    code: '',
    name: '',
    description: '',
    status: 'inactive',
    domainId: domainId.value,
    lng: undefined,
    lat: undefined,
    url: ''
  }
}

// 过滤后的设备列表
const filteredDevices = computed(() => {
  if (!deviceStore.devices) return []

  return deviceStore.devices.filter((device: any) => {
    const nameMatch = !searchForm.value.name || device.name.toLowerCase().includes(searchForm.value.name.toLowerCase())
    const typeMatch = !searchForm.value.type || device.type === searchForm.value.type
    const statusMatch = !searchForm.value.status || device.status === searchForm.value.status
    return nameMatch && typeMatch && statusMatch
  })
})

// Helper function to load scene data to form
const loadSceneToForm = (scene: any) => {
  // First reset the form to clear any previous data
  resetFormData()
  
  // Then load the scene data
  if (scene) {
    sceneForm.value.code = scene.code || scene.sceneCode || ''
    sceneForm.value.name = scene.name || scene.sceneName || ''
    sceneForm.value.description = scene.description || scene.sceneDescription || ''
    sceneForm.value.status = scene.status || 'active'
    sceneForm.value.domainId = scene.domainId || domainId.value
    
    // Load location if available
    // 首先尝试使用location对象
    if (scene.location) {
      sceneForm.value.lng = scene.location.lng
      sceneForm.value.lat = scene.location.lat
    } 
    // 如果没有location对象，尝试使用独立的经纬度字段
    else if (scene.longitude !== undefined && scene.latitude !== undefined) {
      sceneForm.value.lng = scene.longitude
      sceneForm.value.lat = scene.latitude
    }
    
    console.log('Scene data loaded to form:', sceneForm.value)
  }
}

const handleEdit = (row: any) => {
  isEdit.value = true
  deviceForm.value = {
    name: row.name,
    type: row.type,
    status: row.status,
    sceneId: row.sceneId,
    x: row.location ? row.location.x : 100,
    y: row.location ? row.location.y : 100
  }
  currentId.value = row.id
  deviceDialogVisible.value = true
}

// Initialize location map
const initLocationMap = () => {
  if (!locationMap.value) return
  
  // Create map instance
  baiduMap.value = new BMap.Map('location-map')
  
  // Set initial center and zoom
  let centerPoint: BMap.Point
  
  if (sceneForm.value.lng && sceneForm.value.lat) {
    centerPoint = new BMap.Point(sceneForm.value.lng, sceneForm.value.lat)
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
  //@ts-ignore
  baiduMap.value.addControl(navigationControl)
  
  // Add marker if coordinates are set
  if (sceneForm.value.lng && sceneForm.value.lat) {
    addLocationMarker()
  }
  
  // Allow clicking on map to set location
  //@ts-ignore
  baiduMap.value.addEventListener('click', (e: any) => {
    const clickPoint = e.point
    sceneForm.value.lng = clickPoint.lng
    sceneForm.value.lat = clickPoint.lat
    
    // Update marker
    addLocationMarker()
  })
}

const currentScene = computed(() => {
  return sceneStore.currentScene
})

// Add marker for location
const addLocationMarker = () => {
  if (!baiduMap.value || !sceneForm.value.lng || !sceneForm.value.lat) return
  
  // Clear any existing marker
  if (locationMarker.value) {
    baiduMap.value.removeOverlay(locationMarker.value)
  }
  
  // Create and add new marker
  const point = new BMap.Point(sceneForm.value.lng, sceneForm.value.lat)
  locationMarker.value = new BMap.Marker(point)
  baiduMap.value.addOverlay(locationMarker.value)
  
  // Center map on marker
  baiduMap.value.setCenter(point)
}

// Watch for changes in coordinates to update marker
watch([() => sceneForm.value.lng, () => sceneForm.value.lat], () => {
  if (sceneForm.value.lng && sceneForm.value.lat) {
    if (!baiduMap.value && locationMap.value) {
      nextTick(() => {
        initLocationMap()
      })
    } else if (baiduMap.value) {
      addLocationMarker()
    }
  }
})

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
      const res: any = await getSceneById(parseInt(newSceneId as string))
      if (res.data && res.status === 200) {
        sceneStore.setCurrentScene(res.data)
        loadSceneToForm(res.data)
        
        // Initialize map after data is loaded
        nextTick(() => {
          if (sceneForm.value.lng && sceneForm.value.lat) {
            initLocationMap()
          }
        })
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

  // Get scemeId
  const sceneId = parseInt(route.query.sceneId as string)

  // Clear form when in create mode
  if (!isEditMode.value) {
    resetFormData()
  }
  else if (isEditMode.value && sceneId) {
    const currentScene = sceneStore.currentScene
    
    if (currentScene && currentScene.id === sceneId) {
      // Load from current scene in store
      loadSceneToForm(currentScene)
      
      // Initialize map after data is loaded
      nextTick(() => {
        if (sceneForm.value.lng && sceneForm.value.lat) {
          initLocationMap()
        }
      })
    } else {
      // Try to fetch scene data from API if not in store
      try {
        const res: any = await getSceneById(sceneId)
        if (res.data && res.status === 200) {
          sceneStore.setCurrentScene(res.data)
          loadSceneToForm(res.data)
          
          // Initialize map after data is loaded
          nextTick(() => {
            if (sceneForm.value.lng && sceneForm.value.lat) {
              initLocationMap()
            }
          })
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
  if (sceneId) {
    await deviceStore.fetchDevices(sceneId)
  } else if (currentScene.value && currentScene.value.id) {
    await deviceStore.fetchDevices(currentScene.value.id)
  } else {
    await deviceStore.fetchDevices()
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
        // Create location object if coordinates are set
        const formData = { ...sceneForm.value }
        
        if (formData.lng && formData.lat) {
          //@ts-ignore
          formData.location = {
            lng: formData.lng,
            lat: formData.lat
          }
        }
        
        // Remove lng and lat properties as they're now in the location object
        delete formData.lng
        delete formData.lat
        
        if (isEditMode.value && sceneId.value) {
          // Update existing scene
          await sceneStore.updateScene(sceneId.value, formData)
          ElMessage.success('更新成功')
        } else {
          // Create new scene
          await sceneStore.createScene(formData)
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

const publishScene = () => {
  if(sceneForm.value.url){

  }else{
    ElMessage.warning('请输入url')
  }
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

.location-fields {
  display: flex;
  gap: 10px;
}

.coordinate-input {
  width: 180px;
}

.location-map {
  width: 100%;
  height: 300px;
  margin-top: 10px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}
.device-search {
  margin-bottom: 20px;
}
</style>
