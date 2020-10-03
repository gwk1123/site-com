package com.sibecommon.utils.redis.impl;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibecommon.repository.entity.PolicyInfo;
import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.utils.constant.DirectConstants;
import com.sibecommon.utils.copy.CopyUtils;
import com.sibecommon.utils.redis.util.RedisCacheKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gwk
 */
@Component
public class PolicyInfoRepositoryImpl {

    private Logger LOGGER = LoggerFactory.getLogger(PolicyInfoRepositoryImpl.class);

    private ObjectMapper objectMapper =new ObjectMapper();

    @Autowired
    private RedisTemplate redisTemplate;

    private final static String REDIS_KEY = "sibe_policy_info";

    private final static String REDIS_KEY_ALL = "sibe_policy_info_all_key";



    public PolicyInfo saveOrUpdate(PolicyInfo item) {
        PolicyInfo policyInfo = CopyUtils.deepCopy(item);
        if (policyInfo == null
                || StringUtils.isEmpty(policyInfo.getOtaCode())
                || StringUtils.isEmpty(policyInfo.getOtaSiteCode())
                || StringUtils.isEmpty(policyInfo.getAirline())
                || StringUtils.isEmpty(policyInfo.getTripType())) {
            throw new IllegalStateException("细节政策不能为空");
        }

        //如果出发地，或者目的地为空，则不限(UNLIMIT)， 不转换为TC1/TC2/TC3(增加重复的内容)
        String depCityInfo = policyInfo.getDepCity();
        String arrCityInfo = policyInfo.getArrCity();

        depCityInfo = StringUtils.isEmpty(depCityInfo) ? DirectConstants.ALL : depCityInfo;
        arrCityInfo = StringUtils.isEmpty(arrCityInfo) ? DirectConstants.ALL : arrCityInfo;
        policyInfo.setDepCity(depCityInfo);
        policyInfo.setArrCity(arrCityInfo);
        this.saveOrUpdateCache(policyInfo);
        return null;
    }


    public PolicyInfo saveOrUpdateCache(PolicyInfo apiPolicyInfoRedis) {
        String key = RedisCacheKeyUtil.getPolicyInfoRedisCacheKey(apiPolicyInfoRedis);
        redisTemplate.opsForHash().put(REDIS_KEY, key, apiPolicyInfoRedis);
        //s:site, a:airline, t:trip type, d:dep_city

        if (DirectConstants.TRIP_TYPE_ALL.equals(apiPolicyInfoRedis.getTripType())) {
            //1 单程
            String key1 = REDIS_KEY + ":s:" + apiPolicyInfoRedis.getOtaSiteCode() + ":t:1";
            redisTemplate.opsForSet().add(key1, key);
            String key2 = REDIS_KEY + ":s:" + apiPolicyInfoRedis.getOtaSiteCode() + ":t:1" + ":a:" + apiPolicyInfoRedis.getAirline();
            redisTemplate.opsForSet().add(key2, key);

            // 2 往返
            String key3 = REDIS_KEY + ":s:" + apiPolicyInfoRedis.getOtaSiteCode() + ":t:2";
            redisTemplate.opsForSet().add(key3, key);
            String key4 = REDIS_KEY + ":s:" + apiPolicyInfoRedis.getOtaSiteCode() + ":t:2" + ":a:" + apiPolicyInfoRedis.getAirline();
            redisTemplate.opsForSet().add(key4, key);

            this.addKeyAll( key1);
            this.addKeyAll( key2);
            this.addKeyAll( key3);
            this.addKeyAll( key4);
        } else {
            String key1 = REDIS_KEY + ":s:" + apiPolicyInfoRedis.getOtaSiteCode() + ":t:" + apiPolicyInfoRedis.getTripType();
            redisTemplate.opsForSet().add(key1, key);
            String key2 = REDIS_KEY + ":s:" + apiPolicyInfoRedis.getOtaSiteCode() + ":t:" + apiPolicyInfoRedis.getTripType() + ":a:" + apiPolicyInfoRedis.getAirline();
            redisTemplate.opsForSet().add(key2, key);

            this.addKeyAll( key1);
            this.addKeyAll( key2);
        }

        return apiPolicyInfoRedis;
    }


    public void delete(PolicyInfo policyInfo) {

        String key = RedisCacheKeyUtil.getPolicyInfoRedisCacheKey(policyInfo);
        redisTemplate.opsForHash().delete(REDIS_KEY, key);
        //s:site, a:airline, t:trip type, d:dep_city

        if (DirectConstants.TRIP_TYPE_ALL.equals(policyInfo.getTripType())) {
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

        } else {
            redisTemplate.opsForSet().remove(REDIS_KEY + ":s:" + policyInfo.getOtaSiteCode()
                            + ":t:" + policyInfo.getTripType()
                    , key);

            redisTemplate.opsForSet().remove(REDIS_KEY + ":s:" + policyInfo.getOtaSiteCode()
                            + ":t:" + policyInfo.getTripType()
                            + ":a:" + policyInfo.getAirline()
                    , key);
        }

    }


    public List<PolicyInfo> findBySiteAndAirline(SibeSearchRequest sibeSearchRequest, String airline, Set<String> otherAirlineSet) {
        LOGGER.debug("uuid:" + sibeSearchRequest.getUuid() + " 4.1.3.3.1 findBySiteAndAirline" + (SystemClock.now() - sibeSearchRequest.getStartTime()) / (1000) + "秒");
        if (StringUtils.isNotEmpty(airline)) {
            otherAirlineSet.add(airline);
        }
        Set<Object> policyInfoKeys = new HashSet<>();
        otherAirlineSet
                .stream()
                .filter(Objects::nonNull)
                .forEach(otherAirline -> {
                    //1.获得明细政策Key(当前查询出发城市)
                    Optional<Set<Object>> airlineKeysOptional = getPolicyInfoKeys(sibeSearchRequest, otherAirline);
                    if (airlineKeysOptional.isPresent()) {
                        policyInfoKeys.addAll(airlineKeysOptional.get());
                    }
                    //2.获得明细政策Key(DEP_ARR_UNLIMITED)
                    Optional<Set<Object>> airlineKeysOptional2 = getPolicyInfoKeys(sibeSearchRequest, otherAirline);
                    if (airlineKeysOptional2.isPresent()) {
                        policyInfoKeys.addAll(airlineKeysOptional2.get());
                    }
                });

        LOGGER.debug("uuid:" + sibeSearchRequest.getUuid() + " 4.1.3.3.3 findBySiteAndAirline " + (SystemClock.now() - sibeSearchRequest.getStartTime()) / (1000) + "秒");
        String citiesCommaSeparated = policyInfoKeys.stream().map(x -> (x.toString())).collect(Collectors.joining(","));
        LOGGER.debug("uuid:" + sibeSearchRequest.getUuid() + " 4.1.3.3.4 citiesCommaSeparated " + citiesCommaSeparated);

        List<Object> policyInfoRedisObjects = new ArrayList<>();
        for (Object obj : policyInfoKeys) {
            policyInfoRedisObjects.add(this.findAll().get(obj));
        }

        LOGGER.debug("uuid:" + sibeSearchRequest.getUuid() + " 4.1.3.3.5 findBySiteAndAirline " + (SystemClock.now() - sibeSearchRequest.getStartTime()) / (1000) + "秒");
        return policyInfoRedisObjects.stream()
                .map(apiPolicyInfoRedis ->  objectMapper.convertValue(apiPolicyInfoRedis,PolicyInfo.class))
                .collect(Collectors.toList());
    }

    public Map<Object, Object> findAll() {
        return redisTemplate.opsForHash().entries(REDIS_KEY);

    }

    /**
     * 获得明细政策Keys
     *
     * @param sibeSearchRequest sibeSearchRequest
     * @param otherAirline      otherAirline
     * @return Set<key>
     */
    private Optional<Set<Object>> getPolicyInfoKeys(SibeSearchRequest sibeSearchRequest, String otherAirline) {

        StringBuffer buf = new StringBuffer(REDIS_KEY)
                .append(":s:")
                .append(sibeSearchRequest.getSite())
                .append(":t:")
                .append(sibeSearchRequest.getTripType())
                .append(":a:")
                .append(otherAirline);

        LOGGER.debug("uuid:" + sibeSearchRequest.getUuid() + " 4.1.3.3.2 findBySiteAndAirline:" + " airlineKey:" + buf.toString());
        Set<Object> otherAirlineKeys = this.getPolicyKey(buf.toString());

        LOGGER.debug("uuid:" + sibeSearchRequest.getUuid() + " 4.1.3.3.2 findBySiteAndAirline " + otherAirline + " " + (SystemClock.now() - sibeSearchRequest.getStartTime()) / (1000) + "秒");
        return Optional.ofNullable(otherAirlineKeys);
    }

    public Set<Object> getPolicyKey(String keyValue) {

        return redisTemplate.opsForSet().members(keyValue);

    }


    public void addKeyAll(String key){
        redisTemplate.opsForSet().add(REDIS_KEY_ALL,key);
    }

    public void deleteKeyAll(){
        //删除Hash
        redisTemplate.delete(REDIS_KEY);
        //删除Set
        redisTemplate.delete(redisTemplate.opsForSet().members(REDIS_KEY_ALL));
        //删除自己
        redisTemplate.delete(REDIS_KEY_ALL);
    }

}
