package cz.maku.servershandler.menu;

import cz.maku.servershandler.item.FinalItemJob;
import cz.maku.servershandler.item.ItemBuilder;
import cz.maku.servershandler.item.ItemPack;
import cz.maku.servershandler.item.response.ItemResponse;
import cz.maku.servershandler.menu.response.WindowResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

public abstract class AbstractMenu implements Listener, WindowResponse {

    private final UUID id;

    private final List<FinalItemJob> jobs = new ArrayList<>();
    private final Map<Integer, WindowElement> elements = new HashMap<>();
    private GUISettings guiSettings;
    private Window gui;
    private WindowResponse windowResponse;

    private Plugin plugin;

    public AbstractMenu(Window gui, Plugin plugin) {
        this.gui = gui;
        this.plugin = plugin;

        this.id = UUID.randomUUID(); // fixme: fix

        this.guiSettings = new GUISettings();
        this.guiSettings.setCanEnterItems(false);
        this.guiSettings.setCanDrag(false);


        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    public void setGUI(Window gui) {
        this.gui = gui;
        this.elements.clear();
        this.jobs.clear();
    }

    public void addEmptyElementResponse(int slot, boolean pullable) {
        WindowElement guiElement = new WindowElement(slot, pullable);
        elements.put(slot, guiElement);
    }

    public void addElementResponse(int slot, ItemResponse itemResponse) {
        WindowElement guiElement = new WindowElement(slot);
        guiElement.addElementResponse(slot, itemResponse);
        elements.put(slot, guiElement);
    }

    public void addElementResponse(int slot, boolean pullable, ItemResponse itemResponse) {
        WindowElement guiElement = new WindowElement(slot);
        guiElement.addElementResponse(slot, pullable, itemResponse);
        elements.put(slot, guiElement);
    }

    public void addElementResponse(int slot, WindowItem guiExtenderItem) {
        WindowElement guiElement = new WindowElement(slot);
        guiElement.addElementResponse(slot, guiExtenderItem.isPullable(), guiExtenderItem);
        elements.put(slot, guiElement);
    }

    private void addEmptyElementResponse(int slot) {
        WindowElement guiElement = new WindowElement(slot);
        elements.put(slot, guiElement);
    }

    public void addWindowResponse(WindowResponse windowResponse) {
        this.windowResponse = windowResponse;
    }


    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView().getTopInventory().equals(getBukkitInventory())
                && !guiSettings.isCanDrag()) {
            event.setCancelled(true);
            return;
        }
        if (guiSettings.isCanDrag() &&
                canEnter(event.getCursor())) {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView() == null
                || event.getView().getTopInventory() == null
                || event.getView().getBottomInventory() == null
                || event.getClickedInventory() == null)
            return;

        if (guiSettings.isCanEnterItems()) {
            if (!event.isShiftClick()) {
                // TODO: refactorThis
                if (event.getView().getTopInventory().equals(getBukkitInventory())
                        && event.getClickedInventory().equals(getBukkitInventory())
                        && event.getCursor() != null
                        && canEnter(event.getCursor())) {
                    if (guiSettings.getItemResponse() != null)
                        guiSettings.getItemResponse().onEnter(event);
                    event.setCancelled(false);
                    return;
                } else if (event.getView().getTopInventory().equals(getBukkitInventory())
                        && event.getClickedInventory().equals(getBukkitInventory())
                        && event.getCursor() != null
                        && !canEnter(event.getCursor())) {
                    if (guiSettings.getItemResponse() != null)
                        guiSettings.getItemResponse().onForbiddenEnter(event);
                    event.setCancelled(true);
                    return;
                }
            } else {
                if (event.getView().getTopInventory().equals(getBukkitInventory())
                        && !event.getClickedInventory().equals(getBukkitInventory())
                        && event.getCurrentItem() != null
                        && canEnter(event.getCurrentItem())) {
                    if (guiSettings.getItemResponse() != null)
                        guiSettings.getItemResponse().onClick(event);
                    event.setCancelled(false);
                    return;
                } else if (event.getView().getTopInventory().equals(getBukkitInventory())
                        && !event.getClickedInventory().equals(getBukkitInventory())
                        && event.getCurrentItem() != null
                        && !canEnter(event.getCurrentItem())) {
                    if (guiSettings.getItemResponse() != null)
                        guiSettings.getItemResponse().onForbiddenClick(event);
                    event.setCancelled(true);
                    return;
                }
            }
        }

        if (event.getView().getTopInventory().equals(getBukkitInventory())
                && !guiSettings.isCanEnterItems()) {
            if (event.isShiftClick() &&
                    !event.getClickedInventory().equals(getBukkitInventory())) {
                event.setCancelled(true);
                return;
            } else if (!event.isShiftClick() &&
                    event.getClickedInventory().equals(getBukkitInventory())
                    && (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR))) {
                event.setCancelled(true);
                return;
            } else if (!event.isShiftClick() &&
                    event.getClickedInventory().equals(getBukkitInventory())) {
                checkElements(event);
                return;
            } else if (event.isShiftClick() &&
                    event.getClickedInventory().equals(getBukkitInventory())) {
                checkElements(event);
                return;
            }
            event.setCancelled(false);
            return;
        }
        checkElements(event);
    }

    private boolean canEnter(ItemStack itemStack) {
        if (guiSettings.isCanEnterItems()) {
            List<ItemStack> materials = guiSettings.getEnterableItems();

            if (materials.isEmpty())
                return true;

            if (itemStack == null || itemStack.getType().equals(Material.AIR))
                return true;

            for (ItemStack entry : materials) {
                Material material = entry.getType();
                short data = entry.getDurability();

                if (itemStack.getType().equals(material)
                        && itemStack.getDurability() == data)
                    return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getView().getTopInventory().equals(getBukkitInventory())) {
            if (windowResponse != null)
                windowResponse.onOpen(e);

            this.onOpen(e);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getView().getTopInventory().equals(getBukkitInventory())) {
            if (windowResponse != null)
                windowResponse.onClose(e);

            this.onClose(e);

            Bukkit.getScheduler().runTaskLater(plugin, () ->
                    ((Player) e.getPlayer()).updateInventory(), 5);
            // I need to update the inventory because minecraft is weird
            // otherwise you can make the item to.. "stay" in your inventory until you do something with it
        }
    }

    public void setItem(ItemPack... itemPacks) {
        for (ItemPack itemPack : itemPacks) {
            ItemBuilder itemBuilder = itemPack.getItemBuilder();
            int slot = itemPack.getSlot();

            if (itemPack.getElementResponse() != null)
                addElementResponse(slot, itemPack.getElementResponse());
            else addEmptyElementResponse(slot);

            gui.setItem(slot, itemBuilder);
            updateInventory();
        }
    }

    public void changeItem(int slot, ItemBuilder itemBuilder) {
        gui.setItem(slot, itemBuilder);
        updateInventory();
    }

    public void setItem(int slot, ItemBuilder itemBuilder) {
        gui.setItem(slot, itemBuilder);
        addEmptyElementResponse(slot);
        updateInventory();
    }

    public void setItem(int slot, ItemBuilder itemBuilder, ItemResponse itemResponse) {
        gui.setItem(slot, itemBuilder);
        addElementResponse(slot, itemResponse);
        updateInventory();
    }

    public void setItem(int slot, WindowItem guiExtenderItem) {
        jobs.add(new FinalItemJob(slot, guiExtenderItem));
    }

    public int addItem(ItemBuilder itemBuilder) {
        int index = gui.addItem(itemBuilder);
        addEmptyElementResponse(index);
        updateInventory();
        return index;
    }

    public int addItem(ItemBuilder itemBuilder, ItemResponse itemResponse) {
        int index = gui.addItem(itemBuilder);
        addElementResponse(index, itemResponse);
        updateInventory();
        return index;
    }

    public void addItem(WindowItem guiExtenderItem) {
        jobs.add(new FinalItemJob(guiExtenderItem));
    }

    public void removeItem(int slot) {
        gui.removeItem(slot);
    }

    public void openInventory(Player player) {
        if (!jobs.isEmpty()) {
            for (FinalItemJob finalItemJob : jobs) {
                if (finalItemJob.getSlot() == -1) {
                    addExtenderItem(finalItemJob.getGuiExtenderItem(),
                            player);
                } else setExtenderItem(finalItemJob.getSlot(),
                        finalItemJob.getGuiExtenderItem(),
                        player);
            }
            jobs.clear();
        }

        player.openInventory(getBukkitInventory());
    }

    public Inventory getBukkitInventory() {
        return gui.getInventory();
    }

    public void updateInventory() {
        List<Integer> slots = new ArrayList<>();
        int temp = 0;
        for (ItemStack itemStack : getBukkitInventory().getContents()) {
            temp++;
            if (itemStack == null || itemStack.getType().equals(Material.AIR))
                continue;

            int current = temp - 1;
            slots.add(current);
        }

        for (int slot : elements.keySet())
            if (slots.contains(slot))
                slots.remove((Integer) slot);

        for (int slot : slots)
            addEmptyElementResponse(slot);

        getBukkitInventory().getViewers().forEach(viewer -> ((Player) viewer).updateInventory());
    }

    private void setExtenderItem(int slot, WindowItem guiExtenderItem, Player player) {
        gui.setItem(slot, guiExtenderItem.getItemBuilder(player));
        addElementResponse(slot, guiExtenderItem);
        updateInventory();
    }

    private void addExtenderItem(WindowItem windowItem, Player player) {
        int index = gui.addItem(windowItem.getItemBuilder(player));
        addElementResponse(index, windowItem);
        updateInventory();
    }

    private void checkElements(InventoryClickEvent event) {
        for (WindowElement element : elements.values()) {
            int slot = element.getSlot();

            if (slot != event.getSlot() ||
                    !Objects.equals(event.getClickedInventory(), getBukkitInventory())
                    || !event.getView().getTopInventory().equals(getBukkitInventory())
            )
                continue;

            event.setCancelled(!element.isPullable());

            if (element.getItemResponse() != null)
                element.getItemResponse().onClick(event);

            else if (element.getWindowItem() != null)
                element.getWindowItem().onClick(event);
        }

    }

    public UUID getId() {
        return id;
    }

    public List<FinalItemJob> getJobs() {
        return jobs;
    }

    public Map<Integer, WindowElement> getElements() {
        return elements;
    }

    public GUISettings getGuiSettings() {
        return guiSettings;
    }

    public Window getGui() {
        return gui;
    }

    public WindowResponse getWindowResponse() {
        return windowResponse;
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }
}
