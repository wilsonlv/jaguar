/**
 *
 */
package com.jaguar.core.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lvws
 * @version 2016年6月3日 下午2:30:14
 */
public interface BaseMapper<T extends BaseModel> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    List<Long> selectIdPage(@Param("cm") Map<String, Object> params);

    List<Long> selectIdPage(IPage page, @Param("cm") Map<String, Object> params);

    void physicalDelete(@Param("id") Long id);
}
