package fr.twah2em.survivor.game.player;

import fr.twah2em.survivor.game.Room;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class SurvivorPlayer {
    private final UUID uuid;
    private final String name;

    private int kills = 0;
    private int deaths = 0;
    private int points = 0;
    private PlayerState state = PlayerState.ALIVE;
    private Room room = null;

    public SurvivorPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public SurvivorPlayer(Player player) {
        this(player.getUniqueId(), player.getName());
    }

    public UUID uniqueId() {
        return uuid;
    }

    public String name() {
        return name;
    }

    public int kills() {
        return kills;
    }

    public void kills(int kills) {
        this.kills = kills;
    }

    public int deaths() {
        return deaths;
    }

    public void deaths(int deaths) {
        this.deaths = deaths;
    }

    public int points() {
        return points;
    }

    public void points(int points) {
        this.points = points;
    }

    public void addPoints(int points) {
        this.points(this.points + points);
    }

    public void removePoints(int points) {
        this.points(this.points - points);
    }

    public PlayerState state() {
        return state;
    }

    public void state(PlayerState playerState) {
        this.state = playerState;
    }

    public Room room() {
        return room;
    }

    public void room(Room room) {
        this.room = room;
    }

    public Player player() {
        return Bukkit.getPlayer(uuid);
    }

    public Location closestWindow() {
        Location closestWindow = null;

        for (Location window : room.windows()) {
            if (closestWindow == null) {
                closestWindow = window;
            } else {
                if (player().getLocation().distanceSquared(window) < player().getLocation().distanceSquared(closestWindow)) {
                    closestWindow = window;
                }
            }
        }

        return closestWindow;
    }

    public static boolean playerIsSurvivorPlayer(Player player, List<SurvivorPlayer> players) {
        return players
                .stream()
                .anyMatch(survivorPlayer -> survivorPlayer.uniqueId().equals(player.getUniqueId()));
    }

    public static SurvivorPlayer survivorPlayer(Player player, List<SurvivorPlayer> players) {
        return players
                .stream()
                .filter(survivorPlayer -> survivorPlayer.uniqueId().equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    public enum PlayerState {
        ALIVE,
        DEAD,
        DISCONNECTED_ALIVE,
        DISCONNECTED_DEAD
    }
}
