package cz.maku.servershandler.menu;

import cz.maku.servershandler.item.ItemBuilder;
import cz.maku.servershandler.item.response.ItemResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public abstract class WindowItem implements ItemResponse {

    private final ItemBuilder itemBuilder;
    private boolean pullable;

    public WindowItem(ItemBuilder itemBuilder) {
        this.itemBuilder = itemBuilder;
    }

    public WindowItem() {
        this.itemBuilder = new ItemBuilder(Material.AIR);
    }

    // You can override this based on a player for example.
    public ItemBuilder getItemBuilder(Player player) {
        return itemBuilder;
    }

    public boolean isPullable() {
        return pullable;
    }

    public void setPullable(boolean pullable) {
        this.pullable = pullable;
    }

}