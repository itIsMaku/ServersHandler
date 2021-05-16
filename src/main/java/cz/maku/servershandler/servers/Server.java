package cz.maku.servershandler.servers;

import cz.maku.servershandler.Instance;
import cz.maku.servershandler.ServersHandler;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class Server {

    private final String name;
    private final int id;
    private final boolean privateServer;
    private final int maxOnline;
    private int online;

    public Server(String name, int id, boolean privateServer) {
        this.name = name;
        this.id = id;
        this.privateServer = privateServer;
        this.online = Bukkit.getOnlinePlayers().size();
        this.maxOnline = Bukkit.getMaxPlayers();
    }

    public Server(String name, int id, boolean privateServer, int online, int maxOnline) {
        this.name = name;
        this.id = id;
        this.privateServer = privateServer;
        this.online = online;
        this.maxOnline = maxOnline;
    }

    public void setOnline(int online) {
        this.online = online;
        ServersHandler serversHandler = Instance.getInstance().getServersHandler();
        serversHandler.getJedisHandler().getJedis().hset("servers", name, serversHandler.deserialize(this));
        serversHandler.publish();
        serversHandler.onServersUpdate(serversHandler.getServers());
    }
}
