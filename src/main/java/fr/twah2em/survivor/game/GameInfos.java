package fr.twah2em.survivor.game;

import fr.twah2em.survivor.Main;
import fr.twah2em.survivor.game.player.SurvivorPlayer;
import fr.twah2em.survivor.game.rooms.Room;
import fr.twah2em.survivor.game.rooms.RoomsManager;
import org.apache.commons.collections4.list.SetUniqueList;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class GameInfos {
    private final SetUniqueList<SurvivorPlayer> players;
    private final SetUniqueList<Player> spectators;
    private final Map<UUID, CompletableFuture<Boolean>> startCommandConfirmation;

    private final Round round;
    private final List<Room> rooms;
    private GameState state;

    private BukkitTask cuboidParticlesTask = null;

    public GameInfos(Main main, FileConfiguration config) {
        this.players = SetUniqueList.setUniqueList(new ArrayList<>());
        this.spectators = SetUniqueList.setUniqueList(new ArrayList<>());
        this.startCommandConfirmation = new HashMap<>();

        this.round = new Round(main, this);
        this.rooms = RoomsManager.fromConfig(config);
        this.state = GameState.WAITING;
    }

    public SetUniqueList<SurvivorPlayer> players() {
        return players;
    }

    public SetUniqueList<Player> spectators() {
        return spectators;
    }

    public Map<UUID, CompletableFuture<Boolean>> startCommandConfirmation() {
        return startCommandConfirmation;
    }

    public Round round() {
        return round;
    }

    public GameState state() {
        return state;
    }

    public void state(GameState state) {
        this.state = state;
    }

    public List<Room> rooms() {
        return rooms;
    }

    public BukkitTask cuboidParticlesTask() {
        return cuboidParticlesTask;
    }

    public void cuboidParticlesTask(BukkitTask cuboidParticlesTask) {
        this.cuboidParticlesTask = cuboidParticlesTask;
    }

    public enum GameState {
        WAITING,
        STARTING,
        PLAYING,
        ENDING
    }
}
