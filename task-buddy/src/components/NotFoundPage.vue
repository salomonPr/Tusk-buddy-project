<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
</script>

<template>
  <div class="not-found">
    <p class="status">404</p>
    <h1 class="title">Resource Not Found</h1>
    <p class="message">The page <code>{{ $route.path }}</code> does not exist.</p>
    <button class="btn btn-dark" @click="router.push('/')">Go Home</button>
  </div>
</template>

<style scoped>
.not-found {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  gap: 1rem;
  text-align: center;
  padding: 2rem;
}

.status {
  font-size: 6rem;
  font-weight: 800;
  color: #e2e8f0;
  margin: 0;
  line-height: 1;
}

.title {
  font-size: 1.875rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0;
}

.message {
  color: #718096;
  font-size: 0.95rem;
  margin: 0;
}

.message code {
  background: #edf2f7;
  padding: 0.1rem 0.4rem;
  border-radius: 4px;
  font-size: 0.9rem;
  color: #e53e3e;
}
</style>
