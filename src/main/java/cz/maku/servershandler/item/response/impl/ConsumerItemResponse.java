package cz.maku.servershandler.item.response.impl;

import cz.maku.servershandler.item.response.ItemResponse;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.util.Consumer;

import javax.annotation.Nullable;

public class ConsumerItemResponse implements ItemResponse {

    @Nullable
    private final Consumer<InventoryClickEvent> enter;

    @Nullable
    private final Consumer<InventoryClickEvent> click;

    @Nullable
    private final Consumer<InventoryClickEvent> forbiddenEnter;

    @Nullable
    private final Consumer<InventoryClickEvent> forbiddenClick;

    public ConsumerItemResponse(
            @Nullable Consumer<InventoryClickEvent> enter,
            @Nullable Consumer<InventoryClickEvent> click,
            @Nullable Consumer<InventoryClickEvent> forbiddenEnter,
            @Nullable Consumer<InventoryClickEvent> forbiddenClick
    ) {
        this.enter = enter;
        this.click = click;
        this.forbiddenEnter = forbiddenEnter;
        this.forbiddenClick = forbiddenClick;
    }

    @Override
    public void onEnter(InventoryClickEvent event) {
        if (enter != null) enter.accept(event);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (click != null) click.accept(event);
    }

    @Override
    public void onForbiddenEnter(InventoryClickEvent event) {
        if (forbiddenEnter != null) forbiddenEnter.accept(event);
    }

    @Override
    public void onForbiddenClick(InventoryClickEvent event) {
        if (forbiddenClick != null) forbiddenClick.accept(event);
    }
}
