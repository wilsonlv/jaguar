# jaguar

#### 介绍
jaguar是基于springboot V2.0.1的开发框架，主要技术选型有
- 核心框架：Spring Boot + Spring Framework + MyBatis-Plus
- 缓存管理：Redis
- 数据库连接池：Alibaba Druid
- 安全框架：Apache Shiro
- 分布式框架：Dubbo
- 流程框架：Flowable
- 接口文档：Swagger
- 日志管理：Logback

#### 软件架构
jaguar包含多个子模块，并且后续会可以一直容纳，主要子模块说明：

- jaguar-mybatis-plus：整合了mybatis-plus与druid数据库连接池作为持久层组件；
    使用mybatis-plus-generator + apache velocity来生成从实体类到控制层的基础crud代码，
    是jaguar的基础模块
    
- jaguar-redis：整合了redis。1、作为框架实体类的二级缓存；2、shiro-session的共享存储。
    对于本框架有着举足轻重的作用，是jaguar的基础模块
    
- jaguar-shiro：整合了shiro，作为框架的权限控制组件，依赖于jaguar-redis，是jaguar的基础模块
    
- jaguar-core：依赖于jaguar-redis，是jaguar的核心模块，包含实体类、持久层、业务层在内的基础组件；
    实现了以redis作为二级缓存的快速查询；定义了基础异常类和响应码，以及一些通用的工具类
    
- jaguar-web：依赖于jaguar-core和jaguar-shiro，定义了控制层的基础组件，以及接口的返回格式；
    开发人员可以通过引入该模块来快速搭建开发环境
    
- jaguar-dubbo：整合了dubbo框架，依赖于jaguar-core，封装了调用dubbo服务的中间件，
    以减少大量provider的接口定义与层次调用关系；如果开发人员想搭建分布式结构的项目，
    引入此模块无疑是你最好的选择
    
- jaguar-swagger：整合了swagger，作为框架的接口文档生成与调试工具

- jaguar-sys-log：通过spring拦截器来实现的接口请求日志记录

- jaguar-malice：防范恶意请求的功能模块，使用到了spring的拦截器

- jaguar-aviator：整合了google的aviator，是表达式解析组件

- jaguar-process：整合了flowable，内置表单和流程设计器，是完整的一套包含设计、部署、发起、查询、处理的
    工单流程系统，用作工作流的功能模块

- jaguar-data-encription：数据加密组价，主要包含DES对称加密，RSA非对称加密，MD5和BASE64加密算法

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
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
            </dependency>
        
            <dependency>
                <groupId>com.itqingning</groupId>
                <artifactId>jaguar-web</artifactId>
                <version>${jaguar.version}</version>
            </dependency>
        
            <dependency>
                <groupId>com.itqingning</groupId>
                <artifactId>jaguar-swagger</artifactId>
                <version>${jaguar.version}</version>
            </dependency>
        
            <dependency>
                <groupId>com.itqingning</groupId>
                <artifactId>jaguar-sys-log</artifactId>
                <version>${jaguar.version}</version>
            </dependency>
        </dependencies>