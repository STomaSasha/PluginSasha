package be.sasha.pluginsasha.listeners;

import be.sasha.pluginsasha.PluginSasha;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final PluginSasha plugin;

    public ChatListener(PluginSasha plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        // Récupération du préfixe
        String prefix = plugin.getGradeManager().getPrefix(player);

        // Empêche l’event de gérer lui-même l’envoi du message
        event.setCancelled(true);

        // Construction du message + conversion legacy -> Adventure
        String rawMessage = prefix + player.getName() + " §r " + event.getMessage();
        Component message = LegacyComponentSerializer.legacySection().deserialize(rawMessage);

        // Envoi du message à tous les joueurs
        Bukkit.broadcast(message);
    }
}
