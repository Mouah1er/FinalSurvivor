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
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

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
        event.setCancelled(true);

        final int reloadCooldown = main.gameLogic().reloadCooldownManager().playerCooldown(player.getUniqueId());
        if (reloadCooldown > 0) return;

        final int shootCooldown = main.gameLogic().shootCooldownManager().playerCooldown(player.getUniqueId());
        if (shootCooldown > 0) return;

        if (weapon.ammoInClip(itemStack) <= 0) {
            if (weapon.totalRemainingAmmo(itemStack) <= 0) {
                player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1.5f, 1.7f);
                return;
            }

            weapon.reload(player, itemStack, main);

            return;
        }

        weapon.shoot(player, itemStack, main);

        final int weaponCooldownInTick = (int) (20 / (weapon.rateOfFire() / 60));
        main.gameLogic().shootCooldownManager().cooldownRunnable(player.getUniqueId(), weaponCooldownInTick);
    }
}
