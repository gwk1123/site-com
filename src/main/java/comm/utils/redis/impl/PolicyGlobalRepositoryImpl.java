package comm.utils.redis.impl;

import comm.repository.entity.PolicyGlobal;
import comm.utils.copy.CopyUtils;
import comm.utils.exception.CustomException;
import comm.utils.redis.util.RedisCacheKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author gwk
 */
@Component
public class PolicyGlobalRepositoryImpl {

    @Autowired
    private RedisTemplate redisTemplate;
    private final static String REDIS_KEY = "sibePolicyGlobal";


    public PolicyGlobal saveOrUpdate(PolicyGlobal item) {
        PolicyGlobal policyGlobal = CopyUtils.deepCopy(item);
        if (policyGlobal == null
                || StringUtils.isEmpty(policyGlobal.getOtaSiteCode())
                || StringUtils.isEmpty(policyGlobal.getOtaCode())
        ) {
            throw new CustomException("OTA规则信息不能为空");
        }
        return this.saveOrUpdateCache(policyGlobal);
    }

    public PolicyGlobal saveOrUpdateCache(PolicyGlobal policyGlobal) {
        String key = RedisCacheKeyUtil.getPolicyGlobalRedisCacheKey(policyGlobal);
        redisTemplate.opsForHash().put(REDIS_KEY, key, policyGlobal);
        redisTemplate.opsForSet().add(REDIS_KEY + ":s:" + policyGlobal.getOtaSiteCode(), key);
        return policyGlobal;
    }


    public void delete(PolicyGlobal policyGlobal) {
        String key = RedisCacheKeyUtil.getPolicyGlobalRedisCacheKey(policyGlobal);
        redisTemplate.opsForHash().delete(REDIS_KEY, key, policyGlobal);
        redisTemplate.opsForSet().remove(REDIS_KEY + ":s:" + policyGlobal.getOtaSiteCode(), key);
    }
}
