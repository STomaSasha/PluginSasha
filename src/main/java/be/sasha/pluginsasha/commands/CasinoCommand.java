package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.PluginSasha;
import be.sasha.pluginsasha.utils.InventoryBuilder;
import be.sasha.pluginsasha.utils.ItemBuilder;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CasinoCommand implements CommandExecutor, Listener {

    private final PluginSasha plugin;

    public CasinoCommand(PluginSasha plugin) {
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

        // Créer et ouvrir le GUI Casino
        CasinoInventory casinoInventory = new CasinoInventory();
        player.openInventory(casinoInventory.getInventory());

        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        if (inv == null || !(inv.getHolder() instanceof CasinoInventory casinoInventory)) return;

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked != null) {
            switch (clicked.getType()) {
                case DIAMOND -> {
                    // Exemple : ajouter des actions spécifiques
                    event.getWhoClicked().sendMessage("Spinning wheel");
                }
                case EMERALD -> {
                    event.getWhoClicked().sendMessage("§aVous avez cliqué sur l'émeraude !");
                }
                default -> {
                    // Rien à faire
                }
            }
        }
    }

    // ----------------------------
    // Classe interne CasinoInventory
    // ----------------------------
    private static class CasinoInventory implements InventoryHolder {

        private final Inventory inventory;

        public CasinoInventory() {
            this.inventory = new InventoryBuilder(this, 9, "Casino").build();

            // Exemple : bouton diamant avec enchantement
            ItemStack diamond = new ItemBuilder(Material.DIAMOND)
                    .setName("Spin Wheel")
                    .addEnchants(Map.of(Enchantment.KNOCKBACK, 1))
                    .build();

            // Exemple : bouton émeraude
            ItemStack emerald = new ItemBuilder(Material.EMERALD)
                    .setName("Cashout")
                    .build();

            inventory.setItem(3, diamond);
            inventory.setItem(5, emerald);
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }

    }
}
