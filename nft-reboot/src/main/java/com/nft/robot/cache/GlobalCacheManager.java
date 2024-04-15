package com.nft.robot.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

/**
 * @Author liangshijie
 * @Date 2024/4/15
 * @Description:
 */
public class GlobalCacheManager {

    private static final TimedCache<String, Object> globalCache = CacheUtil.newTimedCache(60 * 1000); // 缓存有效期为1分钟

    static {
        init();
    }

    public static void init() {
        //启动定时任务，每5毫秒清理一次过期条目，注释此行首次启动仍会清理过期条目
        globalCache.schedulePrune(3000);
    }

    public static TimedCache<String, Object> getGlobalCache() {
        return globalCache;
    }

    public static Object get(String key) {
        return globalCache.get(key);
    }

    public static void set(String key, Object value) {
        globalCache.put(key, value);
    }
}
