<template>
  <div class="file-item">
    <div class="file-card-item" :class="{ 'file-card-item-selected': canSelect && cardItem.isSelected }" @mouseover="isHovered = true" @mouseleave="isHovered = false">
      <div style="z-index: 3" v-if="canSelect">
        <el-checkbox v-bind="cardItem.isSelected" @change="handleCheckboxChange" class="checkbox" v-show="isHovered || cardItem.isSelected" size="large"></el-checkbox>
      </div>
      <div style="z-index: 1; margin: 15px" @click="handleItemClick">
        <el-image :src="cardItem.imageUrl" class="folder-icon"></el-image>
      </div>

      <el-dropdown trigger="click" class="more-options" @command="handleDropdownItemClick" v-if="dropDownItems.length !== 0">
        <div >
          <el-button v-if="isHovered">
            <el-icon><More /></el-icon></el-button>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item v-for="item in dropDownItems" :key="item.code" :command="item.code">{{item.name}}</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
    <div class="file-name">{{cardItem.name}}</div>
  </div>
</template>

<script setup lang="ts">
import {More} from "@element-plus/icons-vue";

const props = defineProps({
  cardItem: Object,
  dropDownItems: {
    type: Array,
    default: []
  },
  canSelect: {
    type: Boolean,
    default: false
  }
});
const emit = defineEmits(['update:isSelected', 'commandClick', 'itemClick']);

const handleCheckboxChange = (value) => {
  emit('update:isSelected', value);
};

interface State {
  isHovered: Boolean;
}
const state = reactive<State>({
  isHovered: false
})
const {isHovered} = toRefs(state)

const handleItemClick = () => {
  isHovered.value = false
  emit('itemClick')
}

const handleDropdownItemClick = (item) => {
  // 处理菜单项点击事件逻辑
  emit('commandClick', item)
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
