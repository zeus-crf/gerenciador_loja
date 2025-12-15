<script setup lang="ts">
import { ref } from 'vue';
import { login } from '@/services/authService' 


const props = defineProps<{
    disabled?: boolean;
}>();


</script>
<template>
    <button
            type="submit"
            :disabled="disabled"
            class="flex w-full items-center justify-center rounded-lg h-12 bg-primary 
                   hover:bg-primary-hover active:scale-[0.99] transition-all font-bold"
        >
            <span v-if="!disabled">Entrar</span>
            <span v-else class="animate-pulse">Autenticando...</span>
            <span class="material-symbols-outlined ml-2"></span>
        </button>

</template>
