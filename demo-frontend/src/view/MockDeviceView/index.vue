<template>
  <div style="padding: 20px;display: flex">
    <div>
      <div style="margin-left: 30%;margin-bottom: 20px">咖啡机器人</div>
      <img :src="imageUrl" alt="device Image" />
    </div>
    <div style="line-height: 32px">
      <div>当前状态：{{currentStatus}}</div>
      <div>当前操作：{{currentOperation}}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import {getMockDeviceInformation} from "../../api/mockDeviceApi";

  onMounted(()=>{
    setInterval(function(){
      getDeviceInformation()
    }, 2000);
  })

  interface State {
    currentStatus: String;
    currentOperation: String;
  }

 const state = reactive<State>({
   currentStatus: '-',
   currentOperation: '-'
 })
  const { currentStatus, currentOperation } = toRefs(state)

  const imageUrl = computed(() => {
    return new URL(`../../assets/images/${currentStatus.value === "-" ? "空闲" : currentStatus.value}.png`, import.meta.url).href;
  });

  const getDeviceInformation = () => {
    getMockDeviceInformation().then((res: any) => {
      if (res.status === 200){
        currentStatus.value = res.data.status
        currentOperation.value = res.data.currentOperation === "" ? "无" : res.data.currentOperation
      }
    })

  }

</script>
