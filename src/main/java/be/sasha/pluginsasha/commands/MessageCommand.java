package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.PluginSasha;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCommand implements CommandExecutor {

    private final PluginSasha plugin;

    public MessageCommand(PluginSasha plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage("Usage: /message <joueur> <message>");
            return true;
        }
        Player receiver = Bukkit.getPlayer(args[0]);
        if (receiver == null) {
            sender.sendMessage("Ce joueur n'est pas en ligne.");
            return true;
        }

        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            messageBuilder.append(args[i]).append(" ");
        }

        Component msgReceivCom = Component.text("§e" + player.getName() + " -> Toi: §r" + messageBuilder.toString().trim());
        receiver.sendMessage(msgReceivCom);
        Component msgSenderCom = Component.text("§eToi -> " + receiver.getName() + ": §r" + messageBuilder.toString().trim());
        player.sendMessage(msgSenderCom);

        return true;
    }

}
