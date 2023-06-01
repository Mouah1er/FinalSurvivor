package fr.twah2em.survivor.commands.internal;

import org.bukkit.command.CommandSender;

public abstract class SurvivorSubCommand {
    private final String name;

    public SurvivorSubCommand(String name) {
        this.name = name;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public abstract SurvivorCommandCallback shouldBeExecuted(CommandSender sender, String[] args);

    public String getName() {
        return name;
    }
}
