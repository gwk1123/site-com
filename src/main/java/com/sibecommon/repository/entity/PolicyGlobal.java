package com.sibecommon.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author gwk
 * @since 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PolicyGlobal对象", description="")
public class PolicyGlobal extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "平台")
    private String otaCode;

    @ApiModelProperty(value = "站点")
    private String otaSiteCode;

    @ApiModelProperty(value = "产品类型 单选框")
    private String productType;

    @ApiModelProperty(value = "航司二字代码只允许录入一家")
    private String airline;

    @ApiModelProperty(value = "适用运价类型是 下拉框")
    private String priceType;

    @ApiModelProperty(value = "销售开始时间")
    private LocalDateTime saleStartTime;

    @ApiModelProperty(value = "销售结束时间")
    private LocalDateTime saleEndTime;

    @ApiModelProperty(value = "是否适用于共享 下拉框")
    private Integer permitCodeShare;

    @ApiModelProperty(value = "是否适用于联运 下拉框")
    private Integer permitInterline;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "成人票面留钱")
    private BigDecimal adultPrice;

    @ApiModelProperty(value = "成人税费留钱")
    private BigDecimal adultTax;

    @ApiModelProperty(value = "儿童票面留钱")
    private BigDecimal childPrice;

    @ApiModelProperty(value = "儿童税费留钱")
    private BigDecimal childTax;

    @ApiModelProperty(value = "是否允许中转")
    private Integer permitTransit;

    @ApiModelProperty(value = "行程类型")
    private String tripType;

    @ApiModelProperty(value = "备注说明")
    private String remark;

    @ApiModelProperty(value = "GDS类型")
    private String bookGdsChannel;

    @ApiModelProperty(value = "乐观锁版本号")
    private Integer version;


}
