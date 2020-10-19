# jaguar

#### 介绍
jaguar是基于springboot V2.2.0-RELEASE 的开发框架，主要技术选型有
- 核心框架：Spring Boot + Spring Framework + MyBatis-Plus
- 缓存管理：Redis
- 数据库连接池：Alibaba Druid
- 安全框架：Apache Shiro
- 分布式框架：Dubbo
- 接口文档：Swagger
- 日志管理：Logback

#### 软件架构
jaguar又四大模块构成，分别是基础通用模块，核心模块，项目模块和支持模块，并且后续还会可以持续整合更多，模块说明：

- jaguar-commons：基础通用模块，该模块包含以下子模块

    - commons-activemq：整合了apache的activemq，是消息组件
    - commons-aviator：整合了google的aviator，是表达式解析组件
    - commons-data-encription：数据加密组价，主要包含DES对称加密，RSA非对称加密，MD5和BASE64加密算法
    - commons-mybatis-plus：整合了mybatis-plus与druid数据库连接池作为持久层组件
    - commons-qrcode：整合了google的zxing，二维码生成组件
    - commons-redis：整合了redis，可以共享session，可以做缓存
    - commons-shiro：整合了shiro，作为框架的权限控制组件
    - commons-swagger：整合了swagger，作为框架的接口文档生成与调试工具
    - commons-utils：基础工具组件
    - commons-websocket：整合了websocket，后台向前台的实时消息推送

- jaguar-core：jaguar的核心模块，依赖于commons-mybatis-plus，commons-shiro，commons-swagger与commons-utils，
    包含实体类、持久层、业务层、控制层在内的基础组件；定义了一些基础异常类和响应码；是快速搭建生产级项目的启动器
     
- jaguar-modules：项目模块

    - code-generator：代码生成模块，快速生成entity、mapper、mapper xml、service和controller
    - document：文档管理模块，包括文档的上传下载，数据的存储、关联和查询
    - jasper-report：报表生成模块
    - numbering：编号规则模块
    - system-mgm：后台管理系统模块，包含基本的用户管理、角色管理、菜单管理、权限管理、登录管理
    - workflow：整合了flowable，内置表单和流程设计器，是完整的一套包含设计、部署、发起、查询、处理的
        工单流程系统，用作工作流的功能模块

- jaguar-support：支持模块

    - dubbo：整合了dubbo框架，依赖于jaguar-core，封装了调用dubbo服务的中间件，
            以减少大量provider的接口定义与层次调用关系；如果开发人员想搭建分布式结构的项目，
            引入此模块无疑是你最好的选择
    - handler-log：通过spring拦截器来实现的接口请求日志记录
    - malice-prevention：防范恶意请求的功能模块，使用到了spring的拦截器
    - redis-cache：基于redis的实体类二级缓存

#### 安装教程

1. 下载jaguar框架，https://gitee.com/Wilson_Lws/jaguar.git
2. 在框架的目录下执行mvn clean install命令
3. 创建自己的项目，引入以下依赖，快速开始开发

        <dependencyManagement>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-dependencies</artifactId>
                    <version>2.2.0.RELEASE</version>
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