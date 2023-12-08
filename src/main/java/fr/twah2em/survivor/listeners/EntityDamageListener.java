package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.entities.NormalZombieEntity;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements SurvivorListener<EntityDamageEvent> {
    private final Main main;

    public EntityDamageListener(Main main) {
        this.main = main;
    }

    @Override
    @EventHandler
    public void onEvent(EntityDamageEvent event) {
        final Entity entity = event.getEntity();

        if (entity instanceof final Zombie zombie) {
            if (main.gameInfos().round().zombiesInMap().contains(entity.getUniqueId())) {
                NormalZombieEntity.updateName(event, zombie, event.getFinalDamage());
            }
        }
    }
}
