package comm.utils.redis.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import comm.config.SibeProperties;
import comm.ota.site.SibeSearchRequest;
import comm.ota.site.SibeSearchResponse;
import comm.utils.compression.CompressUtil;
import comm.utils.redis.GdsCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author gwk
 */
@Service
public class GdsCacheServiceImpl implements GdsCacheService {

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


    @Override
    @Async("repositoryTaskExecutor")
    public void saveDataToRedis(SibeSearchResponse sibeSearchResponse, SibeSearchRequest sibeSearchRequest) {

        //data的缓存时间 = OTA平台站点的时间 + SIBE站点的缓存时间 + 30分钟
        long cacheValidTime = sibeProperties.getOta().getOtaSiteCacheTime() + sibeSearchRequest.getOtaCacheValidTime()*60 + 1800;

        //Long cacheValidTime = Long.valueOf(60 * 60 * 2);//2个小时
        Map<String, String> dataMap = new ConcurrentHashMap<>();

        sibeSearchResponse.getRoutings().stream().filter(Objects::nonNull).forEach(routing->{
            dataMap.put(routing.getId()+"",routing.getSibeRoutingData().getDecryptData());
        });

        this.saveOrUpdate(dataMap,sibeSearchRequest.getSite()+"-"+sibeSearchRequest.getUuid(), "", cacheValidTime,2);
    }



    //GDS 保存 //map 是 方案数
    //method 1 -GDS  ,2 - data
    public void saveOrUpdate(Object gDSSearchResponseDTO, String redisKey, String key, long expireTime, int method)  {

        if(method != 2 && "true".equals(sibeProperties.getCompass().getSwitchgds())){
            saveOrUpdateCompass(gDSSearchResponseDTO, redisKey,  key, expireTime) ;
//            redisTemplate.opsForValue().set(redisKey+key+"-GDS", "1",expireTime,TimeUnit.SECONDS);
        }else {
            if (gDSSearchResponseDTO instanceof java.util.Map) {
                redisTemplate.opsForHash().putAll(redisKey, (java.util.Map<?, ?>) gDSSearchResponseDTO);
            } else {
                redisTemplate.opsForHash().put(redisKey, key, gDSSearchResponseDTO);
            }
            redisTemplate.expire(redisKey, expireTime, TimeUnit.SECONDS);

        }

    }


    //GDS 保存-压缩
    public void saveOrUpdateCompass(Object gDSSearchResponseDTO,String redisKey,String key,long expireTime)   {

        Map <String ,byte[]> byMap=  new ConcurrentHashMap() ;
        if (gDSSearchResponseDTO instanceof Map){
            ((Map<String,String>)gDSSearchResponseDTO).forEach( (k,v)->{
                if("GZIP".equals(sibeProperties.getCompass().getCompresstype())){
                    byMap.put(k, (CompressUtil.compressGIP(v))) ;
                }/*else if("LZO".equals(sibeProperties.getCompass().getCompresstype())){
                    byMap.put(k, (CompressUtil.compressLZO(v))) ;
                }else{
                    byMap.put(k, (CompressUtil.compressSnappy(v))) ;
                }*/
            } );

            redisTemplate.opsForHash().putAll(redisKey, byMap);
        }else {
            if("GZIP".equals(sibeProperties.getCompass().getCompresstype())){
                redisTemplate.opsForHash().put(redisKey, key,  CompressUtil.compressGIP(gDSSearchResponseDTO));
            }/*else if("LZO".equals(sibeProperties.getCompass().getCompresstype())){
                redisTemplate.opsForHash().put(redisKey, key,  CompressUtil.compressLZO(gDSSearchResponseDTO));
            }else{
                redisTemplate.opsForHash().put(redisKey, key,  CompressUtil.compressSnappy(gDSSearchResponseDTO));
            }*/
        }
        redisTemplate.expire(redisKey,expireTime, TimeUnit.SECONDS);
    }



}
