package cz.maku.servershandler;

import com.google.gson.Gson;
import cz.maku.servershandler.jedis.JedisPoolBuilder;
import cz.maku.servershandler.server.SynchronizedServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class Example extends JavaPlugin {

    private ServersHandler serversHandler;

    @Override
    public void onEnable() {
        serversHandler = ServersHandler.initialize(this, "Lobby", new JedisPoolBuilder()
                .withDefaultConfig()
                .hostname("localhost")
                .port(6379)
        );

        serversHandler.registerListener(synchronizedServer -> {
            System.out.printf(
                    "Server %s was updated! (%s/%s)%n",
                    synchronizedServer.getName(),
                    synchronizedServer.getOnlinePlayers(),
                    synchronizedServer.getMaxPlayers()
            );

            Map<String, Object> data = synchronizedServer.getData();
            System.out.println("value: " + data.get("key"));
        });

        Map<String, SynchronizedServer> servers = serversHandler.getServers();
        System.out.println("Servers: " + servers.size());

        SynchronizedServer local = serversHandler.local();
        System.out.println("Local server: " + local.getName());

        SynchronizedServer server = serversHandler.getServers().get("Lobby");
        System.out.println("Lobby players: " + server.getOnlinePlayers());
    }

    @Override
    public void onDisable() {
        serversHandler.shutdown();
    }

    public void setKeyValue() {
        SynchronizedServer local = serversHandler.local();
        local.getData().put("key", "value");
        serversHandler.publish(local);
    }
}
