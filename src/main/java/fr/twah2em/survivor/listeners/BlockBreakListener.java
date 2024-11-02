package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.rooms.RoomsManager;
import fr.twah2em.survivor.inventories.ItemBuilder;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import fr.twah2em.survivor.utils.Cuboid;
import fr.twah2em.survivor.utils.LocationUtils;
import fr.twah2em.survivor.utils.Messages;
import fr.twah2em.survivor.utils.items.Items;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements SurvivorListener<BlockBreakEvent> {
    private final Main main;

    public BlockBreakListener(Main main) {
        this.main = main;
    }

    @Override
    @EventHandler
    public void onEvent(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.WOODEN_AXE) return;

        final ItemBuilder itemBuilder = new ItemBuilder(item);

        if (!itemBuilder.isSimilar(Items.WAND)) return;

        if (RoomsManager.CREATING_ROOM == null || !RoomsManager.CREATING_ROOM.left().getUniqueId().equals(player.getUniqueId())) {
            player.getInventory().remove(item);

            return;
        }

        final String cuboidLoc1 = itemBuilder.cuboid1();
        final String cuboidLoc2 = itemBuilder.cuboid2();

        final Location blockLocation = event.getBlock().getLocation();
        final String prettyLocationString = LocationUtils.prettyLocationToString(blockLocation);

        event.setCancelled(true);

        if (cuboidLoc1 == null || cuboidLoc1.isEmpty()) {
            Cuboid.writeCuboidInItem(itemBuilder, 1, blockLocation);
            player.sendMessage(Messages.CUBOID_1_SUCCESSFULLY_CREATED(prettyLocationString));

            return;
        }

        if (cuboidLoc2 == null || cuboidLoc2.isEmpty()) {
            Cuboid.writeCuboidInItem(itemBuilder, 2, blockLocation);

            player.sendMessage(Messages.CUBOID_2_SUCCESSFULLY_CREATED(prettyLocationString));

            return;
        }

        player.sendMessage(Messages.CANNOT_ADD_MORE_POINT);
    }
}
