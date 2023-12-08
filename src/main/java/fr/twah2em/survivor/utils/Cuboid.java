package fr.twah2em.survivor.utils;

import com.destroystokyo.paper.ParticleBuilder;
import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.GameInfos;
import fr.twah2em.survivor.inventories.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Cuboid {

    private final int xMin;
    private final int xMax;
    private final int yMin;
    private final int yMax;
    private final int zMin;
    private final int zMax;
    private final double xMinCentered;
    private final double xMaxCentered;
    private final double yMinCentered;
    private final double yMaxCentered;
    private final double zMinCentered;
    private final double zMaxCentered;
    private final World world;

    public Cuboid(final Location point1, final Location point2) {
        this.xMin = Math.min(point1.getBlockX(), point2.getBlockX());
        this.xMax = Math.max(point1.getBlockX(), point2.getBlockX());
        this.yMin = Math.min(point1.getBlockY(), point2.getBlockY());
        this.yMax = Math.max(point1.getBlockY(), point2.getBlockY());
        this.zMin = Math.min(point1.getBlockZ(), point2.getBlockZ());
        this.zMax = Math.max(point1.getBlockZ(), point2.getBlockZ());
        this.world = point1.getWorld();
        this.xMinCentered = this.xMin + 0.5;
        this.xMaxCentered = this.xMax + 0.5;
        this.yMinCentered = this.yMin + 0.5;
        this.yMaxCentered = this.yMax + 0.5;
        this.zMinCentered = this.zMin + 0.5;
        this.zMaxCentered = this.zMax + 0.5;
    }

    public static Cuboid fromString(String cuboid) {
        final String[] locations = cuboid.split(", ");

        final World world = Bukkit.getWorld(locations[0]);
        final double xMin = Double.parseDouble(locations[1]);
        final double yMin = Double.parseDouble(locations[2]);
        final double zMin = Double.parseDouble(locations[3]);
        final double xMax = Double.parseDouble(locations[4]);
        final double yMax = Double.parseDouble(locations[5]);
        final double zMax = Double.parseDouble(locations[6]);

        final Location minLocation = new Location(world, xMin, yMin, zMin);
        final Location maxLocation = new Location(world, xMax, yMax, zMax);

        return new Cuboid(minLocation, maxLocation);
    }

    public static Cuboid fromString2Loc(String cuboid1, String cuboid2) {
        final Location minLocation = LocationUtils.locationFromString(cuboid1);
        final Location maxLocation = LocationUtils.locationFromString(cuboid2);

        if (minLocation == null || maxLocation == null) return null;

        return new Cuboid(minLocation, maxLocation);
    }

    public static Cuboid fromLocation(GameInfos gameInfos, Location location) {
        final AtomicReference<Cuboid> cuboid = new AtomicReference<>();

        gameInfos.rooms().forEach(room -> Arrays.stream(room.cuboids()).forEach(cuboid1 -> {
            if (cuboid1.isIn(location)) {
                cuboid.set(cuboid1);
            }
        }));

        return cuboid.get();
    }

    public Iterator<Block> blockList() {
        final List<Block> bL = new ArrayList<>(this.getTotalBlockSize());
        for (int x = this.xMin; x <= this.xMax; ++x) {
            for (int y = this.yMin; y <= this.yMax; ++y) {
                for (int z = this.zMin; z <= this.zMax; ++z) {
                    Block b = this.world.getBlockAt(x, y, z);
                    bL.add(b);
                }
            }
        }
        return bL.iterator();
    }

    public BukkitTask showEdges(Player player, Main main) {
        final AtomicReference<BukkitTask> bukkitTask = new AtomicReference<>();

        final Color color = Color.fromRGB((int) (Math.random() * 0x1000000)); // random color
        this.edgesList().forEach(location -> bukkitTask.set(Bukkit.getScheduler().runTaskTimer(main, () -> new ParticleBuilder(Particle.REDSTONE)
                        .color(color)
                        .location(location)
                        .receivers(player)
                        .spawn(),
                0, 5))
        );

        System.out.println(bukkitTask.get().getTaskId());

        Bukkit.getScheduler().runTaskLater(main, () -> Bukkit.getScheduler().cancelTask(bukkitTask.get().getTaskId()), 40);

        return bukkitTask.get();
    }

    public List<Location> edgesList() {
        final List<Location> edgeList = new ArrayList<>();

        for (double x = xMin; x <= xMax + 1; x++) {
            for (double y = yMin; y <= yMax + 1; y++) {
                for (double z = zMin; z <= zMax + 1; z++) {
                    boolean edge = ((int) x == xMin || (int) x == xMax + 1) &&
                            ((int) y == yMin || (int) y == yMax + 1);
                    if (((int) z == zMin || (int) z == zMax + 1) &&
                            ((int) y == yMin || (int) y == yMax + 1)) edge = true;
                    if (((int) x == xMin || (int) x == xMax + 1) &&
                            ((int) z == zMin || (int) z == zMax + 1)) edge = true;

                    if (edge) {
                        edgeList.add(new Location(this.world, x, y, z));
                    }
                }
            }
        }

        return edgeList;
    }

    public static void emptyCuboidInItem(ItemBuilder itemBuilder, int cuboidNumber) {
        if (cuboidNumber != 1 && cuboidNumber != 2) return;

        itemBuilder
                .withPersistentData("survivor", "cuboid_" + cuboidNumber, "", PersistentDataType.STRING)
                .build();
    }

    public static void writeCuboidInItem(ItemBuilder itemBuilder, int cuboidNumber, Location location) {
        if (cuboidNumber != 1 && cuboidNumber != 2) return;

        itemBuilder.withPersistentData("survivor", "cuboid_" + cuboidNumber, LocationUtils.locationToString(location), PersistentDataType.STRING)
                .build();
    }

    public Location getCenter() {
        return new Location(this.world, (double) (this.xMax - this.xMin) / 2 + this.xMin, (double) (this.yMax - this.yMin) / 2 + this.yMin, (double) (this.zMax - this.zMin) / 2 + this.zMin);
    }

    public double getDistance() {
        return this.getPoint1().distance(this.getPoint2());
    }

    public double getDistanceSquared() {
        return this.getPoint1().distanceSquared(this.getPoint2());
    }

    public int getHeight() {
        return this.yMax - this.yMin + 1;
    }

    public Location getPoint1() {
        return new Location(this.world, this.xMin, this.yMin, this.zMin);
    }

    public Location getPoint2() {
        return new Location(this.world, this.xMax, this.yMax, this.zMax);
    }

    public Location getRandomLocation() {
        final Random rand = new Random();
        final int x = rand.nextInt(Math.abs(this.xMax - this.xMin) + 1) + this.xMin;
        final int y = rand.nextInt(Math.abs(this.yMax - this.yMin) + 1) + this.yMin;
        final int z = rand.nextInt(Math.abs(this.zMax - this.zMin) + 1) + this.zMin;
        return new Location(this.world, x, y, z);
    }

    public int getTotalBlockSize() {
        return this.getHeight() * this.getXWidth() * this.getZWidth();
    }

    public int getXWidth() {
        return this.xMax - this.xMin + 1;
    }

    public int getZWidth() {
        return this.zMax - this.zMin + 1;
    }

    public boolean isIn(final Location loc) {
        return loc.getWorld() == this.world && loc.getBlockX() >= this.xMin && loc.getBlockX() <= this.xMax && loc.getBlockY() >= this.yMin && loc.getBlockY() <= this.yMax && loc
                .getBlockZ() >= this.zMin && loc.getBlockZ() <= this.zMax;
    }

    public boolean isIn(final Player player) {
        return this.isIn(player.getLocation());
    }

    public boolean isInWithMarge(final Location loc, final double marge) {
        return loc.getWorld() == this.world && loc.getX() >= this.xMinCentered - marge && loc.getX() <= this.xMaxCentered + marge && loc.getY() >= this.yMinCentered - marge && loc
                .getY() <= this.yMaxCentered + marge && loc.getZ() >= this.zMinCentered - marge && loc.getZ() <= this.zMaxCentered + marge;
    }

    public boolean compare(Cuboid other) {
        return this.xMax == other.xMax && this.xMin == other.xMin && this.yMax == other.yMax && this.yMin == other.yMin && this.zMax == other.zMax && this.zMin == other.zMin && this.world == other.world;
    }

    @Override
    public String toString() {
        return "Cuboid{" +
                "xMin=" + xMin +
                ", xMax=" + xMax +
                ", yMin=" + yMin +
                ", yMax=" + yMax +
                ", zMin=" + zMin +
                ", zMax=" + zMax +
                ", xMinCentered=" + xMinCentered +
                ", xMaxCentered=" + xMaxCentered +
                ", yMinCentered=" + yMinCentered +
                ", yMaxCentered=" + yMaxCentered +
                ", zMinCentered=" + zMinCentered +
                ", zMaxCentered=" + zMaxCentered +
                ", world=" + world +
                '}';
    }
}
