package fr.twah2em.survivor.game;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.player.SurvivorPlayer;
import fr.twah2em.survivor.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class GameLogic {
    private final Main main;

    public GameLogic(Main main) {
        this.main = main;
    }

    public void start() {
        // on fait cette task pour éviter que ça soit exécuté en async
        final Runnable task = () -> main.gameInfos().players()
                .stream()
                .map(SurvivorPlayer::player)
                .forEach(player -> {
                    if (player != null) {
                        player.teleport(new Location(player.getWorld(), -821, 69, -81));
                    }
                });

        Bukkit.getScheduler().runTask(main, task);

        final Round round = main.gameInfos().roundInfo();

        round.calculateRoundInfos();
        round.start(main);
    }

    public void stop() {

    }
}