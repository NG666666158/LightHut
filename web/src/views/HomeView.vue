<script setup>
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { storeToRefs } from 'pinia'
import { useHomeStore } from '@/stores/home'
import { legacyPage } from '@/config'
import { getHomeTimeGreeting, slotForHour } from '@/utils/homeGreetings'

const home = useHomeStore()
const { overview, loadError } = storeToRefs(home)

const moodOpen = ref(false)
const moodContent = ref('')
const moodTag = ref('')
const moodErr = ref('')

const cheerHref = legacyPage('/cheer')

const treeStemHeight = computed(() => {
  const stage = home.treeStage
  const heights = { 1: 34, 2: 52, 3: 74, 4: 102, 5: 128 }
  return `${heights[stage] || 34}px`
})

/** 与 Thymeleaf 一致：第 n 片叶子在 stage >= n 时显示 */
function leafVisible(n) {
  return home.treeStage >= n
}

/** 每分钟检查一次是否跨时段，便于长时间停留时问候与本地时间一致 */
const timeRefresh = ref(0)
let greetingSlotTimer

const timeGreeting = computed(() => {
  timeRefresh.value
  return getHomeTimeGreeting()
})

onMounted(() => {
  home.fetchOverview()
  let last = slotForHour(new Date().getHours())
  greetingSlotTimer = window.setInterval(() => {
    const slot = slotForHour(new Date().getHours())
    if (slot !== last) {
      last = slot
      timeRefresh.value++
    }
  }, 60000)
})

onUnmounted(() => {
  if (greetingSlotTimer) window.clearInterval(greetingSlotTimer)
})

function openMood() {
  moodErr.value = ''
  moodContent.value = ''
  moodTag.value = ''
  moodOpen.value = true
}

function closeMood() {
  moodOpen.value = false
}

async function submitMood() {
  moodErr.value = ''
  const c = moodContent.value.trim()
  if (!c) {
    moodErr.value = '请填写内容'
    return
  }
  try {
    await home.saveMood(c, moodTag.value.trim() || undefined)
    closeMood()
  } catch (e) {
    moodErr.value = e.message || '保存失败'
  }
}

async function onSignIn() {
  await home.signIn()
}

function onCtaClick() {
  const href = home.dm?.ctaHref
  if (!href || href === '#') return
  if (href.startsWith('http://') || href.startsWith('https://')) {
    window.open(href, '_blank', 'noopener,noreferrer')
  } else {
    window.location.href = href
  }
}
</script>

<template>
  <div class="home">
    <p v-if="loadError" class="banner-err">{{ loadError }}</p>

    <template v-else-if="overview">
      <header class="hero">
        <h1>{{ timeGreeting.headline }}</h1>
        <p class="hero-sub">{{ timeGreeting.sub }}</p>
        <p v-if="overview.nickname" class="nick">你好，{{ overview.nickname }} · {{ overview.onlineDuration }}</p>
      </header>

      <div class="grid">
        <section class="dm-card">
          <div class="dm-bg">
            <img v-if="dm?.backgroundImageUrl" :src="dm.backgroundImageUrl" alt="" />
          </div>
          <div class="dm-overlay" />
          <div class="dm-content">
            <span v-if="dm?.tag" class="dm-tag">{{ dm.tag }}</span>
            <h2>{{ dm?.title }}</h2>
            <p class="dm-desc">{{ dm?.subtitle }}</p>
            <button type="button" class="dm-cta" @click="onCtaClick">{{ dm?.ctaLabel || '开启旅程' }}</button>
          </div>
        </section>

        <aside class="side">
          <div class="card forest">
            <div class="icon eco">🌿</div>
            <h3>你的成长森林</h3>
            <p class="muted">
              你已经连续 <strong>{{ home.streakDays }}</strong> 天通过冥想照看你的心灵花园了。
            </p>
            <div class="sign-row">
              <button
                type="button"
                class="btn-sign"
                :disabled="home.signedToday || home.signInBusy"
                @click="onSignIn"
              >
                {{ home.signedToday ? '今日已签到' : home.signInBusy ? '…' : '今日签到' }}
              </button>
              <span class="hint">{{ home.signedToday ? '今天的能量已注入小树。' : '每天签到，小树就会长大。' }}</span>
            </div>
            <p v-if="home.signInMessage" class="msg">{{ home.signInMessage }}</p>

            <div class="tree-wrap">
              <div class="tree">
                <div class="soil" />
                <div class="stem" :style="{ height: treeStemHeight }" />
                <div class="leaf l1" :class="{ on: leafVisible(1) }" />
                <div class="leaf l2" :class="{ on: leafVisible(2) }" />
                <div class="leaf l3" :class="{ on: leafVisible(3) }" />
                <div class="leaf l4" :class="{ on: leafVisible(4) }" />
                <div class="leaf l5" :class="{ on: leafVisible(5) }" />
              </div>
              <p class="tree-cap">成长森林 · 小树苗会陪你一起长大</p>
            </div>

            <div class="bar">
              <div class="bar-fill" :style="{ width: `${home.growthProgress}%` }" />
            </div>
          </div>

          <div class="card cheer">
            <div class="icon spark">✨</div>
            <h3>挚友的鼓励</h3>
            <p class="quote">
              “{{ overview.encouragementText }}” —— {{ overview.encouragementSignature }}
            </p>
            <a class="cheer-link" :href="cheerHref">去打气站看看 →</a>
          </div>
        </aside>
      </div>
    </template>

    <p v-else-if="!loadError" class="muted">加载中…</p>

    <button type="button" class="fab" title="记录此刻心情" @click="openMood">+</button>

    <Teleport to="body">
      <div v-if="moodOpen" class="modal-root" @click.self="closeMood">
        <div class="modal">
          <h3>记录此刻心情</h3>
          <textarea v-model="moodContent" rows="5" maxlength="500" placeholder="写下此刻的感受…" />
          <input v-model="moodTag" type="text" maxlength="32" placeholder="心情标签（可选）" />
          <p v-if="moodErr" class="err">{{ moodErr }}</p>
          <div class="modal-actions">
            <button type="button" class="btn-ghost" @click="closeMood">取消</button>
            <button type="button" class="btn-primary" :disabled="home.moodBusy" @click="submitMood">
              {{ home.moodBusy ? '保存中…' : '保存' }}
            </button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<style scoped>
.home {
  position: relative;
  padding-bottom: 4rem;
}

.banner-err {
  background: #fde8e4;
  color: #b23d21;
  padding: 0.75rem 1rem;
  border-radius: 0.5rem;
}

.hero h1 {
  margin: 0 0 0.5rem;
  font-size: clamp(1.75rem, 4vw, 2.5rem);
  color: #373831;
}

.hero-sub {
  margin: 0;
  color: #64655c;
  font-style: italic;
}

.nick {
  margin: 0.75rem 0 0;
  font-size: 0.9rem;
  color: #818178;
}

.grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1.5rem;
  margin-top: 1.5rem;
}

@media (min-width: 960px) {
  .grid {
    grid-template-columns: 1.2fr 0.85fr;
    align-items: start;
  }
}

.dm-card {
  position: relative;
  border-radius: 0.75rem;
  overflow: hidden;
  min-height: min(320px, 70vh);
  box-shadow: 0 4px 24px rgba(55, 56, 49, 0.08);
}

.dm-bg {
  position: absolute;
  inset: 0;
}

.dm-bg img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0.45;
}

.dm-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, #fffcf7 0%, transparent 55%);
}

.dm-content {
  position: relative;
  z-index: 2;
  padding: clamp(1rem, 3vw, 1.75rem);
  max-width: min(28rem, 100%);
  margin-top: auto;
  min-height: min(280px, 50vh);
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
}

.dm-tag {
  display: inline-block;
  font-size: 0.75rem;
  font-weight: 700;
  color: #935252;
  background: rgba(147, 82, 82, 0.12);
  padding: 0.2rem 0.65rem;
  border-radius: 999px;
  margin-bottom: 0.75rem;
}

.dm-content h2 {
  margin: 0 0 0.5rem;
  font-size: 1.35rem;
  color: #373831;
}

.dm-desc {
  margin: 0 0 1rem;
  color: #64655c;
  font-size: 0.95rem;
}

.dm-cta {
  align-self: flex-start;
  border: none;
  padding: 0.65rem 1.25rem;
  border-radius: 999px;
  background: linear-gradient(135deg, #935252, #fdabab);
  color: #fff;
  font-weight: 700;
  cursor: pointer;
}

.side {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.card {
  background: #fcf9f3;
  border: 1px solid #eae9dd;
  border-radius: 0.75rem;
  padding: 1.25rem;
}

.card.forest {
  min-height: 280px;
}

.icon {
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 0.75rem;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.25rem;
  margin-bottom: 0.75rem;
}

.icon.eco {
  background: #e1f7da;
}

.icon.spark {
  background: #fdabab;
}

.card h3 {
  margin: 0 0 0.35rem;
  font-size: 1.1rem;
  color: #373831;
}

.muted {
  margin: 0;
  font-size: 0.875rem;
  color: #64655c;
}

.sign-row {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 0.5rem 0.75rem;
  margin-top: 0.75rem;
}

.btn-sign {
  border: none;
  padding: 0.4rem 0.85rem;
  border-radius: 999px;
  background: #566953;
  color: #fff;
  font-size: 0.8rem;
  font-weight: 600;
  cursor: pointer;
}

.btn-sign:disabled {
  opacity: 0.55;
  cursor: not-allowed;
}

.hint {
  font-size: 0.75rem;
  color: #818178;
}

.msg {
  font-size: 0.8rem;
  color: #566953;
  margin: 0.5rem 0 0;
}

.tree-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 1rem 0 0.5rem;
}

.tree {
  position: relative;
  width: 200px;
  height: 200px;
}

.soil {
  position: absolute;
  bottom: 18px;
  left: 50%;
  transform: translateX(-50%);
  width: 120px;
  height: 22px;
  background: #e8dcc7;
  border-radius: 999px;
}

.stem {
  position: absolute;
  bottom: 38px;
  left: 50%;
  transform: translateX(-50%);
  width: 14px;
  border-radius: 999px;
  background: linear-gradient(180deg, #8b5b3f, #6f442f);
  transition: height 0.4s ease;
  animation: sway 3.6s ease-in-out infinite;
  transform-origin: bottom center;
}

.leaf {
  position: absolute;
  background: #79b857;
  border-radius: 999px;
  opacity: 0;
  transition: opacity 0.35s ease;
  animation: breathe 2.8s ease-in-out infinite;
}

.leaf.on {
  opacity: 1;
}

.l1 {
  width: 22px;
  height: 12px;
  left: calc(50% - 24px);
  bottom: 78px;
  transform: rotate(-28deg);
}
.l2 {
  width: 22px;
  height: 12px;
  left: calc(50% + 2px);
  bottom: 82px;
  transform: rotate(26deg);
}
.l3 {
  width: 26px;
  height: 15px;
  left: calc(50% - 36px);
  bottom: 102px;
  transform: rotate(-34deg);
}
.l4 {
  width: 26px;
  height: 15px;
  left: calc(50% + 8px);
  bottom: 106px;
  transform: rotate(32deg);
}
.l5 {
  width: 32px;
  height: 20px;
  left: calc(50% - 16px);
  bottom: 124px;
  transform: rotate(-4deg);
}

.tree-cap {
  margin: 0.25rem 0 0;
  font-size: 0.7rem;
  color: #566953;
  text-align: center;
}

.bar {
  height: 6px;
  background: #eae9dd;
  border-radius: 999px;
  overflow: hidden;
  margin-top: 0.5rem;
}

.bar-fill {
  height: 100%;
  background: #566953;
  border-radius: 999px;
  transition: width 0.35s ease;
}

.quote {
  margin: 0.5rem 0 0;
  font-size: 0.875rem;
  color: #64655c;
}

.cheer-link {
  display: inline-block;
  margin-top: 0.85rem;
  font-size: 0.85rem;
  font-weight: 700;
  color: #935252;
  text-decoration: none;
}

.cheer-link:hover {
  text-decoration: underline;
}

.fab {
  position: fixed;
  bottom: 1.5rem;
  right: 1.5rem;
  width: 3.5rem;
  height: 3.5rem;
  border-radius: 50%;
  border: none;
  background: linear-gradient(135deg, #935252, #fdabab);
  color: #fff;
  font-size: 1.75rem;
  line-height: 1;
  cursor: pointer;
  box-shadow: 0 8px 24px rgba(147, 82, 82, 0.35);
  z-index: 40;
}

.modal-root {
  position: fixed;
  inset: 0;
  z-index: 200;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1rem;
}

.modal {
  width: 100%;
  max-width: 420px;
  background: #fff;
  border-radius: 1rem;
  padding: 1.25rem;
  border: 1px solid #eae9dd;
}

.modal h3 {
  margin: 0 0 0.75rem;
  font-size: 1.1rem;
}

.modal textarea,
.modal input {
  width: 100%;
  border: 1px solid #babaaf;
  border-radius: 0.5rem;
  padding: 0.5rem 0.65rem;
  font-size: 0.9rem;
}

.modal textarea {
  resize: none;
}

.modal input {
  margin-top: 0.5rem;
}

.err {
  color: #b23d21;
  font-size: 0.8rem;
  margin: 0.35rem 0 0;
}

.modal-actions {
  display: flex;
  gap: 0.5rem;
  margin-top: 1rem;
}

.btn-ghost {
  flex: 1;
  padding: 0.55rem;
  border-radius: 999px;
  border: 1px solid #babaaf;
  background: #fff;
  cursor: pointer;
}

.btn-primary {
  flex: 1;
  padding: 0.55rem;
  border-radius: 999px;
  border: none;
  background: linear-gradient(135deg, #935252, #fdabab);
  color: #fff;
  font-weight: 600;
  cursor: pointer;
}

.btn-primary:disabled {
  opacity: 0.6;
}

@keyframes sway {
  0%,
  100% {
    transform: translateX(-50%) rotate(-2deg);
  }
  50% {
    transform: translateX(-50%) rotate(2deg);
  }
}

@keyframes breathe {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}
</style>
