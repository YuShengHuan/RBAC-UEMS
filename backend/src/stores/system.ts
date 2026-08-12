import {defineStore} from 'pinia';
import {encryptStorage} from "./user";
export const useSystemStore = defineStore('system', {
  state: () => ({
       sideStatus:1,
       themeConfig: {
           dark: {
               bgColor: 'rgb(58,57,57)',
               textColor: '#ffffff',
               activeTextColor: '#fa7304',
               borderColor: 'rgba(255, 255, 255, 0.08)',
               hoverBgColor: 'rgba(255, 255, 255, 0.08)',
               activeBgColor: '#2b91ea',
               activeBorderColor: '#fa8602',
               scrollbarThumbColor: 'rgba(255, 255, 255)',
               scrollbarTrackColor: 'rgba(252,250,250,0.73)',
               logoTextColor: '#ffffff',
               routePath: '/home/admin',
               logoText: '实验教学管理',
           },
           light: {
               bgColor: '#ffffff',
               textColor: '#333333',
               activeTextColor: '#409EFF',
               borderColor: '#e9ecef',
               hoverBgColor: '#f0f7ff',
               activeBgColor: '#e6f4ff',
               activeBorderColor: '#409EFF',
               scrollbarThumbColor: '#dcdfe6',
               scrollbarTrackColor: '#f5f5f5',
               logoTextColor: '#000000',
               routePath: '/home/front',
               logoText: '实验教学平台',
           }
       },
      currentTheme:'light'
  }),
    persist:{
        key:'system-store',
        pick: ["sideStatus","currentTheme"],
        storage: encryptStorage,
    },
  getters: {
      currentSideStatus:(state)=>{
         return state.sideStatus
      },
      currentThemeConfig:(state)=>{
          return state.themeConfig[state.currentTheme]
      },
  },
  actions: {
	 setSideStatus(status){
       //0隐藏/1显示
        this.sideStatus=status
     },
      setCurrenTheme(theme){
         this.currentTheme=theme
      }
  }
})