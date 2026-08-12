<template>
  <div class="window">
    <div class="aside">
      <SiderBar/>
    </div>
    <div class="container">
      <div class="header">
        <div class="toolbar">
           <Headbar/>
        </div>
        <div class="route-tags">
          <RouteTags/>
        </div>
      </div>
      <div class="main">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import SiderBar from "./SiderBar.vue"
import RouteTags from "./RouteTags.vue"
import Headbar from "./Headbar.vue"
import {onMounted, ref, watch} from "vue";
import {useSystemStore} from "../../../stores/system";
const asideWidth=ref('200px')
const systemStore=useSystemStore()
const handleSideStatus=(val)=>{
  if(val===0){
    asideWidth.value='0px'
  }else if(val===1){
    asideWidth.value='200px'
  }
}
watch(()=>systemStore.currentSideStatus,(val)=>{
    handleSideStatus(val)
},{deep:true})
onMounted(
    ()=>{
      handleSideStatus(systemStore.sideStatus)
    }
)
</script>

<style scoped>
.window{
  width: 100%;
  height: 100%;
  display: flex;
}
.aside{
  width: v-bind(asideWidth);
  user-select: none;
}
.container{
  width: v-bind('`calc(100% - ${asideWidth})`');
}
.header{
  height: 100px;
}
.route-tags{
  height: 50px;
}
.toolbar{
  height: calc(100% - 50px);
}
.main{
  width: 100%;
  height: calc(100% - 100px);
  overflow-y: auto;
}
</style>
