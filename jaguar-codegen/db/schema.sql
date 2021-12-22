CREATE DATABASE if NOT EXISTS `jaguar_codegen` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `jaguar_codegen`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for jaguar_codegen_code_template
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_codegen_code_template`;
CREATE TABLE `jaguar_codegen_code_template`
(
    `id_`                   bigint(20) UNSIGNED NOT NULL COMMENT 'ID',
    `code_template_name`    varchar(50)         NOT NULL COMMENT '模板类型',
    `code_template_file`    text                NOT NULL COMMENT '模板文件',
    `code_template_version` int(11)             NOT NULL COMMENT '模板版本',
    `file_name`             varchar(50)         NOT NULL COMMENT '文件名',
    `file_path`             varchar(45)         NOT NULL COMMENT '文件路径',
    `deleted_`              tinyint(1)          NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    `create_time`           timestamp(0)        NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time`           timestamp(0)        NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `remark_`               varchar(50)         NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '代码模板'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jaguar_codegen_code_template
-- ----------------------------
INSERT INTO `jaguar_codegen_code_template`
VALUES (1406568806997471233, 'mapper.xml',
        '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n<mapper namespace=\"${package}.mapper.${entity}Mapper\">\n\n	<!-- 通用查询映射结果 -->\n	<resultMap id=\"baseResultMap\" type=\"${package}.entity.${entity}\">\n		<id property=\"id\" column=\"id_\"/>\n#foreach($column in ${columns})\n		<result column=\"${column.columnName}\" property=\"${column.fieldName}\" />\n#end\n	</resultMap>\n\n</mapper>',
        4, '${entity}Mapper.xml', '${javaPath}/${packagePath}/mapper/xml', 0, '2021-06-19 21:16:21',
        '2021-06-19 21:16:21', NULL);
INSERT INTO `jaguar_codegen_code_template`
VALUES (1457879081621651458, 'edit.vue',
        '<template>\n  <el-dialog\n    v-model=\"dialog.visible\"\n    :title=\"dialogTitle\"\n    @open=\"openHandler\"\n    @close=\"closeHandler\"\n    :close-on-click-modal=\"false\"\n    append-to-body\n    center\n  >\n    <el-form\n      ref=\"dataForm\"\n      :rules=\"formRules\"\n      :model=\"entity\"\n      :disabled=\"operation == 3\"\n      label-suffix=\"：\"\n    >\n#foreach($column in ${columns})\n      <el-row class=\"padding-lr-20\" :gutter=\"20\">\n        <el-col :span=\"24\">\n          <el-form-item prop=\"${column.fieldName}\" label=\"${column.columnComment}\">\n            <el-input\n              v-model=\"entity.${column.fieldName}\"\n              placeholder=\"请输入${column.columnComment}\"\n              clearable\n            />\n          </el-form-item>\n        </el-col>\n      </el-row>\n#end\n    </el-form>\n\n    <template #footer>\n      <cancel-button @click=\"dialog.visible = false\" />\n      <save-button v-if=\"operation != 3\" :loading=\"loading\" @click=\"saveHandler\" />\n    </template>\n  </el-dialog>\n</template>\n<script>\nimport { save, update } from \'@/api/${module}/${entityName}\'\n\nexport default {\n  name: \'${entity}Edit\',\n  props: {\n    dialog: {\n      type: Object,\n      required: true,\n    },\n  },\n  emits: [\'success\'],\n  data() {\n    return {\n      loading: false,\n      entity: {},\n      formRules: {\n        demo: [\n          { required: true, message: \'请输入\', trigger: \'blur\' },\n        ],\n      },\n      titleMap: {\n        1: \'新建${table.tableComment}\',\n        2: \'编辑${table.tableComment}\',\n        3: \'查看${table.tableComment}\',\n      }\n    }\n  },\n  computed: {\n    operation() {\n      return this.dialog.operation\n    },\n    dialogTitle() {\n      return this.titleMap[this.dialog.operation]\n    }\n  },\n  methods: {\n    openHandler() {\n      this.entity = Object.assign({}, this.dialog.entity)\n    },\n    closeHandler() {\n      this.entity = {}\n      this.$refs.dataForm.clearValidate()\n    },\n    saveHandler() {\n      #[[this.$refs.dataForm.validate((valid) => {\n        if (valid) {\n          this.loading = true\n          let edit = this.operation == 1 ? save : update\n          edit(this.entity)\n            .then(() => {\n            this.dialog.visible = false\n            this.$message.success(\'保存成功\')\n            this.$emit(\'success\')\n           })\n           .finally(() => {\n              this.loading = false\n           })\n        }\n      })]]#\n    },\n  },\n}\n</script>',
        8, 'Edit.vue', 'src/views/${module}/${entityName}', 0, '2021-06-21 10:37:29', '2021-06-21 10:37:29', NULL);
INSERT INTO `jaguar_codegen_code_template`
VALUES (1460112097995776001, 'vo',
        'package ${package}.controller.vo;\n\nimport io.swagger.annotations.ApiModel;\nimport io.swagger.annotations.ApiModelProperty;\nimport lombok.Data;\nimport lombok.EqualsAndHashCode;\nimport top.wilsonlv.jaguar.commons.web.base.BaseVO;\n\n#if(${dateTimeScore} == 1 || ${dateTimeScore} == 3)\nimport java.time.LocalDate;\n#end\n#if(${dateTimeScore} == 2 || ${dateTimeScore} == 3)\nimport java.time.LocalDateTime;\n#end\n\n/**\n * <p>\n * ${table.tableComment}\n * </p>\n *\n * @author ${author}\n * @since ${date}\n */\n@Data\n@ApiModel\n@EqualsAndHashCode(callSuper = true)\npublic class ${entity}VO extends BaseVO {\n\n   private static final long serialVersionUID = 1L;\n  \n#foreach($column in ${columns})\n	@ApiModelProperty(\"${column.columnComment}\")\n	private ${column.filedType} ${column.fieldName};\n  \n#end\n}',
        5, '${entity}VO.java', '${javaPath}/${packagePath}/controller/vo', 0, '2021-11-08 14:19:08',
        '2021-11-08 14:19:08', NULL);
INSERT INTO `jaguar_codegen_code_template`
VALUES (1463781062462783490, 'createDTO',
        'package ${package}.controller.dto;\n\nimport io.swagger.annotations.ApiModel;\nimport io.swagger.annotations.ApiModelProperty;\nimport lombok.Data;\nimport lombok.EqualsAndHashCode;\nimport top.wilsonlv.jaguar.commons.web.base.BaseDTO;\n\n#if(${dateTimeScore} == 1 || ${dateTimeScore} == 3)\nimport java.time.LocalDate;\n#end\n#if(${dateTimeScore} == 2 || ${dateTimeScore} == 3)\nimport java.time.LocalDateTime;\n#end\n\n/**\n * <p>\n * ${table.tableComment}\n * </p>\n *\n * @author ${author}\n * @since ${date}\n */\n@Data\n@ApiModel\n@EqualsAndHashCode(callSuper = true)\npublic class ${entity}CreateDTO extends BaseDTO {\n\n   private static final long serialVersionUID = 1L;\n  \n#foreach($column in ${columns})\n	@ApiModelProperty(\"${column.columnComment}\")\n	private ${column.filedType} ${column.fieldName};\n  \n#end\n}',
        3, '${entity}CreateDTO.java', '${javaPath}/${packagePath}/controller/dto', 0, '2021-11-08 14:22:05',
        '2021-11-08 14:19:07', NULL);
INSERT INTO `jaguar_codegen_code_template`
VALUES (1463781119048138754, 'modifyDTO',
        'package ${package}.controller.dto;\n\nimport io.swagger.annotations.ApiModel;\nimport io.swagger.annotations.ApiModelProperty;\nimport lombok.Data;\nimport lombok.EqualsAndHashCode;\nimport top.wilsonlv.jaguar.commons.web.base.BaseModifyDTO;\n\n#if(${dateTimeScore} == 1 || ${dateTimeScore} == 3)\nimport java.time.LocalDate;\n#end\n#if(${dateTimeScore} == 2 || ${dateTimeScore} == 3)\nimport java.time.LocalDateTime;\n#end\n\n/**\n * <p>\n * ${table.tableComment}\n * </p>\n *\n * @author ${author}\n * @since ${date}\n */\n@Data\n@ApiModel\n@EqualsAndHashCode(callSuper = true)\npublic class ${entity}ModifyDTO extends BaseModifyDTO {\n\n   private static final long serialVersionUID = 1L;\n  \n#foreach($column in ${columns})\n	@ApiModelProperty(\"${column.columnComment}\")\n	private ${column.filedType} ${column.fieldName};\n  \n#end\n}',
        3, '${entity}ModifyDTO.java', '${javaPath}/${packagePath}/controller/dto', 0, '2021-11-08 14:20:35',
        '2021-11-08 14:20:35', NULL);
INSERT INTO `jaguar_codegen_code_template`
VALUES (1463795738458308610, 'api.js',
        'import request from \'@/api/axios\'\n\nconst baseurl = \'/admin/${module}/${entityName}\'\n\nexport function fetchList(query) {\n  for (const key in query) {\n    if (query[key] == null || query[key] === \'\') {\n      delete query[key]\n    }\n  }\n  return request({\n    url: baseurl + \'/page\',\n    method: \'get\',\n    params: query,\n  })\n}\n\nexport function getDetail(id) {\n  return request({\n    url: baseurl + \'/\' + id,\n    method: \'get\'\n  })\n}\n\nexport function save(data) {\n  return request({\n    url: baseurl,\n    method: \'post\',\n    data,\n  })\n}\n\nexport function update(data) {\n  return request({\n    url: baseurl,\n    method: \'put\',\n    data,\n  })\n}\n\nexport function del(id) {\n  return request({\n    url: baseurl + \'/\' + id,\n    method: \'delete\',\n  })\n}\n',
        3, '${entityName}.js', 'src/api/${module}', 0, '2021-06-21 10:17:38', '2021-06-21 10:17:38', NULL);
INSERT INTO `jaguar_codegen_code_template`
VALUES (1463797193244262402, 'table.vue',
        '<template>\n  <div class=\"jaguar-view\">\n    <div class=\"jaguar-view-header\">\n      <div class=\"jaguar-search\">\n        <el-form :inline=\"true\" :model=\"searchForm\" @submit.prevent>\n  #foreach($column in ${columns})\n          <search-input\n            label=\"${column.columnComment}\"\n            v-model=\"searchForm.${column.fieldName}\"\n            :search=\"getData\"\n          />\n  #end\n          <el-form-item>\n            <search-button :search=\"getData\" />\n            <reset-button :search-form=\"searchForm\" :search=\"getData\" />\n          </el-form-item>\n        </el-form>\n      </div>\n\n      <div class=\"jaguar-control\">\n        <add-button @click=\"handleCreate\" />\n      </div>\n    </div>\n\n    <div class=\"jaguar-table\">\n      <el-table :data=\"tableData\" v-loading=\"loading\">\n#foreach($column in ${columns})\n        <el-table-column prop=\"${column.fieldName}\" label=\"${column.columnComment}\" />\n#end\n        <el-table-column label=\"操作\">\n          <template #default=\"scope\">\n            <view-button @click=\"handleView(scope.row)\" />\n            <edit-button @click=\"handleEdit(scope.row)\" />\n            <delete-button :confirm=\"handleDelete\" :row=\"scope.row\" />\n          </template>\n        </el-table-column>\n      </el-table>\n\n      <table-page :page=\"page\" :search=\"getData\" />\n    </div>\n\n    <Edit :dialog=\"dialog\" @success=\"getData()\" />\n  </div>\n</template>\n<script>\nimport { fetchList, del } from \'@/api/${module}/${entityName}\'\nimport Edit from \'./Edit\'\n\nexport default {\n  name: \'${entity}\',\n  data() {\n    return {\n      loading: false,\n      tableData: [],\n      searchForm: {},\n      page: {\n        total: 0,\n        current: 1,\n        size: 10,\n      },\n      dialog: {\n        entity: null,\n        operation: null,\n        visible: false,\n      },\n    }\n  },\n  components: {\n    Edit,\n  },\n  created() {\n    this.getData()\n  },\n  methods: {\n    getData(current, size) {\n      if (current) this.page.current = current\n      if (size) this.page.size = size\n\n      this.loading = true\n      fetchList(\n        Object.assign(\n          {\n            descs: \'id_\',\n            current: this.page.current,\n            size: this.page.size,\n          },\n          this.searchForm\n        )\n      )\n        .then((res) => {\n         this.tableData = res.data.data.records\n         this.page.total = res.data.data.total\n        })\n        .finally(() => {\n         this.loading = false\n        })\n    },\n    handleCreate() {\n      this.dialog.entity = null\n      this.dialog.operation = 1\n      this.dialog.visible = true\n    },\n    handleEdit(row) {\n      this.dialog.entity = row\n      this.dialog.operation = 2\n      this.dialog.visible = true\n    },\n    handleView(row) {\n      this.dialog.entity = row\n      this.dialog.operation = 3\n      this.dialog.visible = true\n    },\n   async handleDelete(row) {\n      await del(row.id)\n      this.$message.success(\'删除成功\')\n      this.getData()\n    },\n  },\n}\n</script>',
        8, 'index.vue', 'src/views/${module}/${entityName}', 0, '2021-06-21 10:10:43', '2021-06-21 10:10:43', NULL);
INSERT INTO `jaguar_codegen_code_template`
VALUES (1468086963394940929, 'controller.java',
        'package ${package}.controller;\n\nimport ${package}.entity.${entity};\nimport ${package}.mapper.${entity}Mapper;\nimport ${package}.service.${entity}Service;\nimport ${package}.controller.dto.${entity}CreateDTO;\nimport ${package}.controller.dto.${entity}ModifyDTO;\nimport ${package}.controller.vo.${entity}VO;\n\nimport com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;\nimport com.baomidou.mybatisplus.extension.plugins.pagination.Page;\n\nimport top.wilsonlv.jaguar.basecrud.BaseController;\nimport top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;\nimport top.wilsonlv.jaguar.commons.web.response.JsonResult;\n\nimport io.swagger.annotations.Api;\nimport io.swagger.annotations.ApiParam;\nimport io.swagger.annotations.ApiOperation;\nimport springfox.documentation.annotations.ApiIgnore;\n\nimport javax.validation.Valid;\nimport org.springframework.security.access.prepost.PreAuthorize;\nimport org.springframework.validation.annotation.Validated;\nimport org.springframework.web.bind.annotation.*;\n\n/**\n * <p>\n * ${table.tableComment}  前端控制器\n * </p>\n *\n * @author ${author}\n * @since ${date}\n */\n@Validated\n@RestController\n@RequestMapping(\"/admin/${module}/${entityName}\")\n@Api(tags = \"${table.tableComment}管理\")\npublic class ${entity}Controller extends BaseController<${entity}, ${entity}Mapper, ${entity}Service> {\n\n    @ApiOperation(value = \"分页查询${table.tableComment}\")\n    @PreAuthorize(\"hasAuthority(\'${table.tableComment}管理\')\")\n    @GetMapping(value = \"/page\")\n    public JsonResult<Page<${entity}VO>> page(@ApiIgnore Page<${entity}> page,\n#foreach($column in ${columns})\n	      @ApiParam(\"${column.columnComment}\") @RequestParam(required = false) ${column.filedType} ${column.fieldName}#if($!{velocityCount} != ${columns.size()}),#else) {#end\n\n#end\n        LambdaQueryWrapper<${entity}> wrapper = JaguarLambdaQueryWrapper.<${entity}>newInstance()\n#foreach($column in ${columns})\n  				.eq(${entity}::get${column.fieldMethodName}, ${column.fieldName})#if($!{velocityCount} == ${columns.size()});#end\n\n#end\n			return success(service.query${entity}(page, wrapper));\n    }\n\n    @ApiOperation(value = \"${table.tableComment}详情\")\n    @PreAuthorize(\"hasAuthority(\'${table.tableComment}管理\')\")\n    @GetMapping(value = \"/{id}\")\n    public JsonResult<${entity}VO> detail(@PathVariable Long id){\n        return success(service.getDetail(id));\n    }\n\n    @ApiOperation(value = \"新增${table.tableComment}\")\n    @PreAuthorize(\"hasAuthority(\'${table.tableComment}管理\')\")\n    @PostMapping\n    public JsonResult<Void> create(@RequestBody @Valid ${entity}CreateDTO ${entityName}){\n        service.create(${entityName});\n        return success();\n    }\n  \n    @ApiOperation(value = \"修改${table.tableComment}\")\n    @PreAuthorize(\"hasAuthority(\'${table.tableComment}管理\')\")\n    @PutMapping\n    public JsonResult<Void> modify(@RequestBody @Valid ${entity}ModifyDTO ${entityName}){\n       	 service.modify(${entityName});\n   	     return success();\n    }\n\n    @ApiOperation(value = \"删除${table.tableComment}\")\n    @PreAuthorize(\"hasAuthority(\'${table.tableComment}管理\')\")\n    @DeleteMapping(value = \"/{id}\")\n    public JsonResult<Void> del(@PathVariable Long id){\n        service.checkAndDelete(id);\n        return success();\n    }\n\n}',
        17, '${entity}Controller.java', '${javaPath}/${packagePath}/controller', 0, '2021-06-19 21:18:22',
        '2021-06-19 21:18:22', NULL);
INSERT INTO `jaguar_codegen_code_template`
VALUES (1468087058270097410, 'mapper.java',
        'package ${package}.mapper;\n\nimport ${package}.entity.${entity};\nimport org.apache.ibatis.annotations.Mapper;\nimport top.wilsonlv.jaguar.basecrud.BaseMapper;\n\n/**\n * <p>\n * Mapper接口\n * </p>\n *\n * @author ${author}\n * @since ${date}\n */\n@Mapper\npublic interface ${entity}Mapper extends BaseMapper<${entity}> {\n\n}',
        9, '${entity}Mapper.java', '${javaPath}/${packagePath}/mapper', 0, '2021-06-19 21:15:42', '2021-06-19 21:15:42',
        NULL);
INSERT INTO `jaguar_codegen_code_template`
VALUES (1468087225350197250, 'service.java',
        'package ${package}.service;\n\nimport lombok.RequiredArgsConstructor;\n\nimport ${package}.entity.${entity};\nimport ${package}.mapper.${entity}Mapper;\nimport ${package}.controller.dto.${entity}CreateDTO;\nimport ${package}.controller.dto.${entity}ModifyDTO;\nimport ${package}.controller.vo.${entity}VO;\n\nimport org.springframework.stereotype.Service;\nimport top.wilsonlv.jaguar.basecrud.BaseService;\n\nimport org.springframework.transaction.annotation.Transactional;\n\nimport com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;\nimport com.baomidou.mybatisplus.extension.plugins.pagination.Page;\n\n/**\n * <p>\n * ${table.tableComment} 服务实现类\n * </p>\n *\n * @author ${author}\n * @since ${date}\n */\n@Service\n@RequiredArgsConstructor\npublic class ${entity}Service extends BaseService<${entity}, ${entity}Mapper>  {\n\n    public ${entity}VO getDetail(Long id) {\n        ${entity} ${entityName} = this.getById(id);\n        return ${entityName}.toVo(${entity}VO.class);\n    }\n  \n    public Page<${entity}VO> query${entity}(Page<${entity}> page, LambdaQueryWrapper<${entity}> wrapper) {\n        page = this.query(page, wrapper);\n        Page<${entity}VO> voPage = this.toVoPage(page);\n\n        for (${entity} ${entityName} : page.getRecords()) {\n            ${entity}VO ${entityName}VO = ${entityName}.toVo(${entity}VO.class);\n            voPage.getRecords().add(${entityName}VO);\n        }\n        return voPage;\n    }\n  \n    @Transactional\n    public void create(${entity}CreateDTO createDTO) {\n        ${entity} ${entityName} = createDTO.toEntity(${entity}.class);\n        this.insert(${entityName});\n    }\n  \n    @Transactional\n    public void modify(${entity}ModifyDTO modifyDTO) {\n        ${entity} ${entityName} = modifyDTO.toEntity(${entity}.class);\n        this.updateById(${entityName});\n    }\n  \n    @Transactional\n    public void checkAndDelete(Long id) {\n        this.delete(id);\n    }\n}\n',
        9, '${entity}Service.java', '${javaPath}/${packagePath}/service', 0, '2021-06-19 21:16:48',
        '2021-06-19 21:16:48', NULL);
INSERT INTO `jaguar_codegen_code_template`
VALUES (1468087277405704194, 'entity.java',
        'package ${package}.entity;\n\nimport com.baomidou.mybatisplus.annotation.TableField;\nimport com.baomidou.mybatisplus.annotation.TableName;\nimport lombok.Data;\nimport lombok.EqualsAndHashCode;\nimport top.wilsonlv.jaguar.basecrud.BaseModel;\n\n#if(${dateTimeScore} == 1 || ${dateTimeScore} == 3)\nimport java.time.LocalDate;\n#end\n#if(${dateTimeScore} == 2 || ${dateTimeScore} == 3)\nimport java.time.LocalDateTime;\n#end\n\n\n/**\n * <p>\n * ${table.tableComment}\n * </p>\n *\n * @author ${author}\n * @since ${date}\n */\n@Data\n@EqualsAndHashCode(callSuper = true)\n@TableName(\"${table.tableName}\")\npublic class ${entity} extends BaseModel {\n\n   private static final long serialVersionUID = 1L;\n  \n#foreach($column in ${columns})\n#if(\"$!column.columnComment\" != \"\")\n  /**\n  * ${column.columnComment}\n  */\n#end\n	@TableField(\"${column.columnName}\")\n	private ${column.filedType} ${column.fieldName};\n  \n#end\n}',
        10, '${entity}.java', '${javaPath}/${packagePath}/entity', 0, '2021-06-19 21:15:30', '2021-06-19 21:15:30',
        NULL);

-- ----------------------------
-- Table structure for jaguar_codegen_datasource
-- ----------------------------
DROP TABLE IF EXISTS `jaguar_codegen_datasource`;
CREATE TABLE `jaguar_codegen_datasource`
(
    `id_`         bigint(20) UNSIGNED NOT NULL COMMENT 'ID',
    `name_`       varchar(50)         NOT NULL COMMENT '数据源名称',
    `host_`       varchar(50)         NOT NULL COMMENT 'host',
    `port_`       varchar(10)         NOT NULL COMMENT 'port',
    `schema_`     varchar(45)         NOT NULL COMMENT 'schema',
    `username_`   varchar(50)         NOT NULL COMMENT '用户名',
    `password_`   varchar(50)         NOT NULL COMMENT '密码',
    `deleted_`    tinyint(1)          NULL DEFAULT 0 COMMENT '逻辑删除标记(0--正常 1--删除)',
    `create_time` timestamp(0)        NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` timestamp(0)        NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `remark_`     varchar(50)         NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '数据源'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jaguar_codegen_datasource
-- ----------------------------


SET FOREIGN_KEY_CHECKS = 1;
