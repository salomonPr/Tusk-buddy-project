<script setup>
defineProps({
  icon: String,
  title: String,
  description: String,
  iconBg: String
})
</script>

<template>
  <div class="feature-card">
    <div class="icon-wrapper" :style="{ backgroundColor: iconBg }">
      <span class="icon">{{ icon }}</span>
    </div>
    <h3 class="card-title">{{ title }}</h3>
    <p class="card-description">{{ description }}</p>
  </div>
</template>

<style scoped>
.feature-card {
  background-color: white;
  border: 1px solid #e5e5e5;
  border-radius: 8px;
  padding: 2rem;
  text-align: left;
  transition: box-shadow 0.2s;
}

.feature-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.icon-wrapper {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1.25rem;
}

.icon {
  font-size: 24px;
}

.card-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1a202c;
  margin: 0 0 0.75rem 0;
}

.card-description {
  font-size: 0.95rem;
  color: #718096;
  line-height: 1.6;
  margin: 0;
}
</style>
