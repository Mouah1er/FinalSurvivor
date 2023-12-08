package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.event.PlayerEnterCuboidEvent;
import fr.twah2em.survivor.game.GameInfos;
import fr.twah2em.survivor.game.Room;
import fr.twah2em.survivor.game.RoomsManager;
import fr.twah2em.survivor.game.player.SurvivorPlayer;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import fr.twah2em.survivor.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class PlayerEnterCuboidListener implements SurvivorListener<PlayerEnterCuboidEvent> {
    private final Main main;

    public PlayerEnterCuboidListener(Main main) {
        this.main = main;
    }

    @EventHandler
    @Override
    public void onEvent(PlayerEnterCuboidEvent event) {
        final GameInfos gameInfos = main.gameInfos();
        final Player player = event.getPlayer();

        final SurvivorPlayer survivorPlayer = SurvivorPlayer.survivorPlayer(player, main.gameInfos().players());
        final Room roomFrom = RoomsManager.fromCuboid(gameInfos, event.from());
        final Room roomTo = RoomsManager.fromCuboid(gameInfos, event.to());

        if (roomTo == null) {
            player.teleport(roomFrom.center());
            player.sendMessage(Messages.AREA_NOT_AUTHORIZED_MESSAGE);
        }

        if (roomTo.id() == roomFrom.id() && survivorPlayer.room() != null) return;

        survivorPlayer.room(roomTo);
        survivorPlayer.player().sendMessage(survivorPlayer.room().name());
    }
}
