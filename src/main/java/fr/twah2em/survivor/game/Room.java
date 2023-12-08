package fr.twah2em.survivor.game;

import fr.twah2em.survivor.utils.Cuboid;
import org.bukkit.Location;

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
