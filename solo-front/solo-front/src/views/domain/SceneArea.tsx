import { defineComponent, computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useSceneStore } from '@/store/scene'
import { useAreaStore } from '@/store/area'
import { useDomainStore } from '@/store/domain'

type Id = number | undefined

export const useSceneAreaLogic = () => {
  const route = useRoute()
  const router = useRouter()
  const sceneStore = useSceneStore()
  const areaStore = useAreaStore()
  const domainStore = useDomainStore()

  const domainId = computed<Id>(() => {
    const q = route.query?.domainId as string | undefined
    if (!q) return domainStore.currentDomain?.id
    const n = Number(q)
    return Number.isFinite(n) ? n : domainStore.currentDomain?.id
  })

  const sceneId = computed<Id>(() => {
    const q = route.query?.sceneId as string | undefined
    const n = Number(q)
    return Number.isFinite(n) ? n : undefined
  })

  const loading = ref(false)

  const fetchScenes = async () => {
    if (!domainId.value) return
    loading.value = true
    try {
      await sceneStore.fetchScenes(domainId.value)
    } finally {
      loading.value = false
    }
  }

  const fetchAreasByScene = async (sid?: number) => {
    const id = sid ?? sceneId.value
    if (!id) return
    loading.value = true
    try {
      await areaStore.fetchAreas(id)
    } finally {
      loading.value = false
    }
  }

  const setCurrentDomain = async (id: number) => {
    domainStore.setCurrentDomain(id)
    await fetchScenes()
  }

  const selectScene = async (id: number) => {
    await fetchAreasByScene(id)
  }

  return {
    route,
    router,
    sceneStore,
    areaStore,
    domainStore,
    domainId,
    sceneId,
    loading,
    fetchScenes,
    fetchAreasByScene,
    setCurrentDomain,
    selectScene,
  }
}

export default defineComponent({
  name: 'SceneArea',
  setup() {
    const logic = useSceneAreaLogic()

    onMounted(async () => {
      await logic.fetchScenes()
      await logic.fetchAreasByScene()
    })

    return () => null
  },
})