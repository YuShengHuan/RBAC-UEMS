<template>
  <div class="not-found-container">
    <!-- 404数字主体 -->
    <div class="not-found-number">
      <span class="num num-4">4</span>
      <span class="num num-0">0</span>
      <span class="num num-4">4</span>
    </div>

    <!-- 提示文本 -->
    <div class="not-found-text">
      <h2>页面走失啦～</h2>
      <p>你访问的页面不存在或没得权限访问，试试返回上一页吧！</p>
    </div>

    <!-- 返回首页按钮 -->
    <button class="back-home-btn" @click="goHome">
      返回上一页
    </button>

    <!-- 装饰元素 -->
    <div class="decorations">
      <div class="deco deco-1"></div>
      <div class="deco deco-2"></div>
      <div class="deco deco-3"></div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

// 返回首页逻辑
const goHome = () => {
  router.go(-1)
}
</script>
<style scoped>
/* 全局容器：全屏居中 */
.not-found-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(120deg, #f5f7fa 0%, #e4eaf5 100%);
  position: relative;
  padding: 20px;
  overflow: hidden;
}

/* 404数字样式 */
.not-found-number {
  font-size: clamp(8rem, 20vw, 15rem);
  font-weight: 900;
  margin-bottom: 20px;
  position: relative;
}

.num {
  background: linear-gradient(45deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  color: transparent;
  display: inline-block;
}

/* 数字悬浮动画 */
.num-4 {
  animation: float4 3s ease-in-out infinite;
}

.num-0 {
  animation: float0 3s ease-in-out infinite 0.5s;
}

@keyframes float4 {
  0%, 100% { transform: translateY(0) rotate(0); }
  50% { transform: translateY(-20px) rotate(2deg); }
}

@keyframes float0 {
  0%, 100% { transform: translateY(0) scale(1); }
  50% { transform: translateY(-15px) scale(1.05); }
}

/* 提示文本 */
.not-found-text {
  text-align: center;
  margin-bottom: 40px;
  color: #333;
}

.not-found-text h2 {
  font-size: clamp(1.5rem, 3vw, 2.5rem);
  margin-bottom: 10px;
  font-weight: 600;
}

.not-found-text p {
  font-size: clamp(1rem, 2vw, 1.2rem);
  color: #666;
  max-width: 500px;
}

/* 返回按钮 */
.back-home-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 30px;
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: white;
  border: none;
  border-radius: 50px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.back-home-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.back-home-btn:active {
  transform: translateY(0);
}

/* 装饰元素（动态漂浮） */
.decorations {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.deco {
  position: absolute;
  border-radius: 50%;
  background: rgba(118, 75, 162, 0.05);
}

.deco-1 {
  width: 150px;
  height: 150px;
  top: 10%;
  left: 10%;
  animation: float 8s ease-in-out infinite;
}

.deco-2 {
  width: 200px;
  height: 200px;
  bottom: 15%;
  right: 10%;
  animation: float 10s ease-in-out infinite 2s;
}

.deco-3 {
  width: 100px;
  height: 100px;
  top: 50%;
  right: 20%;
  animation: float 7s ease-in-out infinite 1s;
}
</style>