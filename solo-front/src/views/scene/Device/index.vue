<template>
  <div class="device-list-container">
    <div class="device-header">
      <h2>设备列表 <small v-if="currentScene">- {{ currentScene.name }}</small></h2>
      <el-button type="primary" @click="handleCreate">添加设备</el-button>
    </div>
    
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
    
    <el-table
      v-loading="deviceStore.loading"
      :data="filteredDevices"
      style="width: 100%; margin-top: 20px"
      border
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
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 创建/编辑设备的对话框 -->
    <el-dialog
      v-model="dialogVisible"
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
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitting">
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
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus'

const router = useRouter()
const route = useRoute()
const deviceStore = useDeviceStore()
const sceneStore = useSceneStore()

// 表单引用
const deviceFormRef = ref<FormInstance>()

// 状态
const state = reactive({
  searchForm: {
    name: '',
    type: '',
    status: ''
  },
  deviceForm: {
    name: '',
    type: '传感器',
    status: 'online',
    sceneId: parseInt(route.query.sceneId as string) || null,
    x: 100,
    y: 100
  },
  dialogVisible: false,
  isEdit: false,
  currentId: null,
  submitting: false
})

const { 
  searchForm, deviceForm, dialogVisible, isEdit, currentId, submitting 
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

// 搜索处理
const handleSearch = () => {
  // 过滤是在计算属性中完成的
}

// 重置搜索
const resetSearch = () => {
  searchForm.value.name = ''
  searchForm.value.type = ''
  searchForm.value.status = ''
}

// 打开创建对话框
const handleCreate = () => {
  isEdit.value = false
  deviceForm.value = {
    name: '',
    type: '传感器',
    status: 'online',
    sceneId: parseInt(route.query.sceneId as string) || (currentScene.value ? currentScene.value.id : null),
    x: 100,
    y: 100
  }
  currentId.value = null
  dialogVisible.value = true
}

// 打开编辑对话框
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
  dialogVisible.value = true
}

// 删除设备
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

// 提交表单
const submitForm = async () => {
  if (!deviceFormRef.value) return
  
  await deviceFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
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
        
        if (isEdit.value && currentId.value) {
          await deviceStore.updateDevice(currentId.value, formData)
          ElMessage.success('更新成功')
        } else {
          await deviceStore.createDevice(formData)
          ElMessage.success('创建成功')
        }
        dialogVisible.value = false
      } catch (error) {
        ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
      } finally {
        submitting.value = false
      }
    }
  })
}
</script>

<style scoped>
.device-list-container {
  padding: 20px;
}

.device-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.device-search {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
}
</style>
