package com.sibecommon.ota.gds;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by gwk on 10/26/16.
 */
public class ServiceFee implements Serializable{

    private static final long serialVersionUID = 7157884039676490115L;
    private BigDecimal revalidationFeeByCarrier;

    private BigDecimal refundFeeByCarrier;

    private String currency;

    public BigDecimal getRevalidationFeeByCarrier() {
        return revalidationFeeByCarrier;
    }

    public void setRevalidationFeeByCarrier(BigDecimal revalidationFeeByCarrier) {
        this.revalidationFeeByCarrier = revalidationFeeByCarrier;
    }

    public BigDecimal getRefundFeeByCarrier() {
        return refundFeeByCarrier;
    }

    public void setRefundFeeByCarrier(BigDecimal refundFeeByCarrier) {
        this.refundFeeByCarrier = refundFeeByCarrier;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
