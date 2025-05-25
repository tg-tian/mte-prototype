<template>
  <div class="device-type-setting">
    <div class="devicetype-header">
      <h2>{{ isEditMode ? '编辑设备类型' : '创建设备类型' }}</h2>
      <div class="header-actions">
        <el-button @click="navigateBack">返回列表</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
      </div>
    </div>

    <el-card class="setting-content">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-form 
            :model="deviceTypeForm" 
            :rules="basicRules"
            ref="deviceTypeFormRef"
            label-width="120px">
            <el-form-item label="设备类型编码" prop="code">
              <el-input v-model="deviceTypeForm.code" placeholder="请输入设备类型编码"></el-input>
            </el-form-item>
            <el-form-item label="设备类型名称" prop="name">
              <el-input v-model="deviceTypeForm.name" placeholder="请输入设备类型名称"></el-input>
            </el-form-item>
            <el-form-item label="设备类型描述" prop="description">
              <el-input type="textarea" :rows="3" v-model="deviceTypeForm.description" placeholder="请输入设备类型描述"></el-input>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="模型定义" name="model" :disabled="!isEditMode && !currentDeviceTypeId">
          <div v-if="isEditMode || currentDeviceTypeId">
            <!-- 属性定义 -->
            <div class="model-section">
              <div class="section-header">
                <h3>属性</h3>
                <div class="section-actions">
                  <el-button type="primary" size="small" @click="addProperty">添加属性</el-button>
                  <el-button type="primary" size="small" @click="showModelJson">查看JSON</el-button>
                </div>
              </div>
              
              <el-table :data="modelForm.properties" border style="width: 100%; margin-top: 10px;">
                <el-table-column prop="identify" label="标识符" width="180"></el-table-column>
                <el-table-column prop="name" label="名称" width="180"></el-table-column>
                <el-table-column prop="accessMode" label="读写类型" width="120">
                  <template #default="scope">
                    {{ getAccessModeText(scope.row.accessMode) }}
                  </template>
                </el-table-column>
                <el-table-column prop="dataType.type" label="数据类型" width="120"></el-table-column>
                <el-table-column prop="dataType.specs" label="规格">
                  <template #default="scope">
                    {{ formatDataTypeSpecs(scope.row.dataType) }}
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150">
                  <template #default="scope">
                    <el-button type="primary" size="small" @click="editProperty(scope.row, scope.$index)">编辑</el-button>
                    <el-button type="danger" size="small" @click="removeProperty(scope.$index)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
              
              <el-empty v-if="modelForm.properties.length === 0" description="暂无属性"></el-empty>
            </div>
            
            <!-- 服务定义 -->
            <div class="model-section">
              <div class="section-header">
                <h3>服务</h3>
                <el-button type="primary" size="small" @click="addService">添加服务</el-button>
              </div>
              <el-table :data="modelForm.services" border style="width: 100%; margin-top: 10px;">
                <el-table-column prop="identify" label="标识符" width="180"></el-table-column>
                <el-table-column prop="name" label="名称" width="180"></el-table-column>
                <el-table-column label="输入参数" width="120">
                  <template #default="scope">
                    {{ scope.row.inputData.length }} 个参数
                  </template>
                </el-table-column>
                <el-table-column label="输出参数" width="120">
                  <template #default="scope">
                    {{ scope.row.outputData.length }} 个参数
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150">
                  <template #default="scope">
                    <el-button type="primary" size="small" @click="editService(scope.row, scope.$index)">编辑</el-button>
                    <el-button type="danger" size="small" @click="removeService(scope.$index)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
              
              <el-empty v-if="modelForm.services.length === 0" description="暂无服务"></el-empty>
            </div>
            
            <!-- 事件定义 -->
            <div class="model-section">
              <div class="section-header">
                <h3>事件</h3>
                <el-button type="primary" size="small" @click="addEvent">添加事件</el-button>
              </div>
              <el-table :data="modelForm.events" border style="width: 100%; margin-top: 10px;">
                <el-table-column prop="identify" label="标识符" width="180"></el-table-column>
                <el-table-column prop="name" label="名称" width="180"></el-table-column>
                <el-table-column prop="type" label="事件类型" width="120">
                  <template #default="scope">
                    <el-tag :type="getEventTypeTag(scope.row.type)">{{ getEventTypeText(scope.row.type) }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="输出参数" width="120">
                  <template #default="scope">
                    {{ scope.row.outputData.length }} 个参数
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150">
                  <template #default="scope">
                    <el-button type="primary" size="small" @click="editEvent(scope.row, scope.$index)">编辑</el-button>
                    <el-button type="danger" size="small" @click="removeEvent(scope.$index)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
              
              <el-empty v-if="modelForm.events.length === 0" description="暂无事件"></el-empty>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 属性对话框 -->
    <el-dialog v-model="propertyDialogVisible" :title="isPropertyEdit ? '编辑属性' : '添加属性'" width="50%">
      <el-form :model="propertyForm" :rules="propertyRules" ref="propertyFormRef" label-width="120px">
        <el-form-item label="标识符" prop="identify">
          <el-input v-model="propertyForm.identify" placeholder="请输入属性标识符"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="propertyForm.name" placeholder="请输入属性名称"></el-input>
        </el-form-item>
        <el-form-item label="读写类型" prop="accessMode">
          <el-select v-model="propertyForm.accessMode" placeholder="请选择读写类型">
            <el-option label="读写" value="rw"></el-option>
            <el-option label="只读" value="r"></el-option>
            <el-option label="只写" value="w"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据类型" prop="dataType.type">
          <el-select v-model="propertyForm.dataType.type" placeholder="请选择数据类型" @change="handleDataTypeChange">
            <el-option label="整数" value="int"></el-option>
            <el-option label="浮点数" value="float"></el-option>
            <el-option label="布尔值" value="bool"></el-option>
            <el-option label="字符串" value="string"></el-option>
            <el-option label="枚举" value="enum"></el-option>
          </el-select>
        </el-form-item>
        <!-- 整数和浮点数的规格 -->
        <template v-if="propertyForm.dataType.type === 'int' || propertyForm.dataType.type === 'float'">
          <el-form-item label="最小值" prop="dataType.specs.min">
            <el-input-number v-model="propertyForm.dataType.specs.min" :precision="propertyForm.dataType.type === 'float' ? 2 : 0"></el-input-number>
          </el-form-item>
          <el-form-item label="最大值" prop="dataType.specs.max">
            <el-input-number v-model="propertyForm.dataType.specs.max" :precision="propertyForm.dataType.type === 'float' ? 2 : 0"></el-input-number>
          </el-form-item>
          <el-form-item label="步长" prop="dataType.specs.step">
            <el-input-number v-model="propertyForm.dataType.specs.step" :precision="propertyForm.dataType.type === 'float' ? 2 : 0"></el-input-number>
          </el-form-item>
          <el-form-item label="单位" prop="dataType.specs.unit">
            <el-input v-model="propertyForm.dataType.specs.unit" placeholder="例如：℃、kg"></el-input>
          </el-form-item>
        </template>
        <!-- 字符串规格 -->
        <template v-if="propertyForm.dataType.type === 'string'">
          <el-form-item label="最大长度" prop="dataType.specs.length">
            <el-input-number v-model="propertyForm.dataType.specs['length']" :min="1"></el-input-number>
          </el-form-item>
        </template>
        <!-- 枚举规格 -->
        <template v-if="propertyForm.dataType.type === 'enum'">
          <el-form-item label="枚举值">
            <div v-for="(item, index) in propertyForm.dataType.specs['values']" :key="index" class="enum-item">
              <el-input v-model="item.value" placeholder="值" class="enum-value"></el-input>
              <el-input v-model="item.label" placeholder="标签" class="enum-label"></el-input>
              <el-button type="danger" icon="Delete" circle @click="removeEnumValue(index)"></el-button>
            </div>
            <el-button type="primary" @click="addEnumValue">添加枚举值</el-button>
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="propertyDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitPropertyForm">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 服务对话框 -->
    <el-dialog v-model="serviceDialogVisible" :title="isServiceEdit ? '编辑服务' : '添加服务'" width="60%">
      <el-form :model="serviceForm" :rules="serviceRules" ref="serviceFormRef" label-width="120px">
        <el-form-item label="标识符" prop="identify">
          <el-input v-model="serviceForm.identify" placeholder="请输入服务标识符"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="serviceForm.name" placeholder="请输入服务名称"></el-input>
        </el-form-item>
        <el-divider content-position="left">输入参数</el-divider>
        <div class="params-section">
          <el-button type="primary" size="small" @click="addServiceParam('input')">添加输入参数</el-button>
          <el-table :data="serviceForm.inputData" border style="width: 100%; margin-top: 10px;">
            <el-table-column prop="identify" label="标识符" width="150"></el-table-column>
            <el-table-column prop="name" label="名称" width="150"></el-table-column>
            <el-table-column prop="dataType.type" label="数据类型" width="100"></el-table-column>
            <el-table-column prop="dataType.specs" label="规格">
              <template #default="scope">
                {{ formatDataTypeSpecs(scope.row.dataType) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button type="primary" size="small" @click="editServiceParam('input', scope.row, scope.$index)">编辑</el-button>
                <el-button type="danger" size="small" @click="removeServiceParam('input', scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="serviceForm.inputData.length === 0" description="暂无输入参数"></el-empty>
        </div>
        <el-divider content-position="left">输出参数</el-divider>
        <div class="params-section">
          <el-button type="primary" size="small" @click="addServiceParam('output')">添加输出参数</el-button>
          <el-table :data="serviceForm.outputData" border style="width: 100%; margin-top: 10px;">
            <el-table-column prop="identify" label="标识符" width="150"></el-table-column>
            <el-table-column prop="name" label="名称" width="150"></el-table-column>
            <el-table-column prop="dataType.type" label="数据类型" width="100"></el-table-column>
            <el-table-column prop="dataType.specs" label="规格">
              <template #default="scope">
                {{ formatDataTypeSpecs(scope.row.dataType) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button type="primary" size="small" @click="editServiceParam('output', scope.row, scope.$index)">编辑</el-button>
                <el-button type="danger" size="small" @click="removeServiceParam('output', scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="serviceForm.outputData.length === 0" description="暂无输出参数"></el-empty>
        </div>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="serviceDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitServiceForm">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 事件对话框 -->
    <el-dialog v-model="eventDialogVisible" :title="isEventEdit ? '编辑事件' : '添加事件'" width="60%">
      <el-form :model="eventForm" :rules="eventRules" ref="eventFormRef" label-width="120px">
        <el-form-item label="标识符" prop="identify">
          <el-input v-model="eventForm.identify" placeholder="请输入事件标识符"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="eventForm.name" placeholder="请输入事件名称"></el-input>
        </el-form-item>
        <el-form-item label="事件类型" prop="type">
          <el-select v-model="eventForm.type" placeholder="请选择事件类型">
            <el-option label="信息" value="info"></el-option>
            <el-option label="告警" value="warning"></el-option>
            <el-option label="故障" value="error"></el-option>
          </el-select>
        </el-form-item>
        <el-divider content-position="left">输出参数</el-divider>
        <div class="params-section">
          <el-button type="primary" size="small" @click="addEventParam">添加输出参数</el-button>
          <el-table :data="eventForm.outputData" border style="width: 100%; margin-top: 10px;">
            <el-table-column prop="identify" label="标识符" width="150"></el-table-column>
            <el-table-column prop="name" label="名称" width="150"></el-table-column>
            <el-table-column prop="dataType.type" label="数据类型" width="100"></el-table-column>
            <el-table-column prop="dataType.specs" label="规格">
              <template #default="scope">
                {{ formatDataTypeSpecs(scope.row.dataType) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button type="primary" size="small" @click="editEventParam(scope.row, scope.$index)">编辑</el-button>
                <el-button type="danger" size="small" @click="removeEventParam(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="eventForm.outputData.length === 0" description="暂无输出参数"></el-empty>
        </div>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="eventDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEventForm">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 参数编辑对话框 (用于服务和事件的参数) -->
    <el-dialog v-model="paramDialogVisible" :title="isParamEdit ? '编辑参数' : '添加参数'" width="50%">
      <el-form :model="paramForm" :rules="paramRules" ref="paramFormRef" label-width="120px">
        <el-form-item label="标识符" prop="identify">
          <el-input v-model="paramForm.identify" placeholder="请输入参数标识符"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="paramForm.name" placeholder="请输入参数名称"></el-input>
        </el-form-item>
        <el-form-item label="数据类型" prop="dataType.type">
          <el-select v-model="paramForm.dataType.type" placeholder="请选择数据类型" @change="handleParamDataTypeChange">
            <el-option label="整数" value="int"></el-option>
            <el-option label="浮点数" value="float"></el-option>
            <el-option label="布尔值" value="bool"></el-option>
            <el-option label="字符串" value="string"></el-option>
            <el-option label="枚举" value="enum"></el-option>
          </el-select>
        </el-form-item>
        <!-- 整数和浮点数的规格 -->
        <template v-if="paramForm.dataType.type === 'int' || paramForm.dataType.type === 'float'">
          <el-form-item label="最小值" prop="dataType.specs.min">
            <el-input-number v-model="paramForm.dataType.specs.min" :precision="paramForm.dataType.type === 'float' ? 2 : 0"></el-input-number>
          </el-form-item>
          <el-form-item label="最大值" prop="dataType.specs.max">
            <el-input-number v-model="paramForm.dataType.specs.max" :precision="paramForm.dataType.type === 'float' ? 2 : 0"></el-input-number>
          </el-form-item>
          <el-form-item label="步长" prop="dataType.specs.step">
            <el-input-number v-model="paramForm.dataType.specs.step" :precision="paramForm.dataType.type === 'float' ? 2 : 0"></el-input-number>
          </el-form-item>
          <el-form-item label="单位" prop="dataType.specs.unit">
            <el-input v-model="paramForm.dataType.specs.unit" placeholder="例如：℃、kg"></el-input>
          </el-form-item>
        </template>
        <!-- 字符串规格 -->
        <template v-if="paramForm.dataType.type === 'string'">
          <el-form-item label="最大长度" prop="dataType.specs.length">
            <el-input-number v-model="paramForm.dataType.specs['length']" :min="1"></el-input-number>
          </el-form-item>
        </template>
        <!-- 枚举规格 -->
        <template v-if="paramForm.dataType.type === 'enum'">
          <el-form-item label="枚举值">
            <div v-for="(item, index) in paramForm.dataType.specs['values']" :key="index" class="enum-item">
              <el-input v-model="item.value" placeholder="值" class="enum-value"></el-input>
              <el-input v-model="item.label" placeholder="标签" class="enum-label"></el-input>
              <el-button type="danger" icon="Delete" circle @click="removeParamEnumValue(index)"></el-button>
            </div>
            <el-button type="primary" @click="addParamEnumValue">添加枚举值</el-button>
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="paramDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitParamForm">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- JSON查看对话框 -->
    <el-dialog v-model="jsonDialogVisible" title="模型JSON" width="60%">
      <pre class="json-viewer">{{ formattedModelJson }}</pre>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="copyModelJson">复制</el-button>
          <el-button @click="jsonDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch, toRefs } from 'vue'
import { useDeviceTypeStore } from '@/store/deviceType'
import { ElMessage, type FormInstance } from 'element-plus'
import { getDeviceTypeById, updateDeviceTypeModel } from '@/api/deviceType'
import type { DeviceType, Model, Property, Service, Event, DataType } from '@/types/models'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const deviceTypeStore = useDeviceTypeStore()

// 表单引用
const deviceTypeFormRef = ref<FormInstance>()
const propertyFormRef = ref<FormInstance>()
const serviceFormRef = ref<FormInstance>()
const eventFormRef = ref<FormInstance>()
const paramFormRef = ref<FormInstance>()

// 状态
const state = reactive({
  activeTab: 'basic',
  deviceTypeForm: {
    code: '',
    name: '',
    description: ''
  },
  modelForm: {
    properties: [] as Property[],
    services: [] as Service[],
    events: [] as Event[]
  },
  propertyForm: {
    identify: '',
    name: '',
    accessMode: 'rw',
    dataType: {
      type: 'int',
      specs: {
        min: 0,
        max: 100,
        step: 1,
        unit: ''
      } as Record<string, any>
    }
  },
  serviceForm: {
    identify: '',
    name: '',
    inputData: [] as Property[],
    outputData: [] as Property[]
  },
  eventForm: {
    identify: '',
    name: '',
    type: 'info',
    outputData: [] as Property[]
  },
  paramForm: {
    identify: '',
    name: '',
    dataType: {
      type: 'int',
      specs: {
        min: 0,
        max: 100,
        step: 1,
        unit: ''
      } as Record<string, any>
    }
  },
  submitting: false,
  propertyDialogVisible: false,
  serviceDialogVisible: false,
  eventDialogVisible: false,
  paramDialogVisible: false,
  isPropertyEdit: false,
  isServiceEdit: false,
  isEventEdit: false,
  isParamEdit: false,
  editingPropertyIndex: -1,
  editingServiceIndex: -1,
  editingEventIndex: -1,
  editingParamIndex: -1,
  editingParamType: '' as 'input' | 'output' | 'event',
  tempId: null as number | null , // 临时ID，用于在创建设备类型后添加模型
  currentDeviceTypeId:null        // 临时ID，用于在创建设备类型后添加模型
})

// 计算属性
const {
  activeTab, deviceTypeForm, modelForm, propertyForm, serviceForm, eventForm, paramForm,
  submitting, propertyDialogVisible, serviceDialogVisible, eventDialogVisible, paramDialogVisible,
  isPropertyEdit, isServiceEdit, isEventEdit, isParamEdit, editingPropertyIndex, editingServiceIndex,
  editingEventIndex, editingParamIndex, editingParamType, tempId,currentDeviceTypeId
} = toRefs(state)

// 确定是否是编辑模式
const isEditMode = computed(() => {
  return route.query.mode === 'edit'
})

// 获取设备类型ID
const deviceTypeId = computed(() => {
  return parseInt(route.query.deviceTypeId as string) || null
})

// 验证规则
const basicRules = {
  code: [
    { required: true, message: '请输入设备类型编码', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入设备类型名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入设备类型描述', trigger: 'blur' }
  ]
}

// 验证规则
const propertyRules = {
  identify: [
    { required: true, message: '请输入属性标识符', trigger: 'blur' },
    { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入属性名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  'dataType.type': [
    { required: true, message: '请选择数据类型', trigger: 'change' }
  ]
}

const serviceRules = {
  identify: [
    { required: true, message: '请输入服务标识符', trigger: 'blur' },
    { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入服务名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
}

const eventRules = {
  identify: [
    { required: true, message: '请输入事件标识符', trigger: 'blur' },
    { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入事件名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择事件类型', trigger: 'change' }
  ]
}

const paramRules = {
  identify: [
    { required: true, message: '请输入参数标识符', trigger: 'blur' },
    { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入参数名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  'dataType.type': [
    { required: true, message: '请选择数据类型', trigger: 'change' }
  ]
}

// 处理数据类型变化
const handleDataTypeChange = (type: string) => {
  switch(type) {
    case 'int':
      propertyForm.value.dataType.specs = { min: 0, max: 100, step: 1, unit: '' }
      break
    case 'float':
      propertyForm.value.dataType.specs = { min: 0.0, max: 100.0, step: 0.1, unit: '' }
      break
    case 'bool':
      propertyForm.value.dataType.specs = { '0': '关', '1': '开' }
      break
    case 'string':
      propertyForm.value.dataType.specs = { length: 255 }
      break
    case 'enum':
      propertyForm.value.dataType.specs = { values: [{ value: '0', label: '选项1' }] }
      break
    default:
      propertyForm.value.dataType.specs = { min: 0, max: 100, step: 1, unit: '' }
  }
}

// 处理数据类型变化
const handleParamDataTypeChange = (type: string) => {
  switch(type) {
    case 'int':
      paramForm.value.dataType.specs = { min: 0, max: 100, step: 1, unit: '' }
      break
    case 'float':
      paramForm.value.dataType.specs = { min: 0.0, max: 100.0, step: 0.1, unit: '' }
      break
    case 'bool':
      paramForm.value.dataType.specs = { '0': '关', '1': '开' }
      break
    case 'string':
      paramForm.value.dataType.specs = { length: 255 }
      break
    case 'enum':
      paramForm.value.dataType.specs = { values: [{ value: '0', label: '选项1' }] }
      break
    default:
      paramForm.value.dataType.specs = { min: 0, max: 100, step: 1, unit: '' }
  }
}

// 添加和删除枚举值
const addEnumValue = () => {
  if (!propertyForm.value.dataType.specs.values) {
    propertyForm.value.dataType.specs.values = []
  }
  propertyForm.value.dataType.specs.values.push({ value: '', label: '' })
}

const removeEnumValue = (index: number) => {
  propertyForm.value.dataType.specs.values.splice(index, 1)
}

const addParamEnumValue = () => {
  if (!paramForm.value.dataType.specs.values) {
    paramForm.value.dataType.specs.values = []
  }
  paramForm.value.dataType.specs.values.push({ value: '', label: '' })
}

const removeParamEnumValue = (index: number) => {
  paramForm.value.dataType.specs.values.splice(index, 1)
}

// 格式化数据类型规格
const formatDataTypeSpecs = (dataType: DataType) => {
  if (!dataType || !dataType.specs) return ''
  switch (dataType.type) {
    case 'int':
      return `范围: ${dataType.specs.min} - ${dataType.specs.max}${dataType.specs.unit ? ', 单位: ' + dataType.specs.unit : ''}`
    case 'float':
      return `范围: ${dataType.specs.min} - ${dataType.specs.max}${dataType.specs.unit ? ', 单位: ' + dataType.specs.unit : ''}`
    case 'bool':
      return `${dataType.specs[0]} / ${dataType.specs[1]}`
    case 'string':
      return `最大长度: ${dataType.specs.length}`
    case 'enum':
      return dataType.specs.values ? dataType.specs.values.map(v => `${v.label}(${v.value})`).join(', ') : ''
    default:
      return ''
  }
}

// 获取事件类型文本和标签类型
const getEventTypeText = (type: string) => {
  switch(type) {
    case 'info': return '信息'
    case 'warning': return '告警'
    case 'error': return '故障'
    default: return type
  }
}

const getEventTypeTag = (type: string) => {
  switch(type) {
    case 'info': return 'info'
    case 'warning': return 'warning'
    case 'error': return 'danger'
    default: return 'info'
  }
}

// 获取访问模式文本
const getAccessModeText = (mode: string) => {
  switch(mode) {
    case 'rw': return '读写'
    case 'r': return '只读'
    case 'w': return '只写'
    default: return mode
  }
}

// 初始化表单数据
const resetDeviceTypeForm = () => {
  deviceTypeForm.value = {
    code: '',
    name: '',
    description: ''
  }
}

// 初始化表单数据
const resetModelForm = () => {
  modelForm.value = {
    properties: [],
    services: [],
    events: []
  }
}

// 初始化属性表单
const initPropertyForm = () => {
  propertyForm.value = {
    identify: '',
    name: '',
    accessMode: 'rw',
    dataType: {
      type: 'int',
      specs: {
        min: 0,
        max: 100,
        step: 1,
        unit: ''
      } as Record<string, any>
    }
  }
}

// 初始化服务表单
const initServiceForm = () => {
  serviceForm.value = {
    identify: '',
    name: '',
    inputData: [],
    outputData: []
  }
}

// 初始化事件表单
const initEventForm = () => {
  eventForm.value = {
    identify: '',
    name: '',
    type: 'info',
    outputData: []
  }
}

// 初始化参数表单
const initParamForm = () => {
  paramForm.value = {
    identify: '',
    name: '',
    dataType: {
      type: 'int',
      specs: {
        min: 0,
        max: 100,
        step: 1,
        unit: ''
      } as Record<string, any>
    }
  }
}

// 加载设备类型数据
const loadDeviceTypeData = async (id: number) => {
  try {
    // 从API获取设备类型数据
    const res: any = await getDeviceTypeById(id)
    if (res.data) {
      const deviceType = res.data
      // 加载基本信息'
      deviceTypeForm.value.code = deviceType.code || ''
      deviceTypeForm.value.name = deviceType.name || ''
      deviceTypeForm.value.description = deviceType.description || ''
      
      // 加载模型数据（如果有）
      if (deviceType.model) {
        modelForm.value = deviceType.model
      } else {
        resetModelForm()
      }
    }
  } catch (error) {
    console.error('加载设备类型数据失败:', error)
    ElMessage.error('加载设备类型数据失败')
  }
}

// 监听路由变化，加载对应的设备类型数据
watch([() => route.query.deviceTypeId, () => route.query.mode], async ([newId, newMode]) => {
  if (newMode === 'edit' && newId) {
    await loadDeviceTypeData(parseInt(newId as string))
  } else {
    resetDeviceTypeForm()
    resetModelForm()
  }
}, { immediate: true })

onMounted(async () => {
  if (isEditMode.value && deviceTypeId.value) {
    await loadDeviceTypeData(deviceTypeId.value)
  } else {
    resetDeviceTypeForm()
    resetModelForm()
  }
})

// 返回列表
const navigateBack = () => {
  router.push('/meta/deviceType/list')
}

// 提交表单
const submitForm = async () => {
  if (!deviceTypeFormRef.value) return
  await deviceTypeFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (isEditMode.value && deviceTypeId.value) {
          // 更新设备类型
          const result = await deviceTypeStore.updateDeviceType(deviceTypeId.value, deviceTypeForm.value)
          // 更新设备类型模型
          await updateDeviceTypeModel(deviceTypeId.value, modelForm.value)
          
          ElMessage.success('更新成功')
        }else if(activeTab.value === 'model'){
          tempId.value = currentDeviceTypeId.value
        }else {
          // 创建设备类型 - 符合接口文档格式
          const createData = {
            code: deviceTypeForm.value.code,
            name: deviceTypeForm.value.name,
            description: deviceTypeForm.value.description
          }
          
          const result = await deviceTypeStore.createDeviceType(createData)
          if (result && result.id) {
            currentDeviceTypeId.value = result.id
            // 激活模型选项卡
            activeTab.value = 'model'
            ElMessage.success('创建成功，请继续定义设备类型模型')
          } else {
            throw new Error('创建设备类型失败，未返回有效ID')
          }
        }
      } catch (error) {
        console.error(error)
        ElMessage.error(isEditMode.value ? '更新失败' : '创建失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

// 属性相关方法
const addProperty = () => {
  initPropertyForm()
  isPropertyEdit.value = false
  propertyDialogVisible.value = true
}

const editProperty = (property: Property, index: number) => {
  propertyForm.value = JSON.parse(JSON.stringify(property))
  isPropertyEdit.value = true
  editingPropertyIndex.value = index
  propertyDialogVisible.value = true
}

const removeProperty = (index: number) => {
  modelForm.value.properties.splice(index, 1)
}

const submitPropertyForm = async () => {
  if (!propertyFormRef.value) return
  
  await propertyFormRef.value.validate(async (valid) => {
    if (valid) {
      const property = JSON.parse(JSON.stringify(propertyForm.value))
      if (isPropertyEdit.value) {
        // 更新现有属性
        modelForm.value.properties[editingPropertyIndex.value] = JSON.parse(JSON.stringify(propertyForm.value))
      } else {
        // 添加新属性
        modelForm.value.properties.push(JSON.parse(JSON.stringify(propertyForm.value)))
      }
      propertyDialogVisible.value = false
    }
  })
}

// 服务相关方法
const addService = () => {
  initServiceForm()
  isServiceEdit.value = false
  serviceDialogVisible.value = true
}

const editService = (service: Service, index: number) => {
  serviceForm.value = JSON.parse(JSON.stringify(service))
  isServiceEdit.value = true
  editingServiceIndex.value = index
  serviceDialogVisible.value = true
}

const removeService = (index: number) => {
  modelForm.value.services.splice(index, 1)
}

const submitServiceForm = async () => {
  if (!serviceFormRef.value) return
  
  await serviceFormRef.value.validate(async (valid) => {
    if (valid) {
      const service = JSON.parse(JSON.stringify(serviceForm.value))
      if (isServiceEdit.value) {
        // 更新现有服务
        modelForm.value.services[editingServiceIndex.value] = JSON.parse(JSON.stringify(serviceForm.value))
      } else {
        // 添加新服务
        modelForm.value.services.push(JSON.parse(JSON.stringify(serviceForm.value)))
      }
      serviceDialogVisible.value = false
    }
  })
}

// 添加服务参数
const addServiceParam = (type: 'input' | 'output') => {
  initParamForm()
  isParamEdit.value = false
  editingParamType.value = type
  serviceDialogVisible.value = true
  paramDialogVisible.value = true
}

const editServiceParam = (type: 'input' | 'output', param: Property, index: number) => {
  paramForm.value = JSON.parse(JSON.stringify(param))
  isParamEdit.value = true
  editingParamType.value = type
  editingParamIndex.value = index
  paramDialogVisible.value = true
}

const removeServiceParam = (type: 'input' | 'output', index: number) => {
  if (type === 'input') {
    serviceForm.value.inputData.splice(index, 1)
  } else {
    serviceForm.value.outputData.splice(index, 1)
  }
}

// 事件相关方法
const addEvent = () => {
  initEventForm()
  isEventEdit.value = false
  eventDialogVisible.value = true
}

const editEvent = (event: Event, index: number) => {
  eventForm.value = JSON.parse(JSON.stringify(event))
  isEventEdit.value = true
  editingEventIndex.value = index
  eventDialogVisible.value = true
}

const removeEvent = (index: number) => {
  modelForm.value.events.splice(index, 1)
}

const submitEventForm = async () => {
  if (!eventFormRef.value) return
  
  await eventFormRef.value.validate(async (valid) => {
    if (valid) {
      const event = JSON.parse(JSON.stringify(eventForm.value))
      if (isEventEdit.value) {
        // 更新现有事件
        modelForm.value.events[editingEventIndex.value] = JSON.parse(JSON.stringify(eventForm.value))
      } else {
        // 添加新事件
        modelForm.value.events.push(JSON.parse(JSON.stringify(eventForm.value)))
      }
      eventDialogVisible.value = false
    }
  })
}

// 添加事件参数
const addEventParam = () => {
  initParamForm()
  isParamEdit.value = false
  editingParamType.value = 'event'
  eventDialogVisible.value = true
  paramDialogVisible.value = true
}

const editEventParam = (param: Property, index: number) => {
  paramForm.value = JSON.parse(JSON.stringify(param))
  isParamEdit.value = true
  editingParamType.value = 'event'
  editingParamIndex.value = index
  paramDialogVisible.value = true
}

const removeEventParam = (index: number) => {
  eventForm.value.outputData.splice(index, 1)
}

// 提交参数表单
const submitParamForm = async () => {
  if (!paramFormRef.value) return
  await paramFormRef.value.validate(async (valid) => {
    if (valid) {
      const param = JSON.parse(JSON.stringify(paramForm.value))
      if (editingParamType.value === 'input') {
        if (isParamEdit.value) {
          serviceForm.value.inputData[editingParamIndex.value] = param
        } else {
          serviceForm.value.inputData.push(param)
        }
      } else if (editingParamType.value === 'output') {
        if (isParamEdit.value) {
          serviceForm.value.outputData[editingParamIndex.value] = param
        } else {
          serviceForm.value.outputData.push(param)
        }
      } else if (editingParamType.value === 'event') {
        if (isParamEdit.value) {
          eventForm.value.outputData[editingParamIndex.value] = param
        } else {
          eventForm.value.outputData.push(param)
        }
      }
      paramDialogVisible.value = false
    }
  })
}

// 在保存临时设备类型后，更新设备类型模型
watch(() => tempId.value, async (newId) => {
  if (newId && activeTab.value === 'model' && modelForm.value.properties.length > 0) {
    try {
      // 确保模型数据结构符合接口要求
      const modelData = {
        properties: modelForm.value.properties || [],
        services: modelForm.value.services || [],
        events: modelForm.value.events || []
      }

      // 更新新创建的设备类型的模型
      await updateDeviceTypeModel(newId, modelData)
      ElMessage.success('模型已保存')
    } catch (error) {
      console.error('保存模型失败:', error)
      ElMessage.error('保存模型失败')
    }
  }
})

// JSON对话框相关状态
const jsonDialogVisible = ref(false)

// 格式化JSON
const formattedModelJson = computed(() => {
  return JSON.stringify(modelForm.value, null, 2)
})

// 显示模型JSON
const showModelJson = () => {
  jsonDialogVisible.value = true
}

// 复制JSON内容
const copyModelJson = () => {
  navigator.clipboard.writeText(formattedModelJson.value)
    .then(() => {
      ElMessage.success('JSON已复制到剪贴板')
    })
    .catch(err => {
      console.error('复制失败:', err)
      ElMessage.error('复制失败')
    })
}
</script>

<style scoped>
.device-type-setting {
  padding: 20px;
}

.devicetype-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.model-section {
  margin-bottom: 30px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.section-header h3 {
  margin: 0;
  font-size: 16px;
}

.section-actions {
  display: flex;
  gap: 10px;
}

.enum-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  gap: 10px;
}

.enum-value, .enum-label {
  flex: 1;
}

.params-section {
  margin-bottom: 20px;
}

.json-viewer {
  background-color: #f5f7fa;
  color: #606266;
  padding: 16px;
  border-radius: 4px;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', 'Consolas', 'source-code-pro', monospace;
  font-size: 14px;
  line-height: 1.5;
  overflow: auto;
  max-height: 60vh;
  white-space: pre-wrap;
  word-break: break-word;
}
</style>
