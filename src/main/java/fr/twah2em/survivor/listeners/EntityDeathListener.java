package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements SurvivorListener<EntityDeathEvent> {

    public EntityDeathListener(Main main) {

    }

    @Override
    @EventHandler
    public void onEvent(EntityDeathEvent event) {
        event.setDroppedExp(0);
        event.getDrops().clear();
        event.getEntity().remove();
    }
}
