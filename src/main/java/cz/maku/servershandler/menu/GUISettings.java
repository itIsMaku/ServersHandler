package cz.maku.servershandler.menu;

import cz.maku.servershandler.item.response.ItemResponse;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GUISettings {

    private boolean canEnterItems;
    private boolean canDrag;
    private final List<ItemStack> enterableItems = new ArrayList<>();
    private ItemResponse itemResponse;

    public void addEnterableItem(Material material) {
        this.enterableItems.add(new ItemStack(material));
    }

    public void addEnterableItem(Material material, short data) {
        this.enterableItems.add(new ItemStack(material, 1, data, null));
    }

    public void addEnterableItem(Material material, int data) {
        this.enterableItems.add(new ItemStack(material, 1, (short) data, null));
    }

    public boolean isCanEnterItems() {
        return canEnterItems;
    }

    public void setCanEnterItems(boolean canEnterItems) {
        this.canEnterItems = canEnterItems;
    }

    public boolean isCanDrag() {
        return canDrag;
    }

    public void setCanDrag(boolean canDrag) {
        this.canDrag = canDrag;
    }

    public List<ItemStack> getEnterableItems() {
        return enterableItems;
    }

    public ItemResponse getItemResponse() {
        return itemResponse;
    }

    public void setItemResponse(ItemResponse itemResponse) {
        this.itemResponse = itemResponse;
    }
}
