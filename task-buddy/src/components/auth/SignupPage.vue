<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '../common/NavBar.vue'
import { registerUser } from '../../services/api'

const router = useRouter()
const username = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const error = ref('')

const handleSignUp = async () => {
  error.value = ''
  if (password.value !== confirmPassword.value) {
    error.value = 'Passwords do not match!'
    return
  }
  try {
    await registerUser({ username: username.value, email: email.value, password: password.value })
    router.push('/login')
  } catch (e) {
    error.value = e.response?.data?.message || 'Registration failed. Please try again.'
  }
}

const goToLogin = () => router.push('/login')
</script>

<template>
  <div class="signup-page">
    <NavBar />
    
    <div class="signup-container">
      <div class="signup-card">
        <h1 class="signup-title">Create an account</h1>
        <p class="signup-subtitle">Get started with Task-Buddy today</p>
        
        <form @submit.prevent="handleSignUp" class="signup-form">
          <div class="form-group">
            <label for="username" class="form-label">Username</label>
            <input 
              id="username"
              v-model="username"
              type="text" 
              class="input form-input"
              placeholder="johndoe"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="email" class="form-label">Email address</label>
            <input 
              id="email"
              v-model="email"
              type="email" 
              class="input form-input"
              placeholder="name@example.com"
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
              placeholder="Password"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="confirmPassword" class="form-label">Confirm Password</label>
            <input 
              id="confirmPassword"
              v-model="confirmPassword"
              type="password" 
              class="input form-input"
              placeholder="Confirm Password"
              required
            />
          </div>
          
          <button type="submit" class="btn btn-dark btn-full">Sign up</button>
        </form>
        
        <p v-if="error" style="color: red; text-align: center; font-size: 0.875rem;">{{ error }}</p>
        
        <p class="signin-link">
          Already have an account? <a @click="goToLogin" class="link">Sign in</a>
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.signup-page {
  min-height: 100vh;
  background-color: #f7fafc;
}

.signup-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 4rem 2rem;
  min-height: calc(100vh - 70px);
}

.signup-card {
  background-color: white;
  border-radius: 8px;
  padding: 3rem;
  width: 100%;
  max-width: 420px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.signup-title {
  font-size: 1.875rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 0.5rem 0;
  text-align: center;
}

.signup-subtitle {
  font-size: 0.95rem;
  color: #718096;
  margin: 0 0 2rem 0;
  text-align: center;
}

.signup-form {
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

.signin-link {
  font-size: 0.875rem;
  color: #718096;
  text-align: center;
  margin: 1.5rem 0 0 0;
}

.link {
  color: #2d3748;
  font-weight: 600;
  cursor: pointer;
}

.link:hover {
  text-decoration: underline;
}
</style>
