package com.ai.ecs.guacamole.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author lbw
 * @version 1.0
 * @date 2021/2/5 16:01
 */
@Slf4j
@Component
public class JedisClusterUtils {

    private static JedisCluster jedisCluster;
    @Autowired
    private JedisCluster jedisClusterBean;

    private static int getExpires(int cacheSeconds) {
        int value = cacheSeconds;
        //不设置有效期，默认30分钟
        if (cacheSeconds <= 0) {
            value = 30 * 60;//默认30F
        }
        //超过一天，只给一天的有效期
        if (cacheSeconds > 86400) {
            value = 86400;
        }
        return value;
    }

    public static void expires(String key, int cacheSeconds) {
        jedisCluster.expire(key, cacheSeconds);
    }

    /**
     * 递增
     *
     * @param key
     */
    public static void incr(String key) {
        jedisCluster.incr(key);
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public static String get(String key) {
        String value = null;

        try {

            if (jedisCluster.exists(key)) {
                value = jedisCluster.get(key);
                value = StringUtils.isNotBlank(value)
                        && !"nil".equalsIgnoreCase(value) ? value : null;
                log.debug("get {} = {}", key, value);
            }
        } catch (Exception e) {
            log.warn("get {},{} ", key, e);
        } finally {

        }
        return value;
    }

    /**
     * 设置缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static String set(String key, String value, int cacheSeconds) {
        String result = null;
        cacheSeconds = getExpires(cacheSeconds);
        try {

            result = jedisCluster.set(key, value);
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            log.debug("set {} = {}", key, value);
        } catch (Exception e) {
            log.warn("set {} ,{}", key, e);
        } finally {

        }
        return result;
    }

    /**
     * 获取List缓存
     *
     * @param key 键
     * @return 值
     */
    public static List<String> getList(String key) {
        List<String> value = null;
        try {
            if (jedisCluster.exists(key)) {
                value = jedisCluster.lrange(key, 0, -1);
                log.debug("getList {} = {}", key, value);
            }
        } catch (Exception e) {
            log.warn("getList {} , {}", key, e);
        } finally {

        }
        return value;
    }

    /**
     * 设置List缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static long setList(String key, List<String> value,
                               int cacheSeconds) {
        long result = 0;
        cacheSeconds = getExpires(cacheSeconds);
        try {
            if (jedisCluster.exists(key)) {
                jedisCluster.del(key);
            }
            result = jedisCluster.rpush(key, value.toArray(new String[value.size()]));
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            log.debug("setList {} = {}", key, value);
        } catch (Exception e) {
            log.warn("setList {} , {}", key, e);
        } finally {

        }
        return result;
    }

    /**
     * 向List缓存中添加值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static long listAdd(String key, String... value) {
        long result = 0;
        try {
            result = jedisCluster.rpush(key, value);
            log.debug("listAdd {} = {}", key, value);
        } catch (Exception e) {
            log.warn("listAdd {} , {}", key, e);
        } finally {

        }
        return result;
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public static Set<String> getSet(String key) {
        Set<String> value = null;
        try {
            if (jedisCluster.exists(key)) {
                value = jedisCluster.smembers(key);
                log.debug("getSet {} = {}", key, value);
            }
        } catch (Exception e) {
            log.warn("getSet {} , {}", key, e);
        } finally {

        }
        return value;
    }

    /**
     * 设置Set缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static long setSet(String key, Set<String> value, int cacheSeconds) {
        long result = 0;
        cacheSeconds = getExpires(cacheSeconds);
        try {
            if (jedisCluster.exists(key)) {
                jedisCluster.del(key);
            }
            result = jedisCluster.sadd(key, (String[]) value.toArray());
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            log.debug("setSet {} = {}", key, value);
        } catch (Exception e) {
        } finally {

        }
        return result;
    }

    /**
     * 向Set缓存中添加值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static long setSetAdd(String key, String... value) {
        long result = 0;
        try {
            result = jedisCluster.sadd(key, value);
            log.debug("setSetAdd {} = {}", key, value);
        } catch (Exception e) {
        } finally {

        }
        return result;
    }

    /**
     * 获取Map缓存
     *
     * @param key 键
     * @return 值
     */
    public static Map<String, String> getMap(String key) {
        Map<String, String> value = null;
        try {
            if (jedisCluster.exists(key)) {
                value = jedisCluster.hgetAll(key);
                log.debug("getMap {} = {}", key, value);
            }
        } catch (Exception e) {
        } finally {

        }
        return value;
    }

    /**
     * 设置Map缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static String setMap(String key, Map<String, String> value,
                                int cacheSeconds) {
        String result = null;
        cacheSeconds = getExpires(cacheSeconds);
        try {
            if (jedisCluster.exists(key)) {
                jedisCluster.del(key);
            }
            result = jedisCluster.hmset(key, value);
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            log.debug("setMap {} = {}", key, value);
        } catch (Exception e) {
        } finally {

        }
        return result;
    }

    /**
     * 向Map缓存中添加值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static String mapPut(String key, Map<String, String> value) {
        String result = null;

        try {

            result = jedisCluster.hmset(key, value);
            log.debug("mapPut {} = {}", key, value);
        } catch (Exception e) {
        } finally {

        }
        return result;
    }

    /**
     * 移除Map缓存中的值
     *
     * @param key    键
     * @param mapKey 值键
     * @return
     */
    public static long mapRemove(String key, String mapKey) {
        long result = 0;
        try {
            result = jedisCluster.hdel(key, mapKey);
            log.debug("mapRemove {}  {}", key, mapKey);
        } catch (Exception e) {
        } finally {

        }
        return result;
    }

    /**
     * 判断Map缓存中的Key是否存在
     *
     * @param key    键
     * @param mapKey 值键
     * @return
     */
    public static boolean mapExists(String key, String mapKey) {
        boolean result = false;
        try {
            result = jedisCluster.hexists(key, mapKey);
            log.debug("mapExists {}  {}", key, mapKey);
        } catch (Exception e) {
        } finally {

        }
        return result;
    }

    /**
     * 删除缓存
     *
     * @param key 键
     * @return
     */
    public static long del(String key) {
        long result = 0;
        try {
            if (jedisCluster.exists(key)) {
                result = jedisCluster.del(key);
                log.debug("del {}", key);
            } else {
                log.debug("del {} not exists", key);
            }
        } catch (Exception e) {
            log.warn("del {}", key, e);
        } finally {

        }
        return result;
    }

    /**
     * 缓存是否存在
     *
     * @param key 键
     * @return
     */
    public static boolean exists(String key) {
        boolean result = false;
        try {
            result = jedisCluster.exists(key);
            log.debug("exists {}", key);
        } catch (Exception e) {
            log.warn("exists {}", key, e);
        } finally {

        }
        return result;
    }

    /**
     * 模糊匹配获取key集合
     *
     * @param pattern
     * @return
     */
    public static List<String> keys(String pattern) {
        log.debug("Start getting keys...");
        List<String> keys = Lists.newArrayList();
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        for (String k : clusterNodes.keySet()) {
            log.debug("Getting keys from: {}", k);
            JedisPool jp = clusterNodes.get(k);
            Jedis connection = jp.getResource();
            try {
                keys.addAll(connection.keys(pattern));
            } catch (Exception e) {
                log.error("Getting keys error: {}", e);
            } finally {
                log.debug("Connection closed.");
                connection.close();// 用完一定要close这个链接！！！
            }
        }
        log.debug("Keys gotten!");
//		return keys;

        //去重
        return new ArrayList<String>(new HashSet<String>(keys));
    }

    @PostConstruct
    public void init() {
        jedisCluster = jedisClusterBean;
    }

}
