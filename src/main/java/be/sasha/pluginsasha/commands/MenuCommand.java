package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.PluginSasha;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class MenuCommand implements CommandExecutor, Listener {

    private final PluginSasha plugin;

    public MenuCommand(PluginSasha plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cSeuls les joueurs peuvent exécuter cette commande !");
            return true;
        }

        MyInventory myInventory = new MyInventory(plugin);
        player.openInventory(myInventory.getInventory());
        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // We're getting the clicked inventory to avoid situations where the player
        // already has a stone in their inventory and clicks that one.
        Inventory inventory = event.getClickedInventory();
        // Add a null check in case the player clicked outside the window.
        if (inventory == null || !(inventory.getHolder(false) instanceof MyInventory myInventory)) {
            return;
        }

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        // Check if the player clicked the stone.
        if (clicked != null && clicked.getType() == Material.STONE) {
            // Use the method we have on MyInventory to increment the field
            // and update the counter.
            myInventory.addClick();
        }
    }

    private static class MyInventory implements InventoryHolder {

        private final Inventory inventory;

        private int clicks = 0;

        public MyInventory(PluginSasha plugin) {
            this.inventory = plugin.getServer().createInventory(
                    this,
                    27,
                    Component.text("Mon Menu", NamedTextColor.GOLD, TextDecoration.BOLD)
            );

            ItemStack stone = new ItemStack(Material.STONE);
            ItemMeta meta = stone.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text("Clique !", NamedTextColor.GOLD));
                stone.setItemMeta(meta);
            }



            this.inventory.setItem(13, stone);
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }



        // A method we will call in the listener whenever the player clicks the stone.
        public void addClick() {
            this.clicks++;
            this.updateCounter();
        }

        // A method that will update the counter item.
        private void updateCounter() {
            this.inventory.setItem(13, ItemStack.of(Material.BEDROCK, this.clicks));
        }
    }


}
