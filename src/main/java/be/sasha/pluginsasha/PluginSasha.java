package be.sasha.pluginsasha;

import be.sasha.pluginsasha.commands.*;
import be.sasha.pluginsasha.listeners.*;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class PluginSasha extends JavaPlugin implements Listener {

    private Location spawnLocation;

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    private final Map<String, String> commandDescriptions = new HashMap<>();

    public Map<String, String> getCommandDescriptions() {
        return commandDescriptions;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // --- Crée une instance unique de BagCommand ---
        BagCommand bagCommand = new BagCommand(this);

        // --- Enregistrement des commandes ---
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new SpawnCommand(this));
        Objects.requireNonNull(getCommand("setspawn")).setExecutor(new SetSpawnCommand(this));
        Objects.requireNonNull(getCommand("bag")).setExecutor(bagCommand);
        Objects.requireNonNull(getCommand("annonce")).setExecutor(new AnnonceCommand(this));
        Objects.requireNonNull(getCommand("message")).setExecutor(new MessageCommand(this));
        Objects.requireNonNull(getCommand("help")).setExecutor(new HelpCommand(this));
        Objects.requireNonNull(getCommand("menu")).setExecutor(new MenuCommand(this));

        // --- Enregistrement des événements ---
        getServer().getPluginManager().registerEvents(new MonListener(this, bagCommand), this);

        getLogger().info("PluginSasha activé !");
    }

    @Override
    public void onDisable() {
        getLogger().info("PluginSasha désactivé !");
    }
}
