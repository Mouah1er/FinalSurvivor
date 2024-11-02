package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.player.SurvivorPlayer;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import fr.twah2em.survivor.utils.Messages;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class EntityDeathListener implements SurvivorListener<EntityDeathEvent> {
    private final Main main;

    public EntityDeathListener(Main main) {
        this.main = main;
    }

    @Override
    @EventHandler
    public void onEvent(EntityDeathEvent event) {
        final LivingEntity entity = event.getEntity();

        if (entity instanceof Zombie) {
            final Player killer = event.getEntity().getKiller();
            final UUID entityUniqueId = entity.getUniqueId();

            if (killer != null) {
                if (main.gameInfos().round().zombiesInMap().contains(entityUniqueId)) {
                    event.setDroppedExp(0);
                    event.getDrops().clear();
                }
            }
        }
    }
}
