export const adminConstantRoutes=
[
    {
        path:'/user',
        component:()=>import('../view/admin/user/AdminSysUser.vue'),
        meta:{
            requiresAuth:true,
            title:'用户管理',
            permissions:[
                "user"
            ]
        }
    },
    {
        path:'/authority',
        meta:{
            requiresAuth:true,
            title:'权限管理',
            permissions:[
                "role","perm","userRole","rolePerm"
            ]
        },
        children:[
            {
                path:'permissions',
                component:()=>import('../view/admin/authority/AdminSysPermission.vue'),
                meta:{
                    requiresAuth: true,
                    title:'权限列表',
                    permissions:[
                        "perm:page"
                    ],
                },
            },
            {
                path:'role-permissions',
                component:()=>import('../view/admin/authority/AdminSysRolePermission.vue'),
                meta:{
                    requiresAuth: true,
                    title:'角色-权限列表',
                    permissions:[
                        "rolePerm:page"
                    ],
                },
            },
            {
                path: 'roles',
                component:()=>import('../view/admin/authority/AdminSysRole.vue'),
                meta:{
                    requiresAuth: true,
                    title:'角色列表',
                    permissions:[
                        "role:page"
                    ],
                }
            },
            {
                path:'user-roles',
                component:()=>import('../view/admin/authority/AdminSysUserRole.vue'),
                meta:{
                    requiresAuth: true,
                    title:'用户-角色列表',
                    permissions:[
                        "userRole:page"
                    ],
                },
            },
        ]
    },
    {
        path:'/base',
        meta:{
            requiresAuth:true,
            title:'分院管理',
            permissions:[
                "dept","major","class"
            ],
        },
        children:[
            {
                path:'dept',
                component:()=>import('../view/admin/base/AdminBaseDept.vue'),
                meta:{
                    requiresAuth: true,
                    title:'分院列表',
                    permissions:[
                        "dept:page"
                    ],
                },
            },
            {
                path: 'major',
                component:()=>import('../view/admin/base/AdminBaseMajor.vue'),
                meta:{
                    requiresAuth: true,
                    title:'专业列表',
                    permissions:[
                        "major:page"
                    ],
                }
            },
            {
                path: 'class',
                component:()=>import('../view/admin/base/AdminBaseClass.vue'),
                meta:{
                    requiresAuth: true,
                    title:'班级列表',
                    permissions:[
                        "class:page"
                    ],
                }
            }
        ]
    },
    {
        path:'/exp',
        meta:{
            requiresAuth:true,
            title:'实验管理',
            permissions:[
                "lab","course","teachingCore","courseSchedule","project","report","reportReview"
            ],
        },
        children:[
            {
                path: 'exp-lab',
                component: () => import('../view/admin/exp/AdminExpLab.vue'),
                meta: {
                    requiresAuth: true,
                    title: '实验室列表',
                    permissions: [
                        "lab:page"
                    ],
                },
            },
            {
                path:'exp-course',
                component:()=>import('../view/admin/exp/AdminExpCourse.vue'),
                meta:{
                    requiresAuth: true,
                    title:'开设课程列表',
                    permissions:[
                        "course:page"
                    ],
                },
            },
            {
                path:'exp-teaching-core',
                component:()=>import('../view/admin/exp/AdminExpTeachingCore.vue'),
                meta:{
                    requiresAuth: true,
                    title:'教学课程列表',
                    permissions:[
                        "teachingCore:page"
                    ],
                },
            },
            {
                path: 'exp-course-schedule',
                component:()=>import('../view/admin/exp/AdminExpCourseSchedule.vue'),
                meta:{
                    title:'课程安排列表',
                    permissions:[
                        "courseSchedule:page"
                    ],
                }
            },
            {
                path: 'exp-project',
                component:()=>import('../view/admin/exp/AdminExpProject.vue'),
                meta:{
                    requiresAuth: true,
                    title:'实验项目列表',
                    permissions:[
                        "project:page"
                    ],
                }
            },
            {
                path:'exp-report',
                component:()=>import('../view/admin/exp/AdminExpReport.vue'),
                meta:{
                    requiresAuth: true,
                    title:'报告列表',
                    permissions:[
                        "report:page"
                    ],
                },
            },
            {
                path: 'exp-report-review',
                component:()=>import('../view/admin/exp/AdminExpReportReview.vue'),
                meta:{
                    requiresAuth: true,
                    title:'批改列表',
                    permissions:[
                        "reportReview:page"
                    ],
                }
            }
        ]
    },
    // {
    //     path: '/dict',
    //     meta: {
    //         requiresAuth: true,
    //         title: '字典管理',
    //         permissions:[
    //             "dict"
    //         ],
    //     },
    //     children: [
    //         {
    //             path: '',
    //             component:()=>import('../view/admin/dict/AdminSysDict.vue'),
    //             meta:{
    //                 requiresAuth: true,
    //                 title:'字典列表',
    //                 permissions:[
    //                     "dict:page"
    //                 ],
    //             }
    //         }
    //     ]
    // },
    // {
    //     path: '/notice',
    //     meta: {
    //         requiresAuth: true,
    //         title: '通知管理',
    //         permissions:[
    //             "notice","noticeConfirm"
    //         ],
    //     },
    //     children: [
    //         {
    //             path: '',
    //             component:()=>import('../view/admin/notice/AdminSysNotice.vue'),
    //             meta:{
    //                 requiresAuth: true,
    //                 title:'通知列表',
    //                 permissions:[
    //                     "notice:page"
    //                 ],
    //             }
    //         },
    //         {
    //             path: 'confirm',
    //             component:()=>import('../view/admin/notice/AdminSysNoticeConfirm.vue'),
    //             meta:{
    //                 title:'通知确定列表',
    //                 permissions:[
    //                     "noticeConfirm:page"
    //                 ],
    //             }
    //         }
    //     ]
    // },
    // {
    //     path: '/auto',
    //     meta: {
    //         requiresAuth: true,
    //         title: '智能排课',
    //         permissions:[
    //             "notice","noticeConfirm"
    //         ],
    //     },
    //     children: [
    //         {
    //             path: 'schedule',
    //             component:()=>import('../view/admin/autoschedule/AutoSchedule.vue'),
    //             meta:{
    //                 requiresAuth: true,
    //                 title:'排课使用',
    //                 permissions:[
    //                     "notice:page"
    //                 ],
    //             }
    //         }
    //     ]
    // }
].map(
    (item,index)=>{
         item.meta.icon=new URL(`../assets/admin/${index+1}.png`, import.meta.url).href;
         return {...item}
    }
)