import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView'
import RecordIndexView from '../views/record/RecordIndexView'
import RanklistIndexView from '../views/ranklist/RanklistIndexView'
import UserBotIndexView from '../views/user/bot/UserBotIndexView'
import NotFoundView from '../views/error/NotFoundView'
import UserAccountLoginView from '../views/user/account/UserAccountLoginView'
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView'



const routes = [
  // redirection root 
  {
    path: '/',
    name: "home",
    redirect: "/pk/",
  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PkIndexView,
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
  },
  {
    path: "/ranklist",
    name: "ranklist_index",
    component: RanklistIndexView,

  },
  {
    path: "/user/",
    name: "user_bot_index",
    component: UserBotIndexView,
  },
  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView,
  },
  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegisterView,
  },
  {
    path: "/404/",
    name: "404",
    component: NotFoundView,
  },

  // all other illegal webpages
  {
    path: "/:catchAl(.*)",
    redirect: "/404/"
  }

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router