package fr.twah2em.survivor.game;

import fr.twah2em.survivor.utils.Cuboid;
import fr.twah2em.survivor.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public record Room(String name, Cuboid[] cuboids, Location[] windows) {

    public static List<Room> fromConfig(FileConfiguration fileConfiguration) {
        final ConfigurationSection roomsConfig = fileConfiguration.getConfigurationSection("rooms");

        if (roomsConfig == null) {
            throw new NullPointerException("The roomsConfig section is null");
        }

        final List<Room> rooms = new ArrayList<>();

        final List<Cuboid> cuboids = new ArrayList<>();
        final List<Location> windows = new ArrayList<>();

        roomsConfig.getValues(false).forEach((key, value) -> {
            final AtomicReference<String> name = new AtomicReference<>("");

            final MemorySection roomMemorySection = (MemorySection) value;

            roomMemorySection.getValues(true).forEach((key1, value1) -> {
                switch (key1) {
                    case "name" -> name.set((String) value1);
                    case "cuboids" -> cuboids.addAll(((List<String>) value1).stream().map(Cuboid::fromString).toList());
                    case "windows" -> windows.addAll(((List<String>) value1).stream().map(LocationUtils::fromString).toList());
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

            rooms.add(new Room(name.get(), cuboids.toArray(new Cuboid[0]), windows.toArray(new Location[0])));
        });

        return rooms;
    }

    public static Room fromCuboid(GameInfos gameInfos, Cuboid cuboid) {
        return gameInfos.rooms()
                .stream()
                .filter(room -> Arrays.stream(room.cuboids).toList().contains(cuboid))
                .findFirst()
                .orElse(null);
    }
}
