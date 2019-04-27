CREATE TABLE `sys_field_edit_log` (
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

CREATE TABLE `sys_log` (
  `id_` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `session_id` varchar(50) NOT NULL COMMENT '会话ID',
  `access_time` datetime NOT NULL COMMENT '访问时间',
  `client_host` varchar(50) NOT NULL COMMENT '客户端ip',
  `request_uri` varchar(200) NOT NULL COMMENT '请求URI',
  `api_operation` varchar(50) DEFAULT NULL COMMENT '接口操作名称',
  `parameters_` text COMMENT '请求参数',
  `method_` varchar(10) NOT NULL COMMENT '请求方法',
  `user_agent` varchar(300) DEFAULT NULL COMMENT '客户端引擎',
  `status_` int(3) DEFAULT NULL COMMENT 'http响应状态码',
  `duration_` int(11) DEFAULT NULL COMMENT '响应时长',
  `deleted_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `remark_` text,
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB AUTO_INCREMENT=24245 DEFAULT CHARSET=utf8 COMMENT='接口请求日志表';