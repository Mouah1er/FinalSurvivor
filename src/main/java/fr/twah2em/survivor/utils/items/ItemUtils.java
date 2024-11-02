package fr.twah2em.survivor.utils.items;

import fr.twah2em.survivor.inventories.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ItemUtils {
    public static boolean isWand(ItemStack itemStack) {
        return new ItemBuilder(itemStack).persistentData("survivor", "cuboid_wand", PersistentDataType.BOOLEAN) != null;
    }

    public static void removeWandFromPlayerInventory(Player player) {
        for (final ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null) continue;

            if (isWand(itemStack)) player.getInventory().remove(itemStack);
        }
    }
}
