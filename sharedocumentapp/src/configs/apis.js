import axios from 'axios'
import cookie from 'vue-cookies'
export const baseURL = 'http://localhost:8080/api'

export const endpoints = {
  login: '/login',
  register: '/register',
  profile: '/profile',
  documents: '/documents',
  documentDetail: (id) => `/documents/${id}`,
}

const Apis = axios.create({
  baseURL: baseURL,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const authApis = () => {
  return axios.create({
    baseURL: baseURL,
    headers: {
      Authorization: `Bearer ${cookie.get('token')}`,
    },
  })
}

export default Apis
