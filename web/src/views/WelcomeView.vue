<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

function enter() {
  router.push('/home')
}
</script>

<template>
  <div class="welcome">
    <div class="bg-glow" aria-hidden="true" />
    <main class="content">
      <div class="icon-wrap">
        <span class="spark">✦</span>
      </div>
      <h1 class="title">「欢迎来到醋酸钠的专属小天地✨」</h1>
      <p class="subtitle">
        这里是属于你的温柔角落，<br />
        让心情在这里慢慢深呼吸。
      </p>
      <div class="divider" />
      <button type="button" class="cta" @click="enter">
        <span>点击进入小木屋</span>
        <span class="arrow" aria-hidden="true">→</span>
      </button>
    </main>
    <footer class="foot">
      <p class="foot-en">Your good friend Acetic_Na created for you.</p>
      <p class="foot-line">「你的好朋友醋酸钠 为你打造」</p>
    </footer>
  </div>
</template>

<style scoped>
.welcome {
  min-height: calc(100dvh - 5rem);
  min-height: calc(100vh - 5rem);
  width: 100%;
  max-width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  padding: clamp(1rem, 4vw, 2rem) clamp(0.75rem, 3vw, 1.25rem) clamp(2rem, 6vw, 3rem);
  overflow-x: hidden;
}

.bg-glow {
  position: absolute;
  inset: -20%;
  background:
    radial-gradient(circle at 30% 20%, rgba(253, 171, 171, 0.25), transparent 45%),
    radial-gradient(circle at 70% 80%, rgba(225, 247, 218, 0.35), transparent 50%);
  pointer-events: none;
  z-index: 0;
}

.content {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: min(100%, 56rem);
  padding: 0 0.25rem;
  text-align: center;
}

.icon-wrap {
  width: 6rem;
  height: 6rem;
  margin: 0 auto 2rem;
  border-radius: 999px;
  background: #eae9dd;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 24px rgba(147, 82, 82, 0.12);
}

.spark {
  font-size: 2.25rem;
  color: #935252;
  line-height: 1;
}

.title {
  margin: 0;
  font-weight: 700;
  color: #935252;
  letter-spacing: 0.02em;
  line-height: 1.12;
  white-space: nowrap;
  max-width: 100%;
  /* 与 Thymeleaf 欢迎页一致：整句单行，随视口/缩放变化 */
  font-size: clamp(0.65rem, 2.65vw + 0.35rem, 3.65rem);
}

.subtitle {
  margin: 1.25rem 0 0;
  font-size: 1.1rem;
  color: #64655c;
  line-height: 1.7;
}

.divider {
  width: 5rem;
  height: 1px;
  margin: 2rem auto;
  background: rgba(129, 129, 120, 0.25);
}

.cta {
  display: inline-flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 2.25rem;
  border: none;
  border-radius: 999px;
  cursor: pointer;
  font-size: 1.05rem;
  font-weight: 600;
  font-family: inherit;
  color: #ffffff;
  background: linear-gradient(120deg, #935252, #ee9e9e);
  box-shadow: 0 8px 28px rgba(147, 82, 82, 0.35);
  transition: transform 0.15s ease, box-shadow 0.2s ease;
}

.cta:hover {
  box-shadow: 0 12px 32px rgba(147, 82, 82, 0.42);
}

.cta:active {
  transform: scale(0.98);
}

.arrow {
  font-size: 1.25rem;
  transition: transform 0.2s ease;
}

.cta:hover .arrow {
  transform: translateX(4px);
}

.foot {
  position: relative;
  z-index: 1;
  margin-top: 3rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.65rem;
  text-align: center;
}

.foot-en {
  margin: 0;
  font-size: 0.75rem;
  font-style: italic;
  letter-spacing: 0.2em;
  text-transform: uppercase;
  color: #a8a29e;
}

.foot-line {
  margin: 0;
  font-size: 0.95rem;
  font-weight: 500;
  color: #566953;
  opacity: 0.85;
}
</style>
