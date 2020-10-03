package com.sibecommon.repository.entity;

import lombok.Data;

@Data
public class CarrierCabinBlack {

    private String carrier;
    private String flightNumber;
    private String cabin;
    private String depTime;
    private String gds;
    private String officeId;
    private Long createTime;
    private Integer channel;// 渠道 1验价 2 生单
}
