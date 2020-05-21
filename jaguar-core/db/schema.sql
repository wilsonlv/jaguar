DROP TABLE IF EXISTS `jaguar_core_field_edit_log`;
CREATE TABLE `jaguar_core_field_edit_log` (
  `id_` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `class_name` varchar(100) NOT NULL COMMENT '类全名',
  `record_id` bigint(20) NOT NULL COMMENT '记录ID',
  `field_name` varchar(45) NOT NULL COMMENT '字段名称',
  `old_value` text NOT NULL COMMENT '更新前的值',
  `new_value` text NOT NULL COMMENT '更新后的值',
  `deleted_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `remark_` varchar(50) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL COMMENT '上次更新人',
  `create_time` datetime DEFAULT NULL COMMENT '上次更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '本次更新人',
  `update_time` datetime DEFAULT NULL COMMENT '本次更新时间',
  PRIMARY KEY (`id_`),
  KEY `class_name_index` (`class_name`),
  KEY `record_id_index` (`record_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='字段编辑日志表';