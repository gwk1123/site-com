package com.sibecommon.utils.redis;

import com.sibecommon.ota.site.SibeSearchRequest;
import com.sibecommon.ota.site.SibeSearchResponse;

import java.util.Set;

public interface GdsCacheService {

    /**
     * 删除行程中单个配置的数据
     *
     * @param redisKey
     * @param key
     */
    public void deleteGDS(String redisKey, String key);

    /**
     * 保存站点
     *
     * @param gDSSearchResponseDTO
     * @param rediskey
     * @param expireTime
     */
    void saveOrUpdateString(Object gDSSearchResponseDTO, String rediskey, long expireTime);

    /**
     * 保存data
     *
     * @param sibeSearchResponse
     * @param sibeSearchRequest
     */
    public void saveDataToRedis(SibeSearchResponse sibeSearchResponse, SibeSearchRequest sibeSearchRequest);

    public void saveOrUpdate(Object sibeSearchResponse, String tripCacheKey, String s, long l, int i);

    /**
     * 查找GDS缓存的内容
     */
    public Set<String> findAllKeys(String redisKey);

    /**
     * GDS 获取
     *
     * @param redisKey
     * @param key
     * @param method
     * @return
     */
    public Object findOne(String redisKey, String key, int method);

    Object findSaveSearchKey(String key);

    void saveSearchKey(String key);

    void delete(String redisKey);

    /**
     * 站点 获取
     * @param redisKey
     * @return
     */
    Object findString(String redisKey);

    /**
     *  站点保存-压缩
     */
     Object saveOrUpdateStringCompass(Object gDSSearchResponseDTO,String redisKey,long expireTime);

    /**
     * 站点 获取-压缩
     * @param rediskey
     * @return
     */
     Object findStringCompress(String rediskey);

    /**
     * 可以获取data
     * @param redisKey
     * @return
     */
    Object findOne(String redisKey);
}
