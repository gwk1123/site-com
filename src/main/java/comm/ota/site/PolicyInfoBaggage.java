package comm.ota.site;

import java.io.Serializable;

/**
 * Created by yangdehua on 18/2/8.
 */
public class PolicyInfoBaggage implements Serializable {

    private Long id;

    private Long policyId;

    private Integer passengerType;

    private Integer baggagePiece;

    private Integer baggageWeight;

    private String baggageCn;

    private String baggageEn;

    private Integer freeFlag;

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

    public Integer getBaggagePiece() {
        return baggagePiece;
    }

    public void setBaggagePiece(Integer baggagePiece) {
        this.baggagePiece = baggagePiece;
    }

    public Integer getBaggageWeight() {
        return baggageWeight;
    }

    public void setBaggageWeight(Integer baggageWeight) {
        this.baggageWeight = baggageWeight;
    }

    public String getBaggageCn() {
        return baggageCn;
    }

    public void setBaggageCn(String baggageCn) {
        this.baggageCn = baggageCn;
    }

    public String getBaggageEn() {
        return baggageEn;
    }

    public void setBaggageEn(String baggageEn) {
        this.baggageEn = baggageEn;
    }

    public Integer getFreeFlag() {
        return freeFlag;
    }

    public void setFreeFlag(Integer freeFlag) {
        this.freeFlag = freeFlag;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (policyId != null ? policyId.hashCode() : 0);
        result = 31 * result + (passengerType != null ? passengerType.hashCode() : 0);
        result = 31 * result + (baggagePiece != null ? baggagePiece.hashCode() : 0);
        result = 31 * result + (baggageWeight != null ? baggageWeight.hashCode() : 0);
        result = 31 * result + (baggageCn != null ? baggageCn.hashCode() : 0);
        result = 31 * result + (baggageEn != null ? baggageEn.hashCode() : 0);
        result = 31 * result + (freeFlag != null ? freeFlag.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PolicyInfoBaggage{" +
            "id=" + id +
            ", policyId=" + policyId +
            ", passengerType=" + passengerType +
            ", baggagePiece=" + baggagePiece +
            ", baggageWeight=" + baggageWeight +
            ", baggageCn='" + baggageCn + '\'' +
            ", baggageEn='" + baggageEn + '\'' +
            ", freeFlag=" + freeFlag +
            '}';
    }
}
