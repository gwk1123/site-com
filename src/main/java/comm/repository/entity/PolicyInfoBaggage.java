package comm.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author gwk
 * @since 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PolicyInfoBaggage对象", description="")
public class PolicyInfoBaggage extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "政策ID（运价ID）")
    private Long policyId;

    @ApiModelProperty(value = "行李额 乘客类型 0 成人/1 儿童/2 婴儿")
    private Integer passengerType;

    @ApiModelProperty(value = "行李额 件数，单位PC")
    private String baggagePieces;

    @ApiModelProperty(value = "行李额 重量，单位KG")
    private String baggageWeight;

    @ApiModelProperty(value = "行李额 中文备注信息")
    private String baggageCn;

    @ApiModelProperty(value = "是否免费(去哪儿，飞猪）")
    private Integer freeFlag;

    @ApiModelProperty(value = "乐观锁版本号")
    private Integer version;

}
