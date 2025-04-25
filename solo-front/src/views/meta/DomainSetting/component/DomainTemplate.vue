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
      <el-table-column prop="code" label="模板编码" width="150"></el-table-column>
      <el-table-column prop="name" label="模板名称" min-width="150"></el-table-column>
      <el-table-column prop="description" label="模板描述" min-width="200"></el-table-column>
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <el-button type="primary" size="small" @click="navigateToTemplate(scope.row)">查看</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible"
    title="模板列表"
    width="50%"
    >
    <div>
        <div v-for="(item, index) in filteredTemplates">
            <TemplateCard :template="item" @template-click="handleSelectTemplate"/>
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
const route = useRoute()
const router = useRouter()
const domainTemplateStore = useDomainTemplateStore()

const domainId = computed(() => {
  return route.query.domainId ? parseInt(route.query.domainId as string) : null
})

const state = reactive({
    dialogVisible: false,
    selectedTemplates: []
})
const { dialogVisible, selectedTemplates } = toRefs(state)

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
    try {
        await domainTemplateStore.fetchAllTemplates()
    }catch(error) {
        ElMessage.error("模板库模板获取失败")
    }
}

const handleSelectTemplate = (uuid: string, isSelected: boolean)=>{
    if(isSelected){
        selectedTemplates.value.push(uuid)
    }else{
        selectedTemplates.value = selectedTemplates.value.filter((templateId) => templateId!==uuid)
    }
}

const addTemplate = async () => {
    if(selectedTemplates.value.length === 0){
        ElMessage.warning("请选择要添加的模板")
    } else {
        try {
            await domainTemplateStore.bindingTemplates(domainId.value, selectedTemplates.value)
            ElMessage.success("添加成功")
        }catch(error) {
            ElMessage.error("添加失败")
        }
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

  const domainTemplateIds = filteredDomainTemplates.value.map((template)=>template.uuid)
  
  let templateList = domainTemplateStore.allTemplates.filter((template)=>!domainTemplateIds.includes(template.uuid))

  return templateList
})

//查看模板详情
const navigateToTemplate = (template?: any) => {
  if (template) {
    //TODO: 模板详情（弹窗展示详细信息or跳转到模板库详情页面
  }
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
</script>