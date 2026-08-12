export const frontConstantRoutes=[
    {
        path:'/exp-course-schedule',
        meta:{
            requiresAuth:true,
            title:'课程安排',
            permissions:[
                "courseSchedule"
            ],
        },
        children:[
            {
                path: 'list',
                component:()=>import('../view/front/schedule/List.vue'),
                meta:{
                    requiresAuth: true,
                    title:'课程安排列表',
                    permissions:[
                        "courseSchedule:page"
                    ],
                }
            }
        ]
    },
    {
        path:'/exp-project',
        meta:{
            requiresAuth:true,
            title:'实验项目',
            permissions:[
                "project"
            ],
        },
        children:[
            {
                path: 'list',
                component: () => import('../view/front/project/List.vue'),
                meta: {
                    requiresAuth: true,
                    title: '实验项目列表',
                    permissions: [
                        "project:page"
                    ],
                }
            },
        ]
    },
    {
        path:'/exp-report-template',
        meta:{
            requiresAuth:true,
            title:'实验模版',
            permissions:[
                "report"
            ],
        },
        children:[
            {
                path:'list',
                component:()=>import('../view/front/template/List.vue'),
                meta:{
                    requiresAuth: true,
                    title:'模版列表',
                    permissions:[
                        "report:page:template"
                    ],
                },
            }
        ]
    },
    {
        path:'/exp-report',
        meta:{
            requiresAuth:true,
            title:'实验报告',
            permissions:[
                "report"
            ],
        },
        children:[
            {
                path:'un-committed-list',
                component:()=>import('../view/front/report/UnSubmittedList.vue'),
                meta:{
                    requiresAuth: true,
                    title:'未提交列表',
                    permissions:[
                        "report:page:unSubmitted"
                    ],
                },
            },
            {
                path:'committed-list',
                component:()=>import('../view/front/report/SubmittedList.vue'),
                meta:{
                    requiresAuth: true,
                    title:'已提交列表',
                    permissions:[
                        "report:page:submitted"
                    ],
                },
            }
        ]
    },
    {
        path:'/exp-report-review',
        meta:{
            requiresAuth:true,
            title:'批阅中心',
            permissions:[
                "reportReview"
            ],
        },
        children:[
            {
                path: 'un-reviewed-list',
                component:()=>import('../view/front/review/UnReviewList.vue'),
                meta:{
                    requiresAuth: true,
                    title:'待批改列表',
                    permissions:[
                        "reportReview:page:unReviewed"
                    ],
                }
            },
            {
                path: 'reviewed-list',
                component:()=>import('../view/front/review/ReviewList.vue'),
                meta:{
                    requiresAuth: true,
                    title:'已批改列表',
                    permissions:[
                        "reportReview:page:reviewed"
                    ],
                }
            }
        ]
    }
].map(
    (item,index)=>{
        item.meta.icon=new URL(`../assets/front/${index+1}.png`, import.meta.url).href;
        return {...item}
    }
)