CREATE DATABASE IF NOT EXISTS `jaguar_nginx` default character set utf8mb4 collate utf8mb4_unicode_ci;;
use `jaguar_nginx`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`
(
    `id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `name`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `pass`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `key`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `auth`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `api`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `token`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `name@admin` (`name`(128)) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin`
VALUES ('609074089344241665', 'admin', 'Aa123456', NULL, '0', '0', NULL, '1625381044580', '1625381044580');

-- ----------------------------
-- Table structure for basic
-- ----------------------------
DROP TABLE IF EXISTS `basic`;
CREATE TABLE `basic`
(
    `id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `name`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `value`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `seq`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of basic
-- ----------------------------
INSERT INTO `basic`
VALUES ('608304715213705216', 'worker_processes', 'auto', '1', '1625212719559', '1625212719559');
INSERT INTO `basic`
VALUES ('608304715213705217', 'pid', '/home/nginx-admin/data//nginx.pid', '1', '1625212719559', '1625213503152');
INSERT INTO `basic`
VALUES ('608304715213705218', 'events', '{\r\n    worker_connections  1024;\r\n    accept_mutex on;\r\n}', '2',
        '1625212719559', '1625212719559');

-- ----------------------------
-- Table structure for cert
-- ----------------------------
DROP TABLE IF EXISTS `cert`;
CREATE TABLE `cert`
(
    `id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `domain`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `pem`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `key`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `type`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `make_time`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `auto_renew`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `dns_type`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `dp_id`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `dp_key`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `ali_key`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `ali_secret`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `cf_email`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `cf_key`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `gd_key`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `gd_secret`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `domain@cert` (`domain`(128)) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for credit
-- ----------------------------
DROP TABLE IF EXISTS `credit`;
CREATE TABLE `credit`
(
    `id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `key`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `key@credit` (`key`(128)) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group`
(
    `id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `parent_id`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `name`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for http
-- ----------------------------
DROP TABLE IF EXISTS `http`;
CREATE TABLE `http`
(
    `id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `name`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `value`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `unit`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `seq`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of http
-- ----------------------------
INSERT INTO `http`
VALUES ('608304715264036864', 'include', 'mime.types', NULL, '0', '1625212719561', '1625212719561');
INSERT INTO `http`
VALUES ('608304715264036865', 'default_type', 'application/octet-stream', NULL, '1', '1625212719561', '1625212719561');
INSERT INTO `http`
VALUES ('608338060073111552', 'keepalive_timeout', '75s', 's', '608338060073111552', '1625212719561', '1625212719561');
INSERT INTO `http`
VALUES ('608338060094083072', 'gzip', 'on', '', '608338060089888768', '1625212719561', '1625212719561');
INSERT INTO `http`
VALUES ('608338060110860288', 'gzip_min_length', '4k', 'k', '608338060110860288', '1625212719561', '1625212719561');
INSERT INTO `http`
VALUES ('608338060131831808', 'gzip_comp_level', '4', '', '608338060131831808', '1625212719561', '1625212719561');
INSERT INTO `http`
VALUES ('608338060148609024', 'client_max_body_size', '1024m', 'm', '608338060148609024', '1625212719561',
        '1625212719561');
INSERT INTO `http`
VALUES ('608338060161191936', 'client_header_buffer_size', '32k', 'k', '608338060161191936', '1625212719561',
        '1625212719561');
INSERT INTO `http`
VALUES ('608338060177969152', 'client_body_buffer_size', '8m', 'm', '608338060177969152', '1625212719561',
        '1625212719561');
INSERT INTO `http`
VALUES ('608338060194746368', 'server_names_hash_bucket_size', '512', '', '608338060194746368', '1625212719561',
        '1625212719561');
INSERT INTO `http`
VALUES ('608338060211523584', 'proxy_headers_hash_max_size', '51200', '', '608338060211523584', '1625212719561',
        '1625212719561');
INSERT INTO `http`
VALUES ('608338060228300800', 'proxy_headers_hash_bucket_size', '6400', '', '608338060228300800', '1625212719561',
        '1625212719561');
INSERT INTO `http`
VALUES ('608338060240883712', 'gzip_types', 'application/javascript application/x-javascript text/css ', '',
        '608338060240883712', '1625212719561', '1625212719561');

-- ----------------------------
-- Table structure for location
-- ----------------------------
DROP TABLE IF EXISTS `location`;
CREATE TABLE `location`
(
    `id`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `server_id`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `path`                text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `type`                text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `location_param_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `value`               text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `upstream_type`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `upstream_id`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `upstream_path`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `root_path`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `root_page`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `root_type`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `header`              text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `websocket`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of location
-- ----------------------------
INSERT INTO `location`
VALUES ('660458501918822400', '608308160150769664', '/handlerLog/', '0', '[]', 'http://localhost:9888/', 'http', '', '',
        '', '', 'root', '1', '0', '1637632043213', '1637632043213');
INSERT INTO `location`
VALUES ('660458501931405312', '608308160150769664', '/websocket/', '0', '[]', 'http://localhost:9000/', 'http', '', '',
        '', '', 'root', '1', '1', '1637632043216', '1637632043216');
INSERT INTO `location`
VALUES ('660458501943988224', '608308160150769664', '/upms/', '0', '[]', 'http://localhost:8888/', 'http', '', '', '',
        '', 'root', '1', '0', '1637632043218', '1637632043218');
INSERT INTO `location`
VALUES ('660458501952376832', '608308160150769664', '/auth/', '0', '[]', 'http://localhost:8868/', 'http', '', '', '',
        '', 'root', '1', '0', '1637632043221', '1637632043221');

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log`
(
    `id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `path`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `path@log` (`path`(128)) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for log_info
-- ----------------------------
DROP TABLE IF EXISTS `log_info`;
CREATE TABLE `log_info`
(
    `id`                     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `remote_addr`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `remote_user`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `time_local`             text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `request`                text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `http_host`              text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `status`                 text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `request_length`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `body_bytes_dent`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `http_referer`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `http_user_agent`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `request_time`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `upstream_response_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `hour`                   text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `minute`                 text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `second`                 text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for param
-- ----------------------------
DROP TABLE IF EXISTS `param`;
CREATE TABLE `param`
(
    `id`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `server_id`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `location_id`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `upstream_id`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `template_id`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `name`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `value`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `template_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `template_name`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of param
-- ----------------------------
INSERT INTO `param`
VALUES ('633310992150958080', NULL, '633310992142569472', NULL, NULL, 'try_files', ' $uri $uri/ /index.html', NULL,
        NULL, '1631159572509', '1631159572509');

-- ----------------------------
-- Table structure for password
-- ----------------------------
DROP TABLE IF EXISTS `password`;
CREATE TABLE `password`
(
    `id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `name`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `pass`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `path`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `descr`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `path_str`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for remote
-- ----------------------------
DROP TABLE IF EXISTS `remote`;
CREATE TABLE `remote`
(
    `id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `protocol`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `ip`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `port`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `status`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `credit_key`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `name`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `pass`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `version`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `system`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `descr`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `monitor`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `parent_id`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `type`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `nginx`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for server
-- ----------------------------
DROP TABLE IF EXISTS `server`;
CREATE TABLE `server`
(
    `id`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `server_name`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `listen`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `def`               text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `rewrite`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `rewrite_listen`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `ssl`               text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `http2`             text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `proxy_protocol`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `pem`               text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `key`               text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `proxy_type`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `proxy_upstream_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `pem_str`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `key_str`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `enable`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `descr`             text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `protocols`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `password_id`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `seq`               text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of server
-- ----------------------------
INSERT INTO `server`
VALUES ('608308160150769664', 'localhost', '80', '0', '1', '80', '0', '1', '0', '', '', '0', '', NULL, NULL, '1', NULL,
        'TLSv1 TLSv1.1 TLSv1.2 TLSv1.3', '', '608308160133992448', '1625212719566', '1637632043207');

-- ----------------------------
-- Table structure for setting
-- ----------------------------
DROP TABLE IF EXISTS `setting`;
CREATE TABLE `setting`
(
    `id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `key`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `value`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `key@setting` (`key`(128)) USING BTREE,
    UNIQUE INDEX `key&value@setting` (`key`(128), `value`(128)) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of setting
-- ----------------------------
INSERT INTO `setting`
VALUES ('608367856127709184', 'nginxPath', 'E:/develop/nginx-1.20.1/conf/nginx.conf', '1625212665460', '1631159305247');
INSERT INTO `setting`
VALUES ('608367901082259456', 'lang', '', '1625212676177', '1637632065109');
INSERT INTO `setting`
VALUES ('608368039334907904', 'nginxDir', 'E:/develop/nginx-1.20.1/', '1625212709140', '1631159305255');
INSERT INTO `setting`
VALUES ('608368039343296512', 'nginxExe', 'E:/develop/nginx-1.20.1/nginx.exe', '1625212709142', '1631159305250');
INSERT INTO `setting`
VALUES ('608368083077304320', 'decompose', 'false', '1625212719569', '1625213468992');
INSERT INTO `setting`
VALUES ('608370251473752064', 'cmdStop', 'D:/develop/nginx-1.20.1/nginx.exe -s stop -p D:/develop/nginx-1.20.1/',
        '1625213236555', '1625213236555');
INSERT INTO `setting`
VALUES ('608370447553269760', 'cmdStart',
        'E:/develop/nginx-1.20.1/nginx.exe -c E:/develop/nginx-1.20.1/conf/nginx.conf -p E:/develop/nginx-1.20.1/',
        '1625213283304', '1637550832221');

-- ----------------------------
-- Table structure for stream
-- ----------------------------
DROP TABLE IF EXISTS `stream`;
CREATE TABLE `stream`
(
    `id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `name`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `value`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `seq`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for template
-- ----------------------------
DROP TABLE IF EXISTS `template`;
CREATE TABLE `template`
(
    `id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `name`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `def`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for upstream
-- ----------------------------
DROP TABLE IF EXISTS `upstream`;
CREATE TABLE `upstream`
(
    `id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `name`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `tactics`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `proxy_type`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `monitor`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `seq`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for upstream_server
-- ----------------------------
DROP TABLE IF EXISTS `upstream_server`;
CREATE TABLE `upstream_server`
(
    `id`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `upstream_id`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `server`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `port`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `weight`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `fail_timeout`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `max_fails`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `status`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `monitor_status` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for www
-- ----------------------------
DROP TABLE IF EXISTS `www`;
CREATE TABLE `www`
(
    `id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `dir`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `create_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    `update_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `dir@www` (`dir`(128)) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;