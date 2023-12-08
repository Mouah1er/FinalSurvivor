package fr.twah2em.survivor.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class Holograms {
    public static ArmorStand createHologram(Location location, Component text) {
        final ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(true);
        armorStand.customName(text);

        return armorStand;
    }

    public static List<ArmorStand> createHolograms(List<Pair<Location, TextComponent>> holograms) {
        final List<ArmorStand> holo = new ArrayList<>();

        holograms.forEach(pair -> holo.add(createHologram(pair.left(), pair.right())));

        return holo;
    }
}
