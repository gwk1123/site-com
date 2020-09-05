package comm.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * GDS信息
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Gds对象", description="GDS信息")
public class Gds extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "GDS Code IATA标准2字代码")
    private String gdsCode;

    @ApiModelProperty(value = "GDS 中文名")
    private String gdsCname;

    @ApiModelProperty(value = "GDS 英文名")
    private String gdsEname;

    @ApiModelProperty(value = "GDS 中文全名")
    private String gdsFullCname;

    @ApiModelProperty(value = "GDS 英文全名")
    private String gdsFullEname;

    @ApiModelProperty(value = "GDS 类型 参考数据字典“GDS_TYPE”类型 ")
    private String gdsType;

    @ApiModelProperty(value = "版本控制")
    private Integer version;

    @ApiModelProperty(value = "备注说明")
    private String remark;

    @ApiModelProperty(value = "优先排序序号")
    private Integer sortSeq;

    @ApiModelProperty(value = "生效日期(开始)")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "失效日期(截止)")
    private LocalDateTime endTime;


}
