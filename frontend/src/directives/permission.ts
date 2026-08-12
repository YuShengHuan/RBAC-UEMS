// directives/permission.js
import { useUserStore } from '@/stores/user'
import { watch } from 'vue'

export const permission = {
  created(el, binding) {
    const userStore = useUserStore()

    // 权限判断逻辑（提取为独立函数）
    const checkPermission = () => {
      const requiredPermissions = Array.isArray(binding.value)
          ? binding.value
          : [binding.value]

      let hasPermission = false

      if (binding.modifiers.all) {
        // 必须满足所有权限
        hasPermission = requiredPermissions.every(perm =>
            userStore.hasPermission(perm)
        )
      } else {
        // 满足任意一个权限即可（默认）
        hasPermission = requiredPermissions.some(perm =>
            userStore.hasPermission(perm)
        )
      }

      // 控制元素显示/隐藏
      el.style.display = hasPermission ? '' : 'none'
    }

    // 1. 初始检查一次
    checkPermission()

    // 2. 监听权限数据源变化（关键：实现实时响应）
    // 假设 userStore 中存储权限的字段是 permissions（根据实际情况修改）
    const unwatch = watch(
        () => userStore.currentPermissions, // 监听权限数组的变化
        () => {
          checkPermission() // 权限变化时重新检查
        },
        { deep: true } // 深度监听（如果 permissions 是数组/对象）
    )

    // 3. 保存检查函数和监听器清理函数，用于后续清理
    el._checkPermission = checkPermission
    el._unwatchPermission = unwatch
  },

  // 当指令值变化时（如 v-permission 的值动态改变）
  updated(el, binding) {
    if (el._checkPermission) {
      el._checkPermission()
    }
  },

  // 组件卸载时清理监听器
  unmounted(el) {
    // 移除权限监听
    if (el._unwatchPermission) {
      el._unwatchPermission()
    }
    // 清理缓存的函数
    el._checkPermission = null
    el._unwatchPermission = null
  }
}