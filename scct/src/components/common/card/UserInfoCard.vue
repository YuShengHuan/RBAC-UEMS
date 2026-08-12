<template>
  <el-drawer
      v-model="isVisible"
      title="用户信息"
      direction="rtl"
      :size="`${drawerWidth}%`"
  >
    <div class="user-profile-container">
      <!-- 信息展示卡片 -->
      <el-card shadow="hover" class="info-card" v-show="!isEditing">
        <div class="info-header">
          <span>当前信息</span>
          <el-button
              type="primary"
              icon="Edit"
              size="small"
              @click="enterEditMode"
              :disabled="isEditing"
          >
            基础信息编辑
          </el-button>
        </div>

        <el-divider></el-divider>

        <div class="info-grid">
          <div class="info-item">
            <span class="label">账号：</span>
            <span class="value">{{ userData.userAccount }}</span>
          </div>
          <div class="info-item">
            <span class="label">姓名：</span>
            <span class="value">{{ userData.realName }}</span>
          </div>
          <div class="info-item">
            <span class="label">性别：</span>
            <span class="value">{{ findLabelByValue(genderOptions,userData.gender)}}</span>
          </div>
          <div class="info-item">
            <span class="label">联系电话：</span>
            <span class="value">{{ userData.phone }}</span>
          </div>
          <div class="info-item">
            <span class="label">电子邮箱：</span>
            <span class="value">{{ userData.email }}</span>
          </div>
          <div class="info-item">
            <span class="label">用户类型：</span>
            <span class="value">{{findLabelByValue(userTypeOptions,userData.userType) }}</span>
          </div>

          <!-- 根据 userType 条件显示 -->
          <div class="info-item" v-if="userData.userType===2">
            <span class="label">所在分院：</span>
            <span class="value">{{ findLabelByValue(deptOptions,userData.deptId) }}</span>
          </div>
          <div class="info-item" v-else-if="userData.userType === 3">
            <span class="label">所在班级：</span>
            <span class="value">{{findLabelByValue(classOptions,userData.classId)}}</span>
          </div>

          <div class="info-item">
            <span class="label">状态：</span>
            <span class="value">
              <el-tag :type="userData.userStatus === 1 ? 'success' : 'danger'">
                {{ findLabelByValue(userStatusOptions,userData.userStatus)}}
              </el-tag>
            </span>
          </div>
        </div>
      </el-card>

      <!-- 编辑表单卡片 -->
      <el-card shadow="hover" class="edit-card"  v-show="isEditing">
        <div class="edit-header">
          <span>编辑信息</span>
        </div>
        <el-divider></el-divider>
        <el-form :model="formData" label-width="75px" class="edit-form">
          <el-form-item label="姓名" prop="realName" class="el-form-item">
            <el-input v-model="formData.realName" placeholder="请输入姓名"></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-select v-model="formData.gender" placeholder="请选择性别">
              <el-option
                  v-for="item in genderOptions"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="联系电话" prop="phone" class="el-form-item">
            <el-input v-model="formData.phone" placeholder="请输入联系电话"></el-input>
          </el-form-item>
          <!-- 动态显示的表单项 -->
          <el-form-item
              label="所在分院"
              prop="deptId"
              class="el-form-item"
              v-if="userData.userType===2"
          >
            <el-select v-model="formData.deptId" placeholder="请选择分院">
              <el-option
                  v-for="item in deptOptions"
                  :label="item.label"
                  :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item
              label="所在班级"
              prop="classId"
              class="el-form-item"
              v-else-if="userData.userType === 3"
          >
            <el-select v-model="formData.classId" placeholder="请选择班级">
              <el-option
                  v-for="item in classOptions"
                  :label="item.label"
                  :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="saveChanges">保存</el-button>
            <el-button @click="cancelEdit">取消</el-button>
          </el-form-item>
        </el-form>
      </el-card>
      <el-card shadow="hover" class="edit-especially-card" v-show="!isEditing">
        <div class="especially-header">
          <span>其他操作</span>
        </div>
        <el-divider></el-divider>

        <!-- 邮箱/密码操作按钮组 -->
        <div class="action-button-group">
          <el-button
              class="action-button email-button"
              icon="el-icon-edit"
              type="primary"
              @click="openEmailDialog = true"
          >
            修改邮箱
          </el-button>
          <el-button
              class="action-button password-button"
              icon="el-icon-key"
              type="success"
              @click="openPwdDialog = true"
          >
            修改密码
          </el-button>
        </div>

        <!--  修改邮箱弹框 -->
        <el-dialog
            title="修改邮箱"
            v-model="openEmailDialog"
            align-center
            width="95%"
            :close-on-click-modal="false"
            class="email-pwd-dialog"
        >
          <el-form :model="emailForm">
            <el-form-item label="新邮箱" prop="newEmail" class="el-form-item">
              <el-input
                  v-model="emailForm.newEmail"
                  placeholder="请输入新邮箱"
                  clearable
              ></el-input>
            </el-form-item>
            <el-form-item label="验证码" prop="code" class="el-form-item">
                <el-input
                    v-model="emailForm.code"
                    placeholder="请输入验证码"
                    clearable
                >
                  <template #suffix>
                    <el-button
                        type="text"
                        class="get-code-btn"
                        @click="getEmailCode"
                        :disabled="codeDisabled"
                    >
                      {{ codeText }}
                    </el-button>
                  </template>
                </el-input>
            </el-form-item>
          </el-form>
          <template #footer>
            <el-button @click="openEmailDialog = false">取消</el-button>
            <el-button type="primary" @click="confirmEditEmail">确认修改</el-button>
          </template>
        </el-dialog>

        <!--  修改密码弹框 -->
        <el-dialog
            title="修改密码"
            v-model="openPwdDialog"
            width="95%"
            align-center
            :close-on-click-modal="false"
            class="email-pwd-dialog"
        >
          <el-form :model="pwdForm" :label-width="80">
            <el-form-item label="新密码" prop="newPwd" class="el-form-item">
              <el-input
                  v-model="pwdForm.newPwd"
                  type="password"
                  placeholder="请输入新密码"
                  show-password
              ></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPwd" class="el-form-item">
              <el-input
                  v-model="pwdForm.confirmPwd"
                  type="password"
                  placeholder="请再次输入新密码"
                  show-password
              ></el-input>
            </el-form-item>
          </el-form>
          <template #footer>
            <el-button @click="openPwdDialog = false">取消</el-button>
            <el-button type="primary" @click="confirmEditPwd">确认修改</el-button>
          </template>
        </el-dialog>
      </el-card>
    </div>
  </el-drawer>
</template>

<script setup>
import {ref, defineEmits, defineProps, onMounted, watch, onUnmounted, reactive,computed} from 'vue';
import { ElMessage } from 'element-plus';
import request from "@/request/index.js";
import {useUserStore} from "@/stores/user.js";
import {copyChangeKey, copySameKey, findLabelByValue} from "@/utils/commonUtil.js";
import {userTypeOptions,genderOptions,userStatusOptions} from "@/utils/globalOptionsUtil.js";
const props=defineProps({
  isOpenUserInfoDrawer:{
     type:Boolean
  },
  drawerWidth:{
     type:Number,
     default:95
  }
})
const emit=defineEmits(['update:isOpenUserInfoDrawer'])
const isVisible=ref(props.isOpenUserInfoDrawer)
watch(isVisible,(val)=>{
    emit('update:isOpenUserInfoDrawer',val)
},{deep:true})
// 是否处于编辑模式
const isEditing = ref(false);
const userStore=useUserStore()
const classOptions=ref([])
const deptOptions=ref([])
onMounted(
    async ()=> {
      let res = await request.get(`/api/admin/class/select`)
      if (res.status === 200) {
        classOptions.value = res.data
      }
      res = await request.get(`/api/admin/dept/select`)
      if (res.status === 200) {
        deptOptions.value = res.data
      }
    }
)
// 原始用户数据 (模拟从后端获取)
const userData = computed(
    ()=>{
       return userStore.userInfo
    }
);

// 编辑表单数据
const formData = reactive({
  realName: '',
  gender: '',
  phone: '',
  deptId: '',
  classId: ''
});
// 进入编辑模式
const enterEditMode = () => {
  copySameKey(formData,userData.value)
  isEditing.value = true;
};

// 取消编辑
const cancelEdit = () => {
  isEditing.value = false;
};

// 保存修改
const saveChanges = async () => {
  // 这里可以添加表单校验逻辑

  let doSubmitData=copyChangeKey(formData,userData.value)

  if(doSubmitData==null){
    ElMessage.info('没有需要更新的内容！');
    return
  }
  try {
     const res=await request.put(`/api/basic-info/update`,doSubmitData)
     if(res.status===200){
        ElMessage.success('信息修改成功！');
        let userInfo=userStore.userInfo;
        copySameKey(userInfo,doSubmitData)
        userStore.setUserInfo(userInfo)
        isEditing.value = false;
     }
  }catch (e) {
    ElMessage.error('基础信息修改异常：'+e);
  }
};
// 弹框显示控制
const openEmailDialog = ref(false);
const openPwdDialog = ref(false);


// 邮箱表单数据
const emailForm = reactive({
  newEmail: '',
  code: ''
});

// 密码表单数据
const pwdForm = reactive({
  newPwd: '',
  confirmPwd: ''
});

// 验证码倒计时控制
const codeText = ref('获取验证码');
const codeDisabled = ref(false);
const codeTimer = ref(null);
const COUNT=60
const resetEmailForm=()=>{
  Object.assign(pwdForm,{
    newEmail: '',
    code: ''
  })
  if (codeTimer.value) clearInterval(codeTimer.value);
  codeTimer.value=null
  codeText.value = '获取验证码';
  codeDisabled.value=false
  openEmailDialog.value = false;
}
const resetPwdForm=()=>{
  Object.assign(pwdForm,{
    newPwd: '',
    confirmPwd: ''
  })
  openPwdDialog.value = false;
}
// 获取邮箱验证码
const getEmailCode = async () => {
  if (!emailForm.newEmail) {
    ElMessage.warning('请先输入新邮箱');
    return;
  }
  try {
    const res=await request.get(`/api/email?email=${emailForm.newEmail}`)
    if(res.status===200){
      // 模拟验证码发送（实际替换为接口请求）
      ElMessage.success('验证码已发送至新邮箱');
      // 倒计时逻辑
      codeDisabled.value = true;
      let count = COUNT;
      codeText.value = `${count}秒后重新获取`;
      codeTimer.value = setInterval(() => {
        count--;
        codeText.value = `${count}秒后重新获取`;
        if (count <= 0) {
          clearInterval(codeTimer.value);
          codeText.value = '获取验证码';
          codeDisabled.value = false;
        }
      }, 1000);
    }
  }catch (e) {
    ElMessage.error('邮箱发送异常：'+e);
  }
};

// 确认修改邮箱
const confirmEditEmail =async () => {
  try {
    if(!emailForm.newEmail.length>0||!emailForm.code.length>0){
       ElMessage.info("邮箱/验证码为空")
       return
    }
    const res=await request.put(`/api/email/update?email=${emailForm.newEmail}&code=${emailForm.code}`)
    if(res.status===200){
      ElMessage.success('邮箱修改成功');
      let userInfo=userStore.userInfo;
      userInfo.email=emailForm.newEmail
      userStore.setUserInfo(userInfo)
      resetEmailForm()
    }
  }catch (error) {
    ElMessage.error('邮箱修改异常：'+error);
  }
};

// 确认修改密码
const confirmEditPwd = async () => {
  try {
    if(!pwdForm.newPwd.length>0||!pwdForm.confirmPwd.length>0){
      ElMessage.info("新密码/确认密码为空")
      return
    }
    if(pwdForm.newPwd!==pwdForm.confirmPwd){
      ElMessage.info("前后密码不同，请修改")
      return
    }
    const res=await request.put(`/api/password/update?password=${pwdForm.confirmPwd}`)
    if(res.status===200){
      ElMessage.success("密码修改成功")
      resetPwdForm()
    }
  }catch (error) {
    ElMessage.error('密码修改异常：'+error);
  }
};

// 组件卸载时清除定时器
onUnmounted(() => {
  if (codeTimer.value) clearInterval(codeTimer.value);
});
</script>

<style scoped>
.user-profile-container {
  margin: 0 auto;
}
.info-header, .edit-header{
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}
.edit-form{
  overflow: auto;
}
.info-grid {
  overflow: auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 15px;
}

.info-item {
  display: flex;
}

.label {
  font-weight: bold;
  margin-right: 10px;
  color: #666;
  text-align: right;
  width: 80px;
}

.el-form-item{
  display: flex;
  align-items: center;
}
.edit-especially-card {
  border-radius: 8px;
  transition: all 0.3s ease;
  padding-bottom: 20px;
}
:deep(.el-input__wrapper),:deep(.el-select__wrapper){
   height: 40px;
}
/* 卡片头部样式 */
.especially-header {
  display: flex;
  align-items: center;
  color: #333;
  font-weight: 500;
  margin-bottom: 10px;
  padding: 0 10px;
}

/* 按钮组布局 */
.action-button-group {
  display: flex;
  gap: 16px;
  padding: 10px;
  flex-wrap: wrap;
}

/* 操作按钮通用样式 */
.action-button {
  border-radius: 4px;
  font-size: 14px;
  padding: 8px 20px;
  transition: all 0.2s ease;
}

/* 按钮hover效果 */
.action-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 邮箱按钮样式 */
.email-button {
  background-color: #2b91ea;
}

/* 密码按钮样式 */
.password-button {
  background-color: rgba(46, 197, 35);
}

/* 弹框样式优化 */
.email-pwd-dialog :deep(.el-dialog__body) {
  padding: 20px;
}

.email-pwd-dialog :deep(.el-form-item) {
  margin-bottom: 16px;
}
</style>