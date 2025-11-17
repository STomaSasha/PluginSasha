package be.sasha.pluginsasha.login;

import be.sasha.pluginsasha.PluginSasha;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginCommand implements CommandExecutor {

    private final LoginManager manager;

    public LoginCommand(PluginSasha plugin) {
        this.manager = plugin.getLoginManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cSeuls les joueurs peuvent utiliser cette commande !");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§eUsage: /login <motdepasse>");
            return true;
        }

        String password = args[0];

        if (!manager.isRegistered(player.getName())) {
            player.sendMessage("§cVous n'êtes pas enregistré ! Utilisez /register <motdepasse>");
            return true;
        }

        if (manager.checkPassword(player.getName(), password)) {
            manager.setAuthenticated(player, true);
            player.sendMessage("§aVous êtes maintenant connecté !");
        } else {
            player.sendMessage("§cMot de passe incorrect !");
        }

        return true;
    }
}
