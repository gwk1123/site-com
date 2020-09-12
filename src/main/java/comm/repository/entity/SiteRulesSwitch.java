package comm.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 站点规则开关
 * </p>
 *
 * @author gwk
 * @since 2020-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SiteRulesSwitch对象", description="站点规则开关")
public class SiteRulesSwitch extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "平台")
    private String otaCode;

    @ApiModelProperty(value = "站点")
    private String otaSiteCode;

    @ApiModelProperty(value = "对规则开关进行分组")
    private String groupKey;

    @ApiModelProperty(value = "键")
    private String parameterKey;

    @ApiModelProperty(value = "参数名称")
    private String parameterName;

    @ApiModelProperty(value = "值")
    private String parameterValue;

    @ApiModelProperty(value = "备注说明")
    private String remark;

    @ApiModelProperty(value = "状态：0-无效(删除),1-有效(正常),2-暂停")
    private Integer status;


}
