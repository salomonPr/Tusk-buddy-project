<script setup>
import { useRouter } from 'vue-router'

defineProps({
  showAuthButtons: {
    type: Boolean,
    default: true
  }
})

const router = useRouter()

const goToHome = () => {
  router.push('/')
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<template>
  <header class="navbar">
    <div class="nav-container">
      <div class="nav-brand" @click="goToHome" style="cursor: pointer;">
        <div class="logo">T</div>
        <span class="brand-name">Task-Buddy</span>
      </div>
      <nav class="nav-links" v-if="showAuthButtons">
        <a href="#features">Features</a>
        <a href="#about">About</a>
        <button @click="goToLogin" class="btn btn-dark">Login</button>
      </nav>
    </div>
  </header>
</template>

<style scoped>
.navbar {
  background-color: white;
  border-bottom: 1px solid #e5e5e5;
  padding: 0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-container {
  width: 100%;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-sizing: border-box;
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.logo {
  width: 32px;
  height: 32px;
  background-color: #2d3748;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 6px;
  font-weight: bold;
  font-size: 18px;
}

.brand-name {
  font-size: 1.25rem;
  font-weight: 600;
  color: #2d3748;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 2rem;
}

.nav-links a {
  color: #4a5568;
  text-decoration: none;
  font-size: 0.95rem;
  transition: color 0.2s;
}

.nav-links a:hover {
  color: #2d3748;
}
</style>
