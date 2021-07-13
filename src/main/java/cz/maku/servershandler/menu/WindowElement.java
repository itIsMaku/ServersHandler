package cz.maku.servershandler.menu;

import cz.maku.servershandler.item.response.ItemResponse;

public class WindowElement {

    private final int slot;
    private ItemResponse elementResponse;
    private WindowItem windowItem;
    private boolean pullable;

    public WindowElement(int slot) {
        this.slot = slot;
    }

    public WindowElement(int slot, boolean pullable) {
        this.slot = slot;
        this.pullable = pullable;
    }

    public void addElementResponse(int slot, ItemResponse elementResponse) {
        this.elementResponse = elementResponse;
        this.pullable = false;
    }

    public void addElementResponse(int slot, boolean pullable, ItemResponse elementResponse) {
        this.elementResponse = elementResponse;
        this.pullable = pullable;
    }

    public void addElementResponse(int slot, WindowItem guiExtenderItem) {
        this.pullable = guiExtenderItem.isPullable();
        this.windowItem = guiExtenderItem;
    }

    public int getSlot() {
        return slot;
    }

    public ItemResponse getItemResponse() {
        return elementResponse;
    }

    public WindowItem getWindowItem() {
        return windowItem;
    }

    public boolean isPullable() {
        return pullable;
    }

    @Override
    public String toString() {
        return "[" + slot + "] " + elementResponse.hashCode();
    }
}
