package com.sibecommon.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 航线路由信息(ml_route_config)
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="RouteConfig对象", description="航线路由信息(ml_route_config) ")
public class RouteConfig extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "出发地多个逗号隔开")
    private String origin;

    @ApiModelProperty(value = "目的地多个逗号隔开")
    private String destination;

    @ApiModelProperty(value = "双向标识1-单项，2-双向")
    private Integer bothWaysFlag;

    @ApiModelProperty(value = "search配置号，多个逗号隔开 如1G-xxx,1A-xxx")
    private String searchOfficeNo;

    @ApiModelProperty(value = "verify配置号，多个逗号隔开如1G-xxx,1A-xxx")
    private String verifyOfficeNo;

    @ApiModelProperty(value = "order配置号，多个逗号隔开如1G-xxx,1A-xxx")
    private String orderOfficeNo;

    @ApiModelProperty(value = "版本控制")
    private Integer version;

    @ApiModelProperty(value = "备注说明")
    private String remark;

    @ApiModelProperty(value = "优先排序序号")
    private Integer sortSeq;

    @ApiModelProperty(value = "0-有效(正常),1-暂停(挂起) 99-无效(删除)")
    private String status;

    @ApiModelProperty(value = "生效日期(开始)")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "失效日期(截止)")
    private LocalDateTime endTime;

}
