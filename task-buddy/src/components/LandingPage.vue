<script setup>
import { useRouter } from 'vue-router'
import NavBar from './common/NavBar.vue'
import FeatureCard from './common/FeatureCard.vue'

const router = useRouter()

const goToLogin = () => {
  router.push('/login')
}
</script>

<template>
  <div class="landing-page">
    <NavBar />
    
    <section class="hero">
      <div class="hero-content">
        <p class="version-badge">v1.0.0 Now Available</p>
        <h1 class="hero-title">Manage tasks with clarity</h1>
        <p class="hero-subtitle">
          A simple, accessible, and open-source task manager for students<br>
          and educators.
        </p>
        <div class="hero-actions">
          <button @click="goToLogin" class="btn btn-dark btn-lg">Get Started</button>
          <button class="btn btn-outline btn-lg">Learn More</button>
        </div>
      </div>
    </section>

    <section class="features" id="features">
      <h2 class="features-title">Powerful Features</h2>
      <p class="features-subtitle">Everything you need to stay organized and productive.</p>
      
      <div class="features-grid">
        <FeatureCard 
          icon="✓"
          iconBg="#f0f0f0"
          title="Task Management"
          description="Create, organize, and track your tasks with ease. Set priorities, due dates, and mark them as done."
        />
        <FeatureCard 
          icon="📊"
          iconBg="#f0f0f0"
          title="Smart Dashboard"
          description="Get a clear overview of your day with our smart dashboard. See upcoming, overdue, and today's tasks at a glance."
        />
        <FeatureCard 
          icon="⚡"
          iconBg="#fce7f3"
          title="Instant Updates"
          description="Powered by Vue 3 for lightning-fast interactivity. No page reloads or waiting times."
        />
      </div>
    </section>

    <section class="about" id="about">
      <h2 class="about-title">About Task-Buddy</h2>
      <p class="about-description">
        Task-Buddy is an open-source education project designed to demonstrate modern web<br>
        development practices with Vue 3 and Tailwind CSS. We prioritize accessibility (WCAG 2.1 AA)<br>
        and privacy, ensuring a tool that works for everyone.
      </p>
    </section>
  </div>
</template>

<style scoped>
.landing-page {
  min-height: 100vh;
  background-color: #f7fafc;
}

.hero {
  padding: 5rem 2rem 8rem;
  text-align: center;
  background-color: white;
  border-bottom: 1px solid #e5e5e5;
}

.hero-content {
  max-width: 800px;
  margin: 0 auto;
}

.version-badge {
  font-size: 0.875rem;
  color: #4a5568;
  margin-bottom: 1.5rem;
  font-weight: 500;
}

.hero-title {
  font-size: 3.5rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 1.5rem 0;
  line-height: 1.2;
}

.hero-subtitle {
  font-size: 1.125rem;
  color: #718096;
  margin: 0 0 2.5rem 0;
  line-height: 1.6;
}

.hero-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.features {
  padding: 5rem 2rem;
  text-align: center;
  background-color: #f7fafc;
}

.features-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 1rem 0;
}

.features-subtitle {
  font-size: 1.125rem;
  color: #718096;
  margin: 0 0 3rem 0;
}

.features-grid {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  text-align: left;
}

.about {
  padding: 5rem 2rem;
  text-align: center;
  background-color: white;
}

.about-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 1.5rem 0;
}

.about-description {
  font-size: 1.0625rem;
  color: #718096;
  line-height: 1.8;
  margin: 0;
  max-width: 900px;
  margin: 0 auto;
}
</style>
