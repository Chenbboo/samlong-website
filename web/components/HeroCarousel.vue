<script setup lang="ts">
type Slide = { id: number; title: string; subtitle?: string; eyebrow?: string; imageUrl: string; linkUrl?: string }
const props = defineProps<{ slides: Slide[] }>()
const active = ref(0)
const paused = ref(false)
let timer: ReturnType<typeof setInterval> | undefined
const change = (step: number) => { if (props.slides.length) active.value = (active.value + step + props.slides.length) % props.slides.length }
const start = () => { if (!paused.value && props.slides.length > 1) timer = setInterval(() => change(1), 5500) }
const stop = () => { if (timer) clearInterval(timer) }
watch(paused, () => { stop(); start() })
watch(() => props.slides.length, () => { active.value = 0; stop(); start() })
onMounted(start)
onBeforeUnmount(stop)
</script>

<template>
  <section v-if="slides.length" class="hero" aria-roledescription="carousel" aria-label="Sam.Loong featured vehicles" @mouseenter="paused = true" @mouseleave="paused = false" @focusin="paused = true" @focusout="paused = false" @keydown.left="change(-1)" @keydown.right="change(1)">
    <div class="hero-track" :style="{ transform: `translateX(-${active * 100}%)` }">
      <article v-for="(slide, index) in slides" :key="slide.id" class="hero-slide" :aria-hidden="index !== active">
        <div class="hero-media" :style="{ backgroundImage: `url('${slide.imageUrl}')` }" />
        <div class="hero-shade" />
        <div class="hero-copy"><span>{{ slide.eyebrow }}</span><h1>{{ slide.title }}<br><em>{{ slide.subtitle }}</em></h1><a :href="slide.linkUrl || '#models'" :tabindex="index === active ? 0 : -1">KHÁM PHÁ NGAY</a></div>
      </article>
    </div>
    <button v-if="slides.length > 1" class="hero-arrow left" aria-label="Previous slide" @click="change(-1)">‹</button>
    <button v-if="slides.length > 1" class="hero-arrow right" aria-label="Next slide" @click="change(1)">›</button>
    <div v-if="slides.length > 1" class="hero-dots" :aria-label="`Slide ${active + 1} of ${slides.length}`"><button v-for="(_, index) in slides" :key="index" :class="{ active: index === active }" :aria-label="`Show slide ${index + 1}`" @click="active = index" /></div>
  </section>
</template>
