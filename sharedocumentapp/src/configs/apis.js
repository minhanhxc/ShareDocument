import axios from 'axios'
import cookie from 'vue-cookies'
export const baseURL = 'http://localhost:8080/api'

export const endpoints = {
  login: '/login',
  register: '/register',
  profile: '/profile',
  documents: '/documents',
  documentDetail: (id) => `/documents/${id}`,
  like: (id) => `/documents/${id}/like`,
  upload: `/documents/upload`,
  categories: '/categories',
  tags: '/tags',
  collection: '/collection',
  addOrRemoveDocument: (documentId) => `/collection/${documentId}`,
}

const Apis = axios.create({
  baseURL: baseURL,
  headers: {
    'Content-Type': 'application/json',
  },
})
export const authApis = axios.create({ baseURL })
authApis.interceptors.request.use((config) => {
  const token = cookie.get('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default Apis
