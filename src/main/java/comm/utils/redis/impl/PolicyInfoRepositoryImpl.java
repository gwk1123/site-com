package comm.utils.redis.impl;

import comm.repository.entity.PolicyInfo;
import comm.utils.constant.DirectConstants;
import comm.utils.copy.CopyUtils;
import comm.utils.redis.util.RedisCacheKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author gwk
 */
@Component
public class PolicyInfoRepositoryImpl  {

    @Autowired
    private RedisTemplate redisTemplate;

    private final static String REDIS_KEY = "sibePolicyInfo";



    public PolicyInfo saveOrUpdate(PolicyInfo item) {
        PolicyInfo policyInfo = CopyUtils.deepCopy(item);
        if (policyInfo == null
                || StringUtils.isEmpty(policyInfo.getOtaCode())
                || StringUtils.isEmpty(policyInfo.getOtaSiteCode())
                || StringUtils.isEmpty(policyInfo.getAirline())
                || StringUtils.isEmpty(policyInfo.getTripType())){
            throw new IllegalStateException("细节政策不能为空");
        }

        //如果出发地，或者目的地为空，则不限(UNLIMIT)， 不转换为TC1/TC2/TC3(增加重复的内容)
        String depCityInfo = policyInfo.getDepCity();
        String arrCityInfo = policyInfo.getArrCity();

        depCityInfo = StringUtils.isEmpty(depCityInfo)? DirectConstants.ALL :depCityInfo;
        arrCityInfo = StringUtils.isEmpty(arrCityInfo)?DirectConstants.ALL:arrCityInfo;
        policyInfo.setDepCity(depCityInfo);
        policyInfo.setArrCity(arrCityInfo);
        this.saveOrUpdateCache(policyInfo);
        return null;
    }


    public PolicyInfo saveOrUpdateCache(PolicyInfo apiPolicyInfoRedis) {
        String key= RedisCacheKeyUtil.getPolicyInfoRedisCacheKey(apiPolicyInfoRedis);
        redisTemplate.opsForHash().put(REDIS_KEY, key, apiPolicyInfoRedis);
        //s:site, a:airline, t:trip type, d:dep_city

        if(DirectConstants.TRIP_TYPE_ALL.equals(apiPolicyInfoRedis.getTripType())){
            //1 单程
            redisTemplate.opsForSet().add(REDIS_KEY + ":s:" + apiPolicyInfoRedis.getOtaSiteCode()
                            + ":t:1"
                    , key);

            redisTemplate.opsForSet().add(REDIS_KEY + ":s:" + apiPolicyInfoRedis.getOtaSiteCode()
                            + ":t:1"
                            + ":a:" + apiPolicyInfoRedis.getAirline()
                    , key);

            // 2 往返
            redisTemplate.opsForSet().add(REDIS_KEY + ":s:" + apiPolicyInfoRedis.getOtaSiteCode()
                            + ":t:2"
                    , key);

            redisTemplate.opsForSet().add(REDIS_KEY + ":s:" + apiPolicyInfoRedis.getOtaSiteCode()
                            + ":t:2"
                            + ":a:" + apiPolicyInfoRedis.getAirline()
                    , key);

        }else {
            redisTemplate.opsForSet().add(REDIS_KEY + ":s:" + apiPolicyInfoRedis.getOtaSiteCode()
                            + ":t:" + apiPolicyInfoRedis.getTripType()
                    , key);

            redisTemplate.opsForSet().add(REDIS_KEY + ":s:" + apiPolicyInfoRedis.getOtaSiteCode()
                            + ":t:" + apiPolicyInfoRedis.getTripType()
                            + ":a:" + apiPolicyInfoRedis.getAirline()
                    , key);
        }

        return apiPolicyInfoRedis;
    }


    public void delete(PolicyInfo policyInfo) {

        String key= RedisCacheKeyUtil.getPolicyInfoRedisCacheKey(policyInfo);
        redisTemplate.opsForHash().delete(REDIS_KEY, key);
        //s:site, a:airline, t:trip type, d:dep_city

        if(DirectConstants.TRIP_TYPE_ALL.equals(policyInfo.getTripType())){
            //1 单程
            redisTemplate.opsForSet().remove(REDIS_KEY + ":s:" + policyInfo.getOtaSiteCode()
                            + ":t:1"
                    , key);

            redisTemplate.opsForSet().remove(REDIS_KEY + ":s:" + policyInfo.getOtaSiteCode()
                            + ":t:1"
                            + ":a:" + policyInfo.getAirline()
                    , key);

            // 2 往返
            redisTemplate.opsForSet().remove(REDIS_KEY + ":s:" + policyInfo.getOtaSiteCode()
                            + ":t:2"
                    , key);

            redisTemplate.opsForSet().remove(REDIS_KEY + ":s:" + policyInfo.getOtaSiteCode()
                            + ":t:2"
                            + ":a:" + policyInfo.getAirline()
                    , key);

        }else {
            redisTemplate.opsForSet().remove(REDIS_KEY + ":s:" + policyInfo.getOtaSiteCode()
                            + ":t:" + policyInfo.getTripType()
                    , key);

            redisTemplate.opsForSet().remove(REDIS_KEY + ":s:" + policyInfo.getOtaSiteCode()
                            + ":t:" + policyInfo.getTripType()
                            + ":a:" + policyInfo.getAirline()
                    , key);
        }

    }

}
