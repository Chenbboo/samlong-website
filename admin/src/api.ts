import axios from 'axios'
export const api=axios.create({baseURL:'/api',withCredentials:true})
let csrf=''
export async function loadCsrf(){const {data}=await api.get('/auth/csrf');csrf=data.token;api.defaults.headers.common['X-XSRF-TOKEN']=csrf}
api.interceptors.response.use(r=>r,async error=>{if(error.response?.status===403&&!error.config.__retried){error.config.__retried=true;await loadCsrf();return api(error.config)}return Promise.reject(error)})
