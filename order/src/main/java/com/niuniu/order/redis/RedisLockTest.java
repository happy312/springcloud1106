package com.niuniu.order.redis;

import com.niuniu.common.utils.RedisLockUtil;
import com.niuniu.order.OrderApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
public class RedisLockTest {

    @Autowired
    private RedisLockUtil redisLockUtils;

    // 产品库存lock前缀
    private final String productLockKeyPrefix = "PRODUCT_LOCK_KEY:";

    // 模拟产品库存信息
    private static Map<String, String> productMap = new HashMap<>();
    static {
        productMap.put("id", "P0001");
        productMap.put("title", "分布式锁");
        productMap.put("stock", "10");
        productMap.put("sold", "0");
    }

    public static void main(String[] args) throws InterruptedException {
        RedisLockTest redisLockTest = new RedisLockTest();
        // 定义一个线程池，队列根据需要设置
        ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 100, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000));
        CountDownLatch countDownLatch = new CountDownLatch(1000);

        // 模拟1000个人抢10个商品
        for (int i = 0; i < 1000; i++) {
            executor.execute(() -> {
                // 加锁扣减库存
                redisLockTest.deductStockLock();
                // 不加锁扣减库存
//                deductStockNotLock();
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executor.shutdown();
        System.out.println("productMap=" + productMap.toString());
    }

    /**
     * 扣减库存：使用分布式锁
     */
    private void deductStockLock() {
        // 通过UUID和线程ID组合成value，标识当前线程，解锁的时候判断是否是当前线程持有的锁
        String uuidValue = UUID.randomUUID() + ":" + Thread.currentThread().getId();
        // 组装锁key
        String lockKey = productLockKeyPrefix + productMap.get("id");
        boolean lock = false;
        try {
            // 获取锁
            lock = redisLockUtils.lock(lockKey, uuidValue, 30);
            // 测试锁续期效果 这里模拟业务处理时间40s超过
            Thread.sleep(40000);
            // 再次获取锁，测试可重入效果
            lock = redisLockUtils.lock(lockKey, uuidValue, 30);
            // 如果没有获取到锁则直接返回
            if (!lock) {
                // 这里直接响应失败，也可以进行重试
                return;
            }
            System.out.println("获取锁成功 uuidValue=" + uuidValue);
            // 获取到锁执行业务逻辑，处理库存信息，假设每个线程每次购买1个商品
            Integer stock = Integer.valueOf(productMap.get("stock"));
            if (stock <= 0) {
//                System.out.println("库存不足");
                return;
            }
            // 库存 - 1
            productMap.put("stock", String.valueOf(Integer.valueOf(productMap.get("stock")) - 1));
            // 已售 + 1
            productMap.put("sold", String.valueOf(Integer.valueOf(productMap.get("sold")) + 1));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (lock) {
                // 解锁
                redisLockUtils.unlock(lockKey, uuidValue);
            }
        }
    }

    /**
     * 扣减库存：不使用分布式锁
     */
    private void deductStockNotLock() {
        // 判断库存是否足够，假设每个线程每次购买1个商品
        Integer stock = Integer.valueOf(productMap.get("stock"));
        if (stock <= 0) {
//            System.out.println("库存不足");
            return;
        }
        // 暂停10毫秒方便呈现不加锁超卖效果
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 库存 - 1
        productMap.put("stock", String.valueOf(Integer.valueOf(productMap.get("stock")) - 1));
        // 已售 + 1
        productMap.put("sold", String.valueOf(Integer.valueOf(productMap.get("sold")) + 1));
    }
}



