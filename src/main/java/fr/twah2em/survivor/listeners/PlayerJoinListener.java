package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.event.PlayerEnterCuboidEvent;
import fr.twah2em.survivor.game.GameInfos;
import fr.twah2em.survivor.game.player.SurvivorPlayer;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import fr.twah2em.survivor.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

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
        final GameInfos gameInfos = main.gameInfos();

        if (gameInfos.state() == GameInfos.GameState.WAITING) {
            event.joinMessage(text("[", GRAY)
                    .append(text("+", GREEN))
                    .append(text("] ", GRAY))
                    .append(text(player.getName(), AQUA))
                    .append(text(" a rejoint la partie !", GREEN)));

            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(gameInfos.rooms().get(0).center());

            gameInfos.players().add(new SurvivorPlayer(player));
        } else {
            if (SurvivorPlayer.playerIsSurvivorPlayer(player, gameInfos.players())) {
                event.joinMessage(text("[", GRAY)
                        .append(text("+", GREEN))
                        .append(text("] ", GRAY))
                        .append(text(player.getName(), AQUA))
                        .append(text(" s'est reconnecté !", GREEN)));

                player.setGameMode(GameMode.ADVENTURE);
            } else {
                player.setGameMode(GameMode.SPECTATOR);

                event.joinMessage(text("[", GRAY)
                        .append(text("+", GRAY))
                        .append(text("] ", GRAY))
                        .append(text(player.getName(), GRAY))
                        .append(text(" a rejoint en spectateur !", GRAY)));

                player.teleport(gameInfos.rooms().get(0).center());

                gameInfos.spectators().add(player);
            }
        }

        final Cuboid playerCuboid = Cuboid.fromLocation(gameInfos, player.getLocation());

        Bukkit.getPluginManager().callEvent(new PlayerEnterCuboidEvent(player, playerCuboid, playerCuboid));
    }
}
