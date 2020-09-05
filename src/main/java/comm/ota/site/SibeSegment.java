package comm.ota.site;

/**
 * The type Sibe segment.
 */
public class SibeSegment{

    //航司 IATA 二字码，必须与 flightNumber 航司相同
    private String  carrier;

    // 航班号，如：CA123。航班号数字前若有多余的数字 0，必须去掉；如 CZ006 需返回 CZ6
    private String  flightNumber;

    // 出发机场；IATA 三字码
    private String  depAirport;

    // 出发航站楼；使用简写，例如T1
    private String depTerminal;

    // 起飞日期时间，格式：YYYYMMDDHHMM  例：201203100300 表示 2012 年 3 月 10 日 3 时 0 分
    private String  depTime;

    // 到达机场 IATA 三字码
    private String  arrAirport;

    // 抵达航站楼，使用简写，例如T1
    private String arrTerminal;

    // 到达日期时间，格式：YYYYMMDDHHMM  例：201203101305 表示 2012 年 3 月 10 日 13 时 5 分
    private String  arrTime;

    // 经停城市； IATA 三字码
    private String stopCities;

    // 经停机场； IATA 三字码
    private String  stopAirports;

    // 代码共享标识（true 代码共享/false 非代码共享）
    private Boolean  codeShare;

    // 实际承运航司 若codeShare=true， operatingCarrier不能为空。
    private String operatingCarrier;

    // 实际承运航班号
    private String operatingFlightNo;

    // 子舱位
    private String  cabin;

    private String  originalCabin;


    /**
     * 舱位等级:
     * Y: 经济舱-Economy Class;
     * W: 经济特舱-Economy Class Premium;
     * M: 经济优惠舱-Economy Class Discounted
     * F: 头等舱-First Class;
     * C: 商务舱-Business Class;
     **/
    private String cabinGrade;

    // 机型 ，IATA标准3字码,并过滤下列机型运价信息BUS|ICE|LCH|LMO|MTL|RFS|TGV|THS|THT|TRN|TSL|
    private String  aircraftCode;

    // 飞行时长；单位为分钟，通过时差转换后的结果。
    private Integer duration;

    //当前航班所有舱位座位数列表
    //J4|C0|D0|Z0|Y4|B4|M4|U4|H4|Q4|V4|W4|S1|T0|L0|G0|E0|R1
    private String bookingClassAvail;

    //航段的Farebasis
    private String fareBasis;

    //舱位数
    private String seatCount;

    //指示当前是第几航程
    private Integer flightIndicator;

    //指示当前是第几航段
    private Integer itemNumber;

    //（Galileo预留字段）验价与生单都必须传
    private String segmentKey;

    //起飞地时区(例如 +08:00，-05:00) Galileo预留字段
    private String depZone;

    //抵达地时区(例如 +08:00，-05:00) Galileo预留字段
    private String arrZone;

    //Galileo缓存来源 Galileo预留字段
    private String availabilitySource;

    //航段预定状态  航段的预定状态是否"HK,KL,KK,TK"中之一
    private String  orderStatus;


    /**
     * Gets order status.
     *
     * @return the order status
     */
    public String getOrderStatus() {
        return orderStatus;
    }

    /**
     * Sets order status.
     *
     * @param orderStatus the order status
     */
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * Gets fare basis.
     *
     * @return the fare basis
     */
    public String getFareBasis() {
        return fareBasis;
    }

    /**
     * Sets fare basis.
     *
     * @param fareBasis the fare basis
     */
    public void setFareBasis(String fareBasis) {
        this.fareBasis = fareBasis;
    }

    /**
     * Gets carrier.
     *
     * @return the carrier
     */
    public String getCarrier() {
        return carrier;
    }

    /**
     * Sets carrier.
     *
     * @param carrier the carrier
     */
    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    /**
     * Gets flight number.
     *
     * @return the flight number
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * Sets flight number.
     *
     * @param flightNumber the flight number
     */
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    /**
     * Gets dep airport.
     *
     * @return the dep airport
     */
    public String getDepAirport() {
        return depAirport;
    }

    /**
     * Sets dep airport.
     *
     * @param depAirport the dep airport
     */
    public void setDepAirport(String depAirport) {
        this.depAirport = depAirport;
    }

    /**
     * Gets dep terminal.
     *
     * @return the dep terminal
     */
    public String getDepTerminal() {
        return depTerminal;
    }

    /**
     * Sets dep terminal.
     *
     * @param depTerminal the dep terminal
     */
    public void setDepTerminal(String depTerminal) {
        this.depTerminal = depTerminal;
    }

    /**
     * Gets dep time.
     *
     * @return the dep time
     */
    public String getDepTime() {
        return depTime;
    }

    /**
     * Sets dep time.
     *
     * @param depTime the dep time
     */
    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    /**
     * Gets arr airport.
     *
     * @return the arr airport
     */
    public String getArrAirport() {
        return arrAirport;
    }

    /**
     * Sets arr airport.
     *
     * @param arrAirport the arr airport
     */
    public void setArrAirport(String arrAirport) {
        this.arrAirport = arrAirport;
    }

    /**
     * Gets arr terminal.
     *
     * @return the arr terminal
     */
    public String getArrTerminal() {
        return arrTerminal;
    }

    /**
     * Sets arr terminal.
     *
     * @param arrTerminal the arr terminal
     */
    public void setArrTerminal(String arrTerminal) {
        this.arrTerminal = arrTerminal;
    }

    /**
     * Gets arr time.
     *
     * @return the arr time
     */
    public String getArrTime() {
        return arrTime;
    }

    /**
     * Sets arr time.
     *
     * @param arrTime the arr time
     */
    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    /**
     * Gets stop cities.
     *
     * @return the stop cities
     */
    public String getStopCities() {
        return stopCities;
    }

    /**
     * Sets stop cities.
     *
     * @param stopCities the stop cities
     */
    public void setStopCities(String stopCities) {
        this.stopCities = stopCities;
    }

    /**
     * Gets stop airports.
     *
     * @return the stop airports
     */
    public String getStopAirports() {
        return stopAirports;
    }

    /**
     * Sets stop airports.
     *
     * @param stopAirports the stop airports
     */
    public void setStopAirports(String stopAirports) {
        this.stopAirports = stopAirports;
    }

    /**
     * Gets code share.
     *
     * @return the code share
     */
    public Boolean getCodeShare() {
        return codeShare;
    }

    /**
     * Sets code share.
     *
     * @param codeShare the code share
     */
    public void setCodeShare(Boolean codeShare) {
        this.codeShare = codeShare;
    }

    /**
     * Gets operating carrier.
     *
     * @return the operating carrier
     */
    public String getOperatingCarrier() {
        return operatingCarrier;
    }

    /**
     * Sets operating carrier.
     *
     * @param operatingCarrier the operating carrier
     */
    public void setOperatingCarrier(String operatingCarrier) {
        this.operatingCarrier = operatingCarrier;
    }

    /**
     * Gets operating flight no.
     *
     * @return the operating flight no
     */
    public String getOperatingFlightNo() {
        return operatingFlightNo;
    }

    /**
     * Sets operating flight no.
     *
     * @param operatingFlightNo the operating flight no
     */
    public void setOperatingFlightNo(String operatingFlightNo) {
        this.operatingFlightNo = operatingFlightNo;
    }

    /**
     * Gets cabin.
     *
     * @return the cabin
     */
    public String getCabin() {
        return cabin;
    }

    /**
     * Sets cabin.
     *
     * @param cabin the cabin
     */
    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    /**
     * Gets cabin grade.
     *
     * @return the cabin grade
     */
    public String getCabinGrade() {
        return cabinGrade;
    }

    /**
     * Sets cabin grade.
     *
     * @param cabinGrade the cabin grade
     */
    public void setCabinGrade(String cabinGrade) {
        this.cabinGrade = cabinGrade;
    }

    /**
     * Gets aircraft code.
     *
     * @return the aircraft code
     */
    public String getAircraftCode() {
        return aircraftCode;
    }

    /**
     * Sets aircraft code.
     *
     * @param aircraftCode the aircraft code
     */
    public void setAircraftCode(String aircraftCode) {
        this.aircraftCode = aircraftCode;
    }


    /**
     * Gets booking class avail.
     *
     * @return the booking class avail
     */
    public String getBookingClassAvail() {
        return bookingClassAvail;
    }

    /**
     * Sets booking class avail.
     *
     * @param bookingClassAvail the booking class avail
     */
    public void setBookingClassAvail(String bookingClassAvail) {
        this.bookingClassAvail = bookingClassAvail;
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Gets flight indicator.
     *
     * @return the flight indicator
     */
    public Integer getFlightIndicator() {
        return flightIndicator;
    }

    /**
     * Sets flight indicator.
     *
     * @param flightIndicator the flight indicator
     */
    public void setFlightIndicator(Integer flightIndicator) {
        this.flightIndicator = flightIndicator;
    }

    /**
     * Gets item number.
     *
     * @return the item number
     */
    public Integer getItemNumber() {
        return itemNumber;
    }

    /**
     * Sets item number.
     *
     * @param itemNumber the item number
     */
    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    /**
     * Gets segment key.
     *
     * @return the segment key
     */
    public String getSegmentKey() {
        return segmentKey;
    }

    /**
     * Sets segment key.
     *
     * @param segmentKey the segment key
     */
    public void setSegmentKey(String segmentKey) {
        this.segmentKey = segmentKey;
    }

    /**
     * Gets dep zone.
     *
     * @return the dep zone
     */
    public String getDepZone() {
        return depZone;
    }

    /**
     * Sets dep zone.
     *
     * @param depZone the dep zone
     */
    public void setDepZone(String depZone) {
        this.depZone = depZone;
    }

    /**
     * Gets arr zone.
     *
     * @return the arr zone
     */
    public String getArrZone() {
        return arrZone;
    }

    /**
     * Sets arr zone.
     *
     * @param arrZone the arr zone
     */
    public void setArrZone(String arrZone) {
        this.arrZone = arrZone;
    }

    /**
     * Gets availability source.
     *
     * @return the availability source
     */
    public String getAvailabilitySource() {
        return availabilitySource;
    }

    /**
     * Sets availability source.
     *
     * @param availabilitySource the availability source
     */
    public void setAvailabilitySource(String availabilitySource) {
        this.availabilitySource = availabilitySource;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * Gets seat count.
     *
     * @return the seat count
     */
    public String getSeatCount() {
        return seatCount;
    }

    /**
     * Sets seat count.
     *
     * @param seatCount the seat count
     */
    public void setSeatCount(String seatCount) {
        this.seatCount = seatCount;
    }

    public String getOriginalCabin() {
        return originalCabin;
    }

    public void setOriginalCabin(String originalCabin) {
        this.originalCabin = originalCabin;
    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (carrier != null ? carrier.hashCode() : 0);
        result = 31 * result + (flightNumber != null ? flightNumber.hashCode() : 0);
        result = 31 * result + (depAirport != null ? depAirport.hashCode() : 0);
        result = 31 * result + (depTime != null ? depTime.hashCode() : 0);
        result = 31 * result + (arrAirport != null ? arrAirport.hashCode() : 0);
        result = 31 * result + (cabin != null ? cabin.hashCode() : 0);
        result = 31 * result + (cabinGrade != null ? cabinGrade.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SibeSegment{" +
            "carrier='" + carrier + '\'' +
            ", flightNumber='" + flightNumber + '\'' +
            ", depAirport='" + depAirport + '\'' +
            ", depTerminal='" + depTerminal + '\'' +
            ", depTime='" + depTime + '\'' +
            ", arrAirport='" + arrAirport + '\'' +
            ", arrTerminal='" + arrTerminal + '\'' +
            ", arrTime='" + arrTime + '\'' +
            ", stopCities='" + stopCities + '\'' +
            ", stopAirports='" + stopAirports + '\'' +
            ", codeShare=" + codeShare +
            ", operatingCarrier='" + operatingCarrier + '\'' +
            ", operatingFlightNo='" + operatingFlightNo + '\'' +
            ", cabin='" + cabin + '\'' +
            ", cabinGrade='" + cabinGrade + '\'' +
            ", aircraftCode='" + aircraftCode + '\'' +
            ", duration=" + duration +
            ", bookingClassAvail='" + bookingClassAvail + '\'' +
            ", fareBasis='" + fareBasis + '\'' +
            ", seatCount='" + seatCount + '\'' +
            ", flightIndicator='" + flightIndicator + '\'' +
            ", itemNumber=" + itemNumber +
            ", segmentKey='" + segmentKey + '\'' +
            ", depZone='" + depZone + '\'' +
            ", arrZone='" + arrZone + '\'' +
            ", availabilitySource='" + availabilitySource + '\'' +
            '}';
    }
}
