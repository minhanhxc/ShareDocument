<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Apis, { authApis, endpoints } from '@/configs/apis'
import Footer from '@/components/footer.vue'
import Header from '@/components/header.vue'
const route = useRoute()
const router = useRouter()

const documents = ref([])
const categories = ref([])
const tags = ref([])
const isLoading = ref(false)

const currentKeyword = ref('')
const selectedCategoryId = ref(null)
const selectedTagIds = ref([])
const currentPage = ref(1)
const totalElements = ref(0)
const totalPages = ref(0)
const pageSize = 10

const syncParamsFromUrl = () => {
  currentKeyword.value = route.query.q || ''
  selectedCategoryId.value = route.query.categoryId ? Number(route.query.categoryId) : null

  if (route.query.tagIds) {
    selectedTagIds.value = route.query.tagIds.split(',').map(Number)
  } else {
    selectedTagIds.value = []
  }

  currentPage.value = route.query.page ? Number(route.query.page) : 1
}

const loadFilterData = async () => {
  try {
    const [catRes, tagRes] = await Promise.all([
      authApis.get(endpoints['categories']),
      authApis.get(endpoints['tags']),
    ])
    categories.value = catRes.data
    tags.value = tagRes.data
  } catch (error) {
    console.error('Lỗi tải bộ lọc:', error)
  }
}

const fetchSearchResults = async () => {
  isLoading.value = true
  try {
    const res = await Apis.get(endpoints.search, {
      params: {
        keyword: currentKeyword.value,
        categoryId: selectedCategoryId.value,

        tagId: selectedTagIds.value.length > 0 ? selectedTagIds.value : null,
        page: currentPage.value - 1,
        size: pageSize,
      },
    })
    documents.value = res.data.content
    totalPages.value = res.data.totalPages
    totalElements.value = res.data.totalElements
  } catch (error) {
    console.error('Lỗi khi tìm kiếm:', error)
  } finally {
    isLoading.value = false
  }
}

const applyFilters = () => {
  currentPage.value = 1
  updateUrl()
}

const updateUrl = () => {
  const query = { q: currentKeyword.value }
  if (selectedCategoryId.value) query.categoryId = selectedCategoryId.value
  if (selectedTagIds.value.length > 0) query.tagIds = selectedTagIds.value.join(',')
  router.push({ path: '/documents/search', query })
}

const removeCategoryFilter = () => {
  selectedCategoryId.value = null
  applyFilters()
}

const removeTagFilter = (tagId) => {
  selectedTagIds.value = selectedTagIds.value.filter((id) => id !== tagId)
  applyFilters()
}

const clearAllFilters = () => {
  selectedCategoryId.value = null
  selectedTagIds.value = []
  applyFilters()
}

const changePage = (newPage) => {
  if (newPage >= 1 && newPage <= totalPages.value) {
    currentPage.value = newPage
    updateUrl()
  }
}

const goToDetail = (id) => {
  router.push(`/documents/${id}`)
}

const getCategoryName = (id) => categories.value.find((c) => c.id === id)?.name || ''
const getTagName = (id) => tags.value.find((t) => t.id === id)?.name || ''

watch(
  () => route.query,
  () => {
    syncParamsFromUrl()
    fetchSearchResults()
  },
  { deep: true },
)

onMounted(async () => {
  await loadFilterData()
  syncParamsFromUrl()
  fetchSearchResults()
})
</script>

<template>
  <Header />
  <div class="search-page-wrapper light-theme">
    <div class="search-container">
      <header class="search-header">
        <div class="result-stats">
          Tìm thấy <strong>{{ totalElements }}</strong> tài liệu phù hợp cho
          <span>"{{ currentKeyword }}"</span>
        </div>
      </header>

      <div class="main-layout">
        <aside class="filter-sidebar">
          <div class="filter-header">
            <h3><i class="icon-filter"></i> Bộ lọc tìm kiếm</h3>
            <button class="btn-clear" @click="clearAllFilters">Đặt lại tất cả</button>
          </div>

          <!-- Danh mục: vẫn giữ radio - 1 tài liệu chỉ thuộc 1 category -->
          <div class="filter-group">
            <h4 class="group-title">Danh mục</h4>
            <div class="checkbox-list">
              <label v-for="cat in categories" :key="cat.id" class="checkbox-item">
                <input
                  type="radio"
                  name="category"
                  :value="cat.id"
                  v-model="selectedCategoryId"
                  @change="applyFilters"
                />
                <span class="checkmark"></span>
                <span class="label-text">{{ cat.name }}</span>
              </label>
            </div>
          </div>

          <!-- Tags: đổi sang checkbox - chọn được nhiều tag cùng lúc -->
          <div class="filter-group">
            <h4 class="group-title">Tags</h4>
            <div class="checkbox-list">
              <label v-for="tag in tags" :key="tag.id" class="checkbox-item">
                <input
                  type="checkbox"
                  :value="tag.id"
                  v-model="selectedTagIds"
                  @change="applyFilters"
                />
                <span class="checkmark"></span>
                <span class="label-text">{{ tag.name }}</span>
              </label>
            </div>
          </div>
        </aside>

        <main class="results-content">
          <div class="active-filters" v-if="selectedCategoryId || selectedTagIds.length > 0">
            <span class="filter-label">Đang lọc:</span>
            <div class="chip" v-if="selectedCategoryId">
              Danh mục: {{ getCategoryName(selectedCategoryId) }}
              <button @click="removeCategoryFilter">×</button>
            </div>
            <!-- Hiện từng chip riêng cho MỖI tag đang chọn, không chỉ 1 -->
            <div class="chip" v-for="tagId in selectedTagIds" :key="tagId">
              Tag: #{{ getTagName(tagId) }}
              <button @click="removeTagFilter(tagId)">×</button>
            </div>
          </div>

          <div v-if="isLoading" class="loading-state">Đang tìm kiếm dữ liệu...</div>

          <div v-else-if="documents.length > 0" class="document-list">
            <div v-for="doc in documents" :key="doc.id" class="doc-card-horizontal">
              <div class="doc-thumbnail-wrapper">
                <img :src="doc.thumbnail" alt="Thumbnail" class="doc-thumb-img" />
              </div>

              <div class="doc-info">
                <h3 class="doc-title" @click="goToDetail(doc.id)">{{ doc.title }}</h3>
                <div class="doc-meta">
                  <span class="uploader">🧑‍💻 {{ doc.uploaderName }}</span>
                  <span class="dot">•</span>
                  <span class="date">{{ doc.createdAt }}</span>
                </div>
                <div class="doc-tags">
                  <span v-for="tag in doc.tagNames" :key="tag" class="tag-chip">#{{ tag }}</span>
                </div>
                <div class="doc-stats">
                  <span>👁 {{ doc.totalView }} lượt xem</span>
                  <span>❤️ {{ doc.totalLike }} lượt thích</span>
                </div>
              </div>
            </div>
          </div>

          <div v-else class="empty-state">Không tìm thấy tài liệu nào phù hợp với bộ lọc.</div>

          <div v-if="totalPages > 1" class="pagination">
            <button :disabled="currentPage === 1" @click="changePage(currentPage - 1)">‹</button>
            <button
              v-for="page in totalPages"
              :key="page"
              :class="{ active: page === currentPage }"
              @click="changePage(page)"
            >
              {{ page }}
            </button>
            <button :disabled="currentPage === totalPages" @click="changePage(currentPage + 1)">
              ›
            </button>
          </div>
        </main>
      </div>
    </div>
  </div>
  <Footer />
</template>

<style scoped>
.light-theme {
  background-color: #f8f9fa;
  color: #212529;
  min-height: 100vh;
  padding: 24px 0;
  font-family: 'Inter', sans-serif;
}
.search-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}
.search-header {
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.breadcrumb {
  font-size: 13px;
  color: #6b7280;
}
.breadcrumb span {
  color: #111827;
  font-weight: 600;
}
.result-stats {
  font-size: 14px;
  color: #6b7280;
}
.result-stats strong {
  color: #0d6efd;
  font-size: 15px;
} /* Số lượng màu xanh */
.result-stats span {
  color: #111827;
  font-weight: bold;
}

/* BỐ CỤC CHÍNH */
.main-layout {
  display: flex;
  gap: 24px;
  align-items: flex-start;
}

/* SIDEBAR BỘ LỌC */
.filter-sidebar {
  width: 260px;
  background-color: #ffffff;
  border: 1px solid #e5e7eb; /* Viền xám nhạt */
  border-radius: 8px;
  padding: 20px;
  flex-shrink: 0;
}
.filter-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  align-items: center;
}
.filter-header h3 {
  font-size: 16px;
  margin: 0;
  color: #111827;
  font-weight: 600;
}
.btn-clear {
  background: none;
  border: none;
  color: #0d6efd;
  cursor: pointer;
  font-size: 13px;
  font-weight: 500;
}
.btn-clear:hover {
  text-decoration: underline;
}

.group-title {
  font-size: 14px;
  color: #111827;
  font-weight: 600;
  margin-bottom: 12px;
}
.filter-group {
  margin-bottom: 24px;
}
.checkbox-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  cursor: pointer;
  font-size: 14px;
  color: #4b5563;
}
.checkbox-item input {
  margin-right: 10px;
  accent-color: #0d6efd;
}

.doc-thumbnail-wrapper {
  width: 80px;
  height: 100px;
  background: #f3f4f6;
  border-radius: 6px;
  overflow: hidden;
  flex-shrink: 0;
  border: 1px solid #e5e7eb;
  display: flex;
  align-items: center;
  justify-content: center;
}

.doc-thumb-img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* Giúp ảnh tự động căn chỉnh lấp đầy khung mà không bị bóp méo */
}

/* MAIN CONTENT */
.results-content {
  flex-grow: 1;
}

/* Các chip đang lọc */
.active-filters {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-bottom: 20px;
  background: #ffffff;
  padding: 12px 16px;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
}
.filter-label {
  font-size: 13px;
  color: #6b7280;
}
.chip {
  background: #eff6ff;
  color: #1d4ed8;
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 500;
}
.chip button {
  background: none;
  border: none;
  color: #1d4ed8;
  cursor: pointer;
  font-weight: bold;
  font-size: 14px;
  padding: 0;
}

/* CARD TÀI LIỆU (HORIZONTAL) */
.doc-card-horizontal {
  display: flex;
  background-color: #ffffff;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 16px;
  gap: 20px;
  border: 1px solid #e5e7eb;
  transition: all 0.2s ease;
}
.doc-card-horizontal:hover {
  border-color: #a4cafe;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.pdf-icon {
  color: #ef4444;
  font-weight: bold;
  font-size: 18px;
}

.doc-info {
  flex-grow: 1;
}
.doc-title {
  margin: 0 0 8px 0;
  font-size: 18px;
  color: #111827;
  cursor: pointer;
  font-weight: 600;
  line-height: 1.4;
}
.doc-title:hover {
  color: #0d6efd;
}
.doc-meta {
  font-size: 13px;
  color: #6b7280;
  margin-bottom: 12px;
  display: flex;
  gap: 8px;
  align-items: center;
}
.uploader {
  color: #111827;
  font-weight: 500;
}
.doc-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}
.tag-chip {
  background: #f3f4f6;
  color: #4b5563;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}
.doc-stats {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: #6b7280;
  font-weight: 500;
}

/* NÚT BẤM (BUTTONS) */
.doc-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
  justify-content: center;
  min-width: 120px;
}
.btn-primary {
  background: #0d6efd;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  font-size: 14px;
  transition: background 0.2s;
}
.btn-outline {
  background: transparent;
  color: #4b5563;
  border: 1px solid #d1d5db;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  font-size: 14px;
  transition: all 0.2s;
}
.btn-primary:hover {
  background: #0b5ed7;
}
.btn-outline:hover {
  background: #f9fafb;
  border-color: #9ca3af;
  color: #111827;
}

/* PHÂN TRANG (PAGINATION) */
.pagination {
  display: flex;
  justify-content: center;
  gap: 6px;
  margin-top: 32px;
  padding-bottom: 24px;
}
.pagination button {
  background: #ffffff;
  border: 1px solid #e5e7eb;
  color: #4b5563;
  width: 36px;
  height: 36px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.2s;
}
.pagination button:hover:not(:disabled) {
  background: #f3f4f6;
}
.pagination button.active {
  background: #0d6efd;
  color: white;
  border-color: #0d6efd;
}
.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  background: #f9fafb;
}

/* TRẠNG THÁI LOADING / TRỐNG */
.loading-state,
.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: #ffffff;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
  color: #6b7280;
  font-size: 15px;
}
</style>
