package comm.ota.gds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by yangdehua on 10/26/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Baggage implements Serializable{

    private static final long serialVersionUID = -1287034949674846021L;
    private int segmentNo;
    private String adultBaggage;
    private String childBaggage;
    private String enAdultBaggage;
    private String enChildBaggage;
    private String adultHandBaggage;
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

    public int getSegmentNo() {
        return segmentNo;
    }

    public void setSegmentNo(int segmentNo) {
        this.segmentNo = segmentNo;
    }

    public String getAdultBaggage() {
        return adultBaggage;
    }

    public void setAdultBaggage(String adultBaggage) {
        this.adultBaggage = adultBaggage;
    }

    public String getChildBaggage() {
        return childBaggage;
    }

    public void setChildBaggage(String childBaggage) {
        this.childBaggage = childBaggage;
    }

    public String getEnAdultBaggage() {
        return enAdultBaggage;
    }

    public void setEnAdultBaggage(String enAdultBaggage) {
        this.enAdultBaggage = enAdultBaggage;
    }

    public String getEnChildBaggage() {
        return enChildBaggage;
    }

    public void setEnChildBaggage(String enChildBaggage) {
        this.enChildBaggage = enChildBaggage;
    }
}
