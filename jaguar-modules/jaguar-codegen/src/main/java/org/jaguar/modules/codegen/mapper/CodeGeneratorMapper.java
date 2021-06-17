package org.jaguar.modules.codegen.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jaguar.commons.basecrud.BaseMapper;
import org.jaguar.modules.codegen.controller.dto.CodeGenerator;
import org.jaguar.modules.codegen.controller.vo.TableVO;

/**
 * @author lvws
 * @since 2019/4/30.
 */
@Mapper
public interface CodeGeneratorMapper extends BaseMapper<CodeGenerator> {

    /**
     * 查询数据库表列表
     *
     * @param page           分页
     * @param schema         数据库
     * @param fuzzyTableName 模糊表名
     * @return 表信息
     */
    Page<TableVO> showTables(Page<TableVO> page, @Param("schema") String schema, @Param("fuzzyTableName") String fuzzyTableName);

}
