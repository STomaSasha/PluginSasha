package be.sasha.pluginsasha.grades;

import be.sasha.pluginsasha.PluginSasha;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GradeManager {
    private static final String DEFAULT_GRADE = "Joueur";

    private final PluginSasha plugin;
    private final Map<UUID, String> playerGrades = new HashMap<>();
    private final Map<String, Grade> gradeDefinitions = new HashMap<>();
    private final Map<UUID, PermissionAttachment> attachments = new HashMap<>();

    public GradeManager(PluginSasha plugin) {
        this.plugin = plugin;
        loadGrades();
        loadPlayerGrades();
    }

    /**
     * Charge les grades depuis grades.yml
     */
    public void loadGrades() {
        File file = new File(plugin.getDataFolder(), "grades.yml");
        if (!file.exists()) plugin.saveResource("grades.yml", false);

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("grades");

        if (section == null) {
            plugin.getLogger().warning("Aucune section 'grades' trouvée dans grades.yml !");
            return;
        }

        for (String gradeName : section.getKeys(false)) {
            String prefix = section.getString(gradeName + ".prefix", "");
            List<String> perms = section.getStringList(gradeName + ".permissions");
            gradeDefinitions.put(gradeName, new Grade(gradeName, prefix, perms));
        }

        plugin.getLogger().info("Grades chargés : " + gradeDefinitions.keySet());
    }

    /**
     * Définit le grade d’un joueur et applique ses permissions
     */
    public void setGrade(Player player, String gradeName) {
        if (!gradeDefinitions.containsKey(gradeName)) {
            player.sendMessage("§cLe grade '" + gradeName + "' n'existe pas !");
            return;
        }

        playerGrades.put(player.getUniqueId(), gradeName);
        applyPermissions(player);
    }

    /**
     * Retourne le grade actuel du joueur
     */
    public String getGrade(Player player) {
        return playerGrades.getOrDefault(player.getUniqueId(), DEFAULT_GRADE);
    }

    /**
     * Retourne la définition du grade (nom, prefix, permissions)
     */
    public Grade getGradeDefinition(String gradeName) {
        return gradeDefinitions.getOrDefault(gradeName, gradeDefinitions.get(DEFAULT_GRADE));
    }

    /**
     * Retourne le préfixe du grade du joueur
     */
    public String getPrefix(Player player) {
        return getGradeDefinition(getGrade(player)).prefix();
    }

    /**
     * Applique les permissions correspondant au grade du joueur
     */
    public void applyPermissions(Player player) {
        // Si le joueur a déjà un attachment, on le garde (les perms ne changent pas à chaque connexion)
        if (attachments.containsKey(player.getUniqueId())) return;

        Grade grade = getGradeDefinition(getGrade(player));
        PermissionAttachment attachment = player.addAttachment(plugin);

        for (String perm : grade.permissions()) {
            attachment.setPermission(perm, true);
        }

        attachments.put(player.getUniqueId(), attachment);
    }

    /**
     * Sauvegarde tous les grades des joueurs dans playergrades.yml
     */
    public void savePlayerGrades() {
        File file = new File(plugin.getDataFolder(), "playergrades.yml");
        FileConfiguration config = new YamlConfiguration();

        for (UUID uuid : playerGrades.keySet()) {
            config.set("playergrades." + uuid, playerGrades.get(uuid));
        }

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Erreur lors de la sauvegarde de playergrades.yml !");
            e.printStackTrace();
        }
    }

    /**
     * Charge les grades des joueurs depuis playergrades.yml
     */
    public void loadPlayerGrades() {
        File file = new File(plugin.getDataFolder(), "playergrades.yml");
        if (!file.exists()) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("playergrades");
        if (section == null) return;

        for (String uuidStr : section.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            String grade = config.getString("playergrades." + uuidStr, DEFAULT_GRADE);
            playerGrades.put(uuid, grade);
        }

        plugin.getLogger().info("Grades des joueurs chargés (" + playerGrades.size() + ")");
    }

    /**
     * Vérifie si un joueur a un grade spécifique
     */
    public boolean hasGrade(Player player, String grade) {
        return grade.equalsIgnoreCase(getGrade(player));
    }
}
