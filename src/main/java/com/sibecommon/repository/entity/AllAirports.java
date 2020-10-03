package com.sibecommon.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 所有机场表
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="AllAirports对象", description="所有机场表")
public class AllAirports extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "洲")
    private String continent;

    @ApiModelProperty(value = "洲码")
    private String zcode;

    @ApiModelProperty(value = "区域")
    private String region;

    @ApiModelProperty(value = "区域码")
    private String qcode;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "国家码")
    private String gcode;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "城市码")
    private String ccode;

    @ApiModelProperty(value = "机场")
    private String airport;

    @ApiModelProperty(value = "机场码")
    private String code;


}
