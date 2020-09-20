package comm.sibe;

import comm.ota.gds.Routing;
import comm.ota.site.SibeRouting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

/**
 * Created by gwk on 18/3/14.
 */

@Mapper(componentModel = "spring", uses = {})
public interface SibeRoutingMapper extends SibeEntityMapper<SibeRouting, Routing>{

    @Mapping(source = "productType", target = "fareType")
    @Mapping(source = "adultPrice", target = "adultPriceGDS")
    @Mapping(source = "adultTax", target = "adultTaxGDS")
    @Mapping(source = "childPrice", target = "childPriceGDS")
    @Mapping(source = "childTax", target = "childTaxGDS")
    @Mapping(source = "infantsPrice", target = "infantPriceGDS")
    @Mapping(source = "infantsTax", target = "infantTaxGDS")
    @Mapping(source = "currency", target = "currencyGDS")
    SibeRouting toSibe(Routing routing);

    default Integer bigDecimalToInteger(BigDecimal bigDecimalValue){
        if(bigDecimalValue == null){
            return null;
        }
        Integer integerValue = bigDecimalValue.setScale(0, BigDecimal.ROUND_UP).intValue();
        return integerValue;
    }

    @Mapping(source = "fareType", target =  "productType" )
    @Mapping(source = "adultPriceGDS", target =  "adultPrice" )
    @Mapping(source = "adultTaxGDS", target =  "adultTax" )
    @Mapping(source = "childPriceGDS", target =  "childPrice")
    @Mapping(source = "childTaxGDS", target =  "childTax"  )
    @Mapping(source = "infantPriceGDS", target =  "infantsPrice")
    @Mapping(source = "infantTaxGDS", target =  "infantsTax" )
    Routing toGds(SibeRouting sibeRouting);
}
