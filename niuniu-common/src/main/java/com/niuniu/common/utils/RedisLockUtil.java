package com.niuniu.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁
 * @author niuniu
 */
@Component
public class RedisLockUtil {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 活跃锁key+value集合，续期的时候会使用
    private volatile static CopyOnWriteArraySet activeLockKeySet = new CopyOnWriteArraySet();
    // 定时线程池 用于续期
    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);

    /**
     * 加锁
     * @Param key
     * @Param value 锁的value一般使用线程ID，在解锁时需要使用
     * @Param expireTime 过期时间 单位秒
     */
    public boolean lock(String key, String value, long expireTime) {
        // 为了实现锁的可重入这里要自己封装一个lua脚本，如果不考虑可重入可以直接使用redisTemplate.opsForValue().setIfAbsent(K key, V value, long timeout, TimeUnit unit)
        String lockLua = getLockLua();
        boolean result = executeLua(lockLua, key, value, String.valueOf(expireTime));
        // 当加锁成功且活跃锁key+value不在集合中则添加续期任务
        if (result && !activeLockKeySet.contains(key + value)) {
            // 将活跃锁key+value放入集合中
            activeLockKeySet.add(key + value);
            // 加锁成功添加续期任务
            resetExpire(key, value, expireTime);
        }
        return result;
    }

    /**
     * 获取加锁lua脚本
     */
    private String getLockLua() {
        // 封装加锁lua脚本 PS: 这个lua脚本应该是要定义成全局的，我这里为了演示定义成局部组装方便介绍每一步流程
        // lua脚本参数介绍 KEYS[1]：传入的key  ARGV[1]：传入的value  ARGV[2]：传入的过期时间
        // 在使用redisTemplate执行lua脚本时会传入key数组和参数数组，List<K> keys, Object... args，在lua脚本中取key值和参数值时使用KEYS和ARGV，数组下标从1开始
        StringBuilder lockLua = new StringBuilder();
        // 通过SETNX命令设置锁，如果设置成功则添加一个过期时间并且返回1，否则判断是否为重入锁
        lockLua.append("if redis.call('SETNX', KEYS[1], ARGV[1]) == 1 then\n");
        lockLua.append("    redis.call('EXPIRE', KEYS[1], tonumber(ARGV[2]))\n");
        lockLua.append("    return 1\n");
        lockLua.append("else\n");
        // 当锁已经存在时，判断传入的value是否相等，如果相等代表为重入锁返回1并且重置过期时间，否则返回0
        lockLua.append("    if redis.call('GET', KEYS[1]) == ARGV[1] then\n");
        lockLua.append("        redis.call('EXPIRE', KEYS[1], tonumber(ARGV[2]))\n");
        lockLua.append("        return 1\n");
        lockLua.append("    else\n");
        lockLua.append("        return 0\n");
        lockLua.append("    end\n");
        lockLua.append("end");
        return lockLua.toString();
    }

    /**
     * 解锁
     * @Param key
     * @Param value 锁的value一般使用线程ID，在解锁时需要判断是当前线程才运行删除
     */
    public boolean unlock(String key, String value) {
        // 为了实现避免误删锁，这里要自己封装一个lua脚本
        String unLockLua = getUnlockLua();
        boolean result = executeLua(unLockLua, key, value);
        if (result) {
            // 将活跃锁key+value从集合中删除
            activeLockKeySet.remove(key + value);
        }
        return result;
    }

    /**
     * 获取解锁lua脚本
     */
    private String getUnlockLua() {
        // 封装解锁lua脚本 PS: 这个lua脚本应该是要定义成全局的，我这里为了演示定义成局部组装方便介绍每一步流程
        // lua脚本参数介绍 KEYS[1]：传入的key  ARGV[1]：传入的value
        // 在使用redisTemplate执行lua脚本时会传入key数组和参数数组，List<K> keys, Object... args，在lua脚本中取key值和参数值时使用KEYS和ARGV，数组下标从1开始
        StringBuilder unlockLua = new StringBuilder();
        // 判断传入的锁key是否存在，如果不存在则直接返回1，如果存在则判断传入的value值是否和获取到的value相等
        unlockLua.append("if redis.call('EXISTS',KEYS[1]) == 0 then\n");
        unlockLua.append("    return 1\n");
        unlockLua.append("else\n");
        // 判断传入的value值是否和获取到的value相等，如果相等则代表是当前线程删除锁，执行删除对应key逻辑，然后返回1，否则返回0
        unlockLua.append("    if redis.call('GET',KEYS[1]) == ARGV[1] then\n");
        unlockLua.append("        return redis.call('DEL',KEYS[1])\n");
        unlockLua.append("    else\n");
        unlockLua.append("        return 0\n");
        unlockLua.append("    end\n");
        unlockLua.append("end");
        return unlockLua.toString();
    }


    /**
     * 封装redisTemplate执行lua脚本返回boolean类型执行器
     * @param scriptText lua脚本
     * @param key        传入数组keys的第一个元素这里就是我们锁key
     * @param args       传入数组args的第一个元素这里就是我们传入的value
     */
    private boolean executeLua(String scriptText, String key, Object... args) {
        // 通过 DefaultRedisScript 来执行 lua脚本
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        // Boolean 对应 lua脚本返回的 0 1
        redisScript.setResultType(Boolean.class);
        // 指定需要执行的 lua脚本
        redisScript.setScriptText(scriptText);
        // 注意 需要提供 List<K> keys, Object... args 代表 keys 和 ARGV
        return redisTemplate.execute(redisScript, Collections.singletonList(key), args);
    }

    /**
     * 锁续期
     * @Param key
     * @Param value 锁的value一般使用线程ID，在解锁时需要使用
     * @Param expireTime 过期时间 单位秒，
     */
    private void resetExpire(String key, String value, long expireTime) {
        // 如果key+value在集合中不存在，则不再进行续期操作
        if (!activeLockKeySet.contains(key + value)) {
            return;
        }

        //设置过期时间，推荐设置成过期时间的1/3时间续期一次，比如30s过期，10s续期一次
        long delay = expireTime <= 3 ? 1 : expireTime / 3;
        executorService.schedule(() -> {
            System.out.println("自动续期 key="+key+ "  value="+value);

            // 执行续期操作，如果续期成功则再次添加续期任务，如果不成功则将不在进行任务续期，并且将活跃锁key+value从集合中删除
            if (executeLua(getResetExpireLua(), key, value, String.valueOf(expireTime))) {
                System.out.println("自动续期成功开启下一轮自动续期");
                resetExpire(key, value, expireTime);
            } else {
                System.out.println("自动续期失败锁key已经删除或不是指定value持有的锁，取消自动续期");
                activeLockKeySet.remove(key + value);
            }
        }, delay, TimeUnit.SECONDS);
    }

    /**
     * 获取锁续期lua脚本
     */
    private String getResetExpireLua() {
        // 封装续期lua脚本 PS: 这个lua脚本应该是要定义成全局的，我这里为了演示定义成局部组装方便介绍每一步流程
        // lua脚本参数介绍 KEYS[1]：传入的key  ARGV[1]：传入的value  ARGV[2]：传入的过期时间
        // 在使用redisTemplate执行lua脚本时会传入key数组和参数数组，List<K> keys, Object... args，在lua脚本中取key值和参数值时使用KEYS和ARGV，数组下标从1开始
        StringBuilder resetExpireLua = new StringBuilder();
        // 判断传入的锁key是否存在且获取到的value值是否和传入的value值相等，如果相等则重置过期时间，然后返回1，否则返回0
        resetExpireLua.append("if redis.call('EXISTS',KEYS[1]) == 1 and redis.call('GET',KEYS[1]) == ARGV[1] then\n");
        resetExpireLua.append("    redis.call('EXPIRE',KEYS[1],tonumber(ARGV[2]))\n");
        resetExpireLua.append("    return 1\n");
        resetExpireLua.append("else\n");
        resetExpireLua.append("    return 0\n");
        resetExpireLua.append("end");
        return resetExpireLua.toString();
    }
}


