module.exports = {
    publicPath: '/',
    devServer: {
        port: 80,
        proxy: {
            '/api': {
                //代理以 api 开头的请求        
                target: 'http://127.0.0.1:8080/',
                //当前请求地址代理到配置的目标地址上 
                ws: true,
                changeOrigin: true,
                pathRewrite: {
                    '^/api': ''
                }
            }
        }
    }
}