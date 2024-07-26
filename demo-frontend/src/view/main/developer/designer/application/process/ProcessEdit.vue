<template>
  <div style="padding: 20px">
    <div>流程编辑页面</div>
    <div style="margin-top: 10px;margin-bottom: 10px">
      <el-button type="primary" @click="saveFile">保存</el-button>
      <el-button type="primary">发布</el-button>
    </div>
    <!-- 配置节点（选择设备及操作后，会对应加载该操作需要的参数） -->
    <VAceEditor
        v-model:value="content"
        lang="json"
        theme="monokai"
        :options="options"
        class="vue-ace-editor">
    </VAceEditor>
  </div>
</template>
<script setup lang="ts">
import { VAceEditor } from "vue3-ace-editor";
import "@/aceConfig.js";
import type { Ace } from "ace-builds";
import {getFileData, saveFileData} from "@/api/fileApi";

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
  height: 600px;
  width: 70%;
  font-size: 16px;
  border: 1px solid;
}
</style>
