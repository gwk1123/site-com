package com.sibecommon.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 实时外汇数据
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ExchangeRate对象", description="实时外汇数据")
public class ExchangeRate extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "1：1G汇率，2:1A汇率，3：中国银行，4浙商银行")
    private Integer source;

    @ApiModelProperty(value = "基本币")
    private String currency;

    @ApiModelProperty(value = "交易币")
    private String exchangeCurrency;

    @ApiModelProperty(value = "交易币单位 元为单位")
    private Integer exchangeUnit;

    @ApiModelProperty(value = "中间价")
    private BigDecimal centralParityRate;

    @ApiModelProperty(value = "现汇卖出价")
    private BigDecimal forexSellRate;

    @ApiModelProperty(value = "现钞卖出价")
    private BigDecimal cashSellRate;

    @ApiModelProperty(value = "现汇买入价")
    private BigDecimal forexBuyRate;

    @ApiModelProperty(value = "现钞买入价")
    private BigDecimal cashBuyRate;


}
