<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import Apis, { endpoints } from '@/configs/apis'
import cookie from 'vue-cookies'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const router = useRouter()
const username = ref('')
const password = ref('')
const isLoading = ref(false)

const handleLogin = async () => {
  if (!username.value || !password.value) {
    alert('Vui lòng nhập đầy dủ thông tin')
    return
  }
  isLoading.value = true
  try {
    const res = await Apis.post(endpoints['login'], {
      username: username.value,
      password: password.value,
    })
    cookie.set('token', res.data.token, '1d')

    const userData = {
      username: res.data.username,
      email: res.data.email,
      role: res.data.role,
    }
    authStore.setUser(userData)
    router.push('/')
  } catch (error) {
    console.error('Login failed:', error)
  } finally {
    isLoading.value = false
  }
}
</script>
<template>
  <div class="auth-wrapper">
    <div class="auth-card">
      <div class="header-section">
        <h1 class="logo-title">DocShare</h1>
      </div>

      <form @submit.prevent="handleLogin" class="auth-form">
        <div class="form-group">
          <label for="username" class="form-label">Username</label>
          <input
            id="username"
            v-model="username"
            type="text"
            class="form-input"
            placeholder="Enter your username"
            required
          />
        </div>

        <div class="form-group">
          <div class="label-row">
            <label for="password" class="form-label">Password</label>
          </div>
          <input
            id="password"
            v-model="password"
            type="password"
            class="form-input"
            placeholder="••••••••"
            required
          />
          <div class="label-row">
            <RouterLink to="/forgot-password" class="forgot-link">Forgot Password?</RouterLink>
          </div>
        </div>

        <button type="submit" class="btn-primary" :disabled="isLoading">
          <span>{{ isLoading ? 'Signing in...' : 'Login' }}</span>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="18"
            height="18"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <line x1="5" y1="12" x2="19" y2="12"></line>
            <polyline points="12 5 19 12 12 19"></polyline>
          </svg>
        </button>
      </form>

      <p class="footer-text">
        Don't have an account?
        <RouterLink to="/register" class="signup-link">Sign Up</RouterLink>
      </p>
    </div>
  </div>
</template>
<style scoped>
@import '@/styles/auth.css';
</style>
