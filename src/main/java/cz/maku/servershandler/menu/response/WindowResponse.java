package cz.maku.servershandler.menu.response;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface WindowResponse {

    void onOpen(InventoryOpenEvent event);

    void onClose(InventoryCloseEvent event);

}