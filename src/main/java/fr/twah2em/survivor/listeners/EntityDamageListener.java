package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

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
            if (main.gameInfos().roundInfo().zombiesInMap().contains(entity.getUniqueId())) {
                if (zombie.getHealth() == zombie.getHealth() - event.getFinalDamage()) {
                    System.out.println("test");
                    zombie.damage(event.getFinalDamage());
                }

                if (zombie.getHealth() - event.getFinalDamage() >= 0) {
                    entity.customName(text("§eZombie ", YELLOW)
                            .append(text("[", GRAY))
                            .append(text(zombie.getHealth() + "❤", RED))
                            .append(text("]", GRAY)));
                } else {
                    entity.customName(text("§eZombie ", YELLOW)
                            .append(text("[", GRAY))
                            .append(text("0❤", RED))
                            .append(text("]", GRAY)));
                }

                System.out.println(zombie.getHealth());

                entity.setCustomNameVisible(false);
                entity.setCustomNameVisible(true);
            }
        }
    }
}
