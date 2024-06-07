package com.example;

import com.example.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class DataService {
    public static final Logger logger = LoggerFactory.getLogger(DataService.class);

    public JedisDatabase jedisDatabase;

    public HttpClient httpClient;

    @Autowired
    DataService(JedisDatabase jedisDatabase, HttpClient httpClient){
        this.jedisDatabase = jedisDatabase;
        this.httpClient = httpClient;
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

    public String requestPoi(String id){
        return httpClient.request(id);
    }
}
