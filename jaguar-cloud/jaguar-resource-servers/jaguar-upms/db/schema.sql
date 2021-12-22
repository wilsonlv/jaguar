CREATE DATABASE IF NOT EXISTS `jaguar_upms` default character set utf8mb4 collate utf8mb4_unicode_ci;
USE `jaguar_upms`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for jaguar_cloud_upms_dept
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_cloud_upms_dept`;
CREATE TABLE `jaguar_cloud_upms_dept`
(
    `id_`            bigint(20)                                            NOT NULL COMMENT 'ID',
    `parent_id`      bigint(20)                                            NULL DEFAULT NULL COMMENT '父ID',
    `dept_name`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '部门名称',
    `remark_`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
    `create_user_id` bigint(20)                                            NULL DEFAULT NULL COMMENT '创建人ID',
    `create_time`    timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
    `update_user_id` bigint(20)                                            NULL DEFAULT NULL COMMENT '更新人ID',
    `update_time`    timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`       tinyint(1)                                            NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '部门'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jaguar_cloud_upms_dept
-- ----------------------------
INSERT INTO `jaguar_cloud_upms_dept`
VALUES (1460134989638668289, 0, '总公司', NULL, NULL, NULL, '2021-11-15 14:38:04', NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_dept`
VALUES (1460135982304595970, 1460134989638668289, '研发中心', NULL, NULL, NULL, '2021-11-15 14:42:01', NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_dept`
VALUES (1460136096729403394, 1460135982304595970, '技术1部', NULL, NULL, NULL, '2021-11-15 14:42:28', NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_dept`
VALUES (1460136520400244738, 1460135982304595970, '技术2部', NULL, NULL, NULL, '2021-11-15 14:44:09', NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for jaguar_cloud_upms_menu
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_cloud_upms_menu`;
CREATE TABLE `jaguar_cloud_upms_menu`
(
    `id_`                bigint(20)                                            NOT NULL COMMENT 'ID',
    `resource_server_id` bigint(20)                                            NULL     DEFAULT NULL COMMENT '资源服务ID',
    `parent_id`          bigint(20)                                            NOT NULL DEFAULT 0 COMMENT '父ID',
    `menu_name`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
    `menu_permission`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '权限',
    `menu_button`        tinyint(1)                                            NOT NULL DEFAULT 0 COMMENT '是否为按钮',
    `menu_icon`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '图标',
    `menu_page`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '展示页面',
    `menu_order`         int(11)                                               NULL     DEFAULT NULL COMMENT '排序',
    `remark_`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '备注',
    `create_by`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '创建人',
    `create_user_id`     bigint(20)                                            NULL     DEFAULT NULL COMMENT '创建人ID',
    `create_time`        timestamp(0)                                          NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '更新人',
    `update_user_id`     bigint(20)                                            NULL     DEFAULT NULL COMMENT '更新人ID',
    `update_time`        timestamp(0)                                          NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`           tinyint(1)                                            NULL     DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '菜单表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jaguar_cloud_upms_menu
-- ----------------------------
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1419901871475785732, 1423482512532475906, 0, '系统管理', '系统管理', 0, 'Setting', '/upms', 1000, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1419901871475785733, 1423482512532475906, 1419901871475785732, '用户管理', '用户管理', 0, 'User', '/user', 1005, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1419901871475785734, 1423482512532475906, 1419901871475785732, '权限管理', '权限管理', 0, 'UserFilled', '/role', 1010,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1456431998242934786, 1423482512532475906, 1419901871475785732, '部门管理', '部门管理', 0, 'OfficeBuilding', '/dept',
        1015, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1458315020626051073, 1423482512532475906, 0, '开发管理', '开发管理', 0, 'CoffeeCup', '/dev', 2000, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1458315020626051074, 1423482512532475906, 1458315020626051073, '菜单管理', '菜单管理', 0, 'Menu', '/menu', 2005, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1458315020626051075, 1423482512532475906, 1458315020626051073, 'OAuth客户端', 'oauth2客户端管理', 0, 'Iphone',
        '/oauthClient', 2010, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1462695377932759042, 1423482512532475906, 0, '日志管理', '日志管理', 0, 'DocumentCopy', '/log', 1900, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1462695981488910338, 1423482512532475906, 1462695377932759042, '数据修改日志', '数据修改日志', 0, NULL, '/dataModifyLog',
        1905, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1462696137332469761, 1423482512532475906, 1462695377932759042, '接口请求日志', '接口请求日志', 0, NULL, '/handlerLog', 1910,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1466323333678747650, 1423482512532475906, 1458315020626051073, '资源服务', '资源服务管理', 0, 'Platform',
        '/resourceServer', 2001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for jaguar_cloud_upms_oauth_client
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_cloud_upms_oauth_client`;
CREATE TABLE `jaguar_cloud_upms_oauth_client`
(
    `id_`                            bigint(20)                                             NOT NULL COMMENT 'ID',
    `client_id`                      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端ID',
    `client_secret`                  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端密钥',
    `third_party`                    tinyint(1)                                             NOT NULL DEFAULT 0 COMMENT '是否第三方',
    `resource_ids`                   varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '资源ID',
    `authorized_grant_types`         varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '授权类型',
    `registered_redirect_uri`        varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '重定向URI',
    `access_token_validity_seconds`  int(11)                                                NOT NULL COMMENT 'accessToken有效期',
    `refresh_token_validity_seconds` int(11)                                                NOT NULL COMMENT 'refreshToken有效期',
    `scope_`                         varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '权限范围',
    `auto_approve_scopes`            varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '自动授权',
    `client_type`                    varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '客户端类型',
    `user_type`                      varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '用户类型',
    `enable_`                        tinyint(1)                                             NOT NULL COMMENT '是否启用',
    `remark_`                        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '备注',
    `create_by`                      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '创建人',
    `create_user_id`                 bigint(20)                                             NULL     DEFAULT NULL COMMENT '创建人ID',
    `create_time`                    timestamp(0)                                           NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`                      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '更新人',
    `update_user_id`                 bigint(20)                                             NULL     DEFAULT NULL COMMENT '更新人ID',
    `update_time`                    timestamp(0)                                           NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`                       tinyint(1)                                             NULL     DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = 'oauth2客户端'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jaguar_cloud_upms_oauth_client
-- ----------------------------
INSERT INTO `jaguar_cloud_upms_oauth_client`
VALUES (1423482512532475910, 'jaguar-upms-pc', '$2a$10$94CLjZ98IRNWzEkJubfIk.rr3DS7YJnqpCiHUSNDGmx2q.xcQsBcG', 0,
        'jaguar-upms-server', 'implicit', 'http://localhost:8889,http://upms.wilsonlv.top', 7200, 604800, '平台管理员',
        '平台管理员', 'PC', 'ADMIN', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for jaguar_cloud_upms_resource_server
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_cloud_upms_resource_server`;
CREATE TABLE `jaguar_cloud_upms_resource_server`
(
    `id_`            bigint(20)                                             NOT NULL COMMENT 'ID',
    `server_id`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '服务ID',
    `server_name`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '服务名称',
    `server_secret`  varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '服务密钥',
    `server_menu`    tinyint(1)                                             NOT NULL DEFAULT 0 COMMENT '是否展示菜单',
    `server_url`     varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '服务网址',
    `server_enable`  tinyint(1)                                             NOT NULL DEFAULT 1 COMMENT '是否启用',
    `remark_`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '创建人',
    `create_user_id` bigint(20)                                             NULL     DEFAULT NULL COMMENT '创建人ID',
    `create_time`    timestamp(0)                                           NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '更新人',
    `update_user_id` bigint(20)                                             NULL     DEFAULT NULL COMMENT '更新人ID',
    `update_time`    timestamp(0)                                           NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`       tinyint(1)                                             NULL     DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '资源服务器'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jaguar_cloud_upms_resource_server
-- ----------------------------
INSERT INTO `jaguar_cloud_upms_resource_server`
VALUES (1423482512532475905, 'jaguar-auth-server', '认证服务器',
        '$2a$10$WOBNoOM./AGJ26M3RKHwn.0B1VvSopp6UIGiG0Zns0SkOceimKSyO', 0, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 0);
INSERT INTO `jaguar_cloud_upms_resource_server`
VALUES (1423482512532475906, 'jaguar-upms-server', '用户权限管理系统',
        '$2a$10$gmE9X0d0F7cI2y3tMq8g5uH86mhYCBr3Wcbj4huVm9U0FqSoGFimS', 1, null, 1, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 0);
INSERT INTO `jaguar_cloud_upms_resource_server`
VALUES (1423482512532475907, 'jaguar-websocket-server', 'websocket服务',
        '$2a$10$ppVKv2WIrhqLdLeN4tLIJeUBmNXsRd3vOqiENXSj92kDjIddRSnTa', 0, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 0);
INSERT INTO `jaguar_cloud_upms_resource_server`
VALUES (1423482512532475908, 'jaguar-handler-log-server', '接口请求日志服务',
        '$2a$10$s6l9eDccvLajyhUvbpdHHOipIh3nCGinyBkDedc4.IXkT8h/lyVXW', 0, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 0);

-- ----------------------------
-- Table structure for jaguar_cloud_upms_role
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_cloud_upms_role`;
CREATE TABLE `jaguar_cloud_upms_role`
(
    `id_`            bigint(20)                                            NOT NULL COMMENT 'ID',
    `role_name`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
    `role_enable`    tinyint(1)                                            NULL DEFAULT 1 COMMENT '角色是否启用',
    `role_built_in`  tinyint(1)                                            NULL DEFAULT 0 COMMENT '是否内置角色',
    `remark_`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
    `create_user_id` bigint(20)                                            NULL DEFAULT NULL COMMENT '创建人ID',
    `create_time`    timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
    `update_user_id` bigint(20)                                            NULL DEFAULT NULL COMMENT '更新人ID',
    `update_time`    timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`       tinyint(1)                                            NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jaguar_cloud_upms_role
-- ----------------------------
INSERT INTO `jaguar_cloud_upms_role`
VALUES (1419901871475785730, '超级管理员', 1, 1, NULL, NULL, NULL, NULL, null, null, NULL, 0);

-- ----------------------------
-- Table structure for jaguar_cloud_upms_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_cloud_upms_role_menu`;
CREATE TABLE `jaguar_cloud_upms_role_menu`
(
    `id_`            bigint(20)                                            NOT NULL COMMENT 'ID',
    `role_id`        bigint(20)                                            NOT NULL COMMENT '角色ID',
    `menu_id`        bigint(50)                                            NOT NULL COMMENT '菜单ID',
    `built_in`       tinyint(1)                                            NULL DEFAULT 0 COMMENT '是否内置',
    `remark_`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
    `create_user_id` bigint(20)                                            NULL DEFAULT NULL COMMENT '创建人ID',
    `create_time`    timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
    `update_user_id` bigint(20)                                            NULL DEFAULT NULL COMMENT '更新人ID',
    `update_time`    timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`       tinyint(1)                                            NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '角色菜单表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jaguar_cloud_upms_role_menu
-- ----------------------------
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1419901871475785736, 1419901871475785730, 1419901871475785732, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1419901871475785737, 1419901871475785730, 1419901871475785733, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1419901871475785738, 1419901871475785730, 1419901871475785734, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1456442334765498370, 1419901871475785730, 1456431998242934786, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1458316939197169666, 1419901871475785730, 1458315020626051073, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1458316939218141185, 1419901871475785730, 1458315020626051074, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1458316939218141186, 1419901871475785730, 1458315020626051075, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1462700825473839105, 1419901871475785730, 1462695377932759042, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1462700825612251138, 1419901871475785730, 1462696137332469761, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1462700825654194177, 1419901871475785730, 1462695981488910338, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1466564002812801025, 1419901871475785730, 1466323333678747650, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for jaguar_cloud_upms_user
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_cloud_upms_user`;
CREATE TABLE `jaguar_cloud_upms_user`
(
    `id_`                            bigint(20)                                             NOT NULL COMMENT 'ID',
    `user_account`                   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '用户账号（唯一）',
    `user_built_in`                  tinyint(1)                                             NOT NULL DEFAULT 0 COMMENT '是否内置用户',
    `user_phone`                     varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '用户手机号（唯一）',
    `user_email`                     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '用户邮箱（唯一）',
    `user_password`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户密码',
    `user_password_last_modify_time` datetime(0)                                            NULL     DEFAULT NULL COMMENT '密码上次修改时间',
    `user_nick_name`                 varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '用户昵称',
    `user_dept_id`                   bigint(20)                                             NOT NULL DEFAULT 0 COMMENT '用户部门ID',
    `user_enable`                    tinyint(1)                                             NULL     DEFAULT 1 COMMENT '用户是否启用',
    `user_locked`                    tinyint(1)                                             NULL     DEFAULT 0 COMMENT '用户是否锁定',
    `remark_`                        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '备注',
    `create_by`                      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '创建人',
    `create_user_id`                 bigint(20)                                             NULL     DEFAULT NULL COMMENT '创建人ID',
    `create_time`                    timestamp(0)                                           NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`                      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '更新人',
    `update_user_id`                 bigint(20)                                             NULL     DEFAULT NULL COMMENT '更新人ID',
    `update_time`                    timestamp(0)                                           NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`                       tinyint(1)                                             NULL     DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE,
    UNIQUE INDEX `user_account` (`user_account`) USING BTREE,
    UNIQUE INDEX `user_phone` (`user_phone`) USING BTREE,
    UNIQUE INDEX `user_email` (`user_email`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '用户表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jaguar_cloud_upms_user
-- ----------------------------
INSERT INTO `jaguar_cloud_upms_user`
VALUES (1419901871475785729, 'admin', 1, null, null, '$2a$10$DKVtiWyzhO5pWWJ.BhZ.aekIa2n9rwMIiwQFTPxOlzJzPMZVPCZh2',
        '2021-11-22 14:57:04', '管理员', 0, 1, 0, NULL, NULL, NULL, NULL, null, null, null, 0);

-- ----------------------------
-- Table structure for jaguar_cloud_upms_user_role
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_cloud_upms_user_role`;
CREATE TABLE `jaguar_cloud_upms_user_role`
(
    `id_`            bigint(20)                                            NOT NULL COMMENT 'ID',
    `user_id`        bigint(20)                                            NOT NULL COMMENT '用户ID',
    `role_id`        bigint(20)                                            NOT NULL COMMENT '角色ID',
    `built_in`       tinyint(1)                                            NULL DEFAULT 0 COMMENT '是否内置',
    `remark_`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
    `create_user_id` bigint(20)                                            NULL DEFAULT NULL COMMENT '创建人ID',
    `create_time`    timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新人',
    `update_user_id` bigint(20)                                            NULL DEFAULT NULL COMMENT '更新人ID',
    `update_time`    timestamp(0)                                          NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`       tinyint(1)                                            NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '用户角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jaguar_cloud_upms_user_role
-- ----------------------------
INSERT INTO `jaguar_cloud_upms_user_role`
VALUES (1419901871475785731, 1419901871475785729, 1419901871475785730, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for jaguar_support_data_modify_log
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_support_data_modify_log`;
CREATE TABLE `jaguar_support_data_modify_log`
(
    `id_`                   bigint(20)                                             NOT NULL COMMENT 'ID',
    `class_name`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '类全名',
    `record_id`             bigint(20)                                             NOT NULL COMMENT '记录ID',
    `field_name`            varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '字段名称',
    `old_value`             text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         NULL COMMENT '更新前的值',
    `new_value`             text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         NOT NULL COMMENT '更新后的值',
    `last_modify_time`      datetime(0)                                            NULL DEFAULT NULL COMMENT '上次更新时间',
    `last_modify_user_id`   bigint(20)                                             NULL DEFAULT NULL COMMENT '上次更新人ID',
    `last_modify_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '上次更新人用户名',
    `modify_time`           datetime(0)                                            NOT NULL COMMENT '本次更新人',
    `modify_user_id`        bigint(20)                                             NULL DEFAULT NULL COMMENT '本次更新人ID',
    `modify_user_name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '本次更新人用户名',
    PRIMARY KEY (`id_`) USING BTREE,
    INDEX `class_name_index` (`class_name`) USING BTREE,
    INDEX `record_id_index` (`record_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '数据修改日志'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jaguar_support_data_modify_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
