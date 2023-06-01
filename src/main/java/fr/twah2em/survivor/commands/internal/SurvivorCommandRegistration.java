package fr.twah2em.survivor.commands.internal;

import fr.twah2em.survivor.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.util.function.Function;

public class SurvivorCommandRegistration {
    @SafeVarargs
    public static void registerCommands(Main main, Function<Main, ? extends SurvivorCommand>... commands) {
        final CommandMap bukkitCommandMap = Bukkit.getCommandMap();

        for (Function<Main, ? extends SurvivorCommand> command : commands) {
            final SurvivorCommand uhcCommand = command.apply(main);

            bukkitCommandMap.register(uhcCommand.getName(), uhcCommand);
        }
    }
}
