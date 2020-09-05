package comm.ota.site;


/**
 * Created by yangdehua on 18/3/14.
 */
public class SibeBaggage{

    //航段序号，从1开始
    private Integer segmentNo;

    //成人行李额中文版，格式重量+单位 如1件，每件15KG；如只限重量不限件数，赋重量即可，如23KG；此节点值不能为空
    private String adultBaggage;

    //儿童行李额中文版，格式同成人【对于含儿童的情况，此节点不能为空
    private String childBaggage;

    //婴儿行李额中文版，格式同成人【对于含儿童的情况，此节点不能为空
    private String enAdultBaggage;

    //成人行李额英文版，格式重量+单位 示例：1P，23KG
    private String enChildBaggage;

    //成人行李额英文版，格式重量+单位 示例：1P，23KG
    private String infantBaggage;

    //成人行李额英文版，格式重量+单位 示例：1P，23KG
    private String enInfantBaggage;

    //VJ返回；成人手提行李额，格式重量+单位 如15KG
     private String adultHandBaggage;

    //VJ返回；儿童手提行李额，格式重量+单位 如15KG
     private String childHandBaggage;

    public String getAdultHandBaggage() {
        return adultHandBaggage;
    }

    public void setAdultHandBaggage(String adultHandBaggage) {
        this.adultHandBaggage = adultHandBaggage;
    }

    public String getChildHandBaggage() {
        return childHandBaggage;
    }

    public void setChildHandBaggage(String childHandBaggage) {
        this.childHandBaggage = childHandBaggage;
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
     * Gets adult baggage.
     *
     * @return the adult baggage
     */
    public String getAdultBaggage() {
        return adultBaggage;
    }

    /**
     * Sets adult baggage.
     *
     * @param adultBaggage the adult baggage
     */
    public void setAdultBaggage(String adultBaggage) {
        this.adultBaggage = adultBaggage;
    }

    /**
     * Gets child baggage.
     *
     * @return the child baggage
     */
    public String getChildBaggage() {
        return childBaggage;
    }

    /**
     * Sets child baggage.
     *
     * @param childBaggage the child baggage
     */
    public void setChildBaggage(String childBaggage) {
        this.childBaggage = childBaggage;
    }

    /**
     * Gets en adult baggage.
     *
     * @return the en adult baggage
     */
    public String getEnAdultBaggage() {
        return enAdultBaggage;
    }

    /**
     * Sets en adult baggage.
     *
     * @param enAdultBaggage the en adult baggage
     */
    public void setEnAdultBaggage(String enAdultBaggage) {
        this.enAdultBaggage = enAdultBaggage;
    }

    /**
     * Gets en child baggage.
     *
     * @return the en child baggage
     */
    public String getEnChildBaggage() {
        return enChildBaggage;
    }

    /**
     * Sets en child baggage.
     *
     * @param enChildBaggage the en child baggage
     */
    public void setEnChildBaggage(String enChildBaggage) {
        this.enChildBaggage = enChildBaggage;
    }

    /**
     * Gets infant baggage.
     *
     * @return the infant baggage
     */
    public String getInfantBaggage() {
        return infantBaggage;
    }

    /**
     * Sets infant baggage.
     *
     * @param infantBaggage the infant baggage
     */
    public void setInfantBaggage(String infantBaggage) {
        this.infantBaggage = infantBaggage;
    }

    /**
     * Gets en infant baggage.
     *
     * @return the en infant baggage
     */
    public String getEnInfantBaggage() {
        return enInfantBaggage;
    }

    /**
     * Sets en infant baggage.
     *
     * @param enInfantBaggage the en infant baggage
     */
    public void setEnInfantBaggage(String enInfantBaggage) {
        this.enInfantBaggage = enInfantBaggage;
    }
}
