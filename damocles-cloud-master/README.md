## damocles-cloud简介

* 采用前后端分离的模式，微服务版本前端(基于 [damocles-ui](https://gitee.com/moonholder/damocles-ui))。
* 后端采用Spring Boot、Spring Cloud & Alibaba。
* 注册中心、配置中心选型Nacos，权限认证使用Jwt。
* 如需单体项目，请移步 [damocles](https://gitee.com/moonholder/damocles)

## 系统模块

~~~
org.damocles     
├── damocles-ui              // 前端框架 [80]
├── damocles-gateway         // 网关模块 [8080]
├── damocles-security        // 认证中心 [9000]
├── damocles-file            // 文件服务 [9015]
├── damocles-common          // 通用模块
│       └── damocles-common-core                         // 核心模块
│       └── damocles-common-datasource                   // 数据源
│       └── damocles-common-feign                        // 服务调用
│       └── damocles-common-redis                        // 缓存服务
│       └── damocles-common-task                         // 动态任务
├── damocles-modules         // 业务模块
│       └── damocles-authority                            // 权限管理 [9011]
│       └── damocles-student                              // 学员管理 [9012]
│       └── damocles-daily                                // 日报管理 [9013]
│       └── damocles-word                                 // 单词管理 [9014]
├── damocles-admin          // 应用监控
├──pom.xml                // 公共依赖
~~~


