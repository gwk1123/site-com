package comm.utils.redis.impl;

import comm.repository.entity.OtaRule;
import comm.repository.entity.SiteRulesSwitch;
import comm.utils.constant.SibeConstants;
import comm.utils.exception.CustomException;
import comm.utils.exception.CustomSibeException;
import comm.utils.redis.util.RedisCacheKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SiteRulesSwitchRepositoryImpl {

    @Autowired
    private RedisTemplate redisTemplate;
    private String SWITCH_KEY = "switch";

    public Set<SiteRulesSwitch> findSiteRulesSwitchByGroupKey(String groupKey){
        Set<String> keys = redisTemplate.opsForSet().members(SWITCH_KEY + ":type:" + groupKey);
        return (Set<SiteRulesSwitch>) redisTemplate.opsForHash().multiGet(SWITCH_KEY, keys);
    }

    public SiteRulesSwitch findSiteRulesSwitchByGroupKeyAndParameterKey(String groupKey,String parameterKey){
        SiteRulesSwitch siteRulesSwitch=new SiteRulesSwitch();
        siteRulesSwitch.setGroupKey(groupKey);
        siteRulesSwitch.setParameterKey(parameterKey);
        String key = RedisCacheKeyUtil.getSiteRulesSwitchCacheKey(siteRulesSwitch);
        return (SiteRulesSwitch) redisTemplate.opsForHash().get(SWITCH_KEY,key);
    }


    public SiteRulesSwitch saveOrUpdateCache(SiteRulesSwitch siteRulesSwitch) {
        if(StringUtils.isEmpty(siteRulesSwitch.getGroupKey())){
            throw new CustomException("站点规则开关的分组Key不能为空");
        }
        String key = RedisCacheKeyUtil.getSiteRulesSwitchCacheKey(siteRulesSwitch);
        redisTemplate.opsForHash().put(SWITCH_KEY, key, siteRulesSwitch);
        redisTemplate.opsForSet().add(SWITCH_KEY + ":type:" + siteRulesSwitch.getGroupKey(),key);
        return siteRulesSwitch;
    }

    public void delete(SiteRulesSwitch siteRulesSwitch) {
        String key = RedisCacheKeyUtil.getSiteRulesSwitchCacheKey(siteRulesSwitch);
        redisTemplate.opsForHash().delete(SWITCH_KEY, key);
        redisTemplate.opsForSet().remove(SWITCH_KEY + ":type:" + siteRulesSwitch.getGroupKey(),key);
    }



}
