package be.sasha.pluginsasha.listeners;

import be.sasha.pluginsasha.PluginSasha;
import be.sasha.pluginsasha.commands.BagCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

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

        // Clear l'inventaire
        player.getInventory().clear();

        // Donner la boussole spéciale
        ItemStack menuCompass = new ItemStack(Material.COMPASS);
        ItemMeta meta = menuCompass.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("§aMenu Principal"));
            meta.lore(List.of(Component.text("§7Clique droit pour ouvrir")));
            menuCompass.setItemMeta(meta);
        }

        // Mettre la boussole au milieu de la barre principale
        player.getInventory().setItem(4, menuCompass);

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

    @EventHandler
    public void onRightClickStick(PlayerInteractEvent event) {
        switch (event.getAction()) {
            case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> {
                ItemStack item = event.getItem();
                if (item == null) return;

                ItemMeta meta = item.getItemMeta();
                if (meta == null) return;

                Player player = event.getPlayer();

                // Stick "Salut"
                if (item.getType() == Material.STICK
                        && meta.hasDisplayName() && meta.displayName().equals(Component.text("Salut"))
                        && meta.hasLore() && meta.lore().contains(Component.text("Clique droit"))) {
                    // Explosion visuelle sans dégâts ni destruction
                    player.getWorld().createExplosion(player.getLocation(), 0f, false, false);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
                    // Faire sauter le joueur
                    player.setVelocity(player.getVelocity().add(new org.bukkit.util.Vector(0, 1.2, 0)));
                }

                // Boussole "Menu Principal" (ouvre le menu)
                if (item.getType() == Material.COMPASS
                        && meta.hasDisplayName() && meta.displayName().equals(Component.text("§aMenu Principal"))
                        && meta.hasLore() && meta.lore().contains(Component.text("§7Clique droit pour ouvrir"))) {
                    // Ouvrir le menu
                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        new be.sasha.pluginsasha.commands.MenuCommand(plugin)
                                .onCommand(player, null, "menu", new String[]{});
                    });
                }
            }
        }
    }
}
