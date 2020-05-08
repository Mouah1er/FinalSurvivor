package fr.bibouh.survivor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class helpSurvivor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        sender.sendMessage("§6§l§m----§2Survivor§6§l§m----");
        sender.sendMessage("§3-/packapunch; §6Permet de pack à puncher l'arme en main en cas de bug.");
        sender.sendMessage("§3-/giveweapon <arme> <joueur>; §6Permet de se donner l'arme donné au joueur sélectionné en cas de bug. ");
        sender.sendMessage("§3-/skipwave <vague>; §6Sans argument elle permet de passer à la prochaine manche. Avec l'argument, elle permet de passer à la manche donnée.");
        sender.sendMessage("§3- /giveperk <atout> <joueur>; §6Permet de donner l'atout donné au joueur sélectionné ou à sois même en cas de bug. ");
        sender.sendMessage("§6§l§m----§2Survivor§6§l§m----");
        return false;
    }
}
