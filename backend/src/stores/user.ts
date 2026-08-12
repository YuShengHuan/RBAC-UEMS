import {defineStore} from 'pinia';
import {EncryptStorage} from "encrypt-storage";
// 引入加密解密函数'
export const encryptStorage = new EncryptStorage(import.meta.env.VITE_CRYPTO_SECRET_KEY, {
	stateManagementUse: true
})
export const useUserStore = defineStore('user', {
	state: () => ({
		accessToken:'',
		userInfo:{},
		loginBox:{},
		humanVerificationStatus:true
	}),
	persist:{
		key:'user-store',
		pick: ['accessToken',"userInfo","loginBox","humanVerificationStatus"],
		storage: encryptStorage,
	},
	getters: {
		currentAccessToken:(state)=>{
			return state?.accessToken
		},
		currentRole:(state)=>{
            return state.userInfo?.currentRoleCode
		},
		currentRoleName:(state)=>{
			return state.userInfo?.currentRoleName
		},
		hasPermission: (state) => (permCode) => {
			return state.userInfo?.selfRolePermissionList?.find(item=>item.permCode===permCode);
		},
		allRole:(state)=>{
			return state.userInfo?.selfRoleList?.map(item=>({roleCode:item.roleCode,roleName:item.roleName}))
		}
	},
	actions: {
		setAccessToken(accessToken){
			this.accessToken=accessToken;
		},
		setUserInfo(userInfo){
			this.userInfo=userInfo;
			console.log(userInfo)
		},
		setLoginBox(loginData){
			this.loginBox=loginData
		},
		setHumanVerificationStatus(status){
			this.humanVerificationStatus=status;
		},
		setCurrentRole(currentRoleCode){
			this.userInfo.currentRoleCode=currentRoleCode
		},
		setCurrentPermissions(currentPermissions){
			this.userInfo.selfRolePermissionList=currentPermissions
		},
		// 退出登录：清空状态
		logout() {
			this.accessToken ='';
			this.userInfo={};
			this.loginBox={}
			window.location.href = '/login';
		}
	}
})