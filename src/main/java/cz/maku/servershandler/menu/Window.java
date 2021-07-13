package cz.maku.servershandler.menu;

import cz.maku.servershandler.item.ItemBuilder;
import cz.maku.servershandler.item.serializable.ItemSection;
import cz.maku.servershandler.item.serializable.SerializableItemBuilder;
import cz.maku.servershandler.menu.utils.ColorUtil;
import cz.maku.servershandler.menu.utils.Rows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.IntStream;

public class Window implements ConfigurationSerializable {

    private final UUID uniqueId = UUID.randomUUID();

    private Inventory inventory;

    private int slots = Rows.SIX.getSlots();

    private String title = "";

    private InventoryHolder holder;

    public Window(InventoryHolder holder, String title) {
        setupWindow(holder, title, slots);
    }

    public Window(InventoryHolder holder, String title, Rows rows) {
        setupWindow(holder, title, rows.getSlots());
    }

    public Window(String title, Rows rows) {
        setupWindow(null, title, rows.getSlots());
    }

    public Window(String title) {
        setupWindow(null, title, slots);
    }

    private void setupWindow(
            @Nullable InventoryHolder holder,
            String title,
            int slots
    ) {
        this.slots = slots;
        this.holder = holder;
        this.title = title;

        this.inventory = createInventory(holder, slots, title);
    }

    public void open(Player player) {
        player.openInventory(this.inventory);
    }

    public int addItem(ItemBuilder itemBuilder) {
        try {
            this.inventory.addItem(itemBuilder.getItem());
        } catch (Exception ignored) {}

        return getPosition(itemBuilder.getItem().getType(), itemBuilder.getItem().getDurability());
    }

    private int getPosition(Material material, int data) {
        ItemStack[] contents = inventory.getContents();

        return IntStream
                .range(0, contents.length)
                .filter(i -> contents[i] != null && contents[i].getType().equals(material) && contents[i].getDurability() == data)
                .findFirst()
                .orElse(-1);
    }

    public void setItem(int slot, ItemBuilder itemBuilder) {
        try {
            this.inventory.setItem(slot, itemBuilder.getItem());
        } catch (Exception ignored) {}
    }

    public void removeItem(int slot) {
        this.inventory.setItem(slot, new ItemStack(Material.AIR));
    }

    public static Inventory createInventory(InventoryHolder holder, Rows rows, String title) {
       return createInventory(holder, rows.getSlots(), title);
    }

    public static Inventory createInventory(InventoryHolder holder, int size, String title) {
        return Bukkit.createInventory(holder, size, ColorUtil.fixColor(title));
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public String toString() {
        return uniqueId.toString();
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getSlots() {
        return slots;
    }

    public String getTitle() {
        return title;
    }

    public InventoryHolder getHolder() {
        return holder;
    }

    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("title", getTitle());

        List<ItemSection> itemSections = new ArrayList<>();
        int slot = 0;
        for (ItemStack itemStack : getInventory().getContents()) {
            if (itemStack == null || itemStack.getType().equals(Material.AIR))
                continue;
            itemSections.add(new ItemSection(slot, new SerializableItemBuilder(new ItemBuilder(itemStack))));
            slot++;
        }
        List<Map<String, Object>> itemSectionsSerialized = new ArrayList<>();
        for (ItemSection section : itemSections) {
            itemSectionsSerialized.add(section.serialize());
        }

        data.put("inventory", itemSectionsSerialized);
        return data;
    }
}
