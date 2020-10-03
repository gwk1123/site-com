package com.sibecommon.ota.site;

import java.math.BigDecimal;

/**
 * Created by yangdehua on 18/3/12.
 */
public class SibeChange {


    //改期类型
    // 0：客票全部未使用；
    // 1：客票部分使用【即去程已使用，在往返行程中使用，代表回程的退票信息】
    // 【单程时0；往返时0、1都要有】
    private Integer changesType;

    //改期标识
    // T：不可改期
    // H：有条件改期
    // F：免费改期
    // E：按航司客规【公布运价专用】
    private String changesStatus;

    //退票费
    // 1）当changesStatus =H,必须赋值；
    // 2）若changesStatus =T/F,此字段可不赋值。
    private BigDecimal changesFee;

    // 退票费币种
    // 当refundStatus =H，必须赋值。

    private String currency;

    //乘客类型，0 成人/1 儿童/2 婴儿
    // 1）对于对乘客类型的查询、验价，必须按乘客类型返回；如成人+儿童的查询，成人和儿童的退改签都要有。
    private Integer passengerType;

    //是否允许NoShow改期，T：不可退/改，H：有条件退/改，F：免费退/改,E：按航司客规为准，
    private String revNoshow;

    //改期时航班起飞前多久算NoShow，单位：小时
    // 1）若无法确认此时间，请默认赋0。
    private Integer revNoShowCondition;

    //NoShow改期费用
    // 1）当revNoshow =H，必须赋值；
    // 2）展示给客人的noshow改期费= changesFee + revNoshowFee。
    private BigDecimal revNoshowFee;

    //中文改期备注
    private String cnRevRemark;

    //英文改期备注
    private String enRevRemark;


    private String noshowOperationDealine;

    private String changeFeeStr;

    private Integer changeFeeMode;

    public Integer getChangeFeeMode() {
        return changeFeeMode;
    }

    public void setChangeFeeMode(Integer changeFeeMode) {
        this.changeFeeMode = changeFeeMode;
    }

    /**
     * Gets noshow operation dealine.
     *
     * @return the noshow operation dealine
     */
    public String getNoshowOperationDealine() {
        return noshowOperationDealine;
    }

    /**
     * Sets noshow operation dealine.
     *
     * @param noshowOperationDealine the noshow operation dealine
     */
    public void setNoshowOperationDealine(String noshowOperationDealine) {
        this.noshowOperationDealine = noshowOperationDealine;
    }

    /**
     * Gets change fee str.
     *
     * @return the change fee str
     */
    public String getChangeFeeStr() {
        return changeFeeStr;
    }

    /**
     * Sets change fee str.
     *
     * @param changeFeeStr the change fee str
     */
    public void setChangeFeeStr(String changeFeeStr) {
        this.changeFeeStr = changeFeeStr;
    }

    /**
     * Gets changes type.
     *
     * @return the changes type
     */
    public Integer getChangesType() {
        return changesType;
    }

    /**
     * Sets changes type.
     *
     * @param changesType the changes type
     */
    public void setChangesType(Integer changesType) {
        this.changesType = changesType;
    }

    /**
     * Gets changes status.
     *
     * @return the changes status
     */
    public String getChangesStatus() {
        return changesStatus;
    }

    /**
     * Sets changes status.
     *
     * @param changesStatus the changes status
     */
    public void setChangesStatus(String changesStatus) {
        this.changesStatus = changesStatus;
    }

    /**
     * Gets changes fee.
     *
     * @return the changes fee
     */
    public BigDecimal getChangesFee() {
        return changesFee;
    }

    /**
     * Sets changes fee.
     *
     * @param changesFee the changes fee
     */
    public void setChangesFee(BigDecimal changesFee) {
        this.changesFee = changesFee;
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
     * Gets rev noshow.
     *
     * @return the rev noshow
     */
    public String getRevNoshow() {
        return revNoshow;
    }

    /**
     * Sets rev noshow.
     *
     * @param revNoshow the rev noshow
     */
    public void setRevNoshow(String revNoshow) {
        this.revNoshow = revNoshow;
    }

    /**
     * Gets rev no show condition.
     *
     * @return the rev no show condition
     */
    public Integer getRevNoShowCondition() {
        return revNoShowCondition;
    }

    /**
     * Sets rev no show condition.
     *
     * @param revNoShowCondition the rev no show condition
     */
    public void setRevNoShowCondition(Integer revNoShowCondition) {
        this.revNoShowCondition = revNoShowCondition;
    }

    /**
     * Gets rev noshow fee.
     *
     * @return the rev noshow fee
     */
    public BigDecimal getRevNoshowFee() {
        return revNoshowFee;
    }

    /**
     * Sets rev noshow fee.
     *
     * @param revNoshowFee the rev noshow fee
     */
    public void setRevNoshowFee(BigDecimal revNoshowFee) {
        this.revNoshowFee = revNoshowFee;
    }

    /**
     * Gets cn rev remark.
     *
     * @return the cn rev remark
     */
    public String getCnRevRemark() {
        return cnRevRemark;
    }

    /**
     * Sets cn rev remark.
     *
     * @param cnRevRemark the cn rev remark
     */
    public void setCnRevRemark(String cnRevRemark) {
        this.cnRevRemark = cnRevRemark;
    }

    /**
     * Gets en rev remark.
     *
     * @return the en rev remark
     */
    public String getEnRevRemark() {
        return enRevRemark;
    }

    /**
     * Sets en rev remark.
     *
     * @param enRevRemark the en rev remark
     */
    public void setEnRevRemark(String enRevRemark) {
        this.enRevRemark = enRevRemark;
    }
}
