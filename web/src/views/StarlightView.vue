<script setup>
import { computed, onMounted, ref, watch } from 'vue'

const category = ref('ALL')
const sort = ref('createdAt')
const order = ref('desc')
const page = ref(0)
const pageSize = 9

const loading = ref(false)
const err = ref('')
const items = ref([])
const hasMore = ref(false)
const total = ref(0)

const categories = [
  { code: 'ALL', label: '全部' },
  { code: 'TRAVEL', label: '旅行' },
  { code: 'DAILY', label: '日常' },
  { code: 'BIRTHDAY', label: '庆生' },
  { code: 'SPORT', label: '运动' },
]

const sortLabel = computed(() => {
  if (sort.value === 'eventDate' && order.value === 'desc') return '拍摄·近→远'
  if (sort.value === 'eventDate' && order.value === 'asc') return '拍摄·远→近'
  if (sort.value === 'createdAt' && order.value === 'desc') return '最近添加'
  return '最早添加'
})

async function load(reset) {
  loading.value = true
  err.value = ''
  if (reset) {
    page.value = 0
    items.value = []
  }
  const params = new URLSearchParams()
  if (category.value && category.value !== 'ALL') {
    params.set('category', category.value)
  }
  params.set('sort', sort.value)
  params.set('order', order.value)
  params.set('page', String(page.value))
  params.set('size', String(pageSize))
  try {
    const res = await fetch(`/api/starlight/memories?${params}`, { credentials: 'include' })
    if (!res.ok) {
      const j = await res.json().catch(() => ({}))
      throw new Error(j.error || `加载失败 (${res.status})`)
    }
    const data = await res.json()
    const chunk = data.items ?? []
    items.value = reset ? chunk : [...items.value, ...chunk]
    hasMore.value = !!data.hasMore
    total.value = data.total ?? items.value.length
  } catch (e) {
    err.value = e.message || '加载失败'
  } finally {
    loading.value = false
  }
}

function loadMore() {
  if (!hasMore.value || loading.value) return
  page.value += 1
  load(false)
}

function toggleSort() {
  const modes = [
    ['createdAt', 'desc'],
    ['createdAt', 'asc'],
    ['eventDate', 'desc'],
    ['eventDate', 'asc'],
  ]
  const i = modes.findIndex(
    ([s, o]) => s === sort.value && o === order.value
  )
  const next = modes[(i + 1) % modes.length]
  sort.value = next[0]
  order.value = next[1]
  load(true)
}

function formatDate(iso) {
  if (!iso) return ''
  const m = String(iso).match(/^(\d{4})-(\d{2})-(\d{2})/)
  if (!m) return iso
  return `${m[1]}年${Number(m[2])}月${Number(m[3])}日`
}

watch(category, () => load(true))

onMounted(() => load(true))
</script>

<template>
  <div class="starlight">
    <header class="hero">
      <div>
        <h1>友谊星光墙</h1>
        <p class="sub">浏览回忆瞬间；完整上传与管理请使用站点内的星光墙页面。</p>
      </div>
    </header>

    <div class="toolbar">
      <div class="tabs">
        <button
          v-for="c in categories"
          :key="c.code"
          type="button"
          class="tab"
          :class="{ active: category === c.code }"
          @click="category = c.code"
        >
          {{ c.label }}
        </button>
      </div>
      <button type="button" class="sort-btn" @click="toggleSort">
        排序：{{ sortLabel }}
      </button>
    </div>

    <p v-if="err" class="err">{{ err }}</p>
    <p v-else-if="loading && items.length === 0" class="muted">加载中…</p>

    <ul class="grid">
      <li v-for="item in items" :key="item.id" class="card">
        <div class="thumb">
          <img :src="item.imageUrl" :alt="item.title" loading="lazy" />
        </div>
        <div class="meta">
          <span class="badge">{{ item.categoryLabel || item.category }}回忆</span>
          <h2>{{ item.title }}</h2>
          <p class="date">{{ formatDate(item.eventDate) }}</p>
          <p v-if="item.description" class="desc">{{ item.description }}</p>
        </div>
      </li>
    </ul>

    <div v-if="items.length && hasMore" class="more-wrap">
      <button type="button" class="more" :disabled="loading" @click="loadMore">
        {{ loading ? '加载中…' : '加载更多' }}
      </button>
    </div>

    <p v-if="!loading && !items.length && !err" class="muted">暂无回忆</p>
    <p v-if="total" class="muted small">共 {{ total }} 条</p>
  </div>
</template>

<style scoped>
.starlight {
  padding-bottom: 2rem;
}

.hero {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-start;
  justify-content: space-between;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.hero h1 {
  margin: 0 0 0.35rem;
  font-size: 1.75rem;
  color: #373831;
}

.sub {
  margin: 0;
  color: #64655c;
  font-size: 0.9rem;
  max-width: 36rem;
}

.toolbar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  margin-bottom: 1.25rem;
}

.tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.tab {
  border: none;
  padding: 0.4rem 0.85rem;
  border-radius: 999px;
  background: #f0eee5;
  color: #64655c;
  cursor: pointer;
  font-size: 0.875rem;
}

.tab.active {
  background: #935252;
  color: #fff;
}

.sort-btn {
  border: 1px solid #babaaf;
  background: #fff;
  padding: 0.4rem 0.85rem;
  border-radius: 999px;
  cursor: pointer;
  font-size: 0.875rem;
  color: #935252;
}

.grid {
  list-style: none;
  margin: 0;
  padding: 0;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(min(100%, 220px), 1fr));
  gap: clamp(0.75rem, 2vw, 1.25rem);
}

.card {
  background: #fff;
  border-radius: 0.75rem;
  overflow: hidden;
  border: 1px solid #eae9dd;
  box-shadow: 0 2px 12px rgba(55, 56, 49, 0.06);
}

.thumb {
  aspect-ratio: 4 / 3;
  background: #eae9dd;
}

.thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.meta {
  padding: 0.85rem 1rem 1rem;
}

.badge {
  font-size: 0.7rem;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: #935252;
  font-weight: 600;
}

.meta h2 {
  margin: 0.35rem 0 0.25rem;
  font-size: 1rem;
  line-height: 1.35;
}

.date {
  margin: 0;
  font-size: 0.8rem;
  color: #818178;
}

.desc {
  margin: 0.5rem 0 0;
  font-size: 0.85rem;
  color: #64655c;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.err {
  color: #b23d21;
}

.muted {
  color: #818178;
}

.muted.small {
  font-size: 0.8rem;
  margin-top: 1rem;
}

.more-wrap {
  display: flex;
  justify-content: center;
  margin-top: 1.5rem;
}

.more {
  padding: 0.5rem 1.5rem;
  border-radius: 999px;
  border: 2px solid #935252;
  background: transparent;
  color: #935252;
  font-weight: 600;
  cursor: pointer;
}

.more:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
