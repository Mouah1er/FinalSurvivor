package fr.twah2em.survivor.game.weapons;

import fr.twah2em.survivor.utils.SoundWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.Set;

import static fr.twah2em.survivor.game.weapons.Weapon.AmmoRange.MEDIUM;
import static fr.twah2em.survivor.game.weapons.Weapon.AmmoType.EXPLOSIVE;
import static fr.twah2em.survivor.game.weapons.Weapon.AmmoType.NORMAL;
import static org.bukkit.Material.*;

public enum Weapons {
    M1911(new Weapon("M1911", 8, 88, 20, 1.63, 1.8, 500, NORMAL,
            Material.GOLDEN_HORSE_ARMOR, new SoundWrapper(Sound.ENTITY_WITHER_SHOOT, 0.5F, 2.0F), MEDIUM, "Mustang and Sally",
            false)),
    MUSTANG_AND_SALLY(new Weapon("Mustang and Sally", 6, 56, 2200, 1.63, 1.8, 300, EXPLOSIVE, Material.GOLDEN_HORSE_ARMOR, new SoundWrapper(Sound.ENTITY_ARROW_HIT_PLAYER, 0.5F, 2.0F), MEDIUM,
            "M1911", true)),

    CHINA_LAKE(new Weapon("China Lake", 2, 22, 600, 2, 3, 60, EXPLOSIVE,
            Material.DIAMOND_HORSE_ARMOR, new SoundWrapper(Sound.ITEM_CROP_PLANT, 10F, 2.0F), MEDIUM, "China Beach",
            false)),
    CHINA_BEACH(new Weapon("China Beach", 5, 45, 1000, 2, 3, 60, EXPLOSIVE,
            Material.DIAMOND_HORSE_ARMOR, new SoundWrapper(Sound.ENTITY_ARROW_HIT_PLAYER, 0.7F, 1.2F), MEDIUM, "China Beach",
            true)),
    ;

    private final Weapon weapon;

    public static final Set<Material> PENETRABLE_BLOCKS = Set.of(
            AIR, CAVE_AIR, VOID_AIR, BARRIER, IRON_BARS, CHAIN,
            OAK_FENCE, SPRUCE_FENCE, BIRCH_FENCE, JUNGLE_FENCE,
            ACACIA_FENCE, DARK_OAK_FENCE, MANGROVE_FENCE, BAMBOO_FENCE,
            CHERRY_FENCE, CRIMSON_FENCE, WARPED_FENCE, NETHER_BRICK_FENCE
    );

    Weapons(Weapon weapon) {
        this.weapon = weapon;
    }

    public Weapon weapon() {
        return this.weapon;
    }

    public static Weapon weaponByName(String name) {
        for (final Weapons weapons : Weapons.values()) {
            if (weapons.weapon().name().equals(name)) {
                return weapons.weapon();
            }
        }

        return null;
    }

    public static Weapon weaponByName(Component name) {
        if (name instanceof final TextComponent text) {
            return weaponByName(text.content());
        }

        return null;
    }
}
