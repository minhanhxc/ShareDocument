<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Apis, { endpoints } from '@/configs/apis'
import Header from '@/components/header.vue'
import Footer from '@/components/footer.vue'

const router = useRouter()
const searchKeyword = ref('')
const isLoading = ref(true)

const trendingDocs = ref([])

const onHeroSearch = () => {
  if (!searchKeyword.value.trim()) return
  router.push({ path: '/documents', query: { q: searchKeyword.value } })
}

const goToUpload = () => {
  router.push('/documents/upload')
}

const goToDetail = (id) => {
  router.push(`/documents/${id}`)
}

const fetchTrendingDocuments = async () => {
  try {
    const response = await Apis.get(`${endpoints.documents}?sortBy=views&size=4`)
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
            <p class="section-subtitle">The most downloaded resources this week.</p>
          </div>
          <RouterLink to="/documents" class="view-all-link"> View all trending → </RouterLink>
        </div>

        <div v-if="isLoading" class="text-center py-10 text-gray-500">Đang tải dữ liệu...</div>

        <div v-else class="bento-grid">
          <div
            v-if="trendingDocs.length > 0"
            @click="goToDetail(trendingDocs[0].id)"
            class="card card-featured cursor-pointer"
          >
            <div class="card-featured-img-wrap">
              <!-- ĐÃ CẬP NHẬT: thumbnail -->
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
                <!-- ĐÃ CẬP NHẬT: categoryName -->
                <span class="meta-pill">{{ trendingDocs[0].categoryName || 'Tài liệu' }}</span>
              </div>
              <h3 class="card-title">{{ trendingDocs[0].title }}</h3>

              <!-- DTO không có trường description, đặt tĩnh để giữ layout -->
              <p class="card-summary line-clamp-2">
                Tài liệu này hiện chưa có mô tả chi tiết từ người đăng tải. Vui lòng bấm vào để xem
                nội dung đầy đủ.
              </p>

              <div class="card-footer">
                <div class="author-row">
                  <div class="author-avatar">
                    <!-- ĐÃ CẬP NHẬT: uploaderName -->
                    {{
                      trendingDocs[0].uploaderName
                        ? trendingDocs[0].uploaderName.charAt(0).toUpperCase()
                        : 'U'
                    }}
                  </div>
                  <span class="author-name">{{ trendingDocs[0].uploaderName || 'Ẩn danh' }}</span>
                </div>
                <!-- ĐÃ CẬP NHẬT: totalView -->
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
              <!-- ĐÃ CẬP NHẬT: thumbnail -->
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
              <!-- ĐÃ CẬP NHẬT: categoryName -->
              <span class="meta-pill">{{ doc.categoryName || 'Tài liệu' }}</span>
              <h3 class="card-sec-title line-clamp-2">{{ doc.title }}</h3>
              <div class="card-sec-footer">
                <!-- ĐÃ CẬP NHẬT: uploaderName -->
                <span class="meta-dim">By {{ doc.uploaderName || 'Ẩn danh' }}</span>
                <!-- ĐÃ CẬP NHẬT: totalView -->
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
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

.page-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #faf8ff;
  color: #131b2e;
  font-family: 'Inter', sans-serif;
}

.main-body {
  flex: 1;
}

/* Hero Section */
.hero-section {
  background-color: #f2f3ff;
  padding: 64px 24px;
  text-align: center;
}

@media (min-width: 768px) {
  .hero-section {
    padding: 96px 24px;
  }
}

.hero-content {
  max-width: 820px;
  margin: 0 auto;
}

.hero-title {
  font-size: 36px;
  line-height: 1.25;
  font-weight: 700;
  margin: 0 0 16px 0;
  color: #131b2e;
}

@media (min-width: 768px) {
  .hero-title {
    font-size: 48px;
  }
}

.hero-title-highlight {
  color: #004ac6;
}

.hero-desc {
  font-size: 16px;
  line-height: 1.6;
  color: #434655;
  margin: 0 auto 32px auto;
  max-width: 640px;
}

.hero-search-box {
  max-width: 620px;
  margin: 0 auto 24px auto;
  background-color: #ffffff;
  border-radius: 9999px;
  border: 1px solid #c3c6d7;
  padding: 6px 8px 6px 18px;
  display: flex;
  align-items: center;
  box-shadow: 0 8px 30px rgba(0, 74, 198, 0.08);
}

.hero-search-icon {
  width: 20px;
  height: 20px;
  color: #737686;
  margin-right: 8px;
}

.hero-search-input {
  flex: 1;
  border: none;
  background: transparent;
  font-size: 15px;
  outline: none;
  color: #131b2e;
}

.btn-hero-search {
  padding: 10px 24px;
  background-color: #004ac6;
  color: #ffffff;
  border: none;
  border-radius: 9999px;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
}

.hero-badges {
  display: flex;
  justify-content: center;
  gap: 20px;
  font-size: 13.5px;
  color: #434655;
}

/* Trending Section */
.trending-section {
  max-width: 1280px;
  margin: 0 auto;
  padding: 64px 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 32px;
}

.section-title {
  font-size: 26px;
  font-weight: 600;
  margin: 0 0 6px 0;
}

.section-subtitle {
  font-size: 14.5px;
  color: #434655;
  margin: 0;
}

.view-all-link {
  color: #004ac6;
  font-weight: 500;
  font-size: 14px;
  text-decoration: none;
}

/* Bento Grid */
.bento-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 24px;
}

@media (min-width: 768px) {
  .bento-grid {
    grid-template-columns: repeat(12, 1fr);
  }
}

.card {
  background-color: #ffffff;
  border: 1px solid #c3c6d7;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.03);
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.card:hover {
  box-shadow: 0 10px 24px rgba(0, 74, 198, 0.08);
}

.card-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
}

.card:hover .card-img {
  transform: scale(1.03);
}

/* Featured Card (Col span 8) */
.card-featured {
  display: flex;
  flex-direction: column;
}

@media (min-width: 768px) {
  .card-featured {
    grid-column: span 8;
    flex-direction: row;
  }
}

.card-featured-img-wrap {
  position: relative;
  overflow: hidden;
  height: 240px;
}

@media (min-width: 768px) {
  .card-featured-img-wrap {
    width: 50%;
    height: auto;
  }
}

.tag-floating {
  position: absolute;
  top: 14px;
  left: 14px;
  background: rgba(255, 255, 255, 0.9);
  padding: 4px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 600;
  border: 1px solid #c3c6d7;
}

.card-featured-body {
  padding: 24px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

@media (min-width: 768px) {
  .card-featured-body {
    width: 50%;
  }
}

.meta-pill {
  display: inline-block;
  background-color: #eaedff;
  color: #004ac6;
  font-size: 11.5px;
  font-weight: 600;
  padding: 3px 8px;
  border-radius: 4px;
}

.meta-dim {
  font-size: 12px;
  color: #737686;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  margin: 12px 0 8px 0;
  line-height: 1.4;
}

.card-summary {
  font-size: 13.5px;
  color: #434655;
  line-height: 1.5;
  margin: 0 0 16px 0;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #f2f3ff;
}

.author-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background-color: #2563eb;
  color: #ffffff;
  font-size: 11px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
}

.author-name {
  font-size: 13px;
  font-weight: 500;
}

/* Secondary Cards (Col span 4) */
.card-secondary {
  display: flex;
  flex-direction: column;
}

@media (min-width: 768px) {
  .card-secondary {
    grid-column: span 4;
  }
}

.card-sec-img-wrap {
  height: 170px;
  overflow: hidden;
}

.card-sec-body {
  padding: 16px;
  display: flex;
  flex-direction: column;
  flex: 1;
}

.card-sec-title {
  font-size: 15px;
  font-weight: 600;
  margin: 8px 0 16px 0;
  line-height: 1.4;
}

.card-sec-footer {
  margin-top: auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.download-counter {
  font-size: 12px;
  color: #737686;
  font-weight: 500;
}

/* CTA Card */
.card-cta {
  background-color: #004ac6;
  color: #ffffff;
  padding: 32px 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

@media (min-width: 768px) {
  .card-cta {
    grid-column: span 4;
  }
}

.cta-icon-wrap {
  margin-bottom: 12px;
}

.cta-title {
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 6px 0;
}

.cta-desc {
  font-size: 13.5px;
  color: #dbe1ff;
  margin: 0 0 20px 0;
}

.btn-cta {
  background-color: #ffffff;
  color: #004ac6;
  border: none;
  border-radius: 9999px;
  padding: 10px 22px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.btn-cta:hover {
  background-color: #f2f3ff;
}
</style>
