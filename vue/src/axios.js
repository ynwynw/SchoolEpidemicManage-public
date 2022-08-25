import axios from "axios";
import Element from 'element-ui';
import router from "./router";

axios.defaults.baseURL = "http://localhost:8081"
axios.defaults.withCredentials = false

const request = axios.create({
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json; charset=utf-8'
    }
});

request.interceptors.request.use(config => {
    config.headers['Authorization'] = localStorage.getItem("token")
    return config
});

request.interceptors.response.use(response => {
    let res = response.data;
    if (res.code === 200) {
        return response;
    } else {
        Element.Message.error(res.msg ? res.msg : '系统异常！', {
            duration: 3 * 1000
        });
        return Promise.reject(response.data.msg);
    }
}, error => {
    if (error.response.data) {
        error.message = error.response.data.msg;
    }
    if (error.response.status === 401) {
        if (router.currentRoute.path !== '/login'){
            router.push({path: '/login'})
        }
    }
    let doms= document.getElementsByClassName('el-message')[0]
    if (doms === undefined){
        Element.Message.error(error.message ? error.message : '操作失败', {
            duration: 3 * 1000
        });
    }
    return Promise.reject(error)
})

export default request
