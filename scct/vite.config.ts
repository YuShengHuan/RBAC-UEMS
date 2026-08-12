import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import * as path from 'path'


export default defineConfig({
  plugins: [
      vue(),
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src') // 关键配置
    },
    extensions: ['.js', '.ts', '.vue', '.json']
  },
  server: {
    host: '0.0.0.0',        // 监听所有网络接口
    port: 5173,             // 端口号
    open: false,            // 不自动打开浏览器
    cors: true,             // 允许跨域
    hmr: {                  // 配置热更新
      clientPort: 5173,     // 热更新客户端端口
      overlay: false        // 关闭错误覆盖层
    },
    proxy: {
      '/api': {
        target: 'http://localhost:8089', // 本地后端地址
        changeOrigin: true, // 开启跨域
      }
    }
  }
})
