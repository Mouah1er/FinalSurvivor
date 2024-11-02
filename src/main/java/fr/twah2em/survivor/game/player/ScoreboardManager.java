package fr.twah2em.survivor.game.player;

import fr.mrmicky.fastboard.adventure.FastBoard;
import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.player.SurvivorPlayer;
import fr.twah2em.survivor.utils.Messages;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public class ScoreboardManager {
    private final Main main;

    public ScoreboardManager(Main main) {
        this.main = main;

        main.getServer().getScheduler().runTaskTimerAsynchronously(main, () -> {
            main.gameInfos().players()
                    .stream()
                    .filter(SurvivorPlayer::isOnline)
                    .collect(Collectors.toSet())
                    .forEach(this::playerScoreboard);
            main.gameInfos().spectators()
                    .forEach(this::spectatorScoreboard);
        }, 0, 20);
    }

    public void playerScoreboard(SurvivorPlayer player) {
        final FastBoard board = player.board();

        final int points = player.points();
        final int kills = player.kills();

        defaultBoard(board);
        board.updateLine(5, text("» Argent: ", GRAY).append(text(points, GOLD)).append(text("$", DARK_GREEN)));
        board.updateLine(6, text("» Kills: ", GRAY).append(text(kills, RED)));

        player.board(board);
    }

    public void spectatorScoreboard(Player player) {
        final FastBoard board = new FastBoard(player);

        defaultBoard(board);
        board.updateLine(5, text("Spectateur", GRAY, ITALIC));

    }

    private void defaultBoard(FastBoard board) {
        final int players = main.gameInfos().players().stream().filter(SurvivorPlayer::isOnline).collect(Collectors.toSet()).size();
        final int round = main.gameInfos().round().roundNumber();
        final int zombies = main.gameInfos().round().remainingZombies();

        board.updateTitle(Messages.PREFIX);

        board.updateLine(0, text("» Joueurs: ", GRAY).append(text(players, YELLOW)));
        board.updateLine(1, text(""));
        board.updateLine(2, text("» Manche: ", GRAY).append(text(round, WHITE)));
        board.updateLine(3, text("» Zombies: ", GRAY).append(text(zombies, RED)));
        board.updateLine(4, text(""));
    }
}
