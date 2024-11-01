<template>
  <div style="display: flex;flex-direction: column;height: 100%">
    <div class="process-header">
      <div>{{processName}}-流程编辑页面</div>
      <div>
        <el-button type="primary" @click="saveFile">保存</el-button>
        <el-button type="primary">发布</el-button>
      </div>
    </div>
    <div class="process-content">
      <div style="border: 1px solid lightgray; padding: 20px;width: 25%;overflow-y: auto">
        <div style="margin-bottom: 10px">工具栏</div>
        <el-input
            placeholder="搜索"
            :prefix-icon="Search"
        />
        <ToolBox :toolboxJson="processTool" designerType="Process"/>
      </div>

      <div style="border: 1px solid lightgray; padding: 20px;margin-left: 20px;width: 50%;overflow-y: auto">
        <!-- 配置节点（选择设备及操作后，会对应加载该操作需要的参数） -->
        <VAceEditor
            v-model:value="content"
            lang="json"
            theme="monokai"
            :options="options"
            class="vue-ace-editor">
        </VAceEditor>
      </div>

      <div style="border: 1px solid lightgray; padding: 20px;margin-left: 20px;width: 20%;overflow-y: auto">
        属性配置区

        <el-form>
          <el-form-item label="id" class="attribute-item">
            <el-input placeholder="请输入"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import ToolBox from '@/view/main/common/ToolBox/index.vue'
import { VAceEditor } from "vue3-ace-editor";
import "@/aceConfig.js";
import type { Ace } from "ace-builds";
import {getFileData, saveFileData} from "@/api/fileApi";
import processTool from './processTool.json'
import {Search} from "@element-plus/icons-vue";

interface State{
  processId: String;
  processName: String;
}
const state = reactive<State>({
  processId: '',
  processName: '',
})
const {processId, processName} = toRefs(state)

const router = useRouter()
watchEffect(() => {
  if (typeof router.currentRoute.value.query.processId === 'string') {
    processId.value = router.currentRoute.value.query.processId || ''
  }
  if (typeof router.currentRoute.value.query.processName === 'string') {
    processName.value = router.currentRoute.value.query.processName || ''
  }
})

const content = ref(''); // 显示的内容

const options: Partial<Ace.EditorOptions> = reactive({
  useWorker: true, // 启用语法检查,必须为true
  enableBasicAutocompletion: true, // 自动补全
  enableLiveAutocompletion: true, // 智能补全
  enableSnippets: true, // 启用代码段
  showPrintMargin: false, // 去掉灰色的线，printMarginColumn
  highlightActiveLine: true, // 高亮行
  highlightSelectedWord: true, // 高亮选中的字符
  tabSize: 2, // tab锁进字符
  fontSize: 14, // 设置字号
  wrap: false, // 是否换行
  readOnly: false, // 是否可编辑
});

onMounted(async ()=>{
  const result = await getFileData("/definition/ConferenceService.proc")
  content.value = JSON.stringify(result.data, null, 2)
})

const saveFile=()=>{
  console.log('save')
  saveFileData("/definition/ConferenceService.proc", content.value)  //调用后端数据
}
</script>

<style scoped>
.vue-ace-editor {
  /* ace-editor默认没有高度，所以必须设置高度，或者同时设置最小行和最大行使编辑器的高度自动增高 */
  height: 99%;
  width: 99%;
  font-size: 16px;
  border: 1px solid;
}

.process-header {
  display: flex;
  justify-content: space-between;
  border-bottom: lightgray 1px solid;
  padding: 10px;
  margin: 20px 20px 10px;
}

.process-content {
  display: flex;
  flex: 1;
  overflow: hidden;
  margin: 20px;
}

.attribute-item {
  margin-top: 10px;
}
</style>
