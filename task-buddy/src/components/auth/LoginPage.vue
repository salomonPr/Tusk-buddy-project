<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '../common/NavBar.vue'
import { loginUser, setToken } from '../../services/api'

const router = useRouter()
const email = ref('')
const password = ref('')
const error = ref('')

const handleSignIn = async () => {
  error.value = ''
  try {
    const response = await loginUser({ email: email.value, password: password.value })
    const { token, ...user } = response.data
    setToken(token)
    localStorage.setItem('currentUser', JSON.stringify(user))
    router.push('/tasks')
  } catch (e) {
    error.value = e.response?.data?.message || 'Invalid email or password!'
  }
}

const goToSignup = () => router.push('/signup')
</script>

<template>
  <div class="login-page">
    <NavBar />
    
    <div class="login-container">
      <div class="login-card">
        <h1 class="login-title">Sign in to your account</h1>
        <p class="login-subtitle">Welcome back to Task-Buddy</p>
        
        <form @submit.prevent="handleSignIn" class="login-form">
          <div class="form-group">
            <label for="email" class="form-label">Email address</label>
            <input 
              id="email"
              v-model="email"
              type="email" 
              class="input form-input"
              placeholder="student@taskbuddy.edu"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="password" class="form-label">Password</label>
            <input 
              id="password"
              v-model="password"
              type="password" 
              class="input form-input"
              placeholder="•••"
              required
            />
          </div>
          
          <button type="submit" class="btn btn-dark btn-full">Sign in</button>
        </form>
        
        <p v-if="error" style="color: red; text-align: center; font-size: 0.875rem;">{{ error }}</p>
        
        <p class="signup-link">
          Don't have an account? <a @click="goToSignup" class="link">Sign up</a>
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  background-color: #f7fafc;
}

.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 4rem 2rem;
  min-height: calc(100vh - 70px);
}

.login-card {
  background-color: white;
  border-radius: 8px;
  padding: 3rem;
  width: 100%;
  max-width: 420px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.login-title {
  font-size: 1.875rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 0.5rem 0;
  text-align: center;
}

.login-subtitle {
  font-size: 0.95rem;
  color: #718096;
  margin: 0 0 2rem 0;
  text-align: center;
}

.login-form {
  margin-bottom: 1.5rem;
}

.form-group {
  margin-bottom: 1.25rem;
}

.form-label {
  display: block;
  font-size: 0.875rem;
  font-weight: 500;
  color: #2d3748;
  margin-bottom: 0.5rem;
}

.form-input {
  width: 100%;
  box-sizing: border-box;
}

.btn-full {
  width: 100%;
  margin-top: 0.5rem;
}

.demo-info {
  font-size: 0.8125rem;
  color: #a0aec0;
  text-align: center;
  margin: 1rem 0;
}

.signup-link {
  font-size: 0.875rem;
  color: #718096;
  text-align: center;
  margin: 1.5rem 0 0 0;
}

.link {
  color: #2d3748;
  font-weight: 600;
  text-decoration: none;
}

.link:hover {
  text-decoration: underline;
}
</style>
