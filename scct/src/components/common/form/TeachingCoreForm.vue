<template>
  <el-dialog
      class="teaching-core-page"
      v-model="selectVisible"
      title="选择"
      :show-close="true"
      width="50%"
  >
    <template #default>
      <div class="operate-contain">
          <el-button class="create-button" type="primary" @click="handleCreate">新建</el-button>
          <el-button v-show="String(currentValue).trim().length>0" class="update-button" type="success" @click="handleUpdate">更新</el-button>
          <el-button v-show="String(currentValue).trim().length>0" class="delete-button"  style="margin-left: 10px;" type="danger" @click="handleDelete">删除</el-button>
      </div>
      <el-input
          style="margin-bottom: 5px;"
          v-model="searchContent"
          placeholder="搜索">
        <template #prefix>
          <el-icon><Search/></el-icon>
        </template>
      </el-input>
      <div class="item-contain">
        <div
            v-for="item in filterTeachingCoreOption"
            @click="handleClickSelect(item.value)"
            :class="{'item':true,'item-active':item.value===currentValue}"
        >{{item.label}}</div>
      </div>
    </template>
    <template #footer>
      <el-button-group>
        <el-button class="cancel-button" @click="setSelectVisible(false)">取消</el-button>
        <el-button class="sure-button" style="margin-left: 10px;" type="primary" @click="handleSure">确定</el-button>
      </el-button-group>
    </template>
  </el-dialog>
  <el-dialog
      v-model="formVisible"
      :title="currentFormStatus ? '更新数据' : '创建数据'"
      :show-close="true"
      width="600px"
  >
    <el-form
        :model="formData"
        label-width="100px"
        class="sys-user-form"
        ref="formRef"
    >
      <!-- 原有基础字段（保持不变） -->
      <el-form-item
          label="学期"
          prop="semester"
          :rules="[
          { required: true, message: '请输入学期', trigger: 'blur' },
          { pattern: /^\d{4}-\d{4}-\d$/, message: '请输入正确格式（如：2023-2024-1）', trigger: 'blur' }
        ]"
      >
        <SemesterPicker
            v-model="formData.semester"
        />
      </el-form-item>

      <el-form-item
          label="课程"
          prop="courseId"
          :rules="[
          { required: true, message: '请选择课程', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.courseId"
            filterable
            placeholder="请选择课程"
            clearable
        >
          <el-option
              v-for="item in courseOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item
          label="授课教师"
          prop="userId"
          :rules="[
          { required: true, message: '请选择授课教师', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.userId"
            filterable
            placeholder="请选择授课教师"
            clearable
        >
          <el-option
              v-for="item in userOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item
          label="班级"
          prop="classId"
          :rules="[
          { required: true, message: '请选择班级', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.classId"
            filterable
            placeholder="请选择班级"
            clearable
        >
          <el-option
              v-for="item in classOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="el-button-footer">
        <el-button @click="setFormVisible(false)">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {ref, defineExpose, onMounted,reactive,computed,defineEmits} from "vue";
import {baseApi} from "../../../config/module/AdminExpTeachingCoreConfig";
import {Search} from '@element-plus/icons-vue'
import request from "../../../request";
import SemesterPicker from '../../../components/common/form/SemesterPicker.vue'
import {ElMessage, FormInstance} from "element-plus";
import {copyChangeKey, copySameKey} from "../../../utils/commonUtil";
const selectVisible = ref(false);
const formVisible=ref(false)
const currentValue=ref('')
const handleClickSelect=(value)=>{
    currentValue.value=value
}
const emit=defineEmits(
    ['sure-select']
)
// 当前表单状态 (0-创建 1-更新)
const currentFormStatus = computed(() => {
  return formData.id ? 1 : 0;
});
const teachingCoreOptions=ref([])
const searchContent=ref('')
const filterTeachingCoreOption=computed(
    ()=>{
         if(String(searchContent.value).trim().length>0){
              return teachingCoreOptions.value.filter(
                  item=>item.label.includes(searchContent.value)
              )
         }
         return teachingCoreOptions.value
    }
)
// 设置弹窗显示/隐藏
const setSelectVisible = (val: boolean) => {
  selectVisible.value = val;
};
const courseOptions = ref([]) //课程选项
const classOptions = ref([])  // 班级选项
const userOptions = ref([])   // 授课教师选项
const setFormVisible = (val: boolean) => {
    formVisible.value = val;
};
const initData=async ()=>{
  const res = await request.get(`${baseApi}/select`);
  if (res.status === 200) {
    teachingCoreOptions.value = res.data;
    emit('sure-select',teachingCoreOptions.value,currentValue.value)
  }

}
// 初始化下拉选项
onMounted(async () => {
  await initData()
  let courseRes = await request.get(`/api/admin/course/select`)
  if (courseRes.status === 200) {
    courseOptions.value = courseRes.data
  }
  let classRes = await request.get(`/api/admin/class/select`)
  if (classRes.status === 200) {
    classOptions.value = classRes.data
  }
  let userRes = await request.get(`/api/admin/user/select?userType=2`)
  if (userRes.status === 200) {
    userOptions.value = userRes.data
  }
})

// 表单数据（完全对齐ExpCourseSchedule实体）
const formData = reactive({
  id: undefined,                // 课程安排ID
  semester: '' ,                 // 学期
  courseId: '',                 // 课程ID（字符串接收，提交时自动转数字）
  classId: '',                  // 班级ID
  userId: '',                   // 授课用户ID
});
// 初始表单数据（用于重置）
const initialFormData = reactive({ ...formData });

// 重置表单
const resetForm = () => {
  formRef.value?.clearValidate()
  Object.assign(formData, { ...initialFormData })
}
const initSourceData=reactive({})
const initFormData=async ()=>{
  const res = await request.get(`${baseApi}/detail/${currentValue.value}`);
  if (res.status === 200) {
     copySameKey(formData,res.data)
     Object.assign(initSourceData, res.data)
     setFormVisible(true)
  }
}
// 表单DOM引用
const formRef = ref<FormInstance>();
const handleDelete=async ()=>{
  try {
    const res=await request.delete(`${baseApi}/delete/${currentValue.value}`)
    if(res.status===200){
      await initData()
    }
  }catch (e) {
    console.log(e)
  }
}
const handleCreate=()=>{
  setFormVisible(true)
  resetForm()
}
const handleUpdate=async ()=>{
   await initFormData()
}
const handleSave=async ()=>{
  if (!formRef.value) return;
  try {
    // 触发表单校验
    await formRef.value.validate();
    // 校验通过，提交数据
    console.log('表单数据提交:', formData);

    let res=null
    if(currentFormStatus.value==0){
      res=await request.post(`${baseApi}/create`,formData)
    }else if(currentFormStatus.value==1){
      let changeFormData=copyChangeKey(formData,initSourceData)
      if(changeFormData!=null){
        res=await request.put(`${baseApi}/update`,changeFormData)
      }
    }
    if(res?.status===200 || res?.status===201){
      setFormVisible(false)
      await initData()
    }
  } catch (error) {
    // 校验失败，不提交
    ElMessage.error('表单校验失败:'+error);
  }
}
const handleSure=()=>{
    emit('sure-select',teachingCoreOptions.value,currentValue.value)
}
defineExpose({
  setSelectVisible,
  initFormData
})
</script>

<style scoped>
.operate-contain{
   margin-bottom:5px;
}
.item-contain{
  border: 1px solid #d9d9d9;
  width: 100%;
  height: 200px;
  overflow-y: auto;
}
.item{
  min-height: 35px;
  cursor: pointer;
  display: flex;
  align-items: center;
  padding: 0 10px;
}
.item-active{
  background: #2b91ea;
  color: #FFFFFF;
}
</style>