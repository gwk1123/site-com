package comm.repository.entity;

import java.util.Objects;

public class ApiCarrierCabinBlackListRedis {
    private String carrier;
    private String flightNumber;
    private String cabin;
    private String depTime;
    private String gds;
    private String officeId;
    private Long createTime;
    private Integer channel;// 渠道 1验价 2 生单

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

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    public String getGds() {
        return gds;
    }

    public void setGds(String gds) {
        this.gds = gds;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(carrier, flightNumber, cabin, depTime, gds, officeId, createTime, channel);
    }
}
