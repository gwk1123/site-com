package comm.ota.ctrip.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangdehua on 18/3/11.
 */
public class CtripRule {

    //格式化行李额规定；参考下方FormatBaggage Element 1）查询及验价时，Baggage和FormatBaggage需同时返回，缺一不可
    private List<CtripFormatBaggage> formatBaggageInfoList;

    //退票规定；参考下面的Refund Element 1）单程和往返格式不同；2）需要按照乘客类型分别赋值
    private List<CtripRefund> refundInfoList;

    //改期规定；参考下面的Changes Element 已弃用】服务费规定；参考下面serviceFee Element
    private List<CtripChange> changesInfoList;

    //备注信息，最大 300 个字符
    private String note;

    //是否要使用携程退改签：（true 使用；false 不使用）
    private Boolean isUseCtripRule;

    //公布运价相关参数，地理区间见运价集群编码
    private String tariffNo;

    //公布运价相关参数
    private String ruleNo;

    //退改签匹配模式：（0准确匹配；1模糊匹配）
    private Integer fareRuleMatchMode;

    /**
     * Gets format baggage info list.
     *
     * @return the format baggage info list
     */
    public List<CtripFormatBaggage> getFormatBaggageInfoList() {
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
    public void setFormatBaggageInfoList(List<CtripFormatBaggage> formatBaggageInfoList) {
        this.formatBaggageInfoList = formatBaggageInfoList;
    }

    /**
     * Gets refund info list.
     *
     * @return the refund info list
     */
    public List<CtripRefund> getRefundInfoList() {
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
    public void setRefundInfoList(List<CtripRefund> refundInfoList) {
        this.refundInfoList = refundInfoList;
    }

    /**
     * Gets changes info list.
     *
     * @return the changes info list
     */
    public List<CtripChange> getChangesInfoList() {
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
    public void setChangesInfoList(List<CtripChange> changesInfoList) {
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

    public Boolean getUseCtripRule() {
        return isUseCtripRule;
    }

    public void setUseCtripRule(Boolean useCtripRule) {
        isUseCtripRule = useCtripRule;
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
