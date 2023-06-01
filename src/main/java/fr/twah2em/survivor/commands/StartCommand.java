package fr.twah2em.survivor.commands;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.commands.internal.SurvivorCommand;
import fr.twah2em.survivor.commands.internal.SurvivorCommandCallback;
import fr.twah2em.survivor.commands.internal.SurvivorCommandMetadata;
import fr.twah2em.survivor.game.GameInfos;
import fr.twah2em.survivor.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.Component.*;

import java.util.concurrent.CompletableFuture;

public class StartCommand extends SurvivorCommand {
    private final Main main;

    public StartCommand(Main main) {
        super(new SurvivorCommandMetadata.SurvivorCommandMetadataBuilder("start")
                .withDescription("Force le lancement du jeu")
                .withUsage("start")
                .withPermission("survivor.start")
                .build());

        this.main = main;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        final Player player = (Player) commandSender;

        player.sendMessage(Messages.PREFIX
                .append(text("Êtes-vous sûr? Tapez ", YELLOW))
                .append(text("Oui ", GREEN))
                .append(text("ou ", YELLOW))
                .append(text("Non ", RED))
                .append(text("dans le chat.", YELLOW)));

        final CompletableFuture<Boolean> startCommandConfirmation = new CompletableFuture<>();

        main.gameInfos().startCommandConfirmation().put(player.getUniqueId(), startCommandConfirmation);

        startCommandConfirmation.thenAccept(acceptation -> {
            if (acceptation) {
                main.gameLogic().start();
            } else {
                player.sendMessage(Messages.PREFIX
                        .append(text("Vous avez bien annulé le lancement de la partie.", RED)));
            }
        });
    }

    @Override
    public SurvivorCommandCallback shouldBeExecuted(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) return SurvivorCommandCallback.notAPlayerCallback(commandSender);

        if (!commandSender.hasPermission("survivor.start")) return SurvivorCommandCallback.permissionCallback(commandSender);

        if (main.gameInfos().state() != GameInfos.GameState.WAITING) return () -> {
            commandSender.sendMessage(Messages.PREFIX
                    .append(text("La partie est déjà en cours.", RED)));
            return false;
        };

        return SurvivorCommandCallback.emptyTrueCallback();
    }
}
