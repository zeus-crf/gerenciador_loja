import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import LoginView from '../views/LoginView.vue'

const routes: Array<RouteRecordRaw> = [
  { path: '/', component: LoginView }
]

export const router = createRouter({
  history: createWebHistory(),
  routes
})
