package fr.twah2em.survivor.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.annotation.Nullable;

public class LocationUtils {
    public static Location fromString(String string) {
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
}
