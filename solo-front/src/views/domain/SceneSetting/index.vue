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
              <el-select v-model="sceneForm.status" placeholder="请选择状态">
                <el-option label="已发布" value="active"></el-option>
                <el-option label="定制中" value="inactive"></el-option>
              </el-select>
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
          <el-empty description="暂无设备" />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch, toRefs, nextTick } from 'vue'
import { useSceneStore } from '@/store/scene'
import { useDomainStore } from '@/store/domain'
import { ElMessage, type FormInstance } from 'element-plus'
import { getSceneById } from '@/api/scene'

const router = useRouter()
const route = useRoute()
const sceneStore = useSceneStore()
const domainStore = useDomainStore()
const sceneFormRef = ref<FormInstance>()
const locationMap = ref<HTMLElement | null>(null)

// State
const state = reactive({
  activeTab: 'basic',
  sceneForm: {
    code: '',
    name: '',
    description: '',
    status: 'active',
    domainId: parseInt(route.query.domainId as string) || null,
    lng: undefined as number | undefined,
    lat: undefined as number | undefined
  },
  submitting: false,
  baiduMap: null as BMap.Map | null,
  locationMarker: null as BMap.Marker | null
})

const { activeTab, sceneForm, submitting, baiduMap, locationMarker } = toRefs(state)

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
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// Clear form for creation mode
const resetFormData = () => {
  sceneForm.value = {
    code: '',
    name: '',
    description: '',
    status: 'active',
    domainId: domainId.value,
    lng: undefined,
    lat: undefined
  }
}

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
  // Clear form when in create mode
  if (!isEditMode.value) {
    resetFormData()
  }
  else if (isEditMode.value && sceneId.value) {
    const currentScene = sceneStore.currentScene
    
    if (currentScene && currentScene.id === sceneId.value) {
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
        const res: any = await getSceneById(sceneId.value)
        if (res.data && res.data.code === 200 && res.data.data) {
          sceneStore.setCurrentScene(res.data.data)
          loadSceneToForm(res.data.data)
          
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
</style>
