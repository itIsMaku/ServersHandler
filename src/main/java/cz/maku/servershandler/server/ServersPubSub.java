package cz.maku.servershandler.server;

import cz.maku.servershandler.bukkit.AsyncSynchronizedServerUpdatedEvent;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.JedisPubSub;

import static cz.maku.servershandler.PubSubConfiguration.CHANNEL;
import static cz.maku.servershandler.PubSubConfiguration.GSON;

@AllArgsConstructor
public abstract class ServersPubSub extends JedisPubSub {

    private final Plugin plugin;

    @Override
    public void onMessage(String channel, String message) {
        if (!channel.equals(CHANNEL)) return;
        SynchronizedServer synchronizedServer = GSON.fromJson(message, SynchronizedServer.class);
        onUpdate(synchronizedServer);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getPluginManager().callEvent(new AsyncSynchronizedServerUpdatedEvent(synchronizedServer)));
    }

    public abstract void onUpdate(SynchronizedServer synchronizedServer);

}
