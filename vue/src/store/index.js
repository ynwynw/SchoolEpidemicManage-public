import Vue from 'vue'
import Vuex from 'vuex'
import side from './side'
import menu from './menu'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: '',
    userInfo: null
  },
  modules: {
    side,
    menu
  },
  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
      localStorage.setItem('token', token)
    },
    SET_USER_INFO: (state, userInfo) => {
      state.userInfo = userInfo
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    },
    clearData: (state) => {
      state.token = null
    }
  }
})
