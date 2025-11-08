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
        if (slot >= 0 && slot < inventory.getSize()) {
            inventory.setItem(slot, item);
        } else if (slot == -1) {
            int size = inventory.getSize();
            int rows = size / 9;

            for (int i = 0; i < size; i++) {
                int col = i % 9;
                int row = i / 9;

                boolean isTopRow = row == 0;
                boolean isBottomRow = row == rows - 1;
                boolean isLeftCol = col == 0;
                boolean isRightCol = col == 8;

                if (isTopRow || isBottomRow || isLeftCol || isRightCol) {
                    inventory.setItem(i, item);
                }
            }
        }
        return this;
    }


    public Inventory build() {
        return inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }
}