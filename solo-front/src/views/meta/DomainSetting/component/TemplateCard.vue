<template>
    <div @click="handleClick">
        {{ props.template.name }}
    </div>
</template>

<script setup lang="ts">
import { Template } from '@/types/models';
const props = defineProps({
    template: {
        type: Object as ()=>Template
    }
})
const emit = defineEmits(['template-click']);
const state = reactive({
    selected: false
})
const { selected } = toRefs(state)
const handleClick = () => {
    selected.value = !selected.value
    emit('template-click', props.template.uuid ,selected.value);
};
</script>