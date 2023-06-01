package fr.twah2em.survivor.listeners.internal;

import fr.twah2em.survivor.Main;
import org.bukkit.Bukkit;

import java.util.function.Function;

public class SurvivorListenerRegistration {
    @SafeVarargs
    public static void registerListeners(Main main, Function<Main, ? extends SurvivorListener<?>>... listeners) {
        for (Function<Main, ? extends SurvivorListener<?>> listener : listeners) {
            final SurvivorListener<?> uhcListener = listener.apply(main);

            Bukkit.getPluginManager().registerEvents(uhcListener, main);
        }
    }
}
