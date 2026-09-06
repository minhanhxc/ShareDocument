<script setup>
import { ref } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const router = useRouter()

const searchQuery = ref('')

const handleSearch = () => {
  if (!searchQuery.value.trim()) return
  router.push({ path: '/documents', query: { q: searchQuery.value } })
}

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<template>
  <header class="top-navbar">
    <div class="nav-container">
      <!-- Brand Logo -->
      <RouterLink to="/" class="brand-title">DocShare</RouterLink>

      <!-- Desktop Search Bar -->
      <div class="search-box-wrapper">
        <form @submit.prevent="handleSearch" class="search-form">
          <svg
            class="search-icon"
            xmlns="http://www.w3.org/2000/svg"
            width="18"
            height="18"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <circle cx="11" cy="11" r="8"></circle>
            <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
          </svg>
          <input
            v-model="searchQuery"
            type="text"
            class="search-input"
            placeholder="Search for notes, papers, or textbooks..."
          />
        </form>
      </div>

      <!-- Actions Group -->
      <div class="actions-group">
        <!-- Đã đăng nhập -->
        <template v-if="authStore.isAuthenticated">
          <RouterLink to="/profile" class="user-greeting profile-link" title="Trang cá nhân">
            Hi, {{ authStore.user?.username }}
          </RouterLink>
          <RouterLink to="/documents/upload" class="btn-primary-pill">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2.2"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
              <polyline points="17 8 12 3 7 8"></polyline>
              <line x1="12" y1="3" x2="12" y2="15"></line>
            </svg>
            Upload
          </RouterLink>
          <button @click="handleLogout" class="btn-text">Logout</button>
        </template>

        <!-- Chưa đăng nhập -->
        <template v-else>
          <RouterLink to="/login" class="btn-text">Login</RouterLink>
          <RouterLink to="/register" class="btn-primary-pill">Sign Up</RouterLink>
        </template>
      </div>
    </div>
  </header>
</template>

<style scoped>
.top-navbar {
  position: sticky;
  top: 0;
  z-index: 50;
  background-color: rgba(250, 248, 255, 0.85);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid #c3c6d7;
}

.nav-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 24px;
  height: 72px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

.brand-title {
  font-size: 24px;
  font-weight: 700;
  color: #004ac6;
  text-decoration: none;
  flex-shrink: 0;
}

.search-box-wrapper {
  display: none;
  flex: 1;
  max-width: 420px;
}

@media (min-width: 768px) {
  .search-box-wrapper {
    display: block;
  }
}

.search-form {
  position: relative;
  width: 100%;
}

.search-icon {
  position: absolute;
  left: 14px;
  top: 50%;
  transform: translateY(-50%);
  color: #737686;
}

.search-input {
  width: 100%;
  padding: 10px 16px 10px 42px;
  background-color: #f2f3ff;
  border: 1px solid #c3c6d7;
  border-radius: 9999px;
  font-size: 14px;
  color: #131b2e;
  outline: none;
  transition: all 0.2s ease;
  box-sizing: border-box;
}

.search-input:focus {
  border-color: #004ac6;
  box-shadow: 0 0 0 2px #b4c5ff;
}

.nav-link {
  font-size: 14px;
  font-weight: 500;
  color: #434655;
  text-decoration: none;
  transition: color 0.2s ease;
}

.nav-link:hover {
  color: #004ac6;
}

.actions-group {
  display: flex;
  align-items: center;
  gap: 14px;
}

.user-greeting {
  font-size: 13.5px;
  font-weight: 600;
  color: #131b2e;
}

.btn-text {
  font-size: 14px;
  font-weight: 600;
  color: #004ac6;
  background: none;
  border: none;
  cursor: pointer;
  text-decoration: none;
  padding: 8px 12px;
}

.profile-link {
  text-decoration: none;
  color: inherit; /* Giữ nguyên màu chữ gốc */
  cursor: pointer;
  transition: color 0.2s ease;
}

.profile-link:hover {
  color: #0d6efd; /* Đổi màu khi di chuột vào */
}
.btn-primary-pill {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 18px;
  background-color: #004ac6;
  color: #ffffff;
  border-radius: 9999px;
  font-size: 14px;
  font-weight: 500;
  text-decoration: none;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.btn-primary-pill:hover {
  background-color: #003ea8;
}
</style>
