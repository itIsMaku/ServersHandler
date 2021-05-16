package cz.maku.servershandler;

import com.google.common.collect.Maps;
import cz.maku.servershandler.jedis.JedisHandler;
import cz.maku.servershandler.servers.Server;
import cz.maku.servershandler.servers.Updater;
import cz.maku.servershandler.servers.type.Mode;
import cz.maku.servershandler.servers.type.State;
import lombok.Getter;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;

@Getter
public class ServersHandler {

    private final Instance instance;
    private final JedisHandler jedisHandler;
    private final Server server;
    private Map<String, Server> servers;
    private final Updater updater;

    public ServersHandler(Instance instance) {
        this.instance = instance;
        this.jedisHandler = new JedisHandler();
        this.server = new Server(instance.getConfig().getString("server.name"), instance.getConfig().getInt("server.id"), instance.getConfig().getBoolean("server.privateServer"), Mode.valueOf(instance.getConfig().getString("server.mode")));
        this.servers = Maps.newHashMap();
        this.updater = new Updater();
    }

    public Server serialize(String string) {
        String[] serverArgs = string.split(";");
        return new Server(serverArgs[0], Integer.parseInt(serverArgs[1]), Boolean.parseBoolean(serverArgs[2]), Mode.valueOf(serverArgs[3]), Integer.parseInt(serverArgs[4]), Integer.parseInt(serverArgs[5]), serverArgs[5], State.getByDisplayName(serverArgs[6]));
    }

    public String deserialize(Server server) {
        return server.getName() + ";" + server.getId() + ";" + server.isPrivateServer() + ";" + server.getMode().name() + ";" + server.getOnline() + ";" + server.getMaxOnline() + ";" + server.getMap() + ";" + server.getState().name() + ";";
    }

    public void download() {
        Map<String, String> map = jedisHandler.getJedis().hgetAll("mservers");
        map.forEach((name, deserializedServer) -> {
            Server s = serialize(deserializedServer);
            servers.put(name + "-" + s.getId(), s);
        });
    }

    public void upload() {
        jedisHandler.getJedis().hset("mservers", server.getName() + "-" + server.getId(), deserialize(server));
        servers.put(server.getName() + "-" + server.getId(), server);
    }

    public void subscribe() {
        jedisHandler.getJedis().subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (channel.equalsIgnoreCase("channel1")) {
                    if (servers.containsKey(message.replace("update: ", ""))) {
                        servers.put(message.replace("update: ", ""), serialize(jedisHandler.getJedis().hget("mservers", message.replace("update: ", ""))));
                    }
                }
            }
        }, "channel1");

    }

    public void publish() {
        jedisHandler.getJedis().publish("channel1", "update: " + server.getName() + "-" + server.getId());
    }

    public void stop() {
        jedisHandler.getJedis().hdel("mservers", server.getName() + "-" + server.getId());
        if (jedisHandler.getJedis().isConnected()) {
            jedisHandler.getJedis().disconnect();
            instance.getLogger().warning("Disconnecting from Redis...");
        }
    }
}
