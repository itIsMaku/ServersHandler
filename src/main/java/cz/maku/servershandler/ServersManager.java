package cz.maku.servershandler;

import cz.maku.servershandler.servers.Server;

public class ServersManager {

    public static Server getServer() {
        return Instance.getInstance().getServersHandler().getServer();
    }


}
