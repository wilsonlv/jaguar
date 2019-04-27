package com.jaguar.process.mapper;

import com.jaguar.process.model.po.ButtonInst;
import com.jaguar.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2019-04-04
 */
public interface ButtonInstMapper extends BaseMapper<ButtonInst> {

    List<Long> queryPageButtonList(@Param("cm") Map<String, Object> param);
}