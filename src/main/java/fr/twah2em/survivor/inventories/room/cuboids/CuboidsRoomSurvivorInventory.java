package fr.twah2em.survivor.inventories.room.cuboids;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.rooms.Room;
import fr.twah2em.survivor.game.rooms.RoomsManager;
import fr.twah2em.survivor.inventories.ItemBuilder;
import fr.twah2em.survivor.inventories.SurvivorInventory;
import fr.twah2em.survivor.inventories.room.RoomCreateSurvivorInventory;
import fr.twah2em.survivor.utils.Messages;
import fr.twah2em.survivor.utils.Pair;
import fr.twah2em.survivor.utils.items.Items;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CuboidsRoomSurvivorInventory {
    private final Main main;

    private final SurvivorInventory.SurvivorInventoryBuilder survivorInventoryBuilder;
    private final RoomCreateSurvivorInventory inventoryFrom;

    // true si le joueur ferme l'inventaire pour utiliser ses items, ou s'il ferme l'inventaire pour revenir en arrière
    private boolean canClose = false;

    public CuboidsRoomSurvivorInventory(Main main, RoomCreateSurvivorInventory inventoryFrom) {
        this.main = main;

        survivorInventoryBuilder = new SurvivorInventory.SurvivorInventoryBuilder("§eCuboids", 2)
                .withGlassInEmptySlots()
                .withItemsInSlots(items())
                .withClickConsumer(clickConsumer())
                .withCloseConsumer(closeConsumer(main));

        this.inventoryFrom = inventoryFrom;
    }

    public SurvivorInventory survivorInventory() {
        return survivorInventoryBuilder.buildToSurvivorInventory();
    }

    private List<Pair<Integer, ItemStack>> items() {
        return List.of(
                Pair.of(3, new ItemBuilder(Material.WOODEN_AXE)
                        .withName("§aCréer un cuboid")
                        .withLore("§7§oVous permet de créer un cuboid.",
                                "§7§oCliquez sur cet item pour recevoir une hâche",
                                "§7§oet l'utiliser à la manière de world edit pour",
                                "§7§ocréer un région.")
                        .build()),
                Pair.of(5, new ItemBuilder(Material.OAK_SIGN)
                        .withName("§aAfficher les cuboids déjà existants de la pièce")
                        .withLore("§7§oVous montre les cuboids que vous avez déjà définis.")
                        .build()),
                Pair.of(17, new ItemBuilder(Material.BARRIER)
                        .withName("§aRevenir au menu de la pièce")
                        .build())
        );
    }

    private Consumer<InventoryClickEvent> clickConsumer() {
        return event -> {
            final Player player = (Player) event.getWhoClicked();
            final ItemStack currentItem = event.getCurrentItem();

            if (currentItem == null) return;

            final ItemMeta itemMeta = currentItem.getItemMeta();

            if (itemMeta == null || itemMeta.displayName() == null)
                return;

            event.setCancelled(true);

            final Material type = currentItem.getType();

            if (!(type == Material.WOODEN_AXE || type == Material.OAK_SIGN || type == Material.BARRIER)) return;

            canClose = true;

            if (type == Material.WOODEN_AXE) {
                player.closeInventory();

                player.getInventory().addItem(Items.WAND.build());

                return;
            }

            if (type == Material.OAK_SIGN) {
                final Room creatingRoom = RoomsManager.CREATING_ROOM.right();

                if (creatingRoom.cuboids() == null || creatingRoom.cuboids().length == 0) {
                    // on fait un set et pas une list ici pour éviter que le message se rajoute plusieurs fois si le joueur reclique sur l'item
                    final Set<Component> lore = new HashSet<>(itemMeta.lore());
                    lore.add(Messages.NO_CUBOIDS_IN_ROOM);
                    itemMeta.lore(lore.stream().toList());
                    currentItem.setItemMeta(itemMeta);

                    return;
                }

                player.closeInventory();
                player.getInventory().addItem(Items.RETURN_CUBOIDS_GUI.build());

                Arrays.stream(creatingRoom.cuboids()).forEach(cuboid -> cuboid.showEdges(player, main));

                return;
            }

            player.openInventory(new RoomCreateSurvivorInventory(main).survivorInventory().getInventory());
        };
    }

    private Consumer<InventoryCloseEvent> closeConsumer(Main main) {
        return event -> {
            if (canClose) return;

            inventoryFrom.isClosed(true);
            Bukkit.getScheduler().runTaskLater(main, () -> event.getPlayer().openInventory(inventoryFrom.survivorInventory().getInventory()), 1L);
        };
    }
}
