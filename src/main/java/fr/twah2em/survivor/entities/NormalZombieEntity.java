package fr.twah2em.survivor.entities;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class NormalZombieEntity {

    public static Zombie spawn(Location location) {
        final Zombie zombie = location.getWorld().spawn(location, Zombie.class);
        metadata(zombie);

        return zombie;
    }

    private static void metadata(Zombie zombie) {
        zombie.customName(text("§eZombie ", YELLOW)
                .append(text("[", GRAY))
                .append(text(zombie.getHealth() + "❤", RED))
                .append(text("]", GRAY)));
        zombie.setCustomNameVisible(true);
        zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        zombie.setHealth(20);
        zombie.getPassengers().forEach(zombie::removePassenger);
        zombie.setAdult();
        zombie.setShouldBurnInDay(false);
    }

    public static void updateName(EntityDamageEvent event, Zombie zombie, double damage) {
        if (zombie.getHealth() - damage >= 0) {
            zombie.setHealth(zombie.getHealth() - damage);
            event.setDamage(0);
            final double health = new BigDecimal(String.valueOf(zombie.getHealth()))
                    .setScale(1, RoundingMode.HALF_DOWN)
                    .doubleValue();
            zombie.customName(text("§eZombie ", YELLOW)
                    .append(text("[", GRAY))
                    .append(text(health + "❤", RED))
                    .append(text("]", GRAY)));
        } else {
            zombie.setHealth(0);
            zombie.customName(text("§eZombie ", YELLOW)
                    .append(text("[", GRAY))
                    .append(text("0❤", RED))
                    .append(text("]", GRAY)));
        }
    }
}
