package cz.maku.servershandler.jedis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolBuilder {

    private JedisPoolConfig jedisPoolConfig;
    private String hostname;
    private int port;
    private String password;

    public JedisPoolBuilder withConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
        return this;
    }

    public JedisPoolBuilder withDefaultConfig() {
        return withConfig(new JedisPoolConfig());
    }

    public JedisPoolBuilder hostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public JedisPoolBuilder port(int port) {
        this.port = port;
        return this;
    }

    public JedisPoolBuilder password(String password) {
        this.password = password;
        return this;
    }

    public JedisPool build() {
        return new JedisPool(jedisPoolConfig, hostname, port, 0, password);
    }
}
