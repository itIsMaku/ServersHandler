package cz.maku.servershandler.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class SynchronizedServer {

    private final String name;
    private final String address;
    private final int port;
    private int maxPlayers;
    private int onlinePlayers;
    private Map<String, Object> data;
    private boolean destroyed;

}
