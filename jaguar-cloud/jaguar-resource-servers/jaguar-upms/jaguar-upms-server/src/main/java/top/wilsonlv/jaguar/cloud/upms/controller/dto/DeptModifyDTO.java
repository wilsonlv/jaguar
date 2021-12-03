package top.wilsonlv.jaguar.cloud.upms.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.web.base.BaseModifyDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * <p>
 * 部门
 * </p>
 *
 * @author lvws
 * @since 2021-11-15
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class DeptModifyDTO extends BaseModifyDTO {

    @NotNull(message = "父ID为非空")
    @ApiModelProperty(value = "父ID", required = true)
    private Long parentId;

    @NotBlank(message = "部门名称为非空")
    @ApiModelProperty("部门名称")
    private String deptName;

}