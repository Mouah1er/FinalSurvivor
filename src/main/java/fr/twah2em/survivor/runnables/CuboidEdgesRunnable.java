package fr.twah2em.survivor.runnables;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CuboidEdgesRunnable extends BukkitRunnable {
    private final Color color;
    private final Location location;
    private final Player player;

    public CuboidEdgesRunnable(Color color, Location location, Player player) {
        this.color = color;
        this.location = location;
        this.player = player;
    }

    @Override
    public void run() {
        new ParticleBuilder(Particle.REDSTONE)
                .color(color)
                .location(location)
                .receivers(player)
                .spawn();
    }
}
