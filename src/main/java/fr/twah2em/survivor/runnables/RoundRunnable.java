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
import java.util.UUID;

public class RoundRunnable extends BukkitRunnable {
    private final Main main;

    private int playerNumber;

    final List<UUID> zombiesInMap;
    int zombiesToSpawn;
    final int remainingZombies;
    final double zombieHealth;


    public RoundRunnable(Main main, Round round) {
        this.main = main;

        this.zombiesInMap = round.zombiesInMap();
        this.zombiesToSpawn = round.zombiesToSpawn();
        this.remainingZombies = round.remainingZombies();
        this.zombieHealth = round.zombieHealth();
    }

    @Override
    public void run() {
        if (zombiesInMap.size() >= 24) return;
        if (zombiesToSpawn <= 0) cancel();

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

        final Location[] zombieSpawnLocations = survivorPlayer.room().windows();

        for (final Location zombieSpawnLocation : zombieSpawnLocations) {
            if (zombiesToSpawn <= 0) {
                cancel();
                break;
            }

            final Zombie zombie = NormalZombieEntity.spawn(zombieHealth, zombieSpawnLocation);
            zombie.setTarget(survivorPlayer.player());
            zombie.clearActiveItem();
            zombiesInMap.add(zombie.getUniqueId());

            zombiesToSpawn--;
        }

        if (playerNumber == players.size() - 1) {
            playerNumber = 0;
        } else {
            playerNumber++;
        }
    }
}
