package comm.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 信箱配置
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="QueueLetterBox对象", description="信箱配置")
public class QueueLetterBox implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "出票GDS")
    private String issueGds;

    @ApiModelProperty(value = "出票PCC")
    private String issuePcc;

    @ApiModelProperty(value = "航变通知信箱")
    private String flightChange;

    @ApiModelProperty(value = "出票通知信箱")
    private String issueToNotice;

    @ApiModelProperty(value = "出票成功信箱")
    private String issueSuccess;

    @ApiModelProperty(value = "出票失败信箱")
    private String issueFailed;

    @ApiModelProperty(value = "废票通知信箱")
    private String voidToNotice;

    @ApiModelProperty(value = "废票成功信箱")
    private String voidSuccess;

    @ApiModelProperty(value = "废票失败信箱")
    private String voidFailed;

    @ApiModelProperty(value = "改期信箱")
    private String changeToNotice;

    @ApiModelProperty(value = "退票信箱")
    private String refundToNotice;

    @ApiModelProperty(value = "修改人ID")
    private Long updateUserId;

    @ApiModelProperty(value = "更新时间")
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
