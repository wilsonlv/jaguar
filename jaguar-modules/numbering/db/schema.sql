DROP TABLE IF EXISTS `jaguar_modules_numbering_rule`;
CREATE TABLE `jaguar_modules_numbering_rule`
(
    `id_`          bigint(20)  NOT NULL COMMENT 'ID',
    `name_`        varchar(50) NOT NULL COMMENT '规则名称',
    `description_` varchar(100)         DEFAULT NULL COMMENT '描述',
    `deleted_`     tinyint(1)  NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
    `create_by`    bigint(20) unsigned  DEFAULT NULL COMMENT '创建人',
    `create_time`  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`    bigint(20) unsigned  DEFAULT NULL COMMENT '最新修改人',
    `update_time`  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='编号规则表';

DROP TABLE IF EXISTS `jaguar_modules_numbering_rule_item`;
CREATE TABLE `jaguar_modules_numbering_rule_item`
(
    `id_`                      bigint(20)  NOT NULL COMMENT 'ID',
    `rule_id`                  bigint(20)  NOT NULL COMMENT '编号规则ID',
    `numbering_rule_item_type` varchar(20) NOT NULL COMMENT '类型（固定类型：FIXED，日期类型：DATETIME，流水号：SERIAL_NUMBER，sql查询：SQL_QUERY）',
    `level_`                   tinyint(1)           DEFAULT NULL COMMENT '流水序列等级',
    `name_`                    text        NOT NULL COMMENT '条目名称（固定类型：固定的字符、日期类型：日期格式，流水号：初试值，sql查询：查询的sql语句）',
    `effect_`                  tinyint(1)  NOT NULL DEFAULT '1' COMMENT '是否影响流水号',
    `show_`                    tinyint(1)  NOT NULL DEFAULT '1' COMMENT '是否在编号中显示',
    `length_`                  int(11)              DEFAULT NULL COMMENT '条目长度',
    `sort_no`                  int(11)     NOT NULL COMMENT '排序号',
    `deleted_`                 tinyint(1)  NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
    `create_by`                bigint(20) unsigned  DEFAULT NULL COMMENT '创建人',
    `create_time`              timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`                bigint(20) unsigned  DEFAULT NULL COMMENT '最新修改人',
    `update_time`              timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='编号规则条目表';

DROP TABLE IF EXISTS `jaguar_modules_numbering_rule_serial`;
CREATE TABLE `jaguar_modules_numbering_rule_serial`
(
    `id_`           bigint(20)  NOT NULL COMMENT 'ID',
    `rule_id`       bigint(20)  NOT NULL COMMENT '编号规则ID',
    `rule_item_id`  bigint(20)  NOT NULL COMMENT '编号规则条目ID',
    `parttern_`     varchar(50) NOT NULL COMMENT '格式',
    `serial_number` varchar(10) NOT NULL COMMENT '序列号',
    `deleted_`      tinyint(1)  NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
    `create_by`     bigint(20) unsigned  DEFAULT NULL COMMENT '创建人',
    `create_time`   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`     bigint(20) unsigned  DEFAULT NULL COMMENT '最新修改人',
    `update_time`   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id_`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='编号规则序列表';