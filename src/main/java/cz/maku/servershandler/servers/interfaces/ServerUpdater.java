package cz.maku.servershandler.servers.interfaces;

import cz.maku.servershandler.servers.cons.Server;

import java.util.Map;

public interface ServerUpdater {

    void onServersUpdate(Map<String, Server> map);

}
