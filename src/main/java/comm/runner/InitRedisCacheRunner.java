package comm.runner;

import comm.repository.mapper.*;
import comm.utils.redis.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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

    @Override
    public void run(String... args) throws Exception {

    }



}
