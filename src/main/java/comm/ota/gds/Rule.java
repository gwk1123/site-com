package comm.ota.gds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rule implements Serializable{

    private static final long serialVersionUID = -5264197717146085902L;
    private ArrayList<Baggage> baggageInfoList;
    private ArrayList<Refund> refundInfoList;
    private ArrayList<Changes> revalidationList;
    private ArrayList<Changes> changesInfoList;
    private ServiceFee serviceFee;
    private String note;
    private String tariffNo;
    private String ruleNo;

    public ArrayList<Baggage> getBaggageInfoList() {

        if (baggageInfoList == null) {
            baggageInfoList = new ArrayList<>();
        }
        return this.baggageInfoList;
    }

    public ArrayList<Refund> getRefundInfoList() {
        if (refundInfoList == null) {
            refundInfoList = new ArrayList<>();
        }
        return this.refundInfoList;
    }

    public ArrayList<Changes> getChangesInfoList() {
        if (changesInfoList == null) {
            changesInfoList = new ArrayList<>();
        }
        return this.changesInfoList;
    }

    public ArrayList<Changes> getRevalidationList() {
        if (revalidationList == null) {
            revalidationList = new ArrayList<>();
        }
        return this.revalidationList;
    }


    public void setBaggageInfoList(ArrayList<Baggage> baggageInfoList) {
        this.baggageInfoList = baggageInfoList;
    }

    public void setRefundInfoList(ArrayList<Refund> refundInfoList) {
        this.refundInfoList = refundInfoList;
    }

    public void setRevalidationList(ArrayList<Changes> revalidationList) {
        this.revalidationList = revalidationList;
    }

    public void setChangesInfoList(ArrayList<Changes> changesInfoList) {
        this.changesInfoList = changesInfoList;
    }

    public ServiceFee getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(ServiceFee serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTariffNo() {
        return tariffNo;
    }

    public void setTariffNo(String tariffNo) {
        this.tariffNo = tariffNo;
    }

    public String getRuleNo() {
        return ruleNo;
    }

    public void setRuleNo(String ruleNo) {
        this.ruleNo = ruleNo;
    }
}
