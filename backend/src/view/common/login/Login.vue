<template>
  <VerificationGuard />
  <div class="login-container">
    <!-- 渐变背景层 -->
    <div class="login-bg"></div>

    <!-- 气泡背景：使用 v-for 循环生成 -->
    <div class="bubbles-container">
      <div
          v-for="bubble in bubbles"
          :key="bubble.id"
          class="bubble"
          :style="bubble.style"
      ></div>
    </div>

    <!-- 登录表单卡片 -->
    <div class="login-card">
      <div class="login-header">
        <h2 class="login-title">实验教学管理系统</h2>
      </div>

      <!-- 登录表单 (内容不变) -->
      <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          label-width="0"
          class="login-form"
      >
        <el-form-item prop="username">
          <el-input
              v-model="loginForm.userAccount"
              placeholder="请输入账号（学号/工号）或者邮箱"
              :disabled="isLoading"
          >
            <template #prepend>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
              v-model="loginForm.userPassword"
              :type="'password'"
              show-password
              placeholder="请输入密码"
              :disabled="isLoading"
          >
            <template #prepend>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <div class="login-form-footer">
          <el-checkbox
              v-model="loginForm.remember"
              :disabled="isLoading"
              label="记住密码"
          ></el-checkbox>
          <el-link
              type="primary"
              @click="handleForgotPassword"
              :disabled="isLoading"
              class="forgot-link"
          >
            忘记密码？
          </el-link>
        </div>

        <el-form-item>
          <el-button
              type="primary"
              size="default"
              class="login-btn"
              @click="handleLogin"
              :loading="isLoading"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import VerificationGuard from '../login/VerificationGuard.vue'
import { User, Lock } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ElMessage } from "element-plus";
import request from "@/request/index.ts";
import { useUserStore } from "@/stores/user.ts";
import { useSystemStore } from "@/stores/system.js";

// 路由和状态管理
const router = useRouter()
const userStore = useUserStore()
const systemStore = useSystemStore()

// 表单相关
const loginFormRef = ref(null)
const isLoading = ref(false)
const loginForm = reactive({
  userAccount: '',
  userPassword: '',
  remember: false
})
const loginRules = reactive({
  userAccount: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度需在6-20位之间', trigger: 'blur' }
  ]
})

// --- 气泡背景逻辑 ---
const bubbles = ref([]); // 响应式数组，存储每个气泡的样式

// 生成随机气泡的函数
const generateBubbles = () => {
  const numberOfBubbles = 30;
  const bubbleColors = [
    'rgba(22, 93, 255, 0.25)',
    'rgba(16, 185, 129, 0.25)',
    'rgba(236, 72, 153, 0.25)',
    'rgba(249, 115, 22, 0.25)',
    'rgba(139, 92, 246, 0.25)'
  ];

  const newBubbles = [];
  for (let i = 0; i < numberOfBubbles; i++) {
    const size = Math.random() * 60 + 10; // 10px ~ 70px
    const duration = Math.random() * 20 + 10; // 10s ~ 30s
    const delay = Math.random() * 5; // 0s ~ 5s delay

    newBubbles.push({
      id: i,
      style: {
        backgroundColor: bubbleColors[Math.floor(Math.random() * bubbleColors.length)],
        width: `${size}px`,
        height: `${size}px`,
        left: `${Math.random() * 100}%`, // 随机水平位置
        bottom: `-${size}px`, // 从底部开始
        animationDuration: `${duration}s`,
        animationDelay: `${delay}s`,
      }
    });
  }
  bubbles.value = newBubbles;
};

// 组件挂载时生成气泡
onMounted(() => {
  // 读取记住的密码
  const savedUser = userStore.loginBox
  if (savedUser) {
    Object.assign(loginForm, savedUser);
  }

  // 生成气泡
  generateBubbles();
});

// --- 事件处理 ---
const handleLogin = async () => {
  isLoading.value = true
  try {
    // 表单验证
    await loginFormRef.value.validate();

    const res = await request.post("/api/login", {
      userAccount: loginForm.userAccount,
      userPassword: loginForm.userPassword
    })

    if (res.status === 200) {
      userStore.setUserInfo(res.data)

      // 记住密码
      loginForm.remember ? userStore.setLoginBox(loginForm) : userStore.setLoginBox({});

      ElMessage.success('登录成功！')

      // 根据角色跳转
      if (userStore.currentRole.includes("R") || userStore.currentRole.includes("A")) {
        systemStore.setCurrenTheme('dark')
        await router.push("/home/admin")
      } else {
        systemStore.setCurrenTheme('light')
        await router.push("/home/front")
      }
    }
  } catch (error) {
    // 表单验证失败或网络错误
    ElMessage.error(error.message || '登录失败，请检查账号或密码！')
  } finally {
    isLoading.value = false
  }
}

const handleForgotPassword = () => {
  router.push('/forgot-password')
}
</script>

<style scoped>
/* 登录容器 */
.login-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden; /* 隐藏超出容器的气泡 */
  background-color: #f0f2f5;
}

/* 渐变背景层 */
.login-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: linear-gradient(135deg, #e8f4f8 0%, #f0f8fb 100%);
  z-index: 1;
}

/* 气泡容器 */
.bubbles-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 2; /* 位于渐变背景之上，登录卡片之下 */
  pointer-events: none; /* 让鼠标事件穿透气泡层，不影响点击登录按钮 */
}

/* 单个气泡样式 */
.bubble {
  position: absolute;
  border-radius: 50%;
  opacity: 0.8;
  /* 应用浮动动画 */
  animation: floatBubble linear infinite;
}

/* 气泡浮动动画 */
@keyframes floatBubble {
  0% {
    transform: translateY(0) translateX(0) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 0.8;
  }
  90% {
    opacity: 0.8;
  }
  100% {
    transform: translateY(-100vh) translateX(20px) rotate(360deg);
    opacity: 0;
  }
}

/* 登录卡片 */
.login-card {
  width: 100%;
  max-width: 420px;
  padding: 40px 30px;
  background-color: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  z-index: 3; /* 确保在最上层 */
  backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.7);
}

/* ... (其他表单相关样式保持不变) ... */
.login-header {
  text-align: center;
  margin-bottom: 30px;
}
.login-title {
  font-size: 24px;
  font-weight: 600;
  color: #1D2129;
  margin-bottom: 8px;
}
.login-form { width: 100%; }
.login-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  background-color: #165DFF;
  border-color: #165DFF;
}
.login-btn:hover {
  background-color: #0F48C9;
  border-color: #0F48C9;
}
.login-form-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  font-size: 14px;
}
.forgot-link { color: #165DFF; }
input:-webkit-autofill,
input:-webkit-autofill:hover,
input:-webkit-autofill:focus,
input:-webkit-autofill:active {
  transition: background-color 5000s ease-in-out 0s;
  -webkit-text-fill-color: #333 !important;
}
</style>