DROP TABLE IF EXISTS `jaguar_modules_handler_log`;
CREATE TABLE `jaguar_modules_handler_log`
(
    `id_`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '编号',
    `session_id`    varchar(50)  NOT NULL COMMENT '会话ID',
    `access_time`   datetime     NOT NULL COMMENT '访问时间',
    `client_host`   varchar(50)  NOT NULL COMMENT '客户端ip',
    `request_uri`   varchar(200) NOT NULL COMMENT '请求URI',
    `api_operation` varchar(50)           DEFAULT NULL COMMENT '接口操作名称',
    `parameters_`   text COMMENT '请求参数',
    `method_`       varchar(10)  NOT NULL COMMENT '请求方法',
    `user_agent`    varchar(300)          DEFAULT NULL COMMENT '客户端引擎',
    `status_`       int(3)                DEFAULT NULL COMMENT 'http响应状态码',
    `error_msg`     text                  DEFAULT NULL COMMENT '客户端引擎',
    `duration_`     int(11)               DEFAULT NULL COMMENT '响应时长',
    `deleted_`      tinyint(1)   NOT NULL DEFAULT '0' COMMENT '逻辑删除',
    `remark_`       text,
    `create_by`     bigint(20)            DEFAULT NULL,
    `create_time`   datetime              DEFAULT NULL,
    `update_by`     bigint(20)            DEFAULT NULL,
    `update_time`   datetime              DEFAULT NULL,
    PRIMARY KEY (`id_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='接口请求日志表';