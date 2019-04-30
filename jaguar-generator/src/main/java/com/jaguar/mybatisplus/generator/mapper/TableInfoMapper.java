package com.jaguar.mybatisplus.generator.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jaguar.core.base.BaseMapper;
import com.jaguar.mybatisplus.generator.model.TableInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by lvws on 2019/4/30.
 */
public interface TableInfoMapper extends BaseMapper<TableInfo> {

    List<TableInfo> showTables(IPage page, @Param("cm") Map<String, Object> params);

}
