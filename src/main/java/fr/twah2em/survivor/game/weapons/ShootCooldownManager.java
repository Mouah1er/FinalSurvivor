package fr.twah2em.survivor.game.weapons;

import fr.twah2em.survivor.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShootCooldownManager {
    private final Main main;
    private final Map<UUID, Integer> cooldowns;

    public ShootCooldownManager(Main main) {
        this.main = main;
        this.cooldowns = new HashMap<>();
    }

    public int playerCooldown(UUID player) {
        return cooldowns.getOrDefault(player, 0);
    }

    public void playerCooldown(UUID player, int time) {
        if (time == -1) {
            cooldowns.remove(player);
            return;
        }

        cooldowns.put(player, time);
    }

    public void cooldownRunnable(UUID player, int time) {
        playerCooldown(player, time);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> playerCooldown(player, -1), time);
    }
}
