package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.event.PlayerEnterCuboidEvent;
import fr.twah2em.survivor.game.GameInfos;
import fr.twah2em.survivor.game.rooms.RoomsManager;
import fr.twah2em.survivor.game.player.SurvivorPlayer;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import fr.twah2em.survivor.utils.Cuboid;
import fr.twah2em.survivor.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements SurvivorListener<PlayerMoveEvent> {
    private final Main main;

    public PlayerMoveListener(Main main) {
        this.main = main;
    }

    @Override
    @EventHandler
    public void onEvent(PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        final Location from = event.getFrom();
        final Location to = event.getTo();

        if (player.getGameMode() != GameMode.SPECTATOR) {
            final GameInfos gameInfos = main.gameInfos();
            if (SurvivorPlayer.playerIsSurvivorPlayer(player, gameInfos.players())) {
                final Cuboid cuboidFrom = Cuboid.fromLocation(gameInfos, from);
                final Cuboid cuboidTo = Cuboid.fromLocation(gameInfos, to);

                if (cuboidFrom == null) {
                    if (player.getGameMode() == GameMode.ADVENTURE) {
                        player.sendMessage(Messages.ERROR_OCCURRED);
                        player.teleport(gameInfos.rooms().get(0).center());
                    }

                    return;
                }

                if (cuboidTo == null) {
                    if (player.getGameMode() == GameMode.ADVENTURE) {
                        player.sendMessage(Messages.AREA_NOT_AUTHORIZED_MESSAGE);
                        player.teleport(RoomsManager.fromCuboid(gameInfos, cuboidFrom).center());
                    }

                    return;
                }

                if (cuboidTo.compare(cuboidFrom)) return;

                Bukkit.getPluginManager().callEvent(new PlayerEnterCuboidEvent(player, cuboidFrom, cuboidTo));
            }
        }
    }
}
