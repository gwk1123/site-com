package comm.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * OTA平台站点表
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OtaSite对象", description="OTA平台站点表")
public class OtaSite extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "OTA 站点Code")
    private String otaSiteCode;

    @ApiModelProperty(value = "OTA 站点中文名")
    private String otaSiteCname;

    @ApiModelProperty(value = "OTA 站点英文名")
    private String otaSiteEname;

    @ApiModelProperty(value = "OTACode")
    private String otaCode;

    @ApiModelProperty(value = "版本控制")
    private Integer version;

    @ApiModelProperty(value = "备注说明")
    private String remark;

    @ApiModelProperty(value = "优先排序序号")
    private Integer sortSeq;

    @ApiModelProperty(value = "生效日期(开始) ")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "失效日期(截止)")
    private LocalDateTime endTime;

}
