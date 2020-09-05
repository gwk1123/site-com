package comm.repository.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 供应商信息(ml_supplier)
 * </p>
 *
 * @author liuyc
 * @since 2020-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Supplier对象", description="供应商信息(ml_supplier) ")
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "供应商 ")
    private String supplierCode;

    @ApiModelProperty(value = "供应商 中文名")
    private String supplierCname;

    @ApiModelProperty(value = "供应商 英文名")
    private String supplierEname;

    @ApiModelProperty(value = "供应商中文全名")
    private String supplierFullCname;

    @ApiModelProperty(value = "供应商英文全名")
    private String supplierFullEname;

    @ApiModelProperty(value = "供应商类型（B2B,供应商，平台外采）")
    private String supplierType;

    @ApiModelProperty(value = "结算币种 ref table sibe_currency")
    private String settlementCurrency;

    @ApiModelProperty(value = "国家Code,2字码 如 CN/US/KR ref table base_country.country_code")
    private String countryCode;

    @ApiModelProperty(value = "城市Code 3字码 如 SHA/NYC/SEL ref table base_city.city_code")
    private String cityCode;

    @ApiModelProperty(value = "供应商级别 ref table system_com_type_det")
    private String grade;

    @ApiModelProperty(value = "版本控制")
    private Integer version;

    @ApiModelProperty(value = "备注说明")
    private String remark;

    @ApiModelProperty(value = "优先排序序号")
    private Integer sortSeq;

    @ApiModelProperty(value = "状态：0-无效(删除),1-有效(正常 解挂),2-暂停（挂起）")
    private Boolean status;

    @ApiModelProperty(value = "生效日期(开始)")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "失效日期(截止)")
    private LocalDateTime endTime;

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
