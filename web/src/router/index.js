import { createRouter, createWebHistory } from 'vue-router'

const WelcomeView = () => import('@/views/WelcomeView.vue')
const StarlightView = () => import('@/views/StarlightView.vue')
const HomeView = () => import('@/views/HomeView.vue')

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'welcome',
      component: WelcomeView,
      meta: { title: '欢迎' },
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView,
      meta: { title: '首页' },
    },
    { path: '/starlight', name: 'starlight', component: StarlightView, meta: { title: '星光墙' } },
  ],
})

router.beforeEach((to) => {
  document.title = to.meta.title ? `${to.meta.title} · 挚友树洞` : '挚友树洞 · Web'
})

export default router
