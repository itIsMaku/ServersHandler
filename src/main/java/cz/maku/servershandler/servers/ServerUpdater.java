package cz.maku.servershandler.servers;

import java.util.Map;

public interface ServerUpdater {

    void onServersUpdate(Map<String, Server> map);

}
