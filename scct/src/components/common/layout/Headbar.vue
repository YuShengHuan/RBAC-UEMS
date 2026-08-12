<template>
   <div class="toolbar-page">
       <div class="left-area">
          <el-button
              class="ex-button-ex-side"
              @click="handleShowAndHideAside"
          >
            <el-icon :size="35">
               <Fold v-if="systemStore.sideStatus===1"/>
               <Expand v-else/>
            </el-icon>
          </el-button>
       </div>
       <div class="right-area">
         <el-avatar @click="handleClickAvatar" class="el-avatar">{{userStore?.currentRoleName||'未知'}}</el-avatar>
         <span>{{userStore.userInfo?.realName||'游客'}}</span>
         <div class="ex-area" v-if="userStore.allRole.length>0">
           <el-button
               class="el-button-ex-role"
               :icon="Sort"
               @click="handleExchangeRoleButton"
           >切换
           </el-button>
           <div class="ex-role-items" :style="{display:isHideExRoleItems?'none':'block',zIndex:10,position: 'absolute'}">
             <template v-for="item in userStore.allRole" >
               <div
                   :class="{'role-item':true,'role-item-active':item.roleCode===userStore.currentRole}"
                   @click="handleExchangeCurrentRole(item)"
               >
                 {{item.roleName}}
               </div>
             </template>
           </div>
         </div>
         <el-button
             class="el-button-logout"
             type="danger"
             @click="handleLogout"
             plain
         >
           退出登录
         </el-button>
       </div>
       <UserInfoCard
           v-model="isOpenUserInfoDrawer"
       />
   </div>
</template>

<script setup lang="ts">
import UserInfoCard  from '../../common/card/UserInfoCard.vue'
import {useUserStore} from "../../../stores/user";
import {ref} from "vue";
import {Fold,Expand,Sort} from '@element-plus/icons-vue'
import {useSystemStore} from "../../../stores/system";
import request from "../../../request";
import {useRouter} from "vue-router";

const router=useRouter()
const userStore=useUserStore()
const systemStore=useSystemStore()
const isHideExRoleItems=ref(true)
const currentAutoHideTimeoutId=ref(-1)
const isOpenUserInfoDrawer=ref(false)
const handleShowAndHideAside=()=>{
    systemStore.sideStatus?
        systemStore.setSideStatus(0):
        systemStore.setSideStatus(1)
}
const handleExchangeRoleButton=()=>{
  isHideExRoleItems.value=false
  currentAutoHideTimeoutId.value=setTimeout(()=>{
    isHideExRoleItems.value=true
  },5000)
}
const handleExchangeCurrentRole=async (item)=>{
   isHideExRoleItems.value=true
   clearTimeout(currentAutoHideTimeoutId.value)
   const res=await request.put(`/api/current-role/update`,item)
   if(res.status===200){
     userStore.setCurrentRole(item.roleCode)
     userStore.setCurrentPermissions(res.data)
     if(item.roleCode.includes("R")|| item.roleCode.includes("A")){
       systemStore.setCurrenTheme('dark')
       await router.push("/home/admin")
     }else{
       systemStore.setCurrenTheme('light')
       await router.push("/home/front")
     }
   }
}
const handleLogout=()=>{
   userStore.logout()
}
const handleClickAvatar=()=>{
  isOpenUserInfoDrawer.value=true
}
</script>

<style scoped>
.toolbar-page{
  width: 100%;
  height: 100%;
  display: flex;
  user-select: none;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}
.left-area{
  height: 100%;
  display: flex;
  align-items: center;
  margin-left: 10px;
}
.ex-button-ex-side{
   background: transparent;
   border: none;
   width: 35px;
}
.right-area{
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  margin-right: 20px;
}
.el-avatar{
  margin-right: 5px;
  background: #d1ebff;
  color: #0086fc;
  box-shadow: inset 0 0 20px rgb(197, 212, 213); /* 内部阴影 */
  padding: 5px;
  width: 45px;
  border-radius: 50%;
  font-size: 11px;
}
.ex-area{
  position: relative;
  display:block;
  margin-left: 10px;
}
.ex-role-items{
   width: 100%;
   user-select:none;
   font-size: 12px;
}
.role-item{
  display: flex;
  align-items: center;
  justify-content: center;
  height: 35px;
  margin: 0px 2px 1px 2px;
  background-color: rgb(44, 173, 182);
  color: #FFFFFF;
}
.role-item:hover{
  background-color: rgba(64, 158, 255, 0.6);
}
.role-item-active{
  color: #FFFFFF;
  background-color: #0078f3;
  cursor: not-allowed;
  pointer-events: none;
}
.el-button-ex-role{
   height: 35px;
   background-color: #409eff;
   color: #FFFFFF;
}
.el-button-logout{
   height: 35px;
   margin-left: 20px;
}
</style>