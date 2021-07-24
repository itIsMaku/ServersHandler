package cz.maku.servershandler;

import com.google.common.collect.Maps;
import cz.maku.servershandler.jedis.JedisHandler;
import cz.maku.servershandler.servers.Server;
import cz.maku.servershandler.servers.Updater;
import cz.maku.servershandler.servers.type.Mode;
import cz.maku.servershandler.servers.type.State;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class ServersHandler {

    private final Instance instance;
    private final JedisHandler jedisHandler;
    private final Server server;
    private final Map<String, Server> servers;
    private final Updater updater;
    private final JedisPubSub jedisPubSub;

    public ServersHandler(Instance instance) {
        this.instance = instance;
        this.jedisHandler = new JedisHandler();
        this.server = new Server(instance.getConfig().getString("server.name"), instance.getConfig().getInt("server.id"), instance.getConfig().getBoolean("server.privateServer"), Mode.valueOf(instance.getConfig().getString("server.mode")));
        this.servers = Maps.newHashMap();
        this.updater = new Updater();
        jedisPubSub = subscribe();
        new Thread(() -> {
            Jedis jedis = new Jedis(Instance.getInstance().getConfig().getString("jedis.hostname"), Instance.getInstance().getConfig().getInt("jedis.port"), 100);
            jedis.connect();
            if (jedis.isConnected()) {
                Instance.getInstance().getLogger().info("Â§a[Subscriber] Successfully connected to Redis.");
                Instance.getInstance().getLogger().info("[Subscriber] Trying to auth to Redis...");
                jedis.auth(Instance.getInstance().getConfig().getString("jedis.password"));
                jedis.subscribe(jedisPubSub, "channel1");
            }
        }).start();
    }

    public Server deserialize(String string) {
        if (string == null) return null;
        String[] serverArgs = string.split(";");
        return new Server(serverArgs[0], Integer.parseInt(serverArgs[1]), Boolean.parseBoolean(serverArgs[2]), Mode.valueOf(serverArgs[3]), Integer.parseInt(serverArgs[4]), Integer.parseInt(serverArgs[5]), serverArgs[5], State.getByDisplayName(serverArgs[6]));
    }

    public String serialize(Server server) {
        return server.getName() + ";" + server.getId() + ";" + server.isPrivateServer() + ";" + server.getMode().name() + ";" + server.getOnline() + ";" + server.getMaxOnline() + ";" + server.getMap() + ";" + server.getState().name() + ";";
    }

    public void download() {
        Map<String, String> map = jedisHandler.getJedis().hgetAll("mservers");
        map.forEach((name, deserializedServer) -> {
            Server s = deserialize(deserializedServer);
            servers.put(name, s);
        });
    }

    public void upload() {
        jedisHandler.getJedis().hset("mservers", server.getName() + "-" + server.getId(), serialize(server));
        servers.put(server.getName() + "-" + server.getId(), server);
    }

    public JedisPubSub subscribe() {
        return new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (channel.equalsIgnoreCase("channel1")) {
                    if (message.contains("update: ")) {
                        servers.put(message.replace("update: ", ""), deserialize(jedisHandler.getJedis().hget("mservers", message.replace("update: ", ""))));
                        //Instance.getInstance().getLogger().info(ChatColor.AQUA + "Server " + message.replace("update: ", "") + " updated.");
                    } else if (message.contains("remove: ")) {
                        servers.remove(message.replace("remove: ", ""));
                        //Instance.getInstance().getLogger().info(ChatColor.AQUA + "Server " + message.replace("remove: ", "") + " removed.");
                    }
                }
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                Instance.getInstance().getLogger().info("Client is subscribed to channel " + channel + ".");
            }

            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {
                Instance.getInstance().getLogger().info("Client is unsubscribed from channel " + channel + ".");
            }
        };
    }

    public void publish(boolean update) {
        Jedis jedis = new Jedis(Instance.getInstance().getConfig().getString("jedis.hostname"), Instance.getInstance().getConfig().getInt("jedis.port"), 100);
        jedis.connect();
        jedis.auth(Instance.getInstance().getConfig().getString("jedis.password"));
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
        newFixedThreadPool.submit((Runnable) () -> {
            while (true) {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (update) {
                    jedis.publish("channel1", "update: " + server.getName() + "-" + server.getId());
                } else {
                    jedis.publish("channel1", "remove: " + server.getName() + "-" + server.getId());
                }
            }
        });
        jedis.close();
    }

    public void stop() {
        publish(false);
        jedisHandler.getJedis().hdel("mservers", server.getName() + "-" + server.getId());
        if (jedisHandler.getJedis().isConnected()) {
            jedisHandler.getJedis().disconnect();
            instance.getLogger().warning("Disconnecting from Redis...");
        }
    }
}