package comm.ota.gds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by yangdehua on 10/26/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Refund implements Serializable{

    private static final long serialVersionUID = 7727767056476971740L;
    private int	refundType;
    private String	refundStatus;
    private BigDecimal refundFee;
    private String	currency;
    private int	passengerType;
    private String	refNoshow;
    private int	refNoShowCondition;
    private BigDecimal	refNoshowFee;
    private String	cnRefRemark;
    private String	enRefRemark;

    public String getEnRefRemark() {
        return enRefRemark;
    }

    public void setEnRefRemark(String enRefRemark) {
        this.enRefRemark = enRefRemark;
    }

    public int getRefundType() {
        return refundType;
    }

    public void setRefundType(int refundType) {
        this.refundType = refundType;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public BigDecimal getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(BigDecimal refundFee) {
        this.refundFee = refundFee;
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

    public String getRefNoshow() {
        return refNoshow;
    }

    public void setRefNoshow(String refNoshow) {
        this.refNoshow = refNoshow;
    }

    public int getRefNoShowCondition() {
        return refNoShowCondition;
    }

    public void setRefNoShowCondition(int refNoShowCondition) {
        this.refNoShowCondition = refNoShowCondition;
    }

    public BigDecimal getRefNoshowFee() {
        return refNoshowFee;
    }

    public void setRefNoshowFee(BigDecimal refNoshowFee) {
        this.refNoshowFee = refNoshowFee;
    }

    public String getCnRefRemark() {
        return cnRefRemark;
    }

    public void setCnRefRemark(String cnRefRemark) {
        this.cnRefRemark = cnRefRemark;
    }


}
