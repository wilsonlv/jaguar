package top.wilsonlv.jaguar.commons.basecrud;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.logging.Log;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.commons.web.exception.impl.DataCrudException;

import java.util.Collection;

/**
 * @author lvws
 * @since 2021/11/22
 */
@Component
public class BatchOperation<E extends BaseModel, M extends BaseMapper<E>> {

    public void batchInsertById(Class<E> eClass, Class<M> mClass, Log log, Collection<E> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(mClass, SqlMethod.INSERT_ONE);
        boolean success = SqlHelper.executeBatch(eClass, log, entityList, batchSize,
                (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
        if (!success) {
            throw new DataCrudException("批量插入数据失败！");
        }
    }

    public void batchUpdateById(Class<E> eClass, Class<M> mClass, Log log, Collection<E> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(mClass, SqlMethod.UPDATE_BY_ID);
        boolean success = SqlHelper.executeBatch(eClass, log, entityList, batchSize, (sqlSession, entity) -> {
            MapperMethod.ParamMap<E> param = new MapperMethod.ParamMap<>();
            param.put("et", entity);
            sqlSession.update(sqlStatement, param);
        });
        if (!success) {
            throw new DataCrudException("批量更新数据失败！");
        }
    }

}
