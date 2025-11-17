package be.sasha.pluginsasha.login;

import be.sasha.pluginsasha.PluginSasha;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor {

    private final LoginManager manager;

    public RegisterCommand(PluginSasha plugin) {
        this.manager = plugin.getLoginManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§cUsage : /register <motdepasse>");
            return true;
        }

        if (manager.isRegistered(player.getName())) {
            player.sendMessage("§cVous êtes déjà inscrit !");
            return true;
        }

        manager.register(player.getName(), args[0]);
        player.sendMessage("§aInscription réussie ! Vous pouvez maintenant vous connecter avec /login <mdp>");

        return true;
    }
}
