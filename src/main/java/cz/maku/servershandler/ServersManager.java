package cz.maku.servershandler;

import cz.maku.servershandler.jedis.JedisHandler;
import cz.maku.servershandler.servers.Server;

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
}
