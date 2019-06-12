package com.theorydance.myone.common.utils;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/** 
 * @Description: TODO(将用户信息保存到Redis数据库里面,Redis支持数据的持久化，可以将内存中的数据保存在磁盘中，重启的时候可以再次加载进行使用)
 * @Author gaochao
 * @date   2018-5-9 下午5:31:31
 *	@version V1.0 
 */
@Slf4j
public class RidesUtils {
	
	 //Redis服务器IP
    private static String ADDR = "139.159.243.179";
    
    //Redis的端口号
    private static int PORT = 6379;
    
    //访问密码
    private static String AUTH = "redis_grand";
    
    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 1024;
    
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;
    
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;
    
    private static int TIMEOUT = 10000;
    
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;
    
    private static JedisPool jedisPool = null;
    
    /**
     * 初始化Redis连接池
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取Jedis实例
     * @return
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 释放jedis资源
     * @param jedis
     */
    @SuppressWarnings("deprecation")
	public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

	
	/*
	 * 保存字符串到rides
	 */
	public static boolean saveString(String key,String value){
		// 获取jedis
		Jedis jedis = getJedis();
		try {
			String ret = jedis.set(key, value);
			jedis.expire(key, 60*60*24);//设置key的过期时间为24小时单位s
			if("ok".equals(ret)){
				// 释放jedis
				returnResource(jedis);
				return true;
			}
		} catch (Exception e) {
    		log.error(e.getMessage(), e);
		}
		return false;
	}
    
	/*
	 * rides获取保存的数据
	 */
	public static String queryString(String key){
		// 获取jedis
		Jedis jedis = getJedis();
		String ret = null;
		try {
			ret = jedis.get(key);
			if(ret!=null){
				jedis.expire(key, 60*60*24);//为key续期
			}
			// 释放jedis
			returnResource(jedis);
		} catch (Exception e) {
    		log.error(e.getMessage(), e);
		}
		return ret;
	}
	
	/*
	 * rides获取保存的数据
	 */
	public static String queryStringNOExpire(String key){
		// 获取jedis
		Jedis jedis = getJedis();
		String ret = null;
		try {
			ret = jedis.get(key);
			// 释放jedis
			returnResource(jedis);
		} catch (Exception e) {
    		log.error(e.getMessage(), e);
		}
		return ret;
	}
	
	
	//删除
	public static Long invalidate(String token) {
		// 获取jedis
		Jedis jedis = getJedis();
		Long ret = null;
		try {
			ret = jedis.del(token);
			// 释放jedis
			returnResource(jedis);
		} catch (Exception e) {
    		log.error(e.getMessage(), e);
		}
		return ret;
	}

	
	
	/*
	 * Redis Append 命令用于为指定的 key追加值。
	 */
	public static String appendString(String key,String content){
		// 获取jedis
		Jedis jedis = getJedis();
		String ret = null;
		try {
			Long append = jedis.append(key, content);
			// 释放jedis
			returnResource(jedis);
		} catch (Exception e) {
    		log.error(e.getMessage(), e);
		}
		return ret;
	}

}
