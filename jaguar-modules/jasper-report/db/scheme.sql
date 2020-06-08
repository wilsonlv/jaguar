DROP TABLE IF EXISTS `jaguar_modules_jasper_report_template`;
CREATE TABLE `jaguar_modules_jasper_report_template`
(
    `id_`           bigint(20)  NOT NULL COMMENT 'ID',
    `template_name` varchar(50) NOT NULL COMMENT '模板名称',
    `template_type` varchar(50) NOT NULL COMMENT '模板类型',
    `document_id`   bigint(20)           DEFAULT NULL COMMENT '文档ID',
    `deleted_`      tinyint(1)  NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
    `create_by`     bigint(20) unsigned  DEFAULT NULL COMMENT '创建人',
    `create_time`   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`     bigint(20) unsigned  DEFAULT NULL COMMENT '最新修改人',
    `update_time`   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id_`)
) ENGINE = InnoDB
  CHARSET = utf8mb4 COMMENT ='jasperReport模板表';