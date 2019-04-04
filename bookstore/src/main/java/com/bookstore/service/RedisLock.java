package com.bookstore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /*
    @Param key
    @Param value current time + expiration time
     */
    public boolean lock(String key, String value){
        if(redisTemplate.opsForValue().setIfAbsent(key, value)){
            return true;
        }

        String currentValue = redisTemplate.opsForValue().get(key);

        // if outdate
        if(!StringUtils.isEmpty(currentValue)
            && Long.parseLong(currentValue) < System.currentTimeMillis()){
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
            if(!StringUtils.isEmpty(oldValue) &&
                oldValue.equals(currentValue)){
                return true;
            }
        }
        return false;
    }

    public void unlock(String key, String value){
        try{
            String currentValue = redisTemplate.opsForValue().get(key);
            if(!StringUtils.isEmpty(currentValue) && currentValue.equals(value)){
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        }catch (Exception e){
            log.error("[redis distributed lock] unlock error, {}",e);
        }

    }
}
