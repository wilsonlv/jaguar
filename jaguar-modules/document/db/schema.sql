DROP TABLE IF EXISTS `jaguar_modules_document`;
CREATE TABLE `jaguar_modules_document`
(
    `id_`           bigint(20)                                             NOT NULL COMMENT 'ID',
    `original_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '原始文档名称',
    `extension_`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '文档拓展名',
    `absolute_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '文档绝对路径',
    `total_space`   bigint(20)                                             NOT NULL COMMENT '文档占用空间',
    `deleted_`      tinyint(1)                                             NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    `create_by`     bigint(20) UNSIGNED                                    NULL     DEFAULT NULL COMMENT '创建人',
    `create_time`   timestamp(0)                                           NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`     bigint(20) UNSIGNED                                    NULL     DEFAULT NULL COMMENT '最新修改人',
    `update_time`   timestamp(0)                                           NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '文档上传表'
  ROW_FORMAT = Dynamic;

