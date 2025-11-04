package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.PluginSasha;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class MenuCommand implements CommandExecutor {

    private final PluginSasha plugin;

    public MenuCommand(PluginSasha plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        Player player = (Player) sender; // Assume we have a Player instance.
        // This can be a command, another event or anywhere else you have a Player.

        MyInventory myInventory = new MyInventory(plugin);
        player.openInventory(myInventory.getInventory());
        return true;
    }
}

class MyInventory implements InventoryHolder {

    private final Inventory inventory;

    public MyInventory(PluginSasha plugin) {
        // Create an Inventory with 9 slots, `this` here is our InventoryHolder.
        this.inventory = plugin.getServer().createInventory(this, 9);
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

}
