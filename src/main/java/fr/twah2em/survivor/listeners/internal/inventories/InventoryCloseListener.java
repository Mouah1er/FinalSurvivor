package fr.twah2em.survivor.listeners.internal.inventories;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.inventories.SurvivorInventory;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryCloseListener implements SurvivorListener<InventoryCloseEvent> {
    private final Main main;

    public InventoryCloseListener(Main main) {
        this.main = main;
    }

    @Override
    @EventHandler
    public void onEvent(InventoryCloseEvent event) {
        final InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof final SurvivorInventory inventory) {
            if (inventory.cancelCloseEvent()) {
                Bukkit.getScheduler().runTaskLater(main, () -> event.getPlayer().openInventory(inventory.getInventory()), 1L);
            }

            inventory.closeConsumer().accept(event);
        }
    }
}
