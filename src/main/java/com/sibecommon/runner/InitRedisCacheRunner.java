package com.sibecommon.runner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sibecommon.repository.entity.*;
import com.sibecommon.repository.mapper.*;
import com.sibecommon.repository.entity.*;
import com.sibecommon.repository.mapper.*;
import com.sibecommon.utils.constant.DirectConstants;
import com.sibecommon.utils.redis.impl.*;
import com.sibecommon.utils.redis.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.sibecommon.repository.entity.*;
import com.sibecommon.repository.mapper.*;
import com.sibecommon.utils.redis.impl.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
public class InitRedisCacheRunner implements CommandLineRunner {

    @Autowired
    private AllAirportsMapper allAirportsMapper;
    @Autowired
    private AllAirportRepositoryImpl allAirportRepository;
    @Autowired
    private ExchangeRateMapper exchangeRateMapper;
    @Autowired
    private ExchangeRateRepositoryImpl exchangeRateRepository;
    @Autowired
    private GdsMapper gdsMapper;
    @Autowired
    private GdsRepositoryImpl gdsRepository;
    @Autowired
    private GdsPccMapper gdsPccMapper;
    @Autowired
    private GdsPccRepositoryImpl gdsPccRepository;
    @Autowired
    private GdsRuleMapper gdsRuleMapper;
    @Autowired
    private GdsRuleRepositoryImpl gdsRuleRepository;
    @Autowired
    private OtaMapper otaMapper;
    @Autowired
    private OtaRepositoryImpl otaRepository;
    @Autowired
    private OtaSiteMapper otaSiteMapper;
    @Autowired
    private OtaSiteRepositoryImpl otaSiteRepository;
    @Autowired
    private OtaRuleMapper otaRuleMapper;
    @Autowired
    private OtaRuleRepositoryImpl otaRuleRepository;
    @Autowired
    private PolicyGlobalMapper policyGlobalMapper;
    @Autowired
    private PolicyGlobalRepositoryImpl policyGlobalRepository;
    @Autowired
    private PolicyInfoMapper policyInfoMapper;
    @Autowired
    private PolicyInfoRepositoryImpl policyInfoRepository;
    @Autowired
    private RouteConfigMapper routeConfigMapper;
    @Autowired
    private RouteConfigRepositoryImpl routeConfigRepository;
    @Autowired
    private SiteRulesSwitchMapper siteRulesSwitchMapper;
    @Autowired
    private SiteRulesSwitchRepositoryImpl siteRulesSwitchRepository;

    @Autowired
    @Qualifier("asyncTimeShortExecutor")
    private Executor asyncTimeShortExecutor;

    private Logger logger=LoggerFactory.getLogger(InitRedisCacheRunner.class);

    @Override
    public void run(String... args){
        logger.info("开始初始化缓存。。。。");
        initAllAirportsCache();
        initExchangeRateCache();
        initGdsCache();
        initGdsPccCache();
        initGdsRuleCache();
        initOtaCache();
        initOtaSiteCache();
        initOtaRuleCache();
        initPolicyGlobalCache();
        initPolicyInfoCache();
        initRouteConfigCache();
        initSiteRulesSwitchCache();
        logger.info("缓存已完成。。。。");
    }

    public void initAllAirportsCache(){
        long t1 = System.currentTimeMillis();
        logger.info("初始化机场码。。。。。");
        //删除缓存
        allAirportRepository.deleteKeyAll();
        //初始化缓存
        QueryWrapper<AllAirports> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(AllAirports::getStatus, DirectConstants.NORMAL);
        List<AllAirports> allAirports= allAirportsMapper.selectList(queryWrapper);
        allAirports.stream().filter(Objects::nonNull).forEach(e ->{
            CompletableFuture.runAsync(()->{
                allAirportRepository.saveOrUpdateCache(e);
            },asyncTimeShortExecutor);
        });
        long t2 = System.currentTimeMillis();
//        logger.info("耗时{}s,机场码已完成。。。。。",(t2-t2)/1000);
    }

    public void initExchangeRateCache(){
        exchangeRateRepository.deleteKeyAll();
        //初始化缓存
        QueryWrapper<ExchangeRate> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(ExchangeRate::getStatus, DirectConstants.NORMAL);
        List<ExchangeRate> exchangeRates= exchangeRateMapper.selectList(queryWrapper);
        exchangeRates.stream().filter(Objects::nonNull).forEach(e ->{
            exchangeRateRepository.saveOrUpdateCache(e);
        });
    }

    public void initGdsCache(){
        gdsRepository.deleteKeyAll();
        //初始化缓存
        QueryWrapper<Gds> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(Gds::getStatus, DirectConstants.NORMAL);
        List<Gds> gdss= gdsMapper.selectList(queryWrapper);
        gdss.stream().filter(Objects::nonNull).forEach(e ->{
            gdsRepository.saveOrUpdateCache(e);
        });
    }

    public void initGdsPccCache(){
        gdsPccRepository.deleteKeyAll();
        QueryWrapper<GdsPcc> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(GdsPcc::getStatus, DirectConstants.NORMAL);
        List<GdsPcc> gdsPccs= gdsPccMapper.selectList(queryWrapper);
        gdsPccs.stream().filter(Objects::nonNull).forEach(e ->{
            gdsPccRepository.saveOrUpdateCache(e);
        });
    }

    public void initGdsRuleCache(){
        gdsRuleRepository.deleteKeyAll();
        QueryWrapper<GdsRule> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(GdsRule::getStatus, DirectConstants.NORMAL);
        List<GdsRule> gdsRules= gdsRuleMapper.selectList(queryWrapper);
        gdsRules.stream().filter(Objects::nonNull).forEach(e ->{
            gdsRuleRepository.saveOrUpdateCache(e);
        });
    }

    public void initOtaCache(){
        otaRepository.deleteKeyAll();
        //初始化缓存
        QueryWrapper<Ota> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(Ota::getStatus, DirectConstants.NORMAL);
        List<Ota> otas= otaMapper.selectList(queryWrapper);
        otas.stream().filter(Objects::nonNull).forEach(e ->{
            otaRepository.saveOrUpdateCache(e);
        });
    }

    public void initOtaSiteCache(){
        otaSiteRepository.deleteKeyAll();
        //初始化缓存
        QueryWrapper<OtaSite> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(OtaSite::getStatus, DirectConstants.NORMAL);
        List<OtaSite> otaSites= otaSiteMapper.selectList(queryWrapper);
        otaSites.stream().filter(Objects::nonNull).forEach(e ->{
            otaSiteRepository.saveOrUpdateCache(e);
        });
    }

    public void initOtaRuleCache(){
        otaRuleRepository.deleteKeyAll();
        //初始化缓存
        QueryWrapper<OtaRule> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(OtaRule::getStatus, DirectConstants.NORMAL);
        List<OtaRule> otaRules= otaRuleMapper.selectList(queryWrapper);
        otaRules.stream().filter(Objects::nonNull).forEach(e ->{
            otaRuleRepository.saveOrUpdateCache(e);
        });
    }

    public void initPolicyGlobalCache(){
        policyGlobalRepository.deleteKeyAll();
        //初始化缓存
        QueryWrapper<PolicyGlobal> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(PolicyGlobal::getStatus, DirectConstants.NORMAL);
        List<PolicyGlobal> policyGlobals= policyGlobalMapper.selectList(queryWrapper);
        policyGlobals.stream().filter(Objects::nonNull).forEach(e ->{
            policyGlobalRepository.saveOrUpdateCache(e);
        });
    }

    public void initPolicyInfoCache(){
        policyInfoRepository.deleteKeyAll();
        //初始化缓存
        QueryWrapper<PolicyInfo> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(PolicyInfo::getStatus, DirectConstants.NORMAL);
        List<PolicyInfo> policyInfos= policyInfoMapper.selectList(queryWrapper);
        policyInfos.stream().filter(Objects::nonNull).forEach(e ->{
            policyInfoRepository.saveOrUpdateCache(e);
        });
    }

    public void initRouteConfigCache(){
        routeConfigRepository.deleteKeyAll();
        //初始化缓存
        QueryWrapper<RouteConfig> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(RouteConfig::getStatus, DirectConstants.NORMAL);
        List<RouteConfig> routeConfigs= routeConfigMapper.selectList(queryWrapper);
        routeConfigs.stream().filter(Objects::nonNull).forEach(e ->{
            routeConfigRepository.saveOrUpdateCache(e);
        });
    }

    public void initSiteRulesSwitchCache(){
        siteRulesSwitchRepository.deleteKeyAll();
        //初始化缓存
        QueryWrapper<SiteRulesSwitch> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(SiteRulesSwitch::getStatus, DirectConstants.NORMAL);
        List<SiteRulesSwitch> siteRulesSwitches= siteRulesSwitchMapper.selectList(queryWrapper);
        siteRulesSwitches.stream().filter(Objects::nonNull).forEach(e ->{
            siteRulesSwitchRepository.saveOrUpdateCache(e);
        });
    }

}
