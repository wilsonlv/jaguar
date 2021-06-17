package org.jaguar.modules.codegen.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jaguar.commons.basecrud.BaseMapper;
import org.jaguar.modules.codegen.model.CodeTemplate;

import java.util.List;

/**
 * @author lvws
 * @since 2021-06-17
 */
@Mapper
public interface CodeTemplateMapper extends BaseMapper<CodeTemplate> {

    /**
     * 查询最新版本的代码模板
     *
     * @return 最新版本的代码模板
     */
    List<CodeTemplate> findLatest();

}