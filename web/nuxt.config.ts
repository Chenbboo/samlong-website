export default defineNuxtConfig({
  compatibilityDate: '2026-07-01',
  devtools: { enabled: false },
  css: ['@fontsource-variable/inter', '~/assets/css/main.css', '~/assets/css/font.css'],
  runtimeConfig: { apiServer: process.env.NUXT_API_SERVER || 'http://127.0.0.1:8080/api', public: { apiBase: process.env.NUXT_PUBLIC_API_BASE || '/api' } },
  nitro: { devProxy: { '/api': { target: 'http://127.0.0.1:8080/api', changeOrigin: true }, '/uploads': { target: 'http://127.0.0.1:8080/uploads', changeOrigin: true } } },
  app: {
    head: {
      htmlAttrs: { lang: 'vi' },
      title: 'Sam.Loong | Tương lai di chuyển',
      meta: [
        { name: 'description', content: 'Xe máy điện Sam.Loong — công nghệ mới cho hành trình xanh tại Việt Nam.' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' }
      ]
    }
  }
})
