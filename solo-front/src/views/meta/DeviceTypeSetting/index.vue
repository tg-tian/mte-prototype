<template>
  <div class="device-type-setting">
    <div class="devicetype-header">
      <h2>{{ isEditMode ? '编辑设备类型——'+ deviceTypeForm.modelName : '创建设备类型' }}</h2>
      <div class="header-actions">
        <el-button @click="navigateBack">返回列表</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
      </div>
    </div>

    <!-- 基本信息表单 - 提到最上面 -->
    <el-card class="basic-info-card">
      <el-form 
        :model="deviceTypeForm" 
        :rules="basicRules"
        ref="deviceTypeFormRef"
        label-width="120px">
        <el-form-item label="模型名称" prop="modelName">
          <el-input v-model="deviceTypeForm.modelName" placeholder="请输入模型名称"></el-input>
        </el-form-item>
        <el-form-item label="品类" prop="category">
          <el-input v-model="deviceTypeForm.category" placeholder="请输入品类"></el-input>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Tab页面：属性、服务、事件 -->
    <el-card class="setting-content">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="属性" name="property" :disabled="!isEditMode && !currentDeviceTypeId">
          <div v-if="isEditMode || currentDeviceTypeId" class="tab-content-wrapper">
            <div class="tab-actions">
              <el-button type="primary" :icon="Plus" @click="addProperty">添加属性</el-button>
              <el-button @click="showModelJson">查看JSON</el-button>
            </div>
            
            <el-table :data="propertyList" border style="width: 100%;" header-align="center">
              <el-table-column prop="identify" label="标识符" min-width="120" show-overflow-tooltip></el-table-column>
              <el-table-column prop="description" label="名称/描述" min-width="150" show-overflow-tooltip></el-table-column>
              <el-table-column prop="readOnly" label="只读" width="80" align="center">
                <template #default="scope">
                  <el-tag :type="scope.row.readOnly ? 'info' : 'success'" size="small">{{ scope.row.readOnly ? '是' : '否' }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="type" label="数据类型" width="100" align="center">
                <template #default="scope">
                  <el-tag size="small" effect="plain">{{ getDataTypeLabel(scope.row.type) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="unit" label="单位" width="80" align="center"></el-table-column>
              <el-table-column label="范围/枚举" min-width="150" show-overflow-tooltip>
                <template #default="scope">
                  <span class="spec-text">{{ formatV1PropertySpecs(scope.row) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150" align="center" fixed="right">
                <template #default="scope">
                  <div class="table-ops">
                    <el-button type="primary" :icon="Edit" link @click="editProperty(scope.row.identify)">编辑</el-button>
                    <el-button type="danger" :icon="Delete" link @click="removeProperty(scope.row.identify)">删除</el-button>
                  </div>
                </template>
              </el-table-column>
            </el-table>
            
            <el-empty v-if="propertyList.length === 0" description="暂无属性" :image-size="100"></el-empty>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="操作" name="action" :disabled="!isEditMode && !currentDeviceTypeId">
          <div v-if="isEditMode || currentDeviceTypeId" class="tab-content-wrapper">
            <div class="tab-actions">
              <el-button type="primary" :icon="Plus" @click="addAction">添加操作</el-button>
            </div>
            <el-table :data="actionList" border style="width: 100%;" header-align="center">
              <el-table-column prop="description" label="操作名称" min-width="150" show-overflow-tooltip>
                <template #default="scope">
                  <div class="action-name-cell">
                    <el-icon><Pointer /></el-icon>
                    <span>{{ scope.row.description }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="identify" label="标识符 (Command)" min-width="150" show-overflow-tooltip>
                <template #default="scope">
                  <code>{{ scope.row.identify }}</code>
                </template>
              </el-table-column>
              <el-table-column label="参数配置" min-width="250">
                <template #default="scope">
                  <div class="arg-preview-list">
                    <template v-if="Object.keys(scope.row.arguments || {}).length > 0">
                      <el-tooltip
                        v-for="(arg, argKey) in scope.row.arguments"
                        :key="argKey"
                        :content="`${arg.description} (${getDataTypeLabel(arg.type)})`"
                        placement="top"
                      >
                        <el-tag size="small" class="arg-tag">
                          {{ argKey }}
                        </el-tag>
                      </el-tooltip>
                    </template>
                    <span v-else class="no-args-text">无参数</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150" align="center" fixed="right">
                <template #default="scope">
                  <div class="table-ops">
                    <el-button type="primary" :icon="Edit" link @click="editAction(scope.row.identify)">编辑</el-button>
                    <el-button type="danger" :icon="Delete" link @click="removeAction(scope.row.identify)">删除</el-button>
                  </div>
                </template>
              </el-table-column>
            </el-table>
            
            <el-empty v-if="actionList.length === 0" description="暂无操作" :image-size="100"></el-empty>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="事件" name="event" :disabled="!isEditMode && !currentDeviceTypeId">
          <div v-if="isEditMode || currentDeviceTypeId" class="tab-content-wrapper">
            <div class="tab-actions">
              <el-button type="primary" :icon="Plus" @click="addEvent">添加事件</el-button>
            </div>
            <el-table :data="eventList" border style="width: 100%;" header-align="center">
              <el-table-column prop="identify" label="标识符" min-width="150" show-overflow-tooltip></el-table-column>
              <el-table-column prop="level" label="级别" width="100" align="center">
                <template #default="scope">
                  <el-tag :type="getEventTypeTag(scope.row.level)" size="small" effect="dark">{{ getEventTypeText(scope.row.level) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="description" label="名称/描述" min-width="200" show-overflow-tooltip></el-table-column>
              <el-table-column label="字段" width="120" align="center">
                <template #default="scope">
                  <el-tag size="small" type="info">{{ Object.keys(scope.row.fields || {}).length }} 个字段</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150" align="center" fixed="right">
                <template #default="scope">
                  <div class="table-ops">
                    <el-button type="primary" :icon="Edit" link @click="editEvent(scope.row.identify)">编辑</el-button>
                    <el-button type="danger" :icon="Delete" link @click="removeEvent(scope.row.identify)">删除</el-button>
                  </div>
                </template>
              </el-table-column>
            </el-table>
            
            <el-empty v-if="eventList.length === 0" description="暂无事件" :image-size="100"></el-empty>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 属性对话框 -->
    <el-dialog v-model="propertyDialogVisible" :title="isPropertyEdit ? '编辑属性' : '添加属性'" width="50%">
      <el-form :model="propertyForm" :rules="propertyRules" ref="propertyFormRef" label-width="120px">
        <el-form-item label="标识符" prop="identify">
          <el-input v-model="propertyForm.identify" placeholder="请输入属性标识符" :disabled="isPropertyEdit"></el-input>
        </el-form-item>
        <el-form-item label="名称/描述" prop="description">
          <el-input v-model="propertyForm.description" placeholder="请输入属性名称或描述"></el-input>
        </el-form-item>
        <el-form-item label="只读" prop="readOnly">
          <el-switch v-model="propertyForm.readOnly"></el-switch>
        </el-form-item>
        <el-form-item label="数据类型" prop="type">
          <el-select v-model="propertyForm.type" placeholder="请选择数据类型" @change="handleTypeChange">
            <el-option label="字符串" value="string"></el-option>
            <el-option label="数值" value="number"></el-option>
            <el-option label="布尔值" value="boolean"></el-option>
            <el-option label="枚举" value="enum"></el-option>
            <el-option label="对象" value="object"></el-option>
            <el-option label="数组" value="array"></el-option>
          </el-select>
        </el-form-item>
        
        <template v-if="propertyForm.type === 'number'">
          <el-form-item label="最小值" prop="min">
            <el-input-number v-model="propertyForm.min"></el-input-number>
          </el-form-item>
          <el-form-item label="最大值" prop="max">
            <el-input-number v-model="propertyForm.max"></el-input-number>
          </el-form-item>
          <el-form-item label="单位" prop="unit">
            <el-input v-model="propertyForm.unit" placeholder="例如：℃、kg"></el-input>
          </el-form-item>
        </template>
        
        <template v-if="propertyForm.type === 'enum' || propertyForm.type === 'string'">
          <el-form-item label="枚举值" v-if="propertyForm.type === 'enum'">
            <div class="enum-list-container">
              <div v-for="(val, index) in propertyForm.enumValues" :key="index" class="enum-item">
                <el-input v-model="propertyForm.enumValues[index]" placeholder="请输入枚举值" style="width: 100%;"></el-input>
                <el-button type="danger" :icon="Delete" link @click="propertyForm.enumValues.splice(index, 1)" title="删除"></el-button>
              </div>
              <div class="enum-add-btn">
                <el-button type="primary" :icon="Plus" link @click="propertyForm.enumValues.push('')">添加枚举值</el-button>
              </div>
            </div>
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

    <!-- 操作 (Action) 对话框 -->
    <el-dialog v-model="actionDialogVisible" :title="isActionEdit ? '编辑操作' : '添加操作'" width="60%">
      <el-form :model="actionForm" :rules="actionRules" ref="actionFormRef" label-width="120px">
        <el-form-item label="标识符" prop="identify">
          <el-input v-model="actionForm.identify" placeholder="请输入操作标识符" :disabled="isActionEdit"></el-input>
        </el-form-item>
        <el-form-item label="名称/描述" prop="description">
          <el-input v-model="actionForm.description" placeholder="请输入操作名称及描述"></el-input>
        </el-form-item>
        <el-divider content-position="left">参数 (Arguments)</el-divider>
        <div class="params-section">
           <el-button type="primary" size="small" :icon="Plus" @click="addParam('argument')">添加参数</el-button>
          <el-table :data="argumentList" border style="width: 100%; margin-top: 10px;" header-align="center">
            <el-table-column prop="identify" label="标识符" min-width="120" show-overflow-tooltip></el-table-column>
            <el-table-column prop="description" label="描述" min-width="150" show-overflow-tooltip></el-table-column>
            <el-table-column prop="type" label="数据类型" width="100" align="center">
              <template #default="scope">
                <el-tag size="small" effect="plain">{{ getDataTypeLabel(scope.row.type) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="130" align="center" fixed="right">
              <template #default="scope">
                <div class="table-ops">
                  <el-button type="primary" :icon="Edit" link @click="editParam('argument', scope.row.identify)">编辑</el-button>
                  <el-button type="danger" :icon="Delete" link @click="removeParam('argument', scope.row.identify)">删除</el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="actionDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitActionForm">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 事件对话框 -->
    <el-dialog v-model="eventDialogVisible" :title="isEventEdit ? '编辑事件' : '添加事件'" width="60%">
      <el-form :model="eventForm" :rules="eventRules" ref="eventFormRef" label-width="120px">
        <el-form-item label="标识符" prop="identify">
          <el-input v-model="eventForm.identify" placeholder="请输入事件标识符" :disabled="isEventEdit"></el-input>
        </el-form-item>
        <el-form-item label="名称/描述" prop="description">
          <el-input v-model="eventForm.description" placeholder="请输入事件名称及描述"></el-input>
        </el-form-item>
        <el-form-item label="事件级别" prop="level">
          <el-select v-model="eventForm.level" placeholder="请选择事件级别">
            <el-option label="信息" value="info"></el-option>
            <el-option label="告警" value="warning"></el-option>
            <el-option label="故障" value="error"></el-option>
          </el-select>
        </el-form-item>
        <el-divider content-position="left">字段 (Fields)</el-divider>
        <div class="params-section">
           <el-button type="primary" size="small" :icon="Plus" @click="addParam('field')">添加字段</el-button>
          <el-table :data="fieldList" border style="width: 100%; margin-top: 10px;" header-align="center">
            <el-table-column prop="identify" label="标识符" min-width="120" show-overflow-tooltip></el-table-column>
            <el-table-column prop="description" label="描述" min-width="150" show-overflow-tooltip></el-table-column>
            <el-table-column prop="type" label="数据类型" width="100" align="center">
              <template #default="scope">
                <el-tag size="small" effect="plain">{{ getDataTypeLabel(scope.row.type) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="130" align="center" fixed="right">
              <template #default="scope">
                <div class="table-ops">
                  <el-button type="primary" :icon="Edit" link @click="editParam('field', scope.row.identify)">编辑</el-button>
                  <el-button type="danger" :icon="Delete" link @click="removeParam('field', scope.row.identify)">删除</el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="eventDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEventForm">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 参数/字段编辑对话框 -->
    <el-dialog v-model="paramDialogVisible" :title="isParamEdit ? '编辑参数' : '添加参数'" width="50%" append-to-body>
      <el-form :model="paramForm" :rules="propertyRules" ref="paramFormRef" label-width="120px">
        <el-form-item label="标识符" prop="identify">
          <el-input v-model="paramForm.identify" placeholder="请输入标识符" :disabled="isParamEdit"></el-input>
        </el-form-item>
        <el-form-item label="名称/描述" prop="description">
          <el-input v-model="paramForm.description" placeholder="请输入描述"></el-input>
        </el-form-item>
        <el-form-item label="数据类型" prop="type">
          <el-select v-model="paramForm.type" placeholder="请选择数据类型" @change="handleParamTypeChange">
            <el-option label="字符串" value="string"></el-option>
            <el-option label="数值" value="number"></el-option>
            <el-option label="布尔值" value="boolean"></el-option>
            <el-option label="枚举" value="enum"></el-option>
            <el-option label="对象" value="object"></el-option>
            <el-option label="数组" value="array"></el-option>
          </el-select>
        </el-form-item>
        
        <template v-if="paramForm.type === 'number'">
          <el-form-item label="最小值" prop="min">
            <el-input-number v-model="paramForm.min"></el-input-number>
          </el-form-item>
          <el-form-item label="最大值" prop="max">
            <el-input-number v-model="paramForm.max"></el-input-number>
          </el-form-item>
          <el-form-item label="单位" prop="unit">
            <el-input v-model="paramForm.unit" placeholder="例如：℃、kg"></el-input>
          </el-form-item>
        </template>
        
        <template v-if="paramForm.type === 'enum'">
          <el-form-item label="枚举值">
            <div class="enum-list-container">
              <div v-for="(val, index) in paramForm.enumValues" :key="index" class="enum-item">
                <el-input v-model="paramForm.enumValues[index]" placeholder="请输入枚举值" style="width: 100%;"></el-input>
                <el-button type="danger" :icon="Delete" link @click="paramForm.enumValues.splice(index, 1)" title="删除"></el-button>
              </div>
              <div class="enum-add-btn">
                <el-button type="primary" :icon="Plus" link @click="paramForm.enumValues.push('')">添加枚举值</el-button>
              </div>
            </div>
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
import { Delete, Plus, Edit, Pointer } from '@element-plus/icons-vue'
import { useDeviceTypeStore } from '@/store/deviceType'
import { ElMessage, type FormInstance } from 'element-plus'
import { getDeviceTypeById } from '@/api/deviceType'
import type { DeviceType, PropertyDefinition, ActionDefinition, EventDefinition, BaseDeviceModel } from '@/types/models'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const deviceTypeStore = useDeviceTypeStore()

// 数据类型配置
const dataTypeOptions = [
  // 字符串类
  { label: '字符串', value: 'string' },
  { label: '邮箱', value: 'email' },
  { label: '电话', value: 'phone' },
  { label: 'URL', value: 'url' },
  { label: 'IP地址', value: 'ip' },
  { label: 'JSON', value: 'json' },
  { label: 'Base64', value: 'base64' },
  { label: 'Hash', value: 'hash' },
  { label: 'Markdown文本', value: 'markdown' },
  { label: '代码块', value: 'code' },
  // 数值类
  { label: '数字', value: 'number' },
  { label: '整数', value: 'integer' },
  { label: '小数', value: 'decimal' },
  { label: '浮点数', value: 'float' },
  // 日期类
  { label: '日期', value: 'date' },
  { label: '日期时间', value: 'datetime' },
  { label: '时间', value: 'time' },
  // 布尔类
  { label: '布尔值', value: 'boolean' },
  // 标识符类
  { label: 'UUID', value: 'uuid' },
  // 枚举类
  { label: '枚举', value: 'enum' },
  { label: '单选', value: 'singleSelect' },
  { label: '多选', value: 'multiSelect' },
  // 地理/地址类
  { label: '地址', value: 'address' },
  { label: '定位', value: 'location' },
  { label: '经纬度', value: 'latlng' },
  // 文件/媒体类
  { label: '文件', value: 'file' },
  { label: '图片', value: 'image' },
  // 智能对象（保留原有的）
  { label: '智能对象', value: 'object' },
  // 兼容旧版本
  { label: '整数', value: 'int' },
  { label: '布尔值', value: 'bool' },
]

// 表单引用
const deviceTypeFormRef = ref<FormInstance>()
const propertyFormRef = ref<FormInstance>()
const actionFormRef = ref<FormInstance>()
const eventFormRef = ref<FormInstance>()
const paramFormRef = ref<FormInstance>()

// 状态
const state = reactive({
  activeTab: 'property',
  deviceTypeForm: {
    modelName: '',
    category: ''
  } as Partial<DeviceType>,
  modelForm: {
    modelName: '',
    category: '',
    properties: {} as Record<string, PropertyDefinition>,
    actions: {} as Record<string, ActionDefinition>,
    events: {} as Record<string, EventDefinition>
  } as BaseDeviceModel,
  propertyForm: {
    identify: '',
    type: 'string',
    unit: '',
    readOnly: false,
    min: undefined as number | undefined,
    max: undefined as number | undefined,
    enumValues: [] as string[],
    description: ''
  },
  actionForm: {
    identify: '',
    description: '',
    arguments: {} as Record<string, PropertyDefinition>
  },
  eventForm: {
    identify: '',
    description: '',
    level: 'info' as 'info' | 'warning' | 'error',
    fields: {} as Record<string, PropertyDefinition>
  },
  paramForm: {
    identify: '',
    type: 'string',
    unit: '',
    readOnly: false,
    min: undefined as number | undefined,
    max: undefined as number | undefined,
    enumValues: [] as string[],
    description: ''
  },
  submitting: false,
  propertyDialogVisible: false,
  actionDialogVisible: false,
  eventDialogVisible: false,
  paramDialogVisible: false,
  isPropertyEdit: false,
  isActionEdit: false,
  isEventEdit: false,
  isParamEdit: false,
  editingPropertyKey: '',
  editingActionKey: '',
  editingEventKey: '',
  editingParamKey: '',
  editingParamType: '' as 'argument' | 'field',
  currentDeviceTypeId: null as number | null
})

// 计算属性
const {
  activeTab, deviceTypeForm, modelForm, propertyForm, actionForm, eventForm, paramForm,
  submitting, propertyDialogVisible, actionDialogVisible, eventDialogVisible, paramDialogVisible,
  isPropertyEdit, isActionEdit, isEventEdit, isParamEdit, editingPropertyKey, editingActionKey,
  editingEventKey, editingParamKey, editingParamType, currentDeviceTypeId
} = toRefs(state)

const propertyList = computed(() => {
  return Object.entries(modelForm.value.properties || {}).map(([key, value]) => ({
    identify: key,
    ...value
  }))
})

const actionList = computed(() => {
  return Object.entries(modelForm.value.actions || {}).map(([key, value]) => ({
    identify: key,
    ...value
  }))
})

const eventList = computed(() => {
  return Object.entries(modelForm.value.events || {}).map(([key, value]) => ({
    identify: key,
    ...value
  }))
})

const argumentList = computed(() => {
  return Object.entries(actionForm.value.arguments || {}).map(([key, value]) => ({
    identify: key,
    ...value
  }))
})

const fieldList = computed(() => {
  return Object.entries(eventForm.value.fields || {}).map(([key, value]) => ({
    identify: key,
    ...value
  }))
})

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
  modelName: [
    { required: true, message: '请输入模型名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请输入品类', trigger: 'blur' }
  ]
}

// 验证规则
const propertyRules = {
  identify: [{ required: true, message: '请输入标识符', trigger: 'blur' }],
  description: [{ required: true, message: '请输入说明', trigger: 'blur' }],
  type: [{ required: true, message: '请选择数据类型', trigger: 'change' }]
}

const actionRules = {
  identify: [{ required: true, message: '请输入标识符', trigger: 'blur' }],
  description: [{ required: true, message: '请输入说明', trigger: 'blur' }]
}

const eventRules = {
  identify: [{ required: true, message: '请输入标识符', trigger: 'blur' }],
  description: [{ required: true, message: '请输入说明', trigger: 'blur' }],
  level: [{ required: true, message: '请选择级别', trigger: 'change' }]
}

// 处理数据类型变更
const handleTypeChange = (type: string) => {
  if (type === 'number') {
    propertyForm.value.min = 0
    propertyForm.value.max = 100
    propertyForm.value.unit = ''
  } else if (type === 'enum') {
    propertyForm.value.enumValues = ['']
  }
}

const handleParamTypeChange = (type: string) => {
  if (type === 'number') {
    paramForm.value.min = 0
    paramForm.value.max = 100
    paramForm.value.unit = ''
  } else if (type === 'enum') {
    paramForm.value.enumValues = ['']
  }
}



// 获取事件类型文本和标签类型
const getEventTypeText = (level: string) => {
  switch(level) {
    case 'info': return '信息'
    case 'warning': return '告警'
    case 'error': return '故障'
    default: return level
  }
}

const getEventTypeTag = (level: string) => {
  switch(level) {
    case 'info': return 'info'
    case 'warning': return 'warning'
    case 'error': return 'danger'
    default: return 'info'
  }
}

// 格式化V1属性规格
const formatV1PropertySpecs = (prop: PropertyDefinition) => {
  if (prop.type === 'number') {
    let str = ''
    if (prop.min !== undefined) str += `最小: ${prop.min}`
    if (prop.max !== undefined) str += (str ? ', ' : '') + `最大: ${prop.max}`
    if (prop.unit) str += (str ? ', ' : '') + `单位: ${prop.unit}`
    return str || '-'
  }
  if (prop.type === 'enum' && prop.enumValues) {
    return `枚举: ${prop.enumValues.join(', ')}`
  }
  return '-'
}

// 获取数据类型标签
const getDataTypeLabel = (type: string) => {
  const map: Record<string, string> = {
    'string': '字符串',
    'number': '数值',
    'boolean': '布尔值',
    'enum': '枚举',
    'object': '对象',
    'array': '数组'
  }
  return map[type] || type
}

// 初始化表单数据
const resetDeviceTypeForm = () => {
  deviceTypeForm.value = {
    modelName: '',
    category: ''
  }
}

// 初始化表单数据
const resetModelForm = () => {
  modelForm.value = {
    modelName: '',
    category: '',
    properties: {},
    actions: {},
    events: {}
  }
}

// 初始化属性表单
const initPropertyForm = () => {
  propertyForm.value = {
    identify: '',
    type: 'string',
    unit: '',
    readOnly: false,
    min: undefined,
    max: undefined,
    enumValues: [],
    description: ''
  }
}

// 初始化功能表单
const initActionForm = () => {
  actionForm.value = {
    identify: '',
    description: '',
    arguments: {}
  }
}

// 初始化事件表单
const initEventForm = () => {
  eventForm.value = {
    identify: '',
    description: '',
    level: 'info',
    fields: {}
  }
}

// 初始化参数表单
const initParamForm = () => {
  paramForm.value = {
    identify: '',
    type: 'string',
    unit: '',
    readOnly: false,
    min: undefined,
    max: undefined,
    enumValues: [],
    description: ''
  }
}

// 加载设备类型数据
const loadDeviceTypeData = async (id: number) => {
  try {
    // 从API获取设备类型数据
    const res: any = await getDeviceTypeById(id)
    if (res.data) {
      const deviceType = res.data
      // 加载基本信息
      deviceTypeForm.value.modelName = deviceType.modelName || ''
      deviceTypeForm.value.category = deviceType.category || ''
      
      // 加载模型数据
      if (deviceType.model) {
        modelForm.value = JSON.parse(JSON.stringify(deviceType.model))
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

// 保存模型（不再需要单独方法）

// 提交表单
const submitForm = async () => {
  if (!deviceTypeFormRef.value) return
  await deviceTypeFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        // 同步 modelForm 的基本信息
        modelForm.value.modelName = deviceTypeForm.value.modelName!
        modelForm.value.category = deviceTypeForm.value.category!

        const deviceTypeData = {
          ...deviceTypeForm.value,
          model: modelForm.value
        } as DeviceType

        if (isEditMode.value && deviceTypeId.value) {
          // 更新设备类型 (包含模型)
          await deviceTypeStore.updateDeviceType(deviceTypeId.value, deviceTypeData)
          ElMessage.success('更新成功')
        } else {
          // 创建设备类型
          const result = await deviceTypeStore.createDeviceType(deviceTypeData)
          if (result && result.id) {
            currentDeviceTypeId.value = result.id
            ElMessage.success('创建成功')
            // 如果是新建，可能需要跳转到编辑模式或列表
            router.push('/meta/deviceType/list')
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

const editProperty = (key: string) => {
  const prop = modelForm.value.properties[key]
  if (prop) {
    propertyForm.value = { identify: key, ...JSON.parse(JSON.stringify(prop)) }
    isPropertyEdit.value = true
    editingPropertyKey.value = key
    propertyDialogVisible.value = true
  }
}

const removeProperty = (key: string) => {
  delete modelForm.value.properties[key]
  ElMessage.success('属性已移除，保存表单后生效')
}

const submitPropertyForm = async () => {
  if (!propertyFormRef.value) return
  await propertyFormRef.value.validate((valid) => {
    if (valid) {
      const { identify, ...data } = propertyForm.value
      if (isPropertyEdit.value && editingPropertyKey.value !== identify) {
        delete modelForm.value.properties[editingPropertyKey.value]
      }
      modelForm.value.properties[identify] = data as PropertyDefinition
      propertyDialogVisible.value = false
    }
  })
}

// 操作相关方法
const addAction = () => {
  initActionForm()
  isActionEdit.value = false
  actionDialogVisible.value = true
}

const editAction = (key: string) => {
  const action = modelForm.value.actions[key]
  if (action) {
    actionForm.value = { identify: key, ...JSON.parse(JSON.stringify(action)) }
    isActionEdit.value = true
    editingActionKey.value = key
    actionDialogVisible.value = true
  }
}

const removeAction = (key: string) => {
  delete modelForm.value.actions[key]
  ElMessage.success('操作已移除，保存表单后生效')
}

const submitActionForm = async () => {
  if (!actionFormRef.value) return
  await actionFormRef.value.validate((valid: boolean) => {
    if (valid) {
      const { identify, ...data } = actionForm.value
      if (isActionEdit.value && editingActionKey.value !== identify) {
        delete modelForm.value.actions[editingActionKey.value]
      }
      modelForm.value.actions[identify] = data as ActionDefinition
      actionDialogVisible.value = false
    }
  })
}

// 事件相关方法
const addEvent = () => {
  initEventForm()
  isEventEdit.value = false
  eventDialogVisible.value = true
}

const editEvent = (key: string) => {
  const event = modelForm.value.events[key]
  if (event) {
    eventForm.value = { identify: key, ...JSON.parse(JSON.stringify(event)) }
    isEventEdit.value = true
    editingEventKey.value = key
    eventDialogVisible.value = true
  }
}

const removeEvent = (key: string) => {
  delete modelForm.value.events[key]
  ElMessage.success('事件已移除，保存表单后生效')
}

const submitEventForm = async () => {
  if (!eventFormRef.value) return
  await eventFormRef.value.validate((valid: boolean) => {
    if (valid) {
      const { identify, ...data } = eventForm.value
      if (isEventEdit.value && editingEventKey.value !== identify) {
        delete modelForm.value.events[editingEventKey.value]
      }
      modelForm.value.events[identify] = data as EventDefinition
      eventDialogVisible.value = false
    }
  })
}

// 参数/字段相关方法
const addParam = (type: 'argument' | 'field') => {
  initParamForm()
  isParamEdit.value = false
  editingParamType.value = type
  paramDialogVisible.value = true
}

const editParam = (type: 'argument' | 'field', key: string) => {
  const targetMap = type === 'argument' ? actionForm.value.arguments : eventForm.value.fields
  const param = targetMap[key]
  if (param) {
    paramForm.value = { identify: key, ...JSON.parse(JSON.stringify(param)) }
    isParamEdit.value = true
    editingParamType.value = type
    editingParamKey.value = key
    paramDialogVisible.value = true
  }
}

const removeParam = (type: 'argument' | 'field', key: string) => {
  if (type === 'argument') {
    delete actionForm.value.arguments[key]
  } else {
    delete eventForm.value.fields[key]
  }
}

const submitParamForm = async () => {
  if (!paramFormRef.value) return
  await paramFormRef.value.validate((valid: boolean) => {
    if (valid) {
      const { identify, ...data } = paramForm.value
      const targetMap = editingParamType.value === 'argument' ? actionForm.value.arguments : eventForm.value.fields
      
      if (isParamEdit.value && editingParamKey.value !== identify) {
        delete targetMap[editingParamKey.value]
      }
      
      targetMap[identify] = data as PropertyDefinition
      paramDialogVisible.value = false
    }
  })
}

// 移除旧的watch，改为在操作时直接保存

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

.basic-info-card {
  margin-bottom: 20px;
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

.enum-list-container {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.enum-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  gap: 8px;
}

.enum-add-btn {
  margin-top: 4px;
}

.params-section {
  margin-bottom: 20px;
}

.tab-content-wrapper {
  padding: 10px 0;
}

.tab-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-bottom: 16px;
}

.table-ops {
  display: flex;
  justify-content: center;
  gap: 8px;
}

.spec-text {
  font-size: 13px;
  color: #606266;
}

.action-name-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.arg-preview-list {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.arg-tag {
  cursor: help;
  background-color: #f0f9eb;
  border-color: #e1f3d8;
  color: #67c23a;
}

.no-args-text {
  color: #909399;
  font-size: 12px;
  font-style: italic;
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
