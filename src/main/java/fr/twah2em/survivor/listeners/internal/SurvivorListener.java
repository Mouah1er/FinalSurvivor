package fr.twah2em.survivor.listeners.internal;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public interface SurvivorListener<E extends Event> extends Listener {
    @EventHandler
    void onEvent(E event);
}
