<template>
  <div class="file-div">
    <input
        class="input-file"
        id="file"
        type="file"
        :multiple="multiple"
        :accept="accept"
        @input="(e)=>handleAddFormData(e.target.files)">
    <label for="file" class="file-label">点击文件</label>
    <div class="tip-div">
       <span v-for="tip in props.fileTip">{{tip}}</span>
    </div>
  </div>
</template>
<script lang="ts" setup>
import {defineProps, defineEmits} from 'vue'
const props=defineProps({
   modelValue:{
     type: [Object,String],
     default:null
   },
  fileTip:{
     type:String,
     default:''
  },
  multiple:{
     type:Boolean,
     default:false
  },
  accept:{
     type:String,
     default:'.*'
  },
  disabled:{
     type:Boolean,
     default:false
  }
})
const emit=defineEmits(['update:modelValue',"update:file-tip"])
const handleAddFormData=(files)=>{
  if(props.multiple){
    emit('update:modelValue',files)
    emit('update:file-tip',Object.keys(files).map(item=>files[item].name))
  }else{
    emit('update:modelValue',files[0])
    emit('update:file-tip',files[0].name)
  }
}
</script>

<style scoped>
.file-div{
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
}
.input-file{
   display: none;
}
.file-label{
   background-color: #2b91ea;
   color: #FFFFFF;
   width: 100px;
   height: 40px;
   display: flex;
   align-items: center;
   justify-content: center;
}
.tip-div{
  margin-left: 10px;
  font-size: 12px;
  white-space: nowrap;/*设置不换行*/
  overflow: hidden; /*设置隐藏*/
  text-overflow: ellipsis; /*设置隐藏部分为省略号*/
  width: calc(100% - 110px);
}
</style>
