import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import cookie from 'vue-cookies'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(cookie.get('user') || null)

  const isAuthenticated = computed(() => !!user.value)

  const setUser = (userData) => {
    user.value = userData
    cookie.set('user', userData, '1d')
  }

  const logout = () => {
    user.value = null
    cookie.remove('token')
    cookie.remove('user')
  }

  return { user, isAuthenticated, setUser, logout }
})
