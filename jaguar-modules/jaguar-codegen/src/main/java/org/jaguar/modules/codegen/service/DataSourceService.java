package org.jaguar.modules.codegen.service;

import lombok.RequiredArgsConstructor;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.modules.codegen.mapper.DataSourceMapper;
import org.jaguar.modules.codegen.model.DataSource;
import org.springframework.stereotype.Service;

/**
 * @author lvws
 * @since 2021-06-16
 */
@Service
@RequiredArgsConstructor
public class DataSourceService extends BaseService<DataSource, DataSourceMapper> {



}
