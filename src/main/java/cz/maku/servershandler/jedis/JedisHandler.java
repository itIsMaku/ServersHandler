package cz.maku.servershandler.jedis;

import cz.maku.servershandler.Instance;
import lombok.Getter;
import redis.clients.jedis.Jedis;

@Getter
public class JedisHandler {

    private final Jedis jedis;

    public JedisHandler() {
        this.jedis = new Jedis(Instance.getInstance().getConfig().getString("jedis.hostname"), Instance.getInstance().getConfig().getInt("jedis.port"));
        jedis.connect();
        jedis.auth(Instance.getInstance().getConfig().getString("jedis.password"));
    }

}
