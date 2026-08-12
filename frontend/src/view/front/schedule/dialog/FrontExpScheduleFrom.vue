<template>
  <el-dialog class="create-page"
      v-model="visible"
      :title="currentFormStatus ? '更新数据' : '创建数据'"
      :show-close="true"
             width="95%"
  >
    <el-form
        :model="formData"
        label-width="120px"
        class="sys-user-form"
        ref="formRef"
    >
      <el-form-item
          label="教学课程"
          prop="teachingCoreId"
          :rules="[
          { required: true, message: '请选择教学课程', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.teachingCoreId"
            @click="handleSelectTeachingCore"
            placeholder="请选择教学课程"
        >
          <el-option
              v-for="item in teachingCoreOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item
          label="实验室"
          prop="labId"
          :rules="[
          { required: true, message: '请选择实验室', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.labId"
            placeholder="请选择实验室"
            filterable
            clearable
        >
          <el-option
              v-for="item in labOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <!-- 1. 周次相关调整：替换weekRange为实体字段，新增周次类型选择 -->
      <!-- 周次设置：用栅格布局规范排版，统一间距 -->
      <el-form-item
                    label="周次设置"
                    prop="weekType"
                    :rules="[
          { required: true, message: '请设置周次', trigger: 'change' }
        ]"
      >
        <!-- 周次类型：占满一行 -->
        <el-col :span="24" style="margin-bottom: 10px;">
          <el-form-item
              class="el-form-item"
              :rules="[
          { required: true, message: '请选择周次类型', trigger: 'change' }
        ]"
          >
            <el-select
                v-model="formData.weekType"
                placeholder="请选择周次类型"
                @change="handleWeekTypeChange"
                style="width: 100%"
                clearable
            >
              <el-option v-for="wt in weekTypeOptions" :label="wt.label" :value="wt.value" />
            </el-select>
          </el-form-item>
        </el-col>

        <!-- 连续/单周/双周：2列均分，栅格控制宽度和间距 -->
        <template v-if="formData.weekType !== 4">
          <el-form-item style="width: 100%;">
            <el-form-item
                prop="weekStart"
                :rules="[
            { required: true, message: '请输入开始周', trigger: 'blur' },
            { type: 'number', min: 1, max: 20, message: '周次范围1-20', trigger: 'blur' }
          ]"
                style="width: 48%;"
            >
              <el-input
                  v-model.number="formData.weekStart"
                  type="number"
                  placeholder="开始周（如：2）"

              />
            </el-form-item>
            <el-form-item
                prop="weekEnd"
                :rules="[
            { required: true, message: '请输入结束周', trigger: 'blur' },
            { type: 'number', min: formData.weekStart || 1, max: 20, message: '结束周≥开始周', trigger: 'blur' }
          ]"
                style="width: 48%; margin-left: 4%;"
            >
              <el-input
                  v-model.number="formData.weekEnd"
                  type="number"
                  placeholder="结束周（如：17）"

              />
            </el-form-item>
          </el-form-item>
        </template>
        <!-- 自定义周：占满一行 -->
        <el-col :span="24" v-else>
          <el-form-item
              prop="weekCustom"
              :rules="[
          { required: true, message: '请输入自定义周次，符号必须全英文', trigger: 'blur' },
           {
        pattern: /^(\d+(周)?(-\d+(周)?)?(\([单双]\))?,?)+$/,
        message: '支持：2、2周、2-17、2-17周(单)、2-17(单)，多项用逗号分隔（无空格）',
        trigger: 'blur'
      }
        ]"
          >
            <el-input
                v-model="formData.weekCustom"
                placeholder="支持格式：2、2-15、2-15(单)、2,5-8(双)（无空格，符号必须全英文）"
                style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-form-item>

      <!-- 2. 星期调整：value改为数字（对应实体1-7） -->
      <el-form-item
          label="星期"
          prop="weekDay"
          :rules="[
          { required: true, message: '请选择星期', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.weekDay"
            placeholder="请选择星期"
            clearable
        >
          <el-option v-for="item in weekDayOptions"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value" />
        </el-select>
      </el-form-item>

      <!-- 3. 节次调整：替换classPeriod为实体start/end，数字输入 -->
      <el-form-item label="节次设置"
                    :rules="[
            { required: true, message: '请输入开始节次', trigger: 'blur' },
          ]"
      >
        <el-form-item
            prop="periodStart"
            :rules="[
            { required: true, message: '请输入开始节', trigger: 'blur' },
            { type: 'number', min: 1, max: 12, message: '节次范围1-12', trigger: 'blur' }
          ]"
            style="width: 48%"
        >
          <el-input
              v-model.number="formData.periodStart"
              type="number"
              placeholder="开始节"
          />
        </el-form-item>
        <el-form-item
            prop="periodEnd"
            :rules="[
            { required: true, message: '请输入结束节', trigger: 'blur' },
            { type: 'number', min: formData.periodStart, max: 12, message: '结束节≥开始节', trigger: 'blur' }
          ]"
            style="width: 48%; margin-left: 4%;"
        >
          <el-input
              v-model.number="formData.periodEnd"
              type="number"
              placeholder="结束节"
          />
        </el-form-item>
      </el-form-item>

      <!-- 4. 是否有报告：调整label显示（对应实体0=有/1=无） -->
      <el-form-item
          label="是否有报告"
          prop="isReport"
          :rules="[
          { required: true, message: '请选择是否有报告', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.isReport"
            placeholder="请选择是否有报告"
            clearable
        >
          <el-option v-for="item in isReportOptions" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item
          label="总学时"
          prop="classHours"
      >
        <el-input
            v-model.number="formData.classHours"
            type="number"
            placeholder="请输入总学时"
        />
      </el-form-item>
      <el-form-item>
        <el-button-group>
          <el-button class="cancel-button" @click="setVisible(false)">取消</el-button>
          <el-button class="save-button" style="margin-left: 10px;" type="primary" @click="handleSave">保存</el-button>
        </el-button-group>
      </el-form-item>
    </el-form>
  </el-dialog>
  <TeachingCoreForm
      ref="teachingCoreFormRef"
      @sure-select="handleSureTeachingCore"
  />
</template>

<script setup lang="ts">
import {
  reactive,
  ref,
  defineExpose, onMounted
} from 'vue';
import TeachingCoreForm  from '../../../../components/common/form/TeachingCoreForm.vue'
import type { FormInstance } from 'element-plus';
import {weekDayOptions,weekTypeOptions,isReportOptions} from "../../../../utils/globalOptionsUtil"
import {copyChangeKey, copySameKey} from "../../../../utils/commonUtil";
import request from "../../../../request";
import {ElMessage} from "element-plus";
import {computed, defineEmits, defineProps} from "vue";
import { baseApi } from "../../../../config/module/AdminExpCourseScheduleConfig";
const emit=defineEmits(
    ['success-form']
)
const currentFormStatus = computed(() => {
  return formData.id ? 1 : 0;
});
// 弹窗显示状态
const visible = ref(false);
// 下拉选项（接口请求类选项）
const teachingCoreOptions = ref([]) //课程选项
const labOptions = ref([])    // 实验室选项
// 设置弹窗显示/隐藏
const setVisible = (val: boolean) => {
  visible.value = val;
};
onMounted(
    async ()=>{
      let labRes = await request.get(`/api/admin/lab/select`)
      if (labRes.status === 200) {
        labOptions.value = labRes.data
      }
    }
)
const teachingCoreFormRef=ref(null)
const handleSelectTeachingCore=()=>{
     teachingCoreFormRef.value.setSelectVisible(true)
}
const handleSureTeachingCore=(options,value)=>{
     teachingCoreOptions.value={...options}
     if(value){
       formData.teachingCoreId=value
     }
  teachingCoreFormRef.value.setSelectVisible(false)
}
// 表单DOM引用
const formRef = ref<FormInstance>();

// 表单数据（完全对齐ExpCourseSchedule实体）
const formData = reactive({
  id: undefined,                // 课程安排ID
  teachingCoreId: '',                 // 课程辅助表
  labId: '',                    // 实验室ID
  weekStart: undefined,         // 周次开始（实体Integer）
  weekEnd: undefined,           // 周次结束（实体Integer）
  weekType: 1,                // 周次类型（0-连续，1-单，2-双，3-自定义）
  weekCustom: '',               // 自定义周次
  weekDay: '',                  // 星期（1-7，对应实体Integer）
  periodStart: undefined,       // 节次开始（实体Integer）
  periodEnd: undefined,         // 节次结束（实体Integer）
  classHours:undefined,
  isReport: '',                 // 是否有报告（0-有，1-无）
});
const initialFormData=reactive({...formData})
// 重置表单
const resetForm = () => {
  formRef.value?.clearValidate();
  Object.assign(formData, { ...initialFormData });
};
// 周次类型切换处理：重置对应字段
const handleWeekTypeChange = () => {
  if (formData.weekType !== 4) {
    formData.weekCustom = ''
  } else {
    formData.weekStart = undefined
    formData.weekEnd = undefined
  }
}
const initSourceData=reactive({})
// 初始化表单数据（从父组件传入，适配实体字段）
const initFormData=(source)=>{
  copySameKey(formData,source)
  Object.assign(initSourceData, { ...source });
}

// 保存表单数据（提交前转换字段类型对齐实体）
const handleSave = async () => {
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
      await setVisible(false);
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
.create-page{
  height: calc(100% - 40px);
  width: calc(100% - 40px);
  padding: 20px;
  overflow: auto;
}
.save-button{
  width: 150px;
  margin-right: 10px;
}
.cancel-button{
  width: 100px;
}
</style>