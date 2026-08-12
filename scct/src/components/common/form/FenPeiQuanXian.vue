<template>
  <el-drawer
      v-model="visible"
      title="分配权限"
      direction="rtl"
      size="80%"
  >
    <el-select
        v-model="currentRole"
        @change="handleCurrentRole"
        style="margin-bottom: 5px;"
        size="large"
        placeholder="请选择角色"
    >
      <el-option
          v-for="item in roleOptions"
          :label="item.label"
          :value="item.value"
      />
    </el-select>
    <div style="display: flex;align-items: center;justify-content: flex-end;">
      <el-button-group>
        <el-button type="primary" @click="handleSelectOrNoSelectAll(1)">全选</el-button>
        <el-button type="primary" @click="handleSelectOrNoSelectAll(0)">全不选</el-button>
      </el-button-group>
    </div>

    <el-table class="el-table" v-model:data="tableData" :max-height="tableMaxHeight"
              row-key="id"
              border
    >
      <el-table-column
          v-for="(c,index) in tableDataTreeConfig"
          :key="index"
          :label="c.label"
          :prop="c.prop"
      >
        <template #default="scope">
            <span
                style="font-size: 20px;user-select: none;cursor: pointer;"
                v-if="index===0"
                @click="handleClickOwn(scope)"
            >{{scope.row.isOwn===1?'☑':'☐'}}</span>
          {{
            c.formatter?c.formatter(scope.row):scope.row[c.prop]
          }}
        </template>
      </el-table-column>
    </el-table>
    <div style="display: flex;align-items: center;margin: 10px 0;">
      <el-button type="primary" style="width: 150px;" @click="handleSaveOwnPerm">保存</el-button>
    </div>
  </el-drawer>
</template>

<script setup lang="ts">
import {ref, onBeforeUnmount, onMounted, computed,defineExpose} from "vue";
import {tableDataTreeConfig} from '../../../config/module/AdminSysPermissionConfig'
import request from "../../../request";
import {ElMessage} from "element-plus";
const visible=ref(false)
const setVisible=(val)=>{
    visible.value=val
}
defineExpose({
  setVisible
})
const tableData=ref([])
const tableMaxHeight = ref(0)
// 计算表格最大高度
const calcTableHeight = () => {
  const windowHeight = window.innerHeight
  const tableTop = document.querySelector('.el-table')?.getBoundingClientRect().top
  // 留出 50px 边距
  tableMaxHeight.value = windowHeight - tableTop - 65
}

const roleOptions=ref([])
const currentRole=ref('')
const currentOwnPerm=computed(
    ()=>{
      const ownPerm=[]
      const findOwnPerm=(data,ay)=>{
          if(data.length==0)
            return []
          data.forEach(
              d=>{
                  if(d.isOwn===1){
                     ay.push(d)
                  }
                  if(d.children){
                      findOwnPerm(d.children,ay)
                  }
              }
          )
      }
      findOwnPerm(tableData.value,ownPerm)
      return ownPerm
  }
)
const handleSaveOwnPerm=async ()=>{
    let doData=currentOwnPerm.value.map(item=>{
        return {
           roleId:currentRole.value,
           permId:item.id
        }
    })
  const res=await request.post(`/api/admin/role-perm/${currentRole.value}/batch-insert`,doData)
  if(res.status===200){
      ElMessage.success("保存成功")
  }
}
const handleClickOwn=(scope)=>{
  if(scope.row.isOwn===1){
    scope.row.isOwn=0
  }else{
    scope.row.isOwn=1
  }
  //假如点击的是父权限
  if(scope.row.parentCode.trim().length===0){
    tableData.value.forEach(
        pt=>{
           if(pt.permCode===scope.row.permCode){
              pt.children.forEach(
                  ct=>{
                     ct.isOwn=pt.isOwn
                  }
              )
             return
           }
        }
    )
  }else{
    tableData.value.forEach(
        pt=>{
          if(pt.permCode===scope.row.parentCode){
            if(pt.isOwn==0){
               pt.isOwn=1
            }
            return
          }
        }
    )
  }
}
const handleSelectOrNoSelectAll=(isOwn)=>{
  tableData.value.forEach(
      pt=>{
        pt.isOwn=isOwn
        pt.children.forEach(
            ct=>{
              ct.isOwn=pt.isOwn
            }
        )
      }
  )
}
const initCurrentRolePermTableData=async ()=>{
  const res=await request.get(`/api/admin/perm/tree/${currentRole.value}`)
  if(res.status===200){
      tableData.value=res.data
  }
}
const handleCurrentRole=async ()=>{
    await initCurrentRolePermTableData()
}
onMounted(async () => {
  let res=await request.get('/api/admin/role/select')
  if(res.status===200){
    roleOptions.value=res.data
    if(roleOptions.value.length>0){
      currentRole.value=roleOptions.value[0].value
      await initCurrentRolePermTableData()
    }
  }

  calcTableHeight()
  window.addEventListener('resize', calcTableHeight)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', calcTableHeight)
})
</script>

<style scoped>

</style>