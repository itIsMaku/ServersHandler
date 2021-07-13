package cz.maku.servershandler.menu.response.impl;

import cz.maku.servershandler.menu.response.WindowResponse;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ConsumerWindowResponse implements WindowResponse {

    @Nullable
    private final Consumer<InventoryOpenEvent> open;

    @Nullable
    private final Consumer<InventoryCloseEvent> close;

    public ConsumerWindowResponse(
        @Nullable Consumer<InventoryOpenEvent> open,
        @Nullable Consumer<InventoryCloseEvent> close
    ) {
        this.open = open;
        this.close = close;
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        if (open != null) open.accept(event);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if (close != null) close.accept(event);
    }
}
