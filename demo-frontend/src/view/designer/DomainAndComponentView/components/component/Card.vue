<template>
  <div class="file-item">
    <div class="file-card-item" :class="{ 'file-card-item-selected': isSelected }" @mouseover="isHovered = true" @mouseleave="isHovered = false">
      <div style="z-index: 3">
        <el-checkbox v-bind="isSelected" @change="handleCheckboxChange" class="checkbox" v-show="isHovered || isSelected" size="large"></el-checkbox>
      </div>
      <div style="z-index: 1; margin: 15px">
        <el-image :src="imageUrl" class="folder-icon"></el-image>
      </div>

      <el-dropdown trigger="click" class="more-options" @command="handleDropdownItemClick">
        <div >
          <el-button v-if="isHovered">
            <el-icon><More /></el-icon></el-button>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="rename">重命名</el-dropdown-item>
            <el-dropdown-item command="delete">删除</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
    <div class="file-name">{{name}}</div>
  </div>
</template>

<script setup lang="ts">
import {More} from "@element-plus/icons-vue";
import { defineProps, defineEmits } from 'vue';


const props = defineProps({
  imageUrl: String,
  name: String,
  isSelected: Boolean
});
const emit = defineEmits(['update:isSelected']);

const handleCheckboxChange = (value) => {
  emit('update:isSelected', value);
};

interface State {
  isHovered: Boolean;
  dropdownItems: any[]
}
const state = reactive<State>({
  isHovered: false,
  dropdownItems: [
    {
      id: 'rename',
      label: '重命名'
    },
    {
      id: 'delete',
      label: '删除'
    },
  ]
})
const {isHovered, dropdownItems} = toRefs(state)

const handleDropdownItemClick = (item) => {
  console.log('Clicked item:', item);
  // 处理菜单项点击事件逻辑
};
</script>

<style scoped>
.file-item {
  width: 165px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  overflow: auto;
}

.file-card-item {
  width: 140px;
  height: 140px;
  background-color: #f4f4fc;
  border-radius: 8px;
  position: relative;
  padding: 10px;
}

.file-card-item-selected {
  border: 2px solid #409eff;
}

.file-card-item:hover {
  background-color: #d6d6d6;
  cursor: pointer;
}

.folder-icon {
  width: 100%;
}

.checkbox {
  position: absolute;
  top: 0px;
  left: 10px;
}

.more-options {
  position: absolute;
  bottom: 20px;
  right: 10px;
}

.file-name {
  margin-top: 10px;
  text-align: center;
}
</style>
