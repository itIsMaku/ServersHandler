package cz.maku.servershandler.item;

import cz.maku.servershandler.menu.WindowItem;

public class FinalItemJob {

    private final int slot;
    private final WindowItem guiExtenderItem;

    public FinalItemJob(int slot, WindowItem guiExtenderItem) {
        this.slot = slot;
        this.guiExtenderItem = guiExtenderItem;
    }

    public FinalItemJob(WindowItem guiExtenderItem) {
        this.slot = -1;
        this.guiExtenderItem = guiExtenderItem;
    }

    public int getSlot() {
        return slot;
    }

    public WindowItem getGuiExtenderItem() {
        return guiExtenderItem;
    }
}
