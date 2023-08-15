package fr.twah2em.survivor.game;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.entities.NormalZombieEntity;
import fr.twah2em.survivor.game.player.SurvivorPlayer;
import fr.twah2em.survivor.utils.Messages;
import net.kyori.adventure.text.TextReplacementConfig;
import org.apache.commons.collections4.list.SetUniqueList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Round {
    private final GameInfos gameInfos;

    private final List<SurvivorPlayer> deadPlayers = new ArrayList<>();
    private final List<SurvivorPlayer> alivePlayers = new ArrayList<>();
    private final List<Bonus> activeBonuses = new ArrayList<>();
    private int round;
    private int zombiesToSpawn;
    private double zombieHealth;
    private double spawnDelay;
    private int remainingZombies;

    private final List<UUID> zombiesInMap;

    public Round(GameInfos gameInfos) {
        this.gameInfos = gameInfos;
        this.zombiesInMap = new ArrayList<>();
    }

    public void calculateRoundInfos() {
        calculateRoundNumber();
        calculateZombiesToSpawn();
        calculateZombieHealth();
        calculateZombieSpawnDelay();

        this.remainingZombies = zombiesToSpawn;
    }

    public void start(Main main) {
        Bukkit.broadcast(Messages.ROUND_START_MESSAGE.replaceText(TextReplacementConfig.builder().matchLiteral("%round%").replacement(String.valueOf(round)).build()));

        new BukkitRunnable() {
            int playerNumber = 0;

            @Override
            public void run() {
                if (remainingZombies == 0) {
                    cancel();
                    stopWave(main);
                    return;
                }

                if (zombiesInMap.size() >= 24 || zombiesToSpawn == 0) return;

                final SetUniqueList<SurvivorPlayer> players = main.gameInfos().players();

                final SurvivorPlayer survivorPlayer = players.get(playerNumber);
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
        }.runTaskTimer(main, 0, (int) (spawnDelay * 20L));
    }

    private void stopWave(Main main) {
        Bukkit.broadcast(Messages.ROUND_END_MESSAGE.replaceText(TextReplacementConfig.builder().matchLiteral("%round%").replacement(String.valueOf(round)).build()));
        Bukkit.getScheduler().runTaskLater(main, () -> {
            calculateRoundInfos();
            start(main);
        }, 30 * 20);
    }

    private void calculateRoundNumber() {
        this.round += 1;
    }

    private void calculateZombiesToSpawn() {
        final int players = gameInfos.players().size();

        double multiplier = (double) round / 5;

        if (multiplier < 1) {
            multiplier = 1;
        } else if (round >= 10) {
            multiplier *= round * 0.15;
        }

        final int tempZombiesToSpawn;

        if (players == 1) {
            tempZombiesToSpawn = (int) (24 + (0.5 * 6 * multiplier));
        } else {
            tempZombiesToSpawn = (int) (24 + ((players - 1) * 6 * multiplier));
        }

        this.zombiesToSpawn = tempZombiesToSpawn;

        if (round < 2) {
            this.zombiesToSpawn = (int) (tempZombiesToSpawn * 0.25);
        } else if (round < 3) {
            this.zombiesToSpawn = (int) (tempZombiesToSpawn * 0.3);
        } else if (round < 4) {
            this.zombiesToSpawn = (int) (tempZombiesToSpawn * 0.5);
        } else if (round < 5) {
            this.zombiesToSpawn = (int) (tempZombiesToSpawn * 0.7);
        } else if (round < 6) {
            this.zombiesToSpawn = (int) (tempZombiesToSpawn * 0.9);
        }
    }

    private void calculateZombieHealth() {
        this.zombieHealth = 150;

        // if round = 1 the for isnt executed
        for (int i = 2; i < round + 1; i++) {
            if (i < 10) {
                this.zombieHealth += 100;
            } else {
                this.zombieHealth += (int) (this.zombieHealth * 0.1);
            }
        }
    }

    private void calculateZombieSpawnDelay() {
        this.spawnDelay = 2.0;

        if (round > 1) {
            for (int i = 1; i < round; i++) {
                spawnDelay *= 0.95;

                if (spawnDelay < 0.08) {
                    spawnDelay = 0.08;
                }
            }
        }
    }

    public List<SurvivorPlayer> deadPlayers() {
        return deadPlayers;
    }

    public List<SurvivorPlayer> alivePlayers() {
        return alivePlayers;
    }

    public List<Bonus> activeBonuses() {
        return activeBonuses;
    }

    public int round() {
        return round;
    }

    public int zombiesToSpawn() {
        return zombiesToSpawn;
    }

    public double zombieHealth() {
        return zombieHealth;
    }

    public int remainingZombies() {
        return remainingZombies;
    }

    public void remainingZombies(int remainingZombies) {
        this.remainingZombies = remainingZombies;
    }

    public void zombieKilled(UUID uniqueId) {
        this.remainingZombies(this.remainingZombies() - 1);
        zombiesInMap().remove(uniqueId);
    }

    public double spawnDelay() {
        return spawnDelay;
    }

    public List<UUID> zombiesInMap() {
        return zombiesInMap;
    }

    public enum Bonus {
        INSTA_KILL,
        NUKE,
        MAX_AMMO,
        DOUBLE_POINTS,
        FIRE_SALE,
        CARPENTER,
        BONUS_POINTS,
        DEATH_MACHINE
    }
}
