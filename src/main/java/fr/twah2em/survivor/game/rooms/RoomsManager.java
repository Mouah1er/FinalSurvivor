package fr.twah2em.survivor.game.rooms;

import fr.twah2em.survivor.game.GameInfos;
import fr.twah2em.survivor.utils.Cuboid;
import fr.twah2em.survivor.utils.LocationUtils;
import fr.twah2em.survivor.utils.Pair;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class RoomsManager {
    // La Room en train d'être créée par un Player. Une seule pièce ne peut être créée à la fois.
    public static Pair<Player, Room> CREATING_ROOM = null;

    public static List<Room> fromConfig(FileConfiguration fileConfiguration) {
        final ConfigurationSection roomsConfig = fileConfiguration.getConfigurationSection("rooms");

        if (roomsConfig == null) {
            throw new NullPointerException("The roomsConfig section is null");
        }

        final List<Room> rooms = new ArrayList<>();
        final AtomicInteger roomId = new AtomicInteger();

        roomsConfig.getValues(false).forEach((key, value) -> {
            final AtomicReference<String> name = new AtomicReference<>("");

            final MemorySection roomMemorySection = (MemorySection) value;

            final List<Cuboid> cuboids = new ArrayList<>();
            final List<Location> windows = new ArrayList<>();
            final AtomicReference<Location> center = new AtomicReference<>();

            roomMemorySection.getValues(true).forEach((key1, value1) -> {
                switch (key1) {
                    case "name" -> name.set((String) value1);
                    case "cuboids" -> cuboids.addAll(((List<String>) value1).stream().map(Cuboid::fromString).toList());
                    case "windows" ->
                            windows.addAll(((List<String>) value1).stream().map(LocationUtils::locationFromString).toList());
                    case "center" -> center.set(LocationUtils.locationFromString((String) value1));
                }
            });

            if (name.get() == null || name.get().isEmpty()) {
                throw new NullPointerException("The name of a room is null");
            }

            if (cuboids.isEmpty()) {
                throw new NullPointerException("Room " + name + "'s cuboids are null");
            }

            if (windows.isEmpty()) {
                throw new NullPointerException("Room " + name + "'s windows are null");
            }

            if (center.get() == null) {
                throw new NullPointerException("Room " + name + "'s center is null");
            }

            rooms.add(new Room(roomId.incrementAndGet(), name.get(), cuboids.toArray(new Cuboid[0]), windows.toArray(new Location[0]), center.get()));
        });

        return rooms;
    }

    public static Room fromCuboid(GameInfos gameInfos, Cuboid cuboid) {
        return gameInfos.rooms()
                .stream()
                .filter(room -> Arrays.stream(room.cuboids()).toList().contains(cuboid))
                .findAny()
                .orElse(null);
    }

    public static Room fromLocation(GameInfos gameInfos, Location location) {
        return gameInfos.rooms()
                .stream()
                .filter(room -> Arrays.stream(room.cuboids()).toList()
                        .stream()
                        .anyMatch(cuboid -> cuboid.isIn(location)))
                .findAny()
                .orElse(null);
    }

    public static int roomsNumberFromConfig(FileConfiguration fileConfiguration) {
        final ConfigurationSection roomsConfig = fileConfiguration.getConfigurationSection("rooms");

        if (roomsConfig == null) {
            throw new NullPointerException("The roomsConfig section is null");
        }

        final AtomicInteger roomsNumber = new AtomicInteger();

        roomsConfig.getValues(false).forEach((key, value) -> roomsNumber.incrementAndGet());

        return roomsNumber.get();
    }
}
