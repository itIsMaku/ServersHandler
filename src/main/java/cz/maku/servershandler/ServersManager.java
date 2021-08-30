package cz.maku.servershandler;

import cz.maku.servershandler.jedis.JedisHandler;
import cz.maku.servershandler.servers.cons.Server;
import cz.maku.servershandler.servers.interfaces.ServerUpdater;

import java.util.List;
import java.util.Map;

public class ServersManager {

    public static ServersHandler getServersHandler() {
        return Instance.getInstance().getServersHandler();
    }

    public static Server getServer() {
        return getServersHandler().getServer();
    }

    public static Map<String, Server> getServers() {
        return getServersHandler().getServers();
    }

    public static JedisHandler getJedisHandler() {
        return getServersHandler().getJedisHandler();
    }

    public static List<ServerUpdater> getUpdaters() {
        return getServersHandler().getUpdater().getUpdaters();
    }

    public static void add(ServerUpdater serverUpdater) {
        getServersHandler().getUpdater().add(serverUpdater);
    }

    public static void remove(ServerUpdater serverUpdater) {
        getServersHandler().getUpdater().remove(serverUpdater);
    }
}
