package comm.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * GDS规则信息
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OtaRule对象", description="GDS规则信息(ml_gds_rule) ")
public class OtaRule extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "OTA平台编码")
    private String otaCode;

    @ApiModelProperty(value = "OTA站点编码")
    private String otaSiteCode;

    @ApiModelProperty(value = "GDS规则类型")
    private String ruleType;

    @ApiModelProperty(value = "出发地，多个逗号隔开 如CN,HK")
    private String origin;

    @ApiModelProperty(value = "目的地，多个逗号隔开 如CN,HK")
    private String destination;

    @ApiModelProperty(value = "双向标识1-单项，2-双向")
    private Integer bothWaysFlag;

    @ApiModelProperty(value = "开始旅行日期 如2020-01-12>2020-09-30,2020-01-12>2020-09-30")
    private LocalDateTime travelPeriodFrom;

    @ApiModelProperty(value = "结束旅行日期如2020-01-12>2020-09-30,2020-01-12>2020-09-30")
    private LocalDateTime travelPeriodTo;

    @ApiModelProperty(value = "自定义内容一")
    private String parameter1;

    @ApiModelProperty(value = "自定义内容二")
    private String parameter2;

    @ApiModelProperty(value = "自定义内容三")
    private String parameter3;

    @ApiModelProperty(value = "自定义内容四")
    private String parameter4;

    @ApiModelProperty(value = "自定义内容五")
    private String parameter5;

    @ApiModelProperty(value = "版本控制")
    private Integer version;

    @ApiModelProperty(value = "备注说明")
    private String remark;

    @ApiModelProperty(value = "优先排序序号")
    private Integer sortSeq;

    @ApiModelProperty(value = "生效日期(开始)")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "失效日期(截止)")
    private LocalDateTime endTime;

}
