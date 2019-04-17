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
- 日志管理：SLF4J、Log4j2

#### 软件架构
jaguar包含多个子模块，并且后续会可以一直容纳，主要子模块说明：

- jaguar-core：是jaguar的核心模块，包含实体类、持久层、业务层在内的基础组件；
    实现了以redis作为二级缓存的快速查询；定义了基础异常类和响应码，以及一些通用的工具类
- jaguar-redis：整了redis。1、作为框架的二级缓存；2、spring-session的存储。对于本框架有着举足轻重的作用
- jaguar-mybatis-plus：整合了mybatis-plus与数据库连接池作为持久层组件
- jaguar-web：依赖于jaguar-core、jaguar-mybatis-plus，定义了控制层的基础组件，以及接口的返回格式；
    开发人员可以通过引入该模块来快速搭建开发环境
- jaguar-dubbo：整合了dubbo框架，定义调用dubbo服务的中间件，以减少大量provider的接口定义与层次调用关系；
    如果开发人员想搭建分布式结构的项目，引入此模块无疑是你最好的选择
- jaguar-sys-log：通过spring拦截器来实现的接口请求日志记录

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
            <!-- springboot 启动器 -->
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
                <artifactId>jaguar-shiro</artifactId>
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