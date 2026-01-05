<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { Eye, Pencil, Trash2 } from 'lucide-vue-next'

// ======================
// Tipos
// ======================
interface Usuario {
  id: string
  username?: string
  login?: string
}

// ======================
// Props / Emits
// ======================
const props = defineProps<{
  usuarios: Usuario[]
  loading: boolean
}>()

defineEmits(['view', 'edit', 'delete'])

// ======================
// Estado local
// ======================
const usuariosLocal = ref<Usuario[]>([])

// ======================
// 🔍 LOGS PARA DEBUG
// ======================
watch(
  () => props.usuarios,
  (val) => {
    console.log('[USUÁRIOS] Array recebido:', val)

    if (val.length > 0) {
      console.log('[USUÁRIOS] Primeiro usuário:', val[0])
      console.log('[USUÁRIOS] Chaves:', Object.keys(val[0]))
    }

    usuariosLocal.value = [...val]
  },
  { immediate: true }
)

// ======================
// Paginação
// ======================
const currentPage = ref(1)
const pageSize = ref(8)

const totalUsuarios = computed(() => usuariosLocal.value.length)
const totalPages = computed(() =>
  Math.ceil(totalUsuarios.value / pageSize.value)
)

const usuariosPaginados = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return usuariosLocal.value.slice(start, start + pageSize.value)
})

const startItem = computed(() =>
  totalUsuarios.value === 0
    ? 0
    : (currentPage.value - 1) * pageSize.value + 1
)

const endItem = computed(() =>
  Math.min(currentPage.value * pageSize.value, totalUsuarios.value)
)

const goToPage = (page: number) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
  }
}

const nextPage = () => goToPage(currentPage.value + 1)
const prevPage = () => goToPage(currentPage.value - 1)

// Paginação inteligente (igual pedidos)
const pagesToShow = computed<(number | string)[]>(() => {
  const total = totalPages.value
  const current = currentPage.value
  const pages: (number | string)[] = []

  if (total <= 5) {
    for (let i = 1; i <= total; i++) pages.push(i)
    return pages
  }

  pages.push(1)

  if (current > 3) pages.push('...')

  const start = Math.max(2, current - 1)
  const end = Math.min(total - 1, current + 1)

  for (let i = start; i <= end; i++) pages.push(i)

  if (current < total - 2) pages.push('...')

  pages.push(total)

  return pages
})

const handlePageClick = (page: number | string) => {
  if (typeof page === 'number') {
    goToPage(page)
  }
}

watch(totalUsuarios, () => {
  currentPage.value = 1
})
</script>

<template>
  <div class="mt-4">
    <div class="overflow-hidden rounded-xl border bg-white shadow">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y">
          <!-- HEADER -->
          <thead class="bg-slate-100">
            <tr>
              <th
                class="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase"
              >
                Username
              </th>
              <th
                class="px-6 py-3 text-right text-xs font-medium text-slate-500 uppercase"
              >
                Ações
              </th>
            </tr>
          </thead>

          <!-- BODY -->
          <tbody class="divide-y">
            <!-- LOADING -->
            <tr v-if="loading">
              <td
                colspan="2"
                class="px-6 py-4 text-center text-slate-500"
              >
                Carregando...
              </td>
            </tr>

            <!-- EMPTY -->
            <tr v-else-if="!loading && usuarios.length === 0">
              <td
                colspan="2"
                class="px-6 py-4 text-center text-slate-500"
              >
                Nenhum usuário encontrado
              </td>
            </tr>

            <!-- DATA -->
            <tr
              v-else
              v-for="usuario in usuarios"
              :key="usuario.id"
              class="hover:bg-slate-50"
            >
              <td class="px-6 py-4 font-medium text-slate-900">
                {{ usuario.username }}
              </td>

              <td class="px-6 py-4 text-right">
                <div class="flex justify-end gap-3">
                  <button
                    class="text-primary hover:text-slate-800"
                    @click="$emit('view', usuario)"
                  >
                    <Eye class="w-5 h-5" />
                  </button>

                  <button
                    class="text-primary hover:text-primary-dark"
                    @click="$emit('edit', usuario)"
                  >
                    <Pencil class="w-5 h-5" />
                  </button>

                  <button
                    class="text-red-600 hover:text-red-800"
                    @click="$emit('delete', usuario)"
                  >
                    <Trash2 class="w-5 h-5" />
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
