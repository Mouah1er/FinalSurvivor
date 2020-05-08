package fr.bibouh.survivor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SurvivorStop implements CommandExecutor {

    private Main main;

    public SurvivorStop(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        Player player = (Player) sender;

        main.setState(GState.FINISH);
        player.sendMessage("§4Vous avez bien arrêté la partie.");
        Bukkit.broadcastMessage("§4Le jeu a été arrêté par un modérateur.");

        return false;
    }
}
