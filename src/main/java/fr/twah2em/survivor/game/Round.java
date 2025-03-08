package fr.twah2em.survivor.game;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.entities.NormalZombieEntity;
import fr.twah2em.survivor.game.player.SurvivorPlayer;
import fr.twah2em.survivor.runnables.RoundRunnable;
import fr.twah2em.survivor.utils.Messages;
import org.apache.commons.collections4.list.SetUniqueList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Round {
    private final Main main;

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

    public Round(Main main, GameInfos gameInfos) {
        this.main = main;

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

    public void start() {
        Bukkit.broadcast(Messages.ROUND_START_MESSAGE(String.valueOf(round)));

        new RoundRunnable(main, this).runTaskTimer(main, 0, (int) (spawnDelay * 20L));
    }

    public void stop() {
        Bukkit.broadcast(Messages.ROUND_END_MESSAGE(String.valueOf(round)));
        Bukkit.getScheduler().runTaskLater(main, () -> {
            Bukkit.broadcast(Messages.TEN_SEC_REMAINING);
            Bukkit.getScheduler().runTaskLater(main, () -> {
                calculateRoundInfos();
                start();
            }, 10 * 20);
        }, 20 * 20);
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
        this.spawnDelay = Math.max(2 * Math.pow(0.95, round - 1), 0.1);
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

    public int roundNumber() {
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

    public void zombieKilled(EntityDamageEvent event, Player killer, Zombie zombie) {
        NormalZombieEntity.updateName(event, zombie, event.getFinalDamage());

        this.remainingZombies(this.remainingZombies() - 1);
        zombiesInMap().remove(zombie.getUniqueId());

        final SurvivorPlayer survivorPlayer = SurvivorPlayer.survivorPlayer(killer, main.gameInfos().players());
        survivorPlayer.addPoints(60);
        survivorPlayer.addKill();
        killer.sendMessage(Messages.ZOMBIE_KILLED_MESSAGE);

        if (remainingZombies() == 0) stop();
    }

    public void zombieDamaged(EntityDamageEvent event, Player damager, Zombie zombie) {
        NormalZombieEntity.updateName(event, zombie, event.getFinalDamage());

        SurvivorPlayer.survivorPlayer(damager, main.gameInfos().players()).addPoints(10);
        damager.sendMessage(Messages.ZOMBIE_HIT_MESSAGE);
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
