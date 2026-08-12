<template>
  <el-dialog
      v-model="visible"
      :title="currentFormStatus ? '更新数据' : '创建数据'"
      :show-close="true"
      width="95%"
  >
    <el-form
        :model="formData"
        label-width="100px"
        class="sys-user-form"
        ref="formRef"
    >
      <!-- 用户账号 -->
      <el-form-item
          label="用户账号"
          prop="userAccount"
          :rules="[
          { required: true, message: '请输入用户账号', trigger: 'blur' },
          { min: 1, message: '账号长度在>=1 个字符之间', trigger: 'blur' }
        ]"
      >
        <el-input
            v-model="formData.userAccount"
            placeholder="请输入用户账号"
        />
      </el-form-item>

      <!-- 用户密码 -->
      <el-form-item
          label="用户密码"
          prop="userPassword"
          :rules="[
          { required: true, message: '请输入用户密码', trigger: 'blur' },
          { min: 1, message: '用户密码长度在>=1 个字符之间', trigger: 'blur' }
        ]"
      >
        <el-input
            v-model="formData.userPassword"
            placeholder="请输入用户密码"
            show-password
        />
      </el-form-item>

      <!-- 真实姓名 -->
      <el-form-item
          label="真实姓名"
          prop="realName"
          :rules="[
          { required: true, message: '请输入真实姓名', trigger: 'blur' },
          { min: 1, message: '真实姓名长度在>=1 个字符之间', trigger: 'blur' }
        ]"
      >
        <el-input
            v-model="formData.realName"
            placeholder="请输入真实姓名"
        />
      </el-form-item>

      <!-- 性别 -->
      <el-form-item
          label="性别"
          prop="gender"
      >
        <el-select
            v-model="formData.gender"
            placeholder="请选择性别"
        >

          <el-option v-for="item in genderOptions"
                     :label="item.label" :value="item.value"
          />
        </el-select>
      </el-form-item>

      <!-- 手机号 -->
      <el-form-item
          label="手机号"
          prop="phone"
      >
        <el-input
            v-model="formData.phone"
            placeholder="请输入手机号"
        />
      </el-form-item>

      <!-- 邮箱 -->
      <el-form-item
          label="邮箱"
          prop="email"
      >
        <el-input
            v-model="formData.email"
            placeholder="请输入邮箱"
        />
      </el-form-item>

      <!-- 用户类型 -->
      <el-form-item
          label="用户类型"
          prop="userType"
          :rules="[
          { required: true, message: '请输入用户类型', trigger: 'blur' }
        ]"
      >
        <el-select
            v-model="formData.userType"
            placeholder="请选择用户类型"
            @change="handleUserTypeChange"
        >
          <el-option v-for="item in userTypeOptions"
              :label="item.label" :value="item.value"
          />
        </el-select>
      </el-form-item>

      <!-- 部门ID (仅教师显示) -->
      <el-form-item
          v-show="formData.userType === 2"
          label="学院"
          prop="deptId"
      >
        <el-select
            v-model="formData.deptId"
            placeholder="请选择学院"
        >
          <el-option
              v-for="item in deptOptions"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item
          v-show="formData.userType === 3"
          label="班级"
          prop="classId"
      >
        <el-select
            v-model="formData.classId"
            placeholder="请选择班级"
        >
          <el-option
              v-for="item in classOptions"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <!-- 用户状态 -->
      <el-form-item
          label="用户状态"
          prop="userStatus"
      >
        <el-select
            v-model="formData.userStatus"
            placeholder="请选择用户状态"
        >
          <el-option
              v-for="item in userStatusOptions"
              :label="item.label"
              :value="item.value"
              />
        </el-select>
      </el-form-item>

      <!-- 备注 -->
      <el-form-item
          label="备注"
          prop="remark"
      >
        <el-input
            v-model="formData.remark"
            placeholder="请输入备注"
            type="textarea"
            :rows="3"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="el-button-footer">
        <el-button @click="setVisible(false)">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {
  defineExpose,
  reactive,
  computed,
  ref,
  defineProps,
  defineEmits, onMounted
} from 'vue';
import {userStatusOptions,userTypeOptions,genderOptions} from "../../../../utils/globalOptionsUtil"
import type { FormInstance } from 'element-plus';
import request from "../../../../request";
import {copyChangeKey, copySameKey} from "../../../../utils/commonUtil";
import {ElMessage} from "element-plus";
const props=defineProps({
   baseApi:{
      type:String,
      required:true
   }
})
const emit=defineEmits(
    ['success-form']
)
// 表单DOM引用
const formRef = ref<FormInstance>();

// 弹窗显示状态
const visible = ref(false);

// 设置弹窗显示/隐藏
const setVisible = (val: boolean) => {
  visible.value = val;
};
const classOptions=ref([])
const deptOptions=ref([])
onMounted(
    async ()=>{
      let res=await request.get(`/api/admin/class/select`)
      if(res.status===200){
        classOptions.value=res.data
      }
      res=await request.get(`/api/admin/dept/select`)
      if(res.status===200){
        deptOptions.value=res.data
      }
    }
)

// 用户表单数据
const formData = reactive({
  id: undefined,
  userAccount: '',
  userPassword: '',
  realName: '',
  gender: '', // 男：1 女：2
  phone: '',
  email: '',
  userType: '',  // 管理员：1 教师：2  学生：3
  deptId: '',
  classId: '',
  userStatus: '', // 启用：1 禁用：0
  remark: ''
});
const initialFormData=reactive(
    {...formData}
)
const resetForm=()=>{
    formRef.value?.clearValidate()
    Object.assign(formData, initialFormData)
}
// 用户类型改变时的处理函数
const handleUserTypeChange = () => {
  // 切换用户类型时，清空对应的部门ID和班级ID
  if (formData.userType !== 1 || formData.userType !== 2) {
    formData.deptId = '';
  }
  if (formData.userType !== 3) {
    formData.classId = '';
  }
};
const initSourceData=reactive({})
const initFormData=(source)=>{
  copySameKey(formData,source)
  Object.assign(initSourceData, source)
}
// 当前表单状态 (0-创建 1-更新)
const currentFormStatus = computed(() => {
  return formData.id ? 1 : 0;
});
// 保存表单数据
const handleSave = async () => {
  if (!formRef.value) return;
  try {
    // 触发表单校验
    await formRef.value.validate();
    // 校验通过，提交数据
    console.log('表单数据提交:', formData);

    let res=null
    if(currentFormStatus.value==0){
      res=await request.post(`${props.baseApi}/create`,formData)
    }else if(currentFormStatus.value==1){
      let changeFormData=copyChangeKey(formData,initSourceData)
      if(changeFormData!=null){
        res=await request.put(`${props.baseApi}/update`,changeFormData)
      }
    }
    if(res?.status===200 || res?.status===201){
      setVisible(false);
      emit('success-form')
    }
  } catch (error) {
    // 校验失败，不提交
    ElMessage.error('表单校验失败:'+error);
  }
};

// 暴露给父组件的方法
defineExpose({
  setVisible,
  // 可以暴露表单数据供父组件访问
  resetForm,
  initFormData
});
</script>

<style scoped>
.sys-user-form {
  padding: 20px 0;
}

.el-button-footer {
  text-align: right;
}
</style>