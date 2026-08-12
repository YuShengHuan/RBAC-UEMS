<template>
  <div class="route-tags-page">
    <div ref="tagsContainerRef" class="tags-container">
      <el-tag
          :effect="currentPath === startPagePath ? 'dark' : 'plain'"
          @click="handleTagClick(startPagePath)"
          class="tag-item"
          size="large"
      >
        首页
      </el-tag>
      <el-tag
          v-for="(tag, index) in tags"
          :closable="true"
          :effect="currentPath === tag.path ? 'dark' : 'plain'"
          :hit="currentPath === tag.path"
          @click="handleTagClick(tag.path)"
          @close="handleTagClose(tag.path)"
          class="tag-item"
          size="large"
      >
        {{ tag.title }}
      </el-tag>
    </div>
    <el-button-group class="tag-options">
      <el-button
          class="el-button-tag-operate"
          :icon="DArrowLeft"
          @click="scrollTags('left')"
      ></el-button>
      <el-button
          class="el-button-tag-operate"
          :icon="DArrowRight"
          @click="scrollTags('right')"
      ></el-button>
      <el-popover
          placement="bottom-end"
          popper-style="min-width: 0px;padding: 0;width: auto;margin: 0;"
          trigger="click"
          effect="light"
          :show-arrow="false"
          :popper-options="{
                    modifiers: [
                      {
                        name: 'offset',
                        options: {
                          offset: [0, 0]  // [水平偏移, 垂直偏移]，均设为 0
                        }
                      }
                    ]
                  }"
      >
        <template #reference>
          <el-button
              class="el-button-tag-operate"
              :icon="Operation"
          ></el-button>
        </template>
        <template #default>
          <div class="el-popover-button-options">
            <div class="el-popover-button-options-item" @click="handleTagClose(currentPath)">关闭当前选项</div>
            <div class="el-popover-button-options-item" @click="handleTagCloseAll">关闭所有选项</div>
            <div class="el-popover-button-options-item" @click="handleTagCloseExcludeCurrent">关闭除了当前</div>
          </div>
        </template>
      </el-popover>
    </el-button-group>
  </div>

</template>

<script setup lang="ts">
import {onMounted, ref, watch} from 'vue'
import {useRouter} from "vue-router";
import {DArrowLeft,DArrowRight,Operation} from "@element-plus/icons-vue";
// 获取容器 DOM
const tagsContainerRef = ref(null)
//改变tag的滑动条
const scrollTags = (direction) => {
  const container = tagsContainerRef.value
  if (!container) return
  const step = 100 // 每次滚动的像素
  if (direction === 'left') {
    container.scrollLeft -= step
  } else if (direction === 'right') {
    container.scrollLeft += step
  } else if (direction === 'reset') {
    container.scrollLeft = 0 // 回到最左
  } else if (direction === 'end') {
    container.scrollLeft = container.scrollWidth - container.clientWidth // 滚到最右
  }
}
const tags=ref([])
const startPagePath=ref('/home/admin')
const router = useRouter()
const currentPath = ref('')
const handleTagClick = (path: string) => {
  router.replace(path)
}
onMounted(()=>{
   handleAddTag(router.currentRoute.value)
})
watch(
    router.currentRoute,
    (newValue)=>{
         handleAddTag(newValue)
    }
)
//增加标签
const handleAddTag=(newValue)=>{
  const title=newValue.meta?.title;
  const path=newValue?.path;
  if(title&&path){
    if(!tags.value.find(tag=>tag.path==path)){
      tags.value.push({
        title:title,
        path:path
      })
    }
  }
  currentPath.value=path;
}
//关闭左右，按前关闭
const handleTagClose = (path: string) => {
  let newPath=startPagePath.value
  if(tags.value.length>1){
      let index=tags.value.findIndex(tag => tag.path === path);
      if(index!==0){
        newPath=tags.value[index-1].path
      }
  }
  tags.value = tags.value.filter(tag => tag.path !== path)
  // 跳转到新标签
  router.replace(newPath)

}
//关闭所有
const handleTagCloseAll=()=>{
  tags.value.length=0
  router.replace(startPagePath.value)
}
//关闭除了自身
const handleTagCloseExcludeCurrent=()=>{
  tags.value = tags.value.filter(tag => tag.path === currentPath.value)
}
</script>

<style scoped>
.route-tags-page{
  display: flex;
  width: 100%;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}
.tags-container {
  display: flex;
  align-items: center;
  overflow-x: auto;
  width: calc(100% - 130px);
}
.tags-container::-webkit-scrollbar {
  display: none;
}
.tags-container::-webkit-scrollbar-thumb {
  background: transparent;
}

.tags-container::-webkit-scrollbar-track {
  background: transparent;
}
.el-button-tag-operate{
   border-radius: 0;
   width: 40px;
  background-color:  #409eff;
   color: #FFFFFF;
}
.tag-options{
  width: 130px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.el-popover-button-options{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100px;
  cursor: default;
}
.el-popover-button-options-item{
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 35px;
}
.el-popover-button-options-item:hover{
  background-color:  #409eff;
  color:#FFFFFF;
}
.tag-item {
  cursor: pointer;
  transition: all 0.2s;
  margin: 5px;
}

.tags-container::-webkit-scrollbar {
  height: 6px;
}

.tags-container::-webkit-scrollbar-thumb {
  background-color: #c0c4cc;
  border-radius: 3px;
}

.tags-container::-webkit-scrollbar-track {
  background-color: #f5f7fa;
}
</style>