package comm.ota.site;


/**
 * Created by yangdehua on 18/3/12.
 */
public class SibeFormatBaggage {

    //航段序号，从1开始 1）注意是按航段赋值，而不是按去程/回程赋值
    //指示当前是第几航程
    private String flightIndicator;

    //航段序号，从1开始 1）注意是按航段赋值，而不是按去程/回程赋值
    private Integer segmentNo;

    //乘客类型，0 成人/1 儿童/2 婴儿 1）对于多乘客类型的查询、验价，必须按乘客类型返回；如成人+儿童的查询，成人和儿童的行李额都要有。
    private Integer passengerType;

    //行李额件数，单位PC，枚举值如下：
    // 0无免费托运行李，此时baggageWeight需赋值为-1；
    // -1表示计重制，对应的baggageWeight表示每人可携带的总重量(此时baggageWeight必须赋正值，否则过滤）；
    // >0表示计件制，对应的baggageWeight表示每件行李重量（若计件制时不知每件行李额的重量，baggageWeight必须赋值为-1）。
    private Integer baggagePiece;

    //行李额重量，单位KG，必须赋值，跟BaggagePiece配合使用
    private Integer baggageWeight;

    //中文行李额备注
    private String cnBaggage;

    //英文行李额备注
    private String enBaggage;

    //单件行李长度限制,单位厘米
    private Integer length;

    //单件行李宽度限制,单位厘米。
    private Integer width;

    //单件行李高度限制,单位厘米。
    private Integer height;

    //是否所有行李重量
    private Boolean isAllWeight;

    private Integer handBaggagePiece;

    private Integer handBaggageWeight;

    public Integer getHandBaggagePiece() {
        return handBaggagePiece;
    }

    public void setHandBaggagePiece(Integer handBaggagePiece) {
        this.handBaggagePiece = handBaggagePiece;
    }

    public Integer getHandBaggageWeight() {
        return handBaggageWeight;
    }

    public void setHandBaggageWeight(Integer handBaggageWeight) {
        this.handBaggageWeight = handBaggageWeight;
    }

    /**
     * Is all weight boolean.
     *
     * @return the boolean
     */
    public Boolean isAllWeight() {
        return isAllWeight;
    }

    /**
     * Sets all weight.
     *
     * @param allWeight the all weight
     */
    public void setAllWeight(Boolean allWeight) {
        isAllWeight = allWeight;
    }

    /**
     * Gets flight indicator.
     *
     * @return the flight indicator
     */
    public String getFlightIndicator() {
        return flightIndicator;
    }

    /**
     * Sets flight indicator.
     *
     * @param flightIndicator the flight indicator
     */
    public void setFlightIndicator(String flightIndicator) {
        this.flightIndicator = flightIndicator;
    }

    /**
     * Gets segment no.
     *
     * @return the segment no
     */
    public Integer getSegmentNo() {
        return segmentNo;
    }

    /**
     * Sets segment no.
     *
     * @param segmentNo the segment no
     */
    public void setSegmentNo(Integer segmentNo) {
        this.segmentNo = segmentNo;
    }

    /**
     * Gets passenger type.
     *
     * @return the passenger type
     */
    public Integer getPassengerType() {
        return passengerType;
    }

    /**
     * Sets passenger type.
     *
     * @param passengerType the passenger type
     */
    public void setPassengerType(Integer passengerType) {
        this.passengerType = passengerType;
    }

    /**
     * Gets baggage piece.
     *
     * @return the baggage piece
     */
    public Integer getBaggagePiece() {
        return baggagePiece;
    }

    /**
     * Sets baggage piece.
     *
     * @param baggagePiece the baggage piece
     */
    public void setBaggagePiece(Integer baggagePiece) {
        this.baggagePiece = baggagePiece;
    }

    /**
     * Gets baggage weight.
     *
     * @return the baggage weight
     */
    public Integer getBaggageWeight() {
        return baggageWeight;
    }

    /**
     * Sets baggage weight.
     *
     * @param baggageWeight the baggage weight
     */
    public void setBaggageWeight(Integer baggageWeight) {
        this.baggageWeight = baggageWeight;
    }

    /**
     * Gets cn baggage.
     *
     * @return the cn baggage
     */
    public String getCnBaggage() {
        return cnBaggage;
    }

    /**
     * Sets cn baggage.
     *
     * @param cnBaggage the cn baggage
     */
    public void setCnBaggage(String cnBaggage) {
        this.cnBaggage = cnBaggage;
    }

    /**
     * Gets en baggage.
     *
     * @return the en baggage
     */
    public String getEnBaggage() {
        return enBaggage;
    }

    /**
     * Sets en baggage.
     *
     * @param enBaggage the en baggage
     */
    public void setEnBaggage(String enBaggage) {
        this.enBaggage = enBaggage;
    }

    /**
     * Gets length.
     *
     * @return the length
     */
    public Integer getLength() {
        return length;
    }

    /**
     * Sets length.
     *
     * @param length the length
     */
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Sets width.
     *
     * @param width the width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Sets height.
     *
     * @param height the height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }
}
