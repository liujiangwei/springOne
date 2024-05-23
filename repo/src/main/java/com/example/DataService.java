package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class DataService {
    public static final Logger logger = LoggerFactory.getLogger(DataService.class);

    public JedisDatabase jedisDatabase;

    DataService(JedisDatabase jedisDatabase){
        this.jedisDatabase = jedisDatabase;
    }

    public String get(String key){
        try {
            Jedis jedis  = jedisDatabase.getJedis();
            String str = jedis.get(key);
            jedis.close();

            return str;
        }catch (Exception e){
            logger.error("redis get error",e);
            return "service error";
        }
    }
}
