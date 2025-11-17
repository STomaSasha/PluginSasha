package be.sasha.pluginsasha.login;

import be.sasha.pluginsasha.PluginSasha;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginManager {

    private final PluginSasha plugin;
    private File passwordFile;
    private FileConfiguration passwordConfig;
    private final Map<String, Boolean> authenticatedPlayers = new HashMap<>();

    public LoginManager(PluginSasha plugin) {
        this.plugin = plugin;

        // Crée le fichier password.yml si nécessaire
        passwordFile = new File(plugin.getDataFolder(), "password.yml");
        if (!passwordFile.exists()) {
            try {
                if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
                if (passwordFile.createNewFile()) {
                    plugin.getLogger().info("password.yml créé dans " + plugin.getDataFolder());
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Impossible de créer password.yml !");
                e.printStackTrace();
            }
        }

        // Charge la configuration
        passwordConfig = YamlConfiguration.loadConfiguration(passwordFile);
    }

    public void register(String playerName, String password) {
        passwordConfig.set(playerName, password);
        save();
    }

    public boolean isRegistered(String playerName) {
        return passwordConfig.contains(playerName);
    }

    public boolean checkPassword(String playerName, String password) {
        return passwordConfig.contains(playerName) && passwordConfig.getString(playerName).equals(password);
    }

    public void setAuthenticated(Player player, boolean authenticated) {
        authenticatedPlayers.put(player.getName(), authenticated);
    }

    public boolean isAuthenticated(Player player) {
        return authenticatedPlayers.getOrDefault(player.getName(), false);
    }

    public void save() {
        try {
            passwordConfig.save(passwordFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Impossible de sauvegarder password.yml !");
            e.printStackTrace();
        }
    }
}
