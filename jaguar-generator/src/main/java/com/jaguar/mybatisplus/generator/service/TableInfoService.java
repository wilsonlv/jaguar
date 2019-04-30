package com.jaguar.mybatisplus.generator.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.jaguar.core.base.BaseService;
import com.jaguar.mybatisplus.generator.mapper.TableInfoMapper;
import com.jaguar.mybatisplus.generator.model.TableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lvws on 2019/4/30.
 */
@Service
public class TableInfoService extends BaseService<TableInfo, TableInfoMapper> {

    @Autowired
    private AutoGenerator generator;
    @Autowired
    private GlobalConfig globalConfig;
    @Autowired
    private StrategyConfig strategyConfig;
    @Autowired
    private PackageConfig packageConfig;

    private String schema;

    @Autowired
    public void setSchema(@Value("${spring.datasource.url}") String url) {
        url = url.split("\\?")[0];
        this.schema = url.substring(url.lastIndexOf('/') + 1);
    }

    public List<TableInfo> showTables(Map<String, Object> param) {
        Page<Long> page = getPage(param);
        param.put("schema", schema);
        return mapper.showTables(page, param);
    }

    public void generate(TableInfo tableInfo) {
        globalConfig.setAuthor(tableInfo.getAuthor());
        globalConfig.setOutputDir(tableInfo.getOutputDir());
        strategyConfig.setInclude(tableInfo.getTableName());
        packageConfig.setParent(tableInfo.getParentPackage());

        // 执行生成
        generator.execute();
    }
}
