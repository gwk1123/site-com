package com.sibecommon.ota.gds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by yangdehua on 10/26/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Changes implements Serializable{

    private static final long serialVersionUID = -3014482520357553884L;
    private int	changesType;
    private String	changesStatus;
    private BigDecimal changesFee;
    private String	currency;
    private int	passengerType;
    private String	revNoshow;
    private int	revNoShowCondition;
    private BigDecimal	revNoshowFee;
    private String	cnRevRemark;
    private String	enRevRemark;

    public int getChangesType() {
        return changesType;
    }

    public void setChangesType(int changesType) {
        this.changesType = changesType;
    }

    public String getChangesStatus() {
        return changesStatus;
    }

    public void setChangesStatus(String changesStatus) {
        this.changesStatus = changesStatus;
    }

    public BigDecimal getChangesFee() {
        return changesFee;
    }

    public void setChangesFee(BigDecimal changesFee) {
        this.changesFee = changesFee;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(int passengerType) {
        this.passengerType = passengerType;
    }

    public String getRevNoshow() {
        return revNoshow;
    }

    public void setRevNoshow(String revNoshow) {
        this.revNoshow = revNoshow;
    }

    public int getRevNoShowCondition() {
        return revNoShowCondition;
    }

    public void setRevNoShowCondition(int revNoShowCondition) {
        this.revNoShowCondition = revNoShowCondition;
    }

    public BigDecimal getRevNoshowFee() {
        return revNoshowFee;
    }

    public void setRevNoshowFee(BigDecimal revNoshowFee) {
        this.revNoshowFee = revNoshowFee;
    }

    public String getCnRevRemark() {
        return cnRevRemark;
    }

    public void setCnRevRemark(String cnRevRemark) {
        this.cnRevRemark = cnRevRemark;
    }

    public String getEnRevRemark() {
        return enRevRemark;
    }

    public void setEnRevRemark(String enRevRemark) {
        this.enRevRemark = enRevRemark;
    }
}
