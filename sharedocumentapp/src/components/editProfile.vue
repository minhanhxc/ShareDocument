<script setup>
import { ref, watch } from 'vue'
import { authApis, endpoints } from '@/configs/apis'

const props = defineProps({
  isOpen: Boolean,
  userProfile: Object,
})

const emit = defineEmits(['close', 'updated'])

// Dữ liệu form
const formData = ref({
  ho: '',
  ten: '',
})
const avatarFile = ref(null)
const avatarPreview = ref(null)
const isSubmitting = ref(false)

watch(
  () => props.isOpen,
  (newVal) => {
    if (newVal && props.userProfile) {
      formData.value.ho = props.userProfile.ho || ''
      formData.value.ten = props.userProfile.ten || ''
      avatarPreview.value = props.userProfile.avatar || null
      avatarFile.value = null
    }
  },
)

// Xử lý khi người dùng chọn ảnh
const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    avatarFile.value = file
    avatarPreview.value = URL.createObjectURL(file)
  }
}

// Xử lý Submit
const handleSubmit = async () => {
  isSubmitting.value = true
  try {
    const data = new FormData()
    data.append('ho', formData.value.ho)
    data.append('ten', formData.value.ten)

    if (avatarFile.value) {
      data.append('avatar', avatarFile.value)
    }

    const res = await authApis.put(endpoints['profile'], data, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })

    // Báo cho component cha (profile.vue) biết là đã update thành công
    emit('updated', res.data)
  } catch (error) {
    console.error('Lỗi khi cập nhật:', error)
    alert('Cập nhật thất bại. Vui lòng thử lại!')
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <div v-if="isOpen" class="modal-overlay" @click.self="emit('close')">
    <div class="modal-content">
      <div class="modal-header">
        <h3>Chỉnh sửa thông tin</h3>
        <button class="btn-close" @click="emit('close')">
          <svg
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
          >
            <line x1="18" y1="6" x2="6" y2="18"></line>
            <line x1="6" y1="6" x2="18" y2="18"></line>
          </svg>
        </button>
      </div>

      <form @submit.prevent="handleSubmit" class="modal-body">
        <!-- Khu vực đổi Avatar -->
        <div class="avatar-edit-section">
          <div class="avatar-preview-wrapper">
            <img
              v-if="avatarPreview"
              :src="avatarPreview"
              alt="Preview"
              class="avatar-preview-img"
            />
            <span v-else class="material-symbols-outlined avatar-preview-icon">person</span>
          </div>
          <div class="file-upload-wrapper">
            <label for="avatar-upload" class="btn-outline btn-small">Đổi ảnh đại diện</label>
            <input
              id="avatar-upload"
              type="file"
              accept="image/*"
              @change="handleFileChange"
              class="d-none"
            />
          </div>
        </div>

        <!-- Cập nhật Họ và Tên -->
        <div class="form-group">
          <label>Họ</label>
          <input
            v-model="formData.ho"
            type="text"
            class="form-control"
            placeholder="Nhập họ..."
            required
          />
        </div>
        <div class="form-group">
          <label>Tên</label>
          <input
            v-model="formData.ten"
            type="text"
            class="form-control"
            placeholder="Nhập tên..."
            required
          />
        </div>

        <div class="modal-footer">
          <button type="button" class="btn-text" @click="emit('close')" :disabled="isSubmitting">
            Hủy
          </button>
          <button type="submit" class="btn-primary" :disabled="isSubmitting">
            {{ isSubmitting ? 'Đang lưu...' : 'Lưu thay đổi' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
/* CSS cho lớp mờ phía sau Modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(15, 23, 42, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

/* CSS cho hộp Modal */
.modal-content {
  background: white;
  width: 100%;
  max-width: 450px;
  border-radius: 12px;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #e2e8f0;
}
.modal-header h3 {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 600;
}
.btn-close {
  background: none;
  border: none;
  color: #64748b;
  cursor: pointer;
  padding: 4px;
  display: flex;
}

.modal-body {
  padding: 24px;
}

/* Form inputs */
.form-group {
  margin-bottom: 16px;
}
.form-group label {
  display: block;
  font-size: 0.875rem;
  font-weight: 500;
  color: #475569;
  margin-bottom: 6px;
}
.form-control {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  font-size: 1rem;
  outline: none;
  transition: border-color 0.2s;
}
.form-control:focus {
  border-color: #0d6efd;
}
.d-none {
  display: none;
}

/* Avatar Preview */
.avatar-edit-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}
.avatar-preview-wrapper {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  overflow: hidden;
  background: #f1f5f9;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid #e2e8f0;
}
.avatar-preview-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}
.btn-primary {
  background: #0d6efd;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
}
.btn-text {
  background: none;
  border: none;
  color: #64748b;
  font-weight: 500;
  cursor: pointer;
}
.btn-outline {
  border: 1px solid #cbd5e1;
  background: white;
  padding: 6px 12px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.875rem;
}
</style>
