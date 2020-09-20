package comm.ota.site;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by yangdehua on 18/2/8.
 */
public class PolicyInfoRefund implements Serializable {


    private Long id;

    private Long policyId;

    private Integer passengerType;

    private Integer refundType;

    private String refundStatus;

    private BigDecimal refundFee;

    private String currency;

    private String noshow;

    private Integer noshowCondition;

    private BigDecimal noshowFee;

    private String noshowOperationDealine;

    private String cnRefRemark;

    private String enRefRemark;

    private Integer dataAccuracy;

    private String refundFeeStr;

    private Integer refundFeeMode;

    public Integer getRefundFeeMode() {
        return refundFeeMode;
    }

    public void setRefundFeeMode(Integer refundFeeMode) {
        this.refundFeeMode = refundFeeMode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public Integer getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(Integer passengerType) {
        this.passengerType = passengerType;
    }

    public Integer getRefundType() {
        return refundType;
    }

    public void setRefundType(Integer refundType) {
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

    public String getNoshow() {
        return noshow;
    }

    public void setNoshow(String noshow) {
        this.noshow = noshow;
    }

    public Integer getNoshowCondition() {
        return noshowCondition;
    }

    public void setNoshowCondition(Integer noshowCondition) {
        this.noshowCondition = noshowCondition;
    }

    public BigDecimal getNoshowFee() {
        return noshowFee;
    }

    public void setNoshowFee(BigDecimal noshowFee) {
        this.noshowFee = noshowFee;
    }

    public String getNoshowOperationDealine() {
        return noshowOperationDealine;
    }

    public void setNoshowOperationDealine(String noshowOperationDealine) {
        this.noshowOperationDealine = noshowOperationDealine;
    }

    public String getCnRefRemark() {
        return cnRefRemark;
    }

    public void setCnRefRemark(String cnRefRemark) {
        this.cnRefRemark = cnRefRemark;
    }

    public String getEnRefRemark() {
        return enRefRemark;
    }

    public void setEnRefRemark(String enRefRemark) {
        this.enRefRemark = enRefRemark;
    }

    public Integer getDataAccuracy() {
        return dataAccuracy;
    }

    public void setDataAccuracy(Integer dataAccuracy) {
        this.dataAccuracy = dataAccuracy;
    }

    public String getRefundFeeStr() {
        return refundFeeStr;
    }

    public void setRefundFeeStr(String refundFeeStr) {
        this.refundFeeStr = refundFeeStr;
    }


    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (policyId != null ? policyId.hashCode() : 0);
        result = 31 * result + (passengerType != null ? passengerType.hashCode() : 0);
        result = 31 * result + (refundType != null ? refundType.hashCode() : 0);
        result = 31 * result + (refundStatus != null ? refundStatus.hashCode() : 0);
        result = 31 * result + (noshow != null ? noshow.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PolicyInfoRefund{" +
            "id=" + id +
            ", policyId=" + policyId +
            ", passengerType=" + passengerType +
            ", refundType=" + refundType +
            ", refundStatus='" + refundStatus + '\'' +
            ", refundFee=" + refundFee +
            ", currency='" + currency + '\'' +
            ", noshow='" + noshow + '\'' +
            ", noshowCondition=" + noshowCondition +
            ", noshowFee=" + noshowFee +
            ", noshowOperationDealine='" + noshowOperationDealine + '\'' +
            ", cnRefRemark='" + cnRefRemark + '\'' +
            ", enRefRemark='" + enRefRemark + '\'' +
            ", dataAccuracy=" + dataAccuracy +
            ", refundFeeStr='" + refundFeeStr + '\'' +
            '}';
    }
}
