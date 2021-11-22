DROP TABLE IF EXISTS `jaguar_support_data_modify_log`;
CREATE TABLE `jaguar_support_data_modify_log`
(
    `id_`                   bigint(20)                                             NOT NULL COMMENT 'ID',
    `class_name`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '类全名',
    `record_id`             bigint(20)                                             NOT NULL COMMENT '记录ID',
    `field_name`            varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '字段名称',
    `old_value`             text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         NULL COMMENT '更新前的值',
    `new_value`             text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         NOT NULL COMMENT '更新后的值',
    `last_modify_time`      datetime(0)                                            NOT NULL COMMENT '上次更新时间',
    `last_modify_user_id`   bigint(20)                                             NULL DEFAULT NULL COMMENT '上次更新人ID',
    `last_modify_user_name` varchar(50)                                            NULL DEFAULT NULL COMMENT '上次更新人用户名',
    `modify_time`           datetime(0)                                            NOT NULL COMMENT '本次更新人',
    `modify_user_id`        bigint(20)                                             NULL DEFAULT NULL COMMENT '本次更新人ID',
    `modify_user_name`      varchar(50)                                            NULL DEFAULT NULL COMMENT '本次更新人用户名',
    PRIMARY KEY (`id_`) USING BTREE,
    INDEX `class_name_index` (`class_name`) USING BTREE,
    INDEX `record_id_index` (`record_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '数据修改日志'
  ROW_FORMAT = Dynamic;