DROP TABLE IF EXISTS `jaguar_modules_workflow_button_def`;
CREATE TABLE `jaguar_modules_workflow_button_def` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `name_` varchar(45) NOT NULL COMMENT '按钮名称',
  `label_` varchar(45) NOT NULL COMMENT '按钮标签',
  `button_action_type` varchar(45) NOT NULL COMMENT '按钮类型（内置：BUILT_IN，附加：ADDON）',
  `default_setting` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否默认配置（0：否，1：是）',
  `show_pages` varchar(45) DEFAULT NULL COMMENT '在哪些页面展示，多个页面逗号分隔（PRE_CREATE，TASK_HANDLE，TASK_VIEW）',
  `button_position` varchar(45) NOT NULL DEFAULT 'BUTTOM' COMMENT '按钮位置（BUTTOM，LEFT_TOP，RIGHT_TOP）',
  `component_` varchar(100) NOT NULL COMMENT '按钮组件',
  `class_style` varchar(45) DEFAULT NULL COMMENT '按钮样式',
  `sort_no` int(11) NOT NULL DEFAULT '5' COMMENT '排序号',
  `remark_` varchar(100) DEFAULT NULL COMMENT '备注',
  `deleted_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='按钮定义表';


DROP TABLE IF EXISTS `jaguar_modules_workflow_button_inst`;
CREATE TABLE `jaguar_modules_workflow_button_inst` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `button_def_id` bigint(20) NOT NULL COMMENT '按钮定义ID',
  `process_definition_key` varchar(45) NOT NULL COMMENT '流程定义名称',
  `task_def_name` varchar(45) NOT NULL COMMENT '流程任务定义名称',
  `sort_no` int(11) NOT NULL DEFAULT '0' COMMENT '排序号',
  `params_` varchar(200) DEFAULT NULL COMMENT '实例参数',
  `deleted_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='按钮实例表';


DROP TABLE IF EXISTS `jaguar_modules_workflow_draft_definition`;
CREATE TABLE `jaguar_modules_workflow_draft_definition` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `element_id` varchar(50) DEFAULT NULL,
  `name_` varchar(45) NOT NULL COMMENT '名称',
  `definition_type` varchar(10) NOT NULL COMMENT '类型（FORM：表单，FLOW：流程）',
  `context_` text NOT NULL COMMENT '内容',
  `version_` int(11) NOT NULL COMMENT '版本',
  `deleted_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='草稿表';


DROP TABLE IF EXISTS `jaguar_modules_workflow_form_data`;
CREATE TABLE `jaguar_modules_workflow_form_data` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `process_info_id` bigint(20) NOT NULL COMMENT '流程信息ID',
  `form_template_id` bigint(20) NOT NULL COMMENT '表单模版ID',
  `form_template_sheet_id` bigint(20) NOT NULL COMMENT '表单块ID',
  `form_template_field_id` bigint(20) NOT NULL COMMENT '表单字段ID',
  `field_key` varchar(50) NOT NULL COMMENT '表单字段key',
  `value_` varchar(200) DEFAULT NULL COMMENT '值',
  `batch_num` int(11) NOT NULL DEFAULT '0' COMMENT '批次号',
  `form_data_persistence_type` varchar(20) DEFAULT NULL COMMENT '数据存储方式（VALUE,FORM_DATA_ATTACH,USER_DEFINED）',
  `deleted_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `process_info_id` (`process_info_id`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='表单数据表';


DROP TABLE IF EXISTS `jaguar_modules_workflow_form_data_attach`;
CREATE TABLE `jaguar_modules_workflow_form_data_attach` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `form_data_id` bigint(20) NOT NULL COMMENT '表单数据表ID',
  `value_` text NOT NULL COMMENT '值',
  `deleted_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单数据附件表';


DROP TABLE IF EXISTS `jaguar_modules_workflow_form_template`;
CREATE TABLE `jaguar_modules_workflow_form_template` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `element_id` varchar(50) NOT NULL COMMENT 'xml元素ID',
  `name_` varchar(50) NOT NULL COMMENT '表单名称',
  `version_` int(11) NOT NULL COMMENT '版本',
  `deleted_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='表单模版表';


DROP TABLE IF EXISTS `jaguar_modules_workflow_form_template_field`;
CREATE TABLE `jaguar_modules_workflow_form_template_field` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `form_template_id` bigint(20) NOT NULL COMMENT '表单模版ID',
  `form_template_sheet_id` bigint(20) NOT NULL COMMENT '表单块ID',
  `form_template_row_id` bigint(20) NOT NULL COMMENT '表单行ID',
  `label_` varchar(50) NOT NULL COMMENT '字段标签',
  `key_` varchar(50) NOT NULL COMMENT '字段key',
  `form_template_field_type` varchar(30) NOT NULL COMMENT '字段类型',
  `visible_` tinyint(1) NOT NULL DEFAULT '1' COMMENT '当字段类型为text时，是否显示',
  `unique_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '当字段类型为text时，值是否在流程定义中唯一',
  `placeholder_` varchar(50) DEFAULT NULL COMMENT '提示信息',
  `result_value` varchar(200) DEFAULT NULL COMMENT '结果或结果表达式',
  `default_value` varchar(200) DEFAULT NULL COMMENT '默认值或默认值表达式',
  `foucs_out_event` varchar(50) DEFAULT NULL COMMENT '焦点离开事件',
  `sort_no` tinyint(1) DEFAULT NULL COMMENT '排序号',
  `dic_type` varchar(20) DEFAULT NULL COMMENT '数据字典类型',
  `time_pattern` varchar(45) DEFAULT NULL COMMENT '时间格式',
  `component_config` varchar(1024) DEFAULT NULL COMMENT '组件配置',
  `deleted_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='表单字段表';


DROP TABLE IF EXISTS `jaguar_modules_workflow_form_template_row`;
CREATE TABLE `jaguar_modules_workflow_form_template_row` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `form_template_id` bigint(20) NOT NULL COMMENT '表单模版ID',
  `form_template_sheet_id` bigint(20) NOT NULL COMMENT '表单块ID',
  `sort_no` int(11) DEFAULT NULL COMMENT '排序号',
  `deleted_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='表单行表';


DROP TABLE IF EXISTS `jaguar_modules_workflow_form_template_sheet`;
CREATE TABLE `jaguar_modules_workflow_form_template_sheet` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `element_id` varchar(50) NOT NULL COMMENT 'xml元素ID',
  `form_template_id` bigint(20) NOT NULL COMMENT '表单模版ID',
  `name_` varchar(50) NOT NULL COMMENT '表单块名称',
  `override_` tinyint(1) NOT NULL DEFAULT '1' COMMENT '数据重写',
  `sort_no` int(11) DEFAULT NULL COMMENT '排序号',
  `deleted_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='表单块表';


DROP TABLE IF EXISTS `jaguar_modules_workflow_process_info`;
CREATE TABLE `jaguar_modules_workflow_info` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `process_definition_id` varchar(45) NOT NULL COMMENT '流程定义ID',
  `process_instance_id` varchar(45) NOT NULL COMMENT '流程实例ID',
  `form_template_id` bigint(20) NOT NULL COMMENT '表单模版ID',
  `process_num` varchar(45) NOT NULL COMMENT '流程编号',
  `initiator_` varchar(45) NOT NULL COMMENT '发起人',
  `title_` varchar(100) DEFAULT NULL COMMENT '流程名称',
  `priority_` tinyint(1) NOT NULL DEFAULT '3' COMMENT '优先级（1：非常紧急，2：紧急，3-50：普通）',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `suspend_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否挂起',
  `deleted_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='工单信息表';


DROP TABLE IF EXISTS `jaguar_modules_workflow_operation_record`;
CREATE TABLE `jaguar_modules_workflow_operation_record` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `process_info_id` bigint(20) NOT NULL COMMENT '工单信息ID',
  `operator_` varchar(45) NOT NULL COMMENT '操作人账号',
  `process_operation_type` varchar(45) NOT NULL COMMENT '操作类型',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  `task_name` varchar(45) DEFAULT NULL COMMENT '任务名称',
  `task_inst_id` varchar(45) DEFAULT NULL COMMENT '任务实例ID',
  `task_def_id` varchar(45) DEFAULT NULL COMMENT '任务定义ID',
  `assignee_` varchar(45) DEFAULT NULL COMMENT '受派人账号',
  `reason_` varchar(200) DEFAULT NULL COMMENT '回退原因',
  `batch_num` varchar(32) DEFAULT NULL COMMENT '批次号',
  `deleted_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by` bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='工单操作记录表';
