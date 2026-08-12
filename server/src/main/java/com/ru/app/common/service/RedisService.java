package com.ru.app.common.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    public RedisService(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate=redisTemplate;
    }

    // =============================common============================

    /**
     * 指定缓存失效时间
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key获取过期时间
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存（支持单个或多个键）
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * 仅当键不存在时设置值（SETNX）
     */
    public boolean setIfAbsent(String key, Object value) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 仅当键不存在时设置值，并指定过期时间
     */
    public boolean setIfAbsent(String key, Object value, long time) {
        try {
            boolean result = redisTemplate.opsForValue().setIfAbsent(key, value);
            if (result && time > 0) {
                expire(key, time);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 若键不存在则设置默认值并返回，存在则直接返回值
     */
    public Object getIfAbsent(String key, Object defaultValue) {
        try {
            Object value = get(key);
            if (value == null) {
                set(key, defaultValue);
                return defaultValue;
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 若键不存在则设置默认值、过期时间并返回，存在则直接返回值
     */
    public Object getIfAbsent(String key, Object defaultValue, long time) {
        try {
            Object value = get(key);
            if (value == null) {
                set(key, defaultValue, time);
                return defaultValue;
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    // ================================Map=================================

    /**
     * HashGet
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据并设置时间
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 仅当字段不存在时设置值（HSETNX）
     */
    public boolean putIfAbsent(String key, String hashKey, Object value) {
        try {
            return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 仅当字段不存在时设置值，并设置过期时间
     */
    public boolean putIfAbsent(String key, String hashKey, Object value, long time) {
        try {
            boolean result = putIfAbsent(key, hashKey, value);
            if (result && time > 0) {
                expire(key, time);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 若字段不存在则设置默认值并返回，存在则返回字段值
     */
    public Object getHashIfAbsent(String key, String hashKey, Object defaultValue) {
        try {
            Object value = hget(key, hashKey);
            if (value == null) {
                hset(key, hashKey, defaultValue);
                return defaultValue;
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 若键不存在则初始化空Hash并返回，存在则返回Hash
     */
    public Map<Object, Object> getHashIfAbsent(String key) {
        try {
            Map<Object, Object> hash = hmget(key);
            if (hash == null || hash.isEmpty()) {
                redisTemplate.opsForHash().put(key, "init", "empty");
                redisTemplate.opsForHash().delete(key, "init");
            }
            return hmget(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 若键不存在则初始化Hash并设置过期时间
     */
    public Map<Object, Object> getHashIfAbsent(String key, long time) {
        try {
            Map<Object, Object> hash = hmget(key);
            if (hash == null || hash.isEmpty()) {
                redisTemplate.opsForHash().put(key, "init", "empty");
                redisTemplate.opsForHash().delete(key, "init");
                expire(key, time);
            }
            return hmget(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询是否存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存并设置时间
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的元素
     */
    public long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 仅当元素不存在时添加到集合（相当于SETNX for Set）
     */
    public boolean addIfAbsent(String key, Object value) {
        try {
            Long result = redisTemplate.opsForSet().add(key, value);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量添加元素（仅添加不存在的元素）
     */
    public long addIfAbsent(String key, Object... values) {
        try {
            if (values == null || values.length == 0) {
                return 0;
            }
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 仅当元素不存在时添加，并设置过期时间
     */
    public boolean addIfAbsentAndTime(String key, Object value, long time) {
        try {
            boolean result = addIfAbsent(key, value);
            if (result && time > 0) {
                expire(key, time);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 若键不存在则初始化空集合并返回，存在则返回集合
     */
    public Set<Object> getSetIfAbsent(String key) {
        try {
            Set<Object> set = sGet(key);
            if (set == null || set.isEmpty()) {
                redisTemplate.opsForSet().add(key);
                return sGet(key);
            }
            return set;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 若键不存在则初始化集合并添加默认元素，存在则返回集合
     */
    public Set<Object> getSetIfAbsent(String key, Object... defaultValues) {
        try {
            Set<Object> set = sGet(key);
            if (set == null || set.isEmpty()) {
                sSet(key, defaultValues);
                return sGet(key);
            }
            return set;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 若键不存在则初始化集合并设置过期时间
     */
    public Set<Object> getSetIfAbsent(String key, long time) {
        try {
            Set<Object> set = sGet(key);
            if (set == null || set.isEmpty()) {
                redisTemplate.opsForSet().add(key);
                expire(key, time);
            }
            return sGet(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ===============================list=================================

    /**
     * 获取list缓存的内容
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引获取list中的值
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存并设置时间
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}