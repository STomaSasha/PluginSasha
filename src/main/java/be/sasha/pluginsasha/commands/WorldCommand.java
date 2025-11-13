package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.PluginSasha;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class WorldCommand implements CommandExecutor {

    private final PluginSasha plugin;

    public WorldCommand(PluginSasha plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            sender.sendMessage("§cUtilisation : /world <create|list|tp|delete|load> [arguments]");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "create" -> handleCreate(sender, args);
            case "list" -> handleList(sender);
            case "tp" -> handleTeleport(sender, args);
            case "delete" -> handleDelete(sender, args);
            case "load" -> handleLoad(sender, args);
            default -> sender.sendMessage("§cSous-commande inconnue. Utilisez : create, list, tp, delete, load.");
        }

        return true;
    }

    // ===============================
    //        /world create
    // ===============================
    private void handleCreate(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cSeuls les joueurs peuvent exécuter cette commande.");
            return;
        }

        if (args.length < 2) {
            player.sendMessage("§cUtilisation : /world create <nom> [flat|normal]");
            return;
        }

        String worldName = args[1];
        String type = args.length >= 3 ? args[2].toLowerCase() : "normal";

        if (Bukkit.getWorld(worldName) != null) {
            player.sendMessage("§eLe monde §6" + worldName + " §eexiste déjà !");
            return;
        }

        WorldCreator creator = new WorldCreator(worldName);
        if (type.equals("flat")) creator.type(WorldType.FLAT);
        else creator.type(WorldType.NORMAL);

        player.sendMessage("§aCréation du monde §6" + worldName + "§a (" + type + ")...");

        World world = creator.createWorld();
        if (world == null) {
            player.sendMessage("§cErreur lors de la création du monde !");
            return;
        }

        player.teleport(world.getSpawnLocation());
        player.sendMessage("§aMonde §6" + worldName + " §acréé et chargé !");
    }

    // ===============================
    //        /world list
    // ===============================
    private void handleList(CommandSender sender) {
        sender.sendMessage("§e--- §6Liste des mondes chargés §e---");
        for (World world : Bukkit.getWorlds()) {
            String envName = world.getEnvironment() != null ? world.getEnvironment().name() : "UNKNOWN";
            int playerCount = world.getPlayers() != null ? world.getPlayers().size() : 0;
            sender.sendMessage("§7- §a" + world.getName() + " §7(env: §f" + envName + "§7, joueurs: §f" + playerCount + "§7)");
        }
    }

    // ===============================
    //        /world tp
    // ===============================
    private void handleTeleport(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUtilisation : /world tp <monde> [joueur]");
            return;
        }

        String worldName = args[1];
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            sender.sendMessage("§cLe monde §6" + worldName + " §cn’existe pas ou n’est pas chargé !");
            return;
        }

        if (args.length == 2) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cSeuls les joueurs peuvent se téléporter.");
                return;
            }
            player.teleport(world.getSpawnLocation());
            player.sendMessage("§aTéléporté dans §6" + worldName + "§a !");
        } else {
            Player target = Bukkit.getPlayerExact(args[2]);
            if (target == null) {
                sender.sendMessage("§cLe joueur §6" + args[2] + " §cn’est pas en ligne !");
                return;
            }
            target.teleport(world.getSpawnLocation());
            target.sendMessage("§aVous avez été téléporté dans le monde §6" + worldName + "§a !");
            sender.sendMessage("§a" + target.getName() + " §ea été téléporté dans §6" + worldName + "§e !");
        }
    }

    // ===============================
    //        /world delete
    // ===============================
    private void handleDelete(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUtilisation : /world delete <nom>");
            return;
        }

        String worldName = args[1];
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            sender.sendMessage("§cLe monde §6" + worldName + " §cn’existe pas ou n’est pas chargé !");
            return;
        }

        // Décharge le monde
        Bukkit.unloadWorld(world, false);
        sender.sendMessage("§eMonde §6" + worldName + " §edéchargé avec succès.");

        // Supprime les fichiers du dossier du monde
        File folder = world.getWorldFolder();
        deleteFolder(folder);
        sender.sendMessage("§cLe monde §6" + worldName + " §ca été supprimé définitivement !");
    }

    // ===============================
    //        /world load
    // ===============================
    private void handleLoad(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cSeuls les joueurs peuvent exécuter cette commande !");
            return;
        }

        if (args.length < 2) {
            player.sendMessage("§cUtilisation : /world load <nomDuMonde>");
            return;
        }

        String worldName = args[1];

        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            player.sendMessage("§eLe monde §6" + worldName + " §eest déjà chargé !");
            player.teleport(world.getSpawnLocation());
            return;
        }

        try {
            WorldCreator creator = new WorldCreator(worldName);
            world = creator.createWorld();

            if (world == null) {
                player.sendMessage("§cLe monde n'a pas pu être chargé. Vérifie le nom et les fichiers !");
                return;
            }

            player.sendMessage("§aLe monde §6" + worldName + " §aa été chargé avec succès !");
            player.teleport(world.getSpawnLocation());

        } catch (Exception e) {
            plugin.getLogger().severe("Erreur lors du chargement du monde " + worldName);
            e.printStackTrace();
        }
    }

    // ===============================
    //        Supprime un dossier récursivement
    // ===============================
    private void deleteFolder(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) deleteFolder(file);
                    else file.delete();
                }
            }
            folder.delete();
        }
    }
}
