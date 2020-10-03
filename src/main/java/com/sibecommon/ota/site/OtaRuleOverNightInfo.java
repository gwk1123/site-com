package com.sibecommon.ota.site;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * The type Ota rule over night info.
 *  OTA规则-过夜票面留钱配置
 */
public class OtaRuleOverNightInfo {
    private LocalTime start;//开始时间段
    private LocalTime end; //结束时间段
    private BigDecimal price; //过夜票面留钱

    /**
     * Gets start.
     *
     * @return the start
     */
    public LocalTime getStart() {
        return start;
    }

    /**
     * Sets start.
     *
     * @param start the start
     */
    public void setStart(LocalTime start) {
        this.start = start;
    }

    /**
     * Gets end.
     *
     * @return the end
     */
    public LocalTime getEnd() {
        return end;
    }

    /**
     * Sets end.
     *
     * @param end the end
     */
    public void setEnd(LocalTime end) {
        this.end = end;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
