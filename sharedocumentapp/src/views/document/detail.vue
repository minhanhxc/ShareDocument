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
const errorMessage = ref('')
const isLiked = ref(false)
const isBookmarked = ref(false)
const fetchDocumentDetail = async () => {
  isLoading.value = true
  errorMessage.value = ''
  try {
    const docId = route.params.id
    const res = await authApis.get(endpoints['documentDetail'](docId))
    documentData.value = res.data
    isLiked.value = res.data.isLiked || false
    isBookmarked.value = res.data.isBookmarked || false
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Không thể kết nối tới server'
    console.error('Lỗi khi tải dữ liệu:', errorMessage.value)
  } finally {
    isLoading.value = false
  }
}

const handleLike = async () => {
  if (!documentData.value) return
  errorMessage.value = ''
  const previousState = isLiked.value
  isLiked.value = !isLiked.value
  documentData.value.likeCount = isLiked.value
    ? (documentData.value.likeCount || 0) + 1
    : Math.max(0, (documentData.value.likeCount || 0) - 1)

  try {
    const docId = route.params.id
    const res = await authApis.post(endpoints['like'](docId))
    isLiked.value = res.data
    console.log(`Đã ${isLiked.value ? 'thích' : 'bỏ thích'} tài liệu ${docId}`)
  } catch (error) {
    isLiked.value = previousState
    documentData.value.likeCount = isLiked.value
      ? (documentData.value.likeCount || 0) + 1
      : Math.max(0, (documentData.value.likeCount || 0) - 1)
    errorMessage.value = error.response?.data?.message || 'Không thể kết nối tới server'

    console.error('Lỗi khi thích tài liệu:', errorMessage.value)
  }
}

const handleBookmark = async () => {
  if (!documentData.value) return
  const previousState = isBookmarked.value
  isBookmarked.value = !isBookmarked.value

  try {
    const docId = route.params.id
    const res = await authApis.post(endpoints['bookmarked'](docId))
    isBookmarked.value = res.data
    console.log(`Đã ${isBookmarked.value ? 'lưu' : 'bỏ lưu'} tài liệu ${docId}`)
  } catch (error) {
    isBookmarked.value = previousState
    errorMessage.value = error.response?.data?.message || 'Không thể kết nối tới server'
    console.error('Lỗi khi lưu tài liệu:', errorMessage.value)
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
  <Header />
  <main class="main-content">
    <!-- Breadcrumbs -->
    <nav class="breadcrumbs"></nav>

    <!-- Thêm v-if để tránh lỗi khi dữ liệu chưa gọi xong -->
    <div v-if="documentData" class="doc-layout">
      <!-- CỘT TRÁI: KHUNG XEM TRƯỚC & BÌNH LUẬN -->
      <div class="layout-left">
        <!-- Khung xem trước tài liệu -->
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

        <!-- Phần Bình luận (Di chuyển xuống dưới Iframe) -->
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
          </div>
        </div>
      </div>

      <!-- CỘT PHẢI: THÔNG TIN TÀI LIỆU -->
      <div class="layout-right">
        <!-- KHUNG 1: Metadata & Hành động -->
        <div class="info-card">
          <!-- Hàng 1: Tags -->
          <div class="card-tags">
            <span class="tag-primary">{{ documentData.fileType }}</span>
            <span v-for="tag in documentData.tagNames" :key="tag" class="tag-outline">
              {{ tag }}
            </span>
          </div>

          <!-- Hàng 2: Title -->
          <h1 class="doc-title">{{ documentData.title }}</h1>

          <!-- Hàng 3: Category -->
          <div class="doc-category">
            Danh mục: <strong>{{ documentData.categoryName }}</strong>
          </div>

          <!-- Hàng 4: Stats (Thời gian, Lượt xem, Lượt thích) -->
          <div class="doc-stats">
            <div class="stat-item">
              <span class="material-symbols-outlined icon-sm">calendar_month</span>
              {{ documentData.createdAt }}
            </div>
            <div class="stat-item">
              <span class="material-symbols-outlined icon-sm">visibility</span>
              {{ documentData.viewCount || 0 }}
            </div>
          </div>

          <!-- Hàng 5: Nút Download -->
          <button class="btn-primary btn-download">
            <span class="material-symbols-outlined">download</span>
            Tải xuống ngay
          </button>

          <div class="action-grid">
            <!-- Nút Thích -->
            <button class="btn-outline" :class="{ 'active-btn': isLiked }" @click="handleLike">
              <span
                class="material-symbols-outlined icon-md"
                :data-weight="isLiked ? 'fill' : ''"
                :style="{ color: isLiked ? '#ef4444' : 'inherit' }"
              >
                favorite
              </span>
              {{ documentData.likeCount || 0 }} {{ isLiked ? 'Đã thích' : 'Thích' }}
            </button>

            <!-- Nút Lưu -->
            <button
              class="btn-outline"
              :class="{ 'active-btn': isBookmarked }"
              @click="handleBookmark"
            >
              <span
                class="material-symbols-outlined icon-md"
                :data-weight="isBookmarked ? 'fill' : ''"
                :style="{ color: isBookmarked ? '#0d6efd' : 'inherit' }"
              >
                bookmark_add
              </span>
              {{ isBookmarked ? 'Đã lưu' : 'Lưu' }}
            </button>
          </div>
        </div>

        <!-- KHUNG 2: Description & Author -->
        <div class="info-card flex-col-gap">
          <!-- Phần trên: Mô tả -->
          <div>
            <h3 class="section-title">Mô tả tài liệu</h3>
            <p class="desc-text">{{ documentData.description }}</p>
          </div>

          <!-- Dấu gạch ngang kết thúc phần mô tả -->
          <hr class="divider" />

          <!-- Phần dưới: Đăng tải bởi -->
          <div>
            <h3 class="section-title mb-3">Đăng tải</h3>
            <div class="author-info">
              <!-- Avatar bên trái -->
              <div class="author-avatar-placeholder">
                <span class="material-symbols-outlined">person</span>
              </div>
              <!-- Tên bên phải -->
              <div>
                <div class="author-name">{{ documentData.uploaderName }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
  <Footer />
</template>

<style scoped>
@import '@/styles/detailDocument.css';
</style>
