package com.sibecommon.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * data信息表
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OrderData对象", description="data信息表")
public class OrderData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关联键(bookinfo)")
    private Integer bookInfoId;

    @ApiModelProperty(value = "data内容")
    private String contentInfo;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


}
