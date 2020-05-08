package fr.bibouh.survivor;

import fr.bibouh.survivor.listeners.GPLayerListener;
import fr.bibouh.survivor.listeners.packAPunchBlock;
import fr.bibouh.survivor.listeners.systemeVague;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    private List<Player> players = new ArrayList<>();
    private GState state;

    public void onEnable() {

        setState(GState.WAITING);

        System.out.print("Le survivor est activé");

        getCommand("survivor").setExecutor(new helpSurvivor());
        getCommand("packapunch").setExecutor(new packAPunch());
        getServer().getPluginManager().registerEvents(new packAPunchBlock(), this);
        getServer().getPluginManager().registerEvents(new systemeVague(), this);
        getServer().getPluginManager().registerEvents(new GPLayerListener(this), this);
        getCommand("survivorstop").setExecutor(new SurvivorStop(this));

    }

    public void setState(GState state) {
        this.state = state;
    }

    public boolean isState(GState state) {
        return this.state == state;
    }

    public List<Player> getPlayers() {
        return players;
    }

}
