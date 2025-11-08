package be.sasha.pluginsasha.listeners;

import be.sasha.pluginsasha.PluginSasha;
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
        String prefix = plugin.getGradeManager().getPrefix(event.getPlayer());
        event.setFormat(prefix+ event.getPlayer().getName()+ " §r "+event.getMessage());
    }
}
