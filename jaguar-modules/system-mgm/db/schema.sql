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
    `deleted_`           tinyint(1)                                            NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    `create_by`          bigint(20) UNSIGNED                                   NULL COMMENT '创建人',
    `create_time`        timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`          bigint(20) UNSIGNED                                   NULL COMMENT '最新修改人',
    `update_time`        timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
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
    `user_data_scope` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户数据权限（PERSONAL、LEVEL、UNLIMITED）',
    `user_locked`     tinyint(1)                                            NULL DEFAULT 0 COMMENT '用户是否锁定',
    `deleted_`        tinyint(1)                                            NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    `create_by`       bigint(20) UNSIGNED                                   NOT NULL COMMENT '创建人',
    `create_time`     timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`       bigint(20) UNSIGNED                                   NOT NULL COMMENT '最新修改人',
    `update_time`     timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    PRIMARY KEY (`id_`) USING BTREE,
    UNIQUE INDEX `user_account` (`user_account`) USING BTREE,
    UNIQUE INDEX `user_phone` (`user_phone`) USING BTREE,
    UNIQUE INDEX `user_email` (`user_email`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '系统用户表'
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `jaguar_modules_system_role`;
CREATE TABLE `jaguar_modules_system_role`
(
    `id_`         bigint(20)                                            NOT NULL COMMENT 'ID',
    `role_name`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
    `role_locked` tinyint(1)                                            NULL DEFAULT 0 COMMENT '角色是否锁定',
    `deleted_`    tinyint(1)                                            NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    `create_by`   bigint(20) UNSIGNED                                   NOT NULL COMMENT '创建人',
    `create_time` timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`   bigint(20) UNSIGNED                                   NOT NULL COMMENT '最新修改人',
    `update_time` timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '系统角色表'
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `jaguar_modules_system_user_role`;
CREATE TABLE `jaguar_modules_system_user_role`
(
    `id_`         bigint(20)          NOT NULL COMMENT 'ID',
    `user_id`     bigint(20)          NOT NULL COMMENT '用户ID',
    `role_id`     bigint(20)          NOT NULL COMMENT '角色ID',
    `deleted_`    tinyint(1)          NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    `create_by`   bigint(20) UNSIGNED NOT NULL COMMENT '创建人',
    `create_time` timestamp(0)        NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`   bigint(20) UNSIGNED NOT NULL COMMENT '最新修改人',
    `update_time` timestamp(0)        NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '系统用户角色表'
  ROW_FORMAT = Dynamic;

INSERT INTO `jaguar_modules_system_user_role`
VALUES (1, 1, 1, 0, 1, null, 1, null);


DROP TABLE IF EXISTS `jaguar_modules_system_menu`;
CREATE TABLE `jaguar_modules_system_menu`
(
    `id_`            bigint(20)                                             NOT NULL COMMENT 'ID',
    `menu_name`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '菜单名称',
    `menu_parent_id` bigint(20)                                             NULL DEFAULT NULL COMMENT '上级菜单ID',
    `menu_icon`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '菜单图标',
    `menu_page_uri`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单URI',
    `menu_sort_no`   int(11)                                                NULL DEFAULT NULL COMMENT '菜单排序号',
    `menu_auth_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '菜单权限名称',
    `menu_type`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '菜单类型（MENU，FUNCTION）',
    `deleted_`       tinyint(1)                                             NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    `create_by`      bigint(20) UNSIGNED                                    NOT NULL COMMENT '创建人',
    `create_time`    timestamp(0)                                           NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`      bigint(20) UNSIGNED                                    NOT NULL COMMENT '最新修改人',
    `update_time`    timestamp(0)                                           NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '系统菜单表'
  ROW_FORMAT = Dynamic;

INSERT INTO `jaguar_modules_system_menu`
VALUES (1, '系统管理', NULL, 'el-icon-s-cooperation', '/system_mgm', 100, '系统管理', 'MENU', 0, 1, null, 1, null);
INSERT INTO `jaguar_modules_system_menu`
VALUES (2, '用户管理', 1, NULL, 'user', 1, '系统用户表', 'MENU', 0, 1, null, 1, null);
INSERT INTO `jaguar_modules_system_menu`
VALUES (3, '角色管理', 1, NULL, 'role', 2, '系统角色表', 'MENU', 0, 1, null, 1, null);
INSERT INTO `jaguar_modules_system_menu`
VALUES (4, '菜单管理', 1, NULL, 'menu', 3, '系统菜单表', 'MENU', 0, 1, null, 1, null);
INSERT INTO `jaguar_modules_system_menu`
VALUES (5, '权限管理', 3, NULL, NULL, 4, '系统角色菜单表', 'FUNCTION', 0, 1, null, 1, null);


DROP TABLE IF EXISTS `jaguar_modules_system_role_menu`;
CREATE TABLE `jaguar_modules_system_role_menu`
(
    `id_`                  bigint(20)                                            NOT NULL COMMENT 'ID',
    `role_id`              bigint(20)                                            NOT NULL COMMENT '角色ID',
    `menu_id`              bigint(20)                                            NOT NULL COMMENT '菜单ID',
    `role_menu_permission` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '角色菜单权限（READ，VIEW，UPDATE，DEL）',
    `deleted_`             tinyint(1)                                            NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    `create_by`            bigint(20) UNSIGNED                                   NOT NULL COMMENT '创建人',
    `create_time`          timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`            bigint(20) UNSIGNED                                   NOT NULL COMMENT '最新修改人',
    `update_time`          timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '系统角色菜单表'
  ROW_FORMAT = Dynamic;

INSERT INTO `jaguar_modules_system_role_menu`
VALUES (1, 1, 1, 'READ_VIEW_UPDATE_DELETE', 0, 1, null, 1, null);
INSERT INTO `jaguar_modules_system_role_menu`
VALUES (2, 1, 2, 'READ_VIEW_UPDATE_DELETE', 0, 1, null, 1, null);
INSERT INTO `jaguar_modules_system_role_menu`
VALUES (3, 1, 3, 'READ_VIEW_UPDATE_DELETE', 0, 1, null, 1, null);
INSERT INTO `jaguar_modules_system_role_menu`
VALUES (4, 1, 4, 'READ_VIEW_UPDATE_DELETE', 0, 1, null, 1, null);
INSERT INTO `jaguar_modules_system_role_menu`
VALUES (5, 1, 5, 'READ_VIEW_UPDATE_DELETE', 0, 1, null, 1, null);
INSERT INTO `jaguar_modules_system_role_menu`
VALUES (6, 1, 6, 'READ_VIEW_UPDATE_DELETE', 0, 1, null, 1, null);