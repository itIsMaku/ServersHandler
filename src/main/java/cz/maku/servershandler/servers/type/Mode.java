package cz.maku.servershandler.servers.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Mode {
    TWO("2 týmy"),
    FOUR("4 týmy");

    private final String displayName;
}
