package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

public class EntityDamageByEntityListener implements SurvivorListener<EntityDamageByEntityEvent> {
    private final Main main;

    public EntityDamageByEntityListener(Main main) {
        this.main = main;
    }

    @Override
    @EventHandler
    public void onEvent(EntityDamageByEntityEvent event) {
        final Entity entity = event.getEntity();
        final Entity damager = event.getDamager();
        final double damage = event.getDamage();

        entity.setVelocity(new Vector());
        Bukkit.getScheduler().runTaskLater(main, () -> entity.setVelocity(new Vector()), 1L);

        if (entity instanceof final Zombie zombie) {
            if (damager instanceof final Player player) {
                if (event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                    event.setCancelled(true);
                    return;
                }

                if (main.gameInfos().round().zombiesInMap().contains(entity.getUniqueId())) {
                    if (zombie.getHealth() - damage >= 0) {
                        main.gameInfos().round().zombieDamaged(event, player, zombie);
                        return;
                    }

                    main.gameInfos().round().zombieKilled(event, player, zombie);
                }
            }
        }
    }
}
