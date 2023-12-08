package fr.twah2em.survivor.inventories;

import fr.twah2em.survivor.utils.Pair;
import fr.twah2em.survivor.utils.StreamUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class SurvivorInventory implements InventoryHolder {
    private final Inventory inventory;

    private final String name;
    private final int lines;

    private final List<ItemStack> items;
    private final boolean glassInEmptySlots;

    private final Consumer<InventoryOpenEvent> openConsumer;
    private final Consumer<InventoryClickEvent> clickConsumer;
    private final Consumer<InventoryCloseEvent> closeConsumer;

    private final boolean cancelCloseEvent;

    private SurvivorInventory(String name, int lines, List<ItemStack> items, boolean glassInEmptySlots, Consumer<InventoryOpenEvent> openConsumer, Consumer<InventoryClickEvent> clickConsumer,
                              Consumer<InventoryCloseEvent> closeConsumer, boolean cancelCloseEvent) {
        this.inventory = Bukkit.createInventory(this, lines * 9, Component.text(name));
        this.name = name;
        this.lines = lines;

        this.items = items;
        StreamUtils.forEachIndexed(items, inventory::setItem);
        this.glassInEmptySlots = glassInEmptySlots;

        this.openConsumer = openConsumer;
        this.clickConsumer = clickConsumer;
        this.closeConsumer = closeConsumer;

        this.cancelCloseEvent = cancelCloseEvent;
    }

    public String name() {
        return name;
    }

    public int lines() {
        return lines;
    }

    public List<ItemStack> items() {
        return items;
    }

    public Consumer<InventoryOpenEvent> openConsumer() {
        return openConsumer;
    }

    public Consumer<InventoryClickEvent> clickConsumer() {
        return clickConsumer;
    }

    public Consumer<InventoryCloseEvent> closeConsumer() {
        return closeConsumer;
    }

    public boolean glassInEmptySlots() {
        return glassInEmptySlots;
    }

    public boolean cancelCloseEvent() {
        return cancelCloseEvent;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public String toString() {
        return "SurvivorInventory{" +
                "\ninventory=" + inventory +
                ",\n name='" + name + '\'' +
                ",\n lines=" + lines +
                ",\n items=" + items +
                ",\n glassInEmptySlots=" + glassInEmptySlots +
                ",\n openConsumer=" + openConsumer +
                ",\n clickConsumer=" + clickConsumer +
                ",\n closeConsumer=" + closeConsumer +
                ",\n cancelCloseEvent=" + cancelCloseEvent +
                "\n}";
    }

    public static class SurvivorInventoryBuilder {
        private final String name;
        private final int lines;

        private final List<ItemStack> items;
        private boolean glassInEmptySlots;

        private Consumer<InventoryOpenEvent> openConsumer;
        private Consumer<InventoryClickEvent> clickConsumer;
        private Consumer<InventoryCloseEvent> closeConsumer;

        private boolean cancelCloseEvent;

        public SurvivorInventoryBuilder(String name, int lines) {
            this.name = name;
            this.lines = lines;

            this.items = new ArrayList<>(lines * 9);
            IntStream.range(0, lines * 9).forEach(i -> items.add(null));
            this.glassInEmptySlots = false;

            this.openConsumer = inventoryOpenEvent -> {
            };
            this.clickConsumer = inventoryClickEvent -> {
            };
            this.closeConsumer = inventoryCloseEvent -> {
            };

            this.cancelCloseEvent = false;
        }

        public SurvivorInventoryBuilder(SurvivorInventory survivorInventory) {
            this.name = survivorInventory.name();
            this.lines = survivorInventory.lines();

            this.items = survivorInventory.items();
            this.glassInEmptySlots = survivorInventory.glassInEmptySlots();

            this.openConsumer = survivorInventory.openConsumer();
            this.clickConsumer = survivorInventory.clickConsumer();
            this.closeConsumer = survivorInventory.closeConsumer();
        }

        public SurvivorInventoryBuilder withItem(ItemStack item) {
            return withItems(item);
        }

        public SurvivorInventoryBuilder withItemInSlot(int slot, ItemStack item) {
            if (slot >= 0 && slot < lines * 9) {
                this.items.set(slot, item);
            }

            return this;
        }

        public SurvivorInventoryBuilder withItems(ItemStack... items) {
            return withItems(Arrays.asList(items));
        }

        public SurvivorInventoryBuilder withItems(List<ItemStack> items) {
            return withItemsInSlots(items
                    .stream()
                    .map(item -> {
                        final int slot = items.indexOf(item);

                        return Pair.of(slot, item);
                    }).toList());
        }

        public SurvivorInventoryBuilder withItemsInSlots(List<? extends Pair<Integer, ItemStack>> items) {
            StreamUtils.forEachIndexed(items, (index, pair) -> {
                final Integer slot = pair.left();
                final ItemStack item = pair.right();

                if (item != null && slot >= 0 && slot < lines * 9) {
                    this.items.set(slot, item);
                }
            });

            return this;
        }

        public SurvivorInventoryBuilder withGlassInEmptySlots() {
            this.glassInEmptySlots = !glassInEmptySlots;

            return this;
        }

        public SurvivorInventoryBuilder withOpenConsumer(Consumer<InventoryOpenEvent> openConsumer) {
            this.openConsumer = openConsumer;

            return this;
        }

        public SurvivorInventoryBuilder withClickConsumer(Consumer<InventoryClickEvent> clickConsumer) {
            this.clickConsumer = clickConsumer;

            return this;
        }

        public SurvivorInventoryBuilder withCloseConsumer(Consumer<InventoryCloseEvent> closeConsumer) {
            this.closeConsumer = closeConsumer;

            return this;
        }

        public SurvivorInventoryBuilder cancelCloseEvent(boolean cancelCloseEvent) {
            this.cancelCloseEvent = cancelCloseEvent;

            return this;
        }

        public SurvivorInventoryBuilder cancelCloseEvent(BooleanSupplier supplier) {
            this.cancelCloseEvent = supplier.getAsBoolean();

            return this;
        }

        public SurvivorInventory buildToSurvivorInventory() {
            if (glassInEmptySlots) fillEmptySlotsWithGlassPane();

            return new SurvivorInventory(this.name, this.lines, this.items, this.glassInEmptySlots, this.openConsumer, this.clickConsumer, this.closeConsumer, this.cancelCloseEvent);
        }

        public Inventory buildToBukkitInventory() {
            return buildToSurvivorInventory().getInventory();
        }

        private void fillEmptySlotsWithGlassPane() {
            StreamUtils.fillEmptyElements(this.items, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                    .withName("§c")
                    .withFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .build()
            );
        }
    }
}
