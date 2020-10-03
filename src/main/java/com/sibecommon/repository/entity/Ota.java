package com.sibecommon.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * OTA平台信息
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Ota对象", description="OTA平台信息")
public class Ota extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "OTACode")
    private String otaCode;

    @ApiModelProperty(value = "OTA中文名")
    private String otaCname;

    @ApiModelProperty(value = "OTA英文名")
    private String otaEname;

    @ApiModelProperty(value = "OTA中文全名")
    private String otaFullCname;

    @ApiModelProperty(value = "OTA英文全名")
    private String otaFullEname;

    @ApiModelProperty(value = "OTA类型 数据字典 OTA_TYPE")
    private String otaType;

    @ApiModelProperty(value = "国家Code,2字码 如 CN/US/KR ref table base_country.country_code")
    private String countryCode;

    @ApiModelProperty(value = "城市Code 3字码 如 SHA/NYC/SEL ref table base_city.city_code")
    private String cityCode;

    @ApiModelProperty(value = "客户级别 数据字典OTA_GRADE")
    private String grade;

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
