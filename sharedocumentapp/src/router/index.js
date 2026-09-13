import { createRouter, createWebHistory } from 'vue-router'
import login from '../views/user/login.vue'
import register from '../views/user/register.vue'
import home from '../views/home.vue'
import documentDetail from '../views/document/detail.vue'
import upload from '../views/document/upload.vue'
import profile from '../views/user/profile.vue'
import editDocuemnt from '../views/document/edit.vue'
import searchResult from '../views/document/searchResult.vue'
import { useAuthStore } from '@/stores/auth'
import Swal from 'sweetalert2'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: login,
    },
    {
      path: '/register',
      name: 'register',
      component: register,
    },
    {
      path: '/profile',
      name: 'profile',
      component: profile,
      meta: { requiresAuth: true },
    },
    {
      path: '/',
      name: 'home',
      component: home,
    },
    {
      path: '/documents/:id',
      name: 'documentDetail',
      component: documentDetail,
      meta: { requiresAuth: true },
    },
    {
      path: '/documents/upload',
      name: 'upload',
      component: upload,
      meta: { requiresAuth: true },
    },
    {
      path: '/documents/search',
      name: 'searchResult',
      component: searchResult,
    },
    {
      path: '/documents/edit/:id',
      name: 'editDocument',
      component: editDocuemnt,
      meta: { requiresAuth: true },
    },
  ],
})
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()
  const requiresAuth = to.matched.some((record) => record.meta.requiresAuth)

  if (requiresAuth && !authStore.isAuthenticated) {
    await Swal.fire({
      icon: 'warning',
      title: 'Bạn cần đăng nhập để truy cập trang này!',
      showConfirmButton: true,
      confirmButtonText: 'OK',
    })

    next({
      name: 'login',
      query: { redirect: to.path },
    })
  } else {
    next()
  }
})
export default router
