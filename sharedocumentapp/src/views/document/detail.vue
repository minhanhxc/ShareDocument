<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import Apis, { authApis, endpoints } from '@/configs/apis'
import Header from '@/components/header.vue'
import Footer from '@/components/footer.vue'
import { computed } from 'vue'
const route = useRoute()

const documentData = ref({})
const isLoading = ref(true)

// 2. Hàm gọi API lấy chi tiết tài liệu
const fetchDocumentDetail = async () => {
  isLoading.value = true

  try {
    const docId = route.params.id
    const res = await authApis().get(endpoints['documentDetail'](docId))
    documentData.value = res.data
  } catch (error) {
    console.error('Lỗi khi lấy chi tiết tài liệu:', error)
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  fetchDocumentDetail()
})

const isPdf = computed(() => {
  return documentData.value?.fileType?.toUpperCase() === 'PDF'
})

const isOfficeFile = computed(() => {
  const type = documentData.value?.fileType?.toUpperCase()
  return ['DOCX', 'DOC', 'PPTX', 'PPT', 'XLSX', 'XLS'].includes(type)
})

// Tạo URL nhúng cho file Office thông qua Google Docs Viewer
const googleDocsViewerUrl = computed(() => {
  if (!documentData.value?.fileUrl) return ''
  // Bắt buộc phải encode URL của file
  const encodedUrl = encodeURIComponent(documentData.value.fileUrl)
  return `https://docs.google.com/gview?url=${encodedUrl}&embedded=true`
})

// --- PHẦN BÌNH LUẬN (Tạm giữ logic cũ chờ API bình luận) ---
const newComment = ref('')
const comments = ref([])

const postComment = () => {
  // Logic gọi API POST /comments sẽ viết ở đây sau
  console.log('Post comment:', newComment.value)
}
</script>

<template>
  <main class="main-content">
    <!-- Breadcrumbs -->
    <nav class="breadcrumbs"></nav>

    <!-- Thêm v-if để tránh lỗi khi dữ liệu chưa gọi xong -->
    <div v-if="documentData" class="doc-grid">
      <!-- Left Column: Document Viewer (8 cols) -->
      <div class="doc-viewer-col">
        <div class="pdf-viewer-container">
          <div class="viewer-body">
            <!-- NẾU LÀ FILE PDF -->
            <iframe
              v-if="isPdf"
              :src="documentData.fileUrl"
              class="doc-iframe"
              title="PDF Viewer"
            ></iframe>

            <!-- NẾU LÀ FILE OFFICE -->
            <iframe
              v-else-if="isOfficeFile"
              :src="googleDocsViewerUrl"
              class="doc-iframe"
              title="Office Viewer"
            ></iframe>

            <!-- NẾU ĐỊNH DẠNG KHÔNG HỖ TRỢ -->
            <div v-else class="pdf-page-mock">
              <img class="pdf-image" :alt="documentData.title" :src="documentData.thumbnail" />
              <div class="pdf-overlay"></div>
              <div class="unsupported-msg">
                Định dạng này không hỗ trợ xem trước trực tiếp. Vui lòng tải xuống để xem.
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Right Column: Metadata & Actions (4 cols) -->
      <div class="doc-meta-col">
        <!-- Primary Actions & Title -->
        <div class="info-card">
          <div class="card-tags">
            <span class="tag-primary">{{ documentData.fileType }}</span>
            <span v-for="tag in documentData.tagNames" :key="tag" class="tag-outline">
              {{ tag }}
            </span>
          </div>
          <h1 class="doc-title">{{ documentData.title }}</h1>

          <div class="doc-stats">
            <div class="stat-item">
              <span class="material-symbols-outlined icon-sm">calendar_month</span>
              {{ documentData.createdAt }}
            </div>
            <div class="stat-item">
              <span class="material-symbols-outlined icon-sm">visibility</span>
              {{ documentData.viewCount || 0 }} Lượt xem
            </div>
          </div>

          <div class="action-group">
            <button class="btn-primary">
              <span class="material-symbols-outlined">download</span>
              Tải xuống ngay
            </button>
            <div class="action-grid">
              <button class="btn-outline">
                <span class="material-symbols-outlined icon-md">favorite</span>
                {{ documentData.likeCount || 0 }} Thích
              </button>
              <button class="btn-outline">
                <span class="material-symbols-outlined icon-md">bookmark_add</span>
                Lưu
              </button>
            </div>
          </div>
        </div>

        <!-- Description & Author -->
        <div class="info-card flex-col-gap">
          <div>
            <h3 class="section-title">Mô tả tài liệu</h3>
            <p class="desc-text">{{ documentData.description }}</p>
          </div>
          <hr class="divider" />
          <div>
            <h3 class="section-title mb-3">Đăng tải bởi</h3>
            <div class="author-info">
              <!-- Vì DTO không có avatar người đăng, dùng icon person mặc định -->
              <div class="author-avatar-placeholder">
                <span class="material-symbols-outlined">person</span>
              </div>
              <div>
                <div class="author-name">{{ documentData.uploaderName }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Comments Section (Sử dụng vòng lặp thực tế) -->
    <div class="comments-section">
      <h2 class="comments-title">Thảo luận ({{ comments?.length || 0 }})</h2>

      <!-- Input Box -->
      <div class="comment-input-box">
        <div class="user-avatar placeholder hide-mobile">
          <span class="material-symbols-outlined">person</span>
        </div>
        <div class="input-wrapper">
          <textarea
            v-model="newComment"
            class="comment-textarea"
            placeholder="Thêm bình luận hoặc đặt câu hỏi..."
          ></textarea>
          <div class="btn-right-align">
            <button @click="postComment" class="btn-primary btn-small">Đăng bình luận</button>
          </div>
        </div>
      </div>

      <!-- Comment List -->
      <div class="comment-list">
        <!-- Vòng lặp lấy dữ liệu thật từ mảng comments -->
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <div class="user-avatar placeholder">
            <span class="material-symbols-outlined">person</span>
          </div>
          <div class="comment-body">
            <div class="comment-header">
              <span class="comment-author">{{ comment.authorName }}</span>
              <span class="comment-time">{{ comment.createdAt }}</span>
            </div>
            <p class="comment-text">
              {{ comment.content }}
            </p>
          </div>
        </div>

        <div v-if="!comments || comments.length === 0" class="no-comments">
          Chưa có bình luận nào. Hãy là người đầu tiên thảo luận!
        </div>
      </div>
    </div>
  </main>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

/* --- CSS Variables & Thiết lập cơ bản --- */
.main-content {
  --color-primary: #004ac6;
  --color-primary-hover: #003ea8;
  --color-primary-container: #2563eb;
  --color-on-primary: #ffffff;
  --color-surface: #faf8ff;
  --color-on-surface: #131b2e;
  --color-on-surface-variant: #434655;
  --color-surface-container-lowest: #ffffff;
  --color-surface-container-low: #f2f3ff;
  --color-surface-variant: #dae2fd;
  --color-secondary-container: #e0e3e5;
  --color-on-secondary-container: #626567;
  --color-outline-variant: #c3c6d7;

  --spacing-gutter: 24px;
  --spacing-stack-md: 16px;
  --spacing-stack-lg: 32px;
  --spacing-stack-xl: 64px;

  flex-grow: 1;
  width: 100%;
  max-width: 1280px;
  margin: 0 auto;
  padding: var(--spacing-stack-lg) 48px;
  box-sizing: border-box;
  font-family: 'Inter', sans-serif;
  color: var(--color-on-surface);
}

@media (max-width: 768px) {
  .main-content {
    padding: var(--spacing-stack-lg) 16px;
  }
}

/* Material Icons */
.material-symbols-outlined {
  font-variation-settings:
    'FILL' 0,
    'wght' 400,
    'GRAD' 0,
    'opsz' 24;
  vertical-align: middle;
}
.material-symbols-outlined[data-weight='fill'] {
  font-variation-settings: 'FILL' 1;
}
.icon-sm {
  font-size: 16px;
}
.icon-md {
  font-size: 20px;
}
/* BẮT BUỘC ĐỂ ICON HIỂN THỊ ĐÚNG */
.material-symbols-outlined {
  font-family: 'Material Symbols Outlined' !important;
  font-variation-settings:
    'FILL' 0,
    'wght' 400,
    'GRAD' 0,
    'opsz' 24;
  vertical-align: middle;
  line-height: 1;
  display: inline-block;
}

.material-symbols-outlined[data-weight='fill'] {
  font-variation-settings: 'FILL' 1;
}

/* CSS cho Placeholder Avatar người dùng */
.author-avatar-placeholder,
.user-avatar.placeholder {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background-color: var(--color-surface-variant);
  color: var(--color-on-surface-variant);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid var(--color-outline-variant);
  flex-shrink: 0;
}

.user-avatar.placeholder {
  width: 40px;
  height: 40px;
}

.no-comments {
  text-align: center;
  color: var(--color-on-surface-variant);
  font-size: 14px;
  padding: 24px;
}

/* --- Cấu trúc chung --- */
.breadcrumbs {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: var(--spacing-stack-md);
  font-size: 12px;
  font-weight: 500;
  color: var(--color-on-surface-variant);
}

.doc-grid {
  display: grid;
  grid-template-columns: repeat(12, minmax(0, 1fr));
  gap: var(--spacing-gutter);
}

@media (max-width: 1023px) {
  .doc-grid {
    display: flex;
    flex-direction: column;
  }
}

.doc-viewer-col {
  grid-column: span 8 / span 8;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-stack-md);
}

.doc-meta-col {
  grid-column: span 4 / span 4;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-stack-lg);
}

/* --- PDF Viewer (Cột Trái) --- */
.pdf-viewer-container {
  background-color: var(--color-surface-container-lowest);
  border: 1px solid var(--color-outline-variant);
  border-radius: 12px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  height: 700px;
}

.viewer-toolbar {
  background-color: var(--color-surface-container-low);
  border-bottom: 1px solid var(--color-outline-variant);
  padding: 12px 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.zoom-text {
  font-size: 12px;
  font-weight: 500;
  padding: 0 8px;
  border-left: 1px solid var(--color-outline-variant);
  border-right: 1px solid var(--color-outline-variant);
}

.viewer-body {
  flex-grow: 1;
  background-color: #e5e7eb;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  padding: 32px;
  overflow-y: auto;
}

.pdf-page-mock {
  width: 100%;
  max-width: 800px;
  height: 1050px;
  background-color: white;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  border: 1px solid var(--color-outline-variant);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
}

.pdf-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0.9;
}

.pdf-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to bottom, transparent, rgba(255, 255, 255, 0.2));
  pointer-events: none;
}

/* --- Info Cards (Cột Phải) --- */
.info-card {
  background-color: var(--color-surface-container-lowest);
  border: 1px solid var(--color-outline-variant);
  border-radius: 12px;
  padding: var(--spacing-stack-md);
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}

.info-card.flex-col-gap {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-stack-md);
}

.card-tags {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.tag-primary {
  background-color: var(--color-secondary-container);
  color: var(--color-on-secondary-container);
  font-size: 12px;
  font-weight: 500;
  padding: 4px 8px;
  border-radius: 4px;
}

.doc-title {
  font-size: 24px;
  font-weight: 600;
  line-height: 32px;
  color: var(--color-on-surface);
  margin: 0 0 8px 0;
}

.doc-stats {
  display: flex;
  align-items: center;
  gap: 16px;
  color: var(--color-on-surface-variant);
  font-size: 12px;
  font-weight: 500;
  margin-bottom: 24px;
}
.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.action-group {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 8px;
}

.btn-primary {
  width: 100%;
  background-color: var(--color-primary);
  color: var(--color-on-primary);
  font-size: 14px;
  font-weight: 500;
  padding: 12px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  transition: background-color 0.2s;
}
.btn-primary:hover {
  background-color: var(--color-primary-hover);
}

.btn-outline {
  width: 100%;
  background-color: transparent;
  color: var(--color-primary);
  border: 1px solid var(--color-outline-variant);
  font-size: 14px;
  font-weight: 500;
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition:
    background-color 0.2s,
    border-color 0.2s;
}
.btn-outline:hover {
  border-color: var(--color-primary);
  background-color: var(--color-surface-variant);
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  line-height: 28px;
  color: var(--color-on-surface);
  margin: 0 0 8px 0;
}
.mb-3 {
  margin-bottom: 12px;
}

.desc-text {
  font-size: 14px;
  color: var(--color-on-surface-variant);
  line-height: 1.625;
  margin: 0;
}

.divider {
  border: 0;
  border-top: 1px solid var(--color-outline-variant);
  margin: 0;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: 1px solid var(--color-outline-variant);
}

.author-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-on-surface);
}

/* --- Comments Section --- */
.comments-section {
  margin-top: var(--spacing-stack-xl);
  max-width: 896px;
}

.comments-title {
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 var(--spacing-stack-md) 0;
}

.comment-input-box {
  background-color: var(--color-surface-container-lowest);
  border: 1px solid var(--color-outline-variant);
  border-radius: 12px;
  padding: var(--spacing-stack-md);
  margin-bottom: var(--spacing-stack-md);
  display: flex;
  gap: 16px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 1px solid var(--color-outline-variant);
  flex-shrink: 0;
}
@media (max-width: 639px) {
  .hide-mobile {
    display: none;
  }
}

.input-wrapper {
  flex-grow: 1;
}

.comment-textarea {
  width: 100%;
  background-color: var(--color-surface);
  border: 1px solid var(--color-outline-variant);
  border-radius: 8px;
  padding: 12px;
  font-size: 14px;
  color: var(--color-on-surface);
  outline: none;
  resize: none;
  height: 96px;
  box-sizing: border-box;
  font-family: 'Inter', sans-serif;
}
.comment-textarea::placeholder {
  color: var(--color-on-surface-variant);
}
.comment-textarea:focus {
  border-color: var(--color-primary);
  box-shadow: 0 0 0 2px #b4c5ff;
}

.btn-right-align {
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
}

.btn-small {
  width: auto;
  padding: 8px 16px;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-stack-md);
}

.comment-item {
  display: flex;
  gap: 16px;
}

.comment-body {
  flex-grow: 1;
  background-color: var(--color-surface-container-low);
  border-radius: 12px;
  padding: 16px;
  border: 1px solid var(--color-outline-variant);
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}

.comment-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 4px;
}

.comment-author {
  font-size: 14px;
  font-weight: 700;
  color: var(--color-on-surface);
}

.comment-time {
  font-size: 12px;
  font-weight: 500;
  color: var(--color-on-surface-variant);
}

.comment-text {
  font-size: 14px;
  color: var(--color-on-surface-variant);
  margin: 0 0 12px 0;
}

.comment-actions {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 12px;
  color: var(--color-on-surface-variant);
}
</style>
