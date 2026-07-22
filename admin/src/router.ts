import {createRouter,createWebHistory} from 'vue-router'
import Login from './views/Login.vue'
import Dashboard from './views/Dashboard.vue'
import Resource from './views/Resource.vue'
import AuditLogs from './views/AuditLogs.vue'
import MediaLibrary from './views/MediaLibrary.vue'
import FooterSettings from './views/FooterSettings.vue'

const router=createRouter({history:createWebHistory('/admin/'),routes:[
  {path:'/login',component:Login,meta:{public:true}},
  {path:'/',component:Dashboard},
  {path:'/products',component:Resource,props:{kind:'products',title:'车型管理'}},
  {path:'/news',component:Resource,props:{kind:'news',title:'新闻管理'}},
  {path:'/slides',component:Resource,props:{kind:'slides',title:'轮播图管理'}},
  {path:'/bookings',component:Resource,props:{kind:'bookings',title:'试驾预约'}},
  {path:'/media',component:MediaLibrary},
  {path:'/footer',component:FooterSettings},
  {path:'/audit-logs',component:AuditLogs}
]})
router.beforeEach(async to=>{if(to.meta.public)return;try{await fetch('/api/auth/me',{credentials:'include'}).then(r=>{if(!r.ok)throw 0})}catch{return '/login'}})
export default router
