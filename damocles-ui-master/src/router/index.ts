import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'
import LoginRegister from '../views/LoginRegister.vue'
import { request, responseEntity } from '../utils/request'

const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        children: [
            {
                path: '/authority/index',
                name: 'Authority',
                component: () => import('@/views/Authority.vue')
            },
            {
                path: '/authority/user',
                name: 'AuthorityUser',
                component: () => import('@/views/AuthorityUser.vue')
            },
            {
                path: '/authority/role',
                name: 'AuthorityRole',
                component: () => import('@/views/AuthorityRole.vue')
            },
            {
                path: '/user/userinfo',
                name: 'Userinfo',
                component: () => import('@/views/Userinfo.vue')
            },
            {
                path: '/user/team',
                name: 'UserTeam',
                component: () => import('@/views/UserTeam.vue')
            },
            {
                path: '/user/signin',
                name: 'UserSignin',
                component: () => import('@/views/UserSignin.vue')
            },
            {
                path: '/user/signRecord',
                name: 'UserSignRecord',
                component: () => import('@/views/UserSignRecord.vue')
            },
            {
                path: '/user/dispatchRecord',
                name: 'UserDispatchRecord',
                component: () => import('@/views/UserDispatchRecord.vue')
            },
            {
                path: '/daily/record',
                name: 'DailyRecord',
                component: () => import('@/views/DailyRecord.vue')
            },
            {
                path: '/daily/checkRecord',
                name: 'DailyCheck',
                component: () => import('@/views/DailyCheck.vue')
            },
            {
                path: '/daily/userRecord',
                name: 'DailyUserRecord',
                component: () => import('@/views/DailyUserRecord.vue')
            },
            {
                path: '/daily/list',
                name: 'DailyList',
                component: () => import('@/views/DailyList.vue')
            },
            {
                path: '/word/myWord',
                name: 'MyWord',
                component: () => import('@/views/WordMe.vue')
            },
            {
                path: '/word/record',
                name: 'WordRecord',
                component: () => import('@/views/WordRecord.vue')
            },
            {
                path: '/word/collect',
                name: 'WordCollect',
                component: () => import('@/views/WordCollect.vue')
            },
            {
                path: '/myProfile',
                name: 'MyProfile',
                component: () => import('@/views/MyProfile.vue')
            },
            {
                path: '/changePassword',
                name: 'ChangePassword',
                component: () => import('@/views/ChangePassword.vue')
            },
            {
                path: '/course/myCourse',
                name: 'MyCourse',
                component: ()=>import('@/views/MyCourse.vue')
            },
            {
                path: '/course/myLesson',
                name: 'MyLesson',
                component: ()=>import('@/views/MyLesson.vue')
            },
            {
                path: '/course/allCourse',
                name: 'AllCourse',
                component: ()=>import('@/views/AllCourse.vue')
            },
        ],
    },
    {
        path: '/logout',
        name: 'Logout',
        component: LoginRegister
    },
    {
        path: '/login',
        name: 'Login',
        component: LoginRegister
    },
    {
        path: '/forget',
        name: 'ForgetPassword',
        component: () => import('@/views/ForgetPassword.vue')
    },
    {
        path: '/:catchAll(.*)',
        name: '/404',
        component: () => import('@/views/404.vue')
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

const verify = (items: any, path: string): boolean => {
    for (const item of items) {
        if (item.uri === path) return true;
        if (item.children) {
            if (verify(item.children, path)) return true;
        }
    }
    return false;
}


// 权限认证 - 路由拦截
router.beforeEach((to, form, next) => {
    const targetPath = to.path;
    const excludePath = ['/login', '/logout', '/404', '/403', '/myProfile', '/changePassword', '/forget'];
    if (targetPath === '/forget' && !to.query.forgetToken) {
        console.log(to.query.forgetToken);
        return next('/login');
    }
    for (const path of excludePath) {
        if (targetPath === path) return next();
    }
    request('/authority/menuData').then((response) => {
        console.log(response);
        const res: responseEntity = response as responseEntity;
        if (res.code === 0) {
            if (targetPath === '/' || verify(res.data, targetPath)) {
                return next();
            }
        }
    }).catch((error) => {
        // TODO 处理异常页面重定向
        console.log(error);
        return next('/login');
    });
})


export default router
