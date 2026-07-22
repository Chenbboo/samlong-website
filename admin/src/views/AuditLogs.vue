<script setup lang="ts">
import {onMounted,reactive,ref} from 'vue'
import {api} from '../api.ts'

const rows=ref<any[]>([]);const loading=ref(false);const page=ref(1);const pageSize=ref(20);const total=ref(0)
const filters=reactive({keyword:'',action:'',resourceType:''})
const actionLabel=(value:string)=>({CREATE:'新增',UPDATE:'修改',DELETE:'删除'}[value]||value)
const actionType=(value:string):any=>({CREATE:'success',UPDATE:'primary',DELETE:'danger'}[value]||'info')
const resourceLabel=(value:string)=>({PRODUCT:'车型',NEWS:'新闻',SLIDE:'轮播图',BOOKING:'试驾预约',MEDIA:'图片素材'}[value]||value)
const formatDate=(value:string)=>value?new Date(value).toLocaleString('zh-CN',{hour12:false}):''
async function load(){loading.value=true;try{const values:any={page:page.value-1,size:pageSize.value,...filters};const params=Object.fromEntries(Object.entries(values).filter(([,value])=>value!==''));const response=(await api.get('/audit-logs',{params})).data;rows.value=response.content||[];total.value=response.totalElements||0}finally{loading.value=false}}
async function search(){page.value=1;await load()}
onMounted(load)
</script>

<template>
  <div class="toolbar"><h1 class="page-title">操作日志</h1></div>
  <div class="audit-filters">
    <el-input v-model="filters.keyword" clearable placeholder="搜索管理员或内容标题" @keyup.enter="search"/>
    <el-select v-model="filters.resourceType" clearable placeholder="全部模块"><el-option label="车型" value="PRODUCT"/><el-option label="新闻" value="NEWS"/><el-option label="轮播图" value="SLIDE"/><el-option label="试驾预约" value="BOOKING"/><el-option label="图片素材" value="MEDIA"/></el-select>
    <el-select v-model="filters.action" clearable placeholder="全部操作"><el-option label="新增" value="CREATE"/><el-option label="修改" value="UPDATE"/><el-option label="删除" value="DELETE"/></el-select>
    <el-button type="primary" @click="search">查询</el-button>
  </div>
  <el-table v-loading="loading" :data="rows" stripe>
    <el-table-column prop="id" label="ID" width="75"/>
    <el-table-column prop="username" label="管理员" width="120"/>
    <el-table-column label="操作" width="90"><template #default="scope"><el-tag :type="actionType(scope.row.action)">{{actionLabel(scope.row.action)}}</el-tag></template></el-table-column>
    <el-table-column label="模块" width="120"><template #default="scope">{{resourceLabel(scope.row.resourceType)}}</template></el-table-column>
    <el-table-column prop="resourceId" label="数据 ID" width="90"/>
    <el-table-column prop="summary" label="内容" min-width="260"/>
    <el-table-column label="操作时间" width="180"><template #default="scope">{{formatDate(scope.row.createdAt)}}</template></el-table-column>
  </el-table>
  <div class="table-pagination"><span>共 {{total}} 条</span><el-pagination v-model:current-page="page" v-model:page-size="pageSize" :page-sizes="[20,50,100]" layout="sizes, prev, pager, next" :total="total" @current-change="load" @size-change="search"/></div>
</template>

<style scoped>
.audit-filters{display:grid;grid-template-columns:minmax(260px,480px) 150px 150px auto;gap:10px;margin-bottom:18px}.table-pagination{display:flex;justify-content:space-between;align-items:center;margin-top:18px;color:#7d8590;font-size:13px}@media(max-width:800px){.audit-filters{grid-template-columns:1fr}.table-pagination{align-items:flex-start;gap:12px;flex-direction:column}}
</style>
