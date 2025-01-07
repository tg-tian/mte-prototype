<template>
  <div class="header-box">
    <div class="header-title">
      <el-icon class="header-icon"><Menu /></el-icon>
      <div>{{title}}</div>
    </div>
    <div style="display: flex;margin-right: 10px">
      <div v-for="item in buttonGroup" style="margin-right: 10px">
        <ElButton :type="item.type" @click="handleClick(item.code)"> {{item.name}}</ElButton>
      </div>
    </div>
  </div>
</template>


<script setup lang="ts">
import {Menu} from "@element-plus/icons-vue";

interface ButtonProps {
  code: string,
  name: string,
  type: string
}

const props = defineProps({
  title: String,
  buttonGroup: Array<ButtonProps>
});

const emit = defineEmits(['button-click']);

const handleClick = (code: string) => {
  emit('button-click', code)
}
</script>

<style scoped>

.header-title{
  height: 50%;
  align-items: center;
  display: flex;
}
.header-icon{
  color: #7F51C8;
  opacity: 0.8;
  margin: 10px;
  font-size: 30px;
}
.header-box {
  width: 100%;
  height: 60px;
  display: flex;
  background-color: #F1F5FB;
  flex-direction: row;
  align-items: center;
  overflow: auto;
  border-radius: 9px;
  justify-content: space-between;
}
</style>

