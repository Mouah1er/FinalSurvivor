package fr.twah2em.survivor.inventories.room;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.Room;
import fr.twah2em.survivor.game.RoomsManager;
import fr.twah2em.survivor.inventories.ItemBuilder;
import fr.twah2em.survivor.inventories.SurvivorInventory;
import fr.twah2em.survivor.inventories.room.cuboids.CuboidsRoomSurvivorInventory;
import fr.twah2em.survivor.utils.Messages;
import fr.twah2em.survivor.utils.Pair;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class RoomCreateSurvivorInventory {
    private final SurvivorInventory.SurvivorInventoryBuilder survivorInventoryBuilder;

    // true si l'inventaire est fermé pour annuler la création de la pièce, faux s'il est fermé pour ouvrir un autre inventaire relatif à la création de la pièce
    private boolean isClosed;

    public RoomCreateSurvivorInventory(Main main) {
        this.survivorInventoryBuilder = new SurvivorInventory.SurvivorInventoryBuilder("§aCréer une §bpièce", 2)
                .withItemsInSlots(items())
                .withGlassInEmptySlots()
                .withClickConsumer(clickConsumer(main))
                .withOpenConsumer(openConsumer())
                .withCloseConsumer(closeConsumer())
                ;
        this.isClosed = true;
    }

    public SurvivorInventory survivorInventory() {
        return survivorInventoryBuilder.buildToSurvivorInventory();
    }

    private List<Pair<Integer, ItemStack>> items() {
        return List.of(
                Pair.of(2, new ItemBuilder(Material.WOODEN_AXE)
                        .withName("§aCuboids de la pièces")
                        .withLore("§7§oOuvre le menu affichant les cuboids de la pièce.",
                                "§7§oUn cuboid est un cube qui compose la pièce.")
                        .build()),
                Pair.of(3, new ItemBuilder(Material.GLASS)
                        .withName("§aFenêtres de la pièce")
                        .withLore("§7§oOuvre le menu affichant les fenêtres de la pièce.",
                                "§7§oUne fenêtre est un des points de spawn d'un zombie",
                                "§7§odans la pièce.")
                        .build()),
                Pair.of(5, new ItemBuilder(Material.OAK_SIGN)
                        .withName("§aNom de la pièce")
                        .withLore("§7§oPermet de définir le nom de la pièce.")
                        .build()),
                Pair.of(6, new ItemBuilder(Material.REDSTONE)
                        .withName("§aLe centre de la pièce")
                        .withLore("§7§oDéfinit le centre de la pièce là où vous êtes.",
                                "§7§oLe centre de la pièce est utilisé lorsqu'un joueur",
                                "§7§oarrive par un quelconque moyen à sortir de sa",
                                "§7§opièce actuelle pour aller dans un endroit non pris",
                                "§7§oen charge pour le jeu.",
                                "§7§oCela est prévu pour éviter les usebug en dehors de",
                                "§7§ola map.")
                        .build()),
                Pair.of(17, new ItemBuilder(Material.BARRIER)
                        .withName("§cFermer la création de la pièce")
                        .withLore("§c§lCela supprimera toutes les informations",
                                "§c§lrelatives à la pièce en création !")
                        .build())
        );
    }

    private Consumer<InventoryClickEvent> clickConsumer(Main main) {
        return event -> {
            final Player player = (Player) event.getWhoClicked();
            final ItemStack currentItem = event.getCurrentItem();

            event.setCancelled(true);

            if (currentItem == null || currentItem.getItemMeta() == null || currentItem.getItemMeta().displayName() == null)
                return;

            if (!(currentItem.getType() == Material.WOODEN_AXE || currentItem.getType() == Material.GLASS || currentItem.getType() == Material.OAK_SIGN ||
                    currentItem.getType() == Material.REDSTONE || currentItem.getType() == Material.BARRIER)) return;

            if (currentItem.getType() == Material.WOODEN_AXE) {
                isClosed = false;
                player.openInventory(new CuboidsRoomSurvivorInventory(main, this).survivorInventory().getInventory());
            }

            if (currentItem.getType() == Material.BARRIER) {
                isClosed = true;
                RoomsManager.CREATING_ROOM = null;

                player.closeInventory();
            }
        };
    }

    private Consumer<InventoryOpenEvent> openConsumer() {
        return event -> RoomsManager.CREATING_ROOM = Pair.of((Player) event.getPlayer(), new Room(-1, null, null, null, null));
    }

    private Consumer<InventoryCloseEvent> closeConsumer() {
        return event -> {
            if (!isClosed) return;

            final HumanEntity player = event.getPlayer();

            RoomsManager.CREATING_ROOM = null;

            player.sendMessage(Messages.CANCELLED_ROOM_CREATION);
        };
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void isClosed(boolean closed) {
        isClosed = closed;
    }
}
