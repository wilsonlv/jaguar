# jaguar

#### 介绍
jaguar是基于springboot + springcloud + alibaba cloud的微服务开发框架
- 核心框架：Spring Boot + Spring Cloud + Alibaba Cloud + MyBatis-Plus
- 注册中心：Nacos
- 流量控制：Sentinel
- 服务监控：Spring Boot Admin
- 缓存管理：Redis
- 数据库连接池：Druid
- 安全框架：Spring Security + OAuth2
- 任务调度：Xxl-Job
- 消息中间件：Activemq
- 消息推送：Websocket
- 接口文档：Swagger
- 日志管理：Logback


#### 软件架构
jaguar由四大模块组成，分别是基础通用模块、代码生成模块、微服务模块和支持模块，模块说明如下：

- jaguar-commons：基础通用模块，该模块包含以下子模块
    - jaguar-commons-activemq：activemq消息组件
    - jaguar-commons-aviator：表达式解析组件
    - jaguar-commons-base-crud：基础增删改查
    - jaguar-commons-bom：框架通用版本
    - jaguar-commons-captcha：验证码组件
    - jaguar-commons-cloud-client：微服务组件
    - jaguar-commons-encryption：数据加密组价
    - jaguar-commons-enums：基础枚举类
    - jaguar-commons-export-csv：csv导出
    - jaguar-commons-feign：微服务接口调用组件
    - jaguar-commons-monitor-client：监控客户端组件
    - jaguar-commons-mybatis-plus：持久层组件
    - jaguar-commons-oauth2：OAuth2组件
    - jaguar-commons-pdf：pdf生成组件
    - jaguar-commons-pinyin：汉语拼音组件
    - jaguar-commons-qrcode：二维码生成组件
    - jaguar-commons-redis：redis组件
    - jaguar-commons-redis-cache：基于redis的二级缓存组件
    - jaguar-commons-swagger：接口文档组件
    - jaguar-commons-tenant：多租户组件
    - jaguar-commons-web：web服务组件
     
- jaguar-cloud：微服务模块
    - jaguar-auth：认证服务
    - jaguar-handler-log：接口日志服务
    - jaguar-job：任务调度服务
    - jaguar-monitor：服务监控
    - jaguar-nginx-admin：nginx控制台
    - jaguar-register：nacos注册中心
    - jaguar-sentinel：流量控制
    - jaguar-upms：用户权限管理服务
    - jaguar-websocket：消息推送服务