package com.nft.robot.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.setting.Setting;
import com.nft.robot.entity.TokenInfo;

/**
 * @Author liangshijie
 * @Date 2024/4/15
 * @Description:
 */
public class GlobalCacheManager {
    private static Setting setting = SeetingManager.getSetting();

    private static final TimedCache<String, TokenInfo> globalCache = CacheUtil.newTimedCache(setting.getLong("floorPricetime", 60L * 10L)*1000L); // 缓存有效期为1分钟

    static {
        init();
    }

    public static void init() {
        //启动定时任务，每3000毫秒清理一次过期条目，注释此行首次启动仍会清理过期条目
        globalCache.schedulePrune(3000);
    }

    public static TimedCache<String, TokenInfo> getGlobalCache() {
        return globalCache;
    }

    public static TokenInfo get(String key) {
        return globalCache.get(key,false);
    }

    public static void set(String key, TokenInfo value) {
        globalCache.put(key, value);
    }
}
