DROP TABLE IF EXISTS `t_system_user`;
CREATE TABLE `t_system_user` (
  `id_`                 bigint(20) NOT NULL COMMENT 'ID',
  `user_account`        varchar(50) UNIQUE NOT NULL COMMENT '用户账号（唯一）',
  `user_phone`          varchar(11) UNIQUE DEFAULT NULL COMMENT '用户手机号（唯一）',
  `user_email`          varchar(50) UNIQUE DEFAULT NULL COMMENT '用户邮箱（唯一）',
  `user_password`       varchar(20) NOT NULL COMMENT '用户密码',
  `user_nick_name`      varchar(20) NOT NULL COMMENT '用户昵称',
  `user_dept_id`        bigint(20) DEFAULT NULL COMMENT '用户部门ID',
  `user_locked`         tinyint(1) DEFAULT '0' COMMENT '用户是否锁定',
  `deleted_`            tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by`           bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time`         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`           bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time`         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='系统用户表';


DROP TABLE IF EXISTS `t_system_login`;
CREATE TABLE `t_system_login` (
  `id_`                  bigint(20) NOT NULL COMMENT 'ID',
  `principal_`          varchar(50) NOT NULL COMMENT '登陆主体',
  `credentials_`        varchar(50) NOT NULL COMMENT '登陆凭证',
  `login_ip`            varchar(50) NOT NULL COMMENT '登陆IP',
  `login_time`          datetime NOT NULL COMMENT '登陆时间',
  `session_id`          varchar(20) NOT NULL COMMENT '会话ID',
  `result_code`         int NOT NULL COMMENT '响应码',
  `client_type`         varchar(10) NOT NULL COMMENT '客户端类型',
  `client_version`      varchar(10) NOT NULL COMMENT '客户端版本',
  `device_model`        varchar(20) DEFAULT NULL COMMENT '设备型号',
  `device_sys_version`  varchar(20) DEFAULT NULL COMMENT '设备系统版本',
  `device_imei`         varchar(50) DEFAULT NULL COMMENT '设备唯一编号',
  `deleted_`            tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by`           bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time`         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`           bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time`         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='系统登陆日志表';


DROP TABLE IF EXISTS `t_system_role`;
CREATE TABLE `t_system_role` (
  `id_`                  bigint(20) NOT NULL COMMENT 'ID',
  `role_name`           varchar(20) NOT NULL COMMENT '角色名称',
  `role_data_scope`     varchar(5) NOT NULL COMMENT '角色数据权限（OWNER、DEPT、ALL）',
  `role_locked`         tinyint(1) DEFAULT '0' COMMENT '角色是否锁定',
  `deleted_`            tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by`           bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time`         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`           bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time`         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='系统角色表';


DROP TABLE IF EXISTS `t_system_user_role`;
CREATE TABLE `t_system_user_role` (
  `id_`                  bigint(20) NOT NULL COMMENT 'ID',
  `user_id`             bigint(20) NOT NULL COMMENT '用户ID',
  `role_id`             bigint(20) NOT NULL COMMENT '角色ID',
  `deleted_`            tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by`           bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time`         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`           bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time`         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='系统用户角色表';


DROP TABLE IF EXISTS `t_system_menu`;
CREATE TABLE `t_system_menu` (
  `id_`                  bigint(20) NOT NULL COMMENT 'ID',
  `menu_name`           varchar(20) NOT NULL COMMENT '菜单名称',
  `menu_parent_id`      bigint(20) DEFAULT NULL COMMENT '上级菜单ID',
  `menu_icon`           varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `menu_page_uri`       varchar(100) DEFAULT NULL COMMENT '菜单URI',
  `menu_sort_no`        int DEFAULT NULL COMMENT '菜单排序号',
  `menu_auth_name`      varchar(50) DEFAULT NULL COMMENT '菜单权限名称',
  `menu_type`           varchar(10) NOT NULL COMMENT '菜单类型（MENU，FUNCTION）',
  `deleted_`            tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by`           bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time`         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`           bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time`         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='系统菜单表';


DROP TABLE IF EXISTS `t_system_role_menu`;
CREATE TABLE `t_system_menu` (
  `id_`                      bigint(20) NOT NULL COMMENT 'ID',
  `role_id`                 bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id`                 bigint(20) NOT NULL COMMENT '菜单ID',
  `role_menu_permission`    VARCHAR(20) DEFAULT NULL COMMENT '角色菜单权限（READ，VIEW，UPDATE，DEL）',
  `deleted_`                tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  `create_by`               bigint(20) unsigned DEFAULT NULL COMMENT '创建人',
  `create_time`             timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`               bigint(20) unsigned DEFAULT NULL COMMENT '最新修改人',
  `update_time`             timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='系统角色菜单表';

