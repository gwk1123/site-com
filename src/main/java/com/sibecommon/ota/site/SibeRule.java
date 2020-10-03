package com.sibecommon.ota.site;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangdehua on 18/3/11.
 */
public class SibeRule{

    private List<SibeBaggage> baggageInfoList;

    //格式化行李额规定；
    // 1）查询及验价时，Baggage和FormatBaggage需同时返回，缺一不可
    private List<SibeFormatBaggage> formatBaggageInfoList;

    //退票规定
    // 1）单程和往返格式不同；
    // 2）需要按照乘客类型分别赋值
    private List<SibeRefund> refundInfoList;

    //Revalidation
    private ArrayList<SibeChange> revalidationList;

    //改期(Reissue )
    private List<SibeChange> changesInfoList;

    //备注信息，最大 300 个字符
    private String note;

    //公布运价相关参数，地理区间见运价集群编码
    private String tariffNo;

    //公布运价相关参数
    private String ruleNo;

    //------------需要更新

    //退改签匹配模式：（0准确匹配；1模糊匹配）
    private Integer fareRuleMatchMode;

    //是否使用GDS退改签：（true 使用；false 不使用）
    private Boolean isUseGdsRule;

    /**
     * Gets baggage info list.
     *
     * @return the baggage info list
     */
    public List<SibeBaggage> getBaggageInfoList() {
        if (baggageInfoList == null) {
            baggageInfoList = new ArrayList<>();
        }
        return baggageInfoList;
    }

    /**
     * Sets baggage info list.
     *
     * @param baggageInfoList the baggage info list
     */
    public void setBaggageInfoList(List<SibeBaggage> baggageInfoList) {
        this.baggageInfoList = baggageInfoList;
    }

    /**
     * Gets revalidation list.
     *
     * @return the revalidation list
     */
    public ArrayList<SibeChange> getRevalidationList() {
        if (revalidationList == null) {
            revalidationList = new ArrayList<>();
        }
        return revalidationList;
    }

    /**
     * Sets revalidation list.
     *
     * @param revalidationList the revalidation list
     */
    public void setRevalidationList(ArrayList<SibeChange> revalidationList) {
        this.revalidationList = revalidationList;
    }

    /**
     * Gets format baggage info list.
     *
     * @return the format baggage info list
     */
    public List<SibeFormatBaggage> getFormatBaggageInfoList() {
        if (formatBaggageInfoList == null) {
            formatBaggageInfoList = new ArrayList<>();
        }
        return this.formatBaggageInfoList;
    }

    /**
     * Sets format baggage info list.
     *
     * @param formatBaggageInfoList the format baggage info list
     */
    public void setFormatBaggageInfoList(List<SibeFormatBaggage> formatBaggageInfoList) {
        this.formatBaggageInfoList = formatBaggageInfoList;
    }

    /**
     * Gets refund info list.
     *
     * @return the refund info list
     */
    public List<SibeRefund> getRefundInfoList() {
        if (refundInfoList == null) {
            refundInfoList = new ArrayList<>();
        }
        return this.refundInfoList;
    }

    /**
     * Sets refund info list.
     *
     * @param refundInfoList the refund info list
     */
    public void setRefundInfoList(List<SibeRefund> refundInfoList) {
        this.refundInfoList = refundInfoList;
    }

    /**
     * Gets changes info list.
     *
     * @return the changes info list
     */
    public List<SibeChange> getChangesInfoList() {
        if (changesInfoList == null) {
            changesInfoList = new ArrayList<>();
        }
        return this.changesInfoList;
    }

    /**
     * Sets changes info list.
     *
     * @param changesInfoList the changes info list
     */
    public void setChangesInfoList(List<SibeChange> changesInfoList) {
        this.changesInfoList = changesInfoList;
    }

    /**
     * Gets note.
     *
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets note.
     *
     * @param note the note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Gets use gds rule.
     *
     * @return the use gds rule
     */
    public Boolean getUseGdsRule() {
        return isUseGdsRule;
    }

    /**
     * Sets use gds rule.
     *
     * @param useGdsRule the use gds rule
     */
    public void setUseGdsRule(Boolean useGdsRule) {
        isUseGdsRule = useGdsRule;
    }

    /**
     * Gets tariff no.
     *
     * @return the tariff no
     */
    public String getTariffNo() {
        return tariffNo;
    }

    /**
     * Sets tariff no.
     *
     * @param tariffNo the tariff no
     */
    public void setTariffNo(String tariffNo) {
        this.tariffNo = tariffNo;
    }

    /**
     * Gets rule no.
     *
     * @return the rule no
     */
    public String getRuleNo() {
        return ruleNo;
    }

    /**
     * Sets rule no.
     *
     * @param ruleNo the rule no
     */
    public void setRuleNo(String ruleNo) {
        this.ruleNo = ruleNo;
    }

    /**
     * Gets fare rule match mode.
     *
     * @return the fare rule match mode
     */
    public Integer getFareRuleMatchMode() {
        return fareRuleMatchMode;
    }

    /**
     * Sets fare rule match mode.
     *
     * @param fareRuleMatchMode the fare rule match mode
     */
    public void setFareRuleMatchMode(Integer fareRuleMatchMode) {
        this.fareRuleMatchMode = fareRuleMatchMode;
    }
}
