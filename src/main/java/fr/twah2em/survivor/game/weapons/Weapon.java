package fr.twah2em.survivor.game.weapons;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.inventories.ItemBuilder;
import fr.twah2em.survivor.utils.LocationUtils;
import fr.twah2em.survivor.utils.SoundWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

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
    private final ItemBuilder itemBuilder;

    public Weapon(String name, int clipSize, int totalAmmo, double damage, double reloadTime, double reloadTimeEmpty, double rateOfFire,
                  AmmoType ammoType, Material material, SoundWrapper shootSound, AmmoRange ammoRange, String upgradedOrDowngradedWeaponName, boolean isUpgraded) {
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

        this.itemBuilder = new ItemBuilder(material)
                .withPersistentData("survivor", "weapon_ammo_in_clip", clipSize, PersistentDataType.INTEGER)
                .withPersistentData("survivor", "weapon_total_remaining_ammo", totalAmmo, PersistentDataType.INTEGER)
                .withPersistentData("survivor", "weapon_name", name(), PersistentDataType.STRING)
                .withName("§3" + name + " §6- §f" + clipSize + "§e/§7" + totalAmmo)
                .withLore("§7• Dégâts: " + damage(),
                        "§7• Taille du chargeur: " + clipSize(),
                        "§7• Munitions maximum: " + totalAmmo(),
                        "§7• Type de balle: " + ammoType.ammoType(),
                        "§7• Amélioré: " + (isUpgraded() ? "§aOui" : "§cNon"))
                .withEnchant(isUpgraded() ? Enchantment.UNBREAKING : null)
                .withFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
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

    public int ammoInClip(ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        final PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        return persistentDataContainer.get(new NamespacedKey("survivor", "weapon_ammo_in_clip"), PersistentDataType.INTEGER);
    }

    public void ammoInClip(int ammo, ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        final PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        persistentDataContainer.set(new NamespacedKey("survivor", "weapon_ammo_in_clip"), PersistentDataType.INTEGER, ammo);

        itemStack.setItemMeta(itemMeta);
    }

    public int totalRemainingAmmo(ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        final PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        return persistentDataContainer.get(new NamespacedKey("survivor", "weapon_total_remaining_ammo"), PersistentDataType.INTEGER);
    }

    public void totalRemainingAmmo(int ammo, ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        final PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        persistentDataContainer.set(new NamespacedKey("survivor", "weapon_total_remaining_ammo"), PersistentDataType.INTEGER, ammo);

        itemStack.setItemMeta(itemMeta);
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

    public ItemStack defaultItemStack() {
        return itemBuilder.build();
    }

    public void shoot(Player shooter, ItemStack itemStack, Main main) {
        final Location start = shooter.getEyeLocation();
        final Vector direction = start.getDirection();
        final World world = start.getWorld();
        double range = this.ammoRange().blocks;
        final Particle particle = this.ammoType.particle;

        final float xRadius = ammoType == AmmoType.EXPLOSIVE ? 5 : 0.25F;
        final float yRadius = ammoType == AmmoType.EXPLOSIVE ? 3 : 3.7F;
        final float zRadius = ammoType == AmmoType.EXPLOSIVE ? 5 : 0.25F;

        int hitMobs = 0;

        final RayTraceResult blockRayTrace = world.rayTraceBlocks(
                start,
                direction.clone().normalize(),
                range,
                FluidCollisionMode.ALWAYS,
                true,
                (block) -> !Weapons.PENETRABLE_BLOCKS.contains(block.getType()));

        Location end = start.clone().add(direction.clone().normalize().multiply(range));
        if (blockRayTrace != null) {
            end = blockRayTrace.getHitPosition().toLocation(world);
            world.spawnParticle(Particle.BLOCK_CRUMBLE, end, 100, blockRayTrace.getHitBlock().getType().createBlockData());
        }

        range = start.distance(end);
        Location currentBulletLocation = start.clone();

        bulletLoop:
        for (int i = 0; i < range; i++) {
            if (ammoType == AmmoType.EXPLOSIVE) {
                currentBulletLocation = currentBulletLocation.clone()
                        .add(direction.getX(), direction.getY() - 0.05D - i * 0.01D, direction.getZ());
            } else {
                currentBulletLocation = currentBulletLocation.clone()
                        .add(direction.getX(), direction.getY() - 0.05D, direction.getZ());
            }

            world.spawnParticle(particle, currentBulletLocation, 1, 0.02F, 0.02F, 0.02F, 0.05f);

            for (final Entity entity : world.getNearbyEntities(currentBulletLocation, xRadius, yRadius, zRadius,
                    entity -> entity instanceof LivingEntity && !(entity instanceof Player))) {
                ((LivingEntity) entity).damage(damage, shooter);
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
            world.createExplosion(currentBulletLocation.add(0, 0.5, 0), 4F, false, false, shooter);
        }

        this.shootSound.play(shooter.getLocation());

        final int ammoInClip = ammoInClip(itemStack) - 1;

        ammoInClip(ammoInClip, itemStack);

        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text("§3" + name + " §6- §f" + ammoInClip + "§e/§7" + totalRemainingAmmo(itemStack)));
        itemStack.setItemMeta(itemMeta);

        if (ammoInClip <= 0) {
            if (totalRemainingAmmo(itemStack) <= 0) {
                shooter.playSound(shooter.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1.5f, 1.7f);
                return;
            }
            reload(shooter, itemStack, main);
        }
    }

    public void reload(Player player, ItemStack itemStack, Main main) {
        player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1.0f, 0.8f);
        final int time = (int) (ammoInClip(itemStack) == 0 ? reloadTimeEmpty : reloadTime) * 20;

        player.sendActionBar(Component.text("§7• R..."));
        player.setCooldown(itemStack, time);
        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1f, 1.5f), time - 5);
        main.gameLogic().reloadCooldownManager().cooldownRunnable(player, time, this, itemStack);
    }

    public enum AmmoType {
        NORMAL("Normale", Particle.ELECTRIC_SPARK),
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
