package top.wilsonlv.jaguar.cloud.handlerlog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import top.wilsonlv.jaguar.cloud.handlerlog.entity.HandlerLog;
import top.wilsonlv.jaguar.cloud.handlerlog.mapper.HandlerLogMapper;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.commons.web.JsonResult;

import java.util.List;

/**
 * <p>
 * 接口请求日志  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2021/08/06.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/handlerLog")
@Api(tags = "接口请求日志")
public class HandlerLogController implements InitializingBean {

    private final HandlerLogMapper handlerLogMapper;

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void afterPropertiesSet() {
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(HandlerLog.class);
        if (!indexOperations.exists()) {
            indexOperations.createMapping();
        }
    }

    @ApiOperation(value = "查询接口请求日志")
    @PreAuthorize("hasAuthority('接口请求日志')")
    @GetMapping(value = "/page")
    public JsonResult<List<HandlerLog>> page(
            @ApiIgnore Page<HandlerLog> page,
            @ApiParam(value = "会话ID") @RequestParam(required = false) String sessionId,
            @ApiParam(value = "访问令牌") @RequestParam(required = false) String accessToken,
            @ApiParam(value = "客户端IP") @RequestParam(required = false) String clientHost,
            @ApiParam(value = "模糊请求地址") @RequestParam(required = false) String fuzzyRequestUri,
            @ApiParam(value = "接口名称") @RequestParam(required = false) String apiOperation) {

        LambdaQueryWrapper<HandlerLog> wrapper = JaguarLambdaQueryWrapper.<HandlerLog>newInstance()
                .eq(HandlerLog::getSessionId, sessionId)
                .eq(HandlerLog::getAccessToken, accessToken)
                .eq(HandlerLog::getClientHost, clientHost)
                .like(HandlerLog::getRequestUri, fuzzyRequestUri)
                .eq(HandlerLog::getApiOperation, apiOperation)
                .last("limit " + page.getSize());
        return JsonResult.success(handlerLogMapper.selectList(wrapper));
    }

}