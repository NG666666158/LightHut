import { defineStore } from 'pinia'

export const useHomeStore = defineStore('home', {
  state: () => ({
    overview: null,
    loadError: null,
    signInBusy: false,
    moodBusy: false,
    signInMessage: '',
  }),

  getters: {
    dm: (s) => s.overview?.dailyMeditation ?? null,
    streakDays: (s) => s.overview?.meditationStreakDays ?? 0,
    growthProgress: (s) => s.overview?.growthProgress ?? 0,
    treeStage: (s) => s.overview?.treeStage ?? 1,
    signedToday: (s) => !!s.overview?.signedToday,
  },

  actions: {
    async fetchOverview() {
      this.loadError = null
      const res = await fetch('/api/home/overview', { credentials: 'include' })
      if (!res.ok) {
        this.loadError = '加载首页数据失败'
        this.overview = null
        return
      }
      this.overview = await res.json()
    },

    applySignInPayload(data) {
      if (!this.overview) return
      if (typeof data.signedToday === 'boolean') {
        this.overview.signedToday = data.signedToday
      }
      if (typeof data.streakDays === 'number') {
        this.overview.meditationStreakDays = data.streakDays
      }
      if (typeof data.growthProgress === 'number') {
        this.overview.growthProgress = data.growthProgress
      }
      if (typeof data.treeStage === 'number') {
        this.overview.treeStage = data.treeStage
      }
    },

    async signIn() {
      this.signInBusy = true
      this.signInMessage = ''
      try {
        const res = await fetch('/api/home/sign-in', {
          method: 'POST',
          credentials: 'include',
        })
        const data = await res.json().catch(() => ({}))
        if (res.ok) {
          this.applySignInPayload(data)
          this.signInMessage = data.message || ''
        } else {
          this.signInMessage = data.error || '签到失败'
        }
        return data
      } finally {
        this.signInBusy = false
      }
    },

    async saveMood(content, moodTag) {
      this.moodBusy = true
      try {
        const body = { content }
        if (moodTag) body.mood = moodTag
        const res = await fetch('/api/home/mood', {
          method: 'POST',
          credentials: 'include',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(body),
        })
        const j = await res.json().catch(() => ({}))
        if (!res.ok) {
          throw new Error(j.error || '保存失败')
        }
        return true
      } finally {
        this.moodBusy = false
      }
    },

    reset() {
      this.overview = null
      this.loadError = null
      this.signInMessage = ''
    },
  },
})
