package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.PluginSasha;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    private final PluginSasha plugin;

    public FlyCommand(PluginSasha plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("pluginsasha.commands.fly")) {
            player.sendMessage("§cTu n'as pas la permission de faire ça !");
            return true;
        }

        // --- No arguments: toggle own fly ---
        if (args.length == 0) {
            toggleFly(player, player);
            return true;
        }

        // --- Get target player ---
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§cLe joueur '" + args[0] + "' n'est pas en ligne.");
            return true;
        }

        // --- One argument: toggle target fly ---
        if (args.length == 1) {
            toggleFly(player, target);
            return true;
        }

        // --- Two arguments: explicit on/off ---
        String state = args[1].toLowerCase();
        if (state.equals("on")) {
            setFly(target, true);
            target.sendMessage("§aFly activé !");
            if (!player.equals(target))
                player.sendMessage("§aFly activé pour " + target.getName() + " !");
        } else if (state.equals("off")) {
            setFly(target, false);
            target.sendMessage("§cFly désactivé !");
            if (!player.equals(target))
                player.sendMessage("§cFly désactivé pour " + target.getName() + " !");
        } else {
            player.sendMessage("§cUtilise /fly <joueur> [on|off]");
        }

        return true;
    }

    private void toggleFly(Player sender, Player target) {
        boolean enable = !target.getAllowFlight();
        setFly(target, enable);

        if (enable) {
            target.sendMessage("§aFly activé !");
            if (!sender.equals(target)) sender.sendMessage("§aFly activé pour " + target.getName() + " !");
        } else {
            target.sendMessage("§cFly désactivé !");
            if (!sender.equals(target)) sender.sendMessage("§cFly désactivé pour " + target.getName() + " !");
        }
    }

    private void setFly(Player player, boolean enable) {
        player.setAllowFlight(enable);
        player.setFlying(enable);
    }
}
