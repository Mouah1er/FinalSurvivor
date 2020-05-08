package fr.bibouh.survivor.tasks;

import fr.bibouh.survivor.GState;
import fr.bibouh.survivor.Main;
import fr.bibouh.survivor.listeners.GPLayerListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class GAutaStartTask extends BukkitRunnable {

    private Main main;
    private int timer = 20;
    private GPLayerListener gpLayerListener;


    public GAutaStartTask(Main main) {
        this.main = main;
    }

    public GAutaStartTask(GPLayerListener gpLayerListener){
        this.gpLayerListener = gpLayerListener;
    }


    @Override
    public void run() {

        for(Player player : Bukkit.getOnlinePlayers()) {

            player.setLevel(timer);

        }

        if(timer == 20 ||timer == 10 || timer == 5 || timer == 4)  Bukkit.broadcastMessage("§6Le jeu va commencer dans §a"+timer +"§6s.");

        if(timer == 3) Bukkit.broadcastMessage("§6Le jeu va commencer dans §e" +timer +"§6s.");

        if(timer == 2) Bukkit.broadcastMessage("§6Le jeu va commencer dans §c"+timer+"§6s.");

        if(timer == 1) Bukkit.broadcastMessage("§6Le jeu va commencer dans §4"+timer+"§6s.");

        if(timer == 0) {



                Bukkit.broadcastMessage("§6On commence ! Bonne chance !");
                main.setState(GState.PLAYING);

                cancel();

                for(Player player : Bukkit.getOnlinePlayers()){

                    World world = Bukkit.getWorld("world");
                    Location spawn = new Location (world, -287, 65, 80, 0.3f, 0.8f);
                    player.getInventory().clear();
                    player.teleport(spawn);
                    player.setGameMode(GameMode.ADVENTURE);
                    ItemStack itemStack = GPLayerListener.map.get(player);
                        if(itemStack ==  GPLayerListener.medecin) {
                            ...
                        }
                        else if(itemStack ==  GPLayerListener.grenadier) {
                            ...
                        }
                        else if() {
                            ...
                        }
                    }
                    if(GPLayerListener.map.get()) {



                    }

                }

                GGameCycle cycle = new GGameCycle(main);
                cycle.runTaskTimer(main, 0, 20);

                cancel();

        }

        timer--;

    }
}
