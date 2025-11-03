package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.PluginSasha;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class BagCommand implements CommandExecutor {

    private final PluginSasha plugin;
    private static final Map<Player, Inventory> VIEWS = new HashMap<>();

    public BagCommand (PluginSasha plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return true;
        }

        Inventory inventory = VIEWS.get(player);

        if (inventory == null) {
            inventory = Bukkit.createInventory(null, 54, player.getName() + "'s stash");
            VIEWS.put(player, inventory);
        }

        player.openInventory(inventory);
        return true;
    }

    public static void handlePlayerQuit(Player player) {
        Inventory inventory = VIEWS.remove(player);
        if (inventory != null) {
            // Sauvegarde à implémenter ici
        }
    }
}
