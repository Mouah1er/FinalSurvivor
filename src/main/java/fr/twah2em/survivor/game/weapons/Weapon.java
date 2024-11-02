package fr.twah2em.survivor.game.weapons;

import fr.twah2em.survivor.inventories.ItemBuilder;
import fr.twah2em.survivor.utils.LocationUtils;
import fr.twah2em.survivor.utils.SoundWrapper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Weapon {
    private final String name;
    private final int clipSize;
    private final int totalAmmo;
    private final double damage;
    private final double reloadTime;
    private final double reloadTimeEmpty;
    private final double rateOfFire;
    private final AmmoType ammoType;
    private final Material material;
    private final SoundWrapper shootSound;
    private final AmmoRange ammoRange;
    private final String upgradedOrDowngradedWeaponName;
    private final boolean isUpgraded;

    private final int ammoInClip;
    private final int totalRemainingAmmo;

    public Weapon(String name, int clipSize, int totalAmmo, double damage, double reloadTime, double reloadTimeEmpty, double rateOfFire, AmmoType ammoType,
                  Material material, SoundWrapper shootSound, AmmoRange ammoRange, String upgradedOrDowngradedWeaponName, boolean isUpgraded) {
        this.name = name;
        this.clipSize = clipSize;
        this.totalAmmo = totalAmmo;
        this.damage = damage;
        this.reloadTime = reloadTime;
        this.reloadTimeEmpty = reloadTimeEmpty;
        this.rateOfFire = rateOfFire;
        this.ammoType = ammoType;
        this.material = material;
        this.shootSound = shootSound;
        this.ammoRange = ammoRange;
        this.upgradedOrDowngradedWeaponName = upgradedOrDowngradedWeaponName;
        this.isUpgraded = isUpgraded;

        //System.out.println(Integer.parseInt(weaponItemBuilder().persistentData("survivor", "weapon_ammo_in_clip", PersistentDataType.STRING)));


        this.ammoInClip = ammoInClip();
        this.totalRemainingAmmo = totalRemainingAmmo();
    }

    public String name() {
        return name;
    }

    public int clipSize() {
        return clipSize;
    }

    public int totalAmmo() {
        return totalAmmo;
    }

    public double damage() {
        return damage;
    }

    public double reloadTime() {
        return reloadTime;
    }

    public double reloadTimeEmpty() {
        return reloadTimeEmpty;
    }

    public double rateOfFire() {
        return rateOfFire;
    }

    public AmmoType ammoType() {
        return ammoType;
    }

    public SoundWrapper shootSound() {
        return shootSound;
    }

    public AmmoRange ammoRange() {
        return ammoRange;
    }

    public int ammoInClip() {
        System.out.println("test2");
        if (!weaponItemBuilder().hasPersistentData("survivor", "weapon_ammo_in_clip")) {
            System.out.println("test");
            ammoInClip(clipSize);
        }

        //System.out.println(Integer.parseInt(weaponItemBuilder().persistentData("survivor", "weapon_ammo_in_clip", PersistentDataType.STRING)));

        return Integer.parseInt(weaponItemBuilder().persistentData("survivor", "weapon_ammo_in_clip", PersistentDataType.STRING));
    }

    public void ammoInClip(int ammoInClip) {
        weaponItemBuilder().withPersistentData("survivor", "weapon_ammo_in_clip", String.valueOf(ammoInClip), PersistentDataType.STRING).build();
    }

    public int totalRemainingAmmo() {
        if (!weaponItemBuilder().hasPersistentData("survivor", "weapon_total_remaining_ammo")) {
            totalRemainingAmmo(totalAmmo);
        }

        return Integer.parseInt(weaponItemBuilder().persistentData("survivor", "weapon_total_remaining_ammo", PersistentDataType.STRING));
    }

    public void totalRemainingAmmo(int totalRemainingAmmo) {
        weaponItemBuilder().withPersistentData("survivor", "weapon_total_remaining_ammo", String.valueOf(totalRemainingAmmo), PersistentDataType.STRING).build();
    }

    public boolean isUpgraded() {
        return isUpgraded;
    }

    public Material material() {
        return material;
    }

    public String upgradedOrDowngradedWeaponName() {
        return upgradedOrDowngradedWeaponName;
    }

    public Weapon upgradedOrDowngradedWeapon() {
        return Weapons.weaponByName(upgradedOrDowngradedWeaponName);
    }

    public ItemStack itemStack() {
        return weaponItemBuilder().build();
    }

    private ItemBuilder weaponItemBuilder() {
        System.out.println(ammoInClip);
        return new ItemBuilder(material)
                .withPersistentData("survivor", "weapon_ammo_in_clip", String.valueOf(ammoInClip), PersistentDataType.STRING)
                .withPersistentData("survivor", "weapon_total_remaining_ammo", String.valueOf(totalRemainingAmmo), PersistentDataType.STRING)
                .withPersistentData("survivor", "weapon_name", name(), PersistentDataType.STRING)
                .withName("§3" + name + " §6- §f" + ammoInClip + "§e/§7" + totalRemainingAmmo)
                .withLore("§7• Dégâts: " + damage(),
                        "§7• Taille du chargeur: " + clipSize(),
                        "§7• Munitions maximum: " + totalAmmo(),
                        "§7• Type de balle: " + ammoType.ammoType(),
                        "§7• Amélioré: " + (isUpgraded() ? "§aOui" : "§cNon"))
                .withEnchant(isUpgraded() ? Enchantment.UNBREAKING : null)
                .withFlags(ItemFlag.HIDE_ENCHANTS);
    }

    public void shoot(Player shooter, ItemStack itemStack) {
        final Location eyeLocation = shooter.getEyeLocation();
        Location bulletLocation = eyeLocation.clone();
        final Particle particle = this.ammoType.particle();
        final double damage = this.damage();
        final int range = this.ammoRange.blocks();
        final AmmoType ammoType = this.ammoType();
        final World world = eyeLocation.getWorld();

        final float xRadius = ammoType == AmmoType.EXPLOSIVE ? 5 : 0.25F;
        final float yRadius = ammoType == AmmoType.EXPLOSIVE ? 5 : 3.7F;
        final float zRadius = ammoType == AmmoType.EXPLOSIVE ? 5 : 0.25F;

        int hitMobs = 0;

        bulletLoop:
        for (int i = 0; i <= range; i++) {
            if (ammoType == AmmoType.EXPLOSIVE) {
                bulletLocation = bulletLocation.clone()
                        .add(eyeLocation.getDirection().getX(), eyeLocation.getDirection().getY() - 0.05D - i * 0.01D, eyeLocation.getDirection().getZ());
            } else {
                bulletLocation = bulletLocation.clone()
                        .add(eyeLocation.getDirection().getX(), eyeLocation.getDirection().getY() - 0.05D, eyeLocation.getDirection().getZ());
            }

            world.spawnParticle(particle, bulletLocation, 1, 0.02F, 0.02F, 0.02F, 0.05f);

            if (bulletLocation.getBlock().getType().isSolid()) break;

            for (final Entity entity : LocationUtils.entitiesByLocation(bulletLocation, xRadius, yRadius, zRadius)) {
                if (!(entity instanceof final LivingEntity livingEntity && !(entity instanceof Player))) break;
                livingEntity.damage(damage, shooter);
                hitMobs++;

                if (ammoType == AmmoType.PIERCING_BULLET) {
                    if (hitMobs == 2) {
                        break bulletLoop;
                    }
                } else if (ammoType == AmmoType.NORMAL) {
                    break bulletLoop;
                }
            }
        }

        if (ammoType == AmmoType.EXPLOSIVE) {
            world.createExplosion(bulletLocation, 4F, false, false, shooter);
        }

        this.shootSound.play(shooter.getLocation());
        this.ammoInClip(ammoInClip - 1);

        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(itemStack().displayName());
        itemStack.setItemMeta(itemMeta);

        if (ammoInClip() == 0) reload(itemStack);
    }

    public void reload(ItemStack itemStack) {

    }

    public enum AmmoType {
        NORMAL("Normal", Particle.ELECTRIC_SPARK),
        EXPLOSIVE("Explosive", Particle.ENCHANTED_HIT),
        PIERCING_BULLET("Balle perforante", Particle.CRIT),

        ;

        private final String ammoType;
        private final Particle particle;

        AmmoType(String ammoType, Particle particle) {
            this.ammoType = ammoType;
            this.particle = particle;
        }

        public String ammoType() {
            return ammoType;
        }

        public Particle particle() {
            return particle;
        }
    }

    public enum AmmoRange {
        CLOSE(25),
        MEDIUM(50),
        FAR(100),

        ;

        private final int blocks;

        AmmoRange(int blocks) {
            this.blocks = blocks;
        }

        public int blocks() {
            return blocks;
        }
    }
}
