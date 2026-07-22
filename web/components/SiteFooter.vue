<script setup lang="ts">
type Section='PRODUCTS'|'ABOUT'|'SUPPORT'|'SOCIAL'
type FooterLink={id?:number;section:Section;label:string;url:string;icon?:string;sortOrder:number}
type FooterSettings={id:number;companyName:string;taxCode?:string;address?:string;hotline?:string;email?:string;copyrightText:string;links:FooterLink[]}

const config=useRuntimeConfig()
const contentApi=import.meta.server?config.apiServer:config.public.apiBase
const defaults:FooterSettings={id:1,companyName:'Công ty CP Xe điện SAM Việt Nam',taxCode:'0110234567',address:'593 Nguyễn Duy Trinh, Bình Trưng, Hồ Chí Minh',hotline:'1800 1234',email:'support@samlong.vn',copyrightText:'© 2026 SAM.LOONG. Bảo lưu mọi quyền.',links:[
  {section:'PRODUCTS',label:'F25 Thể thao',url:'/products/1',sortOrder:0},{section:'PRODUCTS',label:'DanDan',url:'/products/2',sortOrder:1},
  {section:'ABOUT',label:'Câu chuyện',url:'/#top',sortOrder:0},{section:'ABOUT',label:'Công nghệ',url:'/#technology',sortOrder:1},{section:'ABOUT',label:'Tuyển dụng',url:'/news',sortOrder:2},{section:'ABOUT',label:'Đối tác',url:'/#support',sortOrder:3},
  {section:'SUPPORT',label:'Chính sách bảo hành',url:'/#support',sortOrder:0},{section:'SUPPORT',label:'Dịch vụ sửa chữa',url:'/#support',sortOrder:1},{section:'SUPPORT',label:'Hướng dẫn sử dụng',url:'/#support',sortOrder:2},{section:'SUPPORT',label:'FAQ',url:'/#support',sortOrder:3},{section:'SUPPORT',label:'Liên hệ',url:'/#booking',sortOrder:4},
  {section:'SOCIAL',label:'Facebook',url:'#',icon:'facebook',sortOrder:0},{section:'SOCIAL',label:'YouTube',url:'#',icon:'youtube',sortOrder:1},{section:'SOCIAL',label:'Instagram',url:'#',icon:'instagram',sortOrder:2}
]}
const {data}=await useFetch<FooterSettings>(`${contentApi}/footer`,{key:'footer-settings',default:()=>defaults})
const settings=computed(()=>data.value||defaults)
const linksFor=(section:Section)=>settings.value.links.filter(item=>item.section===section).sort((a,b)=>a.sortOrder-b.sortOrder)
const isInternal=(url:string)=>url.startsWith('/')
const phoneHref=computed(()=>`tel:${(settings.value.hotline||'').replace(/[^+\d]/g,'')}`)
</script>

<template>
  <footer id="support" class="site-footer">
    <div class="footer-main">
      <section class="footer-brand">
        <NuxtLink class="footer-logo" to="/" aria-label="Sam.Loong">
          <img src="/images/samlong-logo.png" alt="Sam.Loong">
        </NuxtLink>
        <p>{{settings.companyName}}</p>
        <small v-if="settings.taxCode">Mã số thuế: {{settings.taxCode}}</small>
        <small v-if="settings.address">{{settings.address}}</small>
        <div class="social-links" aria-label="Mạng xã hội">
          <a v-for="item in linksFor('SOCIAL')" :key="item.id||item.label" :href="item.url" :aria-label="item.label" :target="item.url.startsWith('http')?'_blank':undefined" rel="noopener">
            <svg v-if="item.icon==='facebook'" viewBox="0 0 24 24" aria-hidden="true"><path d="M14 8h3V4h-3c-3 0-5 2-5 5v2H6v4h3v7h4v-7h3l1-4h-4V9c0-1 .4-1 1-1Z"/></svg>
            <svg v-else-if="item.icon==='youtube'" viewBox="0 0 24 24" aria-hidden="true"><path d="M21 7.2a3 3 0 0 0-2.1-2.1C17 4.6 12 4.6 12 4.6s-5 0-6.9.5A3 3 0 0 0 3 7.2 31 31 0 0 0 2.6 12 31 31 0 0 0 3 16.8a3 3 0 0 0 2.1 2.1c1.9.5 6.9.5 6.9.5s5 0 6.9-.5a3 3 0 0 0 2.1-2.1 31 31 0 0 0 .4-4.8 31 31 0 0 0-.4-4.8ZM10 15.3V8.7l5.7 3.3-5.7 3.3Z"/></svg>
            <svg v-else viewBox="0 0 24 24" aria-hidden="true"><path d="M7 2h10a5 5 0 0 1 5 5v10a5 5 0 0 1-5 5H7a5 5 0 0 1-5-5V7a5 5 0 0 1 5-5Zm0 2a3 3 0 0 0-3 3v10a3 3 0 0 0 3 3h10a3 3 0 0 0 3-3V7a3 3 0 0 0-3-3H7Zm10.5 1.5a1.2 1.2 0 1 1 0 2.4 1.2 1.2 0 0 1 0-2.4ZM12 7a5 5 0 1 1 0 10 5 5 0 0 1 0-10Zm0 2a3 3 0 1 0 0 6 3 3 0 0 0 0-6Z"/></svg>
          </a>
        </div>
      </section>

      <nav class="footer-column" aria-label="Sản phẩm">
        <b>SẢN PHẨM</b>
        <template v-for="item in linksFor('PRODUCTS')" :key="item.id||item.label"><NuxtLink v-if="isInternal(item.url)" :to="item.url">{{item.label}}</NuxtLink><a v-else :href="item.url" target="_blank" rel="noopener">{{item.label}}</a></template>
      </nav>

      <nav class="footer-column" aria-label="Về chúng tôi">
        <b>VỀ CHÚNG TÔI</b>
        <template v-for="item in linksFor('ABOUT')" :key="item.id||item.label"><NuxtLink v-if="isInternal(item.url)" :to="item.url">{{item.label}}</NuxtLink><a v-else :href="item.url" target="_blank" rel="noopener">{{item.label}}</a></template>
      </nav>

      <nav class="footer-column" aria-label="Hỗ trợ">
        <b>HỖ TRỢ</b>
        <template v-for="item in linksFor('SUPPORT')" :key="item.id||item.label"><NuxtLink v-if="isInternal(item.url)" :to="item.url">{{item.label}}</NuxtLink><a v-else :href="item.url" target="_blank" rel="noopener">{{item.label}}</a></template>
      </nav>
    </div>

    <div class="footer-bottom">
      <div>
        <a v-if="settings.hotline" :href="phoneHref">☎ <span>Hotline: {{settings.hotline}} (miễn phí)</span></a>
        <a v-if="settings.email" :href="`mailto:${settings.email}`">✉ <span>{{settings.email}}</span></a>
      </div>
      <small>{{settings.copyrightText}}</small>
    </div>
  </footer>
</template>

<style scoped>
.site-footer{padding:48px max(calc((100% - 1220px)/2),42px) 28px;background:#050505;color:#fff}.footer-main{display:grid;grid-template-columns:1.55fr .75fr .75fr .95fr;gap:72px}.footer-brand{display:flex;flex-direction:column;align-items:flex-start}.footer-logo{display:block;width:160px;height:56px;overflow:hidden;margin-bottom:10px}.footer-logo img{width:160px;height:56px;max-width:none;object-fit:cover;object-position:center 43%;filter:invert(1)}.footer-brand p{margin:0 0 13px;color:#b7b7b7;font-size:13px}.footer-brand small{display:block;margin-bottom:10px;color:#666;font-size:12px}.footer-column{display:flex;flex-direction:column;align-items:flex-start;gap:14px;padding-top:9px}.footer-column b{margin-bottom:7px;font-size:11px;letter-spacing:3px}.footer-column a{color:#6f6f6f;font-size:12px;transition:color .2s}.footer-column a:hover{color:#fff}.social-links{display:flex;gap:10px;margin-top:26px}.social-links a{width:30px;height:30px;display:grid;place-items:center;border:1px solid #272727;border-radius:50%;color:#777}.social-links a:hover{color:#fff;border-color:#777}.social-links svg{width:13px;height:13px;fill:currentColor}.footer-bottom{display:flex;align-items:center;justify-content:space-between;margin-top:10px;padding-top:24px;border-top:1px solid #1a1a1a}.footer-bottom>div{display:flex;gap:26px}.footer-bottom a{color:#696969;font-size:12px}.footer-bottom a:first-child,.footer-bottom a:nth-child(2){color:#2364a5}.footer-bottom a span{color:#696969;margin-left:5px}.footer-bottom small{color:#555;font-size:11px}

@media(max-width:900px){.site-footer{padding:44px 24px 24px}.footer-main{grid-template-columns:1.4fr 1fr;gap:42px}.footer-bottom{align-items:flex-start;gap:20px}.footer-bottom>div{flex-direction:column;gap:10px}}

@media(max-width:600px){.site-footer{padding:38px 20px 24px}.footer-main{grid-template-columns:1fr 1fr;gap:36px 24px}.footer-brand{grid-column:1/-1}.footer-column{gap:12px}.footer-bottom{flex-direction:column;margin-top:34px}.footer-bottom>div{gap:10px}}
</style>
