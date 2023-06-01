package fr.twah2em.survivor.game;

import fr.twah2em.survivor.game.player.SurvivorPlayer;
import org.apache.commons.collections4.list.SetUniqueList;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class GameInfos {
    private final SetUniqueList<SurvivorPlayer> players;
    private final Map<UUID, CompletableFuture<Boolean>> startCommandConfirmation;

    private final Round round;
    private GameState state;

    public GameInfos() {
        this.players = SetUniqueList.setUniqueList(new ArrayList<>());
        this.startCommandConfirmation = new HashMap<>();

        this.round = new Round(this);
        this.state = GameState.WAITING;
    }

    public SetUniqueList<SurvivorPlayer> players() {
        return players;
    }

    public Map<UUID, CompletableFuture<Boolean>> startCommandConfirmation() {
        return startCommandConfirmation;
    }

    public Round roundInfo() {
        return round;
    }

    public GameState state() {
        return state;
    }

    public void state(GameState state) {
        this.state = state;
    }

    public enum GameState {
        WAITING,
        STARTING,
        IN_GAME,
        ENDING
    }
}
