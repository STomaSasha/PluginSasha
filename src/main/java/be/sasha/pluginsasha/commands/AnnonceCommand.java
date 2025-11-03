package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.PluginSasha;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AnnonceCommand implements CommandExecutor {

    private final PluginSasha plugin;

    public AnnonceCommand(PluginSasha plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /" + label + " <message>");
            return true;
        }

        String message = String.join(" ", args);
        Component annonce = Component.text("§l§c[Annonce] " + message);
        Bukkit.broadcast(annonce);

        return true;
    }
}
