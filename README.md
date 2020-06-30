# jaguar

#### 介绍
jaguar是基于springboot V2.2.0-RELEASE 的开发框架，主要技术选型有
- 核心框架：Spring Boot + Spring Framework + MyBatis-Plus
- 缓存管理：Redis
- 数据库连接池：Alibaba Druid
- 安全框架：Apache Shiro
- 分布式框架：Dubbo
- 流程框架：Flowable
- 接口文档：Swagger
- 日志管理：Logback

#### 软件架构
jaguar包含多个子模块，并且后续还会可以持续整合更多的通用模块，主要子模块说明：

- jaguar-commons：公共组件
    
- commons-aviator：整合了google的aviator，是表达式解析组件

- commons-data-encription：数据加密组价，主要包含DES对称加密，RSA非对称加密，MD5和BASE64加密算法

- commons-dubbo：整合了dubbo框架，依赖于jaguar-core，封装了调用dubbo服务的中间件，
    以减少大量provider的接口定义与层次调用关系；如果开发人员想搭建分布式结构的项目，
    引入此模块无疑是你最好的选择

- commons-malice：防范恶意请求的功能模块，使用到了spring的拦截器

- commons-mybatis-plus：整合了mybatis-plus与druid数据库连接池作为持久层组件
    
- commons-redis：整合了redis
    
- commons-shiro：整合了shiro，作为框架的权限控制组件
    
- commons-swagger：整合了swagger，作为框架的接口文档生成与调试工具


- jaguar-core：jaguar的核心模块，依赖于commons-mybatis-plus，commons-shiro与commons-utils，
    包含实体类、持久层、业务层、控制层在内的基础组件；定义了一些基础异常类和响应码；是快速搭建生产级项目的启动器
     
     
- jaguar-modules：通用的系统模块

- workflow：整合了flowable，内置表单和流程设计器，是完整的一套包含设计、部署、发起、查询、处理的
    工单流程系统，用作工作流的功能模块

- handler-log：通过spring拦截器来实现的接口请求日志记录


- jaguar-test：jaguar模块测试


#### 安装教程

1. 下载jaguar框架，https://gitee.com/Wilson_Lws/jaguar.git
2. 在框架的目录下执行mvn clean install命令
3. 创建自己的项目，引入以下依赖，快速开始开发

        <dependencyManagement>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-dependencies</artifactId>
                    <version>2.0.1.RELEASE</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
            </dependencies>
        </dependencyManagement>
        
        <dependencies>
            <!-- springboot启动器 -->
            <dependency>
                <groupId>org.jaguar</groupId>
                <artifactId>jaguar-core</artifactId>
                <version>${jaguar.version}</version>
            </dependency>
        </dependencies>