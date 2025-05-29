package fr.twah2em.survivor.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.Entity;
import org.bukkit.util.NumberConversions;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {
    public static Location locationFromString(String string) {
        final String[] location = string.split(", ");

        return location.length == 6 ?
                new Location(Bukkit.getWorld(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2]),
                        Double.parseDouble(location[3]), Float.parseFloat(location[4]), Float.parseFloat(location[5])) : location.length == 4 ?
                new Location(Bukkit.getWorld(location[0]),
                        Double.parseDouble(location[1]),
                        Double.parseDouble(location[2]),
                        Double.parseDouble(location[3])) :
                null;
    }

    public static String locationToString(Location location) {
        return location.getWorld().getName() + ", " + location.getX() + ", " + location.getY() + ", " + location.getZ() + ", " + location.getYaw() + ", " +
                location.getPitch();
    }

    public static String prettyLocationToString(Location location) {
        return "§ex: " + location.getX() + "§a, §6y: " + location.getY() + "§a, §cz: " + location.getZ();
    }

    public static List<Entity> entitiesByLocation(Location loc, float xRadius, float yRadius, float zRadius) {
        return new ArrayList<>(loc.getWorld().getNearbyEntities(loc, xRadius, yRadius, zRadius));
    }

    private static boolean isWithinBounds(Location center, Location entityLocation, double radiusX, double radiusY, double radiusZ) {
        double dx = Math.abs(center.getX() - entityLocation.getX());
        double dy = Math.abs(center.getY() - entityLocation.getY());
        double dz = Math.abs(center.getZ() - entityLocation.getZ());

        return dx <= radiusX && dy <= radiusY && dz <= radiusZ;
    }

    public static double highestBlockY(Location location) {
        Location highestBlockLocation = location.getWorld().getHighestBlockAt(location).getLocation();

        if (highestBlockLocation.getBlockY() < location.getBlockY()) {
            return highestBlockLocation.getBlockY() + 0.9 + offset(highestBlockLocation);
        }

        return location.getBlockY() - 0.1 + offset(location);
    }

    public static double offset(Location location) {
        if (location.getWorld().getBlockAt(location).getBlockData() instanceof Slab) {
            return 0.5;
        }

        return 0;
    }
}
