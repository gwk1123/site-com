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

import java.util.List;
import java.util.Set;

/**
 * @author gwk
 */
@Component
public class OtaRuleRepositoryImpl {

    @Autowired
    private RedisTemplate redisTemplate;

    private final static String REDIS_KEY = "ota_rule";
    private final static String REDIS_KEY_ALL = "ota_rule_all";


    public OtaRule saveOrUpdateCache(OtaRule item) {
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
        originInfo = StringUtils.isEmpty(originInfo) ? DirectConstants.ALL : originInfo;
        destinationInfo = StringUtils.isEmpty(destinationInfo) ? DirectConstants.ALL : destinationInfo;
        otaRule.setOrigin(originInfo);
        otaRule.setDestination(destinationInfo);
        return this.saveOrUpdate(otaRule);
    }

    public OtaRule saveOrUpdate(OtaRule otaRule) {
        String key = RedisCacheKeyUtil.getOtaRuleCacheKey(otaRule);
        redisTemplate.opsForHash().put(REDIS_KEY, key, otaRule);
        String key1 = REDIS_KEY+":s:"+otaRule.getOtaSiteCode();
        redisTemplate.opsForSet().add(key1,key);
        String key2 = REDIS_KEY+":s:"+otaRule.getOtaSiteCode()+":r:"+  otaRule.getRuleType();
        redisTemplate.opsForSet().add(key2,key);
        addKeyAll(key1);
        addKeyAll(key2);
        return otaRule;
    }


    public void delete(OtaRule otaRule) {
        String key = RedisCacheKeyUtil.getOtaRuleCacheKey(otaRule);
        redisTemplate.opsForHash().delete(REDIS_KEY, key);
        redisTemplate.opsForSet().add(REDIS_KEY+":s:"+otaRule.getOtaSiteCode(),key);
        redisTemplate.opsForSet().remove(REDIS_KEY+":s:"+otaRule.getOtaSiteCode()+":r:"+
                otaRule.getRuleType(),key);
    }

    public List<OtaRule> findOtaRuleBySite(String site){
        Set<String> keys = redisTemplate.opsForSet().members(REDIS_KEY+":s:"+site);
        List<OtaRule> otaRules = redisTemplate.opsForHash().multiGet(REDIS_KEY,keys);
        return otaRules;
    }

    public List<OtaRule> findOtaRuleBySiteAndRuleType(String site,String ruleType){
        Set<String> keys = redisTemplate.opsForSet().members(REDIS_KEY+":s:"+site+":r:"+ruleType);
        List<OtaRule> otaRules = redisTemplate.opsForHash().multiGet(REDIS_KEY,keys);
        return otaRules;
    }

    public void addKeyAll(String key){
        redisTemplate.opsForSet().add(REDIS_KEY_ALL,key);
    }

    public void deleteKeyAll(){
        redisTemplate.delete(REDIS_KEY);
        redisTemplate.delete(redisTemplate.opsForSet().members(REDIS_KEY_ALL));
        redisTemplate.delete(REDIS_KEY_ALL);
    }

}
