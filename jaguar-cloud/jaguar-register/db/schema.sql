CREATE DATABASE IF NOT EXISTS `jaguar_register` default character set utf8mb4 collate utf8mb4_unicode_ci;
use `jaguar_register`;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) NOT NULL COMMENT 'data_id',
    `group_id`     varchar(255) NULL     DEFAULT NULL,
    `content`      longtext     NOT NULL COMMENT 'content',
    `md5`          varchar(32)  NULL     DEFAULT NULL COMMENT 'md5',
    `gmt_create`   datetime(0)  NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `gmt_modified` datetime(0)  NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `src_user`     text         NULL COMMENT 'source user',
    `src_ip`       varchar(50)  NULL     DEFAULT NULL COMMENT 'source ip',
    `app_name`     varchar(128) NULL     DEFAULT NULL,
    `tenant_id`    varchar(128) NULL     DEFAULT '' COMMENT '租户字段',
    `c_desc`       varchar(256) NULL     DEFAULT NULL,
    `c_use`        varchar(64)  NULL     DEFAULT NULL,
    `effect`       varchar(64)  NULL     DEFAULT NULL,
    `type`         varchar(64)  NULL     DEFAULT NULL,
    `c_schema`     text         NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_configinfo_datagrouptenant` (`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 45
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'config_info'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info`
VALUES (4, 'jaguar-auth-server-dev.yml', 'DEFAULT_GROUP',
        'spring:\n  session:\n    store-type: redis\n    timeout: 600\n    redis:\n      namespace: ${spring.application.name}:spring:session\n\naj:\n  captcha:\n    jigsaw: classpath:captcha\n    # pic-click: classpath:images/pic-click\n    cache-type: redis\n    type: default\n    water-mark: jaguar\n    slip-offset: 5\n    aes-status: true\n    interference-options: 2\n    history-data-clear-enable: false\n    # 接口请求次数一分钟限制是否开启 true|false\n    req-frequency-limit-enable: false\n    # 验证失败5次，get接口锁定\n    req-get-lock-limit: 5\n    # 验证失败后，锁定时间间隔,s\n    req-get-lock-seconds: 360\n    # get接口一分钟内请求数限制\n    req-get-minute-limit: 30\n    # check接口一分钟内请求数限制\n    req-check-minute-limit: 60\n    # verify接口一分钟内请求数限制\n    req-verify-minute-limit: 60\n\njaguar:\n  auth:\n    loginLogEnable: true\n  security:\n    client-id: jaguar-auth\n    client-secret: i1Hc2TdvzFS5XG60K1p19uoY',
        '45a47d54385a35d217e63393e58a79fe', '2021-07-01 15:00:32', '2021-11-22 16:59:57', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info`
VALUES (8, 'jaguar-upms-server-dev.yml', 'DEFAULT_GROUP',
        'spring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://jaguar-mysql:3306/jaguar_upms?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=PRC&useSSL=false&rewriteBatchedStatements=true\n    username: root\n    password: root\n    druid:\n      db-type: mysql\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true\n          merge-sql: true\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        login-username: admin\n        login-password: 123456\n        allow: jaguar-monitor\n\nmybatis-plus:\n  type-aliases-package: top.wilsonlv.jaguar.**.entity.*, com.**.entity.*\n  mapper-locations: classpath*:**/mapper/xml/*.xml\n  configuration:\n    call-setters-on-nulls: true\n    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl\n    local-cache-scope: session\n  global-config:\n    db-config:\n      id-type: id_worker\n      update-strategy: NOT_NULL\n      logic-delete-value: 1\n      logic-not-delete-value: 0\n      logic-delete-field: deleted_\n\njaguar:\n  security:\n    client-id: jaguar-upms\n    client-secret: qb68F1s9YHl9mc1nWJPZ44Uu',
        '227f1da8d6952f1a1a19144c8533380d', '2021-07-01 15:17:57', '2021-11-22 10:49:43', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info`
VALUES (9, 'application-dev.yml', 'DEFAULT_GROUP',
        'server:\n  servlet:\n    encoding:\n      charset: UTF-8\n      force: true\n      enabled: true\n\nspring:\n  jackson:\n    date-format: yyyy-MM-dd HH:mm:ss\n    time-zone: GMT+8\n  cloud:\n    sentinel:\n      transport:\n        dashboard: jaguar-sentinel:18060\n      eager: true\n      log:\n        dir: logs/sentinel\n  redis:\n    database: 6\n    host: jaguar-redis\n    port: 6379\n    password:\n    timeout: 2000\n    lettuce:\n      pool:\n        min-idle: 0\n        max-idle: 8\n        max-active: 8\n        max-wait: 1000\n        time-between-eviction-runs: 100000\n  klock:\n    address: jaguar-redis:6379\n    database: 6\n  activemq:\n    broker-url: tcp://jaguar-mq:61616\n    user: admin\n    password: admin\n    packages:\n      trust-all: true\n    pool:\n      enabled: true\n      max-connections: 20\n  boot:\n    admin:\n      client:\n        url: http://jaguar-monitor:18888\n        username: monitor\n        password: Aa123456\n        instance:\n          prefer-ip: true\n\nmanagement:\n  health:\n    jms:\n      enabled: false\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    logfile:\n      external-file: logs/${spring.application.name}.log\n\njaguar:\n  handler-log:\n    enable: true\n\nlogging:\n  level:\n    top.wilsonlv.jaguar: debug',
        '66bd0099a24288155226992fb7002a8e', '2021-07-01 15:24:10', '2021-11-24 09:22:06', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info`
VALUES (12, 'jaguar-websocket-server-dev.yml', 'DEFAULT_GROUP',
        'jaguar:\n  security:\n    client-id: jaguar-websocket\n    client-secret: F34ag14gI5UYLJ8U0lhgHo3m\n',
        '88c6b6c2c3ea95495e504060f5ae6c17', '2021-07-01 15:28:47', '2021-11-16 14:19:32', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info`
VALUES (14, 'jaguar-monitor-dev.yml', 'DEFAULT_GROUP',
        'spring:\n  security:\n    user:\n      name: monitor\n      password: Aa123456\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    logfile:\n      external-file: logs/${spring.application.name}.log\n\njaguar:\n  web:\n    jsonResultResponseEnable: false',
        '76610c1a8c8619c0bfb90a54fbcc39db', '2021-07-01 15:35:55', '2021-11-18 17:07:39', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info`
VALUES (23, 'jaguar-job-executor-dev.yml', 'DEFAULT_GROUP',
        'jaguar:\n  security:\n    client-id: job-executor\n    client-secret: 123456\n  job:\n    executor:\n      admin-addresses: http://jaguar-job-admin:18080\n      app-name: ${spring.application.name}\n      log-path: logs/${spring.application.name}/jobhandler',
        '65ca1bef132f49a67696c6c55d406058', '2021-07-01 22:15:08', '2021-11-16 14:20:59', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info`
VALUES (25, 'jaguar-job-admin-dev.yml', 'DEFAULT_GROUP',
        'management:\n  health:\n    mail:\n      enabled: false\n\nmybatis:\n  mapper-locations: classpath:/mybatis-mapper/*Mapper.xml\n  \nspring:\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      auto-commit: true\n      connection-test-query: SELECT 1\n      connection-timeout: 10000\n      idle-timeout: 30000\n      max-lifetime: 900000\n      maximum-pool-size: 30\n      minimum-idle: 10\n      pool-name: HikariCP\n      validation-timeout: 1000\n    password: root\n    type: com.zaxxer.hikari.HikariDataSource\n    url: jdbc:mysql://jaguar-mysql:3306/jaguar_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai\n    username: root\n  freemarker:\n    charset: UTF-8\n    request-context-attribute: request\n    settings:\n      number_format: 0.##########\n    suffix: .ftl\n    templateLoaderPath: classpath:/templates/\n  mail:\n    from: xxx@qq.com\n    host: smtp.qq.com\n    password: xxx\n    port: 25\n    properties:\n      mail:\n        smtp:\n          auth: true\n          socketFactory:\n            class: javax.net.ssl.SSLSocketFactory\n          starttls:\n            enable: true\n            required: true\n    username: xxx@qq.com\n  mvc:\n    servlet:\n      load-on-startup: 0\n    static-path-pattern: /static/**\n  resources:\n    static-locations: classpath:/static/\n\nxxl:\n  job:\n    accessToken: \n    i18n: zh_CN\n    logretentiondays: 30\n    triggerpool:\n      fast:\n        max: 200\n      slow:\n        max: 100',
        'effdc5a9d06c1073b0a2718e98f656cf', '2021-07-01 22:23:44', '2021-11-16 14:23:48', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info`
VALUES (37, 'jaguar-handler-log-server-dev.yml', 'DEFAULT_GROUP',
        'spring:\n  elasticsearch:\n    rest:\n      uris: http://jaguar-es:9200\n      username: elastic\n      password: Aa123456\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    url: jdbc:es://http://jaguar-es:9200\n    driver-class-name: org.elasticsearch.xpack.sql.jdbc.EsDriver\n    username: elastic\n    password: Aa123456\n    druid:\n      db-type: mysql\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true\n          merge-sql: true\n      web-stat-filter:\n        enabled: true\n\nmybatis-plus:\n  type-aliases-package: top.wilsonlv.jaguar.**.entity*, com.**.entity*\n  mapper-locations: classpath*:**/mapper/xml/*.xml\n  configuration:\n    call-setters-on-nulls: true\n    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl\n    local-cache-scope: session\n  global-config:\n    db-config:\n      id-type: id_worker\n      update-strategy: NOT_NULL\n      logic-delete-value: 1\n      logic-not-delete-value: 0\n      logic-delete-field: deleted_\n\njaguar:\n  security:\n    client-id: jaguar-handler-log\n    client-secret: lJ1MJ80Kmm1oU6kx0W0RCb2b',
        'b61893f21c759fc30ceedb48372daa7c', '2021-08-06 14:04:27', '2021-11-23 08:49:03', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '', '', '', 'yaml', '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) NOT NULL COMMENT 'data_id',
    `group_id`     varchar(255) NOT NULL COMMENT 'group_id',
    `datum_id`     varchar(255) NOT NULL COMMENT 'datum_id',
    `content`      longtext     NOT NULL COMMENT '内容',
    `gmt_modified` datetime(0)  NOT NULL COMMENT '修改时间',
    `app_name`     varchar(128) NULL DEFAULT NULL,
    `tenant_id`    varchar(128) NULL DEFAULT '' COMMENT '租户字段',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum` (`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = '增加租户字段'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`
(
    `id`           bigint(20)    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255)  NOT NULL COMMENT 'data_id',
    `group_id`     varchar(128)  NOT NULL COMMENT 'group_id',
    `app_name`     varchar(128)  NULL     DEFAULT NULL COMMENT 'app_name',
    `content`      longtext      NOT NULL COMMENT 'content',
    `beta_ips`     varchar(1024) NULL     DEFAULT NULL COMMENT 'betaIps',
    `md5`          varchar(32)   NULL     DEFAULT NULL COMMENT 'md5',
    `gmt_create`   datetime(0)   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `gmt_modified` datetime(0)   NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `src_user`     text          NULL COMMENT 'source user',
    `src_ip`       varchar(50)   NULL     DEFAULT NULL COMMENT 'source ip',
    `tenant_id`    varchar(128)  NULL     DEFAULT '' COMMENT '租户字段',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_configinfobeta_datagrouptenant` (`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'config_info_beta'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) NOT NULL COMMENT 'data_id',
    `group_id`     varchar(128) NOT NULL COMMENT 'group_id',
    `tenant_id`    varchar(128) NULL     DEFAULT '' COMMENT 'tenant_id',
    `tag_id`       varchar(128) NOT NULL COMMENT 'tag_id',
    `app_name`     varchar(128) NULL     DEFAULT NULL COMMENT 'app_name',
    `content`      longtext     NOT NULL COMMENT 'content',
    `md5`          varchar(32)  NULL     DEFAULT NULL COMMENT 'md5',
    `gmt_create`   datetime(0)  NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `gmt_modified` datetime(0)  NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    `src_user`     text         NULL COMMENT 'source user',
    `src_ip`       varchar(50)  NULL     DEFAULT NULL COMMENT 'source ip',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_configinfotag_datagrouptenanttag` (`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'config_info_tag'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`
(
    `id`        bigint(20)   NOT NULL COMMENT 'id',
    `tag_name`  varchar(128) NOT NULL COMMENT 'tag_name',
    `tag_type`  varchar(64)  NULL DEFAULT NULL COMMENT 'tag_type',
    `data_id`   varchar(255) NOT NULL COMMENT 'data_id',
    `group_id`  varchar(128) NOT NULL COMMENT 'group_id',
    `tenant_id` varchar(128) NULL DEFAULT '' COMMENT 'tenant_id',
    `nid`       bigint(20)   NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`nid`) USING BTREE,
    UNIQUE INDEX `uk_configtagrelation_configidtag` (`id`, `tag_name`, `tag_type`) USING BTREE,
    INDEX `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'config_tag_relation'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`
(
    `id`                bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `group_id`          varchar(128)        NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
    `quota`             int(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
    `usage`             int(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '使用量',
    `max_size`          int(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
    `max_aggr_count`    int(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
    `max_aggr_size`     int(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    `max_history_count` int(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
    `gmt_create`        datetime(0)         NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `gmt_modified`      datetime(0)         NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_group_id` (`group_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`
(
    `id`           bigint(64) UNSIGNED NOT NULL,
    `nid`          bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `data_id`      varchar(255)        NOT NULL,
    `group_id`     varchar(128)        NOT NULL,
    `app_name`     varchar(128)        NULL     DEFAULT NULL COMMENT 'app_name',
    `content`      longtext            NOT NULL,
    `md5`          varchar(32)         NULL     DEFAULT NULL,
    `gmt_create`   datetime(0)         NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `gmt_modified` datetime(0)         NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `src_user`     text                NULL,
    `src_ip`       varchar(50)         NULL     DEFAULT NULL,
    `op_type`      char(10)            NULL     DEFAULT NULL,
    `tenant_id`    varchar(128)        NULL     DEFAULT '' COMMENT '租户字段',
    PRIMARY KEY (`nid`) USING BTREE,
    INDEX `idx_gmt_create` (`gmt_create`) USING BTREE,
    INDEX `idx_gmt_modified` (`gmt_modified`) USING BTREE,
    INDEX `idx_did` (`data_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 31
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = '多租户改造'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info`
VALUES (9, 1, 'application-dev.yml', 'DEFAULT_GROUP', '',
        'server:\n  servlet:\n    encoding:\n      charset: UTF-8\n      force: true\n      enabled: true\n\nspring:\n  cloud:\n    sentinel:\n      transport:\n        dashboard: jaguar-sentinel:18060\n      eager: true\n      log:\n        dir: logs/sentinel\n  redis:\n    database: 6\n    host: jaguar-redis\n    port: 6379\n    password:\n    timeout: 2000\n    lettuce:\n      pool:\n        min-idle: 0\n        max-idle: 8\n        max-active: 8\n        max-wait: 1000\n        time-between-eviction-runs: 100000\n  klock:\n    address: jaguar-redis:6379\n    database: 6\n  activemq:\n    broker-url: tcp://jaguar-mq:61616\n    user: admin\n    password: admin\n    packages:\n      trust-all: true\n    pool:\n      enabled: true\n      max-connections: 20\n  boot:\n    admin:\n      client:\n        url: http://jaguar-monitor:18888\n        username: monitor\n        password: 123456\n        instance:\n          prefer-ip: true\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    logfile:\n      external-file: logs/${spring.application.name}.log',
        '47597bc8a322a131e462c316929b2e03', '2021-11-01 11:19:40', '2021-11-01 11:19:40', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (8, 2, 'jaguar-upms-server-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  security:\n    oauth2:\n      resourceserver:\n        opaquetoken:\n          client-id: jaguar-upms\n          client-secret: qb68F1s9YHl9mc1nWJPZ44Uu\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://jaguar-mysql:3306/jaguar_upms?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=PRC&useSSL=false\n    username: root\n    password: root\n    druid:\n      db-type: mysql\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true\n          merge-sql: true\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        login-username: admin\n        login-password: 123456\n        allow: jaguar-monitor\n\nmybatis-plus:\n  type-aliases-package: org.jaguar.**.model*, com.**.model*\n  mapper-locations: classpath*:**/mapper/xml/*.xml\n  configuration:\n    call-setters-on-nulls: true\n    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl\n    local-cache-scope: session\n  global-config:\n    db-config:\n      id-type: id_worker\n      update-strategy: NOT_NULL\n      logic-delete-value: 1\n      logic-not-delete-value: 0\n      logic-delete-field: deleted_',
        '5160eb82f14a7d0cfb140f3393e22ba1', '2021-11-15 15:28:21', '2021-11-15 15:28:21', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (4, 3, 'jaguar-auth-server-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  security:\n    oauth2:\n      resourceserver:\n        opaquetoken:\n          client-id: jaguar-auth\n          client-secret: PHn8KG0T06i45jetPS9ejcT7\n  session:\n    store-type: redis\n    timeout: 600\n    redis:\n      namespace: ${spring.application.name}:spring:session\n\naj:\n  captcha:\n    jigsaw: classpath:captcha\n    # pic-click: classpath:images/pic-click\n    cache-type: redis\n    type: default\n    water-mark: jaguar\n    slip-offset: 5\n    aes-status: true\n    interference-options: 2\n    history-data-clear-enable: false\n    # 接口请求次数一分钟限制是否开启 true|false\n    req-frequency-limit-enable: false\n    # 验证失败5次，get接口锁定\n    req-get-lock-limit: 5\n    # 验证失败后，锁定时间间隔,s\n    req-get-lock-seconds: 360\n    # get接口一分钟内请求数限制\n    req-get-minute-limit: 30\n    # check接口一分钟内请求数限制\n    req-check-minute-limit: 60\n    # verify接口一分钟内请求数限制\n    req-verify-minute-limit: 60',
        '8391441e487f448853bccc086c9902e7', '2021-11-16 09:03:39', '2021-11-16 09:03:40', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (9, 4, 'application-dev.yml', 'DEFAULT_GROUP', '',
        'server:\n  servlet:\n    encoding:\n      charset: UTF-8\n      force: true\n      enabled: true\n\nspring:\n  cloud:\n    sentinel:\n      transport:\n        dashboard: jaguar-sentinel:18060\n      eager: true\n      log:\n        dir: logs/sentinel\n  redis:\n    database: 6\n    host: jaguar-redis\n    port: 6379\n    password:\n    timeout: 2000\n    lettuce:\n      pool:\n        min-idle: 0\n        max-idle: 8\n        max-active: 8\n        max-wait: 1000\n        time-between-eviction-runs: 100000\n  klock:\n    address: jaguar-redis:6379\n    database: 6\n  activemq:\n    broker-url: tcp://jaguar-mq:61616\n    user: admin\n    password: admin\n    packages:\n      trust-all: true\n    pool:\n      enabled: true\n      max-connections: 20\n  boot:\n    admin:\n      client:\n        url: http://jaguar-monitor:18888\n        username: monitor\n        password: 123456\n        instance:\n          prefer-ip: true\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    logfile:\n      external-file: logs/${spring.application.name}.log\n\nlogging:\n  level:\n    top.wilsonlv.jaguar: debug',
        '0ed773da4ebad642662bc3f4341ce825', '2021-11-16 09:06:10', '2021-11-16 09:06:11', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (4, 5, 'jaguar-auth-server-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  security:\n    oauth2:\n      resourceserver:\n        opaquetoken:\n          client-id: jaguar-auth\n          client-secret: PHn8KG0T06i45jetPS9ejcT7\n  session:\n    store-type: redis\n    timeout: 600\n    redis:\n      namespace: ${spring.application.name}:spring:session\n\naj:\n  captcha:\n    jigsaw: classpath:captcha\n    # pic-click: classpath:images/pic-click\n    cache-type: redis\n    type: default\n    water-mark: jaguar\n    slip-offset: 5\n    aes-status: true\n    interference-options: 2\n    history-data-clear-enable: false\n    # 接口请求次数一分钟限制是否开启 true|false\n    req-frequency-limit-enable: false\n    # 验证失败5次，get接口锁定\n    req-get-lock-limit: 5\n    # 验证失败后，锁定时间间隔,s\n    req-get-lock-seconds: 360\n    # get接口一分钟内请求数限制\n    req-get-minute-limit: 30\n    # check接口一分钟内请求数限制\n    req-check-minute-limit: 60\n    # verify接口一分钟内请求数限制\n    req-verify-minute-limit: 60\n\njaguar:\n  auth:\n    loginLogEnable: false',
        'b25945d16a027e674e88ef10a8a573a5', '2021-11-16 14:17:11', '2021-11-16 14:17:12', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (8, 6, 'jaguar-upms-server-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  security:\n    oauth2:\n      resourceserver:\n        opaquetoken:\n          client-id: jaguar-upms\n          client-secret: qb68F1s9YHl9mc1nWJPZ44Uu\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://jaguar-mysql:3306/jaguar_upms?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=PRC&useSSL=false&rewriteBatchedStatements=true\n    username: root\n    password: root\n    druid:\n      db-type: mysql\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true\n          merge-sql: true\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        login-username: admin\n        login-password: 123456\n        allow: jaguar-monitor\n\nmybatis-plus:\n  type-aliases-package: org.jaguar.**.model*, com.**.model*\n  mapper-locations: classpath*:**/mapper/xml/*.xml\n  configuration:\n    call-setters-on-nulls: true\n    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl\n    local-cache-scope: session\n  global-config:\n    db-config:\n      id-type: id_worker\n      update-strategy: NOT_NULL\n      logic-delete-value: 1\n      logic-not-delete-value: 0\n      logic-delete-field: deleted_',
        '130e3492dfd0400fe26962cec365bdf5', '2021-11-16 14:17:59', '2021-11-16 14:18:00', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (12, 7, 'jaguar-websocket-server-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  security:\n    oauth2:\n      resourceserver:\n        opaquetoken:\n          client-id: jaguar-websocket\n          client-secret: F34ag14gI5UYLJ8U0lhgHo3m\n',
        'ba6d82d1fd9030e3b71cb3289870e2a9', '2021-11-16 14:19:32', '2021-11-16 14:19:32', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (14, 8, 'jaguar-monitor-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  security:\n    user:\n      name: monitor\n      password: 123456\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    logfile:\n      external-file: logs/${spring.application.name}.log',
        '50c4b00312228972f8b7b8555c5b295b', '2021-11-16 14:19:58', '2021-11-16 14:19:59', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (23, 9, 'jaguar-job-executor-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  security:\n    oauth2:\n      resourceserver:\n        opaquetoken:\n          client-id: job-executor\n          client-secret: 123456\n\njaguar:\n  job:\n    executor:\n      admin-addresses: http://jaguar-job-admin:18080\n      app-name: ${spring.application.name}\n      log-path: logs/${spring.application.name}/jobhandler\n',
        'a7667696081bc971551c8d3f9a5d529f', '2021-11-16 14:20:35', '2021-11-16 14:20:35', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (23, 10, 'jaguar-job-executor-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  security:\n    client-id: job-executor\n    client-secret: 123456\n\njaguar:\n  job:\n    executor:\n      admin-addresses: http://jaguar-job-admin:18080\n      app-name: ${spring.application.name}\n      log-path: logs/${spring.application.name}/jobhandler\n',
        '76ce8231c167c1df1c42e71545d5e33e', '2021-11-16 14:20:59', '2021-11-16 14:20:59', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (25, 11, 'jaguar-job-admin-dev.yml', 'DEFAULT_GROUP', '',
        'management:\n  health:\n    mail:\n      enabled: false\n\nmybatis:\n  mapper-locations: classpath:/mybatis-mapper/*Mapper.xml\n  \nspring:\n  security:\n    oauth2:\n      resourceserver:\n        opaquetoken:\n          client-id: job-admin\n          client-secret: 123456\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      auto-commit: true\n      connection-test-query: SELECT 1\n      connection-timeout: 10000\n      idle-timeout: 30000\n      max-lifetime: 900000\n      maximum-pool-size: 30\n      minimum-idle: 10\n      pool-name: HikariCP\n      validation-timeout: 1000\n    password: root\n    type: com.zaxxer.hikari.HikariDataSource\n    url: jdbc:mysql://jaguar-mysql:3306/jaguar_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai\n    username: root\n  freemarker:\n    charset: UTF-8\n    request-context-attribute: request\n    settings:\n      number_format: 0.##########\n    suffix: .ftl\n    templateLoaderPath: classpath:/templates/\n  mail:\n    from: xxx@qq.com\n    host: smtp.qq.com\n    password: xxx\n    port: 25\n    properties:\n      mail:\n        smtp:\n          auth: true\n          socketFactory:\n            class: javax.net.ssl.SSLSocketFactory\n          starttls:\n            enable: true\n            required: true\n    username: xxx@qq.com\n  mvc:\n    servlet:\n      load-on-startup: 0\n    static-path-pattern: /static/**\n  resources:\n    static-locations: classpath:/static/\n\nxxl:\n  job:\n    accessToken: \n    i18n: zh_CN\n    logretentiondays: 30\n    triggerpool:\n      fast:\n        max: 200\n      slow:\n        max: 100',
        '9eee3c30e4dd64212a33951155cdbd35', '2021-11-16 14:23:48', '2021-11-16 14:23:48', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (37, 12, 'jaguar-handler-log-server-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  security:\n    oauth2:\n      resourceserver:\n        opaquetoken:\n          client-id: jaguar-handler-log\n          client-secret: lJ1MJ80Kmm1oU6kx0W0RCb2b\n  elasticsearch:\n    rest:\n      uris: http://jaguar-es:9200\n      username: elastic\n      password: 123456\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    url: jdbc:es://http://jaguar-es:9200\n    driver-class-name: org.elasticsearch.xpack.sql.jdbc.EsDriver\n    username: elastic\n    password: 123456\n    druid:\n      db-type: mysql\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true\n          merge-sql: true\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        login-username: admin\n        login-password: 123456\n        allow: jaguar-monitor\n\nmybatis-plus:\n  type-aliases-package: org.jaguar.**.model*, com.**.model*\n  mapper-locations: classpath*:**/mapper/xml/*.xml\n  configuration:\n    call-setters-on-nulls: true\n    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl\n    local-cache-scope: session\n  global-config:\n    db-config:\n      id-type: id_worker\n      update-strategy: NOT_NULL\n      logic-delete-value: 1\n      logic-not-delete-value: 0\n      logic-delete-field: deleted_',
        'c81fbb15879d37158701ca1a2930ad52', '2021-11-16 14:24:40', '2021-11-16 14:24:41', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (9, 13, 'application-dev.yml', 'DEFAULT_GROUP', '',
        'server:\n  servlet:\n    encoding:\n      charset: UTF-8\n      force: true\n      enabled: true\n\nspring:\n  cloud:\n    sentinel:\n      transport:\n        dashboard: jaguar-sentinel:18060\n      eager: true\n      log:\n        dir: logs/sentinel\n  redis:\n    database: 6\n    host: jaguar-redis\n    port: 6379\n    password:\n    timeout: 2000\n    lettuce:\n      pool:\n        min-idle: 0\n        max-idle: 8\n        max-active: 8\n        max-wait: 1000\n        time-between-eviction-runs: 100000\n  klock:\n    address: jaguar-redis:6379\n    database: 6\n  activemq:\n    broker-url: tcp://jaguar-mq:61616\n    user: admin\n    password: admin\n    packages:\n      trust-all: true\n    pool:\n      enabled: true\n      max-connections: 20\n  boot:\n    admin:\n      client:\n        url: http://jaguar-monitor:18888\n        username: monitor\n        password: 123456\n        instance:\n          prefer-ip: true\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    logfile:\n      external-file: logs/${spring.application.name}.log\n\njaguar:\n  handler-log:\n    enable: false\n\nlogging:\n  level:\n    top.wilsonlv.jaguar: debug',
        '9bb8cdcdf68f739cd7446b72a601aa0b', '2021-11-17 13:52:44', '2021-11-17 13:52:44', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (4, 14, 'jaguar-auth-server-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  session:\n    store-type: redis\n    timeout: 600\n    redis:\n      namespace: ${spring.application.name}:spring:session\n\naj:\n  captcha:\n    jigsaw: classpath:captcha\n    # pic-click: classpath:images/pic-click\n    cache-type: redis\n    type: default\n    water-mark: jaguar\n    slip-offset: 5\n    aes-status: true\n    interference-options: 2\n    history-data-clear-enable: false\n    # 接口请求次数一分钟限制是否开启 true|false\n    req-frequency-limit-enable: false\n    # 验证失败5次，get接口锁定\n    req-get-lock-limit: 5\n    # 验证失败后，锁定时间间隔,s\n    req-get-lock-seconds: 360\n    # get接口一分钟内请求数限制\n    req-get-minute-limit: 30\n    # check接口一分钟内请求数限制\n    req-check-minute-limit: 60\n    # verify接口一分钟内请求数限制\n    req-verify-minute-limit: 60\n\njaguar:\n  auth:\n    loginLogEnable: false\n  security:\n    client-id: jaguar-auth\n    client-secret: PHn8KG0T06i45jetPS9ejcT7',
        '23a73fe9365c77bb026d889380d83905', '2021-11-17 14:55:04', '2021-11-17 14:55:05', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (4, 15, 'jaguar-auth-server-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  session:\n    store-type: redis\n    timeout: 600\n    redis:\n      namespace: ${spring.application.name}:spring:session\n\naj:\n  captcha:\n    jigsaw: classpath:captcha\n    # pic-click: classpath:images/pic-click\n    cache-type: redis\n    type: default\n    water-mark: jaguar\n    slip-offset: 5\n    aes-status: true\n    interference-options: 2\n    history-data-clear-enable: false\n    # 接口请求次数一分钟限制是否开启 true|false\n    req-frequency-limit-enable: false\n    # 验证失败5次，get接口锁定\n    req-get-lock-limit: 5\n    # 验证失败后，锁定时间间隔,s\n    req-get-lock-seconds: 360\n    # get接口一分钟内请求数限制\n    req-get-minute-limit: 30\n    # check接口一分钟内请求数限制\n    req-check-minute-limit: 60\n    # verify接口一分钟内请求数限制\n    req-verify-minute-limit: 60\n\njaguar:\n  auth:\n    loginLogEnable: false\n  security:\n    client-id: jaguar-auth\n    client-secret: i1Hc2TdvzFS5XG60K1p19uoY',
        'f67b7417e536358b93c30f99e97e290d', '2021-11-17 15:35:11', '2021-11-17 15:35:11', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (4, 16, 'jaguar-auth-server-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  session:\n    store-type: redis\n    timeout: 600\n    redis:\n      namespace: ${spring.application.name}:spring:session\n\naj:\n  captcha:\n    jigsaw: classpath:captcha\n    # pic-click: classpath:images/pic-click\n    cache-type: redis\n    type: default\n    water-mark: jaguar\n    slip-offset: 5\n    aes-status: true\n    interference-options: 2\n    history-data-clear-enable: false\n    # 接口请求次数一分钟限制是否开启 true|false\n    req-frequency-limit-enable: false\n    # 验证失败5次，get接口锁定\n    req-get-lock-limit: 5\n    # 验证失败后，锁定时间间隔,s\n    req-get-lock-seconds: 360\n    # get接口一分钟内请求数限制\n    req-get-minute-limit: 30\n    # check接口一分钟内请求数限制\n    req-check-minute-limit: 60\n    # verify接口一分钟内请求数限制\n    req-verify-minute-limit: 60\n\njaguar:\n  auth:\n    loginLogEnable: false\n  security:\n    client-id: jaguar-auth\n    client-secret: i1Hc2TdvzFS5XG60K1p19uo',
        '229ec15eec237d1046fcb14f544b1df2', '2021-11-17 15:47:54', '2021-11-17 15:47:54', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (9, 17, 'application-dev.yml', 'DEFAULT_GROUP', '',
        'server:\n  servlet:\n    encoding:\n      charset: UTF-8\n      force: true\n      enabled: true\n\nspring:\n  cloud:\n    sentinel:\n      transport:\n        dashboard: jaguar-sentinel:18060\n      eager: true\n      log:\n        dir: logs/sentinel\n  redis:\n    database: 6\n    host: jaguar-redis\n    port: 6379\n    password:\n    timeout: 2000\n    lettuce:\n      pool:\n        min-idle: 0\n        max-idle: 8\n        max-active: 8\n        max-wait: 1000\n        time-between-eviction-runs: 100000\n  klock:\n    address: jaguar-redis:6379\n    database: 6\n  activemq:\n    broker-url: tcp://jaguar-mq:61616\n    user: admin\n    password: admin\n    packages:\n      trust-all: true\n    pool:\n      enabled: true\n      max-connections: 20\n  boot:\n    admin:\n      client:\n        url: http://jaguar-monitor:18888\n        username: monitor\n        password: 123456\n        instance:\n          prefer-ip: true\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    logfile:\n      external-file: logs/${spring.application.name}.log\n\njaguar:\n  handler-log:\n    enable: false\n\nlogging:\n  level:\n    top.wilsonlv.jaguar: debug\n    org.springframework.security: debug',
        '074f2b947ac4461a3580b989b7dd7a10', '2021-11-18 16:18:13', '2021-11-18 16:18:13', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (9, 18, 'application-dev.yml', 'DEFAULT_GROUP', '',
        'server:\n  servlet:\n    encoding:\n      charset: UTF-8\n      force: true\n      enabled: true\n\nspring:\n  cloud:\n    sentinel:\n      transport:\n        dashboard: jaguar-sentinel:18060\n      eager: true\n      log:\n        dir: logs/sentinel\n  redis:\n    database: 6\n    host: jaguar-redis\n    port: 6379\n    password:\n    timeout: 2000\n    lettuce:\n      pool:\n        min-idle: 0\n        max-idle: 8\n        max-active: 8\n        max-wait: 1000\n        time-between-eviction-runs: 100000\n  klock:\n    address: jaguar-redis:6379\n    database: 6\n  activemq:\n    broker-url: tcp://jaguar-mq:61616\n    user: admin\n    password: admin\n    packages:\n      trust-all: true\n    pool:\n      enabled: true\n      max-connections: 20\n  boot:\n    admin:\n      client:\n        url: http://jaguar-monitor:18888\n        username: monitor\n        password: 123456\n        instance:\n          prefer-ip: true\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    logfile:\n      external-file: logs/${spring.application.name}.log\n\njaguar:\n  handler-log:\n    enable: false\n  activemq: \n    enable: false\n\nlogging:\n  level:\n    top.wilsonlv.jaguar: debug\n    org.springframework.security: debug',
        'd081528b744f1ab195eadb9b9da43384', '2021-11-18 16:30:07', '2021-11-18 16:30:08', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (14, 19, 'jaguar-monitor-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  security:\n    user:\n      name: monitor\n      password: Aa123456\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    logfile:\n      external-file: logs/${spring.application.name}.log',
        '67898a06852d6a2f25281907aaeccf2f', '2021-11-18 16:51:45', '2021-11-18 16:51:45', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (14, 20, 'jaguar-monitor-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  security:\n    user:\n      name: jaguar-monitor\n      password: 082RHExjnhS9gQoFQKn1236l\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    logfile:\n      external-file: logs/${spring.application.name}.log',
        'c0092c9dd3b5966085f3b97ee650f9c6', '2021-11-18 16:58:55', '2021-11-18 16:58:56', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (9, 21, 'application-dev.yml', 'DEFAULT_GROUP', '',
        'server:\n  servlet:\n    encoding:\n      charset: UTF-8\n      force: true\n      enabled: true\n\nspring:\n  cloud:\n    sentinel:\n      transport:\n        dashboard: jaguar-sentinel:18060\n      eager: true\n      log:\n        dir: logs/sentinel\n  redis:\n    database: 6\n    host: jaguar-redis\n    port: 6379\n    password:\n    timeout: 2000\n    lettuce:\n      pool:\n        min-idle: 0\n        max-idle: 8\n        max-active: 8\n        max-wait: 1000\n        time-between-eviction-runs: 100000\n  klock:\n    address: jaguar-redis:6379\n    database: 6\n  activemq:\n    broker-url: tcp://jaguar-mq:61616\n    user: admin\n    password: admin\n    packages:\n      trust-all: true\n    pool:\n      enabled: true\n      max-connections: 20\n  boot:\n    admin:\n      client:\n        url: http://jaguar-monitor:18888\n        username: monitor\n        password: 123456\n        instance:\n          prefer-ip: true\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    logfile:\n      external-file: logs/${spring.application.name}.log\n\njaguar:\n  handler-log:\n    enable: false\n\nlogging:\n  level:\n    top.wilsonlv.jaguar: debug\n    org.springframework.security: debug',
        '074f2b947ac4461a3580b989b7dd7a10', '2021-11-18 16:59:11', '2021-11-18 16:59:11', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (14, 22, 'jaguar-monitor-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  security:\n    user:\n      name: monitor\n      password: Aa123456\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    logfile:\n      external-file: logs/${spring.application.name}.log',
        '67898a06852d6a2f25281907aaeccf2f', '2021-11-18 17:07:38', '2021-11-18 17:07:39', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (9, 23, 'application-dev.yml', 'DEFAULT_GROUP', '',
        'server:\n  servlet:\n    encoding:\n      charset: UTF-8\n      force: true\n      enabled: true\n\nspring:\n  cloud:\n    sentinel:\n      transport:\n        dashboard: jaguar-sentinel:18060\n      eager: true\n      log:\n        dir: logs/sentinel\n  redis:\n    database: 6\n    host: jaguar-redis\n    port: 6379\n    password:\n    timeout: 2000\n    lettuce:\n      pool:\n        min-idle: 0\n        max-idle: 8\n        max-active: 8\n        max-wait: 1000\n        time-between-eviction-runs: 100000\n  klock:\n    address: jaguar-redis:6379\n    database: 6\n  activemq:\n    broker-url: tcp://jaguar-mq:61616\n    user: admin\n    password: admin\n    packages:\n      trust-all: true\n    pool:\n      enabled: true\n      max-connections: 20\n  boot:\n    admin:\n      client:\n        url: http://jaguar-monitor:18888\n        username: monitor\n        password: Aa123456\n        instance:\n          prefer-ip: true\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    logfile:\n      external-file: logs/${spring.application.name}.log\n\njaguar:\n  handler-log:\n    enable: false\n\nlogging:\n  level:\n    top.wilsonlv.jaguar: debug\n    org.springframework.security: debug',
        '6505a1fe3d14f96c48f533f713be0251', '2021-11-18 17:24:29', '2021-11-18 17:24:30', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (8, 24, 'jaguar-upms-server-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://jaguar-mysql:3306/jaguar_upms?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=PRC&useSSL=false&rewriteBatchedStatements=true\n    username: root\n    password: root\n    druid:\n      db-type: mysql\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true\n          merge-sql: true\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        login-username: admin\n        login-password: 123456\n        allow: jaguar-monitor\n\nmybatis-plus:\n  type-aliases-package: org.jaguar.**.model*, com.**.model*\n  mapper-locations: classpath*:**/mapper/xml/*.xml\n  configuration:\n    call-setters-on-nulls: true\n    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl\n    local-cache-scope: session\n  global-config:\n    db-config:\n      id-type: id_worker\n      update-strategy: NOT_NULL\n      logic-delete-value: 1\n      logic-not-delete-value: 0\n      logic-delete-field: deleted_\n\njaguar:\n  security:\n    client-id: jaguar-upms\n    client-secret: qb68F1s9YHl9mc1nWJPZ44Uu',
        '84bf46eb38bcefdc7adbb5ab2b1bbf8d', '2021-11-22 10:49:43', '2021-11-22 10:49:43', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (9, 25, 'application-dev.yml', 'DEFAULT_GROUP', '',
        'server:\n  servlet:\n    encoding:\n      charset: UTF-8\n      force: true\n      enabled: true\n\nspring:\n  cloud:\n    sentinel:\n      transport:\n        dashboard: jaguar-sentinel:18060\n      eager: true\n      log:\n        dir: logs/sentinel\n  redis:\n    database: 6\n    host: jaguar-redis\n    port: 6379\n    password:\n    timeout: 2000\n    lettuce:\n      pool:\n        min-idle: 0\n        max-idle: 8\n        max-active: 8\n        max-wait: 1000\n        time-between-eviction-runs: 100000\n  klock:\n    address: jaguar-redis:6379\n    database: 6\n  activemq:\n    broker-url: tcp://jaguar-mq:61616\n    user: admin\n    password: admin\n    packages:\n      trust-all: true\n    pool:\n      enabled: true\n      max-connections: 20\n  boot:\n    admin:\n      client:\n        url: http://jaguar-monitor:18888\n        username: monitor\n        password: Aa123456\n        instance:\n          prefer-ip: true\n\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    logfile:\n      external-file: logs/${spring.application.name}.log\n\njaguar:\n  handler-log:\n    enable: false\n\nlogging:\n  level:\n    top.wilsonlv.jaguar: debug',
        '07875cf11a2268d84073a9b4ce500728', '2021-11-22 11:44:47', '2021-11-22 11:44:48', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (4, 26, 'jaguar-auth-server-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  session:\n    store-type: redis\n    timeout: 600\n    redis:\n      namespace: ${spring.application.name}:spring:session\n\naj:\n  captcha:\n    jigsaw: classpath:captcha\n    # pic-click: classpath:images/pic-click\n    cache-type: redis\n    type: default\n    water-mark: jaguar\n    slip-offset: 5\n    aes-status: true\n    interference-options: 2\n    history-data-clear-enable: false\n    # 接口请求次数一分钟限制是否开启 true|false\n    req-frequency-limit-enable: false\n    # 验证失败5次，get接口锁定\n    req-get-lock-limit: 5\n    # 验证失败后，锁定时间间隔,s\n    req-get-lock-seconds: 360\n    # get接口一分钟内请求数限制\n    req-get-minute-limit: 30\n    # check接口一分钟内请求数限制\n    req-check-minute-limit: 60\n    # verify接口一分钟内请求数限制\n    req-verify-minute-limit: 60\n\njaguar:\n  auth:\n    loginLogEnable: false\n  security:\n    client-id: jaguar-auth\n    client-secret: i1Hc2TdvzFS5XG60K1p19uoY',
        'f67b7417e536358b93c30f99e97e290d', '2021-11-22 16:59:57', '2021-11-22 16:59:57', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (9, 27, 'application-dev.yml', 'DEFAULT_GROUP', '',
        'server:\n  servlet:\n    encoding:\n      charset: UTF-8\n      force: true\n      enabled: true\n\nspring:\n  cloud:\n    sentinel:\n      transport:\n        dashboard: jaguar-sentinel:18060\n      eager: true\n      log:\n        dir: logs/sentinel\n  redis:\n    database: 6\n    host: jaguar-redis\n    port: 6379\n    password:\n    timeout: 2000\n    lettuce:\n      pool:\n        min-idle: 0\n        max-idle: 8\n        max-active: 8\n        max-wait: 1000\n        time-between-eviction-runs: 100000\n  klock:\n    address: jaguar-redis:6379\n    database: 6\n  activemq:\n    broker-url: tcp://jaguar-mq:61616\n    user: admin\n    password: admin\n    packages:\n      trust-all: true\n    pool:\n      enabled: true\n      max-connections: 20\n  boot:\n    admin:\n      client:\n        url: http://jaguar-monitor:18888\n        username: monitor\n        password: Aa123456\n        instance:\n          prefer-ip: true\n\nmanagement:\n  health:\n    jms:\n      enabled: false\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    logfile:\n      external-file: logs/${spring.application.name}.log\n\njaguar:\n  handler-log:\n    enable: false\n\nlogging:\n  level:\n    top.wilsonlv.jaguar: debug',
        '60ba423dc70922fb36b46bc4f79d7f46', '2021-11-22 17:00:10', '2021-11-22 17:00:11', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (37, 28, 'jaguar-handler-log-server-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  elasticsearch:\n    rest:\n      uris: http://jaguar-es:9200\n      username: elastic\n      password: 123456\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    url: jdbc:es://http://jaguar-es:9200\n    driver-class-name: org.elasticsearch.xpack.sql.jdbc.EsDriver\n    username: elastic\n    password: 123456\n    druid:\n      db-type: mysql\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true\n          merge-sql: true\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        login-username: admin\n        login-password: 123456\n        allow: jaguar-monitor\n\nmybatis-plus:\n  type-aliases-package: org.jaguar.**.model*, com.**.model*\n  mapper-locations: classpath*:**/mapper/xml/*.xml\n  configuration:\n    call-setters-on-nulls: true\n    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl\n    local-cache-scope: session\n  global-config:\n    db-config:\n      id-type: id_worker\n      update-strategy: NOT_NULL\n      logic-delete-value: 1\n      logic-not-delete-value: 0\n      logic-delete-field: deleted_\n\njaguar:\n  security:\n    client-id: jaguar-handler-log\n    client-secret: lJ1MJ80Kmm1oU6kx0W0RCb2b',
        '10d76c1680cd5bdbd5116b63f34685b2', '2021-11-22 17:09:43', '2021-11-22 17:09:43', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (37, 29, 'jaguar-handler-log-server-dev.yml', 'DEFAULT_GROUP', '',
        'spring:\n  elasticsearch:\n    rest:\n      uris: http://jaguar-es:9200\n      username: elastic\n      password: 123456\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    url: jdbc:es://http://jaguar-es:9200\n    driver-class-name: org.elasticsearch.xpack.sql.jdbc.EsDriver\n    username: elastic\n    password: 123456\n    druid:\n      db-type: mysql\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true\n          merge-sql: true\n      web-stat-filter:\n        enabled: true\n\nmybatis-plus:\n  type-aliases-package: top.wilsonlv.jaguar.**.entity*, com.**.entity*\n  mapper-locations: classpath*:**/mapper/xml/*.xml\n  configuration:\n    call-setters-on-nulls: true\n    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl\n    local-cache-scope: session\n  global-config:\n    db-config:\n      id-type: id_worker\n      update-strategy: NOT_NULL\n      logic-delete-value: 1\n      logic-not-delete-value: 0\n      logic-delete-field: deleted_\n\njaguar:\n  security:\n    client-id: jaguar-handler-log\n    client-secret: lJ1MJ80Kmm1oU6kx0W0RCb2b',
        'c326b873a0a38f10f29ea6c135983623', '2021-11-23 08:49:03', '2021-11-23 08:49:03', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (9, 30, 'application-dev.yml', 'DEFAULT_GROUP', '',
        'server:\n  servlet:\n    encoding:\n      charset: UTF-8\n      force: true\n      enabled: true\n\nspring:\n  cloud:\n    sentinel:\n      transport:\n        dashboard: jaguar-sentinel:18060\n      eager: true\n      log:\n        dir: logs/sentinel\n  redis:\n    database: 6\n    host: jaguar-redis\n    port: 6379\n    password:\n    timeout: 2000\n    lettuce:\n      pool:\n        min-idle: 0\n        max-idle: 8\n        max-active: 8\n        max-wait: 1000\n        time-between-eviction-runs: 100000\n  klock:\n    address: jaguar-redis:6379\n    database: 6\n  activemq:\n    broker-url: tcp://jaguar-mq:61616\n    user: admin\n    password: admin\n    packages:\n      trust-all: true\n    pool:\n      enabled: true\n      max-connections: 20\n  boot:\n    admin:\n      client:\n        url: http://jaguar-monitor:18888\n        username: monitor\n        password: Aa123456\n        instance:\n          prefer-ip: true\n\nmanagement:\n  health:\n    jms:\n      enabled: false\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    logfile:\n      external-file: logs/${spring.application.name}.log\n\njaguar:\n  handler-log:\n    enable: true\n\nlogging:\n  level:\n    top.wilsonlv.jaguar: debug',
        'c2184a94de86f29ca5ad64658b71725c', '2021-11-24 09:22:06', '2021-11-24 09:22:06', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`
(
    `role`     varchar(50)  NOT NULL,
    `resource` varchar(255) NOT NULL,
    `action`   varchar(8)   NOT NULL,
    UNIQUE INDEX `uk_role_permission` (`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_bin
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`
(
    `username` varchar(50) NOT NULL,
    `role`     varchar(50) NOT NULL,
    UNIQUE INDEX `idx_user_role` (`username`, `role`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_bin
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles`
VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`
(
    `id`                bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`         varchar(128)        NOT NULL DEFAULT '' COMMENT 'Tenant ID',
    `quota`             int(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
    `usage`             int(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '使用量',
    `max_size`          int(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
    `max_aggr_count`    int(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
    `max_aggr_size`     int(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    `max_history_count` int(10) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
    `gmt_create`        datetime(0)         NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `gmt_modified`      datetime(0)         NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_tenant_id` (`tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = '租户容量信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `kp`            varchar(128) NOT NULL COMMENT 'kp',
    `tenant_id`     varchar(128) NULL DEFAULT '' COMMENT 'tenant_id',
    `tenant_name`   varchar(128) NULL DEFAULT '' COMMENT 'tenant_name',
    `tenant_desc`   varchar(256) NULL DEFAULT NULL COMMENT 'tenant_desc',
    `create_source` varchar(32)  NULL DEFAULT NULL COMMENT 'create_source',
    `gmt_create`    bigint(20)   NOT NULL COMMENT '创建时间',
    `gmt_modified`  bigint(20)   NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_tenant_info_kptenantid` (`kp`, `tenant_id`) USING BTREE,
    INDEX `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'tenant_info'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `username` varchar(50)  NOT NULL,
    `password` varchar(500) NOT NULL,
    `enabled`  tinyint(1)   NOT NULL,
    PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_bin
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users`
VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;