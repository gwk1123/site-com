package comm.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 乘客信息表
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OrderPassenger对象", description="乘客信息表")
public class OrderPassenger implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关联键(bookinfo)")
    private Integer bookInfoId;

    @ApiModelProperty(value = "姓名")
    private String passengerName;

    @ApiModelProperty(value = "乘客性别")
    private String gender;

    @ApiModelProperty(value = "证件发行国家")
    private String certIssueCountry;

    @ApiModelProperty(value = "证件类型：PP - 护照;GA - 港澳通行证;TW - 台湾通行证 ;TB - 台胞证 ;HX -回乡证;HY- 国际海员")
    private String certType;

    @ApiModelProperty(value = "证件号码")
    private String certNo;

    @ApiModelProperty(value = "证件有效时间")
    private LocalDate certPeriod;

    @ApiModelProperty(value = "生日")
    private String birthday;

    @ApiModelProperty(value = "乘客类型，0 成人/1 儿童/2 婴儿")
    private String passengerType;

    @ApiModelProperty(value = "乘客电话号码")
    private String passengerMobile;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


}
