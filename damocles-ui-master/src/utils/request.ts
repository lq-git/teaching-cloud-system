import axios, { Method } from "axios";
import { ElNotification, ElMessageBox, ElMessage } from 'element-plus';
import qs from "qs";
import responseEntity from '../types/responseEntity'
axios.defaults.baseURL = '/api' //设置默认路由访问路径以api开头

//post请求头
axios.defaults.headers.post["Content-Type"] =
    "application/x-www-form-urlencoded;charset=UTF-8";
//设置超时
axios.defaults.timeout = 50000; //设置超时时间

// 请求之前的配置
axios.interceptors.request.use(
    config => {
        const token = localStorage.getItem("token");
        if (token) {
            config.headers.Authorization = token;//把localStorage的token放在Authorization里
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// 响应之前的配置
axios.interceptors.response.use(
    response => {
        if (response.status == 200) {
            return Promise.resolve(response);
        } else {
            return Promise.reject(response);
        }
    }
);

// 发送请求
/**
 * 
 * @param url 请求地址
 * @param data 携带的请求参数
 * @param type 请求类型
 * @param serialized 是否序列化
 */
const request = (url: string, data?: object, type?: Method, serialized?: boolean) => {
    type = type ? type : 'GET';
    const isParams: boolean = type === 'get' || type === 'GET' || type === 'delete' || type === 'DELETE';
    return new Promise((resolve, reject) => {
        axios({
            headers: {
                'Content-Type': serialized ? 'application/x-www-form-urlencoded' : 'application/json'
            },
            method: type,
            url,
            data: !isParams ? (serialized ? qs.stringify(data, { arrayFormat: 'repeat' }) : JSON.stringify(data)) : null,
            params: isParams ? data : null
        })
            .then(res => {
                resolve(res.data)
            })
            .catch(err => {
                reject(err)
            });
    })
}

// 请求后的响应消息回调
const responseMsg = {
    success: (response: any, title: string, callback?: Function) => {
        const res = response as responseEntity;
        if (res.code === 0) {
            ElNotification({
                type: 'success',
                title: title + "成功",
                message: res.msg,
            })
            return callback ? callback() : 0;
        }
        ElNotification({
            type: 'error',
            title: title + "失败",
            message: res.msg,
        });
    },
    error: (error: any, title: string) => {
        ElNotification({
            type: 'error',
            title: title + "失败",
            message: error.response.statusText,
        })
    },
    confirmBack: (response: any, title: string, callback?: Function) => {
        const res = response as responseEntity;
        if (res.code === 0) {
            ElMessage({
                type: 'success',
                message: title.concat('成功!')
            });
            return callback ? callback(response) : 0;
        }
        ElMessage({
            type: 'error',
            message: title.concat('失败!')
        });
    },
    confirm: (callback: Function, title: string) => {
        ElMessageBox.confirm('确定要' + title + '吗, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            callback(responseMsg.confirmBack);
        }).catch(() => {
            ElMessage({
                type: 'info',
                message: '已取消'.concat(title)
            });
        });
    }
}

const download = (url: string, data?: object) => {
    axios({
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        url: url,
        data: JSON.stringify(data),
        responseType: 'blob'
    }).then(response => {
        const data = response.data;
        console.log(response);
        if (data) {
            // const url = window.URL.createObjectURL(new Blob([]))
            // const link = document.createElement('a')
            // link.style.display = 'none'
            // link.href = url
            // const contentDisposition = response.headers["content-disposition"]; //从response的headers中获取filename, 后端response.setHeader("Content-disposition", "attachment; filename=xxxx.docx") 设置的文件名;
            // const patt = new RegExp("filename=([^;]+\\.[^\\.;]+);*");
            // const result = patt.exec(contentDisposition);
            // const fileName = result ? decodeURI(result[1]) : 'download.txt';
            // link.setAttribute('download', fileName)
            // document.body.appendChild(link)
            // link.click()
            const blob = new Blob([data], {
                type:
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8",
            });
            const downloadElement = document.createElement("a");
            const href = window.URL.createObjectURL(blob);
            const contentDisposition = response.headers["content-disposition"]; //从response的headers中获取filename, 后端response.setHeader("Content-disposition", "attachment; filename=xxxx.docx") 设置的文件名;
            const patt = new RegExp("filename=([^;]+\\.[^\\.;]+);*");
            const result = patt.exec(contentDisposition);
            const fileName = result ? decodeURI(result[1]) : 'download.txt';
            downloadElement.style.display = "none";
            downloadElement.href = href;
            downloadElement.download = fileName; //下载后文件名
            document.body.appendChild(downloadElement);
            downloadElement.click(); //点击下载
            document.body.removeChild(downloadElement); //下载完成移除元素
            window.URL.revokeObjectURL(href); //释放掉blob对象
        }

    }).catch((error) => {
        responseMsg.error(error, '下载');
    })
}

// 导出接口
export {
    request,
    responseEntity,
    responseMsg,
    download
}
