package fr.twah2em.survivor.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

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
        return "§ax: " + location.getX() + "§a, §ey: " + location.getY() + "§a, §6z: " + location.getZ();
    }
}
