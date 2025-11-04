package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.PluginSasha;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BagCommand implements CommandExecutor {

    private final PluginSasha plugin;
    private static final Map<UUID, Inventory> VIEWS = new HashMap<>();

    public BagCommand(PluginSasha plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cCette commande est réservée aux joueurs !");
            return true;
        }

        Inventory inventory = VIEWS.get(player.getUniqueId());
        if (inventory == null) {
            inventory = loadInventory(player);
            VIEWS.put(player.getUniqueId(), inventory);
        }

        player.openInventory(inventory);
        return true;
    }

    // --- Sauvegarde du sac ---
    public void saveInventory(Player player, Inventory inventory) {
        File file = new File(plugin.getDataFolder(), "bags.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("bags." + player.getUniqueId(), inventory.getContents());

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Erreur lors de la sauvegarde du sac de " + player.getName() + " : " + e.getMessage());
        }
    }

    // --- Chargement du sac ---
    public Inventory loadInventory(Player player) {
        File file = new File(plugin.getDataFolder(), "bags.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        List<?> items = config.getList("bags." + player.getUniqueId());
        Inventory inventory = Bukkit.createInventory(
                new BagHolder(),
                54,
                Component.text("Sac à dos de ", NamedTextColor.GOLD, TextDecoration.BOLD).append(Component.text(player.getName(), NamedTextColor.RED, TextDecoration.BOLD))
        );

        if (items != null) {
            ItemStack[] contents = items.toArray(new ItemStack[0]);
            inventory.setContents(contents);
        }

        return inventory;
    }

    // --- Quand le joueur quitte ---
    public void handlePlayerQuit(Player player) {
        Inventory inventory = VIEWS.remove(player.getUniqueId());
        if (inventory != null) {
            saveInventory(player, inventory);
        }
    }

    // --- Quand le joueur rejoint (optionnel, pour charger direct) ---
    public void handlePlayerJoin(Player player) {
        Inventory inventory = loadInventory(player);
        VIEWS.put(player.getUniqueId(), inventory);
    }

    // --- InventoryHolder pour Bukkit 1.20+ ---
    private static class BagHolder implements InventoryHolder {
        @Override
        public @NotNull Inventory getInventory() {
            return Bukkit.createInventory(this, 36);
        }
    }
}
