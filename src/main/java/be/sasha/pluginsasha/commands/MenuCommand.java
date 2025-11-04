package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.PluginSasha;
import be.sasha.pluginsasha.utils.InventoryBuilder;
import be.sasha.pluginsasha.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MenuCommand implements CommandExecutor, Listener {

    private final PluginSasha plugin;

    public MenuCommand(PluginSasha plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cSeuls les joueurs peuvent exécuter cette commande !");
            return true;
        }

        openMenu(player);
        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null || !(inventory.getHolder() instanceof MyInventory)) return;

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) return;

        // Ici tu peux gérer ce que chaque item fait
        if (clicked.getType() == Material.DIAMOND) {
            event.getWhoClicked().sendMessage("Tu as choisi l'option Diamant !");
        } else if (clicked.getType() == Material.GOLD_INGOT) {
            event.getWhoClicked().sendMessage("Tu as choisi l'option Or !");
        } else if (clicked.getType() == Material.EMERALD) {
            event.getWhoClicked().sendMessage("Tu as choisi l'option Emeraude !");
        }
    }

    @EventHandler
    public void onRightClickMenuStick(PlayerInteractEvent event) {
        switch (event.getAction()) {
            case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> {
                ItemStack item = event.getItem();
                if (item == null || item.getType() != Material.STICK) return;

                if (!item.hasItemMeta()) return;

                if (!item.getItemMeta().displayName().equals(Component.text("§aMenu Principal"))) return;

                Player player = event.getPlayer();
                openMenu(player);
            }
        }
    }

    private void openMenu(Player player) {
        MyInventory menu = new MyInventory(plugin);
        player.openInventory(menu.getInventory());
    }

    private static class MyInventory implements InventoryHolder {
        private final Inventory inventory;

        public MyInventory(PluginSasha plugin) {
            this.inventory = new InventoryBuilder(this, 9, "Choisis ton option !").build();

            // Exemple d'items de menu
            inventory.setItem(2, new ItemBuilder(Material.DIAMOND).setName("Option Diamant").build());
            inventory.setItem(4, new ItemBuilder(Material.GOLD_INGOT).setName("Option Or").build());
            inventory.setItem(6, new ItemBuilder(Material.EMERALD).setName("Option Émeraude").build());
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }
    }
}
