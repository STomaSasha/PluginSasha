package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.PluginSasha;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    private final PluginSasha plugin;

    public SetSpawnCommand(PluginSasha plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande !");
            return true;
        }
        Location spawn = player.getLocation();
        plugin.setSpawnLocation(spawn);
        int x= spawn.getBlockX();
        int y= spawn.getBlockY();
        int z= spawn.getBlockZ();
        World world = spawn.getWorld();
        player.sendMessage("Le spawn a été définis en X: "+x+" Y: "+y+" Z: "+z+" !");
        return true;
    }
}