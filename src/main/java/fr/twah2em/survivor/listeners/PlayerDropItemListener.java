package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.RoomsManager;
import fr.twah2em.survivor.inventories.ItemBuilder;
import fr.twah2em.survivor.inventories.room.RoomCreateSurvivorInventory;
import fr.twah2em.survivor.inventories.room.cuboids.CuboidsRoomSurvivorInventory;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import fr.twah2em.survivor.utils.Cuboid;
import fr.twah2em.survivor.utils.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;

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

        if (itemDrop.getItemStack().getType() != Material.WOODEN_AXE) return;

        final ItemBuilder dropItemBuilder = new ItemBuilder(itemDrop.getItemStack());

        if (!dropItemBuilder.isWand()) return;

        if (RoomsManager.CREATING_ROOM == null || !RoomsManager.CREATING_ROOM.left().getUniqueId().equals(player.getUniqueId())) {
            itemDrop.remove();

            return;
        }

        final String cuboidLoc1 = dropItemBuilder.cuboid1();
        final String cuboidLoc2 = dropItemBuilder.cuboid2();

        if (cuboidLoc1 == null || cuboidLoc1.isEmpty()) {
            itemDrop.remove();
            player.sendMessage(Messages.CUBOID_WAND_SUCCESSFULLY_REMOVED);

            player.openInventory(new CuboidsRoomSurvivorInventory(main, new RoomCreateSurvivorInventory(main)).survivorInventory().getInventory());

            return;
        }

        if (cuboidLoc2 == null || cuboidLoc2.isEmpty()) {
            event.setCancelled(true);

            Cuboid.emptyCuboidInItem(dropItemBuilder, 1);

            player.sendMessage(Messages.CUBOID_1_SUCCESSFULLY_REMOVED);

            return;
        }

        event.setCancelled(true);

        Cuboid.emptyCuboidInItem(dropItemBuilder, 2);

        player.sendMessage(Messages.CUBOID_2_SUCCESSFULLY_REMOVED);
    }

    // TODO: FAIRE UN SYSTEME DE CLIQUE DROIT AVEC HACHE QUI PERMET D'OUVRIR UN MENU ET VALIDER LE CUBOID
}
