package fr.twah2em.survivor.commands.room.subcommands;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.commands.internal.SurvivorCommandCallback;
import fr.twah2em.survivor.commands.internal.SurvivorSubCommand;
import fr.twah2em.survivor.game.RoomsManager;
import fr.twah2em.survivor.inventories.room.RoomCreateSurvivorInventory;
import fr.twah2em.survivor.utils.Messages;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RoomCreateSubCommand extends SurvivorSubCommand {
    private final Main main;

    public RoomCreateSubCommand(Main main) {
        super("create");

        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ((Player) sender).openInventory(new RoomCreateSurvivorInventory(main).survivorInventory().getInventory());
    }

    @Override
    public SurvivorCommandCallback shouldBeExecuted(CommandSender sender, String[] args) {
        if (RoomsManager.CREATING_ROOM != null) return () -> {
            sender.sendMessage(Messages.CANNOT_CREATE_ROOM.replaceText(
                    TextReplacementConfig.builder()
                            .matchLiteral("%player%")
                            .replacement(RoomsManager.CREATING_ROOM.left().getName())
                            .build())
            );

            return false;
        };

        return SurvivorCommandCallback.emptyTrueCallback();
    }
}
