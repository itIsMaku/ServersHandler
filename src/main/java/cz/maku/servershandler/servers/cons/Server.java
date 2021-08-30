package cz.maku.servershandler.servers.cons;

import cz.maku.servershandler.Instance;
import cz.maku.servershandler.ServersHandler;
import cz.maku.servershandler.servers.type.Mode;
import cz.maku.servershandler.servers.type.State;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class Server {

    private final String name;
    private final int id;
    private final boolean privateServer;
    private final int maxOnline;
    private int online;
    private final Mode mode;
    private String map;
    private State state;

    public Server(String name, int id, boolean privateServer, Mode mode) {
        this.name = name;
        this.id = id;
        this.privateServer = privateServer;
        this.online = Bukkit.getOnlinePlayers().size();
        this.maxOnline = Bukkit.getMaxPlayers();
        this.mode = mode;
        this.map = "Hlasuje se";
        this.state = State.WAITING;
    }

    public Server(String name, int id, boolean privateServer, Mode mode, String map, State state) {
        this.name = name;
        this.id = id;
        this.privateServer = privateServer;
        this.online = Bukkit.getOnlinePlayers().size();
        this.maxOnline = Bukkit.getMaxPlayers();
        this.mode = mode;
        this.map = map;
        this.state = state;
    }

    public Server(String name, int id, boolean privateServer, Mode mode, int online, int maxOnline) {
        this.name = name;
        this.id = id;
        this.privateServer = privateServer;
        this.online = online;
        this.maxOnline = maxOnline;
        this.mode = mode;
        this.map = "Hlasuje se";
        this.state = State.WAITING;
    }

    public Server(String name, int id, boolean privateServer, Mode mode, int online, int maxOnline, String map, State state) {
        this.name = name;
        this.id = id;
        this.privateServer = privateServer;
        this.online = online;
        this.maxOnline = maxOnline;
        this.mode = mode;
        this.map = map;
        this.state = state;
    }

    private void update() {
        ServersHandler serversHandler = Instance.getInstance().getServersHandler();
        serversHandler.getJedisHandler().getJedis().hset("mservers", name + "-" + id, serversHandler.serialize(this));
        serversHandler.getServers().put(name + "-" + id, this);
        serversHandler.getUpdater().getUpdaters().forEach(updater -> {
            updater.onServersUpdate(serversHandler.getServers());
        });
    }

    public void setOnline(int online) {
        this.online = online;
        update();
    }

    public void setState(State state) {
        this.state = state;
        update();
    }

    public void setMap(String map) {
        this.map = map;
        update();
    }


    @Override
    public String toString() {
        return "Server{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", privateServer=" + privateServer +
                ", maxOnline=" + maxOnline +
                ", online=" + online +
                ", mode=" + mode +
                ", map='" + map + '\'' +
                ", state=" + state +
                '}';
    }
}
