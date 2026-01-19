<template>
  <PageHeader title="质量保障工具"/>
  <div class="body-box">
    <div class="tool-button" >
      <el-image src="./src/assets/icon/analysis.png" alt="静态分析" @click="StaticAnalysisClick" />
      <span>静态分析工具</span>
    </div>
    <div class="tool-button" >
      <el-image src="./src/assets/icon/bug.png" alt="缺陷检测" @click="BugAnalysisClick" />
      <span>缺陷检测工具</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import PageHeader from "@/view/main/common/PageHeader.vue";

const StaticAnalysisClick = ()=>{
  console.log("do the StaticAnalysis");
}

const BugAnalysisClick =() => {
  console.log("do the BugAnalysisClick");
}
</script>

<style scoped>
.body-box{
  flex-direction: row;
  display: flex;
  height: 90%;
  margin-top: 10px;
  border:solid #eff0f2;
  align-items: center;
  justify-content:center;
  border-radius: 9px;
}
.tool-button{
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content:center;
  height: 250px;
  width: 250px;
  margin: 30px;
  background-color: #f1f5f9;
  border-radius: 4px;
  font-size: large;
}
</style>