package fr.twah2em.survivor.game;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.player.SurvivorPlayer;
import fr.twah2em.survivor.game.player.corpse.ReviveCooldownManager;
import fr.twah2em.survivor.game.weapons.ReloadCooldownManager;
import fr.twah2em.survivor.game.weapons.ShootCooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;

public class GameLogic {
    private final Main main;
    private final ShootCooldownManager shootCooldownManager;
    private final ReloadCooldownManager reloadCooldownManager;
    private final ReviveCooldownManager reviveCooldownManager;

    public GameLogic(Main main) {
        this.main = main;
        this.shootCooldownManager = new ShootCooldownManager(main);
        this.reloadCooldownManager = new ReloadCooldownManager(main);
        this.reviveCooldownManager = new ReviveCooldownManager(main);
    }

    public void start() {
        main.gameInfos().state(GameInfos.GameState.STARTING);

        // on fait cette task pour éviter que ça soit exécuté en async
        final Runnable task = () -> main.gameInfos().players()
                .stream()
                .map(SurvivorPlayer::player)
                .forEach(player -> {
                    if (player != null) {
                        player.teleport(new Location(player.getWorld(), -821, 69, -81));
                        player.setHealth(20);
                        player.setFoodLevel(20);
                        player.setGameMode(GameMode.ADVENTURE);
                    }
                });

        Bukkit.getScheduler().runTask(main, task);

        final Round round = main.gameInfos().round();

        round.calculateRoundInfos();
        round.start();
        main.gameInfos().state(GameInfos.GameState.PLAYING);
    }

    public void stop() {

    }

    public ShootCooldownManager shootCooldownManager() {
        return shootCooldownManager;
    }

    public ReloadCooldownManager reloadCooldownManager() {
        return reloadCooldownManager;
    }

    public ReviveCooldownManager reviveCooldownManager() {
        return reviveCooldownManager;
    }
}
