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
    private final LegacyComponentSerializer legacy = LegacyComponentSerializer.legacySection();

    public ChatListener(PluginSasha plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String prefix = plugin.getGradeManager().getPrefix(player);

        // Empêcher l’event d’envoyer le message
        event.setCancelled(true);

        // Construction du texte avec couleurs
        String raw = prefix + player.getName() + " §r: " + event.getMessage();
        Component component = legacy.deserialize(raw);

        // Broadcast adventure
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(component);
        }

        // Console
        Bukkit.getConsoleSender().sendMessage(raw);
    }
}
