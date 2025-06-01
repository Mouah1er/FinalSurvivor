package fr.twah2em.survivor.game.player.corpse;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.utils.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ReviveCooldownManager {
    private final Main main;
    private final Map<Pair<UUID, UUID>, Integer> cooldowns;

    public ReviveCooldownManager(Main main) {
        this.main = main;
        this.cooldowns = new HashMap<>();
    }

    public int playerCooldown(UUID reviver, UUID revived) {
        return cooldowns.getOrDefault(Pair.of(reviver, revived), -1);
    }

    public void playerCooldown(UUID reviver, UUID revived, int time) {
        if (time == -1) {
            cooldowns.remove(Pair.of(reviver, revived));
            return;
        }

        cooldowns.put(Pair.of(reviver, revived), time);
    }

    public boolean isRevivingSomeone(UUID reviver) {
        return cooldowns.entrySet()
                .stream()
                .anyMatch(entry -> entry.getValue() != 1 && entry.getKey().left().equals(reviver));
    }

    public boolean isBeingRevivedBySomeone(UUID revived) {
        return cooldowns.entrySet()
                .stream()
                .anyMatch(entry -> entry.getValue() != 1 && entry.getKey().right().equals(revived));
    }

    public UUID whoIsBeingRevived(UUID reviver) {
        return cooldowns.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != 1 && entry.getKey().left().equals(reviver))
                .map(entry -> entry.getKey().right())
                .findFirst()
                .orElse(null);
    }


    public void cooldownRunnable(UUID reviver, UUID revived, int time) {
        playerCooldown(reviver, revived, time);
        final Player reviverPlayer = Bukkit.getPlayer(reviver);
        final Player revivedPlayer = Bukkit.getPlayer(revived);

        if (reviverPlayer == null || revivedPlayer == null) return;

        final AtomicInteger atomicTime = new AtomicInteger(time);
        final int totalTime = time;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (playerCooldown(reviver, revived) == -1) {
                    cancel();
                    return;
                }

                final int i = atomicTime.decrementAndGet();

                if (reviverPlayer.getLocation().distance(revivedPlayer.getLocation()) > 1) {
                    cancelReanimation(reviverPlayer, revivedPlayer);
                    cancel();
                    return;
                }

                if (i == 0) {
                    playerCooldown(reviver, revived, -1);
                    revivePlayer(reviverPlayer, revivedPlayer);
                    cancel();
                    return;
                }

                updateProgress(reviverPlayer, revivedPlayer, i, totalTime);
            }
        }.runTaskTimer(main, 0, 1);
    }

    private void updateProgress(Player reviver, Player revived, int remainingTime, int totalTime) {
        final float percentage = 1.0f - ((float) (totalTime - remainingTime) / totalTime);
        reviver.setLevel((int) percentage * 100);
        reviver.setExp(percentage);
        revived.setLevel((int) percentage * 100);
        revived.setExp(percentage);
    }

    private void revivePlayer(Player reviver, Player revived) {
        reviver.setLevel(0);
        reviver.setExp(0);
        revived.setLevel(0);
        revived.setExp(0);

        CorpseManager.destroyCorpse(main, revived);
        revived.teleport(reviver.getLocation().clone().add(0, 1, 0));
        revived.setGameMode(GameMode.ADVENTURE);
        revived.setHealth(6);

        reviver.sendActionBar(Component.text("Réanimation réussie!", TextColor.color(210, 235, 22)));
        revived.sendActionBar(Component.text("Vous avez été réanimé!", TextColor.color(210, 235, 22)));
    }

    public void cancelReanimation(Player reviver, Player revived) {
        reviver.setLevel(0);
        reviver.setExp(0);
        revived.setLevel(0);
        revived.setExp(0);

        playerCooldown(reviver.getUniqueId(), revived.getUniqueId(), -1);
        reviver.sendActionBar(Component.text("Réanimation ratée!", TextColor.color(235, 0, 6)));
    }
}
