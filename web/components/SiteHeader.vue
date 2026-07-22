<script setup lang="ts">
const open=ref(false);const route=useRoute();watch(()=>route.fullPath,()=>open.value=false)
</script>
<template><header class="site-header"><NuxtLink class="logo" to="/" aria-label="Sam.Loong" @click="open=false"><img src="/images/samlong-logo.png" alt="Sam.Loong"></NuxtLink><button class="menu-toggle" type="button" :aria-expanded="open" aria-controls="site-navigation" aria-label="Mở menu" @click="open=!open"><i/><i/><i/></button><nav id="site-navigation" :class="{open}"><NuxtLink to="/#models">Xe máy điện</NuxtLink><NuxtLink to="/#technology">Công nghệ</NuxtLink><NuxtLink to="/#support">Hỗ trợ & Bảo hành</NuxtLink><NuxtLink to="/news">Tin tức</NuxtLink><NuxtLink to="/#booking">Cửa hàng</NuxtLink><NuxtLink class="mobile-booking-link" to="/#booking">ĐĂNG KÝ LÁI THỬ</NuxtLink></nav><NuxtLink class="test-drive" to="/#booking">ĐĂNG KÝ LÁI THỬ</NuxtLink><button v-if="open" class="nav-backdrop" aria-label="Đóng menu" @click="open=false"/></header></template>

<style scoped>
.logo {
  width: 174px;
  min-width: 174px;
  height: 60px;
  overflow: hidden;
}

.logo img {
  width: 174px;
  height: 60px;
  max-width: none;
  object-fit: cover;
  object-position: center 43%;
}

@media (max-width: 600px) {
  .logo,
  .logo img {
    width: 145px;
    min-width: 145px;
    height: 50px;
  }
}
</style>
