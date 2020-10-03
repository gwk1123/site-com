package com.sibecommon.ota.ctrip.model;


/**
 * Created by yangdehua on 18/3/11.
 */
public class CtripAirlineAncillaries {

    //行李增值服务（true 包含/false不包含，默认false）
    private Boolean baggageService;

    //有无免费行李额（True为无免费行李额；默认False，包含免费行李额，或部分无免费行李额）
    private Boolean unFreeBaggage;

    //值机服务状态 0：不支持在线值机 / 1：支持在线值机 / 2：必须在线值机（此选项代表供应商打包值机价格到机票）
    private Integer checkInServiceStatus;


    /**
     * Gets baggage service.
     *
     * @return the baggage service
     */
    public Boolean getBaggageService() {
        return baggageService;
    }

    /**
     * Sets baggage service.
     *
     * @param baggageService the baggage service
     */
    public void setBaggageService(Boolean baggageService) {
        this.baggageService = baggageService;
    }

    /**
     * Gets un free baggage.
     *
     * @return the un free baggage
     */
    public Boolean getUnFreeBaggage() {
        return unFreeBaggage;
    }

    /**
     * Sets un free baggage.
     *
     * @param unFreeBaggage the un free baggage
     */
    public void setUnFreeBaggage(Boolean unFreeBaggage) {
        this.unFreeBaggage = unFreeBaggage;
    }

    /**
     * Gets check in service status.
     *
     * @return the check in service status
     */
    public Integer getCheckInServiceStatus() {
        return checkInServiceStatus;
    }

    /**
     * Sets check in service status.
     *
     * @param checkInServiceStatus the check in service status
     */
    public void setCheckInServiceStatus(Integer checkInServiceStatus) {
        this.checkInServiceStatus = checkInServiceStatus;
    }
}
