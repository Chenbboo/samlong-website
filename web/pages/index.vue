<script setup lang="ts">
import { vietnamProvinces } from '../data/vietnam'

type Product = { id: number; title: string; content?: string; price?: number; imageUrl: string; badge?: string; rangeText?: string; powerText?: string; speedText?: string; featured?: boolean }
type News = { id: number; title: string; content?: string; imageUrl: string; category?: string; publishedAt?: string }
type Slide = { id: number; title: string; subtitle?: string; eyebrow?: string; imageUrl: string; linkUrl?: string }

const config = useRuntimeConfig()
const contentApi = import.meta.server ? config.apiServer : config.public.apiBase
const [{ data: slides }, { data: products }, { data: news }] = await Promise.all([
  useFetch<Slide[]>(`${contentApi}/slides`, { key: 'home-slides', default: () => [] }),
  useFetch<Product[]>(`${contentApi}/products`, { key: 'home-products', default: () => [] }),
  useFetch<News[]>(`${contentApi}/news`, { key: 'home-news', default: () => [] })
])

const booking = reactive({ name: '', phone: '', city: '', district: '' })
const sent = ref(false)
const sending = ref(false)
const bookingError = ref('')
const submitBooking = async () => {
  sending.value = true
  sent.value = false
  bookingError.value = ''
  try {
    const csrf = await $fetch<{ token: string }>(`${config.public.apiBase}/auth/csrf`, { credentials: 'include' })
    await $fetch(`${config.public.apiBase}/bookings`, { method: 'POST', body: booking, credentials: 'include', headers: { 'X-XSRF-TOKEN': csrf.token } })
    sent.value = true
    Object.assign(booking, { name: '', phone: '', city: '', district: '' })
  } catch (error: any) {
    bookingError.value = error?.statusCode === 409
      ? 'Số điện thoại này đã gửi yêu cầu gần đây. Chúng tôi sẽ sớm liên hệ.'
      : error?.statusCode === 400
        ? 'Vui lòng kiểm tra họ tên và số điện thoại.'
        : 'Không thể gửi đăng ký. Vui lòng thử lại.'
  } finally {
    sending.value = false
  }
}

const money = (value?: number) => value == null ? 'Liên hệ' : `${new Intl.NumberFormat('vi-VN').format(value)} đ`
const date = (value?: string) => value ? new Intl.DateTimeFormat('vi-VN', { timeZone: 'Asia/Ho_Chi_Minh' }).format(new Date(value)) : ''
const background = (url?: string) => ({ backgroundImage: url ? `url('${url}')` : 'none' })
const productBackground = (product: Product) => product.id === 1 || product.id === 2
  ? {
      backgroundImage: `url('${product.id === 1 ? '/images/campaign/hero-red-v1.png' : '/images/campaign/hero-green-v1.png'}')`,
      backgroundSize: 'cover',
      backgroundPosition: '65% center'
    }
  : background(product.imageUrl)
const technologyBackground = computed(() => ({ backgroundImage: "linear-gradient(90deg,#07090ce8,#07090c6b),url('/images/campaign/technology-duo-v1.png')" }))
const featuredProducts = computed(() => products.value.filter(product => product.featured).slice(0, 2))
</script>

<template>
  <main id="top">
    <SiteHeader />
    <HeroCarousel :slides="slides" />

    <section id="models" class="featured section">
      <div class="section-kicker">RA MẮT 2026</div>
      <h2>SẢN PHẨM NỔI BẬT</h2>
      <div class="feature-grid">
        <article v-for="product in featuredProducts" :key="product.id" class="feature-card" :style="productBackground(product)">
          <span>{{ product.badge || 'SẢN PHẨM MỚI' }}</span>
          <div><h3>{{ product.title }}</h3><p>Từ <b>{{ money(product.price) }}</b></p><NuxtLink :to="`/products/${product.id}`">TÌM HIỂU THÊM</NuxtLink></div>
        </article>
      </div>
    </section>

    <section class="catalog section">
      <h2>XE MÁY ĐIỆN</h2>
      <div class="product-grid">
        <article v-for="(product, index) in products" :key="product.id" class="product-card">
          <div class="product-photo" :class="`crop-${index}`" :style="productBackground(product)"><span>{{ product.badge || 'MỚI' }}</span></div>
          <div class="product-body"><h3>XE MÁY ĐIỆN {{ product.title }}</h3><strong>{{ money(product.price) }}</strong><div class="specs"><span v-if="product.rangeText">{{ product.rangeText }}</span><span v-if="product.powerText">{{ product.powerText }}</span><span v-if="product.speedText">{{ product.speedText }}</span></div><NuxtLink :to="`/products/${product.id}`">Xem chi tiết</NuxtLink></div>
        </article>
      </div>
    </section>

    <section id="technology" class="technology" :style="technologyBackground">
      <div><span>CÔNG NGHỆ SAM.LOONG</span><h2>NĂNG LƯỢNG MỚI.<br>CHUYỂN ĐỘNG MỚI.</h2><p>Hệ truyền động điện hiệu suất cao, vận hành êm ái và hệ thống pin thông minh cho mỗi hành trình đô thị.</p></div>
    </section>

    <section id="news" class="news section">
      <div class="section-heading"><h2>TIN TỨC</h2><NuxtLink to="/news">Xem tất cả →</NuxtLink></div>
      <div class="news-grid"><NuxtLink v-for="(item, index) in news" :key="item.id" class="news-card" :to="`/news/${item.id}`"><article><div class="news-photo" :class="`news-${index}`" :style="background(item.imageUrl)" /><div class="news-copy"><p><span>{{ item.category }}</span> {{ date(item.publishedAt) }}</p><h3>{{ item.title }}</h3></div></article></NuxtLink></div>
    </section>

    <section id="booking" class="booking">
      <div class="booking-copy"><span>HOÀN TOÀN MIỄN PHÍ</span><h2>ĐẶT LỊCH TRẢI NGHIỆM</h2><p>Trải nghiệm tại showroom gần nhất. Đội ngũ tư vấn sẽ liên hệ xác nhận lịch hẹn của bạn.</p></div>
      <div class="booking-grid">
        <form @submit.prevent="submitBooking">
          <label>HỌ VÀ TÊN *<input v-model="booking.name" required maxlength="100" placeholder="Nguyễn Văn A"></label>
          <label>SỐ ĐIỆN THOẠI *<input v-model="booking.phone" required maxlength="18" pattern="(?:\+?84|0)[0-9 .-]{8,14}" placeholder="0912 345 678"></label>
          <label>TỈNH/THÀNH PHỐ *<select v-model="booking.city" required><option value="" disabled>Chọn tỉnh/thành phố</option><option v-for="province in vietnamProvinces" :key="province" :value="province">{{ province }}</option></select></label>
          <label>PHƯỜNG/XÃ *<input v-model="booking.district" required maxlength="128" placeholder="Phường/Xã"></label>
          <button :disabled="sending">{{ sending ? 'ĐANG GỬI...' : 'ĐĂNG KÝ NGAY' }}</button>
          <p v-if="sent" class="form-success">Cảm ơn bạn! Chúng tôi sẽ sớm liên hệ.</p>
          <p v-if="bookingError" class="form-error">{{ bookingError }}</p>
        </form>
        <div class="booking-photo" :style="background(slides[0]?.imageUrl)"><div><b>SẴN SÀNG<br>LĂN BÁNH?</b><span>Trải nghiệm Sam.Loong hôm nay.</span></div></div>
      </div>
    </section>

    <SiteFooter />
  </main>
</template>
