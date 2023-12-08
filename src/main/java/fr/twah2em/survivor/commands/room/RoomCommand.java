package fr.twah2em.survivor.commands.room;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.commands.internal.SurvivorCommand;
import fr.twah2em.survivor.commands.internal.SurvivorCommandCallback;
import fr.twah2em.survivor.commands.internal.SurvivorCommandMetadata;
import fr.twah2em.survivor.commands.room.subcommands.RoomCreateSubCommand;
import fr.twah2em.survivor.commands.room.subcommands.RoomDisplaySubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RoomCommand extends SurvivorCommand {

    public RoomCommand(Main main) {
        super(new SurvivorCommandMetadata.SurvivorCommandMetadataBuilder("room")
                .withDescription("Commandes relatives aux pièces de la map")
                .withUsage("room")
                .withPermission("survivor.rooms")
                .withSubCommands(new RoomDisplaySubCommand(), new RoomCreateSubCommand(main))
                .build());
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args[0].equalsIgnoreCase("display")) {
            executeSubCommand(0, commandSender, args);
        } else {
            executeSubCommand(1, commandSender, args);
        }
    }

    @Override
    public SurvivorCommandCallback shouldBeExecuted(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) return SurvivorCommandCallback.notAPlayerCallback(commandSender);

        if (!commandSender.hasPermission(commandMetadata().permission())) return SurvivorCommandCallback.permissionCallback(commandSender);

        if (args.length != 1) return SurvivorCommandCallback.argsCallback(commandSender);

        if (!args[0].equalsIgnoreCase("display") && !args[0].equalsIgnoreCase("create")) return SurvivorCommandCallback.argsCallback(commandSender);

        return SurvivorCommandCallback.emptyTrueCallback();
    }
}
