import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
})

// Attach JWT Bearer token to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`
  }
  return config
})

export const setToken = (token) => localStorage.setItem('token', token)
export const clearToken = () => localStorage.removeItem('token')

// --- Auth ---
export const registerUser = (data) => api.post('/users/register', data)
export const loginUser = (data) => api.post('/users/login', data)

// --- Tasks ---
export const getTasks = () => api.get('/tasks')
export const createTask = (task) => api.post('/tasks', task)
export const updateTask = (id, task) => api.put(`/tasks/${id}`, task)
export const deleteTask = (id) => api.delete(`/tasks/${id}`)
