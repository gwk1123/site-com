package com.sibecommon.ota.gds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by yangdehua on 10/26/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Segment  implements Serializable {

    private static final long serialVersionUID = 5457062653931427672L;
    private String carrier;
    private String flightNumber;
    private String depAirport;
    private String depTerminal;
    private String depTime;
    private String arrAirport;
    private String arrTerminal;
    private String arrTime;
    private String StopCities;
    private String stopAirports;
    private Boolean codeShare;
    private String operatingCarrier;
    private String operatingFlightNo;
    private String cabin;
    private String cabinGrade;
    private String aircraftCode;
    private int duration;
    private String bookingClassAvail;
    private String flightIndicator;
    private int itemNumber;
    private String segmentKey;
    private String depZone;
    private String arrZone;
    private String availabilitySource;
    private Long distance;
    private Integer masterSegmentSign; //主航段标志

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepAirport() {
        return depAirport;
    }

    public void setDepAirport(String depAirport) {
        this.depAirport = depAirport;
    }

    public String getDepTerminal() {
        return depTerminal;
    }

    public void setDepTerminal(String depTerminal) {
        this.depTerminal = depTerminal;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    public String getArrAirport() {
        return arrAirport;
    }

    public void setArrAirport(String arrAirport) {
        this.arrAirport = arrAirport;
    }

    public String getArrTerminal() {
        return arrTerminal;
    }

    public void setArrTerminal(String arrTerminal) {
        this.arrTerminal = arrTerminal;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public String getStopCities() {
        return StopCities;
    }

    public void setStopCities(String stopCities) {
        StopCities = stopCities;
    }

    public String getStopAirports() {
        return stopAirports;
    }

    public void setStopAirports(String stopAirports) {
        this.stopAirports = stopAirports;
    }

    public Boolean getCodeShare() {
        return codeShare;
    }

    public void setCodeShare(Boolean codeShare) {
        this.codeShare = codeShare;
    }

    public String getOperatingCarrier() {
        return operatingCarrier;
    }

    public void setOperatingCarrier(String operatingCarrier) {
        this.operatingCarrier = operatingCarrier;
    }

    public String getOperatingFlightNo() {
        return operatingFlightNo;
    }

    public void setOperatingFlightNo(String operatingFlightNo) {
        this.operatingFlightNo = operatingFlightNo;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public String getCabinGrade() {
        return cabinGrade;
    }

    public void setCabinGrade(String cabinGrade) {
        this.cabinGrade = cabinGrade;
    }

    public String getAircraftCode() {
        return aircraftCode;
    }

    public void setAircraftCode(String aircraftCode) {
        this.aircraftCode = aircraftCode;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getBookingClassAvail() {
        return bookingClassAvail;
    }

    public void setBookingClassAvail(String bookingClassAvail) {
        this.bookingClassAvail = bookingClassAvail;
    }

    public String getFlightIndicator() {
        return flightIndicator;
    }

    public void setFlightIndicator(String flightIndicator) {
        this.flightIndicator = flightIndicator;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getSegmentKey() {
        return segmentKey;
    }

    public void setSegmentKey(String segmentKey) {
        this.segmentKey = segmentKey;
    }

    public String getDepZone() {
        return depZone;
    }

    public void setDepZone(String depZone) {
        this.depZone = depZone;
    }

    public String getArrZone() {
        return arrZone;
    }

    public void setArrZone(String arrZone) {
        this.arrZone = arrZone;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAvailabilitySource() {
        return availabilitySource;
    }

    public void setAvailabilitySource(String availabilitySource) {
        this.availabilitySource = availabilitySource;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Integer getMasterSegmentSign() {
        return masterSegmentSign;
    }

    public void setMasterSegmentSign(Integer masterSegmentSign) {
        this.masterSegmentSign = masterSegmentSign;
    }
}
