<template>
    <div class="header">
      <div class="header-title" style="cursor: pointer" @click="router.push({path:'/'})">
        <div style="line-height: 32px; padding-left: 10px">{{ title }}</div>
      </div>
    </div>
  </template>
  
<script setup lang="ts">
  interface State {
    title: string
  }
  
  const state = reactive<State>({
    title: '面向场景计算的低代码平台'
  })
  const {title}=toRefs(state)

  const router = useRouter()
  watchEffect(() => {
    const path = router.currentRoute.value.path
    if(path.startsWith('/domain')){
      title.value='领域低代码开发平台'
    } else if(path.startsWith('/scene')){
      title.value='场景低代码开发平台'
    }else{
      title.value='面向场景计算的低代码元工具平台'
    }
  })
  </script>
  
  <style>
  .header {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #ecf4fc;
  }
  
  .header-title {
    text-align: center;
     /*color: #337ecc; */
    font-size: 18px;
    font-weight: 600;
    font-family: 'Courier New', Courier, monospace;
    display: flex;
    justify-content: space-evenly;
    margin-left: 20px;
  }
  
  .header-right {
    margin-right: 20px;
    display: flex;
  }
  
  .el-dropdown-link {
    cursor: pointer;
    font-size: 1rem;
    color: white;
    align-items: center;
    display: flex;
  }
  </style>
  