<template>
  <div style="padding: 20px;">
    <div>
      <div style="text-align: center;margin-bottom: 20px">咖啡机器人A</div>
      <img :src="imageUrl" alt="device Image" />
    </div>
    <div style="line-height: 32px">
      <div>当前状态：{{currentStatus}}</div>
      <div>当前操作：{{currentOperation}}</div>
    </div>
  </div>
</template>
<script setup lang="ts">
import {getMockDeviceInformationA} from "@/api/deviceApi";

onMounted(()=>{
  setInterval(function(){
    getDeviceInformation()
  }, 2000);
})

interface State {
  currentStatus: String;
  currentOperation: String;
  requestUrl: String;
}

const state = reactive<State>({
  currentStatus: '-',
  currentOperation: '-'
})
const { currentStatus, currentOperation } = toRefs(state)

const imageUrl = computed(() => {
  return new URL(`../assets/images/${currentStatus.value === "-" ? "空闲" : currentStatus.value}.png`, import.meta.url).href;
});

const getDeviceInformation = () => {
  getMockDeviceInformationA().then((res: any) => {
    if (res.status === 200){
      currentStatus.value = res.data.status
      currentOperation.value = res.data.currentOperation === "" ? "无" : res.data.currentOperation
    }
  })
}
</script>
