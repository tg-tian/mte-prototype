<template>
  <el-table ref="tableRef" :data="data" height="auto" border stripe style="width: 100%">
    <el-table-column type="selection" width="55"  v-if="canChoose"/>
    <el-table-column  v-for="col in header" :key="col.code" :prop="col.code" :label="col.name">
      <template v-slot="scope">
        <img
            v-if="col.type === 'Image'"
            :src="scope.row[col.code]"
            alt="Image"
            style="width:50px;height: 50px;cursor: pointer"
            @click="handleImageClick(scope.row[col.code])"
        />
        <el-button link @click="handleLinkClick(scope.row[col.code])" style="color: #50a5fb" v-if="col.type === 'Link'">{{scope.row[col.code]}}</el-button>
        <span v-else>{{ scope.row[col.code] }}</span>
      </template>
    </el-table-column>
    <el-table-column label="操作" v-if="canDelete || canEdit">
      <template #default="scope">
        <el-button size="small" @click="handleEdit(scope.$index, scope.row)" v-if="canEdit">
          编辑
        </el-button>
        <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)"
            v-if="canDelete"
        >
          删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  <div style="margin-top: 20px;" v-if="canChoose">
    <div class="domain-subtitle" style="display: flex;justify-content: space-between">
      <el-button type="primary"  style="margin-left: auto;" plain>保存选择</el-button>
      <el-button type="primary"  style="margin-right: auto;" @click="clearSelection()" plain>清除选择</el-button>
    </div>
  </div>

  <!-- 图片放大弹出框 -->
  <el-dialog v-model="imgVisible" width="50%" @close="handleDialogClose">
    <el-image :src="selectedImage" alt="Expanded Image" style="width: 100%" />
  </el-dialog>
</template>

<script setup lang="ts">

const props = defineProps({
  data: Array,
  header: Array<any>,

  canChoose:{
    type: Boolean,
    default: false
  },
  canEdit:{
    type: Boolean,
    default: true
  },
  canDelete:{
    type: Boolean,
    default: true
  }
});
//设置表格组件
const tableRef = ref<any>(null);

/**
 * ref用于获取元素结点
 * 在vue3中 想要获取 ref，我们只能通过声明变量的方式
 * 如上面的 const tableRef = ref()，就是在用声明变量的方式来获取表格的元素结点
 * 变量名称必须要与 ref 命名的属性名称一致。
 * */


interface State{
  imgVisible: boolean;
  selectedImage: String
}
const state = reactive<State>({
  imgVisible: false,
  selectedImage: ''
})
const {imgVisible, selectedImage} = toRefs(state)

//清空表格选择的情况
const clearSelection=() =>{
  tableRef.value.clearSelection();
}

const emit = defineEmits(['handleEdit', 'handleDelete', 'handleLinkClick']);

const handleEdit = (index, row) => {
  emit('handleEdit', row);
};

const handleDelete = (index, row) => {
  emit('handleDelete', row);
};

const handleImageClick = (imageUrl)=>{
  selectedImage.value = imageUrl;
  imgVisible.value = true;
}
const handleLinkClick = (link)=>{
  emit('handleLinkClick', link)
}
const handleDialogClose = ()=>{
  selectedImage.value=''
}
</script>
