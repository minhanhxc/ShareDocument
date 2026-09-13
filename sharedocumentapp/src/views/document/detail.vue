<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import Apis, { authApis, endpoints } from '@/configs/apis'
import Header from '@/components/header.vue'
import Footer from '@/components/footer.vue'
import { computed } from 'vue'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/vi'

dayjs.extend(relativeTime)
dayjs.locale('vi')

const route = useRoute()

const documentData = ref({})
const isLoading = ref(true)
const isLiked = ref(false)
const isBookmarked = ref(false)

const comments = ref([])
const newComment = ref('')
const isSubmittingComment = ref(false)
const isLoadingComments = ref(false)

const currentPage = ref(1)
const totalPages = ref(0)
const totalComments = ref(0)
const pageSize = 10

const fetchDocumentDetail = async () => {
  isLoading.value = true
  try {
    const docId = route.params.id
    const res = await authApis.get(endpoints.documentDetail(docId))

    documentData.value = res.data
    isLiked.value = res.data.liked
    isBookmarked.value = res.data.bookmarked
  } catch (error) {
    console.error('Lỗi khi tải dữ liệu:', error)
  } finally {
    isLoading.value = false
  }
}

const fetchComments = async (page = 0) => {
  isLoadingComments.value = true
  try {
    const docId = route.params.id
    const res = await authApis.get(endpoints.comment(docId), {
      params: {
        page: currentPage.value - 1,
        size: pageSize,
      },
    })
    comments.value = res.data.content
    totalComments.value = res.data.totalElements
    currentPage.value = res.data.number
    totalPages.value = res.data.totalPages
  } catch (error) {
    console.error('Lỗi khi tải bình luận:', error)
  } finally {
    isLoadingComments.value = false
  }
}

const changePage = (newPage) => {
  if (newPage >= 1 && newPage <= totalPages.value) {
    currentPage.value = newPage
    fetchComments()
  }
}

const handlePostComment = async () => {
  if (!newComment.value.trim()) return
  isSubmittingComment.value = true

  try {
    const docId = route.params.id
    const payload = { content: newComment.value.trim() }
    const res = await authApis.post(endpoints.addComment(docId), payload)

    comments.value.unshift(res.data)
    totalComments.value++
    newComment.value = ''
  } catch (error) {
    alert(error.response?.data?.message || 'Không thể đăng bình luận')
    console.error('Lỗi đăng bình luận:', error)
  } finally {
    isSubmittingComment.value = false
  }
}

const handleDeleteComment = async (commentId) => {
  if (!confirm('Bạn có chắc chắn muốn xóa bình luận này?')) return

  try {
    await authApis.delete(endpoints.deleteComment(commentId))
    comments.value = comments.value.filter((c) => c.id !== commentId)
    totalComments.value--
  } catch (error) {
    alert(error.response?.data?.message || 'Không thể xóa bình luận')
    console.error('Lỗi xóa bình luận:', error)
  }
}

const handleLike = async () => {
  if (!documentData.value) return

  const previousState = isLiked.value
  isLiked.value = !isLiked.value

  documentData.value.totalLike = isLiked.value
    ? (documentData.value.totalLike || 0) + 1
    : Math.max(0, (documentData.value.totalLike || 0) - 1)
  console.log(isLiked.value)

  try {
    const docId = route.params.id
    const res = await authApis.post(endpoints.like(docId))
    isLiked.value = res.data.liked
  } catch (error) {
    isLiked.value = previousState
    documentData.value.totalLike = isLiked.value
      ? (documentData.value.totalLike || 0) + 1
      : Math.max(0, (documentData.value.totalLike || 0) - 1)

    console.error('Lỗi khi thích tài liệu:', error)
  }
}
const handleDownload = (fileUrl) => {
  if (!fileUrl) {
    alert('Không có file để tải xuống.')
    return
  }
  const link = document.createElement('a')
  link.href = fileUrl
  link.download = ''
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

const handleBookmark = async () => {
  if (!documentData.value) return
  const previousState = isBookmarked.value
  isBookmarked.value = !isBookmarked.value

  try {
    const docId = route.params.id
    const res = await authApis.post(endpoints.addOrRemoveDocument(docId))
    isBookmarked.value = res.data.bookmarked
  } catch (error) {
    isBookmarked.value = previousState
    console.error('Lỗi khi lưu tài liệu:', error)
  }
}

const formatTimeAgo = (dateString) => {
  if (!dateString) return ''
  return dayjs(dateString).fromNow()
}

onMounted(() => {
  fetchDocumentDetail()
  fetchComments()
})

const isPdf = computed(() => {
  return documentData.value?.fileType?.toUpperCase() === 'PDF'
})

const isOfficeFile = computed(() => {
  const type = documentData.value?.fileType?.toUpperCase()
  return ['DOCX', 'DOC', 'PPTX', 'PPT', 'XLSX', 'XLS'].includes(type)
})

const googleDocsViewerUrl = computed(() => {
  if (!documentData.value?.fileUrl) return ''
  const encodedUrl = encodeURIComponent(documentData.value.fileUrl)
  return `https://docs.google.com/gview?url=${encodedUrl}&embedded=true`
})
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

        <!-- Phần Bình luận -->
        <div class="comments-section mt-4">
          <h2 class="comments-title">Thảo luận ({{ totalComments || 0 }})</h2>

          <!-- Input Box -->
          <div class="comment-input-box">
            <!-- GỢI Ý: Lấy avatar từ AuthStore (nếu có) thay thế cho icon tĩnh này -->
            <div
              class="user-avatar placeholder hide-mobile"
              style="overflow: hidden; display: flex; align-items: center; justify-content: center"
            >
              <span class="material-symbols-outlined">person</span>
            </div>

            <div class="input-wrapper">
              <textarea
                v-model="newComment"
                class="comment-textarea"
                placeholder="Thêm bình luận..."
                :disabled="isSubmittingComment"
              ></textarea>
              <div class="btn-right-align" style="margin-top: 8px; text-align: right">
                <button
                  @click="handlePostComment"
                  class="btn-primary btn-small"
                  :disabled="!newComment.trim() || isSubmittingComment"
                >
                  {{ isSubmittingComment ? 'Đang đăng...' : 'Đăng bình luận' }}
                </button>
              </div>
            </div>
          </div>

          <!-- Comment List -->
          <div class="comment-list">
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
              <!-- Avatar -->
              <div
                class="user-avatar"
                :class="{ placeholder: !comment.avatar }"
                style="
                  width: 40px;
                  height: 40px;
                  flex-shrink: 0;
                  border-radius: 50%;
                  overflow: hidden;
                  display: flex;
                  align-items: center;
                  justify-content: center;
                "
              >
                <img
                  v-if="comment.avatar"
                  :src="comment.avatar"
                  alt="Avatar"
                  class="avatar-img"
                  style="width: 100%; height: 100%; object-fit: cover"
                />
                <span v-else class="material-symbols-outlined">person</span>
              </div>

              <div class="comment-body">
                <div class="comment-header flex-between">
                  <div>
                    <span class="comment-author">{{ comment.username }}</span>
                    <span class="comment-time">{{ formatTimeAgo(comment.createdAt) }}</span>
                  </div>
                  <!-- Nút Xóa (Tạm thời hiển thị cho mọi comment) -->
                  <button
                    class="btn-icon text-muted"
                    @click="handleDeleteComment(comment.id)"
                    title="Xóa bình luận"
                    style="border: none; background: none; cursor: pointer"
                  >
                    <span class="material-symbols-outlined" style="font-size: 18px">delete</span>
                  </button>
                </div>
                <p class="comment-text" style="white-space: pre-wrap">{{ comment.content }}</p>
              </div>
            </div>
          </div>

          <!-- Phân trang Comment -->
          <div
            class="pagination-wrapper mt-4 mb-4"
            v-if="totalPages > 1"
            style="display: flex; justify-content: center; gap: 8px"
          >
            <!-- SỬA TẠI ĐÂY 1: Đồng bộ tên hàm changePage và class active -->
            <button
              v-for="pageNum in totalPages"
              :key="pageNum"
              class="page-btn"
              :class="{ active: currentPage === pageNum }"
              @click="changePage(pageNum)"
              style="
                width: 32px;
                height: 32px;
                display: flex;
                align-items: center;
                justify-content: center;
                border: 1px solid #e2e8f0;
                background: white;
                color: #475569;
                border-radius: 6px;
                cursor: pointer;
                transition: all 0.2s;
              "
              onmouseover="
                this.style.borderColor = '#0d6efd'
                this.style.color = '#0d6efd'
              "
              onmouseout="
                if (!this.classList.contains('active')) {
                  this.style.borderColor = '#e2e8f0'
                  this.style.color = '#475569'
                }
              "
            >
              {{ pageNum }}
            </button>
          </div>
        </div>
      </div>

      <!-- CỘT PHẢI: THÔNG TIN TÀI LIỆU -->
      <div class="layout-right">
        <!-- KHUNG 1: Metadata & Hành động -->
        <div class="info-card">
          <div class="card-tags">
            <span class="tag-primary">{{ documentData.fileType }}</span>
            <span v-for="tag in documentData.tagNames" :key="tag" class="tag-outline">
              {{ tag }}
            </span>
          </div>

          <h1 class="doc-title">{{ documentData.title }}</h1>

          <div class="doc-category">
            Danh mục: <strong>{{ documentData.categoryName }}</strong>
          </div>

          <div class="doc-stats">
            <div class="stat-item">
              <span class="material-symbols-outlined icon-sm">calendar_month</span>
              {{ documentData.createdAt }}
            </div>
            <div class="stat-item">
              <span class="material-symbols-outlined icon-sm">visibility</span>
              {{ documentData.totalView || 0 }}
            </div>
          </div>

          <button class="btn-primary btn-download" @click="handleDownload(documentData.fileUrl)">
            <span class="material-symbols-outlined">download</span>
            Tải xuống ngay
          </button>

          <div class="action-grid">
            <button class="btn-outline" :class="{ 'active-btn': isLiked }" @click="handleLike">
              <span
                class="material-symbols-outlined icon-md"
                :data-weight="isLiked ? 'fill' : ''"
                :style="{ color: isLiked ? '#ef4444' : 'inherit' }"
              >
                favorite
              </span>
              {{ documentData.totalLike || 0 }} {{ isLiked ? 'Đã thích' : 'Thích' }}
            </button>

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
          <div>
            <h3 class="section-title">Mô tả tài liệu</h3>
            <p class="desc-text">{{ documentData.description }}</p>
          </div>
          <hr class="divider" />
          <div>
            <h3 class="section-title mb-3">Đăng tải</h3>
            <div class="author-info">
              <!-- SỬA TẠI ĐÂY 2: Thay thế icon tĩnh bằng avatar động của Uploader -->
              <div
                class="author-avatar-placeholder"
                style="
                  overflow: hidden;
                  display: flex;
                  align-items: center;
                  justify-content: center;
                  border-radius: 50%;
                "
              >
                <img
                  v-if="documentData.uploaderAvatar"
                  :src="documentData.uploaderAvatar"
                  alt="Uploader Avatar"
                  style="width: 100%; height: 100%; object-fit: cover"
                />
                <span v-else class="material-symbols-outlined">person</span>
              </div>

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
