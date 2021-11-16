package top.wilsonlv.jaguar.cloud.upms.sdk.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.web.base.BaseVO;

import java.util.List;


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
public class DeptVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("父ID")
    private Long parentId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("子部门")
    private List<DeptVO> children;

}