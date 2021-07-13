package cz.maku.servershandler.item;

import cz.maku.servershandler.item.response.ItemResponse;

public class ItemPack {

    private int slot;
    private ItemBuilder itemBuilder;
    private ItemResponse elementResponse;

    public ItemPack(int slot, ItemBuilder itemBuilder) {
        this.itemBuilder = itemBuilder;
        this.slot = slot;
    }

    public ItemPack(int slot, ItemBuilder itemBuilder, ItemResponse elementResponse) {
        this.itemBuilder = itemBuilder;
        this.slot = slot;
        this.elementResponse = elementResponse;
    }

    public ItemPack(ItemBuilder itemBuilder) {
        this.itemBuilder = itemBuilder;
    }

    public ItemPack(ItemBuilder itemBuilder, ItemResponse elementResponse) {
        this.itemBuilder = itemBuilder;
        this.elementResponse = elementResponse;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    public void setItemBuilder(ItemBuilder itemBuilder) {
        this.itemBuilder = itemBuilder;
    }

    public ItemResponse getElementResponse() {
        return elementResponse;
    }

    public void setElementResponse(ItemResponse elementResponse) {
        this.elementResponse = elementResponse;
    }
}
