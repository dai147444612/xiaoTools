package com.xiaoTools.core.regular.simpleCache;

import com.xiaoTools.core.regular.method.Func0;

import java.io.Serial;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * [简单缓存，无超时实现，默认使用WeakHashMap实现缓存自动清理](Simple cache, no timeout implementation, default to use weakhashmap to realize automatic cache cleaning)
 * @description: zh - 简单缓存，无超时实现，默认使用WeakHashMap实现缓存自动清理
 * @description: en - Simple cache, no timeout implementation, default to use weakhashmap to realize automatic cache cleaning
 * @version: V1.0
 * @author XiaoXunYao
 * @since 2021/6/8 4:47 下午
*/
public class SimpleCache<K, V> implements Iterable<Map.Entry<K, V>>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * [连接池](Connection pool)
    */
    private final Map<K,V> cache;

    /**
     * [乐观读写锁](Optimistic read write lock)
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * [写的时候每个key一把锁，降低锁的粒度](When writing, each key has a lock to reduce the granularity of the lock)
     */
    protected final Map<K, Lock> keyLockMap = new ConcurrentHashMap<>();


    /**
     * [构造方法](Construction method)
     * @description: zh - 构造方法
     * @description: en - Construction method
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/6/8 5:03 下午
    */
    public SimpleCache() {
        this(new WeakHashMap<>());
    }

    /**
     * [构造函数](Constructors)
     * @description: zh - 构造函数
     * @description: en - Constructors
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/6/8 5:47 下午
     * @param initMap:[初始Map，用于定义Map类型](Initial map, used to define the map type)
    */
    public SimpleCache(Map<K, V> initMap) {
        this.cache = initMap;
    }

    /**
     * [从缓存池中查找值](Find value from cache pool)
     * @description: zh - 从缓存池中查找值
     * @description: en - Find value from cache pool
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/6/8 5:49 下午
     * @param key: [键](key)
     * @return V
    */
    public V get(K key) {
        lock.readLock().lock();
        try {
            return cache.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     *
     * @description: zh - 从缓存中获得对象，当对象不在缓存中或已经过期返回Func0回调产生的对象
     * @description: en - Get the object from the cache. When the object is not in the cache or has expired, return the object generated by func0 callback
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/6/8 5:50 下午
     * @param key: [键](key)
     * @param supplier: [如果不存在回调方法，用于生产值对象](If no callback method exists, it is used to produce the value object)
     * @return V
    */
    public V get(K key, Func0<V> supplier) {
        V v = get(key);
        if(null == v && null != supplier){
            //每个key单独获取一把锁，降低锁的粒度提高并发能力，see pr#1385@Github
            final Lock keyLock = keyLockMap.computeIfAbsent(key, k -> new ReentrantLock());
            keyLock.lock();
            try {
                // 双重检查，防止在竞争锁的过程中已经有其它线程写入
                v = cache.get(key);
                if (null == v) {
                    try {
                        v = supplier.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    put(key, v);
                }
            } finally {
                keyLock.unlock();
                keyLockMap.remove(key);
            }
        }

        return v;
    }

    /**
     * [放入缓存](Put in cache)
     * @description: zh - 放入缓存
     * @description: en - Put in cache
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/6/8 5:03 下午
     * @param key: [键](key)
     * @param value: [值](value)
     * @return V
    */
    public V put(K key, V value) {
        // 独占写锁
        lock.writeLock().lock();
        try {
            cache.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
        return value;
    }

    /**
     * [移除缓存](Remove cache)
     * @description: zh - 移除缓存
     * @description: en - Remove cache
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/6/8 5:02 下午
     * @param key: [键](key)
     * @return V
    */
    public V remove(K key) {
        // 独占写锁
        lock.writeLock().lock();
        try {
            return cache.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * [清空缓存池](Clear cache pool)
     * @description: zh - 清空缓存池
     * @description: en - Clear cache pool
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/6/8 5:01 下午
    */
    public void clear() {
        // 独占写锁
        lock.writeLock().lock();
        try {
            this.cache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * [重写迭代器](Override iterator)
     * @description: zh - 重写迭代器
     * @description: en - Override iterator
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/6/8 5:00 下午
     * @return java.util.Iterator<java.util.Map.Entry<K,V>>
    */
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return this.cache.entrySet().iterator();
    }
}
