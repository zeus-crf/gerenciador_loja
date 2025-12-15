<script setup lang="ts">
import { computed,ref } from 'vue';

const props = defineProps<{
    title: string;
    subtitle: string
}>();

</script>
<template>
     <div class="flex flex-col gap-2">
          <h1 class="text-3xl font-black tracking-tight text-text-main  sm:text-4xl">{{ props.title }}</h1>
          <p class="text-text-muted">{{ props.subtitle }}</p>
        </div>
</template>