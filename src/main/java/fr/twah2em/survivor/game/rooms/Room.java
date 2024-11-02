package fr.twah2em.survivor.game.rooms;

import fr.twah2em.survivor.utils.Cuboid;
import fr.twah2em.survivor.utils.items.ItemUtils;
import fr.twah2em.survivor.utils.Messages;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Room {
    private int id;
    private String name;
    private Cuboid[] cuboids;
    private Location[] windows;
    private Location center;

    public Room(int id, String name, Cuboid[] cuboids, Location[] windows, Location center) {
        this.id = id;
        this.name = name;
        this.cuboids = cuboids;
        this.windows = windows;
        this.center = center;
    }

    public void addCuboid(Cuboid cuboid, Player creator) {
        final List<Cuboid> cuboids = new ArrayList<>(Arrays.stream(this.cuboids()).toList());
        cuboids.add(cuboid);

        this.cuboids(cuboids.toArray(new Cuboid[0]));

        creator.sendMessage(Messages.CUBOID_SUCCESSFULLY_CREATED);
        ItemUtils.removeWandFromPlayerInventory(creator);
    }

    public void removeCuboid(Cuboid cuboid, Player remover) {
        final List<Cuboid> cuboids = new ArrayList<>(Arrays.stream(this.cuboids()).toList());
        cuboids.remove(cuboid);

        this.cuboids(cuboids.toArray(new Cuboid[0]));

        remover.sendMessage(Messages.CUBOID_SUCCESSFULLY_DELETED);
    }

    public int id() {
        return id;
    }

    public void id(int id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void name(String name) {
        this.name = name;
    }

    public Cuboid[] cuboids() {
        return cuboids;
    }

    public void cuboids(Cuboid[] cuboids) {
        this.cuboids = cuboids;
    }

    public Location[] windows() {
        return windows;
    }

    public void windows(Location[] windows) {
        this.windows = windows;
    }

    public Location center() {
        return center;
    }

    public void center(Location center) {
        this.center = center;
    }
}
