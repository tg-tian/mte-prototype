<template>
  <el-table :data="data" border stripe style="width: 100%">
    <el-table-column type="selection" width="55"  v-if="canChoose"/>
    <el-table-column v-for="col in header" :key="col.code" :prop="col.code" :label="col.name">
      <template v-slot="scope">
        <img
            v-if="col.type === 'Image'"
            :src="scope.row[col.code]"
            alt="Image"
            style="width:50px;height: 50px;cursor: pointer"
            @click="handleImageClick(scope.row[col.code])"
        />
        <span v-else>{{ scope.row[col.code] }}</span>
      </template>
    </el-table-column>
    <el-table-column label="操作">
      <template #default="scope">
        <el-button size="small" @click="handleEdit(scope.$index, scope.row)">
          编辑
        </el-button>
        <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)"
        >
          删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  <!-- 图片放大弹出框 -->
  <el-dialog v-model="imgVisible" width="50%" @close="handleDialogClose">
    <img :src="selectedImage" alt="Expanded Image" style="width: 100%">
  </el-dialog>
</template>

<script setup lang="ts">

const props = defineProps({
  data: Array,
  header: Array,

  canChoose:{
    type: Boolean,
    default: false
  }
});

interface State{
  imgVisible: boolean;
  selectedImage: String
}
const state = reactive<State>({
  imgVisible: false,
  selectedImage: ''
})
const {imgVisible, selectedImage} = toRefs(state)

const emit = defineEmits(['handleEdit', 'handleDelete']);

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
const handleDialogClose = ()=>{
  selectedImage.value=''
}

onActivated(()=>{
  console.log(props.canChoose)
})
</script>
