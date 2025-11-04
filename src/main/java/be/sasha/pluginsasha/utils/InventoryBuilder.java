package be.sasha.pluginsasha.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryBuilder {

    private final Inventory inventory;

    public InventoryBuilder(InventoryHolder holder, int size, String title) {
        this.inventory = Bukkit.createInventory(holder, size, Component.text(title));
    }

    public InventoryBuilder addItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
        return this;
    }

    public Inventory build() {
        return inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
