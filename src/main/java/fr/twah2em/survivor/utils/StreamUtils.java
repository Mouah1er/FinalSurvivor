package fr.twah2em.survivor.utils;

import fr.twah2em.survivor.game.player.SurvivorPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class StreamUtils {
    public static boolean playerHasPlayerWrapper(Player player, List<SurvivorPlayer> players) {
        return players.stream().anyMatch(survivorPlayer -> survivorPlayer.uniqueId().equals(player.getUniqueId()));
    }
}
