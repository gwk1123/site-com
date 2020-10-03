package com.sibecommon.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 航段信息表
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OrderSegment对象", description="航段信息表")
public class OrderSegment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关联键(bookinfo)")
    private Integer bookInfoId;

    @ApiModelProperty(value = "航程")
    private String journeySequence;

    @ApiModelProperty(value = "航段")
    private String segmentSequence;

    @ApiModelProperty(value = "航班号")
    private String flightNumber;

    @ApiModelProperty(value = "出发城市")
    private String depCity;

    @ApiModelProperty(value = "抵达城市")
    private String arrCity;

    @ApiModelProperty(value = "出发机场")
    private String depAirport;

    @ApiModelProperty(value = "抵达机场")
    private String arrAirport;

    @ApiModelProperty(value = "舱等")
    private String cabinGrade;

    @ApiModelProperty(value = "舱位")
    private String cabin;

    @ApiModelProperty(value = "出发时间")
    private String depTime;

    @ApiModelProperty(value = "抵达时间")
    private String arrTime;

    @ApiModelProperty(value = "共享标识")
    private String codeShare;

    @ApiModelProperty(value = "共享航班号")
    private String operatingFlightNumber;

    @ApiModelProperty(value = "共享航司")
    private String operatingAirline;

    @ApiModelProperty(value = "主销售航司")
    private String marketingAirline;

    @ApiModelProperty(value = "机型")
    private String aircraftCode;

    @ApiModelProperty(value = "出发航站楼")
    private String depTerminal;

    @ApiModelProperty(value = "抵达航站楼")
    private String arrTerminal;

    @ApiModelProperty(value = "舱位")
    private Integer durationMinute;

    @ApiModelProperty(value = "舱位")
    private Integer stopMinute;

    @ApiModelProperty(value = "舱位")
    private Integer airliftFlag;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


}
