<template>
  <el-breadcrumb separator="/" class="scenario-breadcrumb">
    <el-breadcrumb-item :to="{ path: '/developer/workspace', query: {active: 'scenario'}}" class="scenario-breadcrumb-item">我的场景</el-breadcrumb-item>
    <el-breadcrumb-item class="scenario-breadcrumb-item">
      {{scenarioName || '--场景'}}
    </el-breadcrumb-item>
  </el-breadcrumb>
  <el-row>
    <el-col :span="18">
      <div style="padding: 20px">
        <div class="scenario-map">
          <div class="scenario-title" id="场景区域">场景区域</div>
          <div>
            <DetailArea :scenarioId="scenarioId" :scenarioName="scenarioName"/>
          </div>
        </div>
        <div class="scenario-resource">
          <div class="scenario-title" id="场景资源">场景资源</div>
          <div>
            <DetailDevice :scenarioId="scenarioId" :scenarioName="scenarioName"/>
          </div>
        </div>
      </div>
    </el-col>
    <el-col :span="6">
      <el-anchor
          :container="anchorRef"
          direction="vertical"
          type="default"
          :offset="30"
          class="anchor"
      >
        <el-anchor-link href="#场景区域" title="场景区域" >
          <template #sub-link>
            <el-anchor-link href="#场景地图" title="场景地图" />
            <el-anchor-link href="#区域列表" title="区域列表" />
          </template>
        </el-anchor-link>
        <el-anchor-link href="#场景资源" title="场景资源" >
          <template #sub-link>
            <el-anchor-link href="#设备" title="设备" />
          </template>
        </el-anchor-link>
      </el-anchor>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import DetailArea from "@/view/main/developer/designer/scenario/component/DetailArea.vue";
import DetailDevice from "@/view/main/developer/designer/scenario/component/DetailDevice.vue";

const anchorRef = ref<HTMLElement | null>(null)

interface State {
  scenarioId: string;
  scenarioName: string;
}

const state = reactive<State>({
  scenarioId: '',
  scenarioName: ''
})
const {scenarioId, scenarioName} = toRefs(state)

const router = useRouter()
watchEffect(() => {
  if (typeof router.currentRoute.value.query.scenarioId === 'string') {
    scenarioId.value = router.currentRoute.value.query.scenarioId || ''
  }
  if (typeof router.currentRoute.value.query.scenarioName === 'string') {
    scenarioName.value = router.currentRoute.value.query.scenarioName || ''
  }
})
</script>

<style scoped>
.scenario-breadcrumb {
  font-size: 18px;
  margin-top: 10px;
  margin-bottom: 20px;
}
.scenario-map {
  margin-bottom: 20px;
}
.scenario-title {
  font-size: 18px;
  font-weight: bold;
  line-height: 32px;
  color: #606266;
  margin-bottom: 10px;
}
.anchor {
  border-left: 2px solid #d6d6d6;
  position: fixed;
}
</style>
<style>
.scenario-subtitle {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
}
.scenario-content {
  margin-bottom: 10px;
}
</style>
