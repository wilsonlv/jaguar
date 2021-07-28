CREATE DATABASE IF NOT EXISTS `jaguar_upms` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
use `jaguar_upms`;

DROP TABLE IF EXISTS `jaguar_cloud_upms_oauth_client`;
CREATE TABLE `jaguar_cloud_upms_oauth_client`
(
    `id_`                            bigint(20)   NOT NULL COMMENT 'ID',
    `client_id`                      varchar(256) NOT NULL,
    `client_secret`                  varchar(256)      DEFAULT NULL,
    `resource_ids`                   varchar(256)      DEFAULT NULL,
    `scope_`                         varchar(256)      DEFAULT NULL,
    `authorized_grant_types`         varchar(256) NOT NULL,
    `registered_redirect_uris`       varchar(256)      DEFAULT NULL,
    `authorities_`                   varchar(256)      DEFAULT NULL,
    `access_token_validity_seconds`  int(11)      NOT NULL,
    `refresh_token_validity_seconds` int(11)      NOT NULL,
    `additional_information`         varchar(4096)     DEFAULT NULL,
    `auto_approve_scopes`            varchar(256)      DEFAULT NULL,
    `client_enable`                  tinyint(1)   NULL DEFAULT 1,
    `remark_`                        varchar(50)  NULL DEFAULT NULL COMMENT '备注',
    `create_by`                      varchar(50)  NULL COMMENT '创建人',
    `create_user_id`                 bigint(20)   NULL COMMENT '创建人ID',
    `create_time`                    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`                      varchar(50)  NULL COMMENT '更新人',
    `update_user_id`                 bigint(20)   NULL COMMENT '更新人ID',
    `update_time`                    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`                       tinyint(1)   NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = 'oauth2客户端'
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `jaguar_cloud_upms_user`;
CREATE TABLE `jaguar_cloud_upms_user`
(
    `id_`            bigint(20)   NOT NULL COMMENT 'ID',
    `user_account`   varchar(50)  NOT NULL COMMENT '用户账号（唯一）',
    `user_phone`     varchar(11)  NULL DEFAULT NULL COMMENT '用户手机号（唯一）',
    `user_email`     varchar(50)  NULL DEFAULT NULL COMMENT '用户邮箱（唯一）',
    `user_password`  varchar(100) NOT NULL COMMENT '用户密码',
    `user_nick_name` varchar(20)  NULL DEFAULT NULL COMMENT '用户昵称',
    `user_enable`    tinyint(1)   NULL DEFAULT 1 COMMENT '用户是否启用',
    `user_locked`    tinyint(1)   NULL DEFAULT 0 COMMENT '用户是否锁定',
    `remark_`        varchar(50)  NULL DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(50)  NULL COMMENT '创建人',
    `create_user_id` bigint(20)   NULL COMMENT '创建人ID',
    `create_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`      varchar(50)  NULL COMMENT '更新人',
    `update_user_id` bigint(20)   NULL COMMENT '更新人ID',
    `update_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`       tinyint(1)   NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE,
    UNIQUE INDEX `user_account` (`user_account`) USING BTREE,
    UNIQUE INDEX `user_phone` (`user_phone`) USING BTREE,
    UNIQUE INDEX `user_email` (`user_email`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '用户表'
  ROW_FORMAT = Dynamic;

INSERT INTO `jaguar_cloud_upms_user`
VALUES (1419901871475785729, 'admin', NULL, NULL, '$2a$10$zz0cVKy8NFtiR7/vP4mWHOiS144mBXkjlqQ81Cjbox3ayekIa02HG', '管理员',
        1, 0, null, null, null, null, null, null, null, 0);
-- admin/Aa123456

DROP TABLE IF EXISTS `jaguar_cloud_upms_role`;
CREATE TABLE `jaguar_cloud_upms_role`
(
    `id_`            bigint(20)   NOT NULL COMMENT 'ID',
    `role_name`      varchar(20)  NOT NULL COMMENT '角色名称',
    `role_enable`    tinyint(1)   NULL DEFAULT 1 COMMENT '角色是否启用',
    `role_built_in`  tinyint(1)   NULL DEFAULT 0 COMMENT '是否内置角色',
    `remark_`        varchar(50)  NULL DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(50)  NULL COMMENT '创建人',
    `create_user_id` bigint(20)   NULL COMMENT '创建人ID',
    `create_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`      varchar(50)  NULL COMMENT '更新人',
    `update_user_id` bigint(20)   NULL COMMENT '更新人ID',
    `update_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`       tinyint(1)   NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '角色表'
  ROW_FORMAT = Dynamic;

INSERT INTO `jaguar_cloud_upms_role`
VALUES (1419901871475785730, '超级管理员', 1, 1, null, null, null, null, null, null, null, 0);


DROP TABLE IF EXISTS `jaguar_cloud_upms_user_role`;
CREATE TABLE `jaguar_cloud_upms_user_role`
(
    `id_`            bigint(20)   NOT NULL COMMENT 'ID',
    `user_id`        bigint(20)   NOT NULL COMMENT '用户ID',
    `role_id`        bigint(20)   NOT NULL COMMENT '角色ID',
    `built_in`       tinyint(1)   NULL DEFAULT 0 COMMENT '是否内置',
    `remark_`        varchar(50)  NULL DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(50)  NULL COMMENT '创建人',
    `create_user_id` bigint(20)   NULL COMMENT '创建人ID',
    `create_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`      varchar(50)  NULL COMMENT '更新人',
    `update_user_id` bigint(20)   NULL COMMENT '更新人ID',
    `update_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`       tinyint(1)   NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '用户角色表'
  ROW_FORMAT = Dynamic;

INSERT INTO `jaguar_cloud_upms_user_role`
VALUES (1419901871475785731, 1419901871475785729, 1419901871475785730, 1, null, null, null, null, null, null, null, 0);


DROP TABLE IF EXISTS `jaguar_cloud_upms_menu`;
CREATE TABLE `jaguar_cloud_upms_menu`
(
    `id_`             bigint(20)   NOT NULL COMMENT 'ID',
    `parent_id`       bigint(20)   NULL     DEFAULT NULL COMMENT '父ID',
    `menu_name`       varchar(50)  NOT NULL COMMENT '名称',
    `menu_permission` varchar(50)  NOT NULL COMMENT '权限',
    `menu_button`     tinyint(1)   NOT NULL DEFAULT 0 COMMENT '是否为按钮',
    `menu_icon`       varchar(50)  NULL     DEFAULT NULL COMMENT '图标',
    `menu_page`       varchar(50)  NULL     DEFAULT NULL COMMENT '展示页面',
    `remark_`         varchar(50)  NULL     DEFAULT NULL COMMENT '备注',
    `create_by`       varchar(50)  NULL COMMENT '创建人',
    `create_user_id`  bigint(20)   NULL COMMENT '创建人ID',
    `create_time`     timestamp(0) NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`       varchar(50)  NULL COMMENT '更新人',
    `update_user_id`  bigint(20)   NULL COMMENT '更新人ID',
    `update_time`     timestamp(0) NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`        tinyint(1)   NULL     DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '菜单表'
  ROW_FORMAT = Dynamic;

INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1419901871475785732, null, '系统管理', '系统管理', 0, null, '/upms',
        null, null, null, null, null, null, null, 0),
       (1419901871475785733, 1419901871475785732, '用户管理', '用户管理', 0, null, '/user',
        null, null, null, null, null, null, null, 0),
       (1419901871475785734, 1419901871475785732, '权限管理', '权限管理', 0, null, '/role',
        null, null, null, null, null, null, null, 0),
       (1419901871475785735, 1419901871475785732, '菜单管理', '菜单管理', 0, null, '/menu',
        null, null, null, null, null, null, null, 0);


DROP TABLE IF EXISTS `jaguar_cloud_upms_role_menu`;
CREATE TABLE `jaguar_cloud_upms_role_menu`
(
    `id_`            bigint(20)   NOT NULL COMMENT 'ID',
    `role_id`        bigint(20)   NOT NULL COMMENT '角色ID',
    `menu_id`        bigint(50)   NOT NULL COMMENT '菜单ID',
    `built_in`       tinyint(1)   NULL DEFAULT 0 COMMENT '是否内置',
    `remark_`        varchar(50)  NULL DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(50)  NULL COMMENT '创建人',
    `create_user_id` bigint(20)   NULL COMMENT '创建人ID',
    `create_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`      varchar(50)  NULL COMMENT '更新人',
    `update_user_id` bigint(20)   NULL COMMENT '更新人ID',
    `update_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`       tinyint(1)   NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '角色菜单表'
  ROW_FORMAT = Dynamic;

INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1419901871475785736, 1419901871475785730, 1419901871475785732, 1, null, null, null, null, null, null, null, 0),
       (1419901871475785737, 1419901871475785730, 1419901871475785733, 1, null, null, null, null, null, null, null, 0),
       (1419901871475785738, 1419901871475785730, 1419901871475785734, 1, null, null, null, null, null, null, null, 0),
       (1419901871475785739, 1419901871475785730, 1419901871475785735, 1, null, null, null, null, null, null, null, 0);