package comm.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 航段奖励(ml_segment_reward_config)
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SegmentRewardConfig对象", description="航段奖励(ml_segment_reward_config) ")
public class SegmentRewardConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "OTA平台编码")
    private String issueGds;

    @ApiModelProperty(value = "奖励金额")
    private BigDecimal rewardAmount;

    @ApiModelProperty(value = "生效日期(开始)")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "失效日期(截止)")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "版本控制")
    private Integer version;

    @ApiModelProperty(value = "备注说明")
    private String remark;

    @ApiModelProperty(value = "优先排序序号")
    private Integer sortSeq;

    @ApiModelProperty(value = "0-有效(正常),1-暂停(挂起) 99-无效(删除)")
    private Boolean status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人ID")
    private Integer createUserId;

    @ApiModelProperty(value = "创建人名称")
    private String createUserName;

    @ApiModelProperty(value = "修改人ID")
    private Integer updateUserId;

    @ApiModelProperty(value = "修改人名称")
    private String updateUserName;


}
