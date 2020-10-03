package com.sibecommon.utils.redis.impl;

import com.sibecommon.repository.entity.AllAirports;
import com.sibecommon.utils.redis.util.RedisCacheKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllAirportRepositoryImpl {

    @Autowired
    private RedisTemplate redisTemplate;

    private final String AIRPORT_KEY ="airport_key";
    private final String AIRPORT_ALL_KEY = "airport_all_key";
    private final String CITY_KEY ="city_key";



    /**
     * 通过机场码获取机场对像
     */
    public AllAirports findAllAirportByAirport(String airport){
       return  (AllAirports)redisTemplate.opsForHash().get(AIRPORT_KEY,airport);
    }

    /**
     * 通过城市码获取机场对像集合
     */
    public List<AllAirports> findAllAirportsByCity(String city){
        return  (List<AllAirports>)redisTemplate.opsForHash().get(CITY_KEY,city);
    }


    public void saveOrUpdateCache(AllAirports allAirports){
        redisTemplate.opsForHash().put(AIRPORT_KEY,allAirports.getCode(),allAirports);
        String key = RedisCacheKeyUtil.getAllAirportsCacheKey(allAirports);
        String key1 = CITY_KEY+":g:"+allAirports.getGcode()+":c:"+ allAirports.getCcode();
        redisTemplate.opsForSet().add(key1,key);
        redisTemplate.opsForHash().put(CITY_KEY,key,allAirports);
        this.addAirportKey(key1);
    }

    public void delete(AllAirports allAirports){
        redisTemplate.opsForHash().delete(AIRPORT_KEY,allAirports.getCode());
        String key = RedisCacheKeyUtil.getAllAirportsCacheKey(allAirports);
        redisTemplate.opsForSet().remove(CITY_KEY+":g:"+allAirports.getGcode()+":c:"+
                allAirports.getCcode(),key);
        redisTemplate.opsForHash().delete(CITY_KEY,key);
    }



    public void addAirportKey(String key){
        redisTemplate.opsForSet().add(AIRPORT_ALL_KEY,key);
    }

    public void deleteKeyAll(){
        redisTemplate.delete(AIRPORT_KEY);
        redisTemplate.delete(CITY_KEY);
        redisTemplate.delete(redisTemplate.opsForSet().members(AIRPORT_ALL_KEY));
        redisTemplate.delete(AIRPORT_ALL_KEY);
    }
}
