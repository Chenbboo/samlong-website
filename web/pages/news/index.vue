<script setup lang="ts">
type News={id:number;title:string;content?:string;imageUrl:string;category?:string;publishedAt?:string}
const config=useRuntimeConfig();const contentApi=import.meta.server?config.apiServer:config.public.apiBase
const {data:articles}=await useFetch<News[]>(`${contentApi}/news`,{key:'news-list',default:()=>[]})
const date=(value?:string)=>value?new Intl.DateTimeFormat('vi-VN').format(new Date(value)):''
useSeoMeta({title:'Tin tức | Sam.Loong',description:'Tin tức mới nhất về xe máy điện và công nghệ Sam.Loong'})
</script>
<template><main><SiteHeader/><section class="listing-hero"><span>SAM.LOONG NEWS</span><h1>TIN TỨC</h1><p>Cập nhật sản phẩm, công nghệ và hành trình chuyển động xanh.</p></section><section class="news-list section"><div class="news-list-grid"><NuxtLink v-for="(item,index) in articles" :key="item.id" :to="`/news/${item.id}`" class="news-list-card"><div class="news-list-image" :class="`news-${index}`" :style="{backgroundImage:`url('${item.imageUrl}')`}"/><div><p><span>{{item.category}}</span> {{date(item.publishedAt)}}</p><h2>{{item.title}}</h2><div class="news-excerpt">{{item.content}}</div><b>Đọc thêm →</b></div></NuxtLink></div></section><SiteFooter/></main></template>
