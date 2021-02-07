package com.ai.ecs.guacamole.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lbw
 * @version 1.0
 * @date 2021/2/5 13:22
 */

@Configuration
@PropertySource("classpath:redis.properties")
@Slf4j
public class RedisConfig {
    @Value("${spring.redis.cluster.nodes}")
    private String host;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private int connectionTimeout;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxTotal;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWaitMillis;


    private Set<HostAndPort> parseHostAndPort() throws Exception {
        try {
            Set<HostAndPort> haps = new HashSet<HostAndPort>();
            String[] hosts = host.split(",");
            for (String hostAndPort : hosts) {
                String[] hostOrPort = hostAndPort.split(":");
                HostAndPort hap = new HostAndPort(hostOrPort[0],
                        Integer.parseInt(hostOrPort[1]));
                haps.add(hap);
            }

            return haps;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new Exception("解析 jedis 配置文件失败", ex);
        }
    }

    @Bean
    public JedisCluster jedisCluster(JedisPoolConfig jedisPoolConfig) throws Exception {
        Set<HostAndPort> haps = this.parseHostAndPort();
        return new JedisCluster(haps, connectionTimeout, connectionTimeout, 1000, password, jedisPoolConfig);
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(this.maxIdle);
        poolConfig.setMinIdle(this.minIdle);
        poolConfig.setTestOnCreate(true);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);

        return poolConfig;
    }


}
