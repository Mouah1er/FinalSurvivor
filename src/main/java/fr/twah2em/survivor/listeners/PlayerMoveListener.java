package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.event.PlayerEnterCuboidEvent;
import fr.twah2em.survivor.game.Room;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import fr.twah2em.survivor.utils.Cuboid;
import fr.twah2em.survivor.utils.StreamUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class PlayerMoveListener implements SurvivorListener<PlayerMoveEvent> {
    private final Main main;

    public PlayerMoveListener(Main main) {
        this.main = main;
    }

    @Override
    @EventHandler
    public void onEvent(PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.ADVENTURE) {
            if (StreamUtils.playerHasPlayerWrapper(player, main.gameInfos().players())) {
                main.gameInfos().rooms().stream().map(room -> (Supplier<Stream<Cuboid>>) () -> Arrays.stream(room.cuboids())).forEach(supplier -> {
                    final Location from = event.getFrom();
                    final Location to = event.getTo();

                    if (supplier.get().anyMatch(cuboid -> !cuboid.isIn(from))) {
                        if (supplier.get().anyMatch(cuboid -> cuboid.isIn(to))) {
                            final Cuboid fromCuboid = supplier.get().filter(cuboid -> cuboid.isIn(from)).findFirst().orElse(null);
                            final Cuboid toCuboid = supplier.get().filter(cuboid -> cuboid.isIn(to)).findFirst().orElse(null);

                            if (fromCuboid != null && toCuboid != null) {
                                Bukkit.getPluginManager().callEvent(new PlayerEnterCuboidEvent(player, fromCuboid, toCuboid));
                            }
                        }
                    }
                });
            }
        }
    }
}
