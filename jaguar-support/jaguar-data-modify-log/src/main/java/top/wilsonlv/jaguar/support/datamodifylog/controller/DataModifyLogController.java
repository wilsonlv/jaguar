package top.wilsonlv.jaguar.support.datamodifylog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import top.wilsonlv.jaguar.support.datamodifylog.entity.DataModifyLog;
import top.wilsonlv.jaguar.support.datamodifylog.mapper.DataModifyLogMapper;

import java.time.LocalDateTime;

/**
 * @author lvws
 * @since 2021/11/22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dataModifyLog")
@Api(tags = "数据修改日志管理")
public class DataModifyLogController {

    private final DataModifyLogMapper dataModifyLogMapper;

    @ApiOperation(value = "分页查询数据修改日志")
    @PreAuthorize("hasAuthority('数据修改日志')")
    @GetMapping(value = "/page")
    public JsonResult<Page<DataModifyLog>> page(
            @ApiIgnore Page<DataModifyLog> page,
            @ApiParam(value = "类全名") @RequestParam(required = false) String className,
            @ApiParam(value = "记录ID") @RequestParam(required = false) Long recordId,
            @ApiParam(value = "字段名称") @RequestParam(required = false) String fieldName,
            @ApiParam(value = "本次更新开始时间") @RequestParam(required = false) LocalDateTime modifyStartTime,
            @ApiParam(value = "本次更新结束时间") @RequestParam(required = false) LocalDateTime modifyEndTime,
            @ApiParam(value = "本次更新人用户名") @RequestParam(required = false) String modifyUserName) {

        LambdaQueryWrapper<DataModifyLog> wrapper = JaguarLambdaQueryWrapper.<DataModifyLog>newInstance()
                .eq(DataModifyLog::getClassName, className)
                .eq(DataModifyLog::getRecordId, recordId)
                .eq(DataModifyLog::getFieldName, fieldName)
                .ge(DataModifyLog::getModifyTime, modifyStartTime)
                .le(DataModifyLog::getModifyTime, modifyEndTime)
                .eq(DataModifyLog::getModifyUserName, modifyUserName);

        page = dataModifyLogMapper.selectPage(page, wrapper);
        return JsonResult.success(page);
    }

}
