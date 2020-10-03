package com.sibecommon.utils.redis.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibecommon.repository.entity.CarrierCabinBlack;
import com.sibecommon.utils.redis.util.RedisCacheKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CarrierCabinBlackRepositoryImpl {

    @Autowired
    private RedisTemplate redisTemplate;

    private ObjectMapper objectMapper=new ObjectMapper();
    private final static String REDIS_KEY = "sibe_carrier_cabinBlack_list";

    public List<CarrierCabinBlack> findAll() {
        List<Object> apiCarrierCabinBlackListRedisObjects = redisTemplate.opsForHash().values(REDIS_KEY);
        if (apiCarrierCabinBlackListRedisObjects==null||apiCarrierCabinBlackListRedisObjects.size()==0) {
            return new ArrayList<>();
        }
        return apiCarrierCabinBlackListRedisObjects.stream()
                .map(apiCarrierCabinBlackListItem -> objectMapper.convertValue(apiCarrierCabinBlackListItem, CarrierCabinBlack.class))
                .collect(Collectors.toList());
    }

    public Set<CarrierCabinBlack> findByCarriers(Set<String> carriers) {
        String airlineKey = REDIS_KEY + ":airline:" + carriers.iterator().next();
        Set<String> otherAirlineKeySet = carriers.stream().map(airline -> REDIS_KEY + ":airline:" + airline).collect(Collectors.toSet());
        Set<Object> keys = redisTemplate.opsForSet().union(airlineKey,otherAirlineKeySet);

        List<Object> apiCarrierCabinBlackListRedisObjects = redisTemplate.opsForHash().multiGet(REDIS_KEY, keys);
        return apiCarrierCabinBlackListRedisObjects.stream()
                .map(apiCarrierCabinBlackListItem -> objectMapper.convertValue(apiCarrierCabinBlackListItem, CarrierCabinBlack.class))
                .collect(Collectors.toSet());
    }

    public CarrierCabinBlack save(CarrierCabinBlack apiCarrierCabinBlackListRedis) {
        String key= RedisCacheKeyUtil.getCarrierCabinBlackCacheKey(apiCarrierCabinBlackListRedis);
        redisTemplate.opsForHash().put(REDIS_KEY, key, apiCarrierCabinBlackListRedis);
        redisTemplate.opsForSet().add(REDIS_KEY + ":airline:" + apiCarrierCabinBlackListRedis.getCarrier(), key);
        return apiCarrierCabinBlackListRedis;
    }

    public List<CarrierCabinBlack> saveBatch(List<CarrierCabinBlack> apiCarrierCabinBlackListRedis) {
        List<CarrierCabinBlack> saveList = new ArrayList<>();
        apiCarrierCabinBlackListRedis.forEach(apiCarrierCabinBlackListRedis1 -> {
            CarrierCabinBlack saveItem = save(apiCarrierCabinBlackListRedis1);
            saveList.add(saveItem);
        });
        return saveList;
    }


    public CarrierCabinBlack delete(CarrierCabinBlack apiCarrierCabinBlackListRedis) {
        String key= RedisCacheKeyUtil.getCarrierCabinBlackCacheKey(apiCarrierCabinBlackListRedis);
        redisTemplate.opsForHash().delete(REDIS_KEY, key);
        redisTemplate.opsForSet().remove(REDIS_KEY + ":airline:" + apiCarrierCabinBlackListRedis.getCarrier(), key);
        return apiCarrierCabinBlackListRedis;
    }

    public List<CarrierCabinBlack> delete(List<CarrierCabinBlack> apiCarrierCabinBlackListRedisList) {
        List<CarrierCabinBlack> deleteList = new ArrayList<>();
        apiCarrierCabinBlackListRedisList.forEach(apiCarrierCabinBlackListRedis -> {
            CarrierCabinBlack deleteItem = delete(apiCarrierCabinBlackListRedis);
            deleteList.add(deleteItem);
        });

        return deleteList;
    }

}
