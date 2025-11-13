package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.PluginSasha;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SetSpawnCommand implements CommandExecutor {

    private final PluginSasha plugin;

    public SetSpawnCommand(PluginSasha plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cSeuls les joueurs peuvent utiliser cette commande !");
            return true;
        }

        Location spawn = player.getLocation();
        World world = spawn.getWorld();
        if (world == null) {
            player.sendMessage("§cErreur : monde introuvable !");
            return true;
        }

        // Création du fichier si nécessaire
        File file = new File(plugin.getDataFolder(), "spawn.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Sauvegarde des coordonnées
        config.set("spawn.world", world.getName());
        config.set("spawn.x", spawn.getX());
        config.set("spawn.y", spawn.getY());
        config.set("spawn.z", spawn.getZ());
        config.set("spawn.yaw", spawn.getYaw());
        config.set("spawn.pitch", spawn.getPitch());

        try {
            config.save(file);
            player.sendMessage(String.format("§aSpawn défini en §eX: %.1f Y: %.1f Z: %.1f §a!", spawn.getX(), spawn.getY(), spawn.getZ()));
        } catch (IOException e) {
            plugin.getLogger().severe("Erreur lors de la sauvegarde de spawn.yml !");
            e.printStackTrace();
            player.sendMessage("§cErreur lors de la sauvegarde du spawn !");
        }

        // Si tu veux que ton plugin garde le spawn en mémoire :
        plugin.setSpawnLocation(spawn);

        return true;
    }
}
