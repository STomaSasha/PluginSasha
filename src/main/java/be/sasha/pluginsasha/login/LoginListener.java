package be.sasha.pluginsasha.login;

import be.sasha.pluginsasha.PluginSasha;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class LoginListener implements Listener {

    private final LoginManager manager;

    public LoginListener(PluginSasha plugin) {
        this.manager = plugin.getLoginManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // Au join, le joueur n'est pas encore authentifié
        manager.setAuthenticated(event.getPlayer(), false);
        event.getPlayer().sendMessage("§eVeuillez vous connecter avec /login <mdp>");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        // Si non authentifié, on bloque le mouvement
        if (!manager.isAuthenticated(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
