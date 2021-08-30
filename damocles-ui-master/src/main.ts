import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus';
import 'element-plus/lib/theme-chalk/index.css';
import router from './router'
import locale from 'element-plus/lib/locale/lang/zh-cn' // element-plus中
// import locale from 'element-plus/lib/locale/zh-CN'   // elemet-ui 
import './style/iconfont/iconfont.css'

const app = createApp(App)
app.use(router)
app.use(ElementPlus, { locale })
app.mount('#app')

//自定义全局的指令
// app.directive('loadmore', loadmore.componentUpdated);
