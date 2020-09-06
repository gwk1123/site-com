package comm.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="GDS规则", description="GDS规则信息")
public class GdsRule extends BaseEntity{

    /**
     * 出票GDS系统（参考sibe_gds）
     */
    @ApiModelProperty(value = "出票GDS系统（参考sibe_gds）")
    private String gdsCode;

    /**
     * 配置号
     */
    @ApiModelProperty(value = "配置号")
    private String pccCode;

    /**
     * 规则类型
     */
    @ApiModelProperty(value = "规则类型")
    private String ruleType;

    /**
     * 规则内容一
     */
    @ApiModelProperty(value = "规则内容一")
    private String parameter1;

    /**
     * 规则内容二
     */
    @ApiModelProperty(value = "规则内容二")
    private String parameter2;

    /**
     * 规则内容三
     */
    @ApiModelProperty(value = "规则内容三")
    private String parameter3;

    /**
     * 规则内容四
     */
    @ApiModelProperty(value = "规则内容四")
    private String parameter4;

    /**
     * 规则内容五
     */
    @ApiModelProperty(value = "规则内容五")
    private String parameter5;

    /**
     * 始发地
     */
    @ApiModelProperty(value = "始发地")
    private String origin;

    /**
     * 抵达
     */
    @ApiModelProperty(value = "抵达")
    private String destination;

    /**
     * 双向标志
     */
    @ApiModelProperty(value = "双向标志")
    private Integer bothWaysFlag;

    /**
     * 规则生效日期
     */
    @ApiModelProperty(value = "规则生效日期")
    private LocalDate effectiveFrom;

    /**
     * 规则结束日期
     */
    @ApiModelProperty(value = "规则结束日期")
    private LocalDate effectiveTo;

    /**
     * 乐观锁版本号
     */
    @ApiModelProperty(value = "乐观锁版本号")
    private Integer version;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
