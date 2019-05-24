package org.jaguar.modules.code.generator.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.jaguar.core.base.BaseMapper;
import org.jaguar.modules.code.generator.model.CodeGenerator;

import java.util.List;

/**
 * Created by lvws on 2019/4/30.
 */
public interface CodeGeneratorMapper extends BaseMapper<CodeGenerator> {

    List<CodeGenerator> showTables(IPage page, @Param("schema") String schema, @Param("fuzzyTableName") String fuzzyTableName);

}
