package comm.utils.redis.impl;

import comm.repository.entity.AllAirports;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllAirportRepositoryImpl {

    @Autowired
    private RedisTemplate redisTemplate;

    private final String AIRPORT_KEY ="AIRPORT_KEY";

    private final String CITY_KEY ="CITY_KEY";



    /**
     * 通过机场码获取机场对像
     */
    public AllAirports findAllAirportByAirport(String airport){
       return  (AllAirports)redisTemplate.opsForHash().get(AIRPORT_KEY,airport);
    }

    /**
     * 通过城市码获取机场对像集合
     */
    public List<AllAirports> findAllAirportsByCity(String city){
        return  (List<AllAirports>)redisTemplate.opsForHash().get(CITY_KEY,city);
    }


}
