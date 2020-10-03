package com.sibecommon.ota.site;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestrictCodeShareFlightInfo {
    private Map<String,String> flightNumberMap;
    private List<FlightRules> flightRulesList;

    public RestrictCodeShareFlightInfo() {
        flightNumberMap = new HashMap<>();
        flightRulesList = new ArrayList<>();
    }

    public Map<String, String> getFlightNumberMap() {
        return flightNumberMap;
    }

    public void setFlightNumberMap(Map<String, String> flightNumberMap) {
        this.flightNumberMap = flightNumberMap;
    }

    public List<FlightRules> getFlightRulesList() {
        return flightRulesList;
    }

    public void setFlightRulesList(List<FlightRules> flightRulesList) {
        this.flightRulesList = flightRulesList;
    }
}
