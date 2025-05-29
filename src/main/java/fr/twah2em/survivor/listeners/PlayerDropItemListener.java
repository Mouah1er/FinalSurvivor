package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.rooms.RoomsManager;
import fr.twah2em.survivor.game.weapons.Weapon;
import fr.twah2em.survivor.game.weapons.Weapons;
import fr.twah2em.survivor.inventories.ItemBuilder;
import fr.twah2em.survivor.inventories.room.RoomCreateSurvivorInventory;
import fr.twah2em.survivor.inventories.room.cuboids.CuboidsRoomSurvivorInventory;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import fr.twah2em.survivor.utils.Cuboid;
import fr.twah2em.survivor.utils.Messages;
import fr.twah2em.survivor.utils.items.Items;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PlayerDropItemListener implements SurvivorListener<PlayerDropItemEvent> {
    private final Main main;

    public PlayerDropItemListener(Main main) {
        this.main = main;
    }

    @Override
    @EventHandler
    public void onEvent(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        final Item itemDrop = event.getItemDrop();

        handleWand(event, player, itemDrop);
        handleCuboidSign(event, player, itemDrop);
        handleWeapon(event, player, itemDrop);
    }

    private void handleWand(PlayerDropItemEvent event, Player player, Item itemDrop) {
        if (itemDrop.getItemStack().getType() != Material.WOODEN_AXE) return;

        final ItemBuilder itemDropBuilder = new ItemBuilder(itemDrop.getItemStack());

        if (!itemDropBuilder.isSimilar(Items.WAND)) return;

        if (RoomsManager.CREATING_ROOM == null || !RoomsManager.CREATING_ROOM.left().getUniqueId().equals(player.getUniqueId())) {
            itemDrop.remove();

            return;
        }

        final String cuboidLoc1 = itemDropBuilder.cuboid1();
        final String cuboidLoc2 = itemDropBuilder.cuboid2();

        if (cuboidLoc1 == null || cuboidLoc1.isEmpty()) {
            itemDrop.remove();
            player.sendMessage(Messages.CUBOID_WAND_SUCCESSFULLY_REMOVED);

            player.openInventory(new CuboidsRoomSurvivorInventory(main, new RoomCreateSurvivorInventory(main)).survivorInventory().getInventory());

            return;
        }

        if (cuboidLoc2 == null || cuboidLoc2.isEmpty()) {
            event.setCancelled(true);

            Cuboid.emptyCuboidInItem(itemDropBuilder, 1);

            player.sendMessage(Messages.CUBOID_1_SUCCESSFULLY_REMOVED);

            return;
        }

        event.setCancelled(true);

        Cuboid.emptyCuboidInItem(itemDropBuilder, 2);

        player.sendMessage(Messages.CUBOID_2_SUCCESSFULLY_REMOVED);
    }

    private void handleCuboidSign(PlayerDropItemEvent event, Player player, Item itemDrop) {
        if (itemDrop.getItemStack().getType() != Material.OAK_SIGN) return;

        final ItemBuilder itemDropBuilder = new ItemBuilder(itemDrop.getItemStack());

        if (!itemDropBuilder.isSimilar(Items.RETURN_CUBOIDS_GUI)) return;

        event.setCancelled(true);
        player.sendMessage(Messages.CANT_DROP);
    }

    private void handleWeapon(PlayerDropItemEvent event, Player player, Item itemDrop) {
        final ItemStack itemStack = itemDrop.getItemStack();

        final ItemBuilder itemBuilder = new ItemBuilder(itemStack);
        final String name = itemBuilder.persistentData("survivor", "weapon_name", PersistentDataType.STRING);
        if (name == null) return;

        final Weapon weapon = Weapons.weaponByName(name);

        if (weapon == null) return;
        if (weapon.material() != itemStack.getType() || !weapon.name().equals(name)) return;
        event.setCancelled(true);

        final int reloadCooldown = main.gameLogic().reloadCooldownManager().playerCooldown(player.getUniqueId());
        if (reloadCooldown > 0) return;

        if (weapon.ammoInClip(itemStack) <= 0) {
            if (weapon.totalRemainingAmmo(itemStack) <= 0) {
                return;
            }
        }

        if (weapon.ammoInClip(itemStack) >= weapon.clipSize()) return;
        
        weapon.reload(player, itemStack, main);
    }
}
