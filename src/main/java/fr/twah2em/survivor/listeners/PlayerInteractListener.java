package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.rooms.RoomsManager;
import fr.twah2em.survivor.game.weapons.Weapon;
import fr.twah2em.survivor.game.weapons.Weapons;
import fr.twah2em.survivor.inventories.ItemBuilder;
import fr.twah2em.survivor.inventories.room.RoomCreateSurvivorInventory;
import fr.twah2em.survivor.inventories.room.cuboids.CuboidsRoomSurvivorInventory;
import fr.twah2em.survivor.inventories.room.cuboids.ShowCuboidInfoSurvivorInventory;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import fr.twah2em.survivor.utils.Messages;
import fr.twah2em.survivor.utils.items.Items;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerInteractListener implements SurvivorListener<PlayerInteractEvent> {
    private final Main main;

    public PlayerInteractListener(Main main) {
        this.main = main;
    }

    @Override
    @EventHandler
    public void onEvent(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = event.getItem();

        if (itemStack == null) return;

        handleWand(event, player, itemStack);
        handleCuboidSign(event, player, itemStack);
        handleWeaponUse(event, player, itemStack);

        System.out.println(new ItemBuilder(itemStack).hasPersistentData("survivor", "weapon_ammo_in_clip"));
    }

    private void handleWand(PlayerInteractEvent event, Player player, ItemStack itemStack) {
        if (itemStack.getType() != Material.WOODEN_AXE) return;

        final ItemBuilder itemBuilder = new ItemBuilder(itemStack);

        if (!itemBuilder.isSimilar(Items.WAND)) return;

        if (event.getAction().isLeftClick()) return;

        if (RoomsManager.CREATING_ROOM == null || !RoomsManager.CREATING_ROOM.left().getUniqueId().equals(player.getUniqueId())) {
            player.getInventory().remove(itemStack);

            return;
        }

        final String cuboidLoc1 = itemBuilder.cuboid1();
        final String cuboidLoc2 = itemBuilder.cuboid2();

        if (cuboidLoc1 == null || cuboidLoc2 == null) {
            player.sendMessage(Messages.CANNOT_CREATE_CUBOID);

            return;
        }

        player.openInventory(new ShowCuboidInfoSurvivorInventory(main, cuboidLoc1, cuboidLoc2).survivorInventory().getInventory());
    }

    private void handleCuboidSign(PlayerInteractEvent event, Player player, ItemStack itemStack) {
        if (itemStack.getType() != Material.OAK_SIGN) return;

        final ItemBuilder itemBuilder = new ItemBuilder(itemStack);

        if (!itemBuilder.isSimilar(Items.WAND)) return;

        if (event.getAction().isLeftClick()) return;

        final Inventory inventory = new CuboidsRoomSurvivorInventory(main, new RoomCreateSurvivorInventory(main)).survivorInventory().getInventory();
    }

    private void handleWeaponUse(PlayerInteractEvent event, Player player, ItemStack itemStack) {
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;

        final ItemBuilder itemBuilder = new ItemBuilder(itemStack);
        final String name = itemBuilder.persistentData("survivor", "weapon_name", PersistentDataType.STRING);
        if (name == null) return;

        final Weapon weapon = Weapons.weaponByName(name);

        if (weapon == null) return;
        if (weapon.material() != itemStack.getType() || !weapon.name().equals(name)) return;

        final int reloadCooldown = main.gameLogic().reloadCooldownManager().playerCooldown(player.getUniqueId());
        if (reloadCooldown > 0) return;

        final int shootCooldown = main.gameLogic().shootCooldownManager().playerCooldown(player.getUniqueId());
        if (shootCooldown > 0) return;

        if (weapon.ammoInClip() == 0) {
            weapon.reload(itemStack);

            return;
        }

        weapon.shoot(player, itemStack);

        final int weaponCooldownInTick = (int) weapon.rateOfFire() / 1000 * 20;
        main.gameLogic().shootCooldownManager().cooldownRunnable(player.getUniqueId(), weaponCooldownInTick);
    }
}
