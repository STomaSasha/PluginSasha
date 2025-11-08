package be.sasha.pluginsasha.commands;

import be.sasha.pluginsasha.grades.GradeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetGradeCommand implements CommandExecutor {
    private final GradeManager gradeManager;
    private final String prefix = "§5[GradeManager] §r";

    public SetGradeCommand(GradeManager gradeManager) {
        this.gradeManager = gradeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1 && sender instanceof Player player) {
            // /setgrade <grade> → pour soi-même
            String grade = args[0];
            gradeManager.setGrade(player, grade);
            player.sendMessage(prefix + "§aTon grade a été défini sur §e" + grade);
            return true;
        }

        if (args.length == 2) {
            // /setgrade <joueur> <grade> → pour un autre joueur
            Player target = Bukkit.getPlayer(args[0]);
            String grade = args[1];

            if (target == null) {
                sender.sendMessage("§cJoueur introuvable.");
                return true;
            }

            gradeManager.setGrade(target, grade);
            sender.sendMessage(prefix + "§aGrade de " + target.getName() + " défini sur §e" + grade);
            return true;
        }

        sender.sendMessage("§cUtilisation: /setgrade <joueur> <grade> ou /setgrade <grade>");
        return true;
    }
}
