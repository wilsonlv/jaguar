package top.wilsonlv.jaguar.codegen.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.wilsonlv.jaguar.codegen.model.CodeTemplate;
import top.wilsonlv.jaguar.commons.basecrud.BaseMapper;

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