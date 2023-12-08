package fr.twah2em.survivor.commands.room.subcommands;

import fr.twah2em.survivor.commands.internal.SurvivorCommandCallback;
import fr.twah2em.survivor.commands.internal.SurvivorSubCommand;
import org.bukkit.command.CommandSender;

public class RoomDisplaySubCommand extends SurvivorSubCommand {

    public RoomDisplaySubCommand() {
        super("display");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

    }

    @Override
    public SurvivorCommandCallback shouldBeExecuted(CommandSender sender, String[] args) {
        return SurvivorCommandCallback.emptyTrueCallback();
    }
}
