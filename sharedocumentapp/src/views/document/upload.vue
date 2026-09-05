<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { authApis, endpoints } from '@/configs/apis' // Đảm bảo đường dẫn này đúng với dự án của bạn
import Header from '@/components/header.vue'
import Footer from '@/components/footer.vue'

const router = useRouter()

const title = ref('')
const categoryId = ref('')
const description = ref('')
const tags = ref([])
const tagInput = ref('')
const categories = ref([])
const tagsList = ref([])
const thumbnailInputRef = ref(null)
const thumbnailFile = ref(null)
const thumbnailPreview = ref('')
const selectedFile = ref(null)

const isUploading = ref(false)
const errorMessage = ref('')

const fetchCategories = async () => {
  try {
    // Sửa lại endpoint nếu file apis.js của bạn cấu hình khác
    const res = await authApis.get(endpoints['categories'])
    categories.value = res.data
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Không thể kết nối tới server'
    console.error('Lỗi khi tải danh mục:', errorMessage.value)
  }
}
const fetchTags = async () => {
  try {
    const res = await authApis.get(endpoints['tags'])
    tagsList.value = res.data
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Không thể kết nối tới server'
    console.error('Lỗi khi tải danh sách tag:', errorMessage.value)
  }
}

onMounted(() => {
  fetchCategories()
  fetchTags()
})

// --- 3. XỬ LÝ KÉO THẢ & CHỌN FILE ---
const fileInputRef = ref(null)

const triggerFileInput = () => {
  fileInputRef.value.click()
}

const handleFileSelect = (event) => {
  const file = event.target.files[0]
  if (file) {
    selectedFile.value = file
  }
}

const handleDrop = (event) => {
  const file = event.dataTransfer.files[0]
  if (file) {
    selectedFile.value = file
  }
}

const removeFile = () => {
  selectedFile.value = null
  if (fileInputRef.value) fileInputRef.value.value = ''
}

// Format dung lượng file
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i]
}
// --- XỬ LÝ ẢNH BÌA (THUMBNAIL) ---

const triggerThumbnailInput = () => {
  thumbnailInputRef.value.click()
}

const handleThumbnailSelect = (event) => {
  const file = event.target.files[0]
  if (file && file.type.startsWith('image/')) {
    thumbnailFile.value = file
    thumbnailPreview.value = URL.createObjectURL(file) // Tạo URL tạm để hiển thị trước
  }
}

const removeThumbnail = () => {
  thumbnailFile.value = null
  thumbnailPreview.value = ''
  if (thumbnailInputRef.value) thumbnailInputRef.value.value = ''
}

// --- 4. XỬ LÝ THẺ (TAGS) ---
const addTag = () => {
  const newTagName = tagInput.value.trim()
  if (!newTagName) return

  // Kiểm tra xem tên thẻ đã tồn tại trong danh sách đang chọn chưa
  const isExist = tags.value.some((t) => t.name?.toLowerCase() === newTagName.toLowerCase())

  if (!isExist && tags.value.length < 8) {
    // Thẻ tự nhập không có ID (id: null)
    tags.value.push({ id: null, name: newTagName })
  }
  tagInput.value = '' // Reset ô nhập
}

const removeTag = (index) => {
  tags.value.splice(index, 1)
}

const addSuggestedTag = (selectedTag) => {
  const isExist = tags.value.some((t) => t.id === selectedTag.id)

  if (!isExist && tags.value.length < 8) {
    // Thẻ từ API sẽ có ID
    tags.value.push({ id: selectedTag.id, name: selectedTag.name })
  }
}

// --- 5. GỬI DỮ LIỆU LÊN BACKEND (SUBMIT) ---
const handleUpload = async () => {
  // Validate cơ bản ở Frontend
  if (!title.value || !categoryId.value || !selectedFile.value) {
    errorMessage.value = 'Vui lòng nhập đầy đủ Tiêu đề, Danh mục và chọn File đính kèm.'
    return
  }

  isUploading.value = true
  errorMessage.value = ''

  // Sử dụng FormData để gửi multipart/form-data
  const formData = new FormData()
  formData.append('title', title.value)
  formData.append('categoryId', categoryId.value)
  formData.append('description', description.value)
  formData.append('fileUrl', selectedFile.value)

  if (thumbnailFile.value) {
    formData.append('thumbnail', thumbnailFile.value)
  }

  // Gửi mảng tags lên backend (tên 'newTagNames' phải khớp với DTO)
  tags.value.forEach((tag) => {
    if (tag.id) {
      // Nếu có ID -> Gửi vào existingTagIds
      formData.append('existingTagIds', tag.id)
    } else {
      // Nếu không có ID -> Gửi vào newTagNames
      formData.append('newTagNames', tag.name)
    }
  })

  try {
    const res = await authApis.post(endpoints['upload'], formData)

    console.log('Upload thành công:', res.data)
    alert('Tải lên thành công!')
    router.push(`/documents/${res.data.id}`)
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Có lỗi xảy ra khi upload!'
    console.error('Lỗi upload:', errorMessage.value)
  } finally {
    isUploading.value = false
  }
}
</script>
<template>
  <Header />
  <main class="upload-page">
    <div class="upload-container">
      <!-- Tiêu đề trang -->
      <div class="page-header">
        <h1 class="page-title">Tải lên tài liệu mới</h1>
        <p class="page-subtitle">
          Đóng góp kiến thức và chia sẻ tài liệu học tập hữu ích với cộng đồng DocShare
        </p>
      </div>

      <!-- Khung hiển thị thông báo lỗi chung -->
      <div
        v-if="errorMessage"
        style="
          background-color: #fee2e2;
          color: #ef4444;
          padding: 12px 16px;
          border-radius: 8px;
          margin-bottom: 24px;
          font-weight: 500;
        "
      >
        {{ errorMessage }}
      </div>

      <!-- Khu vực nội dung chính (2 cột) -->
      <div class="upload-content">
        <!-- CỘT TRÁI: Khu vực tải file -->
        <div class="left-col">
          <div class="panel">
            <div class="panel-header flex-between">
              <div class="panel-title">
                <span class="material-symbols-outlined icon-sm text-blue">description</span>
                Tệp tài liệu đính kèm
              </div>
              <span class="required-text">* Bắt buộc</span>
            </div>

            <!-- Vùng Kéo thả file -->
            <div class="drag-drop-zone" @dragover.prevent @drop.prevent="handleDrop">
              <div class="icon-box">
                <span class="material-symbols-outlined icon-large text-blue">cloud_upload</span>
              </div>
              <h3 class="drop-title">Kéo & thả tệp tin vào đây</h3>
              <p class="drop-subtitle">hoặc nhấp chuột để duyệt từ thiết bị của bạn</p>

              <button class="btn-primary btn-sm mt-3" @click="triggerFileInput">
                <span class="material-symbols-outlined icon-sm">folder_open</span>
                Chọn file từ máy
              </button>

              <!-- Input file ẩn -->
              <input
                type="file"
                hidden
                ref="fileInputRef"
                @change="handleFileSelect"
                accept=".pdf,.doc,.docx,.ppt,.pptx,.xls,.xlsx"
              />
            </div>

            <!-- Tệp đang chọn (File Preview) -->
            <div class="selected-file-section" v-if="selectedFile">
              <div class="section-label">TỆP ĐANG CHỌN</div>
              <div class="file-card">
                <div class="file-info-row" style="margin-bottom: 0">
                  <div class="file-icon pdf-icon">FILE</div>
                  <div class="file-details">
                    <div class="file-name">{{ selectedFile.name }}</div>
                    <div class="file-meta">{{ formatFileSize(selectedFile.size) }}</div>
                  </div>
                  <button class="btn-icon" @click="removeFile" :disabled="isUploading">
                    <span class="material-symbols-outlined">delete</span>
                  </button>
                </div>
              </div>
            </div>
          </div>
          <!-- KHUNG UPLOAD ẢNH BÌA (TÙY CHỌN) -->
          <div class="panel mt-4">
            <div class="panel-header flex-between">
              <div class="panel-title">
                <span class="material-symbols-outlined icon-sm text-blue">image</span>
                Ảnh bìa tài liệu
              </div>
              <span class="helper-text">Tùy chọn</span>
            </div>

            <!-- Vùng chọn ảnh (khi chưa có ảnh) -->
            <div
              class="thumbnail-upload-zone"
              v-if="!thumbnailPreview"
              @click="triggerThumbnailInput"
            >
              <span class="material-symbols-outlined icon-large text-muted"
                >add_photo_alternate</span
              >
              <p class="drop-subtitle mt-2" style="color: #1e293b; font-weight: 500">
                Nhấn để chọn ảnh bìa
              </p>
              <p class="helper-text">Nếu để trống, hệ thống sẽ tự động tạo ảnh bìa</p>
            </div>

            <!-- Vùng hiển thị ảnh (khi đã chọn ảnh) -->
            <div class="thumbnail-preview-zone" v-else>
              <img :src="thumbnailPreview" alt="Thumbnail Preview" class="thumbnail-image" />
              <button class="btn-icon btn-remove-thumb" @click.stop="removeThumbnail">
                <span class="material-symbols-outlined">close</span>
              </button>
            </div>

            <!-- Input file ẩn cho ảnh bìa -->
            <input
              type="file"
              hidden
              ref="thumbnailInputRef"
              @change="handleThumbnailSelect"
              accept="image/jpeg, image/png, image/jpg"
            />
          </div>
        </div>

        <!-- CỘT PHẢI: Thông tin chi tiết -->
        <div class="right-col">
          <div class="panel">
            <h2 class="panel-title mb-4">Thông tin chi tiết tài liệu</h2>

            <!-- Tiêu đề -->
            <div class="form-group">
              <label class="form-label"
                >TIÊU ĐỀ TÀI LIỆU <span class="required-asterisk">*</span></label
              >
              <input
                type="text"
                class="form-control"
                v-model="title"
                placeholder="Ví dụ: Báo cáo Nghiên cứu Ứng dụng AI..."
              />
            </div>

            <!-- Danh mục -->
            <div class="form-group">
              <label class="form-label"
                >DANH MỤC TÀI LIỆU <span class="required-asterisk">*</span></label
              >
              <div class="select-wrapper">
                <select class="form-control" v-model="categoryId">
                  <option value="" disabled>Chọn danh mục...</option>
                  <option v-for="cat in categories" :key="cat.id" :value="cat.id">
                    {{ cat.name }}
                  </option>
                </select>
                <span class="material-symbols-outlined select-icon">expand_more</span>
              </div>
            </div>

            <!-- Thẻ phân loại (Tags) -->
            <div class="form-group">
              <div class="flex-between">
                <label class="form-label">THẺ PHÂN LOẠI (TAGS)</label>
              </div>

              <!-- Danh sách thẻ đã chọn -->
              <div class="tags-container" v-if="tags.length > 0">
                <div class="tag-item" v-for="(tag, index) in tags" :key="index">
                  {{ tag.name }}
                  <span class="material-symbols-outlined icon-close" @click="removeTag(index)"
                    >close</span
                  >
                </div>
              </div>
              <div
                class="tags-container"
                v-else
                style="align-items: center; color: #94a3b8; font-size: 13px"
              >
                Chưa có thẻ nào được thêm.
              </div>

              <!-- Input nhập thẻ mới -->
              <div class="tag-input-group">
                <input
                  type="text"
                  class="form-control"
                  v-model="tagInput"
                  @keyup.enter="addTag"
                  placeholder="Nhập tên thẻ mới"
                />
                <button class="btn-outline btn-add-tag" @click="addTag">
                  <span class="material-symbols-outlined icon-sm">add</span> Thêm thẻ
                </button>
              </div>
              <div class="tag-suggestions" v-if="tagsList.length > 0">
                <div class="suggestion-label">Chọn từ danh sách:</div>
                <div class="suggestion-list">
                  <button
                    v-for="tag in tagsList"
                    :key="tag.id"
                    class="btn-suggestion"
                    @click="addSuggestedTag(tag)"
                  >
                    + {{ tag.name }}
                  </button>
                </div>
              </div>
            </div>

            <!-- Mô tả -->
            <div class="form-group mb-0">
              <div class="flex-between">
                <label class="form-label">MÔ TẢ TÓM TẮT NỘI DUNG</label>
                <span class="helper-text">{{ description.length }} / 1000 ký tự</span>
              </div>
              <textarea
                class="form-control textarea-desc"
                v-model="description"
                maxlength="1000"
                placeholder="Nhập mô tả tóm tắt nội dung tài liệu..."
              ></textarea>
            </div>

            <!-- Divider -->
            <hr class="panel-divider" />

            <!-- Nút hành động -->
            <div class="action-buttons">
              <button class="btn-cancel" @click="router.push('/')">Hủy bỏ</button>
              <button class="btn-primary" @click="handleUpload" :disabled="isUploading">
                <span class="material-symbols-outlined icon-sm">cloud_upload</span>
                {{ isUploading ? 'Đang tải lên...' : 'Tải lên tài liệu' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
  <Footer />
</template>

<style scoped>
@import '@/styles/uploadDocument.css';
</style>
