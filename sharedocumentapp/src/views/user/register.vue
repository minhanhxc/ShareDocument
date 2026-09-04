<script setup>
import { ref } from 'vue'
import { useRouter, RouterLink } from 'vue-router'
import Apis, { endpoints } from '@/configs/apis'

const router = useRouter()

const ho = ref('')
const ten = ref('')
const email = ref('')
const username = ref('')
const password = ref('')
const confirmPassword = ref('')
const avatarFile = ref(null)
const avatarPreview = ref('')
const isLoading = ref(false)

const handleAvatarChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    avatarFile.value = file
    avatarPreview.value = URL.createObjectURL(file)
  }
}

const handleRegister = async () => {
  if (!ho.value || !ten.value || !username.value || !email.value || !password.value) {
    alert('Vui lòng điền đầy đủ các thông tin!')
    return
  }

  if (password.value !== confirmPassword.value) {
    alert('Mật khẩu xác nhận không khớp!')
    return
  }

  isLoading.value = true
  try {
    const formData = new FormData()
    formData.append('ho', ho.value)
    formData.append('ten', ten.value)
    formData.append('username', username.value)
    formData.append('email', email.value)
    formData.append('password', password.value)
    formData.append('avatar', avatarFile.value)
    if (avatarFile.value) {
      formData.append('avatar', avatarFile.value)
    }

    const res = await Apis.post(endpoints['register'], formData)

    alert('Đăng ký tài khoản thành công! Vui lòng đăng nhập.')
    router.push('/login')
  } catch (error) {
    console.error('Register failed:', error)
    alert('Đăng ký thất bại. Vui lòng kiểm tra lại thông tin!')
  } finally {
    isLoading.value = false
  }
}
</script>
<template>
  <div class="auth-wrapper">
    <main class="main-content">
      <div class="auth-card">
        <div class="gradient-bar"></div>

        <div class="header-section">
          <h1 class="auth-title">Create an account</h1>
        </div>

        <form @submit.prevent="handleRegister" class="auth-form">
          <!-- KHỐI AVATAR UPLOAD ĐÃ TINH CHỈNH -->
          <div class="avatar-center-wrapper">
            <div class="avatar-circle-container">
              <div class="avatar-circle">
                <!-- Hiển thị ảnh preview nếu đã chọn -->
                <img
                  v-if="avatarPreview"
                  :src="avatarPreview"
                  alt="Avatar preview"
                  class="avatar-circle-img"
                />
                <!-- Icon mặc định khi chưa chọn ảnh -->
                <svg
                  v-else
                  xmlns="http://www.w3.org/2000/svg"
                  width="44"
                  height="44"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="1.8"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  class="default-person-icon"
                >
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                  <circle cx="12" cy="7" r="4"></circle>
                </svg>
              </div>

              <!-- Nút tròn thêm ảnh ở góc dưới -->
              <label for="avatarInput" class="avatar-camera-btn" title="Chọn ảnh đại diện">
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
                  <path
                    d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"
                  ></path>
                  <circle cx="12" cy="13" r="4"></circle>
                </svg>
                <input
                  id="avatarInput"
                  type="file"
                  accept="image/*"
                  class="hidden-file-input"
                  @change="handleAvatarChange"
                />
              </label>
            </div>
            <span class="avatar-helper-text">Upload Profile Picture</span>
          </div>
          <!-- First Name & Last Name (2 Cột) -->
          <div class="form-row">
            <div class="form-group">
              <label for="ho" class="form-label">Họ (Last Name)</label>
              <input
                id="ho"
                v-model="ho"
                type="text"
                class="form-input"
                placeholder="Nguyễn"
                required
              />
            </div>
            <div class="form-group">
              <label for="ten" class="form-label">Tên (First Name)</label>
              <input
                id="ten"
                v-model="ten"
                type="text"
                class="form-input"
                placeholder="Văn A"
                required
              />
            </div>
          </div>

          <!-- Email -->
          <div class="form-group">
            <label for="email" class="form-label">Email</label>
            <input
              id="email"
              v-model="email"
              type="email"
              class="form-input"
              placeholder="name@gmail.com"
              required
            />
          </div>
          <div class="form-group">
            <label for="username" class="form-label">Tên đăng nhập (Username)</label>
            <input
              id="username"
              v-model="username"
              type="text"
              class="form-input"
              placeholder="nguyenvana"
              required
            />
          </div>
          <!-- Password -->
          <div class="form-group">
            <label for="password" class="form-label">Mật khẩu</label>
            <input
              id="password"
              v-model="password"
              type="password"
              class="form-input"
              placeholder="••••••••"
              required
            />
          </div>

          <!-- Confirm Password -->
          <div class="form-group">
            <label for="confirmPassword" class="form-label">Xác nhận mật khẩu</label>
            <input
              id="confirmPassword"
              v-model="confirmPassword"
              type="password"
              class="form-input"
              placeholder="••••••••"
              required
            />
          </div>

          <!-- Submit Button -->
          <button type="submit" class="btn-primary" :disabled="isLoading">
            <span>{{ isLoading ? 'Creating Account...' : 'Create Account' }}</span>
          </button>
        </form>

        <!-- Footer -->
        <p class="footer-text">
          Already have an account?
          <RouterLink to="/login" class="login-link">Log in</RouterLink>
        </p>
      </div>
    </main>
  </div>
</template>
<style scoped>
@import '@/styles/auth.css';
</style>
