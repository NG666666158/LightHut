<script setup>
import { useRoute } from 'vue-router'
import { legacyPage } from '@/config'

const route = useRoute()

const reminderHref = legacyPage('/reminder')
</script>

<template>
  <div class="app-shell">
    <header class="top-bar">
      <router-link to="/" class="brand">挚友树洞</router-link>
      <nav class="nav">
        <router-link to="/home">首页</router-link>
        <router-link to="/starlight">星光墙</router-link>
        <a :href="legacyPage('/cheer')" class="legacy">打气站</a>
        <a :href="reminderHref" class="legacy">温柔提醒</a>
      </nav>
    </header>
    <main class="main">
      <router-view :key="route.path" />
    </main>
  </div>
</template>

<style scoped>
.app-shell {
  min-height: 100vh;
  min-height: 100dvh;
  width: 100%;
  max-width: 100vw;
  display: flex;
  flex-direction: column;
}

.top-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 0.5rem;
  padding: 0.65rem clamp(0.75rem, 3vw, 1.25rem);
  background: #fcf9f3;
  border-bottom: 1px solid #eae9dd;
}

.brand {
  font-weight: 700;
  color: #935252;
  text-decoration: none;
}

.brand:hover {
  text-decoration: none;
  opacity: 0.9;
}

.nav {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 0.65rem 1rem;
}

.nav a {
  text-decoration: none;
  font-weight: 500;
  font-size: 0.9rem;
}

.nav a.legacy {
  color: #696458;
}

.nav a.router-link-active {
  color: #935252;
}

.main {
  flex: 1;
  min-width: 0;
  width: 100%;
  max-width: min(1200px, 100%);
  margin: 0 auto;
  padding: clamp(0.75rem, 2.5vw, 1.25rem);
}
</style>
