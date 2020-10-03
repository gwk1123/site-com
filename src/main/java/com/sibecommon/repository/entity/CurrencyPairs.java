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
 * 实时外汇数据
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CurrencyPairs对象", description="实时外汇数据")
public class CurrencyPairs implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "基本币")
    private String currency;

    @ApiModelProperty(value = "交易币")
    private String exchangeCurrency;

    @ApiModelProperty(value = "现汇卖出价")
    private BigDecimal forexSellRate;

    @ApiModelProperty(value = "修改人ID")
    private Long updateUserId;

    @ApiModelProperty(value = "外汇更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人名称")
    private String updateUserName;

    @ApiModelProperty(value = "创建人名称")
    private String createUserName;

    @ApiModelProperty(value = "0-有效(正常),1-暂停(挂起) 99-无效(删除)")
    private Integer status;


}
