# jaguar

#### 介绍
jaguar是基于springboot + spring cloud + alibaba cloud的微服务开发框架
- 核心框架：Spring Boot + Spring Cloud + Alibaba Cloud + MyBatis-Plus
- 注册中心：Nacos
- 配置中心：Nacos Config 
- 微服务调用：Open Feign 
- 流量控制：Sentinel
- 服务监控：Spring Boot Admin
- 网关管理：Nginx + Nginx Admin
- 安全框架：Spring Security + OAuth2
- 缓存管理：Redis
- 数据库连接池：Druid
- 任务调度：Xxl-Job
- 消息中间件：Activemq
- 消息推送：Websocket
- 接口文档：Swagger
- 日志打印：Logback
- 数据库：Mysql5.7 + ElasticSearch


#### 软件架构
jaguar由三大模块组成，分别是基础通用模块、代码生成模块和微服务模块

- jaguar-commons：基础通用模块
    - jaguar-commons-activemq：activemq消息组件
    - jaguar-commons-aviator：表达式解析组件
    - jaguar-commons-cloud-client：微服务组件
    - jaguar-commons-encryption：数据加密组价
    - jaguar-commons-export-csv：csv导出
    - jaguar-commons-monitor-client：监控客户端组件
    - jaguar-commons-mybatis-plus：持久层组件
    - jaguar-commons-pdf：pdf生成组件
    - jaguar-commons-pinyin：汉语拼音组件
    - jaguar-commons-qrcode：二维码生成组件
    - jaguar-commons-redis：redis组件
    - jaguar-commons-web：web服务组件

- jaguar-support：扩展增强模块
    - jaguar-base-crud：基础增删改查
    - jaguar-captcha：验证码组件
    - jaguar-data-modify-log：数据修改日志
    - jaguar-export-csv：csv流式导出
    - jaguar-openfeign：微服务接口调用组件
    - jaguar-redis-cache：redis二级缓存
    - jaguar-security-oauth2：OAuth2资源服务器
    - jaguar-tenant：多租户数据隔离查询

- jaguar-cloud：微服务模块
    - jaguar-auth：认证服务
    - jaguar-job：任务调度服务   
    - jaguar-monitor：服务监控
    - jaguar-register：注册中心
    - jaguar-sentinel：流量控制
    - jaguar-upms：用户权限管理服务
    - jaguar-websocket：消息推送服务
    - jaguar-handler-log：接口日志服务
  
- jaguar-codegen：代码生成模块
- jaguar-dependencies：框架通用版本


#### 使用说明
- 1、创建数据库
    - jaguar-cloud/jaguar-register/db/schema.sql
    - jaguar-cloud/jaguar-resource-servers/jaguar-upms/db/schema.sql
    - jaguar-cloud/jaguar-resource-servers/jaguar-job/jaguar-job-admin/db/schema.sql
    - jaguar-codegen/db/schema.sql
    - jaguar-nginx-admin/db/schema.sql
  
- 2、 配置host
    - 127.0.0.1       jaguar-mysql
    - 127.0.0.1       jaguar-es
    - 127.0.0.1       jaguar-redis
    - 127.0.0.1       jaguar-mq
    - 127.0.0.1       jaguar-register
    - 127.0.0.1       jaguar-sentinel
    - 127.0.0.1       jaguar-monitor
    - 127.0.0.1       jaguar-job-admin
  
- 3、启动服务
    - jaguar-codegen和jaguar-nginx-admin是单体项目，不依赖任何服务
    - jaguar-register是微服务的注册中心，启动微服务必须首先启动该服务