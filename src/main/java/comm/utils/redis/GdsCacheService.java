package comm.utils.redis;

import comm.ota.site.SibeSearchRequest;
import comm.ota.site.SibeSearchResponse;

import java.util.Optional;
import java.util.Set;

public interface GdsCacheService {

    /**
     *  删除行程中单个配置的数据
     * @param redisKey
     * @param key
     */
    public void deleteGDS(String redisKey,String key) ;

    /**
     * 保存站点
     * @param gDSSearchResponseDTO
     * @param rediskey
     * @param expireTime
     */
    void saveOrUpdateString(Object gDSSearchResponseDTO, String rediskey, long expireTime);

    /**
     * 保存data
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
     * @param redisKey
     * @param key
     * @param method
     * @return
     */
    public Object findOne(String redisKey, String key, int method );
}
