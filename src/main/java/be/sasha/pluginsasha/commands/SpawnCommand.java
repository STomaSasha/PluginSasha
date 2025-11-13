package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.PluginSasha;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class SpawnCommand implements CommandExecutor {

    private final PluginSasha plugin;

    public SpawnCommand(PluginSasha plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cSeuls les joueurs peuvent utiliser cette commande !");
            return true;
        }

        File file = new File(plugin.getDataFolder(), "spawn.yml");
        if (!file.exists()) {
            player.sendMessage("§cAucun spawn n’a été défini !");
            return true;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String worldName = config.getString("spawn.world");
        if (worldName == null) {
            player.sendMessage("§cLe monde du spawn n’a pas été trouvé dans la configuration !");
            return true;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage("§cLe monde spécifié (" + worldName + ") n’existe pas !");
            return true;
        }

        double x = config.getDouble("spawn.x");
        double y = config.getDouble("spawn.y");
        double z = config.getDouble("spawn.z");
        float yaw = (float) config.getDouble("spawn.yaw");
        float pitch = (float) config.getDouble("spawn.pitch");

        Location spawn = new Location(world, x, y, z, yaw, pitch);
        player.teleport(spawn);
        player.sendMessage("§aTéléportation au spawn !");
        return true;
    }
}
