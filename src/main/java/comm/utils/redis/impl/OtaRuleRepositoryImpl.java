package comm.utils.redis.impl;

import comm.repository.entity.OtaRule;
import comm.utils.constant.DirectConstants;
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
public class OtaRuleRepositoryImpl {

    @Autowired
    private RedisTemplate redisTemplate;

    private final static String REDIS_KEY = "sibe_ota_rule";


    public OtaRule saveOrUpdate(OtaRule item) {
        OtaRule otaRule = CopyUtils.deepCopy(item);
        if (otaRule == null
                || StringUtils.isEmpty(otaRule.getOtaCode())
                || StringUtils.isEmpty(otaRule.getOtaSiteCode())
                || StringUtils.isEmpty(otaRule.getRuleType())
        ) {
            throw new CustomException("OTA规则信息不能为空");
        }
        //如果出发地，或者目的地为空，则不限(UNLIMIT)， 不转换为TC1/TC2/TC3(增加重复的内容)
        String originInfo = otaRule.getOrigin();
        String destinationInfo = otaRule.getDestination();
        originInfo = StringUtils.isEmpty(originInfo) ? DirectConstants.ALL  : originInfo;
        destinationInfo = StringUtils.isEmpty(destinationInfo) ? DirectConstants.ALL : destinationInfo;
        otaRule.setOrigin(originInfo);
        otaRule.setDestination(destinationInfo);
        return this.saveOrUpdateCache(otaRule);
    }

    public OtaRule saveOrUpdateCache(OtaRule otaRule) {
        String key = RedisCacheKeyUtil.getOtaRuleCacheKey(otaRule);
        redisTemplate.opsForHash().put(REDIS_KEY, key, otaRule);
        redisTemplate.opsForSet().add(REDIS_KEY+":s:"+otaRule.getOtaSiteCode()+":r:"+
                otaRule.getRuleType(),key);
        return otaRule;
    }


    public void delete(OtaRule otaRule) {
        String key = RedisCacheKeyUtil.getOtaRuleCacheKey(otaRule);
        redisTemplate.opsForHash().delete(REDIS_KEY, key);
        redisTemplate.opsForSet().remove(REDIS_KEY+":s:"+otaRule.getOtaSiteCode()+":r:"+
                otaRule.getRuleType(),key);
    }

}
