package fr.twah2em.survivor.entities;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class NormalZombieEntity {

    public static Zombie spawn(double health, Location location) {
        final Zombie zombie = location.getWorld().spawn(location, Zombie.class);
        metadata(health, zombie);

        return zombie;
    }

    private static void metadata(double health, Zombie zombie) {
        zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(/*health*/20);
        zombie.setHealth(/*health*/20);
        zombie.getPassengers().forEach(zombie::removePassenger);
        zombie.setAdult();
        zombie.setShouldBurnInDay(false);
        zombie.setCanPickupItems(false);
        zombie.clearActiveItem();
        zombie.setMaximumNoDamageTicks(0);
        zombie.customName(customName(zombie.getHealth()));
        zombie.setCustomNameVisible(true);
    }

    public static void updateName(EntityDamageEvent event, Zombie zombie, double damage) {
        if (zombie.getHealth() - damage >= 0) {
            zombie.setHealth(zombie.getHealth() - damage);
            event.setDamage(0);
            final double health = new BigDecimal(String.valueOf(zombie.getHealth()))
                    .setScale(1, RoundingMode.HALF_DOWN)
                    .doubleValue();
            zombie.customName(customName(health));
        } else {
            zombie.setHealth(0);
            zombie.customName(customName(0));
        }
    }

    private static Component customName(double health) {
        return text("§eZombie ", YELLOW)
                .append(text("[", GRAY))
                .append(text(health + "❤", RED))
                .append(text("]", GRAY));
    }
}
