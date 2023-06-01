package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.GameInfos;
import fr.twah2em.survivor.game.player.SurvivorPlayer;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import fr.twah2em.survivor.utils.StreamUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class PlayerJoinListener implements SurvivorListener<PlayerJoinEvent> {
    private final Main main;

    public PlayerJoinListener(Main main) {
        this.main = main;
    }

    @Override
    @EventHandler
    public void onEvent(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (main.gameInfos().state() == GameInfos.GameState.WAITING) {
            event.joinMessage(text("[", GRAY)
                    .append(text("+", GREEN))
                    .append(text("] ", GRAY))
                    .append(text(player.getName(), AQUA))
                    .append(text(" a rejoint la partie !", GREEN)));

            main.gameInfos().players().add(new SurvivorPlayer(player));
        } else {
            if (StreamUtils.playerHasPlayerWrapper(player, main.gameInfos().players())) {
                event.joinMessage(text("[", GRAY)
                        .append(text("+", GREEN))
                        .append(text("] ", GRAY))
                        .append(text(player.getName(), AQUA))
                        .append(text(" s'est reconnecté !", GREEN)));
            } else {
                event.joinMessage(text("[", GRAY)
                        .append(text("+", GRAY))
                        .append(text("] ", GRAY))
                        .append(text(player.getName(), GRAY))
                        .append(text(" a rejoint en spectateur !", GRAY)));
            }
        }
    }
}
