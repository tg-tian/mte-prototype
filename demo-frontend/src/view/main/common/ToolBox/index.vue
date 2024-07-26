<template>
  <div>
    <div v-for="itemCategory in itemList" :key="itemCategory.type">
      <div class="sub-title">{{itemCategory.name}}</div>
      <div style="display: flex;flex-wrap: wrap;gap: 2px">
        <div v-for="item in itemCategory.items" :key="item.id" class="tool-item">
          <div class="tool-icon">
            <el-image :src="requireIcon(item.icon ?? '')" style="width: 70%;margin: 15%;"></el-image>
          </div>
          <div class="tool-name">{{item.name}}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ToolboxCategory} from "@/view/main/common/ToolBox/toolboxTypes";

const props = defineProps({
  toolboxJson: Array
});

const itemList = ref<ToolboxCategory[]>(props.toolboxJson);

interface State{
  icons: any
}

const state = reactive<State>({
  icons: {}
})
const {icons} = toRefs(state)

onMounted(()=>{
  icons.value = import.meta.glob('@/assets/icon/*');
})

const requireIcon = (iconPath) => {
  if (iconPath === ""){
    return new URL('@/assets/logo.png', import.meta.url).href
  }else {
    const absolutePath = '../../../../assets/icon/'+iconPath
    return new URL(absolutePath, import.meta.url).href
  }
}
</script>

<style scoped>
.sub-title {
  color: grey;
  margin-top: 10px;
  font-size: 14px;
  margin-bottom: 5px;
}

.tool-item {
  text-align: center;
  padding: 10px;
}

.tool-icon {
  border: #d6d6d6 1px solid;
  width: 60px;
  height: 60px;
}

.tool-name {
  font-size: 14px;
  margin-top: 5px;
}
</style>
