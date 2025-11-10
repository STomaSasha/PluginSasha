package be.sasha.pluginsasha;

import be.sasha.pluginsasha.commands.*;
import be.sasha.pluginsasha.grades.GradeManager;
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
    private final Map<String, String> commandDescriptions = new HashMap<>();
    private GradeManager gradeManager;

    private final Map<Player, Long> sessionStartTimes = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        BagCommand bagCommand = new BagCommand(this);
        MenuCommand menuCommand = new MenuCommand(this);
        gradeManager = new GradeManager(this);

        // --- Enregistrement des commandes ---
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new SpawnCommand(this));
        Objects.requireNonNull(getCommand("setspawn")).setExecutor(new SetSpawnCommand(this));
        Objects.requireNonNull(getCommand("bag")).setExecutor(bagCommand);
        Objects.requireNonNull(getCommand("annonce")).setExecutor(new AnnonceCommand(this));
        Objects.requireNonNull(getCommand("message")).setExecutor(new MessageCommand(this));
        Objects.requireNonNull(getCommand("help")).setExecutor(new HelpCommand(this));
        Objects.requireNonNull(getCommand("menu")).setExecutor(menuCommand);
        Objects.requireNonNull(getCommand("casino")).setExecutor(new CasinoCommand(this));
        Objects.requireNonNull(getCommand("setgrade")).setExecutor(new SetGradeCommand(gradeManager));

        // --- Enregistrement des listeners ---
        getServer().getPluginManager().registerEvents(menuCommand, this);
        getServer().getPluginManager().registerEvents(new MonListener(this, bagCommand), this);

        // ✅ AJOUT IMPORTANT : enregistrement du listener de chat
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        getLogger().info("PluginSasha activé !");
    }

    @Override
    public void onDisable() {
        getLogger().info("PluginSasha désactivé !");
        getServer().getScheduler().cancelTasks(this);
        gradeManager.savePlayerGrades();
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Map<String, String> getCommandDescriptions() {
        return commandDescriptions;
    }

    public void setSessionStart(Player player) {
        sessionStartTimes.put(player, System.currentTimeMillis());
    }

    public long getSessionDuration(Player player) {
        Long start = sessionStartTimes.get(player);
        if (start == null) return 0;
        return (System.currentTimeMillis() - start) / 1000;
    }

    public GradeManager getGradeManager() {
        return gradeManager;
    }

}
