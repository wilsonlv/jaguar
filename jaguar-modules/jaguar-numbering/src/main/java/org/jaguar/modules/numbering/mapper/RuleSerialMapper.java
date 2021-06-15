package org.jaguar.modules.numbering.mapper;

import org.apache.ibatis.annotations.Param;
import org.jaguar.commons.basecrud.BaseMapper;
import org.jaguar.modules.numbering.model.RuleSerial;

import javax.validation.constraints.NotBlank;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * 编号规则序列   Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2019-04-02
 */
public interface RuleSerialMapper extends BaseMapper<RuleSerial> {

    /**
     * 执行sql
     *
     * @param sql sql语句
     * @return 执行结果
     */
    List<LinkedHashMap<String, Object>> execute(@Param("sql") String sql);
}