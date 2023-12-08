package fr.twah2em.survivor.commands.internal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public abstract class SurvivorCommand extends Command {
    private final SurvivorCommandMetadata commandMetadata;

    public SurvivorCommand(SurvivorCommandMetadata commandMetadata) {
        super(commandMetadata.name(), commandMetadata.description(), commandMetadata.usage(),
                commandMetadata.aliases() == null ? Collections.emptyList() : commandMetadata.aliases());

        this.commandMetadata = commandMetadata;

        final boolean hasPermission = commandMetadata.permission() == null ||
                commandMetadata.permission().isEmpty();
        this.setPermission(hasPermission ? null : commandMetadata.permission());
    }

    public abstract void execute(CommandSender commandSender, String[] args);

    public abstract SurvivorCommandCallback shouldBeExecuted(CommandSender commandSender, String[] args);

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        final boolean shouldBeExecuted = this.shouldBeExecuted(sender, args).call();

        if (shouldBeExecuted) execute(sender, args);

        return shouldBeExecuted;
    }

    protected void executeSubCommand(int index, CommandSender commandSender, String[] args) {
        final SurvivorSubCommand subCommand = commandMetadata.subCommands().get(index);

        if (subCommand != null) {
            final boolean shouldBeExecuted = subCommand.shouldBeExecuted(commandSender, args).call();

            if (shouldBeExecuted) subCommand.execute(commandSender, args);
        }
    }

    protected SurvivorCommandMetadata commandMetadata() {
        return commandMetadata;
    }
}
