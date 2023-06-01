package fr.twah2em.survivor.commands.internal;

import fr.twah2em.survivor.utils.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            sender.sendMessage(Messages.PREFIX
                    .append(Component.text("Vous devez être un jour pour utiliser cette commande !", NamedTextColor.RED)));

            return false;
        };
    }

    static SurvivorCommandCallback permissionCallback(CommandSender sender) {
        return () -> {
            sender.sendMessage(Messages.PREFIX
                    .append(Component.text("Vous n'avez pas la permission pour faire cela !", NamedTextColor.RED)));

            return false;
        };
    }
}
