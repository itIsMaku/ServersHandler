package cz.maku.servershandler.servers.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum State {
    WAITING("Čekání na hráče..."),
    STARTING("Startuje..."),
    RUNNING("Aréna je ve hře"),
    ENDING("Aréna je ve hře");

    private final String displayName;

    public static State getByDisplayName(String displayName) {
        return Arrays.stream(State.values()).filter(state -> displayName.equalsIgnoreCase(state.getDisplayName())).findFirst().orElse(null);
    }
}
