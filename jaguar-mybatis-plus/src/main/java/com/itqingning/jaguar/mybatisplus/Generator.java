package com.itqingning.jaguar.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @author lws
 */
public class Generator {

    public static void main(String[] args) {

        String context = "lims";
        String author = "lvws";
//        String outputDir = "/Users/apple/develop/data/generator";
        String outputDir = "/Users/apple/develop/IdeaProjects/Lims/Lims-trunk/lims-sys-server/src/main/java";

        String dbUserName = "root";
        String dbPassword = "lxj900221";
        String dbUrl = "jdbc:mysql://127.0.0.1:3306/lims?characterEncoding=utf8";

        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(outputDir);
        gc.setFileOverride(true);
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setOpen(false);
        gc.setAuthor(author);
        gc.setServiceImplName("%sService");
        mpg.setGlobalConfig(gc);
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(dbUserName);
        dsc.setPassword(dbPassword);
        dsc.setUrl(dbUrl);
        mpg.setDataSource(dsc);
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(new String[]{"process_operation_record"}); // 需要生成的表
        // 字段名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 自定义实体父类
        strategy.setSuperEntityClass("com." + context + ".core.base.BaseModel");
        // 自定义实体，公共字段
        strategy.setSuperEntityColumns(new String[]{"id_", "enable_", "remark_", "create_by", "create_time", "update_by", "update_time"});
        // 自定义 mapper 父类
        strategy.setSuperMapperClass("com." + context + ".core.base.BaseMapper");
        // 自定义 service 父类
        strategy.setSuperServiceClass("com." + context + ".core.base.BaseService");
        // 自定义 service 实现类父类
        strategy.setSuperServiceImplClass("com." + context + ".core.base.BaseService");
        // 自定义 controller 父类
        strategy.setSuperControllerClass("com." + context + ".core.base.AbstractController");
        mpg.setStrategy(strategy);
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com." + context + ".sys");
        pc.setEntity("model");
        pc.setMapper("mapper");
        pc.setXml("mapper.xml");
        pc.setServiceImpl("service");
        pc.setController("controller");
        mpg.setPackageInfo(pc);

        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/template 下面内容修改，
        // 放置自己项目的 src/main/resources/template 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        TemplateConfig tc = new TemplateConfig();
        tc.setEntity("com/" + context + "/core/support/mybaits/template/entity.java.vm");
        tc.setMapper("com/" + context + "/core/support/mybaits/template/mapper.java.vm");
        tc.setXml("com/" + context + "/core/support/mybaits/template/mapper.xml.vm");
        tc.setServiceImpl("com/" + context + "/core/support/mybaits/template/service.java.vm");
        tc.setController("com/" + context + "/core/support/mybaits/template/controller.java.vm");
        mpg.setTemplate(tc);
        // 执行生成
        mpg.execute();
    }
}
