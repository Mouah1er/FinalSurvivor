package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.event.PlayerEnterCuboidEvent;
import fr.twah2em.survivor.game.Room;
import fr.twah2em.survivor.game.player.SurvivorPlayer;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
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
        final Player player = event.getPlayer();

        final SurvivorPlayer survivorPlayer = SurvivorPlayer.survivorPlayer(player, main.gameInfos().players());
        survivorPlayer.room(Room.fromCuboid(main.gameInfos(), event.to()));

        survivorPlayer.player().sendMessage(survivorPlayer.room().name());
    }
}
