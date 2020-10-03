package com.sibecommon.utils.redis.impl;

import com.sibecommon.repository.entity.SiteRulesSwitch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SiteRulesSwitchRepositoryImpl {

    @Autowired
    private RedisTemplate redisTemplate;
//    private String SWITCH_KEY = "switch";
//
//    //ota站点开关switch_ota_site_ctrip_001 , switch_ota_site_filggy_001
//    private String SWITCH_OTA_SITE_KEY = "switch_ota_site";
//
//    //ota站点开关switch_ota_site_ctrip_001_rule_31 , switch_ota_site_filggy_001_
//    private String SWITCH_OTA_SITE_RULE_KEY = "switch_ota_site_rule";
//
//    //gds站点开关
//    private String SWITCH_GDS_KEY = "switch_gds";
//
//    public List<SiteRulesSwitch> findSiteRulesSwitchByGroupKey(String groupKey) {
//        Set<String> keys = redisTemplate.opsForSet().members(SWITCH_KEY + ":group_key:" + groupKey);
//        return redisTemplate.opsForHash().multiGet(SWITCH_KEY, keys);
//    }
//
//    public SiteRulesSwitch findSiteRulesSwitchByGroupKeyAndParameterKey(String groupKey, String parameterKey) {
//        SiteRulesSwitch siteRulesSwitch = new SiteRulesSwitch();
//        siteRulesSwitch.setGroupKey(groupKey);
//        siteRulesSwitch.setParameterKey(parameterKey);
//        String key = RedisCacheKeyUtil.getSiteRulesSwitchCacheKey(siteRulesSwitch);
//        return (SiteRulesSwitch) redisTemplate.opsForHash().get(SWITCH_KEY, key);
//    }
//
//    public List<SiteRulesSwitch> findAll(){
//        return redisTemplate.opsForHash().values(SWITCH_KEY);
//    }
//
//
//    public SiteRulesSwitch saveOrUpdateCache(SiteRulesSwitch siteRulesSwitch) {
//        if (StringUtils.isEmpty(siteRulesSwitch.getGroupKey())) {
//            throw new CustomException("站点规则开关的分组Key不能为空");
//        }
//        String key = RedisCacheKeyUtil.getSiteRulesSwitchCacheKey(siteRulesSwitch);
//        redisTemplate.opsForHash().put(SWITCH_KEY, key, siteRulesSwitch);
//        redisTemplate.opsForSet().add(SWITCH_KEY + ":group_key:" + siteRulesSwitch.getGroupKey(), key);
//        return siteRulesSwitch;
//    }
//
//    public void delete(SiteRulesSwitch siteRulesSwitch) {
//        String key = RedisCacheKeyUtil.getSiteRulesSwitchCacheKey(siteRulesSwitch);
//        redisTemplate.opsForHash().delete(SWITCH_KEY, key);
//        redisTemplate.opsForSet().remove(SWITCH_KEY + ":group_key:" + siteRulesSwitch.getGroupKey(), key);
//    }

    /**
     * 获取
     * @return
     */
//    public List<SiteRulesSwitch> findSiteRulesSwitchByGroupKeyAndSite(String groupKey,String site){
//        List<SiteRulesSwitch> siteRulesSwitches= findSiteRulesSwitchByGroupKey(String groupKey);
//        if(CollectionUtils.isEmpty(siteRulesSwitches)){
//            return null;
//        }
//        siteRulesSwitches = siteRulesSwitches.stream().filter(Objects::nonNull).filter(f ->(site.equals(f.getGdsCode()))).collect(Collectors.toList());
//        return siteRulesSwitches;
//    }

    private final static String REDIS_KEY_ALL = "site_rules_switch_all";

    public SiteRulesSwitch saveOrUpdateCache(SiteRulesSwitch siteRulesSwitch) {
        String key = siteRulesSwitch.getGroupKey();
        String key1 = siteRulesSwitch.getParameterKey();
        redisTemplate.opsForHash().put(key, key1,siteRulesSwitch);
        addKeyAll(key);
        return siteRulesSwitch;
    }

    public void delete(SiteRulesSwitch siteRulesSwitch) {
        String key = siteRulesSwitch.getGroupKey();
        String key1 = siteRulesSwitch.getParameterKey();
        redisTemplate.opsForHash().delete(key, key1);
    }

    public List<SiteRulesSwitch> findSiteRulesSwitchesByGroupKey(String groupKey){
        return redisTemplate.opsForHash().values(groupKey);
    }

    public SiteRulesSwitch findSiteRulesSwitchesByGroupKeyAndParameterKey(String groupKey,String parameterKey){
        return (SiteRulesSwitch) redisTemplate.opsForHash().get(groupKey,parameterKey);
    }

    public void addKeyAll(String key){
        redisTemplate.opsForSet().add(REDIS_KEY_ALL,key);
    }

    public void deleteKeyAll(){
        redisTemplate.delete(redisTemplate.opsForSet().members(REDIS_KEY_ALL));
    }

}
