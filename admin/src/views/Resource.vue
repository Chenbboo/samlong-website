<script setup lang="ts">
import {ref,reactive,onMounted,watch} from 'vue'
import {ElMessage,ElMessageBox} from 'element-plus'
import {useRoute} from 'vue-router'
import {api} from '../api.ts'
import MediaPicker from '../components/MediaPicker.vue'
import {optimizeImage} from '../image.ts'
const props=defineProps<{kind:string,title:string}>();const route=useRoute();const rows=ref<any[]>([]);const dialog=ref(false);const saving=ref(false);const form=ref<any>({});const filters=reactive({keyword:'',status:'',city:'',from:'',to:''});const page=ref(1);const pageSize=ref(10);const total=ref(0)
async function load(){const values:any={page:page.value-1,size:pageSize.value,keyword:filters.keyword,status:filters.status};if(props.kind==='bookings')Object.assign(values,{city:filters.city,from:filters.from,to:filters.to});const params=Object.fromEntries(Object.entries(values).filter(([,value])=>value!==''&&value!=null));const response=(await api.get('/'+props.kind,{params})).data;if(Array.isArray(response)){rows.value=response;total.value=response.length}else{rows.value=response.content||[];total.value=response.totalElements||0}}
async function search(){page.value=1;await load()}
function localDateTime(){const now=new Date();const offset=now.getTimezoneOffset()*60000;return new Date(now.getTime()-offset).toISOString().slice(0,19)}
function edit(row:any={}){form.value={status:'ACTIVE',sortOrder:0,featured:false,...row,imageUrls:[...(row.imageUrls||[])],colors:(row.colors||[]).map((v:any)=>({...v}))};if(props.kind==='bookings')form.value.unread=false;if(props.kind==='news'&&!form.value.publishedAt)form.value.publishedAt=localDateTime();dialog.value=true}
function validateForm(){
  if(props.kind==='bookings'){
    if(!String(form.value.name||'').trim())return '请填写客户姓名'
    if(!/^(?:\+?84|0)[0-9 .-]{8,14}$/.test(String(form.value.phone||'').trim()))return '请输入正确的越南手机号码'
  }else if(!String(form.value.title||'').trim())return '请填写标题'
  if(props.kind==='products'){
    if(form.value.price!=null&&Number(form.value.price)<0)return '价格不能小于 0'
    if(Number(form.value.sortOrder||0)<0)return '排序不能小于 0'
    for(const color of form.value.colors||[]){
      if(!String(color.colorName||'').trim())return '请填写颜色名称'
      if(!/^#[0-9a-fA-F]{6}$/.test(String(color.hexCode||'')))return '颜色值必须是 #RRGGBB 格式'
    }
  }
  if(props.kind==='news'){
    if(!form.value.publishedAt)return '请选择发布时间'
    if(new Date(form.value.publishedAt).getTime()>Date.now())return '发布时间不能晚于当前时间'
  }
  if(props.kind==='slides'&&!String(form.value.imageUrl||'').trim())return '请上传轮播图片'
  return ''
}
async function save(){const validationMessage=validateForm();if(validationMessage){ElMessage.warning(validationMessage);return}saving.value=true;try{if(form.value.id)await api.put(`/${props.kind}/${form.value.id}`,form.value);else await api.post('/'+props.kind,form.value);dialog.value=false;await load();ElMessage.success('保存成功，官网刷新后即可看到')}catch(error:any){const fieldErrors=error?.response?.data?.fieldErrors;const message=fieldErrors?Object.values(fieldErrors)[0]:error?.response?.data?.message;ElMessage.error(String(message||'保存失败，请检查填写内容'))}finally{saving.value=false}}
async function remove(id:number){await ElMessageBox.confirm('确定删除这条内容吗？','删除确认',{type:'warning'});await api.delete(`/${props.kind}/${id}`);await load()}
async function sendImage(file:File){const optimized=await optimizeImage(file);const data=new FormData();data.append('file',optimized.file);data.append('thumbnail',optimized.thumbnail);return(await api.post('/uploads',data)).data.url as string}
async function uploadMainImage(options:any){try{const url=await sendImage(options.file);form.value.imageUrl=url;if(props.kind==='products'&&!form.value.imageUrls.length)form.value.imageUrls.push(url);options.onSuccess({url});ElMessage.success('主图上传成功')}catch(error){options.onError(error);ElMessage.error('图片上传失败')}}
async function uploadGalleryImage(options:any){try{const url=await sendImage(options.file);form.value.imageUrls.push(url);if(!form.value.imageUrl)form.value.imageUrl=url;options.onSuccess({url});ElMessage.success('车型图片已添加')}catch(error){options.onError(error);ElMessage.error('图片上传失败')}}
function selectMainImage(url:string){form.value.imageUrl=url;if(props.kind==='products'&&!form.value.imageUrls.includes(url))form.value.imageUrls.unshift(url)}
function selectGalleryImage(url:string){if(!form.value.imageUrls.includes(url))form.value.imageUrls.push(url);if(!form.value.imageUrl)form.value.imageUrl=url}
function removeGallery(index:number){form.value.imageUrls.splice(index,1)}
function addColor(){form.value.colors.push({colorName:'新颜色',hexCode:'#0878ee'})}
const money=(value:number)=>value==null?'':new Intl.NumberFormat('vi-VN').format(value)+' đ'
const bookingStatus=(value:string)=>({NEW:'待联系',CONTACTED:'已联系',VISITED:'已到店',COMPLETED:'已完成',CANCELLED:'已取消'}[value]||value)
const statusType=(value:string):any=>({NEW:'danger',CONTACTED:'warning',VISITED:'primary',COMPLETED:'success',CANCELLED:'info'}[value]||'info')
const formatDate=(value:string)=>value?new Date(value).toLocaleString('zh-CN',{hour12:false}):''
async function initialize(){page.value=1;filters.keyword=String(route.query.keyword||'');filters.status=String(route.query.status||'');if(props.kind!=='bookings'){filters.city='';filters.from='';filters.to=''}await load()}
onMounted(initialize);watch(()=>props.kind,initialize);watch(()=>route.query,()=>{if(props.kind==='bookings')initialize()})
</script>
<template>
  <div class="toolbar"><h1 class="page-title">{{title}}</h1><el-button v-if="kind!=='bookings'" type="primary" @click="edit()">新增</el-button></div>
  <div v-if="kind==='bookings'" class="booking-filters"><el-input v-model="filters.keyword" clearable placeholder="搜索姓名或手机号" @keyup.enter="search"/><el-select v-model="filters.status" clearable placeholder="全部状态"><el-option label="待联系" value="NEW"/><el-option label="已联系" value="CONTACTED"/><el-option label="已到店" value="VISITED"/><el-option label="已完成" value="COMPLETED"/><el-option label="已取消" value="CANCELLED"/></el-select><el-input v-model="filters.city" clearable placeholder="城市"/><el-date-picker v-model="filters.from" value-format="YYYY-MM-DD" placeholder="开始日期"/><el-date-picker v-model="filters.to" value-format="YYYY-MM-DD" placeholder="结束日期"/><el-button type="primary" @click="search">查询</el-button></div>
  <div v-else class="resource-filters"><el-input v-model="filters.keyword" clearable placeholder="搜索标题或内容" @keyup.enter="search"/><el-select v-model="filters.status" clearable placeholder="全部状态"><el-option label="发布" value="ACTIVE"/><el-option label="草稿" value="DRAFT"/></el-select><el-button type="primary" @click="search">查询</el-button></div>
  <el-table :data="rows" stripe><el-table-column prop="id" label="ID" width="70"/><el-table-column v-if="kind==='bookings'" label="客户" min-width="140"><template #default="scope"><el-badge :is-dot="scope.row.unread" class="booking-dot">{{scope.row.name}}</el-badge></template></el-table-column><el-table-column v-else prop="title" label="标题" min-width="220"/><el-table-column v-if="kind==='bookings'" prop="phone" label="手机号" width="150"/><el-table-column v-if="kind==='bookings'" prop="city" label="城市" width="130"/><el-table-column v-if="kind==='bookings'" label="状态" width="100"><template #default="scope"><el-tag :type="statusType(scope.row.status)">{{bookingStatus(scope.row.status)}}</el-tag></template></el-table-column><el-table-column v-if="kind==='bookings'" label="提交时间" width="180"><template #default="scope">{{formatDate(scope.row.createdAt)}}</template></el-table-column><el-table-column v-if="kind==='products'" label="价格" width="150"><template #default="scope">{{money(scope.row.price)}}</template></el-table-column><el-table-column v-if="kind==='products'" label="推荐" width="80"><template #default="scope">{{scope.row.featured?'是':'否'}}</template></el-table-column><el-table-column v-if="kind!=='bookings'" prop="status" label="状态" width="100"/><el-table-column label="操作" width="150"><template #default="scope"><el-button text type="primary" @click="edit(scope.row)">编辑</el-button><el-button text type="danger" @click="remove(scope.row.id)">删除</el-button></template></el-table-column></el-table>
  <div class="table-pagination"><span>共 {{total}} 条</span><el-pagination v-model:current-page="page" v-model:page-size="pageSize" :page-sizes="[10,20,50]" layout="sizes, prev, pager, next" :total="total" @current-change="load" @size-change="search"/></div>
  <el-dialog v-model="dialog" :title="title" width="760"><el-form label-position="top">
    <template v-if="kind==='bookings'"><div class="form-grid"><el-form-item label="姓名"><el-input v-model="form.name"/></el-form-item><el-form-item label="手机号"><el-input v-model="form.phone"/></el-form-item><el-form-item label="省/直辖市"><el-input v-model="form.city"/></el-form-item><el-form-item label="坊/社"><el-input v-model="form.district"/></el-form-item></div><el-form-item label="预约状态"><el-select v-model="form.status"><el-option label="待联系" value="NEW"/><el-option label="已联系" value="CONTACTED"/><el-option label="已到店" value="VISITED"/><el-option label="已完成" value="COMPLETED"/><el-option label="已取消" value="CANCELLED"/></el-select></el-form-item><el-form-item label="客户备注"><el-input v-model="form.notes" type="textarea" :rows="3" placeholder="记录客户需求、方便联系时间等"/></el-form-item><el-form-item label="跟进记录"><el-input v-model="form.followUp" type="textarea" :rows="5" placeholder="记录每次电话或到店跟进情况"/></el-form-item><p class="booking-created">提交时间：{{formatDate(form.createdAt)}}　最后更新：{{formatDate(form.updatedAt)}}</p></template>
    <template v-else><el-form-item label="标题"><el-input v-model="form.title"/></el-form-item><el-form-item v-if="kind==='slides'" label="第二行标题"><el-input v-model="form.subtitle"/></el-form-item><el-form-item v-if="kind==='slides'" label="上方小标题"><el-input v-model="form.eyebrow"/></el-form-item><el-form-item v-if="kind==='news'" label="分类"><el-input v-model="form.category"/></el-form-item><el-form-item v-if="kind!=='slides'" label="详细介绍"><el-input v-model="form.content" type="textarea" :rows="5"/></el-form-item>
      <el-form-item label="主图"><div class="image-actions"><el-upload accept="image/jpeg,image/png,image/webp" :show-file-list="false" :http-request="uploadMainImage"><el-button type="primary" plain>选择并上传主图</el-button></el-upload><MediaPicker @select="selectMainImage"/></div><img v-if="form.imageUrl" class="image-preview" :src="form.imageUrl" alt="主图预览"></el-form-item><el-form-item label="主图地址"><el-input v-model="form.imageUrl"/></el-form-item>
      <template v-if="kind==='products'">
        <el-divider content-position="left">车型图库</el-divider><el-form-item><div class="image-actions"><el-upload accept="image/jpeg,image/png,image/webp" :show-file-list="false" :http-request="uploadGalleryImage"><el-button type="primary" plain>添加车型图片</el-button></el-upload><MediaPicker @select="selectGalleryImage"/></div><div class="gallery-editor"><div v-for="(url,index) in form.imageUrls" :key="url+index"><img :src="url" alt="车型图片"><el-button type="danger" text @click="removeGallery(index)">移除</el-button></div></div></el-form-item>
        <el-divider content-position="left">价格与参数</el-divider><el-form-item label="价格（越南盾）"><el-input-number v-model="form.price" :min="0" :step="100000"/></el-form-item><div class="form-grid"><el-form-item label="续航"><el-input v-model="form.rangeText" placeholder="120 km"/></el-form-item><el-form-item label="功率"><el-input v-model="form.powerText" placeholder="1.500 W"/></el-form-item><el-form-item label="速度"><el-input v-model="form.speedText" placeholder="50 km/h"/></el-form-item><el-form-item label="电池"><el-input v-model="form.batteryText" placeholder="Lithium-ion"/></el-form-item><el-form-item label="充电时间"><el-input v-model="form.chargeTimeText" placeholder="4–6 giờ"/></el-form-item><el-form-item label="整车重量"><el-input v-model="form.weightText" placeholder="110 kg"/></el-form-item><el-form-item label="质保"><el-input v-model="form.warrantyText" placeholder="3 năm"/></el-form-item></div>
        <el-form-item label="产品卖点（每行一条）"><el-input v-model="form.highlights" type="textarea" :rows="4"/></el-form-item><el-form-item label="角标"><el-input v-model="form.badge" placeholder="MỚI"/></el-form-item>
        <el-divider content-position="left">可选颜色</el-divider><div class="color-editor"><div v-for="(color,index) in form.colors" :key="index"><el-color-picker v-model="color.hexCode"/><el-input v-model="color.colorName"/><el-button type="danger" text @click="form.colors.splice(index,1)">删除</el-button></div><el-button plain @click="addColor">添加颜色</el-button></div>
        <el-form-item label="首页推荐"><el-switch v-model="form.featured" active-text="推荐到首页"/></el-form-item>
      </template>
      <el-form-item v-if="kind==='slides'" label="跳转链接"><el-input v-model="form.linkUrl" placeholder="#models"/></el-form-item><el-form-item v-if="kind==='news'" label="发布时间"><el-date-picker v-model="form.publishedAt" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss"/></el-form-item><el-form-item v-if="kind!=='news'" label="排序"><el-input-number v-model="form.sortOrder" :min="0"/></el-form-item><el-form-item label="状态"><el-select v-model="form.status"><el-option label="发布" value="ACTIVE"/><el-option label="草稿" value="DRAFT"/></el-select></el-form-item>
    </template></el-form><template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" :loading="saving" @click="save">保存</el-button></template></el-dialog>
</template>
<style scoped>
.resource-filters{display:grid;grid-template-columns:minmax(260px,480px) 160px auto;gap:10px;margin:0 0 18px}.table-pagination{display:flex;justify-content:space-between;align-items:center;margin-top:18px;color:#7d8590;font-size:13px}@media(max-width:700px){.resource-filters{grid-template-columns:1fr}.table-pagination{align-items:flex-start;gap:12px;flex-direction:column}}
.image-actions{display:flex;gap:10px;align-items:center}
</style>
