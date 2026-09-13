<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Apis, { endpoints } from '@/configs/apis'
import Header from '@/components/header.vue'
import Footer from '@/components/footer.vue'

const router = useRouter()

// ==========================================
// 1. STATE
// ==========================================
const searchKeyword = ref('')
const isLoading = ref(true)
const trendingDocs = ref([])

// ==========================================
// 2. HÀM XỬ LÝ SỰ KIỆN (ACTIONS)
// ==========================================
const onHeroSearch = () => {
  if (!searchKeyword.value.trim()) return
  router.push({ path: '/documents/search', query: { q: searchKeyword.value } })
}

const goToUpload = () => {
  router.push('/documents/upload')
}

const goToDetail = (id) => {
  router.push(`/documents/${id}`)
}

// ==========================================
// 3. API CALLS
// ==========================================
const fetchTrendingDocuments = async () => {
  isLoading.value = true
  try {
    // Chuẩn hóa gọi endpoint bằng dấu chấm
    const response = await Apis.get(endpoints.documents)
    trendingDocs.value = response.data.content
  } catch (error) {
    console.error('Lỗi khi tải danh sách trending:', error)
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  fetchTrendingDocuments()
})
</script>

<template>
  <div class="page-layout">
    <Header />
    <main class="main-body">
      <!-- Hero Section -->
      <section class="hero-section">
        <div class="hero-content">
          <h1 class="hero-title">
            The world's knowledge, <br />
            <span class="hero-title-highlight">shared professionally.</span>
          </h1>
          <p class="hero-desc">
            Access millions of research papers, study notes, and textbooks shared by academics and
            professionals globally.
          </p>

          <form @submit.prevent="onHeroSearch" class="hero-search-box">
            <svg
              class="hero-search-icon"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
            >
              <circle cx="11" cy="11" r="8"></circle>
              <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
            </svg>
            <input
              v-model="searchKeyword"
              type="text"
              class="hero-search-input"
              placeholder="Search for notes, papers, or textbooks..."
            />
            <button type="submit" class="btn-hero-search">Search</button>
          </form>
        </div>
      </section>

      <!-- Trending Section -->
      <section class="trending-section">
        <div class="section-header">
          <div>
            <h2 class="section-title">Trending Documents</h2>
          </div>
        </div>

        <div v-if="isLoading" class="text-center py-10 text-gray-500">Đang tải dữ liệu...</div>

        <div v-else class="bento-grid">
          <!-- Main Featured Card -->
          <div
            v-if="trendingDocs.length > 0"
            @click="goToDetail(trendingDocs[0].id)"
            class="card card-featured cursor-pointer"
          >
            <div class="card-featured-img-wrap">
              <img
                :src="
                  trendingDocs[0].thumbnail ||
                  'https://images.unsplash.com/photo-1635070041078-e363dbe005cb?w=800&auto=format&fit=crop&q=60'
                "
                :alt="trendingDocs[0].title"
                class="card-img"
              />
            </div>
            <div class="card-featured-body">
              <div class="card-meta">
                <span class="meta-pill">{{ trendingDocs[0].categoryName || 'Tài liệu' }}</span>
              </div>
              <h3 class="card-title">{{ trendingDocs[0].title }}</h3>

              <!-- Đoạn text tĩnh thay thế do DTO chưa có description -->
              <p class="card-summary line-clamp-2">
                {{ trendingDocs[0].description }}
              </p>

              <div class="card-footer">
                <div class="author-row">
                  <div class="author-avatar">
                    {{
                      trendingDocs[0].uploaderName
                        ? trendingDocs[0].uploaderName.charAt(0).toUpperCase()
                        : 'U'
                    }}
                  </div>
                  <span class="author-name">{{ trendingDocs[0].uploaderName || 'Ẩn danh' }}</span>
                </div>
                <span class="download-counter">👁 {{ trendingDocs[0].totalView || 0 }}</span>
              </div>
            </div>
          </div>

          <!-- Secondary Cards (slice(1, 4)) -->
          <div
            v-for="doc in trendingDocs.slice(1, 4)"
            :key="doc.id"
            @click="goToDetail(doc.id)"
            class="card card-secondary cursor-pointer"
          >
            <div class="card-sec-img-wrap">
              <img
                :src="
                  doc.thumbnail ||
                  'https://images.unsplash.com/photo-1532012164546-f432f2e3777a?w=800&auto=format&fit=crop&q=60'
                "
                :alt="doc.title"
                class="card-img"
              />
            </div>
            <div class="card-sec-body">
              <span class="meta-pill">{{ doc.categoryName || 'Tài liệu' }}</span>
              <h3 class="card-sec-title line-clamp-2">{{ doc.title }}</h3>
              <div class="card-sec-footer">
                <span class="meta-dim">By {{ doc.uploaderName || 'Ẩn danh' }}</span>
                <span class="download-counter">👁 {{ doc.totalView || 0 }}</span>
              </div>
            </div>
          </div>

          <!-- CTA Card (Static) -->
          <div class="card card-cta">
            <div class="cta-icon-wrap">
              <svg
                width="42"
                height="42"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="1.8"
              >
                <path d="M17.5 19H9a7 7 0 1 1 6.71-9h1.79a4.5 4.5 0 1 1 0 9Z"></path>
                <polyline points="12 13 12 9 9 12"></polyline>
              </svg>
            </div>
            <h3 class="cta-title">Share Your Knowledge</h3>
            <p class="cta-desc">Upload your documents and contribute to the community.</p>
            <button @click="goToUpload" class="btn-cta">Upload Document</button>
          </div>
        </div>
      </section>
    </main>
    <Footer />
  </div>
</template>

<style scoped>
@import '../styles/style.css';
</style>

<style scoped>
@import '../styles/style.css';
</style>
