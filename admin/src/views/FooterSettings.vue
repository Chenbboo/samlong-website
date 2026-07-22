<script setup lang="ts">
import {onMounted,reactive,ref} from 'vue'
import {ElMessage,ElMessageBox} from 'element-plus'
import {api} from '../api.ts'

type Section='PRODUCTS'|'ABOUT'|'SUPPORT'|'SOCIAL'
type FooterLink={id?:number;section:Section;label:string;url:string;icon?:string;sortOrder:number}
type FooterSettings={id:number;companyName:string;taxCode:string;address:string;hotline:string;email:string;copyrightText:string;links:FooterLink[]}

const loading=ref(true)
const saving=ref(false)
const form=reactive<FooterSettings>({id:1,companyName:'',taxCode:'',address:'',hotline:'',email:'',copyrightText:'',links:[]})
const groups:{section:Section;title:string;hint:string}[]=[
  {section:'PRODUCTS',title:'产品链接',hint:'页脚“产品”栏目'},
  {section:'ABOUT',title:'关于我们',hint:'公司相关页面'},
  {section:'SUPPORT',title:'支持链接',hint:'售后与联系页面'},
  {section:'SOCIAL',title:'社交媒体',hint:'Facebook、YouTube、Instagram 等'}
]

const linksFor=(section:Section)=>form.links.filter(item=>item.section===section).sort((a,b)=>a.sortOrder-b.sortOrder)
function addLink(section:Section){form.links.push({section,label:'',url:'',icon:section==='SOCIAL'?'facebook':'',sortOrder:linksFor(section).length})}
async function removeLink(item:FooterLink){
  await ElMessageBox.confirm(`确定删除“${item.label||'未命名链接'}”吗？`,'删除链接',{type:'warning'})
  form.links.splice(form.links.indexOf(item),1)
}
async function load(){
  loading.value=true
  try{Object.assign(form,(await api.get<FooterSettings>('/footer')).data)}
  catch{ElMessage.error('页脚配置加载失败')}
  finally{loading.value=false}
}
async function save(){
  if(!form.companyName.trim()||!form.copyrightText.trim()){ElMessage.warning('公司名称和版权信息不能为空');return}
  for(const group of groups){linksFor(group.section).forEach((item,index)=>item.sortOrder=index)}
  saving.value=true
  try{Object.assign(form,(await api.put<FooterSettings>('/footer',form)).data);ElMessage.success('页脚设置已保存，官网会立即显示新内容')}
  catch(error:any){ElMessage.error(error?.response?.data?.message||'保存失败，请检查链接和邮箱格式')}
  finally{saving.value=false}
}
onMounted(load)
</script>

<template>
  <div v-loading="loading" class="footer-settings-page">
    <div class="settings-heading"><div><h1 class="page-title">页脚设置</h1><p>修改官网最底部显示的公司资料、联系方式和栏目链接。</p></div><el-button type="primary" :loading="saving" @click="save">保存设置</el-button></div>

    <el-card shadow="never" class="settings-card">
      <template #header><b>公司与联系信息</b></template>
      <el-form label-position="top">
        <div class="settings-grid">
          <el-form-item label="公司名称"><el-input v-model="form.companyName" maxlength="255"/></el-form-item>
          <el-form-item label="税号"><el-input v-model="form.taxCode" maxlength="64"/></el-form-item>
          <el-form-item label="热线电话"><el-input v-model="form.hotline" maxlength="64"/></el-form-item>
          <el-form-item label="联系邮箱"><el-input v-model="form.email" maxlength="255"/></el-form-item>
        </div>
        <el-form-item label="公司地址"><el-input v-model="form.address" maxlength="500"/></el-form-item>
        <el-form-item label="版权信息"><el-input v-model="form.copyrightText" maxlength="255"/></el-form-item>
      </el-form>
    </el-card>

    <el-card v-for="group in groups" :key="group.section" shadow="never" class="settings-card link-card">
      <template #header><div class="card-heading"><div><b>{{group.title}}</b><small>{{group.hint}}</small></div><el-button text type="primary" @click="addLink(group.section)">＋ 添加链接</el-button></div></template>
      <div v-for="(item,index) in linksFor(group.section)" :key="item.id||`${group.section}-${index}`" class="link-row">
        <span class="order-number">{{index+1}}</span>
        <el-select v-if="group.section==='SOCIAL'" v-model="item.icon" placeholder="图标">
          <el-option label="Facebook" value="facebook"/><el-option label="YouTube" value="youtube"/><el-option label="Instagram" value="instagram"/>
        </el-select>
        <el-input v-model="item.label" maxlength="128" placeholder="显示名称"/>
        <el-input v-model="item.url" maxlength="500" placeholder="链接，如 /products/1 或 https://..."/>
        <el-button text type="danger" @click="removeLink(item)">删除</el-button>
      </div>
      <el-empty v-if="!linksFor(group.section).length" description="暂无链接" :image-size="58"/>
    </el-card>

    <div class="save-bar"><el-button type="primary" size="large" :loading="saving" @click="save">保存全部设置</el-button></div>
  </div>
</template>

<style scoped>
.footer-settings-page{max-width:1120px}.settings-heading{display:flex;justify-content:space-between;align-items:flex-start;margin-bottom:22px}.settings-heading .page-title{margin-bottom:8px}.settings-heading p{margin:0;color:#7b8490;font-size:13px}.settings-card{margin-bottom:20px;border-radius:10px}.settings-grid{display:grid;grid-template-columns:1fr 1fr;gap:0 20px}.card-heading{display:flex;align-items:center;justify-content:space-between}.card-heading>div{display:flex;flex-direction:column;gap:5px}.card-heading small{color:#9098a3;font-weight:400}.link-row{display:grid;grid-template-columns:34px minmax(180px,.8fr) minmax(280px,1.5fr) 54px;gap:12px;align-items:center;margin-bottom:12px}.link-row:has(.el-select){grid-template-columns:34px 150px minmax(150px,.7fr) minmax(260px,1.4fr) 54px}.order-number{width:28px;height:28px;display:grid;place-items:center;border-radius:50%;background:#f1f4f8;color:#76808d;font-size:12px}.save-bar{display:flex;justify-content:flex-end;padding:2px 0 30px}.save-bar .el-button{min-width:160px}
@media(max-width:760px){.settings-heading{gap:16px}.settings-grid{grid-template-columns:1fr}.link-row,.link-row:has(.el-select){grid-template-columns:28px 1fr}.link-row .el-input,.link-row .el-select{grid-column:2}.link-row .el-button{grid-column:2;justify-self:end}.order-number{grid-row:1/4}.settings-heading .el-button{flex-shrink:0}}
</style>
