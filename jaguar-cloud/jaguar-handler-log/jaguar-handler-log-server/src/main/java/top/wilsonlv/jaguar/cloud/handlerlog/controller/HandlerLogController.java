package top.wilsonlv.jaguar.cloud.handlerlog.controller;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.wilsonlv.jaguar.cloud.handlerlog.entity.HandlerLog;
import top.wilsonlv.jaguar.cloud.handlerlog.mapper.HandlerLogMapper;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.commons.web.JsonResult;

import javax.validation.constraints.Max;
import java.util.*;

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
    @GetMapping(value = "/list")
    public JsonResult<List<HandlerLog>> list(
            @ApiParam(value = "最大ID（用于查询更多历史记录）") @RequestParam(required = false) Long maxId,
            @ApiParam(value = "sessionId") @RequestParam(required = false) String sessionId,
            @ApiParam(value = "authorization") @RequestParam(required = false) String authorization,
            @ApiParam(value = "客户端IP") @RequestParam(required = false) String clientHost,
            @ApiParam(value = "接口名称") @RequestParam(required = false) String apiOperation,
            @ApiParam(value = "请求方式") @RequestParam(required = false) String method,
            @ApiParam(value = "响应状态码") @RequestParam(required = false) Integer status,
            @ApiParam(value = "模糊请求地址") @RequestParam(required = false) String fuzzyRequestUri,
            @ApiParam(value = "模糊客户端引擎") @RequestParam(required = false) String fuzzyUserAgent,
            @ApiParam(value = "请求参数") @RequestParam(required = false) String matchParameters,
            @ApiParam(value = "响应结果") @RequestParam(required = false) String matchJsonResult,
            @ApiParam(value = "错误信息") @RequestParam(required = false) String matchErrorMsg,
            @ApiParam(value = "请求开始时间") @RequestParam(required = false)
            @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN) Date accessStartTime,
            @ApiParam(value = "请求结束时间") @RequestParam(required = false)
            @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN) Date accessEndTime,
            @ApiParam(value = "请求最小时长") @RequestParam(required = false) Long minDuration,
            @ApiParam(value = "请求最大时长") @RequestParam(required = false) Long maxDuration) {

        Map<String, Object> params = new HashMap<>();
        params.put("maxId", maxId);
        params.put("sessionId", sessionId);
        params.put("authorization", authorization);
        params.put("clientHost", clientHost);
        params.put("apiOperation", apiOperation);
        params.put("method", method);
        params.put("status", status);

        params.put("fuzzyRequestUri", fuzzyRequestUri);
        params.put("fuzzyUserAgent", fuzzyUserAgent);

        params.put("matchParameters", matchParameters);
        params.put("matchJsonResult", matchJsonResult);
        params.put("matchErrorMsg", matchErrorMsg);

        params.put("accessStartTime", accessStartTime);
        params.put("accessEndTime", accessEndTime);
        params.put("minDuration", minDuration);
        params.put("maxDuration", maxDuration);

        return JsonResult.success(handlerLogMapper.queryHandlerLog(params));
    }

    @ApiOperation(value = "接口请求日志条目总数")
    @PreAuthorize("hasAuthority('接口请求日志')")
    @GetMapping(value = "/count")
    public JsonResult<Integer> count(
            @ApiParam(value = "会话ID") @RequestParam(required = false) String sessionId,
            @ApiParam(value = "访问令牌") @RequestParam(required = false) String accessToken,
            @ApiParam(value = "客户端IP") @RequestParam(required = false) String clientHost,
            @ApiParam(value = "模糊请求地址") @RequestParam(required = false) String fuzzyRequestUri,
            @ApiParam(value = "接口名称") @RequestParam(required = false) String apiOperation) {

        LambdaQueryWrapper<HandlerLog> wrapper = JaguarLambdaQueryWrapper.<HandlerLog>newInstance()
                .eq(HandlerLog::getSessionId, sessionId)
                .eq(HandlerLog::getAuthorization, accessToken)
                .eq(HandlerLog::getClientHost, clientHost)
                .like(HandlerLog::getRequestUri, fuzzyRequestUri)
                .eq(HandlerLog::getApiOperation, apiOperation);

        return JsonResult.success(handlerLogMapper.selectCount(wrapper));
    }

    @ApiOperation(value = "接口请求日志条目总数")
    @PreAuthorize("hasAuthority('接口请求日志')")
    @GetMapping(value = "/{id}")
    public JsonResult<HandlerLog> detail(@PathVariable Long id) {
        return JsonResult.success(handlerLogMapper.selectById(id));
    }

}