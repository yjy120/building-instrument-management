import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    name: 'Main',
    component: () => import('../views/Main.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue')
      },
      {
        path: '/device',
        name: 'Device',
        component: () => import('../views/Device.vue')
      },
      {
        path: '/stock',
        name: 'Stock',
        component: () => import('../views/Stock.vue')
      },
      {
        path: '/purchase',
        name: 'Purchase',
        component: () => import('../views/Purchase.vue')
      },
      {
        path: '/use',
        name: 'UseRecord',
        component: () => import('../views/UseRecord.vue')
      },
      {
        path: '/calibration',
        name: 'Calibration',
        component: () => import('../views/Calibration.vue')
      },
      {
        path: '/repair',
        name: 'Repair',
        component: () => import('../views/Repair.vue')
      },
      {
        path: '/scrap',
        name: 'Scrap',
        component: () => import('../views/Scrap.vue')
      },
      {
        path: '/user',
        name: 'User',
        component: () => import('../views/User.vue')
      },
      {
        path: '/review',
        name: 'Review',
        component: () => import('../views/Review.vue')
      },
      {
        path: '/my-records',
        name: 'MyRecords',
        component: () => import('../views/MyRecords.vue')
      },
      {
        path: '/issue-report',
        name: 'DeviceIssueReport',
        component: () => import('../views/DeviceIssueReport.vue')
      },
      {
        path: '/issue-handle',
        name: 'IssueReportHandle',
        component: () => import('../views/IssueReportHandle.vue')
      },
      {
        path: '/report',
        name: 'Report',
        component: () => import('../views/Report.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
