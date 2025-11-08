package be.sasha.pluginsasha.grades;

import be.sasha.pluginsasha.PluginSasha;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GradeManager {
    private final PluginSasha plugin;
    private final Map<UUID, String> playerGrades = new HashMap<>();
    private final Map<String, Grade> gradeDefinitions = new HashMap<>();

    public GradeManager(PluginSasha plugin) {
        this.plugin = plugin;
        loadGrades();
        loadPlayerGrades();
    }

    public void loadGrades() {
        File file = new File(plugin.getDataFolder(), "grades.yml");
        if (!file.exists()) plugin.saveResource("grades.yml", false);

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("grades");
        if (section == null) return;

        for (String gradeName : section.getKeys(false)) {
            String prefix = section.getString(gradeName + ".prefix", "");
            List<String> perms = section.getStringList(gradeName + ".permissions");
            gradeDefinitions.put(gradeName, new Grade(gradeName, prefix, perms));
        }
    }

    public void setGrade(Player player, String gradeName) {
        if (!gradeDefinitions.containsKey(gradeName)) return;
        playerGrades.put(player.getUniqueId(), gradeName);
        applyPermissions(player);
    }

    public String getGrade(Player player) {
        return playerGrades.getOrDefault(player.getUniqueId(), "Joueur");
    }

    public Grade getGradeDefinition(String gradeName) {
        return gradeDefinitions.getOrDefault(gradeName, gradeDefinitions.get("Joueur"));
    }

    public String getPrefix(Player player) {
        return getGradeDefinition(getGrade(player)).prefix();
    }

    public void applyPermissions(Player player) {
        Grade grade = getGradeDefinition(getGrade(player));
        for(String perm : grade.permissions()) {
            player.addAttachment(plugin, perm, true);
        }
    }

    public void savePlayerGrades() {
        File file = new File(plugin.getDataFolder(), "playergrades.yml");
        FileConfiguration config = new YamlConfiguration();
        for(UUID uuid : playerGrades.keySet()) {
            config.set("players."+uuid.toString(), playerGrades.get(uuid));
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPlayerGrades() {
        File file = new File(plugin.getDataFolder(), "playergrades.yml");
        if(!file.exists()) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("playergrades");
        if (section == null) return;

        for (String uuidStr : section.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            String grade = section.getString(uuidStr);
            playerGrades.put(uuid, grade);
        }
    }

    public boolean hasGrade(Player player, String grade) {
        return grade.equals(getGrade(player));
    }
}
