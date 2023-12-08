package fr.twah2em.survivor.inventories.room.cuboids;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.Room;
import fr.twah2em.survivor.game.RoomsManager;
import fr.twah2em.survivor.inventories.ItemBuilder;
import fr.twah2em.survivor.inventories.SurvivorInventory;
import fr.twah2em.survivor.utils.Cuboid;
import fr.twah2em.survivor.utils.Messages;
import fr.twah2em.survivor.utils.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ShowCuboidInfoSurvivorInventory {
    private final Main main;

    private final SurvivorInventory.SurvivorInventoryBuilder survivorInventoryBuilder;
    private final String cuboid1;
    private final String cuboid2;

    public ShowCuboidInfoSurvivorInventory(Main main, String cuboid1, String cuboid2) {
        this.main = main;
        this.survivorInventoryBuilder = new SurvivorInventory.SurvivorInventoryBuilder("§aInformations du cuboid en cours de création", 1)
                .withItemsInSlots(items())
                .withClickConsumer(clickConsumer())
                .withOpenConsumer(openConsumer())
                .withGlassInEmptySlots();

        this.cuboid1 = cuboid1;
        this.cuboid2 = cuboid2;
    }

    public SurvivorInventory survivorInventory() {
        return survivorInventoryBuilder.buildToSurvivorInventory();
    }

    private List<Pair<Integer, ItemStack>> items() {
        return List.of(
                Pair.of(3, new ItemBuilder(Material.GREEN_DYE)
                        .withName("§aValider le cuboid")
                        .build()),
                Pair.of(5, new ItemBuilder(Material.OAK_SIGN)
                        .withName("§aVisualiser le cuboid")
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

            if (!(type == Material.GREEN_DYE || type == Material.OAK_SIGN)) return;

            final Cuboid cuboid = Cuboid.fromString2Loc(cuboid1, cuboid2);

            if (cuboid == null) {
                player.sendMessage(Messages.ERROR_WITH_CUBOID);

                return;
            }

            player.closeInventory();

            if (type == Material.GREEN_DYE) {
                final Room creatingRoom = RoomsManager.CREATING_ROOM.right();

                final List<Cuboid> cuboids = new ArrayList<>(Arrays.stream(creatingRoom.cuboids()).toList());
                cuboids.add(cuboid);

                creatingRoom.cuboids(cuboids.toArray(new Cuboid[0]));

                player.sendMessage(Messages.CUBOID_SUCCESSFULLY_CREATED);

                return;
            }

            main.gameInfos().cuboidParticlesTask(cuboid.showEdges(player, main));
        };
    }

    public Consumer<InventoryOpenEvent> openConsumer() {
        return event -> {
            final BukkitTask bukkitTask = main.gameInfos().cuboidParticlesTask();
            System.out.println("test");
            if (bukkitTask != null) {
                System.out.println(bukkitTask.getTaskId());
                bukkitTask.cancel();
                System.out.println(Bukkit.getScheduler().getPendingTasks().stream()
                        .filter(bukkitTask1 -> bukkitTask1.getOwner().getName().equals(Main.getPlugin(Main.class).getName()))
                        .map(BukkitTask::getTaskId).toList());
            }
        };
    }
}
