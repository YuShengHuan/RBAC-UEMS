<template>
  <div class="forgot-password-container">
    <el-card class="forgot-card" >
      <div class="notice-content">此为邮箱验证重置通道，若没有绑定邮箱，请及时联系管理员重置密码</div>
      <h2 class="title">忘记密码</h2>
      <!-- 步骤指示器 -->
      <el-steps :active="activeStep" class="steps" finish-status="success">
        <el-step title="输入账号" />
        <el-step title="验证邮箱" />
        <el-step title="重置密码" />
      </el-steps>

      <!-- 步骤1：输入账号 -->
      <div v-if="activeStep === 0" class="step-content">
        <el-form :model="form" :rules="step1Rules" ref="step1FormRef" label-width="80px">
          <el-form-item label="账号" prop="username" class="el-form-item">
            <el-input
                v-model="form.username"
                placeholder="请输入您的登录账号"
            />
          </el-form-item>
          <el-form-item>
            <el-button
                type="primary"
                @click="handleStep1Submit"
            >
              下一步
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 步骤2：验证邮箱（脱敏展示+输入完整邮箱） -->
      <div v-if="activeStep === 1" class="step-content">
        <el-form :model="form" :rules="step2Rules" ref="step2FormRef" label-width="80px">
          <el-form-item label="绑定邮箱" class="el-form-item">
            <div class="desensitize-email" v-if="desensitizedEmail">
              <span class="highlight">{{ desensitizedEmail }}</span>
            </div>
          </el-form-item>
          <el-form-item label="邮箱"  prop="email" class="el-form-item" ref="emailItem">
            <el-input
                v-model="form.email"
                placeholder="请输入完整绑定邮箱"
            />
          </el-form-item>
          <el-form-item label="验证码" prop="code" class="el-form-item">
                <el-input
                    v-model="form.code"
                    placeholder="请输入验证码"
                >
                  <template #suffix>
                    <el-button
                        class="full-width-btn"
                        :disabled="countdown > 0"
                        @click="getVerifyCode"
                    >
                      {{ countdown > 0 ? `${countdown}s后重新获取` : '获取验证码' }}
                    </el-button>
                  </template>
                </el-input>
          </el-form-item>
          <el-form-item class="el-form-item">
            <el-button
                type="default"
                class="mr-2"
                @click="activeStep = 0"
            >
              上一步
            </el-button>
            <el-button
                type="primary"
                @click="handleStep2Submit"
            >
              下一步
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 步骤3：修改密码 -->
      <div v-if="activeStep === 2" class="step-content" >
        <el-form :model="form" :rules="step3Rules" ref="step3FormRef" label-width="80px">
          <el-form-item label="新密码" prop="newPassword" class="el-form-item">
            <el-input
                v-model="form.newPassword"
                type="password"
                placeholder="请输入新密码（6-20位）"
                show-password
            />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword" class="el-form-item">
            <el-input
                v-model="form.confirmPassword"
                type="password"
                placeholder="请再次输入新密码"
                show-password
            />
          </el-form-item>
          <el-form-item>
            <el-button
                type="default"
                class="mr-2"
                @click="activeStep = 1"
            >
              上一步
            </el-button>
            <el-button
                type="primary"
                @click="handleStep3Submit"
            >
              确认重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import {ref, reactive, onUnmounted,} from 'vue';
import {
  ElForm,
  ElFormItem,
  ElInput,
  ElButton,
  ElSteps,
  ElStep,
  ElCard,
  ElRow,
  ElCol,
  ElMessage,
  FormItemInstance
} from 'element-plus';
import request from "../../../request";
import {useRouter} from "vue-router";

const router=useRouter()
// 表单引用
const step1FormRef = ref<InstanceType<typeof ElForm>>();
const step2FormRef = ref<InstanceType<typeof ElForm>>();
const step3FormRef = ref<InstanceType<typeof ElForm>>();

// 步骤状态
const activeStep = ref(0);
// 倒计时
const countdown = ref(0)
let timer = null // 保存定时器ID，用于清理

// 组件卸载时清理定时器（防止内存泄漏）
onUnmounted(() => {
  if (timer) clearInterval(timer)
})
// 脱敏邮箱
const desensitizedEmail = ref('');

// 表单数据
const form = reactive({
  username: '',
  email: '',
  code: '',
  newPassword: '',
  confirmPassword: ''
});
const resetStatus=()=>{
   Object.assign(form,{
     username: '',
     email: '',
     code: '',
     newPassword: '',
     confirmPassword: ''}
   )
  activeStep.value=0
  countdown.value=0
  desensitizedEmail.value=''
}
// 步骤1校验规则
const step1Rules = reactive({
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ]
});

// 步骤2校验规则
const step2Rules = reactive({
  email: [
    { required: true, message: '请输入完整邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  code: [
    { required: form.email.length>0, message: '请输入验证码', trigger: 'blur' },
    { min: 6, max: 6, message: '验证码为6位', trigger: 'blur' }
  ]
});

// 步骤3校验规则
const step3Rules = reactive({
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为6-20位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.newPassword) {
          callback(new Error('两次输入密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ]
});

// 步骤1提交（获取脱敏邮箱）
const handleStep1Submit = async () => {
  await step1FormRef.value?.validate();
  const res=await request.post(`/api/forgot-password`,
      {
        userAccount:form.username,
        step:activeStep.value
      }
  )
  if(res.status===200){
    desensitizedEmail.value=res.data.email
    activeStep.value = 1;
    ElMessage.success('账号验证通过');
    return
  }
};

const emailItem=ref<FormItemInstance>()
// 获取验证码
const getVerifyCode = async () => {
  // 防止重复点击：倒计时中直接返回
  if (countdown.value > 0) return

  // 3. 开始倒计时
  countdown.value = 60
  timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
      timer = null // 重置定时器ID
    }
  }, 1000)

  // 2. 先同步校验邮箱（避免async回调的兼容性问题）
  const valid = await emailItem.value?.validate()
  if (!valid) return

  // 4. 发送请求（加try/catch处理异常）
  try {
    const res = await request.post('/api/forgot-password', {
      userAccount: form.username,
      email: form.email,
      step: activeStep.value
    })
    if (res.status === 200) {
      ElMessage.success('验证码已发送至您的邮箱')
    }
  } catch (error) {
    // 请求失败：重置倒计时+提示错误
    clearInterval(timer)
    timer = null
    countdown.value = 0
    ElMessage.error('验证码发送失败，请稍后重试')
  }
};

// 步骤2提交（验证邮箱和验证码）
const handleStep2Submit = async () => {
  await step2FormRef.value?.validate();
  const res=await request.post(`/api/forgot-password`,
      {
        userAccount:form.username,
        email:form.email,
        code:form.code,
        step:activeStep.value}
  )
  if(res.status===200){
    ElMessage.success('邮箱验证通过');
    countdown.value=0
    activeStep.value = 2;
  }
};

// 步骤3提交（重置密码）
const handleStep3Submit = async () => {
  await step3FormRef.value?.validate();
  const res=await request.post(`/api/forgot-password`,
      {userAccount:form.username,
        email:form.email,
        code:form.code,
        password:form.confirmPassword,
        step:activeStep.value
      }
  )
  if(res.status===200){
    ElMessage.success('密码重置成功！');
    //重置状态回归原始
    resetStatus()
    router.push("/login")
  }
};
</script>

<style scoped>
.forgot-password-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-color: #f5f7fa;
  padding: 20px;
}
.notice-content{
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 17px;
  color: rgba(211, 19, 19, 0.8);
}
.forgot-card {
  padding: 30px;
  box-sizing: border-box;
}

.title {
  text-align: center;
  margin-bottom: 30px;
  color: #1989fa;
  font-size: 20px;
  font-weight: 600;
}

.steps {
  margin-bottom: 30px;
}

.step-content {
  margin-top: 20px;
}

.full-width-btn {
  border: none;
}
.full-width-btn:hover {
  background: #FFFFFF;
}

.desensitize-email {
  display: flex;
  align-items: center;
  background-color: #f5fafe;
  width: 100%;
  padding: 10px 10px;
}
.el-form-item{
   display: flex;
  align-items: center;
}
.highlight {
  color: #1989fa;
  font-weight: 500;
  margin: 0 8px;
}
:deep(.el-input__wrapper){
  height: 40px;
}
.mr-2 {
  margin-right: 8px;
}
</style>