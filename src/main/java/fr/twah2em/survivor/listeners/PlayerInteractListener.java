package fr.twah2em.survivor.listeners;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.RoomsManager;
import fr.twah2em.survivor.inventories.ItemBuilder;
import fr.twah2em.survivor.inventories.room.cuboids.ShowCuboidInfoSurvivorInventory;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import fr.twah2em.survivor.utils.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements SurvivorListener<PlayerInteractEvent> {
    private final Main main;

    public PlayerInteractListener(Main main) {
        this.main = main;
    }

    @Override
    @EventHandler
    public void onEvent(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();

        if (item == null) return;

        if (item.getType() != Material.WOODEN_AXE) return;

        final ItemBuilder itemBuilder = new ItemBuilder(item);

        if (!itemBuilder.isWand()) return;

        if (event.getAction().isLeftClick()) return;

        if (RoomsManager.CREATING_ROOM == null || !RoomsManager.CREATING_ROOM.left().getUniqueId().equals(player.getUniqueId())) {
            player.getInventory().remove(item);

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
}
