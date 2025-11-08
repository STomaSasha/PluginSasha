package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.PluginSasha;
import be.sasha.pluginsasha.utils.MenuFactory;
import be.sasha.pluginsasha.utils.MenuHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MenuCommand implements CommandExecutor, Listener {

    private final PluginSasha plugin;
    private final Inventory menu2;
    private final Inventory menuOr;

    public MenuCommand(PluginSasha plugin) {
        this.plugin = plugin;

        menu2 = MenuFactory.createMenu2();
        menuOr = MenuFactory.createOptionOr();

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cSeuls les joueurs peuvent exécuter cette commande !");
            return true;
        }

        Inventory menuPrincipal = MenuFactory.createMenuPrincipal(player);
        player.openInventory(menuPrincipal);
        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        int ticksPlayed = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        long totalSeconds = ticksPlayed / 20;
        long sessionSeconds = plugin.getSessionDuration(player);
        String totalTime = formatTime(totalSeconds);
        String sessionTime = formatTime(sessionSeconds);



        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        event.setCancelled(true);

        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof MenuHolder menuHolder) {
            switch (menuHolder.getId()) {
                case "menuPrincipal":
                    if (event.getSlot() == 36 && clicked.getType() == Material.PLAYER_HEAD) {
                        player.sendMessage(Component.text("§7Pour nous rejoindre sur Discord, ")
                                .append(Component.text("§n§l§9clique ici !")
                                        .clickEvent(ClickEvent.openUrl("https://discord.gg/laube"))));
                    }
                    if (event.getSlot() == 40 && clicked.getType() == Material.PLAYER_HEAD) {

                        player.sendMessage(Component.text("§6§n§l"+player.getName()+":"
                                +"\n§r§eVie: §7"+player.getHealthScale()
                                +"\n§r§eLevel (ex): §7"+player.getLevel()
                                +"\n§r§eTemps de jeu (session): §7"+sessionTime
                                +"\n§r§eTemps de jeu total: §7"+totalTime));
                    }
                    break;
                case "menu2":
                    // actions pour menu2 si besoin
                    break;
            }
        }
    }

    private String formatTime(long seconds) {
        long h = seconds / 3600;
        long m = (seconds % 3600) / 60;
        long s = seconds % 60;
        return String.format("%02dh %02dm %02ds", h, m, s);
    }

}