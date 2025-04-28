<template>
    <el-button type="primary" style="float: right;margin-bottom: 10px" @click="openDialog">添加模板</el-button>
    <el-empty description="暂无模板" v-if="filteredDomainTemplates.length===0"/>
    <el-table
      v-loading="domainTemplateStore.loading"
      :data="filteredDomainTemplates"
      style="width: 100%; margin-top: 20px"
      border
      v-else
    >
      <el-table-column prop="name" label="模板名称" width="150"></el-table-column>
      <el-table-column prop="category" label="模板类型" width="150"></el-table-column>
      <el-table-column prop="description" label="模板描述" min-width="200"></el-table-column>
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <el-button type="primary" size="small" @click="navigateToTemplate(scope.row)">查看</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogDetailVisible"
    title="模板详情"
    width="50%"
    >
        <div>
            <div class="template_item">
                <div class="template_key">模板ID：</div>
                <div class="template_value">{{ domainTemplateStore.currentTemplate.template_id }}</div>
            </div>
            <div class="template_item">
                <div class="template_key">模板名：</div>
                <div class="template_value">{{ domainTemplateStore.currentTemplate.name }}</div>
            </div>
            <div class="template_item">
                <div class="template_key">模板描述：</div>
                <div class="template_value">{{ domainTemplateStore.currentTemplate.description }}</div>
            </div>
            <div class="template_item">
                <div class="template_key">模板类别：</div>
                <div class="template_value">{{ domainTemplateStore.currentTemplate.category }}</div>
            </div>
            <div class="template_item">
                <div class="template_key">模板标签：</div>
                <div class="template_value">{{ domainTemplateStore.currentTemplate.tags }}</div>
            </div>
            <div class="template_item">
                <div class="template_key">业务标签：</div>
                <div class="template_value">{{ domainTemplateStore.currentTemplate.domain }}</div>
            </div>
            <div class="template_item">
                <div class="template_key">DSL/平台：</div>
                <div class="template_value">{{ domainTemplateStore.currentTemplate.describing_the_model }}</div>
            </div>
            <div class="template_item">
                <div class="template_key">描述详情：</div>
                <div class="template_value">
                  <a :href="domainTemplateStore.currentTemplate.url.substring(0, domainTemplateStore.currentTemplate.url.length-5)" target="_blank">
                    {{ domainTemplateStore.currentTemplate.url }}
                  </a>
                </div>
            </div>
            <el-image :src="domainTemplateStore.currentTemplate.image_url" />
        </div>
    </el-dialog>

    <el-dialog v-model="dialogVisible"
    title="模板列表"
    width="50%"
    >
    <div class="search-container" style="margin-bottom: 20px;">
        <el-form :inline="true">
            <el-form-item style="width: 400px;">
                <el-input
                    v-model="state.searchQuery.name_or_category_or_description_or_domain_or_tags_or_code_key_word_string_cont"
                    placeholder="请输入搜索内容"
                    clearable
                    @clear="handleSearch"
                >
                    <template #prefix>
                        <el-icon><Search /></el-icon>
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="handleSearch">搜索</el-button>
                <el-button @click="resetSearch">重置</el-button>
                <el-button link @click="state.showAdvanced = !state.showAdvanced">
                    {{ state.showAdvanced ? '收起' : '展开' }}高级搜索
                </el-button>
            </el-form-item>
            <el-form-item>
                <el-collapse-transition>
                    <div v-show="state.showAdvanced" style="display: flex; flex-wrap: wrap; gap: 20px;">
                        <el-form-item label="模板名称" style="width: 400px;">
                            <el-input
                                v-model="state.searchQuery.name_cont"
                                placeholder="请输入模板名称"
                                clearable
                                @clear="handleSearch"
                            >
                                <template #prefix>
                                    <el-icon><Document /></el-icon>
                                </template>
                            </el-input>
                        </el-form-item>
                        <el-form-item label="模板描述" style="width: 400px;">
                            <el-input
                                v-model="state.searchQuery.description_cont"
                                placeholder="请输入模板描述"
                                clearable
                                @clear="handleSearch"
                            >
                                <template #prefix>
                                    <el-icon><Document /></el-icon>
                                </template>
                            </el-input>
                        </el-form-item>
                        <el-form-item label="模板标签" style="width: 400px;">
                            <el-input
                                v-model="state.searchQuery.tags_cont"
                                placeholder="请输入模板标签"
                                clearable
                                @clear="handleSearch"
                            >
                                <template #prefix>
                                    <el-icon><Collection /></el-icon>
                                </template>
                            </el-input>
                        </el-form-item>
                    </div>
                </el-collapse-transition>
            </el-form-item>
        </el-form>
    </div>
    <div style="max-height: 400px; overflow-y: scroll;">
        <div v-for="(item, index) in filteredTemplates" :key="item.id">
            <TemplateCard 
                :template="item" 
                :reset-selected="resetSelected"
                @template-click="handleSelectTemplate"
            />
        </div>
    </div>
    <div style="display: flex;justify-content: center; margin-top: 20px;">
        <el-button 
            link 
            :loading="domainTemplateStore.loading"
            @click="loadMore"
            v-if="domainTemplateStore.allTemplates.length > 0 && domainTemplateStore.hasMore"
        >
            加载更多
        </el-button>
        <div v-else-if="domainTemplateStore.allTemplates.length > 0" style="color: #909399;">
            没有更多数据了
        </div>
    </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="addTemplate">
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>
</template>

<script setup lang="ts">
import { useDomainTemplateStore } from '@/store/domainTemplate'
import TemplateCard from './TemplateCard.vue';
import { Search, Document, Collection } from '@element-plus/icons-vue'
const route = useRoute()
const router = useRouter()
const domainTemplateStore = useDomainTemplateStore()

const domainId = computed(() => {
  return route.query.domainId ? parseInt(route.query.domainId as string) : null
})

const state = reactive({
    dialogVisible: false,
    dialogDetailVisible: false,
    selectedTemplates: [],
    showAdvanced: false,
    searchQuery: {
        name_or_category_or_description_or_domain_or_tags_or_code_key_word_string_cont: '',
        name_cont: '',
        description_cont: '',
        tags_cont: ''
    },
    resetSelected: false
})
const { dialogVisible, selectedTemplates, dialogDetailVisible, showAdvanced, searchQuery, resetSelected } = toRefs(state)

const currentPage = ref(1)

// 初始化
onMounted(async () => {
    try {
        const domain = route.query.domainId ? parseInt(route.query.domainId as string) : null
        domainTemplateStore.setCurrentDomain(domain)
        await domainTemplateStore.fetchTemplates(domain)
    } catch (error) {
        console.error('加载模板列表失败:', error)
    }
})

watch([() => route.query.domainId], async ([newDomainId]) => {
    try {
        const newDomain = newDomainId ? parseInt(newDomainId as string) : null
        domainTemplateStore.setCurrentDomain(newDomain)
        await domainTemplateStore.fetchTemplates(newDomain)
    } catch (error) {
        console.error('加载模板列表失败:', error)
    }
})

const openDialog = async ()=>{
    dialogVisible.value=true
    selectedTemplates.value = []
    resetSelected.value = true
    currentPage.value = 1
    resetSearch()
    try {
        await domainTemplateStore.fetchAllTemplates(1)
    }catch(error) {
        ElMessage.error("模板库模板获取失败")
    }
    // 重置后立即设置回 false，避免影响后续的选择
    nextTick(() => {
        resetSelected.value = false
    })
}

const handleSelectTemplate = (id: number, isSelected: boolean)=>{
    if(isSelected && !selectedTemplates.value.includes(id)){
        selectedTemplates.value.push(id)
    }else{
        selectedTemplates.value = selectedTemplates.value.filter((templateId) => templateId!==id)
    }
}

const addTemplate = async () => {
    if(selectedTemplates.value.length === 0){
        ElMessage.warning("请选择要添加的模板")
    } else {
        try {
          const selectedData = domainTemplateStore.allTemplates.filter((item)=>selectedTemplates.value.includes(item.id))
          await domainTemplateStore.bindingTemplates(domainId.value, selectedData)
          ElMessage.success("添加成功")
          dialogVisible.value = false
        }catch(error) {
          ElMessage.error("添加失败")
        }
    }
}

const loadMore = async () => {
    currentPage.value++
    try {
        await domainTemplateStore.fetchAllTemplates(currentPage.value, searchQuery.value)
    } catch (error) {
        ElMessage.error("加载更多失败")
        currentPage.value--
    }
}

// 过滤后的领域模板列表
const filteredDomainTemplates = computed(() => {
  if (!domainTemplateStore.templates) return []
  
  let templates = domainTemplateStore.templates

  return templates
})

// 过滤已绑定该领域的模板后的模板库模板列表
const filteredTemplates = computed(() => {
  if (!domainTemplateStore.allTemplates) return []

  const domainTemplateIds = filteredDomainTemplates.value.map((template)=>template.template_id)
  
  let templateList = domainTemplateStore.allTemplates.filter((template)=>!domainTemplateIds.includes(template.id))

  return templateList
})

//查看模板详情
const navigateToTemplate = (template: any) => {
  domainTemplateStore.setCurrentTemplate(template)
  dialogDetailVisible.value = true
}

// 删除领域绑定信息
const handleDelete = (row: any) => {
  ElMessageBox.confirm(
    `确定要取消绑定模板 "${row.name}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
  .then(async () => {
    try {
      await domainTemplateStore.unbindingTemplates(domainId.value, row.id)
      ElMessage.success('取消绑定成功')
    } catch (error) {
      ElMessage.error('取消绑定失败')
    }
  })
  .catch(() => {
    // 用户取消操作
  })
}

const handleSearch = async () => {
    currentPage.value = 1
    try {
        await domainTemplateStore.fetchAllTemplates(1, searchQuery.value)
    } catch (error) {
        ElMessage.error("搜索失败")
    }
}

const resetSearch = () => {
    currentPage.value = 1
    searchQuery.value = {
        name_or_category_or_description_or_domain_or_tags_or_code_key_word_string_cont: '',
        name_cont: '',
        description_cont: '',
        tags_cont: ''
    }
    handleSearch()
}
</script>