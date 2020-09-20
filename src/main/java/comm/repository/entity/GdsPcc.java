package comm.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * GDS PCC配置(ml_gds_pcc)
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="GdsPcc对象", description="GDSPCC配置")
public class GdsPcc extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "PCC(1G), OID(1A)")
    private String pccCode;

    @ApiModelProperty(value = "GDS Code IATA标准2字代码")
    private String gdsCode;

    @ApiModelProperty(value = "用户名")
    private String apiUserName;

    @ApiModelProperty(value = "密码（请加密）")
    private String apiPassword;

    @ApiModelProperty(value = "1G 专用")
    private String apiTargetBranch;

    @ApiModelProperty(value = "1A 专用")
    private String apiWsap;

    @ApiModelProperty(value = "扩展字段1")
    private String apiAttribete1;

    @ApiModelProperty(value = "扩展字段2")
    private String apiAttribete2;

    @ApiModelProperty(value = "扩展字段3")
    private String apiAttribete3;

    @ApiModelProperty(value = "扩展字段4")
    private String apiAttribete4;

    @ApiModelProperty(value = "扩展字段5")
    private String apiAttribete5;

    @ApiModelProperty(value = "PCC原始币种")
    private String pccCurrency;

    @ApiModelProperty(value = "结算币种 ref table sibe_currency")
    private String pccSettlementCurrency;

    @ApiModelProperty(value = "位置 暂仅供参考")
    private String pccPosition;

    @ApiModelProperty(value = "供应商 暂仅供参考 ref— sibe_supplier")
    private String pccSupplier;

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

    @ApiModelProperty("时区")
    private String timeZone;

}
