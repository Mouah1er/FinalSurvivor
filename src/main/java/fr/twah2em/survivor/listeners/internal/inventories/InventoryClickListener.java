package fr.twah2em.survivor.listeners.internal.inventories;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.inventories.SurvivorInventory;
import fr.twah2em.survivor.listeners.internal.SurvivorListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryClickListener implements SurvivorListener<InventoryClickEvent> {
    public InventoryClickListener(Main main) {}

    @Override
    @EventHandler
    public void onEvent(InventoryClickEvent event) {
        final InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof final SurvivorInventory inventory) {
            inventory.clickConsumer().accept(event);
        }
    }
}
