import { createRouter, createWebHistory } from 'vue-router'
import login from '../views/user/login.vue'
import register from '../views/user/register.vue'
import home from '../views/home.vue'
import documentDetail from '../views/document/detail.vue'
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
    // {
    //   path: '/profile',
    //   name: 'profile',
    //   component: () => import('../views/ProfileView.vue'),
    // },
    {
      path: '/',
      name: 'home',
      component: home,
    },
    {
      path: '/documents/:id',
      name: 'documentDetail',
      component: documentDetail,
    },
  ],
})

export default router
