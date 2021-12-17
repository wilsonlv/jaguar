CREATE DATABASE IF NOT EXISTS `jaguar_upms` default character set utf8mb4 collate utf8mb4_unicode_ci;
use `jaguar_upms`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for jaguar_cloud_upms_dept
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_cloud_upms_dept`;
CREATE TABLE `jaguar_cloud_upms_dept`
(
    `id_`            bigint(20)   NOT NULL COMMENT 'ID',
    `parent_id`      bigint(20)   NULL DEFAULT NULL COMMENT '父ID',
    `dept_name`      varchar(20)  NOT NULL COMMENT '部门名称',
    `remark_`        varchar(50)  NULL DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(50)  NULL DEFAULT NULL COMMENT '创建人',
    `create_user_id` bigint(20)   NULL DEFAULT NULL COMMENT '创建人ID',
    `create_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`      varchar(50)  NULL DEFAULT NULL COMMENT '更新人',
    `update_user_id` bigint(20)   NULL DEFAULT NULL COMMENT '更新人ID',
    `update_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`       tinyint(1)   NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '部门'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jaguar_cloud_upms_dept
-- ----------------------------
INSERT INTO `jaguar_cloud_upms_dept`
VALUES (1460134989638668289, 0, '总公司', NULL, NULL, NULL, '2021-11-15 14:38:04', NULL, NULL, '2021-11-22 15:03:01', 0);
INSERT INTO `jaguar_cloud_upms_dept`
VALUES (1460135982304595970, 1460134989638668289, '研发中心', NULL, NULL, NULL, '2021-11-15 14:42:01', NULL, NULL,
        '2021-11-22 15:03:01', 0);
INSERT INTO `jaguar_cloud_upms_dept`
VALUES (1460136096729403394, 1460135982304595970, '技术1部', NULL, NULL, NULL, '2021-11-15 14:42:28', NULL, NULL,
        '2021-11-22 15:03:01', 0);
INSERT INTO `jaguar_cloud_upms_dept`
VALUES (1460136520400244738, 1460135982304595970, '技术2部', NULL, NULL, NULL, '2021-11-15 14:44:09', NULL, NULL,
        '2021-11-22 15:03:01', 0);

-- ----------------------------
-- Table structure for jaguar_cloud_upms_menu
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_cloud_upms_menu`;
CREATE TABLE `jaguar_cloud_upms_menu`
(
    `id_`             bigint(20)   NOT NULL COMMENT 'ID',
    `parent_id`       bigint(20)   NOT NULL DEFAULT 0 COMMENT '父ID',
    `menu_name`       varchar(50)  NOT NULL COMMENT '名称',
    `menu_built_in`   tinyint(1)   NOT NULL DEFAULT 0 COMMENT '是否内置菜单',
    `menu_permission` varchar(50)  NOT NULL COMMENT '权限',
    `menu_button`     tinyint(1)   NOT NULL DEFAULT 0 COMMENT '是否为按钮',
    `menu_icon`       varchar(50)  NULL     DEFAULT NULL COMMENT '图标',
    `menu_page`       varchar(50)  NULL     DEFAULT NULL COMMENT '展示页面',
    `menu_order`      int(11)      NULL     DEFAULT NULL COMMENT '排序',
    `remark_`         varchar(50)  NULL     DEFAULT NULL COMMENT '备注',
    `create_by`       varchar(50)  NULL     DEFAULT NULL COMMENT '创建人',
    `create_user_id`  bigint(20)   NULL     DEFAULT NULL COMMENT '创建人ID',
    `create_time`     timestamp(0) NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`       varchar(50)  NULL     DEFAULT NULL COMMENT '更新人',
    `update_user_id`  bigint(20)   NULL     DEFAULT NULL COMMENT '更新人ID',
    `update_time`     timestamp(0) NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`        tinyint(1)   NULL     DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '菜单表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jaguar_cloud_upms_menu
-- ----------------------------
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1419901871475785732, 0, '系统管理', 1, '系统管理', 0, 'Setting', '/upms', 1000, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1419901871475785733, 1419901871475785732, '用户管理', 1, '用户管理', 0, 'User', '/user', 1005, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1419901871475785734, 1419901871475785732, '权限管理', 1, '权限管理', 0, 'UserFilled', '/role', 1010, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1456431998242934786, 1419901871475785732, '部门管理', 1, '部门管理', 0, 'OfficeBuilding', '/dept', 1015, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1458315020626051073, 0, '开发管理', 1, '开发管理', 0, 'Platform', '/dev', 2000, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1458315020626051074, 1458315020626051073, '菜单管理', 1, '菜单管理', 0, 'Menu', '/menu', 2005, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1458315020626051075, 1458315020626051073, 'OAuth客户端', 1, 'oauth2客户端管理', 0, 'Monitor', '/oauthClient', 2010,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1462695377932759042, 0, '日志管理', 1, '日志管理', 0, 'DocumentCopy', '/log', 1900, NULL, 'admin', 1419901871475785729,
        '2021-11-22 16:12:08', 'admin', 1419901871475785729, '2021-11-22 16:12:08', 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1462695981488910338, 1462695377932759042, '数据修改日志', 1, '数据修改日志', 0, NULL, '/dataModifyLog', 1905, NULL, 'admin',
        1419901871475785729, '2021-11-22 16:14:32', 'admin', 1419901871475785729, '2021-11-22 16:14:32', 0);
INSERT INTO `jaguar_cloud_upms_menu`
VALUES (1462696137332469761, 1462695377932759042, '接口请求日志', 1, '接口请求日志', 0, NULL, '/handlerLog', 1910, NULL, 'admin',
        1419901871475785729, '2021-11-22 16:15:09', 'admin', 1419901871475785729, '2021-11-22 16:15:09', 0);

-- ----------------------------
-- Table structure for jaguar_cloud_upms_resource_server
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_cloud_upms_resource_server`;
CREATE TABLE `jaguar_cloud_upms_resource_server`
(
    `id_`            bigint(20)   NOT NULL COMMENT 'ID',
    `server_id`      varchar(50)  NOT NULL COMMENT '服务ID',
    `server_name`    varchar(50)  NOT NULL COMMENT '服务名称',
    `server_secret`  varchar(50)  NOT NULL COMMENT '服务密钥',
    `remark_`        varchar(50)  NULL DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(50)  NULL DEFAULT NULL COMMENT '创建人',
    `create_user_id` bigint(20)   NULL DEFAULT NULL COMMENT '创建人ID',
    `create_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`      varchar(50)  NULL DEFAULT NULL COMMENT '更新人',
    `update_user_id` bigint(20)   NULL DEFAULT NULL COMMENT '更新人ID',
    `update_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`       tinyint(1)   NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '资源服务'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jaguar_cloud_upms_oauth_client
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_cloud_upms_oauth_client`;
CREATE TABLE `jaguar_cloud_upms_oauth_client`
(
    `id_`                            bigint(20)    NOT NULL COMMENT 'ID',
    `client_id`                      varchar(256)  NOT NULL COMMENT '客户端ID',
    `client_secret`                  varchar(256)  NULL     DEFAULT NULL COMMENT '客户端密钥',
    `resource_ids`                   varchar(256)  NULL     DEFAULT NULL COMMENT '资源ID',
    `authorized_grant_types`         varchar(256)  NULL     DEFAULT NULL COMMENT '授权类型',
    `access_token_validity_seconds`  int(11)       NULL     DEFAULT NULL COMMENT 'accessToken有效期',
    `refresh_token_validity_seconds` int(11)       NULL     DEFAULT NULL COMMENT 'refreshToken有效期',
    `scope_`                         varchar(256)  NULL     DEFAULT NULL COMMENT '权限范围',
    `auto_approve_scopes`            varchar(256)  NULL     DEFAULT NULL COMMENT '自动授权',
    `built_in`                       tinyint(1)    NOT NULL DEFAULT 0 COMMENT '是否内置',
    `enable_`                        tinyint(1)    NOT NULL COMMENT '是否启用',
    `client_type`                    varchar(45)   NOT NULL COMMENT '客户端类型',
    `user_type`                      varchar(45)   NULL     DEFAULT NULL COMMENT '用户类型',
    `captcha_`                       tinyint(1)    NULL     DEFAULT 0 COMMENT '是否需要验证码',
    `remark_`                        varchar(50)   NULL     DEFAULT NULL COMMENT '备注',
    `create_by`                      varchar(50)   NULL     DEFAULT NULL COMMENT '创建人',
    `create_user_id`                 bigint(20)    NULL     DEFAULT NULL COMMENT '创建人ID',
    `create_time`                    timestamp(0)  NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`                      varchar(50)   NULL     DEFAULT NULL COMMENT '更新人',
    `update_user_id`                 bigint(20)    NULL     DEFAULT NULL COMMENT '更新人ID',
    `update_time`                    timestamp(0)  NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`                       tinyint(1)    NULL     DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = 'oauth2客户端'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jaguar_cloud_upms_oauth_client
-- ----------------------------
INSERT INTO `jaguar_cloud_upms_oauth_client`
VALUES (1423482512532475905, 'jaguar-auth', '$2a$10$WOBNoOM./AGJ26M3RKHwn.0B1VvSopp6UIGiG0Zns0SkOceimKSyO', NULL, NULL,
        NULL, NULL, 'feign', NULL, NULL, NULL, NULL, 1, 1, 'SERVER', NULL, 0, NULL, NULL, NULL, '2021-08-06 11:44:54',
        NULL, NULL, '2021-11-16 16:00:48', 0);
INSERT INTO `jaguar_cloud_upms_oauth_client`
VALUES (1423482512532475906, 'jaguar-upms', '$2a$10$gmE9X0d0F7cI2y3tMq8g5uH86mhYCBr3Wcbj4huVm9U0FqSoGFimS',
        'jaguar-upms-server', NULL, NULL, NULL, 'feign', NULL, NULL, NULL, NULL, 1, 1, 'SERVER', NULL, 0, NULL, NULL,
        NULL, '2021-08-06 11:17:35', NULL, NULL, '2021-08-06 11:17:35', 0);
INSERT INTO `jaguar_cloud_upms_oauth_client`
VALUES (1423482512532475907, 'jaguar-websocket', '$2a$10$ssSInpunW4K5NllSYT7oA.zQ0ny9ijtkPcsKJJbvD5/vj9GvitPCi',
        'jaguar-websocket-server', NULL, NULL, NULL, 'feign', NULL, NULL, NULL, NULL, 1, 1, 'SERVER', NULL, 0, NULL,
        NULL, NULL, '2021-08-06 11:19:03', NULL, NULL, '2021-08-06 11:19:03', 0);
INSERT INTO `jaguar_cloud_upms_oauth_client`
VALUES (1423482512532475908, 'jaguar-handler-log', '$2a$10$s6l9eDccvLajyhUvbpdHHOipIh3nCGinyBkDedc4.IXkT8h/lyVXW',
        'jaguar-handler-log-server', NULL, NULL, NULL, 'feign', NULL, NULL, NULL, NULL, 1, 1, 'SERVER', NULL, 0, NULL,
        NULL, NULL, '2021-08-06 14:02:55', NULL, NULL, '2021-08-06 14:02:55', 0);
INSERT INTO `jaguar_cloud_upms_oauth_client`
VALUES (1423482512532475910, 'jaguar-admin-pc', '$2a$10$94CLjZ98IRNWzEkJubfIk.rr3DS7YJnqpCiHUSNDGmx2q.xcQsBcG', NULL,
        '平台管理员', 'password,refresh_token', NULL, NULL, 7200, 604800, NULL, NULL, 1, 1, 'PC', 'ADMIN', 1, NULL, NULL,
        NULL, '2021-08-06 11:23:50', NULL, NULL, '2021-08-06 11:23:50', 0);
INSERT INTO `jaguar_cloud_upms_oauth_client`
VALUES (1423482512532475911, 'jaguar-admin-pc-test', '$2a$10$zO1vQenRQZwrCKSBldCrgOXRIzIM0Myyymkx92k3Pc0nOq9aawRIu',
        NULL, '平台管理员', 'password,refresh_token', NULL, NULL, 7200, 604800, NULL, NULL, 0, 1, 'PC', 'ADMIN', 1, NULL,
        NULL, NULL, '2021-08-06 11:23:50', NULL, NULL, '2021-11-19 09:38:17', 0);
INSERT INTO `jaguar_cloud_upms_oauth_client`
VALUES (1423482512532475912, 'thirdParty', '$2a$10$x.DmCRCV.hljeFjQUAIXJOnjm9xan4EgoPPTNZAczQYEWOzo53vIS', NULL, '个人信息',
        'authorization_code,refresh_token', 'http://localhost:9999', NULL, 7200, 604800, NULL, NULL, 0, 1, 'PC', 'USER',
        1, NULL, NULL, NULL, '2021-08-06 11:23:50', NULL, NULL, '2021-08-06 11:23:50', 0);

-- ----------------------------
-- Table structure for jaguar_cloud_upms_role
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_cloud_upms_role`;
CREATE TABLE `jaguar_cloud_upms_role`
(
    `id_`            bigint(20)   NOT NULL COMMENT 'ID',
    `role_name`      varchar(20)  NOT NULL COMMENT '角色名称',
    `role_enable`    tinyint(1)   NULL DEFAULT 1 COMMENT '角色是否启用',
    `role_built_in`  tinyint(1)   NULL DEFAULT 0 COMMENT '是否内置角色',
    `remark_`        varchar(50)  NULL DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(50)  NULL DEFAULT NULL COMMENT '创建人',
    `create_user_id` bigint(20)   NULL DEFAULT NULL COMMENT '创建人ID',
    `create_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`      varchar(50)  NULL DEFAULT NULL COMMENT '更新人',
    `update_user_id` bigint(20)   NULL DEFAULT NULL COMMENT '更新人ID',
    `update_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`       tinyint(1)   NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jaguar_cloud_upms_role
-- ----------------------------
INSERT INTO `jaguar_cloud_upms_role`
VALUES (1419901871475785730, '超级管理员', 1, 1, NULL, NULL, NULL, NULL, 'admin', 1419901871475785729, '2021-11-22 16:33:47',
        0);
INSERT INTO `jaguar_cloud_upms_role`
VALUES (1460429695023136769, '管理员', 1, 0, NULL, NULL, NULL, '2021-11-16 10:09:07', NULL, NULL, '2021-11-16 10:09:07',
        0);

-- ----------------------------
-- Table structure for jaguar_cloud_upms_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_cloud_upms_role_menu`;
CREATE TABLE `jaguar_cloud_upms_role_menu`
(
    `id_`            bigint(20)   NOT NULL COMMENT 'ID',
    `role_id`        bigint(20)   NOT NULL COMMENT '角色ID',
    `menu_id`        bigint(50)   NOT NULL COMMENT '菜单ID',
    `built_in`       tinyint(1)   NULL DEFAULT 0 COMMENT '是否内置',
    `remark_`        varchar(50)  NULL DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(50)  NULL DEFAULT NULL COMMENT '创建人',
    `create_user_id` bigint(20)   NULL DEFAULT NULL COMMENT '创建人ID',
    `create_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`      varchar(50)  NULL DEFAULT NULL COMMENT '更新人',
    `update_user_id` bigint(20)   NULL DEFAULT NULL COMMENT '更新人ID',
    `update_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`       tinyint(1)   NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
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
VALUES (1419901871475785739, 1419901871475785730, 1419901871475785735, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1419901871475785740, 1419901871475785730, 1419901871475785736, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1456442334765498370, 1419901871475785730, 1456431998242934786, 1, NULL, NULL, NULL, '2021-11-05 10:04:47', NULL,
        NULL, '2021-11-05 10:04:47', 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1458316939197169666, 1419901871475785730, 1458315020626051073, 1, NULL, NULL, NULL, '2021-11-10 14:13:47', NULL,
        NULL, '2021-11-10 14:13:47', 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1458316939218141185, 1419901871475785730, 1458315020626051074, 1, NULL, NULL, NULL, '2021-11-10 14:13:47', NULL,
        NULL, '2021-11-10 14:13:47', 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1458316939218141186, 1419901871475785730, 1458315020626051075, 1, NULL, NULL, NULL, '2021-11-10 14:13:47', NULL,
        NULL, '2021-11-10 14:13:47', 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1460429695140577281, 1460429695023136769, 1419901871475785732, 0, NULL, NULL, NULL, '2021-11-16 10:09:07', NULL,
        NULL, '2021-11-16 10:09:07', 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1460429695178326018, 1460429695023136769, 1419901871475785733, 0, NULL, NULL, NULL, '2021-11-16 10:09:07', NULL,
        NULL, '2021-11-16 10:09:07', 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1460429695178326019, 1460429695023136769, 1419901871475785734, 0, NULL, NULL, NULL, '2021-11-16 10:09:07', NULL,
        NULL, '2021-11-16 10:09:07', 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1460429695253823490, 1460429695023136769, 1456431998242934786, 0, NULL, NULL, NULL, '2021-11-16 10:09:07', NULL,
        NULL, '2021-11-16 10:09:07', 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1462700825473839105, 1419901871475785730, 1462695377932759042, 1, NULL, NULL, NULL, '2021-11-22 16:33:47', NULL,
        NULL, '2021-11-22 16:33:47', 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1462700825612251138, 1419901871475785730, 1462696137332469761, 1, NULL, NULL, NULL, '2021-11-22 16:33:47', NULL,
        NULL, '2021-11-22 16:33:47', 0);
INSERT INTO `jaguar_cloud_upms_role_menu`
VALUES (1462700825654194177, 1419901871475785730, 1462695981488910338, 1, NULL, NULL, NULL, '2021-11-22 16:33:47', NULL,
        NULL, '2021-11-22 16:33:47', 0);

-- ----------------------------
-- Table structure for jaguar_cloud_upms_user
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_cloud_upms_user`;
CREATE TABLE `jaguar_cloud_upms_user`
(
    `id_`                            bigint(20)   NOT NULL COMMENT 'ID',
    `user_account`                   varchar(50)  NOT NULL COMMENT '用户账号（唯一）',
    `user_built_in`                  tinyint(1)   NOT NULL DEFAULT 0 COMMENT '是否内置用户',
    `user_phone`                     varchar(11)  NULL     DEFAULT NULL COMMENT '用户手机号（唯一）',
    `user_email`                     varchar(50)  NULL     DEFAULT NULL COMMENT '用户邮箱（唯一）',
    `user_password`                  varchar(100) NOT NULL COMMENT '用户密码',
    `user_password_last_modify_time` datetime(0)  NULL     DEFAULT NULL COMMENT '密码上次修改时间',
    `user_nick_name`                 varchar(20)  NULL     DEFAULT NULL COMMENT '用户昵称',
    `user_dept_id`                   bigint(20)   NOT NULL DEFAULT 0 COMMENT '用户部门ID',
    `user_enable`                    tinyint(1)   NULL     DEFAULT 1 COMMENT '用户是否启用',
    `user_locked`                    tinyint(1)   NULL     DEFAULT 0 COMMENT '用户是否锁定',
    `remark_`                        varchar(50)  NULL     DEFAULT NULL COMMENT '备注',
    `create_by`                      varchar(50)  NULL     DEFAULT NULL COMMENT '创建人',
    `create_user_id`                 bigint(20)   NULL     DEFAULT NULL COMMENT '创建人ID',
    `create_time`                    timestamp(0) NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`                      varchar(50)  NULL     DEFAULT NULL COMMENT '更新人',
    `update_user_id`                 bigint(20)   NULL     DEFAULT NULL COMMENT '更新人ID',
    `update_time`                    timestamp(0) NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`                       tinyint(1)   NULL     DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
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
VALUES (1419901871475785729, 'admin', 1, '17788210611', '515444876@qq.com',
        '$2a$10$DKVtiWyzhO5pWWJ.BhZ.aekIa2n9rwMIiwQFTPxOlzJzPMZVPCZh2', '2021-11-22 14:57:04', '管理员', 0, 1, 0, NULL,
        NULL, NULL, NULL, 'admin', 1419901871475785729, '2021-11-22 15:03:01', 0);
INSERT INTO `jaguar_cloud_upms_user`
VALUES (1456095453350789121, 'test', 0, NULL, NULL, '$2a$10$ObvfAsm5KhhMHDHBO9tHm.1P4zuFs0JYx8WBag7xZdHHGfNs7xMo6',
        NULL, 'test', 0, 1, 0, NULL, NULL, NULL, '2021-11-04 11:06:24', NULL, NULL, '2021-11-04 11:06:24', 1);
INSERT INTO `jaguar_cloud_upms_user`
VALUES (1460428904841428993, 'zhangsan', 0, '18888883333', 'wilson.lv@icloud.com',
        '$2a$10$eNS0Hsbw8vl4hpzsmXxhCObZR3pnV4Qmn2zU.F6bEeZyTqe5z6DXG', '2021-11-22 15:00:14', '张三', 0, 1, 0, NULL,
        NULL, NULL, '2021-11-16 10:05:59', 'admin', 1419901871475785729, '2021-11-22 15:03:01', 0);

-- ----------------------------
-- Table structure for jaguar_cloud_upms_user_role
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_cloud_upms_user_role`;
CREATE TABLE `jaguar_cloud_upms_user_role`
(
    `id_`            bigint(20)   NOT NULL COMMENT 'ID',
    `user_id`        bigint(20)   NOT NULL COMMENT '用户ID',
    `role_id`        bigint(20)   NOT NULL COMMENT '角色ID',
    `built_in`       tinyint(1)   NULL DEFAULT 0 COMMENT '是否内置',
    `remark_`        varchar(50)  NULL DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(50)  NULL DEFAULT NULL COMMENT '创建人',
    `create_user_id` bigint(20)   NULL DEFAULT NULL COMMENT '创建人ID',
    `create_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_by`      varchar(50)  NULL DEFAULT NULL COMMENT '更新人',
    `update_user_id` bigint(20)   NULL DEFAULT NULL COMMENT '更新人ID',
    `update_time`    timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `deleted_`       tinyint(1)   NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
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
INSERT INTO `jaguar_cloud_upms_user_role`
VALUES (1456095453644390401, 1456095453350789121, 1419901871475785730, 0, NULL, NULL, NULL, '2021-11-04 11:06:24', NULL,
        NULL, '2021-11-04 11:06:24', 0);
INSERT INTO `jaguar_cloud_upms_user_role`
VALUES (1460428904950480897, 1460428904841428993, 1419901871475785730, 0, NULL, NULL, NULL, '2021-11-16 10:05:59', NULL,
        NULL, '2021-11-16 10:09:39', 1);
INSERT INTO `jaguar_cloud_upms_user_role`
VALUES (1460429828611719169, 1460428904841428993, 1460429695023136769, 0, NULL, NULL, NULL, '2021-11-16 10:09:39', NULL,
        NULL, '2021-11-16 10:09:39', 0);

-- ----------------------------
-- Table structure for jaguar_support_data_modify_log
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_support_data_modify_log`;
CREATE TABLE `jaguar_support_data_modify_log`
(
    `id_`                   bigint(20)   NOT NULL COMMENT 'ID',
    `class_name`            varchar(100) NOT NULL COMMENT '类全名',
    `record_id`             bigint(20)   NOT NULL COMMENT '记录ID',
    `field_name`            varchar(45)  NOT NULL COMMENT '字段名称',
    `old_value`             text         NULL COMMENT '更新前的值',
    `new_value`             text         NOT NULL COMMENT '更新后的值',
    `last_modify_time`      datetime(0)  NULL DEFAULT NULL COMMENT '上次更新时间',
    `last_modify_user_id`   bigint(20)   NULL DEFAULT NULL COMMENT '上次更新人ID',
    `last_modify_user_name` varchar(50)  NULL DEFAULT NULL COMMENT '上次更新人用户名',
    `modify_time`           datetime(0)  NOT NULL COMMENT '本次更新人',
    `modify_user_id`        bigint(20)   NULL DEFAULT NULL COMMENT '本次更新人ID',
    `modify_user_name`      varchar(50)  NULL DEFAULT NULL COMMENT '本次更新人用户名',
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
INSERT INTO `jaguar_support_data_modify_log`
VALUES (1462676484958814209, 'top.wilsonlv.jaguar.cloud.upms.entity.User', 1419901871475785729, 'userPassword', '',
        '$2a$10$DKVtiWyzhO5pWWJ.BhZ.aekIa2n9rwMIiwQFTPxOlzJzPMZVPCZh2', '2021-11-22 14:33:25', 1419901871475785729,
        'admin', '2021-11-22 14:57:04', 1419901871475785729, 'admin');
INSERT INTO `jaguar_support_data_modify_log`
VALUES (1462676485088837634, 'top.wilsonlv.jaguar.cloud.upms.entity.User', 1419901871475785729,
        'userPasswordLastModifyTime', '2021-11-15T10:51', '2021-11-22T14:57:03.804', '2021-11-22 14:33:25',
        1419901871475785729, 'admin', '2021-11-22 14:57:04', 1419901871475785729, 'admin');
INSERT INTO `jaguar_support_data_modify_log`
VALUES (1462676485097226241, 'top.wilsonlv.jaguar.cloud.upms.entity.User', 1419901871475785729, 'userDeptId', '0',
        '1460134989638668289', '2021-11-22 14:33:25', 1419901871475785729, 'admin', '2021-11-22 14:57:04',
        1419901871475785729, 'admin');
INSERT INTO `jaguar_support_data_modify_log`
VALUES (1462677284233773058, 'top.wilsonlv.jaguar.cloud.upms.entity.User', 1460428904841428993, 'userPassword', '',
        '$2a$10$eNS0Hsbw8vl4hpzsmXxhCObZR3pnV4Qmn2zU.F6bEeZyTqe5z6DXG', '2021-11-22 14:33:25', 1419901871475785729,
        'admin', '2021-11-22 15:00:14', 1419901871475785729, 'admin');
INSERT INTO `jaguar_support_data_modify_log`
VALUES (1462677284233773059, 'top.wilsonlv.jaguar.cloud.upms.entity.User', 1460428904841428993,
        'userPasswordLastModifyTime', '2021-11-16T10:31:09', '2021-11-22T15:00:14.435', '2021-11-22 14:33:25',
        1419901871475785729, 'admin', '2021-11-22 15:00:14', 1419901871475785729, 'admin');
INSERT INTO `jaguar_support_data_modify_log`
VALUES (1462677284254744578, 'top.wilsonlv.jaguar.cloud.upms.entity.User', 1460428904841428993, 'userDeptId', '0',
        '1460134989638668289', '2021-11-22 14:33:25', 1419901871475785729, 'admin', '2021-11-22 15:00:14',
        1419901871475785729, 'admin');
INSERT INTO `jaguar_support_data_modify_log`
VALUES (1462677982954487809, 'top.wilsonlv.jaguar.cloud.upms.entity.User', 1419901871475785729, 'userDeptId', NULL, '0',
        NULL, NULL, NULL, '2021-11-22 15:03:01', 1419901871475785729, 'admin');
INSERT INTO `jaguar_support_data_modify_log`
VALUES (1462677983311003649, 'top.wilsonlv.jaguar.cloud.upms.entity.User', 1460428904841428993, 'userDeptId', NULL, '0',
        NULL, NULL, NULL, '2021-11-22 15:03:01', 1419901871475785729, 'admin');

SET FOREIGN_KEY_CHECKS = 1;
