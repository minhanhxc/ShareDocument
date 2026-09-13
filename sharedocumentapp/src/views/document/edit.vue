<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { authApis, endpoints } from '@/configs/apis'
import Header from '@/components/header.vue'
import Footer from '@/components/footer.vue'

const route = useRoute()
const router = useRouter()
const docId = route.params.id

const title = ref('')
const categoryId = ref('')
const description = ref('')
const tags = ref([])
const tagInput = ref('')
const categories = ref([])
const tagsList = ref([])

const fileInputRef = ref(null)
const selectedFile = ref(null)
const currentFileName = ref('')

const thumbnailInputRef = ref(null)
const thumbnailFile = ref(null)
const thumbnailPreview = ref('')

const isLoadingData = ref(true)
const isSubmitting = ref(false)
const errorMessage = ref('')

const loadInitialData = async () => {
  isLoadingData.value = true
  try {
    const [catRes, tagRes, docRes] = await Promise.all([
      authApis.get(endpoints.categories),
      authApis.get(endpoints.tags),
      authApis.get(endpoints.documentDetail(docId)),
    ])

    categories.value = catRes.data
    tagsList.value = tagRes.data

    const docData = docRes.data

    // Điền dữ liệu vào Form
    title.value = docData.title
    categoryId.value = docData.categoryId
    description.value = docData.description || ''
    currentFileName.value = docData.fileUrl ? 'Tài liệu đã được đính kèm trên hệ thống' : ''
    thumbnailPreview.value = docData.thumbnail || ''

    // Map tagNames trả về từ API chi tiết với danh sách tagsList để lấy ID
    if (docData.tagNames && docData.tagNames.length > 0) {
      tags.value = docData.tagNames.map((tagName) => {
        const foundTag = tagsList.value.find((t) => t.name === tagName)
        return foundTag ? { id: foundTag.id, name: tagName } : { id: null, name: tagName }
      })
    }
  } catch (error) {
    errorMessage.value =
      'Không thể tải dữ liệu tài liệu. Có thể tài liệu không tồn tại hoặc bạn không có quyền.'
    console.error('Lỗi khi tải dữ liệu:', error)
  } finally {
    isLoadingData.value = false
  }
}

const triggerFileInput = () => fileInputRef.value.click()

const handleFileSelect = (event) => {
  const file = event.target.files[0]
  if (file) selectedFile.value = file
}

const handleDrop = (event) => {
  const file = event.dataTransfer.files[0]
  if (file) selectedFile.value = file
}

const removeFile = () => {
  selectedFile.value = null
  if (fileInputRef.value) fileInputRef.value.value = ''
}

const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i]
}

// ==========================================
// 5. HÀM XỬ LÝ SỰ KIỆN: ẢNH BÌA (THUMBNAIL)
// ==========================================
const triggerThumbnailInput = () => thumbnailInputRef.value.click()

const handleThumbnailSelect = (event) => {
  const file = event.target.files[0]
  if (file && file.type.startsWith('image/')) {
    thumbnailFile.value = file
    thumbnailPreview.value = URL.createObjectURL(file)
  }
}

const removeThumbnail = () => {
  thumbnailFile.value = null
  thumbnailPreview.value = ''
  if (thumbnailInputRef.value) thumbnailInputRef.value.value = ''
}

const addTag = () => {
  const newTagName = tagInput.value.trim()
  if (!newTagName) return
  const isExist = tags.value.some((t) => t.name?.toLowerCase() === newTagName.toLowerCase())
  if (!isExist && tags.value.length < 8) {
    tags.value.push({ id: null, name: newTagName })
  }
  tagInput.value = ''
}

const removeTag = (index) => tags.value.splice(index, 1)

const addSuggestedTag = (selectedTag) => {
  const isExist = tags.value.some((t) => t.id === selectedTag.id)
  if (!isExist && tags.value.length < 8) {
    tags.value.push({ id: selectedTag.id, name: selectedTag.name })
  }
}

const handleUpdate = async () => {
  if (!title.value || !categoryId.value) {
    errorMessage.value = 'Vui lòng nhập đầy đủ Tiêu đề và Danh mục.'
    return
  }

  isSubmitting.value = true
  errorMessage.value = ''

  const formData = new FormData()
  formData.append('title', title.value)
  formData.append('categoryId', categoryId.value)
  formData.append('description', description.value)

  if (selectedFile.value) {
    formData.append('file', selectedFile.value)
  }
  if (thumbnailFile.value) {
    formData.append('thumbnail', thumbnailFile.value)
  }

  tags.value.forEach((tag) => {
    if (tag.id) formData.append('existingTagIds', tag.id)
    else formData.append('newTagNames', tag.name)
  })

  try {
    await authApis.put(endpoints.editDocuemnt(docId), formData)
    alert('Cập nhật tài liệu thành công!')
    router.push(`/documents/${docId}`)
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Có lỗi xảy ra khi cập nhật!'
    console.error('Lỗi cập nhật:', errorMessage.value)
  } finally {
    isSubmitting.value = false
  }
}

onMounted(() => {
  loadInitialData()
})
</script>

<template>
  <Header />
  <main class="upload-page">
    <div v-if="isLoadingData" class="text-center py-5">Đang tải dữ liệu tài liệu...</div>

    <div v-else class="upload-container">
      <!-- Tiêu đề trang -->
      <div class="page-header">
        <h1 class="page-title">Chỉnh sửa tài liệu</h1>
        <p class="page-subtitle">
          Cập nhật thông tin hoặc thay thế file đính kèm cho tài liệu của bạn
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

      <div class="upload-content">
        <!-- CỘT TRÁI: Khu vực tải file -->
        <div class="left-col">
          <div class="panel">
            <div class="panel-header flex-between">
              <div class="panel-title">
                <span class="material-symbols-outlined icon-sm text-blue">description</span>
                Tệp tài liệu đính kèm
              </div>
              <span class="helper-text">Tùy chọn thay thế</span>
            </div>

            <!-- Vùng Kéo thả file -->
            <div class="drag-drop-zone" @dragover.prevent @drop.prevent="handleDrop">
              <div class="icon-box">
                <span class="material-symbols-outlined icon-large text-blue">cloud_upload</span>
              </div>
              <h3 class="drop-title">Tải lên file mới để thay thế</h3>
              <p class="drop-subtitle" v-if="currentFileName && !selectedFile">
                Tài liệu gốc vẫn đang được giữ an toàn. Kéo thả file mới vào đây nếu bạn muốn thay
                thế.
              </p>
              <p class="drop-subtitle" v-else>hoặc nhấp chuột để duyệt từ thiết bị của bạn</p>

              <button class="btn-primary btn-sm mt-3" @click="triggerFileInput">
                <span class="material-symbols-outlined icon-sm">folder_open</span>
                Chọn file từ máy
              </button>

              <input
                type="file"
                hidden
                ref="fileInputRef"
                @change="handleFileSelect"
                accept=".pdf,.doc,.docx,.ppt,.pptx,.xls,.xlsx"
              />
            </div>

            <!-- Tệp đang chọn (File Preview MỚI) -->
            <div class="selected-file-section" v-if="selectedFile">
              <div class="section-label">TỆP MỚI ĐANG CHỌN</div>
              <div class="file-card">
                <div class="file-info-row" style="margin-bottom: 0">
                  <div class="file-icon pdf-icon">FILE</div>
                  <div class="file-details">
                    <div class="file-name">{{ selectedFile.name }}</div>
                    <div class="file-meta">{{ formatFileSize(selectedFile.size) }}</div>
                  </div>
                  <button class="btn-icon" @click="removeFile" :disabled="isSubmitting">
                    <span class="material-symbols-outlined">delete</span>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- KHUNG UPLOAD ẢNH BÌA -->
          <div class="panel mt-4">
            <div class="panel-header flex-between">
              <div class="panel-title">
                <span class="material-symbols-outlined icon-sm text-blue">image</span>
                Ảnh bìa tài liệu
              </div>
              <span class="helper-text">Tùy chọn thay thế</span>
            </div>

            <div
              class="thumbnail-upload-zone"
              v-if="!thumbnailPreview"
              @click="triggerThumbnailInput"
            >
              <span class="material-symbols-outlined icon-large text-muted"
                >add_photo_alternate</span
              >
              <p class="drop-subtitle mt-2" style="color: #1e293b; font-weight: 500">
                Nhấn để chọn ảnh bìa mới
              </p>
            </div>

            <div class="thumbnail-preview-zone" v-else>
              <img :src="thumbnailPreview" alt="Thumbnail Preview" class="thumbnail-image" />
              <!-- Có thể cho phép đổi ảnh bìa bằng click vào ảnh -->
              <button
                class="btn-primary btn-sm mt-2"
                @click="triggerThumbnailInput"
                style="position: absolute; bottom: 10px; right: 10px"
              >
                Thay đổi
              </button>
            </div>

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

            <hr class="panel-divider" />

            <!-- Nút hành động -->
            <div class="action-buttons">
              <button class="btn-cancel" @click="router.push(`/documents/${docId}`)">Hủy bỏ</button>
              <button class="btn-primary" @click="handleUpdate" :disabled="isSubmitting">
                <span class="material-symbols-outlined icon-sm">save</span>
                {{ isSubmitting ? 'Đang cập nhật...' : 'Cập nhật tài liệu' }}
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
