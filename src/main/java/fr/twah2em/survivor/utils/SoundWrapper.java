package fr.twah2em.survivor.utils;

import org.bukkit.Location;
import org.bukkit.Sound;

public record SoundWrapper(Sound sound, float volume, float pitch) {

    public void play(Location location) {
        location.getWorld().playSound(location, sound, volume, pitch);
    }
}
