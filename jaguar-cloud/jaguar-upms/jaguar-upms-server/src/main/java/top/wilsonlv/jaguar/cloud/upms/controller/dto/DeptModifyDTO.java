package top.wilsonlv.jaguar.cloud.upms.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.web.base.BaseModifyDTO;


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

   private static final long serialVersionUID = 1L;
  

	@ApiModelProperty("父ID")
	private Long parentId;
  

	@ApiModelProperty("部门名称")
	private String deptName;
  
}