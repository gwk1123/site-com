package comm.ota.gds;


import java.io.Serializable;

/**
 * Created by yangdehua on 10/26/16.
 */
public class AirlineAncillaries implements Serializable{

    private static final long serialVersionUID = 5116479275909183215L;
    private Boolean baggageService;
    private Boolean unFreeBaggage;

    public Boolean getBaggageService() {
        return baggageService;
    }

    public void setBaggageService(Boolean baggageService) {
        this.baggageService = baggageService;
    }

    public Boolean getUnFreeBaggage() {
        return unFreeBaggage;
    }

    public void setUnFreeBaggage(Boolean unFreeBaggage) {
        this.unFreeBaggage = unFreeBaggage;
    }
}
