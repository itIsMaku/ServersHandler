package cz.maku.servershandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cz.maku.servershandler.bukkit.BukkitListener;
import cz.maku.servershandler.jedis.JedisPoolBuilder;
import cz.maku.servershandler.server.ServersPubSub;
import cz.maku.servershandler.server.SynchronizedServer;
import cz.maku.servershandler.server.SynchronizedServerListener;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import static cz.maku.servershandler.PubSubConfiguration.CHANNEL;
import static cz.maku.servershandler.PubSubConfiguration.GSON;

public class ServersHandler {

    private final Plugin plugin;
    private final String serverName;
    private final JedisPool jedisPool;
    @Getter
    private final Map<String, SynchronizedServer> servers;
    private final List<SynchronizedServerListener> listeners;
    private final ServersPubSub serversPubSub;

    private ServersHandler(Plugin plugin, String serverName, JedisPool jedisPool, Map<String, SynchronizedServer> servers, List<SynchronizedServerListener> listeners) {
        this.plugin = plugin;
        this.serverName = serverName;
        this.jedisPool = jedisPool;
        this.servers = servers;
        this.listeners = listeners;
        serversPubSub = new ServersPubSub(plugin) {
            @Override
            public void onUpdate(SynchronizedServer synchronizedServer) {
                if (synchronizedServer.isDestroyed()) {
                    servers.remove(synchronizedServer.getName());
                } else {
                    servers.put(synchronizedServer.getName(), synchronizedServer);
                }
                listeners.forEach(listener -> listener.onUpdate(synchronizedServer));
            }
        };
    }

    public static ServersHandler initialize(Plugin plugin, String serverName, JedisPool jedisPool, List<SynchronizedServerListener> listeners) {
        ServersHandler serversHandler = new ServersHandler(plugin, serverName, jedisPool, Maps.newConcurrentMap(), listeners);
        plugin.getServer().getPluginManager().registerEvents(new BukkitListener(serversHandler), plugin);
        serversHandler.registerLocal();
        serversHandler.subscribe();
        return serversHandler;
    }

    public static ServersHandler initialize(Plugin plugin, String serverName, JedisPoolBuilder jedisPoolBuilder) {
        return initialize(plugin, serverName, jedisPoolBuilder.build(), Lists.newArrayList());
    }

    private SynchronizedServer constructLocal() {
        return new SynchronizedServer(
                serverName,
                plugin.getServer().getIp(),
                plugin.getServer().getPort(),
                plugin.getServer().getMaxPlayers(),
                plugin.getServer().getOnlinePlayers().size(),
                Maps.newConcurrentMap(),
                false
        );
    }

    private void registerLocal() {
        SynchronizedServer synchronizedServer = constructLocal();
        servers.put(synchronizedServer.getName(), synchronizedServer);
        publish(synchronizedServer);
    }

    private void subscribe() {
        Executors.newCachedThreadPool().submit(() -> {
            try (Jedis connection = jedisPool.getResource()) {
                connection.subscribe(serversPubSub, CHANNEL);
            }
        });
    }

    public void publish(SynchronizedServer synchronizedServer) {
        Executors.newCachedThreadPool().submit(() -> {
            try (Jedis connection = jedisPool.getResource()) {
                connection.publish(CHANNEL, GSON.toJson(synchronizedServer));
            }
        });
    }

    public void registerListener(SynchronizedServerListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(SynchronizedServerListener listener) {
        listeners.remove(listener);
    }

    public void unregisterAllListeners() {
        listeners.clear();
    }

    public SynchronizedServer local() {
        return servers.get(serverName);
    }

    public void shutdown() {
        SynchronizedServer synchronizedServer = local();
        synchronizedServer.setDestroyed(true);
        try (Jedis connection = jedisPool.getResource()) {
            connection.publish(CHANNEL, GSON.toJson(synchronizedServer));
        }
        jedisPool.close();
    }

}
