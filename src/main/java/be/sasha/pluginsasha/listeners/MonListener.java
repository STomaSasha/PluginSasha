package be.sasha.pluginsasha.listeners;

import be.sasha.pluginsasha.PluginSasha;
import be.sasha.pluginsasha.commands.BagCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MonListener implements Listener {

    private final PluginSasha plugin;
    private final BagCommand bagCommand;

    public MonListener(PluginSasha plugin, BagCommand bagCommand) {
        this.plugin = plugin;
        this.bagCommand = bagCommand;
    }

    @EventHandler
    public void onJoinPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String pseudo = player.getName();

        // Message personnel
        player.sendMessage(Component.text("Bienvenue à toi !"));

        // Message global de bienvenue
        String msgBvn = plugin.getConfig().getString("join-message");
        if (msgBvn != null && !msgBvn.isEmpty()) {
            msgBvn = msgBvn.replace("%player%", pseudo);
            event.joinMessage(Component.text(msgBvn));
        } else {
            event.joinMessage(Component.text("§a" + pseudo + " a rejoint le serveur."));
        }

        // Chargement du sac du joueur dès qu’il rejoint
        bagCommand.handlePlayerJoin(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String pseudo = player.getName();

        // Message global de départ
        String msgQuit = plugin.getConfig().getString("leave-message");
        if (msgQuit != null && !msgQuit.isEmpty()) {
            msgQuit = msgQuit.replace("%player%", pseudo);
            event.quitMessage(Component.text(msgQuit));
        } else {
            event.quitMessage(Component.text("§c" + pseudo + " a quitté le serveur."));
        }

        // Sauvegarde du sac du joueur
        bagCommand.handlePlayerQuit(player);
    }
}
