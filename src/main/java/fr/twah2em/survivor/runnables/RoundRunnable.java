package fr.twah2em.survivor.runnables;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.entities.NormalZombieEntity;
import fr.twah2em.survivor.game.Round;
import fr.twah2em.survivor.game.player.SurvivorPlayer;
import org.apache.commons.collections4.list.SetUniqueList;
import org.bukkit.Location;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RoundRunnable extends BukkitRunnable {
    private final Main main;
    private final Round round;

    private int playerNumber;

    final List<UUID> zombiesInMap;
    int zombiesToSpawn;
    final int remainingZombies;


    public RoundRunnable(Main main, Round round) {
        this.main = main;
        this.round = round;

        this.zombiesInMap = round.zombiesInMap();
        this.zombiesToSpawn = round.zombiesToSpawn();
        this.remainingZombies = round.remainingZombies();
    }

    @Override
    public void run() {
        if (remainingZombies == 0) {
            cancel();
            round.stopRound(main);
            return;
        }

        if (zombiesInMap.size() >= 24 || zombiesToSpawn == 0) return;

        final SetUniqueList<SurvivorPlayer> players = main.gameInfos().players();

        final SurvivorPlayer survivorPlayer = players.get(playerNumber);

        if (!survivorPlayer.isOnline()) {
            if (playerNumber == players.size() - 1) {
                playerNumber = 0;
            } else {
                playerNumber++;
            }

            return;
        }

        final Location closestWindow = survivorPlayer.closestWindow();

        final Zombie zombie = NormalZombieEntity.spawn(closestWindow);
        zombiesInMap.add(zombie.getUniqueId());

        zombiesToSpawn--;

        if (playerNumber == players.size() - 1) {
            playerNumber = 0;
        } else {
            playerNumber++;
        }
    }
}
