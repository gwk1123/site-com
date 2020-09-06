package comm.utils.redis;

public interface GdsCacheService {

    /**
     *  删除行程中单个配置的数据
     * @param redisKey
     * @param key
     */
    public void deleteGDS(String redisKey,String key) ;
}
