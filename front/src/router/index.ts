import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '@/stores/user.ts';
import Home from '../components/common/layout/Home.vue'
import {adminConstantRoutes } from './adminConstantRoutes'
import {frontConstantRoutes} from './frontConstantRoutes'
const constantRoutes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path:'/login',
    name:'login',
    component:()=>import('../view/common/login/Login.vue')
  },
  {
    path:'/forgot-password',
    name:'forgotPassword',
    component:()=>import('../view/common/login/ForgotPassword.vue')
  },
  {
    path:'/home/admin',
    component:Home,
    children:[
      ...adminConstantRoutes,
    ]
  },
  {
    path:'/home/front',
    component:Home,
    children:[
      ...frontConstantRoutes
    ]
  },
  {
    path: '/404',
    component: () => import('@/view/common/error/404.vue'),
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes: constantRoutes,
});

//导航守卫：校验权限
router.beforeEach(async(to, from, next) => {
  const userStore = useUserStore();
  const accessToken=userStore.accessToken;
  const hasPerm=to.meta?.permissions?.some(perm =>
      userStore.hasPermission(perm)
  )
  // 1. 无需认证的页面直接放行
  if (!to.meta?.requiresAuth) {
    return next();
  }

  // 2. 需要认证但未登录，重定向到登录页
  if (!accessToken) {
    return next({
      path: '/login'
    });
  }

  // 3. 已登录但权限不足，显示错误页或提示
  if (!hasPerm) {
    return next({ path: '/404'}); // 跳转到404页面
  }
  next();
});

export default router;
