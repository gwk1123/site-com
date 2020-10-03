package com.sibecommon.ota.ctrip.model;


import java.math.BigDecimal;

/**
 * Created by yangdehua on 18/3/11.
 */
public class CtripRefund {

    // 退票类型
    // 0：客票全部未使用；
    // 1：客票部分使用【即去程已使用，在往返行程中使用，代表回程的退票信息】
    // 【单程时0；往返时0、1都要有】
    private Integer refundType;

    //退票标识
    // T：不可退
    // H：有条件退
    // F：免费退
    // E：按航司客规【公布运价专用】
    private String refundStatus;

    //退票费
    // 1）当refundStatus =H,必须赋值；
    // 2）若refundStatus =T/F,此字段可不赋值。
    private BigDecimal refundFee;

    // 退票费币种 当refundStatus =H，必须赋值。
    private String currency;

    //乘客类型，0 成人/1 儿童/2 婴儿
    // 1）对于多乘客类型的查询、验价，必须按乘客类型返回；如成人+儿童的查询，成人和儿童的退改签都要有。
    private Integer passengerType;

    //是否允许NoShow退票 T：不可退； H：有条件退；F：免费退；E：按航司客规为准【公布运价专用】
    private String refNoshow;

    //退票时航班起飞前多久算NoShow，单位：小时 1）若无法确认此时间，请默认赋0。
    private Integer refNoShowCondition;

    //NoShow退票费用 1）当IsRefNoshow =H，必须赋值；2）展示给客人的noshow退票费= refundFee+ refNoshowFee。
    private BigDecimal refNoshowFee;

    //中文退票备注
    private String cnRefRemark;

    //英文退票备注
    private String enRefRemark;

    /**
     * Gets refund type.
     *
     * @return the refund type
     */
    public Integer getRefundType() {
        return refundType;
    }

    /**
     * Sets refund type.
     *
     * @param refundType the refund type
     */
    public void setRefundType(Integer refundType) {
        this.refundType = refundType;
    }

    /**
     * Gets refund status.
     *
     * @return the refund status
     */
    public String getRefundStatus() {
        return refundStatus;
    }

    /**
     * Sets refund status.
     *
     * @param refundStatus the refund status
     */
    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    /**
     * Gets refund fee.
     *
     * @return the refund fee
     */
    public BigDecimal getRefundFee() {
        return refundFee;
    }

    /**
     * Sets refund fee.
     *
     * @param refundFee the refund fee
     */
    public void setRefundFee(BigDecimal refundFee) {
        this.refundFee = refundFee;
    }

    /**
     * Gets currency.
     *
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets currency.
     *
     * @param currency the currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Gets passenger type.
     *
     * @return the passenger type
     */
    public Integer getPassengerType() {
        return passengerType;
    }

    /**
     * Sets passenger type.
     *
     * @param passengerType the passenger type
     */
    public void setPassengerType(Integer passengerType) {
        this.passengerType = passengerType;
    }

    /**
     * Gets ref noshow.
     *
     * @return the ref noshow
     */
    public String getRefNoshow() {
        return refNoshow;
    }

    /**
     * Sets ref noshow.
     *
     * @param refNoshow the ref noshow
     */
    public void setRefNoshow(String refNoshow) {
        this.refNoshow = refNoshow;
    }

    /**
     * Gets ref no show condition.
     *
     * @return the ref no show condition
     */
    public Integer getRefNoShowCondition() {
        return refNoShowCondition;
    }

    /**
     * Sets ref no show condition.
     *
     * @param refNoShowCondition the ref no show condition
     */
    public void setRefNoShowCondition(Integer refNoShowCondition) {
        this.refNoShowCondition = refNoShowCondition;
    }

    /**
     * Gets ref noshow fee.
     *
     * @return the ref noshow fee
     */
    public BigDecimal getRefNoshowFee() {
        return refNoshowFee;
    }

    /**
     * Sets ref noshow fee.
     *
     * @param refNoshowFee the ref noshow fee
     */
    public void setRefNoshowFee(BigDecimal refNoshowFee) {
        this.refNoshowFee = refNoshowFee;
    }

    /**
     * Gets cn ref remark.
     *
     * @return the cn ref remark
     */
    public String getCnRefRemark() {
        return cnRefRemark;
    }

    /**
     * Sets cn ref remark.
     *
     * @param cnRefRemark the cn ref remark
     */
    public void setCnRefRemark(String cnRefRemark) {
        this.cnRefRemark = cnRefRemark;
    }

    /**
     * Gets en ref remark.
     *
     * @return the en ref remark
     */
    public String getEnRefRemark() {
        return enRefRemark;
    }

    /**
     * Sets en ref remark.
     *
     * @param enRefRemark the en ref remark
     */
    public void setEnRefRemark(String enRefRemark) {
        this.enRefRemark = enRefRemark;
    }
}
