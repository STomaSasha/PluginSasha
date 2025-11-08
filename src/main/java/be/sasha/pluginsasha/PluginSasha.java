package be.sasha.pluginsasha;

import be.sasha.pluginsasha.commands.*;
import be.sasha.pluginsasha.listeners.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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

        // --- Crée une instance unique de BagCommand et MenuCommand ---
        BagCommand bagCommand = new BagCommand(this);
        MenuCommand menuCommand = new MenuCommand(this); // ✅ instancié une seule fois

        // --- Enregistrement des commandes ---

        // --- spawn et setspawn ---
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new SpawnCommand(this));
        Objects.requireNonNull(getCommand("setspawn")).setExecutor(new SetSpawnCommand(this));

        // --- bag ---
        Objects.requireNonNull(getCommand("bag")).setExecutor(bagCommand);

        // --- annonce et message ---
        Objects.requireNonNull(getCommand("annonce")).setExecutor(new AnnonceCommand(this));
        Objects.requireNonNull(getCommand("message")).setExecutor(new MessageCommand(this));

        // --- help ---
        Objects.requireNonNull(getCommand("help")).setExecutor(new HelpCommand(this));

        // --- Inventaire ---
        Objects.requireNonNull(getCommand("menu")).setExecutor(menuCommand); // ✅ utilise l'instance unique
        Objects.requireNonNull(getCommand("casino")).setExecutor(new CasinoCommand(this));

        // --- Enregistrement des événements ---
        getServer().getPluginManager().registerEvents(menuCommand, this);
        getServer().getPluginManager().registerEvents(new MonListener(this, bagCommand), this);
        // ✅ MenuCommand s'enregistre lui-même dans son constructeur, donc pas besoin de le refaire ici

        getLogger().info("PluginSasha activé !");
    }

    @Override
    public void onDisable() {

        getLogger().info("PluginSasha désactivé !");
        getServer().getScheduler().cancelTasks(this);
    }

    private final Map<Player, Long> sessionStartTimes = new HashMap<>();

    public void setSessionStart(Player player) {
        sessionStartTimes.put(player, System.currentTimeMillis());
    }

    public long getSessionDuration(Player player) {
        Long start = sessionStartTimes.get(player);
        if (start == null) return 0;
        return (System.currentTimeMillis() - start) / 1000; // en secondes
    }

}

