package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.GameInfos;
import fr.twah2em.survivor.game.player.SurvivorPlayer;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import fr.twah2em.survivor.utils.StreamUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

public class PlayerQuitListener implements SurvivorListener<PlayerQuitEvent> {
    private final Main main;

    public PlayerQuitListener(Main main) {
        this.main = main;
    }

    @Override
    @EventHandler
    public void onEvent(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        if (main.gameInfos().state() == GameInfos.GameState.WAITING) {
            event.quitMessage(text("[", GRAY)
                    .append(text("-", RED))
                    .append(text("] ", GRAY))
                    .append(text(player.getName(), AQUA))
                    .append(text(" a quitté la partie !", GREEN)));

            main.gameInfos().players().remove(SurvivorPlayer.survivorPlayer(player, main.gameInfos().players()));
        } else {
            if (StreamUtils.playerHasPlayerWrapper(player, main.gameInfos().players())) {
                event.quitMessage(text("[", GRAY)
                        .append(text("-", RED))
                        .append(text("] ", GRAY))
                        .append(text(player.getName(), AQUA))
                        .append(text(" s'est déconnecté, il peut cependant revenir.", GREEN)));
            }
        }
    }
}
