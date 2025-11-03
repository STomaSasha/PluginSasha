package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.PluginSasha;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class HelpCommand implements CommandExecutor {

    private final PluginSasha plugin;

    public HelpCommand(PluginSasha plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage("§6§lListe des commandes disponibles :");

        PluginDescriptionFile desc = plugin.getDescription();
        Set<String> commandKeys = desc.getCommands().keySet();

        for (String cmd : commandKeys) {
            org.bukkit.command.PluginCommand pluginCommand = plugin.getCommand(cmd);
            if (pluginCommand != null) {
                String usage = pluginCommand.getUsage();
                String description = pluginCommand.getDescription();

                sender.sendMessage("§e" + usage + " §7- " + description);
            }
        }

        return true;
    }
}
