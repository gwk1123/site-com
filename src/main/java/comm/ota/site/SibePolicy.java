package comm.ota.site;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Sibe policy.
 */
public class SibePolicy {
    //政策id
    private Long policyId;

    //行李额来源 1-使用GDS行李额 2-使用指定行李额
    private Integer baggageType; //行李额来源
    private Set<PolicyInfoBaggage> policyInfoBaggages;
    private Set<PolicyInfoChange> policyInfoChanges;
    private Set<PolicyInfoRefund> policyInfoRefunds;

    //销售成人票价
    private Integer policyAdultPriceOTA;

    //销售成人税费
    private Integer policyAdultTaxOTA;

    //销售儿童票价
    private Integer policyChildPriceOTA;

    //销售儿童税费
    private Integer policyChildTaxOTA;

    //返点
    private BigDecimal commition;

    //成人票面留钱
    private BigDecimal adultPricePlus;

    //成人税费留钱
    private BigDecimal adultTaxPlus;

    //儿童票类型
    private Integer childPricePlusType;

    //儿童折扣
    private Integer childPlusDiscount;

    //手工成人票面价
    private BigDecimal manualAdultPrice;

    //手工成人税费
    private BigDecimal manualAdultTax;

    //全局政策id
    private Long globalPolicyId;

    //全局成人票面留钱
    private BigDecimal globalAdultPricePlus;

    //全局成人税费留钱
    private BigDecimal globalAdultTaxPlus;

    //全局成人票面留钱
    private BigDecimal globalChildPricePlus;

    //全局成人税费留钱
    private BigDecimal globalChildTaxPlus;

    //报销发票类型
    private String invoiceType;

    //GDS转OTA汇率
    private BigDecimal gdsToOTARate;

    //GDS转OTA汇率
    private BigDecimal gdsToCNYRate;

    //其他出票说明
    private String otherIssueTicketMsg;

    //产品类型
    private String productType;

    //原始舱位（未修改舱位之前的cabins）
    private String originalCabins;

    //OTA币种
    private String currencyOTA ;
    //GDS币种
    private String currencyGDS ;

    public String getCurrencyOTA() {
        return currencyOTA;
    }

    public void setCurrencyOTA(String currencyOTA) {
        this.currencyOTA = currencyOTA;
    }

    public String getCurrencyGDS() {
        return currencyGDS;
    }

    public void setCurrencyGDS(String currencyGDS) {
        this.currencyGDS = currencyGDS;
    }

    public BigDecimal getGdsToOTARate() {
        return gdsToOTARate;
    }

    public void setGdsToOTARate(BigDecimal gdsToOTARate) {
        this.gdsToOTARate = gdsToOTARate;
    }

    public BigDecimal getGdsToCNYRate() {
        return gdsToCNYRate;
    }

    public void setGdsToCNYRate(BigDecimal gdsToCNYRate) {
        this.gdsToCNYRate = gdsToCNYRate;
    }


    public Set<PolicyInfoBaggage> getPolicyInfoBaggages() {
        if(policyInfoBaggages==null){
            policyInfoBaggages =new HashSet<>();
        }
        return policyInfoBaggages;
    }

    public void setPolicyInfoBaggages(Set<PolicyInfoBaggage> policyInfoBaggages) {
        this.policyInfoBaggages = policyInfoBaggages;
    }

    public Set<PolicyInfoChange> getPolicyInfoChanges() {
        if(policyInfoChanges==null){
            policyInfoChanges =new HashSet<>();
        }
        return policyInfoChanges;
    }

    public void setPolicyInfoChanges(Set<PolicyInfoChange> policyInfoChanges) {
        this.policyInfoChanges = policyInfoChanges;
    }

    public Set<PolicyInfoRefund> getPolicyInfoRefunds() {
        if(policyInfoRefunds==null){
            policyInfoRefunds =new HashSet<>();
        }
        return policyInfoRefunds;
    }

    public void setPolicyInfoRefunds(Set<PolicyInfoRefund> policyInfoRefunds) {
        this.policyInfoRefunds = policyInfoRefunds;
    }

    /**
     * Gets baggage type.
     *
     * @return the baggage type
     */
    public Integer getBaggageType() {
        return baggageType;
    }

    /**
     * Sets baggage type.
     *
     * @param baggageType the baggage type
     */
    public void setBaggageType(Integer baggageType) {
        this.baggageType = baggageType;
    }



    /**
     * Gets invoice type.
     *
     * @return the invoice type
     */
    public String getInvoiceType() {
        return invoiceType;
    }

    /**
     * Sets invoice type.
     *
     * @param invoiceType the invoice type
     */
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    /**
     * Gets commition.
     *
     * @return the commition
     */
    public BigDecimal getCommition() {
        return commition;
    }

    /**
     * Sets commition.
     *
     * @param commition the commition
     */
    public void setCommition(BigDecimal commition) {
        this.commition = commition;
    }

    /**
     * Gets policy id.
     *
     * @return the policy id
     */
    public Long getPolicyId() {
        return policyId;
    }

    /**
     * Sets policy id.
     *
     * @param policyId the policy id
     */
    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public Integer getPolicyAdultPriceOTA() {
        return policyAdultPriceOTA;
    }

    public void setPolicyAdultPriceOTA(Integer policyAdultPriceOTA) {
        this.policyAdultPriceOTA = policyAdultPriceOTA;
    }

    public Integer getPolicyAdultTaxOTA() {
        return policyAdultTaxOTA;
    }

    public void setPolicyAdultTaxOTA(Integer policyAdultTaxOTA) {
        this.policyAdultTaxOTA = policyAdultTaxOTA;
    }

    public Integer getPolicyChildPriceOTA() {
        return policyChildPriceOTA;
    }

    public void setPolicyChildPriceOTA(Integer policyChildPriceOTA) {
        this.policyChildPriceOTA = policyChildPriceOTA;
    }

    public Integer getPolicyChildTaxOTA() {
        return policyChildTaxOTA;
    }

    public void setPolicyChildTaxOTA(Integer policyChildTaxOTA) {
        this.policyChildTaxOTA = policyChildTaxOTA;
    }

    /**
     * Gets global policy id.
     *
     * @return the global policy id
     */
    public Long getGlobalPolicyId() {
        return globalPolicyId;
    }

    /**
     * Sets global policy id.
     *
     * @param globalPolicyId the global policy id
     */
    public void setGlobalPolicyId(Long globalPolicyId) {
        this.globalPolicyId = globalPolicyId;
    }

    /**
     * Gets adult price plus.
     *
     * @return the adult price plus
     */
    public BigDecimal getAdultPricePlus() {
        return adultPricePlus;
    }

    /**
     * Sets adult price plus.
     *
     * @param adultPricePlus the adult price plus
     */
    public void setAdultPricePlus(BigDecimal adultPricePlus) {
        this.adultPricePlus = adultPricePlus;
    }

    /**
     * Gets adult tax plus.
     *
     * @return the adult tax plus
     */
    public BigDecimal getAdultTaxPlus() {
        return adultTaxPlus;
    }

    /**
     * Sets adult tax plus.
     *
     * @param adultTaxPlus the adult tax plus
     */
    public void setAdultTaxPlus(BigDecimal adultTaxPlus) {
        this.adultTaxPlus = adultTaxPlus;
    }

    /**
     * Gets child price plus type.
     *
     * @return the child price plus type
     */
    public Integer getChildPricePlusType() {
        return childPricePlusType;
    }

    /**
     * Sets child price plus type.
     *
     * @param childPricePlusType the child price plus type
     */
    public void setChildPricePlusType(Integer childPricePlusType) {
        this.childPricePlusType = childPricePlusType;
    }

    /**
     * Gets child plus discount.
     *
     * @return the child plus discount
     */
    public Integer getChildPlusDiscount() {
        return childPlusDiscount;
    }

    /**
     * Sets child plus discount.
     *
     * @param childPlusDiscount the child plus discount
     */
    public void setChildPlusDiscount(Integer childPlusDiscount) {
        this.childPlusDiscount = childPlusDiscount;
    }

    /**
     * Gets global adult price plus.
     *
     * @return the global adult price plus
     */
    public BigDecimal getGlobalAdultPricePlus() {
        return globalAdultPricePlus;
    }

    /**
     * Sets global adult price plus.
     *
     * @param globalAdultPricePlus the global adult price plus
     */
    public void setGlobalAdultPricePlus(BigDecimal globalAdultPricePlus) {
        this.globalAdultPricePlus = globalAdultPricePlus;
    }

    /**
     * Gets global adult tax plus.
     *
     * @return the global adult tax plus
     */
    public BigDecimal getGlobalAdultTaxPlus() {
        return globalAdultTaxPlus;
    }

    /**
     * Sets global adult tax plus.
     *
     * @param globalAdultTaxPlus the global adult tax plus
     */
    public void setGlobalAdultTaxPlus(BigDecimal globalAdultTaxPlus) {
        this.globalAdultTaxPlus = globalAdultTaxPlus;
    }

    /**
     * Gets global child price plus.
     *
     * @return the global child price plus
     */
    public BigDecimal getGlobalChildPricePlus() {
        return globalChildPricePlus;
    }

    /**
     * Sets global child price plus.
     *
     * @param globalChildPricePlus the global child price plus
     */
    public void setGlobalChildPricePlus(BigDecimal globalChildPricePlus) {
        this.globalChildPricePlus = globalChildPricePlus;
    }

    /**
     * Gets global child tax plus.
     *
     * @return the global child tax plus
     */
    public BigDecimal getGlobalChildTaxPlus() {
        return globalChildTaxPlus;
    }

    /**
     * Sets global child tax plus.
     *
     * @param globalChildTaxPlus the global child tax plus
     */
    public void setGlobalChildTaxPlus(BigDecimal globalChildTaxPlus) {
        this.globalChildTaxPlus = globalChildTaxPlus;
    }

    /**
     * Gets other issue ticket msg.
     *
     * @return the other issue ticket msg
     */
    public String getOtherIssueTicketMsg() {
        return otherIssueTicketMsg;
    }

    /**
     * Sets other issue ticket msg.
     *
     * @param otherIssueTicketMsg the other issue ticket msg
     */
    public void setOtherIssueTicketMsg(String otherIssueTicketMsg) {
        this.otherIssueTicketMsg = otherIssueTicketMsg;
    }

    public BigDecimal getManualAdultPrice() {
        return manualAdultPrice;
    }

    public void setManualAdultPrice(BigDecimal manualAdultPrice) {
        this.manualAdultPrice = manualAdultPrice;
    }

    public BigDecimal getManualAdultTax() {
        return manualAdultTax;
    }

    public void setManualAdultTax(BigDecimal manualAdultTax) {
        this.manualAdultTax = manualAdultTax;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getOriginalCabins() {
        return originalCabins;
    }

    public void setOriginalCabins(String originalCabins) {
        this.originalCabins = originalCabins;
    }
}
