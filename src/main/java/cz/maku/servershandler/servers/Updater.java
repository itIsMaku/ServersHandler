package cz.maku.servershandler.servers;

import com.google.common.collect.Lists;
import cz.maku.servershandler.servers.interfaces.ServerUpdater;
import lombok.Getter;

import java.util.List;

public class Updater {

    @Getter
    private final List<ServerUpdater> updaters;

    public Updater() {
        updaters = Lists.newArrayList();
    }

    public void add(ServerUpdater serverUpdater) {
        updaters.add(serverUpdater);
    }

    public void remove(ServerUpdater serverUpdater) {
        updaters.remove(serverUpdater);
    }

}
