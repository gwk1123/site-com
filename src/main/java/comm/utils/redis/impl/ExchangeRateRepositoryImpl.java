package comm.utils.redis.impl;

import comm.repository.entity.ExchangeRate;
import comm.repository.entity.GdsPcc;
import comm.utils.redis.util.RedisCacheKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Component
public class ExchangeRateRepositoryImpl {

    @Autowired
    private RedisTemplate redisTemplate;
    private String EXCHANGE_RATE_KEY = "exchange_rate";

    public ExchangeRate findExchangeRate(String key){
        return (ExchangeRate) redisTemplate.opsForHash().get(EXCHANGE_RATE_KEY, key);
    }

    public Map<String, BigDecimal> findAllExchangeRate(){
        return (Map<String, BigDecimal>) redisTemplate.opsForHash().entries(EXCHANGE_RATE_KEY);
    }


    public ExchangeRate saveOrUpdateCache(ExchangeRate exchangeRate) {

        String key = RedisCacheKeyUtil.getExchangeRateCacheKey(exchangeRate);
        redisTemplate.opsForHash().put(EXCHANGE_RATE_KEY, key, exchangeRate.getForexSellRate());
        return exchangeRate;
    }

    public void delete(ExchangeRate exchangeRate) {
        String key = RedisCacheKeyUtil.getExchangeRateCacheKey(exchangeRate);
        redisTemplate.opsForHash().delete(EXCHANGE_RATE_KEY, key);
    }

    public void deleteKeyAll(){
        redisTemplate.delete(EXCHANGE_RATE_KEY);
    }


}
