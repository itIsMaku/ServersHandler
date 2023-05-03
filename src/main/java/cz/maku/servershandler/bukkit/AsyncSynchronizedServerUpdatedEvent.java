package cz.maku.servershandler.bukkit;

import cz.maku.servershandler.server.SynchronizedServer;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AsyncSynchronizedServerUpdatedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    @Getter
    private final SynchronizedServer synchronizedServer;

    public AsyncSynchronizedServerUpdatedEvent(SynchronizedServer synchronizedServer) {
        super(true);
        this.synchronizedServer = synchronizedServer;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
