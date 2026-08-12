import { createApp } from 'vue'
import './style.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import ExportDataView  from './components/common/table/ExportDataView.vue'
import router from './router'
import { permission} from './directives/permission'
import App from './App.vue'
import {createPinia} from "pinia";
import * as echarts from 'echarts';
import VueECharts from 'vue-echarts';
const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.component('ExportDataView', ExportDataView);
const pinia = createPinia()

// 注册持久化插件
pinia.use(piniaPluginPersistedstate)
app.use(pinia)  // 挂载Pinia
//注册permission role钩子
app.directive('permission', permission)
//注册chart
app.component('v-chart', VueECharts);
// 全局注入 ECharts 实例（可选）
app.config.globalProperties.$echarts = echarts;
//注册路由
app.use(router)
//注册ElementPlus
app.use(ElementPlus)
app.mount('#app')

