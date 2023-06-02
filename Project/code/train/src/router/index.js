import { createRouter, createWebHistory } from 'vue-router'
import login from '../views/login.vue'
import sign from '../views/sign.vue'
import forget from '../views/forget.vue'
import home from '../views/home.vue'
import main from "../components/Main.vue"
import buyTickets from "../components/BuyTickets.vue"
import Tickets from "../components/Tickets.vue"
const routes = [
  {
    path: '/',
    name: 'login',
    component: login
  },
  {
    path: '/home',
    name: 'home',
    // redirect:`/main?user_id=000`,//重定向

    component: () => import(/* webpackChunkName: "about" */ '../views/home.vue'),
    children: [
      {
        path: '/main',
        name: 'main',
        component: () => import(/* webpackChunkName: "about" */ '../components/Main.vue'),
      },
      {
        path: '/buyTickets',
        name: 'buyTickets',
        component: () => import(/* webpackChunkName: "about" */ '../components/BuyTickets.vue')

      },
      {
        path: '/loginTime',
        name: 'loginTime',
        component: () => import(/* webpackChunkName: "about" */ '../components/loginTime.vue'),
      },
      {
        path: '/Tickets',
        name: 'Tickets',
        component: () => import(/* webpackChunkName: "about" */ '../components/Tickets.vue'),
      },
      {
        path: '/SecondTickets',
        name: 'SecondTickets',
        component: () => import(/* webpackChunkName: "about" */ '../components/SecondTickets.vue'),
      },
      {
        path: '/emptyData',
        name: 'emptyData',
        component: () => import(/* webpackChunkName: "about" */ '../components/emptyData.vue'),
      },
      {
        path: '/MyPerson',
        name: 'MyPerson',
        component: () => import(/* webpackChunkName: "about" */ '../components/MyPerson.vue'),
      },
      {
        path: '/buyHistory',
        name: 'buyHistory',
        component: () => import(/* webpackChunkName: "about" */ '../components/buyHistory.vue'),
      },
      {
        path: '/changeTickets',
        name: 'changeTickets',
        component: () => import(/* webpackChunkName: "about" */ '../components/changeTickets.vue'),
      },
      {
        path: '/childChange',
        name: 'childChange',
        component: () => import(/* webpackChunkName: "about" */ '../components/childChange.vue'),
      },
      {
        path: '/info_ticket',
        name: 'info_ticket',
        component: () => import(/* webpackChunkName: "about" */ '../components/charts/info_ticket.vue'),
      },
      {
        path: '/ShortDis',
        name: 'ShortDis',
        component: () => import(/* webpackChunkName: "about" */ '../components/ShortDis.vue'),
      },
      {
        path: '/ticketCharts',
        name: 'ticketCharts',
        component: () => import(/* webpackChunkName: "about" */ '../components/charts/ticketCharts.vue'),
      },
    ]

  },
  {
    path: '/adminHome',
    name: 'adminHome',
    // redirect:`/main?user_id=000`,//重定向

    component: () => import(/* webpackChunkName: "about" */ '../components/admin/adminHome.vue'),
    children:[
      {
        path: '/adminAside',
        name: 'adminAside',
        component: () => import(/* webpackChunkName: "about" */ '../components/admin/adminAside.vue'), 
      },
      {
        path: '/allUsers',
        name: 'allUsers',
        component: () => import(/* webpackChunkName: "about" */ '../components/admin/allUsers.vue'),
      },
      {
        path: '/seats',
        name: 'seats',
        component: () => import(/* webpackChunkName: "about" */ '../components/admin/seats.vue'),
      },
      {
        path: '/TicketCenter',
        name: 'TicketCenter',
        component: () => import(/* webpackChunkName: "about" */ '../components/admin/TicketCenter.vue'),
      },
      {
        path: '/BlackUser',
        name: 'BlackUser',
        component: () => import(/* webpackChunkName: "about" */ '../components/admin/BlackUser.vue'),
      },
      {
        path: '/admin_info_ticket',
        name: 'admin_info_ticket',
        component: () => import(/* webpackChunkName: "about" */ '../components/admin/charts/admin_info_ticket.vue'),
      },
      {
        path: '/admin_ticket',
        name: 'admin_ticket',
        component: () => import(/* webpackChunkName: "about" */ '../components/admin/charts/admin_ticket.vue'),
      },
    ]
  },
  {
    path: '/sign',
    name: 'sign',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/sign.vue')
  },
  {
    path: '/forget',
    name: 'forget',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/forget.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

router.beforeEach((to, from, next) => {
  // 1.如果访问的是登录页面（无需权限），直接放行
  if (to.path === "/"||to.path === "/forget"||to.path === "/sign") return next()
  // 2.如果访问的是有登录权限的页面，先要获取token
  const tokenStr = window.sessionStorage.getItem('token')
  // 2.1如果token为空，强制跳转到登录页面；否则，直接放行
  if (!tokenStr) {
    alert("请登录")
    return next('/')
  }
  next()
})
