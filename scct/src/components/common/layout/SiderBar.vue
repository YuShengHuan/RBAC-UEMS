<template>
  <div class="aside-page" :style="{'background-color': theme.bgColor, 'border-right': `1px solid ${theme.borderColor}`}">
    <!-- 侧边栏Logo区域 -->
    <div class="sidebar-logo"
         :style="{'background-color': theme.bgColor}"
    >
      <el-icon class="logo-icon" size="50">
        <el-image
            :src="logoImg"
        />
      </el-icon>
      <span class="logo-text"
            :style="{
              color: theme.logoTextColor
            }">
        {{ logoText }}
      </span>
    </div>

    <el-menu
        :router="true"
        :default-active="currentRoutePath"
        class="sidebar-menu"
        :background-color="theme.bgColor"
        :text-color="theme.textColor"
        :active-text-color="theme.activeTextColor"
        unique-opened
        :collapse-transition="false"
    >
      <template v-for="route in routes" :key="route.path">
        <el-sub-menu
            :index="route.path"
            v-permission="route.meta?.permissions"
            :class="{'no-children':!route?.children,'active-no-children':!route?.children&&currentRoutePath.startsWith(route.path)}"
            @click="handleClickTopMenu(!route?.children,route.path)"
        >
          <template #title>
            <el-icon class="menu-icon" size="60">
              <el-image
                  :src="route.meta?.icon"
              />
            </el-icon>
            <span class="menu-text">{{ route.meta.title }}</span>
          </template>

          <template v-for="child in route?.children" :key="child.path">
            <el-menu-item
                v-permission="child.meta?.permissions"
                :index="route.path + (child.path.length > 0 ? '/' : '') + child.path"
                class="sub-menu-item"
            >
              <span class="sub-menu-text">{{ child.meta.title }}</span>
            </el-menu-item>
          </template>
        </el-sub-menu>
      </template>
    </el-menu>
  </div>
</template>

<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
import { computed} from "vue";
import logoImg from '../../../assets/logo.png';
import { useSystemStore } from "../../../stores/system";

const router = useRouter()
const route = useRoute()
const systemStore=useSystemStore()


const routePath = computed(() => systemStore.currentThemeConfig.routePath);
const logoText = computed(() =>  systemStore.currentThemeConfig.logoText);
const theme=computed(
    ()=>systemStore.currentThemeConfig
)
// 获取当前路由路径
const currentRoutePath = computed(() => route.path);

// 处理无孩子菜单的点击
const handleClickTopMenu = (isTop: boolean, path: string) => {
  if (isTop) {
    router.replace(path)
  }
}
// 根据当前的 routePath 获取对应的子路由
const routes = computed(() => {
  return router.getRoutes().find(r => r.path === routePath.value)?.children || []
})
</script>

<style scoped>
/* 侧边栏容器 */
.aside-page {
  width: 100%;
  height: 100%;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
}

/* 侧边栏Logo */
.sidebar-logo {
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  transition: border-color 0.3s ease;
}
.logo-icon {
  font-size: 24px;
  margin-right: 12px;
  transition: color 0.3s ease;
}

.logo-text {
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s ease;
}

/* 菜单容器 */
.sidebar-menu {
  border-right: none;
  flex: 1;
  padding: 8px 0;
}

/* 一级菜单样式 */
:deep(.el-sub-menu__title) {
  height: 50px !important;
  line-height: 50px !important;
  padding: 0 20px !important;
  transition: all 0.2s ease;
}

:deep(.el-sub-menu__title:hover) {
  transition: background-color 0.2s ease, color 0.2s ease;
}

.menu-icon {
  font-size: 18px !important;
  margin-right: 10px;
  transition: color 0.3s ease;
}

.menu-text {
  font-size: 14px;
  transition: all 0.2s ease;
}

/* 二级菜单样式 */
.sub-menu-item {
  display: flex;
  align-items: center;
}

.sub-menu-icon {
  font-size: 14px !important;
  margin-right: 8px;
  transition: color 0.3s ease;
}

.sub-menu-text {
  font-size: 13px;
}

:deep(.el-menu-item) {
  height: 44px !important;
  line-height: 44px !important;
  padding: 0 20px 0 48px !important;
  transition: all 0.2s ease;
}

:deep(.el-menu-item:hover) {
  transition: background-color 0.2s ease, color 0.2s ease;
}

/* 激活菜单样式 */
:deep(.el-sub-menu .el-menu .is-active),
:deep(.no-children.is-active .el-sub-menu__title),
.active-no-children {
  font-weight: 500;
  transition: all 0.2s ease;
}

/* 移除无孩子菜单的箭头 */
.no-children :deep(.el-sub-menu__title .el-sub-menu__icon-arrow) {
  display: none !important;
  visibility: hidden !important;
}

.no-children :deep(.el-sub-menu__title) {
  padding-right: 20px !important;
}

/* 滚动条美化 */
.aside-page::-webkit-scrollbar {
  width: 4px;
}

.aside-page::-webkit-scrollbar-thumb {
  border-radius: 2px;
  transition: background-color 0.2s;
}

.aside-page::-webkit-scrollbar-track {
  transition: background-color 0.2s;
}

/* 菜单折叠时优化 */
:deep(.el-menu--collapse) .menu-text,
:deep(.el-menu--collapse) .sub-menu-text,
:deep(.el-menu--collapse) .logo-text {
  display: none;
}

:deep(.el-menu--collapse .el-menu-item) {
  padding: 0 20px !important;
  justify-content: center;
}

:deep(.el-menu--collapse .el-sub-menu__title) {
  justify-content: center;
}

/* --- 动态样式 --- */
/* 这些样式会根据 theme 对象的变化而动态更新 */
.sidebar-logo {
  border-bottom-color: v-bind('theme.borderColor');
}
.logo-icon {
  color: v-bind('theme.activeTextColor');
}
:deep(.el-sub-menu__title) {
  color: v-bind('theme.textColor') !important;
}
:deep(.el-sub-menu__title:hover) {
  background-color: v-bind('theme.hoverBgColor') !important;
  color: v-bind('theme.activeTextColor') !important;
}
:deep(.el-menu-item) {
  color: v-bind('theme.textColor') !important;
}
:deep(.el-menu-item:hover) {
  background-color: v-bind('theme.hoverBgColor') !important;
  color: v-bind('theme.activeTextColor') !important;
}
:deep(.el-sub-menu .el-menu .is-active),
:deep(.no-children.is-active .el-sub-menu__title),
.active-no-children {
  background-color: v-bind('theme.activeBgColor') !important;
  color: v-bind('theme.activeTextColor') !important;
  border-left-style: solid;
  border-left-width: 5px;
  border-left-color: v-bind('theme.activeBorderColor');
}
.aside-page::-webkit-scrollbar-thumb {
  background-color: v-bind('theme.scrollbarThumbColor');
}
.aside-page::-webkit-scrollbar-thumb:hover {
  background-color: v-bind('theme.activeTextColor');
}
.aside-page::-webkit-scrollbar-track {
  background-color: v-bind('theme.scrollbarTrackColor');
}
</style>