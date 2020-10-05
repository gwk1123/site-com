package com.sibecommon.utils.redis.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibecommon.utils.redis.GdsCacheService;
import com.sibecommon.config.SibeProperties;
import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.ota.site.SibeSearchResponse;
import com.sibecommon.utils.compression.CompressUtil;
import com.sibecommon.utils.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author gwk
 */
@Service
public class GdsCacheServiceImpl implements GdsCacheService {

    private Logger logger = LoggerFactory.getLogger(GdsCacheServiceImpl.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SibeProperties sibeProperties;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void deleteGDS(String redisKey, String key) {
        redisTemplate.opsForHash().delete(redisKey, key);
    }


    @Override
    public void saveOrUpdateString(Object gDSSearchResponseDTO, String rediskey, long expireTime) {

        try {
            if ("true".equals(sibeProperties.getCompass().getSwitchgds())) {
                if ("GZIP".equals(sibeProperties.getCompass().getCompresstype())) {
                    redisTemplate.opsForValue().set(rediskey, CompressUtil.compressGIP(objectMapper.writeValueAsString(gDSSearchResponseDTO)), expireTime, TimeUnit.SECONDS);
                }
            } else {
                String gDSSearchResponseString = null;
                gDSSearchResponseString = objectMapper.writeValueAsString(gDSSearchResponseDTO);
                redisTemplate.opsForValue().set(rediskey, gDSSearchResponseString, expireTime, TimeUnit.SECONDS);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //站点
    public Object findString(String redisKey){
        Object airlineSolutions = null ;
        if("true".equals(sibeProperties.getCompass().getSwitchgds())){
            airlineSolutions = findStringCompress(redisKey) ;
        }else{
            airlineSolutions = redisTemplate.opsForValue().get(redisKey);
        }
        return airlineSolutions;
    }


    @Override
    @Async("asyncExecutor")
    public void saveDataToRedis(SibeSearchResponse sibeSearchResponse, SibeSearchRequest sibeSearchRequest) {

        //data的缓存时间 = OTA平台站点的时间 + SIBE站点的缓存时间 + 30分钟
        long cacheValidTime = sibeProperties.getOta().getOtaSiteCacheTime() + sibeSearchRequest.getOtaCacheValidTime() * 60 + 1800;

        //Long cacheValidTime = Long.valueOf(60 * 60 * 2);//2个小时
        Map<String, String> dataMap = new ConcurrentHashMap<>();

        sibeSearchResponse.getRoutings().stream().filter(Objects::nonNull).forEach(routing -> {
            dataMap.put(routing.getId() + "", routing.getSibeRoutingData().getDecryptData());
        });

        this.saveOrUpdate(dataMap, sibeSearchRequest.getSite() + "-" + sibeSearchRequest.getUuid(), "", cacheValidTime, 2);
    }


    //GDS 保存 //map 是 方案数
    //method 1 -GDS  ,2 - data
    @Override
    public void saveOrUpdate(Object gDSSearchResponseDTO, String redisKey, String key, long expireTime, int method) {

        if (method != 2 && "true".equals(sibeProperties.getCompass().getSwitchgds())) {
            saveOrUpdateCompass(gDSSearchResponseDTO, redisKey, key, expireTime);
//            redisTemplate.opsForValue().set(redisKey+key+"-GDS", "1",expireTime,TimeUnit.SECONDS);
        } else {
            if (gDSSearchResponseDTO instanceof java.util.Map) {
                redisTemplate.opsForHash().putAll(redisKey, (java.util.Map<?, ?>) gDSSearchResponseDTO);
            } else {
                redisTemplate.opsForHash().put(redisKey, key, gDSSearchResponseDTO);
            }
            redisTemplate.expire(redisKey, expireTime, TimeUnit.SECONDS);

        }

    }


    //GDS 保存-压缩
    public void saveOrUpdateCompass(Object gDSSearchResponseDTO, String redisKey, String key, long expireTime) {

        Map<String, byte[]> byMap = new ConcurrentHashMap();
        if (gDSSearchResponseDTO instanceof Map) {
            ((Map<String, String>) gDSSearchResponseDTO).forEach((k, v) -> {
                if ("GZIP".equals(sibeProperties.getCompass().getCompresstype())) {
                    byMap.put(k, (CompressUtil.compressGIP(v)));
                }
            });

            redisTemplate.opsForHash().putAll(redisKey, byMap);
        } else {
            if ("GZIP".equals(sibeProperties.getCompass().getCompresstype())) {
                redisTemplate.opsForHash().put(redisKey, key, CompressUtil.compressGIP(gDSSearchResponseDTO));
            }
        }
        redisTemplate.expire(redisKey, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 查找GDS缓存的内容
     */
    @Override
    public Set<String> findAllKeys(String redisKey) {
        Set<String> keySet = redisTemplate.opsForHash().keys(redisKey);
        return keySet;
    }


    //GDS 获取
    @Override
    public Object findOne(String redisKey, String key, int method) {
        //存储的类型
        Object airlineSolutions = null;
        if (method != 2 && "true".equals(sibeProperties.getCompass().getSwitchgds())) {
            airlineSolutions = (Object) findOneCompass(redisKey, key);
        } else {
            airlineSolutions = (Object) redisTemplate.opsForHash().get(redisKey, key);
        }
        return airlineSolutions;

    }


    //GDS 获取-压缩
    public Object findOneCompass(String redisKey, String key) {

        try {
            if ("GZIP".equals(sibeProperties.getCompass().getCompresstype())) {
                return CompressUtil.unCompressGIP(redisTemplate.opsForHash().get(redisKey, key));
            }
        } catch (Exception e) {
            logger.error("解压异常" + e);
        }

        return null;
    }

    /**
     * 获取GDS的map
     * @param key
     * @return
     */
    public Object findSaveSearchKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void saveSearchKey(String key) {
        redisTemplate.opsForValue().set(key,"OK", Constants.IDEMPOTENT_SEARCH_TIME_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 删除缓存
     * @param redisKey
     */
    @Override
    public void delete(String redisKey) {
        redisTemplate.delete(redisKey);
    }


    //站点 获取-压缩
    @Override
    public Object findStringCompress(String rediskey) {
        try {
            if("GZIP".equals(sibeProperties.getCompass().getCompresstype())){
                return  CompressUtil.unCompressGIP(redisTemplate.opsForValue().get(rediskey)) ;
            }
        } catch (Exception e) {
            logger.error("解压异常:{}", e);
        }
        return null;
    }


    //站点保存-压缩
    @Override
    public Object saveOrUpdateStringCompass(Object gDSSearchResponseDTO,String redisKey,long expireTime){
        try {
            if("GZIP".equals(sibeProperties.getCompass().getCompresstype())){
                redisTemplate.opsForValue().set(redisKey, CompressUtil.compressGIP(objectMapper.writeValueAsString(gDSSearchResponseDTO)), expireTime, TimeUnit.SECONDS);
            }
        }catch (Exception e) {
            logger.error("压缩异常:{}", e);
        }
        return Optional.ofNullable(gDSSearchResponseDTO);
    }

}
