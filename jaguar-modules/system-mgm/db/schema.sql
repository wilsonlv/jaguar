DROP TABLE IF EXISTS `jaguar_modules_system_login`;
CREATE TABLE `jaguar_modules_system_login`
(
    `id_`                bigint(20)                                            NOT NULL COMMENT 'ID',
    `principal_`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '登陆主体',
    `credentials_`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '登陆凭证',
    `password_free`      tinyint(1)                                            NULL DEFAULT 0 COMMENT '免密登录',
    `verify_code`        varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '验证码',
    `login_ip`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '登陆IP',
    `login_time`         datetime(0)                                           NOT NULL COMMENT '登陆时间',
    `session_id`         varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '会话ID',
    `result_code`        int(11)                                               NOT NULL COMMENT '响应码',
    `client_type`        varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端类型',
    `client_version`     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端版本',
    `device_model`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '设备型号',
    `device_sys_version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '设备系统版本',
    `device_imei`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '设备唯一编号',
    `user_id`            bigint(20)                                            NULL DEFAULT NULL COMMENT '已登录的用户ID',
    `system_name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '系统名称',
    `tenant_`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '租户',
    `remark_`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    `create_time`        timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `deleted_`           tinyint(1)                                            NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '系统登陆日志表'
  ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `jaguar_modules_system_user`;
CREATE TABLE `jaguar_modules_system_user`
(
    `id_`             bigint(20)                                            NOT NULL COMMENT 'ID',
    `user_account`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户账号（唯一）',
    `user_phone`      varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户手机号（唯一）',
    `user_email`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户邮箱（唯一）',
    `user_password`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户密码',
    `user_nick_name`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户昵称',
    `user_data_scope` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户数据权限（PERSONAL、CURRENT_LEVEL、SUBLEVEL、CURRENT_AND_SUBLEVEL、UNLIMITED）',
    `user_enable`     tinyint(1)                                            NULL DEFAULT 1 COMMENT '用户是否启用',
    `user_locked`     tinyint(1)                                            NULL DEFAULT 0 COMMENT '用户是否锁定',
    `remark_`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    `create_time`     timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `deleted_`        tinyint(1)                                            NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE,
    UNIQUE INDEX `user_account` (`user_account`) USING BTREE,
    UNIQUE INDEX `user_phone` (`user_phone`) USING BTREE,
    UNIQUE INDEX `user_email` (`user_email`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '系统用户表'
  ROW_FORMAT = Dynamic;

INSERT INTO `jaguar_modules_system_user`
VALUES (1, 'admin', NULL , NULL , 'admin123!@#ADMIN', '管理员', 'UNLIMITED', 1, 0, NULL , '2021-04-01 12:00:00', 0);


DROP TABLE IF EXISTS `jaguar_modules_system_role`;
CREATE TABLE `jaguar_modules_system_role`
(
    `id_`         bigint(20)                                            NOT NULL COMMENT 'ID',
    `role_name`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
    `role_enable` tinyint(1)                                            NULL DEFAULT 1 COMMENT '角色是否启用',
    `remark_`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    `create_time` timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `deleted_`    tinyint(1)                                            NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '系统角色表'
  ROW_FORMAT = Dynamic;

INSERT INTO `jaguar_modules_system_role`
VALUES (1, '超级管理员', 1, null, '2021-04-01 12:00:00', 0);


DROP TABLE IF EXISTS `jaguar_modules_system_user_role`;
CREATE TABLE `jaguar_modules_system_user_role`
(
    `id_`         bigint(20)                                            NOT NULL COMMENT 'ID',
    `user_id`     bigint(20)                                            NOT NULL COMMENT '用户ID',
    `role_id`     bigint(20)                                            NOT NULL COMMENT '角色ID',
    `remark_`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    `create_time` timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `deleted_`    tinyint(1)                                            NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '系统用户角色表'
  ROW_FORMAT = Dynamic;

INSERT INTO `jaguar_modules_system_user_role`
VALUES (1, 1, 1, null, '2021-04-01 12:00:00', 0);


DROP TABLE IF EXISTS `jaguar_modules_system_role_menu`;
CREATE TABLE `jaguar_modules_system_role_menu`
(
    `id_`                  bigint(20)                                            NOT NULL COMMENT 'ID',
    `role_id`              bigint(20)                                            NOT NULL COMMENT '角色ID',
    `menu_function_name`   varchar(50)                                           NOT NULL COMMENT '菜单功能名称',
    `remark_`              varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    `create_time`          timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `deleted_`             tinyint(1)                                            NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '系统角色菜单表'
  ROW_FORMAT = Dynamic;

INSERT INTO `jaguar_modules_system_role_menu`
VALUES (1, 1, '系统管理', null, '2021-04-01 12:00:00', 0);
INSERT INTO `jaguar_modules_system_role_menu`
VALUES (2, 1, '用户管理', null, '2021-04-01 12:00:00', 0);
INSERT INTO `jaguar_modules_system_role_menu`
VALUES (3, 1, '角色管理', null, '2021-04-01 12:00:00', 0);