package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.PluginSasha;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final PluginSasha plugin;

    public SpawnCommand(PluginSasha plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande !");
            return true;
        }

        Location spawn = plugin.getSpawnLocation();

        player.teleport(spawn);
        player.sendMessage("Téléportation au spawn !");
        return true;
    }
}