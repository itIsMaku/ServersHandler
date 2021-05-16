package cz.maku.servershandler;

import com.google.common.collect.Maps;
import cz.maku.servershandler.jedis.JedisHandler;
import cz.maku.servershandler.servers.Server;
import cz.maku.servershandler.servers.ServerUpdater;
import lombok.Getter;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;

@Getter
public class ServersHandler implements ServerUpdater {

    private final Instance instance;
    private final JedisHandler jedisHandler;
    private final Server server;
    private Map<String, Server> servers;

    public ServersHandler(Instance instance) {
        this.instance = instance;
        this.jedisHandler = new JedisHandler();
        this.server = new Server(instance.getConfig().getString("server.name"), instance.getConfig().getInt("server.id"), instance.getConfig().getBoolean("server.privateServer"));
        this.servers = Maps.newHashMap();
    }

    public Server serialize(String string) {
        String[] serverArgs = string.split(";");
        return new Server(serverArgs[0], Integer.parseInt(serverArgs[1]), Boolean.parseBoolean(serverArgs[2]), Integer.parseInt(serverArgs[3]), Integer.parseInt(serverArgs[4]));
    }

    public String deserialize(Server server) {
        return server.getName() + ";" + server.getId() + ";" + server.isPrivateServer() + ";" + server.getOnline() + ";" + server.getMaxOnline() + ";";
    }

    public void download() {
        Map<String, String> map = jedisHandler.getJedis().hgetAll("servers");
        map.forEach((name, deserializedServer) -> {
            servers.put(name, serialize(deserializedServer));
        });
    }

    public void upload() {
        jedisHandler.getJedis().hset("servers", server.getName(), deserialize(server));
        servers.put(server.getName(), server);
    }

    public void subscribe() {
        jedisHandler.getJedis().subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (channel.equalsIgnoreCase("channel1")) {
                    if (servers.containsKey(message.replace("update: ", ""))) {
                        servers.put(message.replace("update: ", ""), serialize(jedisHandler.getJedis().hget("servers", message.replace("update: ", ""))));
                    }
                }
            }
        }, "channel1");

    }

    public void publish() {
        jedisHandler.getJedis().publish("channel1", "update: " + server.getName());
    }

    public void stop() {
        jedisHandler.getJedis().hdel("servers", server.getName());
        jedisHandler.getJedis().disconnect();
    }

    @Override
    public void onServersUpdate(Map<String, Server> map) {
        servers = map;
    }
}
