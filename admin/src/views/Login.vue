<script setup lang="ts">
import {reactive,ref} from 'vue';import {useRouter} from 'vue-router';import {api,loadCsrf} from '../api.ts'
const router=useRouter();const form=reactive({username:'admin',password:''});const loading=ref(false);const login=async()=>{loading.value=true;try{await loadCsrf();const body=new URLSearchParams(form);await api.post('/auth/login',body,{headers:{'Content-Type':'application/x-www-form-urlencoded'}});router.push('/')}finally{loading.value=false}}
</script>
<template><div class="login"><el-form class="login-card" @submit.prevent="login"><h1>管理后台</h1><el-form-item><el-input v-model="form.username" placeholder="用户名"/></el-form-item><el-form-item><el-input v-model="form.password" type="password" show-password placeholder="密码"/></el-form-item><el-button native-type="submit" type="primary" :loading="loading" style="width:100%">登录</el-button></el-form></div></template>
