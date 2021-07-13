package cz.maku.servershandler.item.response;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface ItemResponse {

    void onForbiddenEnter(InventoryClickEvent event);

    void onForbiddenClick(InventoryClickEvent event);

    void onEnter(InventoryClickEvent event);

    void onClick(InventoryClickEvent event);

}
