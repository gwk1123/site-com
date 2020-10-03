package com.sibecommon.repository.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author gwk
 * @since 2020-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PolicyInfo对象", description="")
public class PolicyInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "平台ota_code")
    private String otaCode;

    @ApiModelProperty(value = "平台站点ota_code")
    private String otaSiteCode;

    @ApiModelProperty(value = "外部政策ID")
    private Integer externalId;

    @ApiModelProperty(value = "文件编号")
    private String fileNumber;

    @ApiModelProperty(value = "可组文件编号")
    private String combinableFileNumber;

    @ApiModelProperty(value = "订位office号")
    private String bookOfficeNo;

    @ApiModelProperty(value = "订位系统")
    private String bookGdsChannel;

    @ApiModelProperty(value = "产品类型")
    private String productType;

    @ApiModelProperty(value = "行程类型")
    private String tripType;

    @ApiModelProperty(value = "航司二字代码只允许录入一家")
    private String airline;

    @ApiModelProperty(value = "出发地")
    private String depCity;

    @ApiModelProperty(value = "目的地")
    private String arrCity;

    @ApiModelProperty(value = "出发地除外")
    private String depCityExcept;

    @ApiModelProperty(value = "目的地除外")
    private String arrCityExcept;

    @ApiModelProperty(value = "适用联运航司")
    private String interlineAirline;

    @ApiModelProperty(value = "除外联运航司")
    private String interlineAirlineExcept;

    @ApiModelProperty(value = "是否适用于中转")
    private Integer permitTransit;

    @ApiModelProperty(value = "指定转机点")
    private String transferPoint;

    @ApiModelProperty(value = "是否适用于代码共享航班")
    private Integer permitCodeShare;

    @ApiModelProperty(value = "是否适用于联运")
    private Integer permitInterline;

    @ApiModelProperty(value = "是否允许缺口")
    private Integer permitOpenJaw;

    @ApiModelProperty(value = "运价渠道")
    private String fareChannel;

    @ApiModelProperty(value = "报销发票类型")
    private String invoiceType;

    @ApiModelProperty(value = "去程旅行开始日期")
    private LocalDate outboundDateStart;

    @ApiModelProperty(value = "去程旅行结束日期")
    private LocalDate outboundDateEnd;

    @ApiModelProperty(value = "回程旅行开始日期")
    private LocalDate inboundDateStart;

    @ApiModelProperty(value = "回程旅行结束日期")
    private LocalDate inboundDateEnd;

    @ApiModelProperty(value = "销售开始日期")
    private LocalDate saleDateStart;

    @ApiModelProperty(value = "销售结束日期")
    private LocalDate saleDateEnd;

    @ApiModelProperty(value = "去程班期")
    private String outboundDayTime;

    @ApiModelProperty(value = "回程班期")
    private String inboundDayTime;

    @ApiModelProperty(value = "去程旅行日期除外")
    private String outboundTravelDateExcept;

    @ApiModelProperty(value = "回程旅行日期除外")
    private String inboundTravelDateExcept;

    @ApiModelProperty(value = "乘客类型")
    private String passengerType;

    @ApiModelProperty(value = "适用舱位等级")
    private String seatGrade;

    @ApiModelProperty(value = "适用舱位")
    private String seatCabin;

    @ApiModelProperty(value = "适用舱位除外")
    private String seatCabinExcept;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "返点")
    private BigDecimal commition;

    @ApiModelProperty(value = "成人票面留钱")
    private BigDecimal adultPrice;

    @ApiModelProperty(value = "成人税费留钱")
    private BigDecimal adultTax;

    @ApiModelProperty(value = "儿童票销售定价")
    private Integer childPriceType;

    @ApiModelProperty(value = "儿童折扣%")
    private Integer childDiscount;

    @ApiModelProperty(value = "出票备注")
    private String ticketRemark;

    @ApiModelProperty(value = "工作时间")
    private String workTime;

    @ApiModelProperty(value = "是否支持1/2RT")
    private Integer rtSupportFlag;

    @ApiModelProperty(value = "1/2RT佣金计算方式")
    private String rtCommissionFormula;

    @ApiModelProperty(value = "出票时限")
    private String ticketDeadline;

    @ApiModelProperty(value = "适用乘客国籍")
    private String nationality;

    @ApiModelProperty(value = "除外乘客国籍")
    private String excludeNationality;

    @ApiModelProperty(value = "是否适用小团价")
    private Integer groupBuyFlag;

    @ApiModelProperty(value = "最小出行人数")
    private Integer minPax;

    @ApiModelProperty(value = "最大出行人数")
    private Integer maxPax;

    @ApiModelProperty(value = "适用运价类型是")
    private String priceType;

    @ApiModelProperty(value = "Fare Basis  第1个版本不做控制")
    private String fareBasis;

    @ApiModelProperty(value = "Fare Type Code 第1个版本不做控制")
    private String fareTypeCode;

    @ApiModelProperty(value = "Tariff  第1个版本不做控制")
    private String tariff;

    @ApiModelProperty(value = "Rule Id 第1个版本不做控制")
    private String ruleId;

    @ApiModelProperty(value = "规则特别说明")
    private String ticketRuleNotes;

    @ApiModelProperty(value = "版本控制")
    private Integer version;

    @ApiModelProperty(value = "备注说明")
    private String remark;

    @ApiModelProperty(value = "行李额来源")
    private Integer baggageType;

    @ApiModelProperty(value = "成人票面价")
    private BigDecimal manualAdultPrice;

    @ApiModelProperty(value = "成人税费")
    private BigDecimal manualAdultTax;

    @ApiModelProperty(value = "禁售航班")
    private String prohibitedFlight;

    @ApiModelProperty(value = "可售航班")
    @TableField("availableFlight")
    private String availableFlight;

    @ApiModelProperty(value = "提前销售天数")
    private Integer advanceSaleDay;

    @ApiModelProperty(value = "手工舱位")
    private String manualSeatCabin;

    @ApiModelProperty(value = "适用共享航司")
    private String codeShareAirline;

    @ApiModelProperty(value = "适用共享航司除外")
    private String codeShareAirlineExcept;

    @ApiModelProperty(value = "最大停留时间（分）")
    private Integer retMaxTime;

    @ApiModelProperty(value = "最小停留时间（分）")
    private Integer retMinTime;

    /**
     * 成人行李额件数
     */
    @TableField(exist = false)
    private String adultBaggagePieces;

    /**
     * 成人行李额重量
     */
    @TableField(exist = false)
    private String adultBaggageWeight;

    /**
     * 儿童行李额件数
     */
    @TableField(exist = false)
    private String childBaggagePieces;

    /**
     * 儿童行李额重量
     */
    @TableField(exist = false)
    private String childBaggageWeight;

    @TableField(exist = false)
    private List<PolicyInfoBaggage> policyInfoBaggages;

}
