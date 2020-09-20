package comm.ota.site;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by yangdehua on 18/2/8.
 */
public class PolicyInfoChange implements Serializable {

    private Long id;

    private Long policyId;

    private Integer passengerType;

    private Integer changeType;

    private String changeStatus;

    private BigDecimal changeFee;

    private String currency;

    private String noshow;

    private Integer noshowCondition;

    private BigDecimal noshowFee;

    private String noshowOperationDealine;

    private String cnRefRemark;

    private String enRefRemark;

    private Integer dataAccuracy;

    private String changeFeeStr;

    private Integer changeFeeMode;

    public Integer getChangeFeeMode() {
        return changeFeeMode;
    }

    public void setChangeFeeMode(Integer changeFeeMode) {
        this.changeFeeMode = changeFeeMode;
    }

    public Integer getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(Integer passengerType) {
        this.passengerType = passengerType;
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

    public Integer getChangeType() {
        return changeType;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public BigDecimal getChangeFee() {
        return changeFee;
    }

    public void setChangeFee(BigDecimal changeFee) {
        this.changeFee = changeFee;
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

    public String getChangeFeeStr() {
        return changeFeeStr;
    }

    public void setChangeFeeStr(String changeFeeStr) {
        this.changeFeeStr = changeFeeStr;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (policyId != null ? policyId.hashCode() : 0);
        result = 31 * result + (passengerType != null ? passengerType.hashCode() : 0);
        result = 31 * result + (changeType != null ? changeType.hashCode() : 0);
        result = 31 * result + (changeStatus != null ? changeStatus.hashCode() : 0);
        result = 31 * result + (noshow != null ? noshow.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PolicyInfoChange{" +
            "id=" + id +
            ", policyId=" + policyId +
            ", passengerType=" + passengerType +
            ", changeType=" + changeType +
            ", changeStatus='" + changeStatus + '\'' +
            ", changeFee=" + changeFee +
            ", currency='" + currency + '\'' +
            ", noshow='" + noshow + '\'' +
            ", noshowCondition=" + noshowCondition +
            ", noshowFee=" + noshowFee +
            ", noshowOperationDealine='" + noshowOperationDealine + '\'' +
            ", cnRefRemark='" + cnRefRemark + '\'' +
            ", enRefRemark='" + enRefRemark + '\'' +
            ", dataAccuracy=" + dataAccuracy +
            ", changeFeeStr='" + changeFeeStr + '\'' +
            '}';
    }
}
