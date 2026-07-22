<script setup lang="ts">
import {ref} from 'vue'
import {api} from '../api.ts'
const emit=defineEmits<{select:[url:string]}>();const visible=ref(false);const loading=ref(false);const items=ref<any[]>([])
async function open(){visible.value=true;loading.value=true;try{items.value=(await api.get('/uploads')).data||[]}finally{loading.value=false}}
function choose(url:string){emit('select',url);visible.value=false}
</script>
<template><el-button plain @click="open">从素材库选择</el-button><el-dialog v-model="visible" title="选择图片素材" width="860"><div v-loading="loading" class="picker-grid"><button v-for="item in items" :key="item.name" type="button" @click="choose(item.url)"><img :src="item.thumbnailUrl" :alt="item.name"><span>{{item.name}}</span></button><el-empty v-if="!loading&&!items.length" description="暂无图片素材"/></div></el-dialog></template>
<style scoped>.picker-grid{display:grid;grid-template-columns:repeat(4,1fr);gap:14px;min-height:180px}.picker-grid button{border:1px solid #e1e5ea;border-radius:9px;background:#fff;padding:7px;cursor:pointer;text-align:left}.picker-grid button:hover{border-color:#0878ee}.picker-grid img{display:block;width:100%;height:105px;object-fit:cover;border-radius:6px}.picker-grid span{display:block;margin-top:7px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;font-size:12px;color:#69717c}@media(max-width:700px){.picker-grid{grid-template-columns:repeat(2,1fr)}}</style>
