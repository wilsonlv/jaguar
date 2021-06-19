package org.jaguar.modules.codegen.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jaguar.modules.codegen.controller.vo.ColumnVO;
import org.jaguar.modules.codegen.controller.vo.TableVO;

import java.util.List;

/**
 * @author lvws
 * @since 2021-06-16
 */
@Mapper
public interface CodeGeneratorMapper {

    /**
     * 查询数据库表列表
     *
     * @param page           分页
     * @param schema         数据库
     * @param fuzzyTableName 模糊表名
     * @return 表信息
     */
    Page<TableVO> showTables(Page<TableVO> page, @Param("schema") String schema, @Param("fuzzyTableName") String fuzzyTableName);

    /**
     * 查询数据库表详情
     *
     * @param schema    数据库
     * @param tableName 精确表名
     * @return 表信息
     */
    TableVO getTableInfo(@Param("schema") String schema, @Param("tableName") String tableName);

    /**
     * 查询数据库表列信息
     *
     * @param schema    数据库
     * @param tableName 精确表名
     * @return 列信息
     */
    List<ColumnVO> listColumnInfo(@Param("schema") String schema, @Param("tableName") String tableName);
}