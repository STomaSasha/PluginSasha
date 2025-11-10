package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.grades.GradeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class SetGradeCommand implements CommandExecutor {

    private final GradeManager gradeManager;
    private final String prefix = "§5[GradeManager] §r";

    public SetGradeCommand(GradeManager gradeManager) {
        this.gradeManager = gradeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // --- /setgrade <grade> (pour soi-même)
        if (args.length == 1 && sender instanceof Player player) {
            String input = args[0];

            // Vérification du grade (ignore-case)
            Optional<String> match = findMatchingGrade(input);
            if (match.isEmpty()) {
                player.sendMessage(prefix + "§cLe grade '" + input + "' n'existe pas !");
                return true;
            }

            String realGrade = match.get();
            gradeManager.setGrade(player, realGrade);
            player.sendMessage(prefix + "§aTon grade a été défini sur §e" + realGrade);
            return true;
        }

        // --- /setgrade <joueur> <grade>
        if (args.length == 2) {
            String playerName = args[0];
            String inputGrade = args[1];

            Player target = Bukkit.getPlayer(playerName);
            if (target == null) {
                sender.sendMessage(prefix + "§cLe joueur '" + playerName + "' n'est pas connecté.");
                return true;
            }

            // Vérification du grade (ignore-case)
            Optional<String> match = findMatchingGrade(inputGrade);
            if (match.isEmpty()) {
                sender.sendMessage(prefix + "§cLe grade '" + inputGrade + "' n'existe pas !");
                return true;
            }

            String realGrade = match.get();
            gradeManager.setGrade(target, realGrade);

            sender.sendMessage(prefix + "§aLe grade de §e" + target.getName() + " §aa été défini sur §e" + realGrade);
            return true;
        }

        // Mauvaise utilisation
        sender.sendMessage("§cUtilisation: /setgrade <grade> ou /setgrade <joueur> <grade>");
        return true;
    }

    /**
     * Tente de trouver le grade correspondant en ignorant les majuscules/minuscules.
     * Retourne un Optional<String> avec le vrai nom du grade si trouvé.
     */
    private Optional<String> findMatchingGrade(String input) {
        return gradeManager.getGradeDefinitions().keySet()
                .stream()
                .filter(g -> g.equalsIgnoreCase(input))
                .findFirst();
    }
}
