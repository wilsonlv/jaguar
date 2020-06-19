DROP TABLE IF EXISTS `jaguar_modules_handler_log`;
CREATE TABLE `jaguar_modules_handler_log`
(
    `id_`           bigint(20)                                             NOT NULL COMMENT '编号',
    `session_id`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '会话ID',
    `access_time`   datetime(0)                                            NOT NULL COMMENT '访问时间',
    `client_host`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '客户端ip',
    `request_uri`   varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '请求URI',
    `api_operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '接口操作名称',
    `parameters_`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         NULL COMMENT '请求参数',
    `method_`       varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '请求方法',
    `user_agent`    varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '客户端引擎',
    `status_`       int(11)                                                NULL     DEFAULT NULL COMMENT 'http响应状态码',
    `error_msg`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         NULL COMMENT '客户端引擎',
    `duration_`     int(11)                                                NULL     DEFAULT NULL COMMENT '响应时长',
    `deleted_`      tinyint(1)                                             NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    `create_by`     bigint(20) UNSIGNED                                    NULL     DEFAULT NULL COMMENT '创建人',
    `create_time`   timestamp(0)                                           NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`     bigint(20) UNSIGNED                                    NULL     DEFAULT NULL COMMENT '最新修改人',
    `update_time`   timestamp(0)                                           NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = MyISAM
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '接口请求日志表'
  ROW_FORMAT = Dynamic;