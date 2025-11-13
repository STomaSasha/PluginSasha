package be.sasha.pluginsasha;

import be.sasha.pluginsasha.commands.*;
import be.sasha.pluginsasha.grades.*;
import be.sasha.pluginsasha.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
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
        Objects.requireNonNull(getCommand("fly")).setExecutor(new FlyCommand(this));
        Objects.requireNonNull(getCommand("world")).setExecutor(new WorldCommand(this));

        // --- Enregistrement des listeners ---
        getServer().getPluginManager().registerEvents(menuCommand, this);
        getServer().getPluginManager().registerEvents(new MonListener(this, bagCommand), this);

        // ✅ AJOUT IMPORTANT : enregistrement du listener de chat
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        loadExistingWorlds();

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

    private void safeRegister(String cmd, CommandExecutor executor) {
        if (getCommand(cmd) != null) {
            getCommand(cmd).setExecutor(executor);
        } else {
            getLogger().warning("⚠️ Commande introuvable dans plugin.yml : " + cmd);
        }
    }

    private void loadExistingWorlds() {
        File folder = getServer().getWorldContainer();

        if (!folder.exists()) return;

        File[] worldFolders = folder.listFiles(File::isDirectory);
        if (worldFolders == null) return;

        for (File worldFolder : worldFolders) {
            String name = worldFolder.getName();

            // Ignore les mondes par défaut
            if (name.equalsIgnoreCase("world") ||
                    name.equalsIgnoreCase("world_nether") ||
                    name.equalsIgnoreCase("world_the_end")) {
                continue;
            }

            // Vérifie que c’est bien un dossier de monde (doit contenir level.dat)
            File level = new File(worldFolder, "level.dat");
            if (!level.exists()) continue;

            // Si le monde n’est pas déjà chargé, on le charge
            if (Bukkit.getWorld(name) == null) {
                getLogger().info("Chargement du monde existant : " + name);
                new org.bukkit.WorldCreator(name).createWorld();
            }
        }

        getLogger().info("Tous les mondes existants ont été chargés !");
    }

}
