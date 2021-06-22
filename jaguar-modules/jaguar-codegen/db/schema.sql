drop table if exists `jaguar_modules_codegen_datasource`;
CREATE TABLE `jaguar_modules_codegen_datasource`
(
    `id_`         bigint(20) unsigned NOT NULL COMMENT 'ID',
    `name_`       varchar(50)         NOT NULL COMMENT '数据源名称',
    `host_`       varchar(50)         NOT NULL COMMENT 'host',
    `port_`       varchar(10)         NOT NULL COMMENT 'port',
    `schema_`     varchar(45)         NOT NULL COMMENT 'schema',
    `username_`   varchar(50)         NOT NULL COMMENT '用户名',
    `password_`   varchar(50)         NOT NULL COMMENT '密码',
    `deleted_`    tinyint(1)  DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
    `create_time` timestamp   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp   DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark_`     varchar(50) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT ='数据源';

drop table if exists `jaguar_modules_codegen_code_template`;
CREATE TABLE `jaguar_modules_codegen_code_template`
(
    `id_`                   bigint(20) unsigned NOT NULL COMMENT 'ID',
    `code_template_name`    varchar(50)         NOT NULL COMMENT '模板类型',
    `code_template_file`    text                NOT NULL COMMENT '模板文件',
    `code_template_version` int(11)             NOT NULL COMMENT '模板版本',
    `file_name`             varchar(50)         NOT NULL COMMENT '文件名',
    `file_path`             varchar(100)        NOT NULL COMMENT '文件路径',
    `deleted_`              tinyint(1)  DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
    `create_time`           timestamp   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`           timestamp   DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark_`               varchar(50) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT ='代码模板'