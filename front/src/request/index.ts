import axios from 'axios';
import { useUserStore } from '@/stores/user';
import { ElMessage } from 'element-plus'
const request = axios.create({
	baseURL: 'http://localhost:8089',
	withCredentials: true, // 保留，允许携带Cookie（和后端跨域配置呼应）
});

// 请求拦截器：添加accessToken到请求头
request.interceptors.request.use(config => {
  const userStore = useUserStore();
  if (userStore.accessToken) {
    config.headers.Authorization = `Bearer ${userStore.accessToken}`; // 按后端要求的格式传递
  }
  return config;
});

// 响应拦截器：处理accessToken过期
request.interceptors.response.use(
  res => {
	  if(res.headers?.authorization&&res.headers.authorization.startsWith("Bearer ")){
		  const userStore = useUserStore();
		  const accessToken=res.headers.authorization.split(' ')[1];
		  userStore.setAccessToken(accessToken);
	  }
	  return Promise.resolve(res);
  },
  error => {
	const userStore = useUserStore();
	if (error.response?.status === 401) {
		const errorData = error.response.data;
		const errorCode = errorData.code;
		// 区分两种错误类型
		if (errorCode === "REFRESH_TOKEN_ERROR"||errorCode === "ACCESS_TOKEN_ERROR") {
			userStore.logout();
		}
		ElMessage.error("认证失败");
		return Promise.resolve("认证失败")
	}else if(error.response?.status===403){
		ElMessage.error("权限不足");
		return Promise.resolve("权限不足")
	}
	else if(error?.response){
		ElMessage.error(error.response.data);
		return Promise.resolve("服务器错误")
	}
	ElMessage.error("网络错误");
	return Promise.resolve("网络错误");
  }
);

export default request;