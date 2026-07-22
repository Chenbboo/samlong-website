<script setup lang="ts">
type News={id:number;title:string;content?:string;imageUrl:string;category?:string;publishedAt?:string}
const route=useRoute();const config=useRuntimeConfig();const contentApi=import.meta.server?config.apiServer:config.public.apiBase
const {data:article,error}=await useFetch<News>(`${contentApi}/news/${route.params.id}`,{key:`news-${route.params.id}`})
if(error.value||!article.value)throw createError({statusCode:404,statusMessage:'Không tìm thấy bài viết'})
const date=(value?:string)=>value?new Intl.DateTimeFormat('vi-VN',{dateStyle:'long'}).format(new Date(value)):''
useSeoMeta({title:()=>`${article.value?.title} | Sam.Loong`,description:()=>article.value?.content||'',ogImage:()=>article.value?.imageUrl})
</script>
<template><main><SiteHeader/><article v-if="article" class="article-detail"><div class="detail-breadcrumb"><NuxtLink to="/">Trang chủ</NuxtLink><span>/</span><NuxtLink to="/news">Tin tức</NuxtLink></div><header><p><span>{{article.category}}</span> {{date(article.publishedAt)}}</p><h1>{{article.title}}</h1></header><div class="article-cover" :style="{backgroundImage:`url('${article.imageUrl}')`}"/><div class="article-body"><p>{{article.content}}</p><NuxtLink to="/news">← Quay lại tin tức</NuxtLink></div></article><SiteFooter/></main></template>
