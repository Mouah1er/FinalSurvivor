package fr.twah2em.survivor.commands.internal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public abstract class SurvivorCommand extends Command {
    private final SurvivorCommandMetadata survivorCommandMetadata;

    public SurvivorCommand(SurvivorCommandMetadata survivorCommandMetadata) {
        super(survivorCommandMetadata.name(), survivorCommandMetadata.description(), survivorCommandMetadata.usage(),
                survivorCommandMetadata.aliases() == null ? Collections.emptyList() : survivorCommandMetadata.aliases());

        this.survivorCommandMetadata = survivorCommandMetadata;

        final boolean hasPermission = survivorCommandMetadata.permission() == null ||
                survivorCommandMetadata.permission().equals("") ||
                survivorCommandMetadata.permission().isEmpty();
        this.setPermission(hasPermission ? null : survivorCommandMetadata.permission());
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
        final SurvivorSubCommand subCommand = survivorCommandMetadata.subCommands().get(index);

        if (subCommand != null) {
            final boolean shouldBeExecuted = subCommand.shouldBeExecuted(commandSender, args).call();

            if (shouldBeExecuted) subCommand.execute(commandSender, args);
        }
    }
}
