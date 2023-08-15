package fr.twah2em.survivor.event;

import fr.twah2em.survivor.utils.Cuboid;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerEnterCuboidEvent extends PlayerEvent {
    private final Cuboid from;
    private final Cuboid to;

    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerEnterCuboidEvent(@NotNull Player who, Cuboid from, Cuboid to) {
        super(who);

        this.from = from;
        this.to = to;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public Cuboid from() {
        return from;
    }

    public Cuboid to() {
        return to;
    }
}
