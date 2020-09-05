package comm.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 航段奖励规则配置
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SegmentRewardRule对象", description="航段奖励规则配置")
public class SegmentRewardRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "出票gds")
    private String issueGds;

    @ApiModelProperty(value = "出票PCC")
    private String issuePcc;

    @ApiModelProperty(value = "航司 7C,3K#,QF,TG#,PG #代表内陆航段")
    private String airline;

    @ApiModelProperty(value = "站点编号 多个逗号隔开")
    private String otaCode;

    @ApiModelProperty(value = "出发地多个逗号隔开")
    private String deptCity;

    @ApiModelProperty(value = "目的地多个逗号隔开")
    private String arrCity;

    @ApiModelProperty(value = "版本控制")
    private Integer version;

    @ApiModelProperty(value = "备注说明")
    private String remark;

    @ApiModelProperty(value = "0-有效(正常),1-暂停(挂起) 99-无效(删除)")
    private Integer status;

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
