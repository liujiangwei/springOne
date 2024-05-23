package com.example;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class JedisDatabase {
    private final JedisPool jedisPool;

    JedisDatabase(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(100); // 设置最大连接数
        jedisPoolConfig.setMaxIdle(50); // 设置最大空闲连接数
        jedisPoolConfig.setMinIdle(10); // 设置最小空闲连接数

        jedisPool = new JedisPool(jedisPoolConfig, "localhost", 6379);
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }
}
