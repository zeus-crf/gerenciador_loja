<script setup lang="ts">
import Input from '@/components/ui/input/Input.vue'
import InputPassword from '@/components/ui/input/InputPassword.vue'
import Button from '../ui/button/Button.vue'
import { toast } from 'vue-sonner'
import { ref } from 'vue'
import { login } from '@/services/authService' 
import { router } from '@/router'


const username = ref('')
const password = ref('')
const remember = ref(false)
const loading = ref(false)
const errorMessage = ref('')


async function handleLogin() {
    loading.value = true
    errorMessage.value = '' 
    
    try {
        const response = await login({
            username: username.value,
            password: password.value
        })

        console.log('LOGIN OK:', response)

        // caso queira salvar token
        localStorage.setItem('token', response.token)

        // redirecionar
       toast.success('Login realizado com sucesso')
       setTimeout(() => {
        router.push('/dashboard')
       }, 1000) 

    } catch (error: any) {
       toast.error( 
       error?.response?.data?.message || 'Usuário ou senha Inválidos'
    )
    } finally {
        loading.value = false
    }
}
</script>

<template>
    <form @submit.prevent="handleLogin" class="flex flex-col gap-5">

        <Input
            v-model="username"
            type="text"
            placeholder="ex: Usuari0"
            :required="true"
            label="Usuario" 
        />

        <InputPassword
            v-model="password" 
            placeholder="Sua senha"
            :required="true"
            label="Senha"
        />

        <div class="flex justify-between items-center">
            <label class="flex items-center gap-2 cursor-pointer">
                <input
                    type="checkbox"
                    v-model="remember"
                    class="form-checkbox h-4 w-4 text-primary"
                />
                <span class="text-sm font-medium">Lembrar-me</span>
            </label>
            <a href="#" class="text-sm font-bold text-primary hover:text-primary-dark">
                Esqueceu a senha?
            </a>
        </div>

        <p v-if="errorMessage" class="text-red-600 text-sm font-medium">
            {{ errorMessage }}
        </p>

        <Button
        :disabled="loading"
        />

    </form>
</template>