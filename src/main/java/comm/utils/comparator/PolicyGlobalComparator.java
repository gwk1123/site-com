package comm.utils.comparator;

import comm.repository.entity.PolicyGlobal;
import comm.utils.constant.PolicyConstans;

import java.util.Comparator;

/**
 * Created by yangdehua on 18/2/24.
 */
public class PolicyGlobalComparator implements Comparator<PolicyGlobal> {

    @Override
    public int compare(PolicyGlobal g1, PolicyGlobal g2) {

        if((g1.getAirline().equals(PolicyConstans.POLICY_AIRLINE_ALL) && g2.getAirline().equals(PolicyConstans.POLICY_AIRLINE_ALL))
            || (!g1.getAirline().equals(PolicyConstans.POLICY_AIRLINE_ALL) && !g2.getAirline().equals(PolicyConstans.POLICY_AIRLINE_ALL))){

            if(g1.getTripType().equals(PolicyConstans.POLICY_TRIP_TYPE_ALL) && g2.getTripType().equals(PolicyConstans.POLICY_TRIP_TYPE_ALL)
                || (!g1.getTripType().equals(PolicyConstans.POLICY_TRIP_TYPE_ALL) && !g2.getTripType().equals(PolicyConstans.POLICY_TRIP_TYPE_ALL))){

            if((g1.getPriceType().equals(PolicyConstans.POLICY_PRICE_TYPE_ALL) && g2.getPriceType().equals(PolicyConstans.POLICY_PRICE_TYPE_ALL))
                    || (!g1.getPriceType().equals(PolicyConstans.POLICY_PRICE_TYPE_ALL) && !g2.getPriceType().equals(PolicyConstans.POLICY_PRICE_TYPE_ALL))){

               if((g1.getPermitTransit() == 3 && g2.getPermitTransit() == 3)
                    || (g1.getPermitTransit()) != 3 && g2.getPermitTransit() != 3) {

                   if ((g1.getPermitInterline() == 3 && g2.getPermitInterline() == 3)
                       || (g1.getPermitInterline() != 3 && g2.getPermitInterline() != 3)) {

                       if ((g1.getPermitCodeShare() == 3 && g2.getPermitCodeShare() == 3)
                           || (g1.getPermitCodeShare() != 3 && g2.getPermitCodeShare() != 3)) {
                           return 0;
                       } else if (g1.getPermitCodeShare() != 3) {
                           return 1;
                       } else {
                           return -1;
                       }

                   } else if (g1.getPermitInterline() != 3) {
                       return 1;
                   } else {
                       return -1;
                   }

               }else if(g1.getPermitTransit() != 3){
                   return 1;
               }else {
                   return -1;
               }

                }else if(!g1.getPriceType().equals(PolicyConstans.POLICY_PRICE_TYPE_ALL)){
                    return 1;
                }else {
                    return -1;
                }

            }else if(!g1.getTripType().equals(PolicyConstans.POLICY_TRIP_TYPE_ALL)){
                return 1;
            }else {
                return -1;
            }

            }else if(!g1.getAirline().equals(PolicyConstans.POLICY_AIRLINE_ALL)){
                return 1;
            }else {
                return -1;
            }

    }
}
