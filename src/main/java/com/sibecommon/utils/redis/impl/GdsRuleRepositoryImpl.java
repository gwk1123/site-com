package com.sibecommon.utils.redis.impl;

import com.sibecommon.repository.entity.GdsRule;
import com.sibecommon.utils.copy.CopyUtils;
import com.sibecommon.utils.exception.CustomException;
import com.sibecommon.utils.redis.util.RedisCacheKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class GdsRuleRepositoryImpl {

    @Autowired
    private RedisTemplate redisTemplate;

    private final static String REDIS_KEY = "gds_rule";
    private final static String REDIS_KEY_ALL = "gds_rule_all";

    public GdsRule saveOrUpdate(GdsRule otaRule) {
        return this.saveOrUpdateCache(otaRule);
    }

    public GdsRule saveOrUpdateCache(GdsRule item) {
        GdsRule gdsRule = CopyUtils.deepCopy(item);
        if (gdsRule == null
                || StringUtils.isEmpty(gdsRule.getGdsCode())
                || StringUtils.isEmpty(gdsRule.getRuleType())
        ) {
            throw new CustomException("GDS规则信息不能为空");
        }

        String key = RedisCacheKeyUtil.getGdsRuleCacheKey(gdsRule);
        redisTemplate.opsForHash().put(REDIS_KEY, key, gdsRule);
        String key1 = REDIS_KEY+":r:"+  gdsRule.getRuleType();
        redisTemplate.opsForSet().add(key1,key);
        String key2 = REDIS_KEY+":g:"+gdsRule.getGdsCode()+":r:"+ gdsRule.getRuleType();
        redisTemplate.opsForSet().add(key2,key);
        addKeyAll(key1);
        addKeyAll(key2);
        return gdsRule;
    }


    public void delete(GdsRule gdsRule) {
        String key = RedisCacheKeyUtil.getGdsRuleCacheKey(gdsRule);
        redisTemplate.opsForHash().delete(REDIS_KEY, key);
        redisTemplate.opsForSet().remove(REDIS_KEY+":r:"+
                gdsRule.getRuleType(),key);
        redisTemplate.opsForSet().remove(REDIS_KEY+":g:"+gdsRule.getGdsCode()+":r:"+
                gdsRule.getRuleType(),key);
    }

    public List<GdsRule> findAll(){
        return redisTemplate.opsForHash().values(REDIS_KEY);
    }

    public List<GdsRule> findGdsRulesByRuleType(String ruleType){
       Set<GdsRule> keys=redisTemplate.opsForSet().members(REDIS_KEY + ":r:" + ruleType);
        return redisTemplate.opsForHash().multiGet(REDIS_KEY, keys);
    }

    public List<GdsRule> findGdsRulesByGdsCodeAndRuleType(String gdsCode,String ruleType){
        Set<GdsRule> keys=redisTemplate.opsForSet().members(REDIS_KEY+":g:"+gdsCode+":r:"+
                ruleType);
        return redisTemplate.opsForHash().multiGet(REDIS_KEY, keys);
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
