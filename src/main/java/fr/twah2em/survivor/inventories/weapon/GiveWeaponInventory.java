package fr.twah2em.survivor.inventories.weapon;

import fr.twah2em.survivor.game.weapons.Weapon;
import fr.twah2em.survivor.game.weapons.Weapons;
import fr.twah2em.survivor.inventories.SurvivorInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class GiveWeaponInventory {

    private final SurvivorInventory.SurvivorInventoryBuilder survivorInventoryBuilder;

    public GiveWeaponInventory() {
        this.survivorInventoryBuilder = new SurvivorInventory.SurvivorInventoryBuilder("§aSe give une arme", 3)
                .withItems(items())
                .withClickConsumer(clickConsumer());
    }

    public SurvivorInventory survivorInventory() {
        return survivorInventoryBuilder.buildToSurvivorInventory();
    }

    private List<ItemStack> items() {
        return Arrays.stream(Weapons.values()).map(weapons -> {
            final Weapon weapon = weapons.weapon();

            return weapon.itemStack();
        }).toList();
    }

    private Consumer<InventoryClickEvent> clickConsumer() {
        return (event) -> {
            final Player player = (Player) event.getWhoClicked();
            final ItemStack itemStack = event.getCurrentItem();

            if (itemStack == null) return;

            player.closeInventory();
            player.getInventory().addItem(itemStack);
        };
    }
}
