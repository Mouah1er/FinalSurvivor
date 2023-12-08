package fr.twah2em.survivor.commands.internal;

import fr.twah2em.survivor.utils.Messages;
import org.bukkit.command.CommandSender;

public interface SurvivorCommandCallback {
    boolean call();

    static SurvivorCommandCallback emptyFalseCallback() {
        return () -> false;
    }

    static SurvivorCommandCallback emptyTrueCallback() {
        return () -> true;
    }

    static SurvivorCommandCallback notAPlayerCallback(CommandSender sender) {
        return () -> {
            sender.sendMessage(Messages.NOT_A_PLAYER_ERROR);

            return false;
        };
    }

    static SurvivorCommandCallback permissionCallback(CommandSender sender) {
        return () -> {
            sender.sendMessage(Messages.PERMISSION_ERROR);

            return false;
        };
    }

    static SurvivorCommandCallback argsCallback(CommandSender sender) {
        return () -> {
            sender.sendMessage(Messages.ARGS_ERROR);

            return false;
        };
    }
}
