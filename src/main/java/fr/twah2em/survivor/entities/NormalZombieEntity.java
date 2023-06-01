package fr.twah2em.survivor.entities;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Zombie;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class NormalZombieEntity {

    public static Zombie spawn(Location location) {
        final Zombie entity = location.getWorld().spawn(location, Zombie.class);

        entity.customName(text("§eZombie ", YELLOW)
                .append(text("[", GRAY))
                .append(text(entity.getHealth() + "❤", RED))
                .append(text("]", GRAY)));
        entity.setCustomNameVisible(true);
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        entity.setHealth(20);
        entity.getPassengers().forEach(entity::removePassenger);
        entity.setAdult();
        entity.setShouldBurnInDay(false);

        return entity;
    }
}
