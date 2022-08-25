import Vue from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'
import './plugins/element.js'
import modal from "./plugins/modal";
import Pagination from "@/components/Pagination";
import axios from './axios'

Vue.prototype.$axios = axios
Vue.config.productionTip = false
Vue.prototype.modal = modal
Vue.component('Pagination', Pagination)


const hasPermission = permission => {
  let permissionList = Array.isArray(permission) ? permission : [permission]
  let userPermission = store.state.menu.authoritys
  return userPermission.some(perm => permissionList.includes(perm))
}

const isAdmin = () => {
  let userRole = store.state.menu.authoritys
  return userRole.some(role => role === "ROLE_admin")
}

Vue.directive('permission', {
  inserted: (el, binding, vnode) => {
    if (!hasPermission(binding.value)){
      el.parentNode && el.parentNode.removeChild(el)
    }
  }
})

Vue.directive('isAdmin', {
  inserted: (el, binding, vnode) => {
    if (isAdmin()){
      el.parentNode && el.parentNode.removeChild(el)
    }
  }
})

Vue.prototype.$_has = hasPermission

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
