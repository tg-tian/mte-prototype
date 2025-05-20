<template>
  <div class="scene-setting-container">
    <div class="scene-header">
      <h2>{{ isEditMode ? '编辑场景-'+sceneForm.name : '创建场景' }} </h2>
      <div class="header-actions">
        <el-button @click="navigateBack">返回列表</el-button>
        <el-button type="primary" @click="publishForm">{{sceneForm.status==='1' ? '取消发布':'发布'}}</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
        <el-button
            type="primary"
            plain
            v-if="isEditMode"
            @click="saveTemplate"
        >保存为模板</el-button>
        <el-button @click="handleDownload" v-if="isEditMode && sceneForm.status==='1'">下载发布制品</el-button>
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
            
            <el-form-item label="场景图片">
              <el-upload
                class="scene-image-uploader"
                action="/api/upload"
                :show-file-list="false"
                :on-success="handleImageSuccess"
                :before-upload="beforeImageUpload"
              >
                <img v-if="sceneForm.imageUrl" :src="sceneImageUrl" class="scene-image" />
                <el-icon v-else class="scene-uploader-icon"><Plus /></el-icon>
              </el-upload>
              <div class="el-upload__tip">
                支持 jpg/png 文件，大小不超过 5MB
              </div>
            </el-form-item>

            <el-form-item label="状态" prop="status">
              <el-tag :type="sceneForm.status==='1' ? 'success':'info'">{{ sceneForm.status==='1' ? '已发布':'定制中' }}</el-tag>
            </el-form-item>
            <el-form-item label="场景地址" prop="url" v-if="sceneForm.status==='1'">
              <el-input v-model="sceneForm.url"></el-input>
            </el-form-item>
            
            <el-form-item label="地理位置">
              <div class="location-fields">
                <el-input-number
                  v-model="sceneForm.lng"
                  :precision="6"
                  :step="0.000001"
                  :min="lngMIn"
                  :max="lngMax"
                  placeholder="经度"
                  class="coordinate-input"
                ></el-input-number>
                <el-input-number
                  v-model="sceneForm.lat"
                  :precision="6"
                  :step="0.000001"
                  :min="latMin"
                  :max="latMax"
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
              <el-form-item label="状态">
                <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
                  <el-option label="在线" :value="Number(1)"></el-option>
                  <el-option label="离线" :value="Number(0)"></el-option>
                  <el-option label="未激活" :value="Number(2)"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleSearch">搜索</el-button>
                <el-button @click="resetSearch">重置</el-button>
              </el-form-item>
            </el-form>
          </el-card>
          <!-- 新增：添加设备按钮 -->
          <div class="device-actions" style="margin:10px 0;">
            <el-button type="primary" @click="handleAddDevice">添加设备</el-button>
          </div>
          <div v-if="filteredDevices && filteredDevices.length>0">
            <el-table
                v-loading="deviceStore.loading"
                :data="filteredDevices"
                style="width: 100%; margin-top: 20px"
                border
            >
              <el-table-column prop="deviceCode" label="设备编码" width="150"></el-table-column>
              <el-table-column prop="deviceName" label="设备名称" min-width="150"></el-table-column>
              <el-table-column prop="deviceType.name" label="设备类型" width="120"></el-table-column>
              <el-table-column prop="protocolType" label="协议类型" width="120"></el-table-column>
              <el-table-column prop="deviceLocation" label="设备位置" width="120"></el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
              <el-table-column prop="lastOnlineTime" label="最后上线时间" width="150"></el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template #default="scope">
                  <el-tag v-if="scope.row.status === 1" type="success">在线</el-tag>
                  <el-tag v-else-if="scope.row.status === 0" type="danger">离线</el-tag>
                  <el-tag v-else type="info">未激活</el-tag>
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

        <el-tab-pane label="场景区域" name="area">
          <div v-if="filteredAreas && filteredAreas.length > 0">
            <el-table
              v-loading="areaStore.loading"
              :data="filteredAreas"
              style="width: 100%; margin-top: 20px"
              border
            >
              <el-table-column prop="name" label="区域名称" min-width="150"></el-table-column>
              <el-table-column prop="description" label="区域描述" min-width="200"></el-table-column>
              <!-- <el-table-column prop="position" label="位置" min-width="150"></el-table-column> -->
            </el-table>
          </div>
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
          :rules="deviceRules"
          ref="deviceFormRef"
      >
        <el-form-item label="设备编码" prop="code">
          <el-input v-model="deviceForm.code" placeholder="请输入设备编码"></el-input>
        </el-form-item>
        <el-form-item label="设备名称" prop="name">
          <el-input v-model="deviceForm.name" placeholder="请输入设备名称"></el-input>
        </el-form-item>
        <el-form-item label="设备类型" prop="deviceTypeId">
          <el-select v-model="deviceForm.deviceTypeId" placeholder="请选择设备类型" >
            <el-option v-for="(item, index) in deviceTypeList" :value="item.id" :label="item.name" :key="item.code"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="协议类型" prop="protocolType">
          <el-select v-model="deviceForm.protocolType" placeholder="请选择协议类型" >
            <el-option label="MQTT" value="MQTT"></el-option>
            <el-option label="HTTP" value="HTTP"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="设备位置" prop="deviceLocation">
          <el-input v-model="deviceForm.deviceLocation" placeholder="请输入设备位置"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deviceDialogVisible = false">取消</el-button>
          <!-- 调整为调用 submitDeviceForm -->
          <el-button type="primary" @click="submitDeviceForm" :loading="submitting">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch, toRefs, nextTick } from 'vue'
import { useSceneStore } from '@/store/scene'
import { useDomainStore } from '@/store/domain'
import { useDeviceStore } from '@/store/device'
import { useAreaStore } from '@/store/area'
import { useSceneTemplateStore} from "@/store/sceneTemplate";
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getSceneById } from '@/api/scene'
import { useRouter, useRoute } from 'vue-router'
import { Device } from '@/types/models'
import axios from "axios";

const router = useRouter()
const route = useRoute()
const sceneStore = useSceneStore()
const domainStore = useDomainStore()
const deviceStore = useDeviceStore()
const areaStore = useAreaStore()
const sceneTemplateStore = useSceneTemplateStore()
const sceneFormRef = ref<FormInstance>()
const deviceFormRef = ref<FormInstance>()           // 新增：设备对话框表单引用
const locationMap = ref<HTMLElement | null>(null)
const lngMIn = 73
const lngMax = 135
const latMin = 3
const latMax = 53
// State
const state = reactive({
  activeTab: 'basic',
  searchForm: {
    name: '',
    status: ''
  },
  sceneForm: {
    code: '',
    name: '',
    description: '',
    status: '0',
    domainId: parseInt(route.query.domainId as string) || null,
    lng: undefined as number | undefined,
    lat: undefined as number | undefined,
    url: '',
    imageUrl: '',
  },
  deviceForm: {
    code: '',
    name: '',
    deviceTypeId: -1,
    protocolType: 'MQTT',
    sceneId: parseInt(route.query.sceneId as string) || null,
    deviceLocation: '',
  },
  submitting: false,
  baiduMap: null as BMap.Map | null,
  locationMarker: null as BMap.Marker | null,
  dialogVisible: false,
  deviceDialogVisible:false,
  isEdit: false,
  currentId: null,
  deviceTypeList: []
})

const { activeTab, sceneForm, submitting, baiduMap, locationMarker, dialogVisible,searchForm,isEdit,deviceForm,currentId,deviceDialogVisible, deviceTypeList } = toRefs(state)

// Determine if we're in edit mode
const isEditMode = computed(() => {
  return route.query.mode === 'edit'
})

// Get current scene ID from query params
const sceneId = computed(() => {
  return parseInt(route.query.sceneId as string) || null
})

const sceneImageUrl = computed(() => {
  const imageUrl = (import.meta.env.VITE_BASE_PATH as string) + sceneForm.value.imageUrl
  console.log("image url", imageUrl)
  return imageUrl
})

// 新增方法：打开新增设备对话框
const handleAddDevice = () => {
  isEdit.value = false
  currentId.value = null
  deviceForm.value = {
    code: '',
    name: '',
    deviceTypeId: null,
    protocolType: 'MQTT',
    sceneId: sceneId.value,
    deviceLocation: null,
  }
  deviceDialogVisible.value = true
}

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
      },
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
  ],
  imageUrl: [
    { required: false }
  ]
}

const deviceRules = {
  code: [
    { required: true, message: '请输入设备编码', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入设备名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  deviceTypeId: [
    { required: true, message: '请输入设备类型', trigger: 'blur' }
  ],
  protocolType: [
    { required: true, message: '请输入协议类型', trigger: 'blur' }
  ],
  deviceLocation: [
    { required: true, message: '请输入设备位置', trigger: 'blur' }
  ]
}

// Clear form for creation mode
const resetFormData = () => {
  sceneForm.value = {
    code: '',
    name: '',
    description: '',
    status: '0',
    domainId: domainId.value,
    lng: undefined,
    lat: undefined,
    url: '',
    imageUrl: "",
  }
}

// 过滤后的设备列表
const filteredDevices = computed(() => {
  if (!deviceStore.devices) return []

  return deviceStore.devices.filter((device: any) => {
    const nameMatch = !searchForm.value.name || device.deviceName.toLowerCase().includes(searchForm.value.name.toLowerCase())
    const statusMatch = !searchForm.value.status || device.status === searchForm.value.status
    return nameMatch && statusMatch
  }).map((device)=>{
    return {
      ...device,
      createTime: device.createTime?.split('.')[0].replace('T', ' '),
      updateTime: device.updateTime?.split('.')[0].replace('T', ' '),
      lastOnlineTime: device.lastOnlineTime?.split('.')[0].replace('T', ' ')
    }
  })
})

// 获取区域列表
const filteredAreas = computed(() => {
  if (!areaStore.areas) return [];
  return areaStore.areas.map((area: any) => ({
    ...area,
    createTime: area.createTime?.split('.')[0].replace('T', ' '),
    updateTime: area.updateTime?.split('.')[0].replace('T', ' ')
  }));
});

// Helper function to load scene data to form
const loadSceneToForm = (scene: any) => {
  // First reset the form to clear any previous data
  resetFormData()
  
  // Then load the scene data
  if (scene) {
    sceneForm.value.code = scene.code || scene.sceneCode || ''
    sceneForm.value.name = scene.name || scene.sceneName || ''
    sceneForm.value.description = scene.description || scene.sceneDescription || ''
    sceneForm.value.status = scene.status || '1'
    sceneForm.value.domainId = scene.domainId || domainId.value
    sceneForm.value.url = scene.url || ''
    sceneForm.value.imageUrl = scene.imageUrl || ''
    
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

const handleEdit = (row: Device) => {
  isEdit.value = true
  deviceForm.value = {
    code: row.deviceCode,
    name: row.deviceName,
    deviceTypeId: row.deviceTypeId,
    protocolType: row.protocolType,
    sceneId: row.sceneId,
    deviceLocation: row.deviceLocation,
  }
  currentId.value = row.id
  deviceDialogVisible.value = true
}

// 新增方法：提交设备表单
const submitDeviceForm = async () => {
  if (!deviceFormRef.value) return        // 现在可以访问到正确的 ref
  await deviceFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (isEdit.value && currentId.value !== null) {
          await deviceStore.updateDevice(currentId.value, { ...deviceForm.value })
          ElMessage.success('更新设备成功')
        } else {
          await deviceStore.createDevice({ ...deviceForm.value })
          ElMessage.success('创建设备成功')
        }
        // 刷新列表
        await deviceStore.fetchDevices(sceneId.value)
        deviceDialogVisible.value = false
      } catch {
        ElMessage.error(isEdit.value ? '更新设备失败' : '创建设备失败')
      } finally {
        submitting.value = false
      }
    }
  })
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
  
  baiduMap.value.centerAndZoom(centerPoint, 18)
  
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
    if (!baiduMap.value) {
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
  activeTab.value = "basic"
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
        deviceTypeList.value = await sceneStore.getSceneDeviceTypes(res.data.sceneId)
        await deviceStore.fetchDevices(newSceneId ? parseInt(newSceneId as string) : undefined)
        await areaStore.fetchAreas(newSceneId ? parseInt(newSceneId as string) : undefined)
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
  activeTab.value = "basic"
  // Get scemeId
  const sceneId = parseInt(route.query.sceneId as string)

  // Clear form when in create mode
  if (!isEditMode.value) {
    resetFormData()
  }
  else if (isEditMode.value && sceneId) {
    const currentScene = sceneStore.currentScene

    deviceTypeList.value = await sceneStore.getSceneDeviceTypes(sceneId)
    
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
    // 根据场景 ID 加载区域数据
    await areaStore.fetchAreas(sceneId);
  }else {
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
  if (!sceneFormRef.value) return;

  await sceneFormRef.value.validate(async (valid) => {
    if (valid) {
      if(sceneForm.value.lng && (sceneForm.value.lng < lngMIn || sceneForm.value.lng > lngMax)){
        ElMessage.error('经度范围不合法, 73-135')
        return
      }
      if(sceneForm.value.lat && (sceneForm.value.lat < latMin || sceneForm.value.lat > latMax)){
        ElMessage.error('纬度范围不合法, 3-53')
        return
      }
      submitting.value = true;
      try {
        if(isEditMode.value){
          await sceneStore.updateScene(sceneId.value, sceneForm.value)
          ElMessage.success("保存成功")
        }else{
          await sceneStore.createScene(sceneForm.value);
          ElMessage.success('创建成功');
          navigateBack();
        }
      } catch (error) {
        console.error('提交失败:', error);
        ElMessage.error(error.response?.data || '保存失败，请检查数据是否冲突');
      } finally {
        submitting.value = false;
      }
    }
  });
};

const publishForm = async()=>{
  if (!sceneFormRef.value) return
  
  await sceneFormRef.value.validate(async (valid) => {
    if (valid) {
      if(sceneForm.value.status === '1') {
        sceneStore.publishScene(domainId.value, sceneId.value, '', '0')
        .then((res)=>{
          ElMessage.success('取消发布成功')
          loadSceneToForm(res)
        }) 
        .catch((error)=>{
          ElMessage.error('取消发布失败:', error)
        }) 
      }else{
        dialogVisible.value=true
      }
    }
  })
}

const saveTemplate = async () => {
  if (!sceneFormRef.value) return
  await sceneFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await sceneTemplateStore.saveSceneTemplate(
            sceneStore.currentScene,
            deviceStore.devices,
        )
        ElMessage.success('保存模版成功')
      } catch (error) {
        console.error('保存模版失败:', error)
        ElMessage.error('保存模版失败')
      }
    }
  })
}

const publishScene = () => {
  if(sceneForm.value.url){
    const locationData = sceneForm.value.lng && sceneForm.value.lat ? { lng: sceneForm.value.lng, lat: sceneForm.value.lat } : undefined
    let dslData = {
        sceneData: {
          ...sceneForm.value,
          location: locationData
        },
        devices: deviceStore.devices,
        locations: []
    }
    sceneStore.publishScene(domainId.value, sceneId.value, sceneForm.value.url, '1', dslData)
    .then((res)=>{
        ElMessage.success('发布成功')
        loadSceneToForm(res)
        dialogVisible.value = false
      }
    ).catch((error)=>{
      ElMessage.error('发布失败:', error)
    })
  }else{
    ElMessage.warning('请输入url')
  }
}

// 下载发布制品
const handleDownload =async () => {
  axios.get(`${import.meta.env.VITE_BASE_PATH as string}/scenes/download/${sceneId.value}`, {
    responseType: 'blob'
  }).then(response => {
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `${sceneForm.value.code}.json`);
    document.body.appendChild(link);
    link.click();
    link.remove();
    ElMessage.success("文件正在下载中")
  });
}

// 添加到 script setup 部分
const handleImageSuccess = (res: any) => {
  sceneForm.value.imageUrl = res;
  ElMessage.success("图片上传成功")
}

const beforeImageUpload = (file: File) => {
  const isImage = file.type === 'image/jpeg' || file.type === 'image/png';
  const isLt5M = file.size / 1024 / 1024 < 5;

  if (!isImage) {
    ElMessage.error('只能上传 JPG/PNG 格式的图片!');
    return false;
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB!');
    return false;
  }
  return true;
}

// 编辑区域
const handleEditArea = (row: any) => {
  console.log('编辑区域:', row);
  // 在这里实现编辑区域的逻辑
};

// 删除区域
const handleDeleteArea = (row: any) => {
  ElMessageBox.confirm(
    `确定要删除区域 "${row.name}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
    .then(async () => {
      try {
        await areaStore.deleteArea(row.id);
        ElMessage.success('删除成功');
      } catch (error) {
        ElMessage.error('删除失败');
      }
    })
    .catch(() => {
      // 用户取消操作
    });
};
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
.scene-image-uploader .scene-image {
  width: 200px;
  height: 200px;
  object-fit: cover;
}
.scene-uploader-icon {
  width: 200px;
  height: 200px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  font-size: 28px;
  color: #8c939d;
  text-align: center;
  line-height: 200px;
}
</style>
