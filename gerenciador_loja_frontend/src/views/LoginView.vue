<template>
  <div class="min-h-screen flex w-full font-display bg-background-light  text-text-main">
    <!-- Left side (imagem) -->
    <div class="hidden lg:flex lg:w-1/2 relative bg-black overflow-hidden">
      <div
        class="absolute inset-0 bg-cover bg-center opacity-80"
        style="background-image: url('https://lh3.googleusercontent.com/aida-public/AB6AXuB-j4_Z2nDRu2RpBX632emQLl9JYSyRB0eMjq_QHV9HvkRNNI9ceBZp50tAS9kLczkN9dZ2JcchIPAmaav2U6OnF246uccwd4Gs5uKCxT21phmz-m9qRWFpRlp1ToJov3cvj-n7pB5Zf1Y8B8WBScrIRQnBcfmN3T1cL0qdE-RQ2zjXe-JUuS4NojKSFDXK58zo7UtFxCk6i1wjLa9T7j09mxvLEjrGmTV40-CmoMqfrsaZqjK5aqfq1hAn3QVYkP8xWBGRhi5-ocA');"
      ></div>
      <div class="absolute inset-0 bg-gradient-to-t from-background-dark/90 via-background-dark/40 to-transparent"></div>

      <div class="relative z-10 flex flex-col justify-end p-16 w-full h-full text-white">
        <div class="mb-8">
          <span
            class="inline-flex items-center justify-center p-3 rounded-xl bg-primary/20 backdrop-blur-sm mb-6 border border-primary/30">
            
          </span>

          <h2 class="text-4xl font-black tracking-tight leading-tight mb-4">
            Gerencie sua moda <br />com estilo.
          </h2>
          <p class="text-lg text-gray-200 max-w-md font-light">
            Acompanhe o estoque, analise as vendas e gerencie sua equipe em uma única plataforma.
          </p>
        </div>
      </div>
    </div>

    <!-- Right side (form) -->
    <div class="w-full lg:w-1/2 background-light flex flex-col justify-center items-center p-6 sm:p-12 xl:p-24">

      <div class="w-full max-w-[440px] flex flex-col gap-8">

        <!-- Header -->
        <div class="flex flex-col gap-2">
          <h1 class="text-3xl   sm:text-4xl">Bem-vindo de volta</h1>
          <p class="text-text-muted">Acesse o painel para começar o seu dia.</p>
        </div>

        <!-- LOGIN FORM -->
        <form @submit.prevent="handleLogin" class="flex flex-col gap-5">

          <!-- Email -->
          <label class="flex flex-col gap-2">
          <span class="text-sm font-semibold">Nome de usuário</span>
          <input
            v-model="email"
            type="text"
            placeholder="ex: Usuari0"
            class="form-input w-full rounded-lg border border-[#cfe7df] bg-background-light text-text-main focus:border-primary h-12 px-4"
            required
          />
      </label>

          <!-- Senha -->
          <label class="flex flex-col gap-2">
            <span class="text-sm font-semibold">Senha</span>
            <div class="relative">
              <input
                v-model="password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="Digite sua senha"
                class="form-input w-full rounded-lg border border-[#cfe7df] text-text-main focus:border-primary h-12 pl-4 pr-12"
                required
              />
              <button
                type="button"
                class="absolute right-0 top-0 h-full px-3 flex items-center"
                @click="showPassword = !showPassword"
              >
                <span class="material-symbols-outlined">
                  {{ showPassword ? 'visibility_off' : 'visibility' }}
                </span>
              </button>
            </div>
          </label>

          <!-- Lembrar -->
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

          <!-- Erro -->
          <p v-if="errorMessage" class="text-red-600 text-sm font-medium">
            {{ errorMessage }}
          </p>

          <!-- Botão -->
          <button
            type="submit"
            :disabled="loading"
            class="flex w-full items-center justify-center rounded-lg h-12 bg-primary 
                   hover:bg-primary-hover active:scale-[0.99] transition-all font-bold"
          >
            <span v-if="!loading">Entrar</span>
            <span v-else class="animate-pulse">Autenticando...</span>
            <span class="material-symbols-outlined ml-2">login</span>
          </button>

        </form>



       

        <p class="text-center text-sm mt-4">
          Não tem uma conta?
          <a href="#" class="font-bold hover:underline">Contate o administrador</a>
        </p>

      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { login } from '../services/authService' 

const email = ref('')
const password = ref('')
const remember = ref(false)
const showPassword = ref(false)
const loading = ref(false)
const errorMessage = ref('')

async function handleLogin() {
  loading.value = true
  errorMessage.value = ''

  try {
    const response = await login({
      username: email.value,
      password: password.value
    })

    console.log('LOGIN OK:', response)

    // caso queira salvar token
    localStorage.setItem('token', response.token)

    // redirecionar
    window.location.href = '/dashboard'

  } catch (error: any) {
    errorMessage.value = error?.message || 'Falha ao fazer login'
  } finally {
    loading.value = false
  }
}
</script>
