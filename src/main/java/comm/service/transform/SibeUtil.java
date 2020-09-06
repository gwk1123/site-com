package comm.service.transform;

import comm.repository.entity.AllAirports;
import comm.utils.constant.SibeConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class SibeUtil {


    /**
     * Get city priority list.
     * 得到
     * @param fromCity the from city redis
     * @param toCity the to city redis
     * @return the list
     */
    public final static List<String> getCityPriority(final AllAirports fromCity,
                                                     final AllAirports toCity){
        List<String> priorityList = new ArrayList<>(16);
        //城市-城市
        priorityList.add(fromCity.getCcode()+"/"+toCity.getCcode());
        //城市-国家
        priorityList.add(fromCity.getCcode()+"/"+toCity.getGcode());
        //城市-大区
        priorityList.add(fromCity.getCcode()+"/"+toCity.getQcode());
        //城市-不限制
        priorityList.add(fromCity.getCcode()+"/"+SibeConstants.ALL);
        //城市-城市
        priorityList.add(fromCity.getGcode()+"/"+toCity.getCcode());
        //城市-国家
        priorityList.add(fromCity.getGcode()+"/"+toCity.getGcode());
        //城市-大区
        priorityList.add(fromCity.getGcode()+"/"+toCity.getQcode());
        //城市-不限制
        priorityList.add(fromCity.getGcode()+"/"+SibeConstants.ALL);
        //城市-城市
        priorityList.add(fromCity.getQcode()+"/"+toCity.getCcode());
        //城市-国家
        priorityList.add(fromCity.getQcode()+"/"+toCity.getGcode());
        //城市-大区
        priorityList.add(fromCity.getQcode()+"/"+toCity.getQcode());
        //城市-不限制
        priorityList.add(fromCity.getQcode()+"/"+SibeConstants.ALL);
        //城市-城市
        priorityList.add(SibeConstants.ALL+"/"+toCity.getCcode());
        //城市-国家
        priorityList.add(SibeConstants.ALL+"/"+toCity.getGcode());
        //城市-大区
        priorityList.add(SibeConstants.ALL+"/"+toCity.getQcode());
        //城市-不限制
        priorityList.add(SibeConstants.ALL+"/"+SibeConstants.ALL);
        return  priorityList;
    }

    /**
     * 判断字符串strContent中是否包括subStr,必须是使用separator分隔
     * @param strContent
     * @param subStr
     * @param separator
     * @return
     */
    public static boolean contains(String strContent, String subStr,String separator){
        if(StringUtils.isBlank(strContent)){
            return true;
        }
        return Stream
                .of(StringUtils.split(strContent,separator))
                .filter(Objects::nonNull)
                .anyMatch(subStrContent->(subStrContent.equals(subStr)));
    }

}
