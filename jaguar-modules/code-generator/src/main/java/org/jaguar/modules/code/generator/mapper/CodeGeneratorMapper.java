package org.jaguar.modules.code.generator.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.jaguar.commons.basecrud.BaseMapper;
import org.jaguar.modules.code.generator.model.CodeGenerator;

/**
 * @author lvws
 * @since 2019/4/30.
 */
public interface CodeGeneratorMapper extends BaseMapper<CodeGenerator> {

    /**
     * 查询数据库表列表
     *
     * @param page           分页
     * @param schema         数据库
     * @param fuzzyTableName 模糊表名
     * @return 实体类
     */
    IPage<CodeGenerator> showTables(IPage<CodeGenerator> page, @Param("schema") String schema, @Param("fuzzyTableName") String fuzzyTableName);

}
