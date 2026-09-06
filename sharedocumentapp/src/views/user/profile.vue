<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Apis, { authApis, endpoints } from '@/configs/apis'
import Header from '@/components/header.vue'
import Footer from '@/components/footer.vue'
import editProfile from '@/components/editProfile.vue' 
const router = useRouter()
const profile = ref({})

// Trạng thái Tab
const activeTab = ref('uploaded')

// Biến quản lý Tài liệu đã đăng (Uploaded)
const uploadedDocs = ref([])
const isUploadedLoading = ref(false)
const uploadedPage = ref(0)
const uploadedTotalPages = ref(0)

// Biến quản lý Tài liệu đã lưu (Bookmark)
const bookmarkedDocs = ref([])
const isBookmarksLoading = ref(false)
const bookmarkedPage = ref(0)
const bookmarkedTotalPages = ref(0)
const isProfileLoading = ref(false)

const isEditOpen = ref(false)

const handleProfileUpdated = (updatedProfile) => {
  profile.value = updatedProfile 
  isEditOpen.value = false 
}

// Gọi API Profile
const fetchProfile = async () => {
  isProfileLoading.value = true
  try {
    const res = await authApis.get(endpoints['profile'])
    profile.value = res.data
  } catch (error) {
    console.error('Lỗi khi tải profile:', error)
  } finally {
    isProfileLoading.value = false
  }
}

// Gọi API Tài liệu đã đăng (Có phân trang)
const fetchMyDocuments = async (page = 0) => {
  isUploadedLoading.value = true
  try {
    const res = await authApis.get(`${endpoints['myDocuments']}?page=${page}&size=10`)
    uploadedDocs.value = res.data.content // Thay thế dữ liệu cũ
    uploadedPage.value = res.data.number
    uploadedTotalPages.value = res.data.totalPages
  } catch (error) {
    console.error('Lỗi khi tải tài liệu đã đăng:', error)
  } finally {
    isUploadedLoading.value = false
  }
}

// Gọi API Bookmark (Có phân trang)
const fetchMyBookmarks = async (page = 0) => {
  isBookmarksLoading.value = true
  try {
    const res = await authApis.get(`${endpoints['collection']}?page=${page}&size=10`)
    bookmarkedDocs.value = res.data.content // Thay thế dữ liệu cũ
    bookmarkedPage.value = res.data.number
    bookmarkedTotalPages.value = res.data.totalPages
  } catch (error) {
    console.error('Lỗi khi tải bookmark:', error)
  } finally {
    isBookmarksLoading.value = false
  }
}

// Chuyển trang
const changeUploadedPage = (pageIndex) => {
  fetchMyDocuments(pageIndex)
}

const changeBookmarkedPage = (pageIndex) => {
  fetchMyBookmarks(pageIndex)
}

onMounted(() => {
  fetchProfile()
  fetchMyBookmarks()
  fetchMyDocuments()
})
</script>
<template>
  <Header />
  <main class="page-background">
    <div class="profile-cover"></div>

    <div class="profile-container container">
      <!-- Loading Profile -->
      <div v-if="isProfileLoading" class="text-center py-5">Đang tải thông tin người dùng...</div>

      <!-- Khối Profile (Giữ nguyên) -->
      <div v-else class="profile-info-card bg-white">
        <div class="profile-info-header">
          <div class="avatar-wrapper">
            <img v-if="profile.avatar" :src="profile.avatar" alt="Avatar" class="avatar-img" />
            <span v-else class="material-symbols-outlined avatar-icon">person</span>
          </div>

          <button class="btn-edit-profile" @click="isEditOpen = true">
            <svg
              width="14"
              height="14"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
            >
              <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
              <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
            </svg>
            Chỉnh sửa thông tin
          </button>
        </div>

        <div class="user-details">
          <h2 class="user-name">{{ profile.ho }} {{ profile.ten }}</h2>
          <div class="user-contact">
            <span class="contact-item">@{{ profile.username }}</span>
            <span class="dot-separator">•</span>
            <span class="contact-item">
              <svg
                width="14"
                height="14"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <path
                  d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"
                ></path>
                <polyline points="22,6 12,13 2,6"></polyline>
              </svg>
              {{ profile.email }}
            </span>
          </div>
        </div>
      </div>

      <!-- Tab & Search (Giữ nguyên) -->
      <div class="profile-tabs-wrapper">
        <div class="tabs-container">
          <button
            class="tab-btn"
            :class="{ active: activeTab === 'uploaded' }"
            @click="activeTab = 'uploaded'"
          >
            Tài liệu đã đăng
            <span class="tab-badge">{{ profile.totalUploaded || 0 }}</span>
          </button>
          <button
            class="tab-btn"
            :class="{ active: activeTab === 'bookmarked' }"
            @click="activeTab = 'bookmarked'"
          >
            Đã lưu (Bookmark)
            <span class="tab-badge">{{ profile.totalBookmarked || 0 }}</span>
          </button>
        </div>
        <div class="profile-search">
          <svg
            class="search-icon"
            width="14"
            height="14"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
          >
            <circle cx="11" cy="11" r="8"></circle>
            <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
          </svg>
          <input type="text" placeholder="Tìm trong hồ sơ..." />
        </div>
      </div>

      <!-- Nội dung Document -->
      <div class="profile-content">
        <!-- ================= TAB 1: ĐÃ ĐĂNG ================= -->
        <div v-if="activeTab === 'uploaded'">
          <div v-if="isUploadedLoading" class="text-center py-5">Đang tải...</div>
          <div v-else-if="uploadedDocs.length === 0" class="empty-state">
            Bạn chưa đăng tài liệu nào.
          </div>

          <div v-else>
            <div class="document-grid">
              <div v-for="doc in uploadedDocs" :key="doc.id" class="doc-card"  @click="router.push(`/documents/${doc.id}`)">
                <div class="doc-header">
                  <!-- Dùng Category Name thay cho File Extension -->
                  <span class="doc-format">{{ doc.categoryName || 'Chưa phân loại' }}</span>
                  <button class="btn-more">
                    <svg
                      width="16"
                      height="16"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      stroke-width="2"
                    >
                      <circle cx="12" cy="12" r="1"></circle>
                      <circle cx="19" cy="12" r="1"></circle>
                      <circle cx="5" cy="12" r="1"></circle>
                    </svg>
                  </button>
                </div>

                <h4 class="doc-title" >
                  {{ doc.title }}
                </h4>

                <!-- Bổ sung Tags và Uploader thay cho Description -->
                <div class="doc-meta-content">
                  <div class="doc-tags mt-2">
                    <span v-for="(tag, index) in doc.tagNames" :key="index" class="doc-tag">{{
                      tag
                    }}</span>
                  </div>
                </div>

                <div class="doc-footer">
                  <span class="doc-stats">
                    <!-- Icon View -->
                    <svg
                      width="14"
                      height="14"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      stroke-width="2"
                    >
                      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                      <circle cx="12" cy="12" r="3"></circle>
                    </svg>
                    {{ doc.totalView }}
                    <span class="ms-2 me-1"></span>
                    <!-- Icon Like -->
                    <svg
                      width="14"
                      height="14"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      stroke-width="2"
                    >
                      <path
                        d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"
                      ></path>
                    </svg>
                    {{ doc.totalLike }}
                  </span>
                  <!-- Dùng createdAt -->
                  <span class="doc-date">{{ doc.createdAt }}</span>
                </div>
              </div>
            </div>

            <!-- Phân trang Uploaded -->
            <div class="pagination-wrapper mt-4 mb-4" v-if="uploadedTotalPages > 1">
              <button
                v-for="pageNum in uploadedTotalPages"
                :key="pageNum"
                class="page-btn"
                :class="{ active: uploadedPage === pageNum - 1 }"
                @click="changeUploadedPage(pageNum - 1)"
              >
                {{ pageNum }}
              </button>
            </div>
          </div>
        </div>

        <!-- ================= TAB 2: BOOKMARK ================= -->
        <div v-if="activeTab === 'bookmarked'">
          <div v-if="isBookmarksLoading" class="text-center py-5">Đang tải...</div>
          <div v-else-if="bookmarkedDocs.length === 0" class="empty-state">
            Bạn chưa lưu tài liệu nào.
          </div>

          <div v-else>
            <div class="document-grid"  >
              <!-- Áp dụng cùng cấu trúc Card cho Bookmark -->
              <div v-for="doc in bookmarkedDocs" :key="doc.id" class="doc-card" @click="router.push(`/documents/${doc.id}`)">
                <div class="doc-header">
                  <span class="doc-format">{{ doc.categoryName || 'Chưa phân loại' }}</span>
                  <button class="btn-more">
                    <svg
                      width="16"
                      height="16"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      stroke-width="2"
                    >
                      <circle cx="12" cy="12" r="1"></circle>
                      <circle cx="19" cy="12" r="1"></circle>
                      <circle cx="5" cy="12" r="1"></circle>
                    </svg>
                  </button>
                </div>

                <h4 class="doc-title"">
                  {{ doc.title }}
                </h4>

                <div class="doc-meta-content">
                  <div class="doc-tags mt-2">
                    <span v-for="(tag, index) in doc.tagNames" :key="index" class="doc-tag">{{
                      tag
                    }}</span>
                  </div>
                </div>

                <div class="doc-footer">
                  <span class="doc-stats">
                    <svg
                      width="14"
                      height="14"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      stroke-width="2"
                    >
                      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                      <circle cx="12" cy="12" r="3"></circle>
                    </svg>
                    {{ doc.totalView }}
                    <span class="ms-2 me-1"></span>
                    <svg
                      width="14"
                      height="14"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      stroke-width="2"
                    >
                      <path
                        d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"
                      ></path>
                    </svg>
                    {{ doc.totalLike }}
                  </span>
                  <span class="doc-date">{{ doc.createdAt }}</span>
                </div>
              </div>
            </div>

            <!-- Phân trang Bookmark -->
            <div class="pagination-wrapper mt-4 mb-4" v-if="bookmarkedTotalPages > 1">
              <button
                v-for="pageNum in bookmarkedTotalPages"
                :key="pageNum"
                class="page-btn"
                :class="{ active: bookmarkedPage === pageNum - 1 }"
                @click="changeBookmarkedPage(pageNum - 1)"
              >
                {{ pageNum }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <editProfile
      :isOpen="isEditOpen" 
      :userProfile="profile" 
      @close="isEditOpen = false" 
      @updated="handleProfileUpdated" 
    />
  </main>
  <Footer />
</template>

<style scoped>
/* 5. PHÂN TRANG (PAGINATION) */
.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  align-items: center;
}

.page-btn {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #e2e8f0;
  background-color: white;
  color: #475569;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}
/* --- PHẦN BỔ SUNG CHO DOCUMENT CARD --- */

/* Meta Content (thay cho Description) */
.doc-meta-content {
  flex-grow: 1; /* Đẩy footer xuống đáy */
  margin-bottom: 16px;
}

/* Tên uploader */
.uploader-name {
  font-size: 13px;
  color: #64748b;
  font-weight: 500;
}

/* CSS cho Thẻ Tags */
.doc-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}
.doc-tag {
  background-color: #f1f5f9;
  color: #475569;
  font-size: 11px;
  padding: 3px 8px;
  border-radius: 4px;
  font-weight: 500;
}

/* CSS cho Lượt xem & Lượt thích ở Footer */
.doc-stats {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #64748b;
}
.doc-stats svg {
  margin-top: -1px; /* Căn giữa icon với chữ */
}

.page-btn:hover {
  border-color: #0d6efd;
  color: #0d6efd;
}

.page-btn.active {
  background-color: #0d6efd;
  color: white;
  border-color: #0d6efd;
}
/* Màu nền tổng thể */
.page-background {
  background-color: #f4f6f8;
  min-height: 100vh;
  padding-bottom: 3rem;
  font-family: 'Inter', system-ui, sans-serif;
}

/* 1. ẢNH BÌA */
.profile-cover {
  height: 160px;
  background: linear-gradient(135deg, #e0eafc 0%, #cfdef3 100%);
  width: 100%;
}

.profile-container {
  max-width: 1100px;
  margin: 0 auto;
  margin-top: -60px; /* Kéo nội dung lên đè vào ảnh bìa */
  padding: 0 15px;
}

/* 2. KHỐI THÔNG TIN USER */
.profile-info-card {
  border-radius: 12px;
  padding: 0 24px 24px 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  margin-bottom: 24px;
}

.profile-info-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

/* Avatar bo góc vuông (Squircle) */
.avatar-wrapper {
  position: relative;
  width: 130px;
  height: 130px;
  border-radius: 16px;
  background: #fff;
  padding: 4px; /* Viền trắng */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-top: -30px; /* Đẩy avatar lên */
}
.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 12px;
}
.avatar-icon {
  font-size: 80px;
  color: #ccc;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f8f9fa;
  border-radius: 12px;
}


/* Nút Chỉnh sửa thông tin */
.btn-edit-profile {
  background: #f8f9fa;
  border: 1px solid #dee2e6;
  color: #495057;
  padding: 8px 16px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  margin-bottom: 10px;
  transition: all 0.2s;
}
.btn-edit-profile:hover {
  background: #e9ecef;
}

/* Chi tiết Tên và Contact */
.user-details {
  margin-top: 16px;
}
.user-name {
  font-size: 24px;
  font-weight: 700;
  color: #1a1f36;
  margin-bottom: 8px;
}
.user-contact {
  display: flex;
  align-items: center;
  color: #6c757d;
  font-size: 14px;
  gap: 8px;
}
.contact-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #0d6efd; /* Màu xanh cho @username */
}
.contact-item:last-child {
  color: #6c757d;
}
.dot-separator {
  color: #adb5bd;
}

/* 3. KHU VỰC TAB VÀ TÌM KIẾM */
.profile-tabs-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 2px solid #e2e8f0;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 16px;
}
.tabs-container {
  display: flex;
  gap: 32px;
}
.tab-btn {
  background: transparent;
  border: none;
  padding: 12px 0;
  font-size: 15px;
  font-weight: 600;
  color: #64748b;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  border-bottom: 3px solid transparent;
  margin-bottom: -2px;
  transition: color 0.2s;
}
.tab-btn:hover {
  color: #0d6efd;
}
.tab-btn.active {
  color: #0d6efd;
  border-bottom-color: #0d6efd;
}

/* Badge số lượng */
.tab-badge {
  background: #f1f5f9;
  color: #475569;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 12px;
  margin-left: 4px;
  font-weight: 600;
}
.tab-btn.active .tab-badge {
  background: #e0f2fe;
  color: #0284c7;
}

/* Ô Search mini */
.profile-search {
  display: flex;
  align-items: center;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  padding: 6px 12px;
  width: 250px;
}
.profile-search input {
  border: none;
  outline: none;
  font-size: 13px;
  margin-left: 8px;
  width: 100%;
}
.profile-search .search-icon {
  color: #94a3b8;
}

/* 4. LƯỚI CARD TÀI LIỆU */
.document-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}

/* Thiết kế từng Card */
.doc-card {
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  transition:
    box-shadow 0.2s,
    transform 0.2s;
}
.doc-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
  transform: translateY(-2px);
}

.doc-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
.doc-format {
  background: #f8f9fa;
  color: #495057;
  font-size: 11px;
  font-weight: 600;
  padding: 4px 8px;
  border-radius: 4px;
  border: 1px solid #e9ecef;
}
.btn-more {
  background: none;
  border: none;
  color: #adb5bd;
  cursor: pointer;
  padding: 0;
}

.doc-title {
  font-size: 16px;
  font-weight: 700;
  color: #1e293b;
  line-height: 1.4;
  margin: 0 0 8px 0;
  cursor: pointer;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}


.doc-desc {
  font-size: 13px;
  color: #64748b;
  margin-bottom: 20px;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  flex-grow: 1; /* Đẩy footer xuống đáy card */
}

.doc-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #94a3b8;
  font-weight: 500;
}
.doc-downloads {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #0284c7; /* Xanh nhạt cho lượt tải */
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #94a3b8;
  background: #fff;
  border-radius: 12px;
  border: 1px dashed #cbd5e1;
}

/* Responsive cơ bản */
@media (max-width: 768px) {
  .profile-info-header {
    flex-direction: column;
    align-items: center;
    gap: 16px;
  }
  .btn-edit-profile {
    width: 100%;
    justify-content: center;
  }
  .user-details {
    text-align: center;
  }
  .user-contact {
    justify-content: center;
  }
  .profile-tabs-wrapper {
    flex-direction: column;
    align-items: flex-start;
  }
  .profile-search {
    width: 100%;
  }
}
</style>
